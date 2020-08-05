package com.dmarcini.app.controllers;

import com.dmarcini.app.workers.WorkersService;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Duration;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class WebCrawlerController {
    private final static int DEFAULT_TIME_LIMIT = 5;
    private final static int DEFAULT_MAX_DEPTH = Integer.MAX_VALUE;

    @FXML
    private TextField urlTextField;
    @FXML
    private Button runButton;
    @FXML
    private TextField pathTextField;
    @FXML
    private Button saveButton;
    @FXML
    private TextField workersTextField;
    @FXML
    private TextField maximumDepthTextField;
    @FXML
    private TextField timeLimitTextField;
    @FXML
    private Label elapsedTimeLabel;
    @FXML
    private Label parsedPageLabel;
    @FXML
    private CheckBox maximumDepthCheckBox;
    @FXML
    private CheckBox timeLimitCheckBox;

    private int startButtonClickCounter;
    private int timeLimit;
    private int second;
    private final Timeline timeline;

    private WorkersService workersService;

    public WebCrawlerController() {
        this.startButtonClickCounter = 1;
        this.second = 0;
        this.timeline = new Timeline(new KeyFrame(Duration.seconds(1), actionEvent -> {
            second++;
            elapsedTimeLabel.setText(String.format("%02d:%02d:%02d", second / 3600, (second % 3600) / 60, second % 60));
        }));
    }

    @FXML
    private void initialize() {
        runButton.setOnAction(actionEvent -> runCrawler());
        saveButton.setOnAction(actionEvent -> saveToFile());

        setDisableTextFieldOnCheckBoxAction(maximumDepthCheckBox, maximumDepthTextField);
        setDisableTextFieldOnCheckBoxAction(timeLimitCheckBox, timeLimitTextField);

        maximumDepthTextField.setDisable(true);
        timeLimitTextField.setDisable(true);

        setTextFieldNumericOnly(workersTextField);
        setTextFieldNumericOnly(maximumDepthTextField);
        setTextFieldNumericOnly(timeLimitTextField);

        Platform.runLater(() -> urlTextField.requestFocus());
    }

    @FXML private void runCrawler() {
        if (workersTextField.getText().isEmpty()) {
            showNoWorkersNumSpecifiedAlert();
            return;
        }

        if (startButtonClickCounter % 2 == 1) {
            startCrawler();
        } else {
            stopCrawler();
        }

        ++startButtonClickCounter;
    }

    @FXML
    private void saveToFile() {
        Path path = Paths.get(pathTextField.getText());

        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path)) {
            for (var crawledPage : workersService.getCrawledPages()) {
                if (crawledPage.getTitle() != null) {
                    bufferedWriter.write(crawledPage.getTitle() + ":\n");
                    bufferedWriter.write(String.join("\n", crawledPage.getLinks()) + "\n");
                }
            }

            showSaveToFileSucceedAlert();
        } catch (IOException e) {
            showIncorrectPathAlert();
        }
    }

    private void startCrawler() {
        resetTimer();
        startTimer();

        runButton.setText("Stop");
        parsedPageLabel.setText("0");

        int workers = Integer.parseInt(workersTextField.getText());
        int maxDepth = maximumDepthTextField.getText().isEmpty() ? DEFAULT_MAX_DEPTH
                                                                 : Integer.parseInt(maximumDepthTextField.getText());

        workersService = new WorkersService(workers, maxDepth);

        workersService.execute(urlTextField.getText());

        Timeline runTimeline = new Timeline(new KeyFrame(Duration.millis(10), event -> {
            parsedPageLabel.setText(String.valueOf(workersService.getCrawledPageNum()));

            if (second >= timeLimit) {
                runButton.setText("Start");

                workersService.shutdown();
            }
        }));

        runTimeline.setCycleCount(Timeline.INDEFINITE);
        runTimeline.play();
    }

    public void stopCrawler() {
        stopTimer();

        runButton.setText("Start");

        workersService.shutdown();
    }

    private void showIncorrectPathAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);

        alert.setHeaderText("Incorrect path.");
        alert.setContentText("Incorrect path to save the file! Please type again.");

        alert.showAndWait();
    }

    private void showSaveToFileSucceedAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setHeaderText("File saved.");
        alert.setContentText("Save to file succeed!");

        alert.showAndWait();
    }

    private void showNoWorkersNumSpecifiedAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);

        alert.setHeaderText("No workers number specified.");
        alert.setContentText("The number of workers was not specified!\nPlease enter the number of workers.");

        alert.showAndWait();
    }

    private void startTimer() {
        timeLimit = timeLimitTextField.getText().isEmpty() ? DEFAULT_TIME_LIMIT
                                                           : Integer.parseInt(timeLimitTextField.getText());

        timeline.setCycleCount(timeLimit);
        timeline.play();
    }

    private void stopTimer() {
        timeline.stop();
    }

    private void resetTimer() {
        elapsedTimeLabel.setText("00:00:00");
        second = 0;
    }

    private void setDisableTextFieldOnCheckBoxAction(CheckBox checkBox, TextField textField) {
        checkBox.setOnAction(actionEvent -> textField.setDisable(!textField.isDisabled()));
    }

    private void setTextFieldNumericOnly(TextField textField) {
        textField.textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                textField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }
}
