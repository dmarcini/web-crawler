package com.dmarcini.app.controllers;

import com.dmarcini.app.htmlparser.HTMLParser;
import com.dmarcini.app.htmlparser.HTMLProperty;
import com.dmarcini.app.htmlparser.HTMLTag;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class WebCrawlerController {
    @FXML
    private TextField URLTextField;
    @FXML
    private Button runButton;
    @FXML
    private Label titleLabel;
    @FXML
    private TableView<URLTitleProperty> URLTableView;
    @FXML
    private TableColumn<URLTitleProperty, String> URLTableColumn;
    @FXML
    private TableColumn<URLTitleProperty, String> titleTableColumn;
    @FXML
    private TextField pathTextField;
    @FXML
    private Button saveButton;

    private String titleLabelInitText;

    public WebCrawlerController() { }

    @FXML
    private void initialize() {
        runButton.setOnAction(actionEvent -> getHTML());
        saveButton.setOnAction(actionEvent -> saveToFile());

        URLTableColumn.setCellValueFactory(cellData -> cellData.getValue().getURLProperty());
        titleTableColumn.setCellValueFactory(cellData -> cellData.getValue().getTitleProperty());

        Platform.runLater(() -> URLTextField.requestFocus());

        titleLabelInitText = titleLabel.getText();
    }

    @FXML
    private void getHTML() {
        String HTMLContents = readHTML(URLTextField.getText());

        titleLabel.setText(titleLabelInitText + HTMLParser.getTagContents(HTMLContents, HTMLTag.TITLE));

        collectLinksToOtherPages(HTMLContents);
    }

    @FXML
    private void saveToFile() {
        Path path = Paths.get(pathTextField.getText());

        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path)) {
            for (var utp : URLTableView.getItems()) {
                String row = utp.getURL() + ";" + utp.getTitle() + "\n";

                bufferedWriter.write(row);
            }

            showSaveToFileSucceedAlert();
        } catch (IOException e) {
            showIncorrectPathAlert();
        }
    }

    private String readHTML(String URLAddress) {
        final StringBuilder stringBuilder = new StringBuilder();

        try {
            final var URLConnection = new URL(URLAddress).openConnection();

            if (URLConnection.getContentType().equals("text/html")) {
                final var bufferedReader = new BufferedReader(new InputStreamReader(URLConnection.getInputStream(),
                                                                                    StandardCharsets.UTF_8));

                String nextLine;

                while ((nextLine = bufferedReader.readLine()) != null) {
                    stringBuilder.append(nextLine).append(System.lineSeparator());
                }
            } else {
                showIncorrectURLAlert();
            }
        } catch (IOException e) {
            showIncorrectURLAlert();
        }

        return stringBuilder.toString();
    }

    private void collectLinksToOtherPages(String URLAddress) {
        for (var line : URLAddress.split("\n")) {
            String href = HTMLParser.getPropertyValue(line, HTMLProperty.HREF);
            String title = HTMLParser.getPropertyValue(line, HTMLProperty.TITLE);

            if (!(href.isEmpty() || title.isEmpty())) {
                URLTableView.getItems().add(new URLTitleProperty(href, title));
            }
        }
    }

    private void showIncorrectURLAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);

        alert.setHeaderText("Incorrect URL");
        alert.setContentText("Incorrect URL address! Please type again.");

        URLTextField.clear();

        alert.showAndWait();
    }

    private void showIncorrectPathAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);

        alert.setHeaderText("Incorrect path");
        alert.setContentText("Incorrect path to save the file! Please type again.");

        alert.showAndWait();
    }

    private void showSaveToFileSucceedAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setHeaderText("File saved");
        alert.setContentText("Save to file succeed!");

        alert.showAndWait();
    }

    static class URLTitleProperty {
        private final SimpleStringProperty URL = new SimpleStringProperty("");;
        private final SimpleStringProperty title = new SimpleStringProperty("");;

        URLTitleProperty(String URL, String title) {
            setURL(URL);
            setTitle(title);
        }

        public String getURL() {
            return URL.get();
        }

        public void setURL(String URL) {
            this.URL.set(URL);
        }

        public StringProperty getURLProperty() {
            return URL;
        }

        public String getTitle() {
            return title.get();
        }

        public void setTitle(String title) {
            this.title.set(title);
        }

        public StringProperty getTitleProperty() {
            return title;
        }
    }
}
