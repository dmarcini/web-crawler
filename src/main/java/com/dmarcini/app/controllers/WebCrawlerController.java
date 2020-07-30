package com.dmarcini.app.controllers;

import com.dmarcini.app.htmlparser.HTMLParser;
import com.dmarcini.app.htmlparser.HTMLTag;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class WebCrawlerController {
    @FXML
    private TextField URLTextField;
    @FXML
    private Button runButton;
    @FXML
    private TextArea HTMLTextArea;
    @FXML
    private Label titleLabel;

    private String titleLabelInitText;

    public WebCrawlerController() { }

    @FXML
    private void initialize() {
        runButton.setOnAction(actionEvent -> getHTML());

        HTMLTextArea.setEditable(false);

        Platform.runLater(() -> URLTextField.requestFocus());

        titleLabelInitText = titleLabel.getText();
    }

    @FXML
    private void getHTML() {
        String HTMLContent = readHTML(URLTextField.getText());

        titleLabel.setText(titleLabelInitText + HTMLParser.getTagContent(HTMLContent, HTMLTag.TITLE));
        HTMLTextArea.setText(HTMLContent);
    }

    private String readHTML(String URLAddress) {
        final StringBuilder stringBuilder = new StringBuilder();

        try (final InputStream inputStream = new URL(URLAddress).openStream();
             final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,
                                                                                            StandardCharsets.UTF_8))) {
             String nextLine;

             while ((nextLine = bufferedReader.readLine()) != null) {
                 stringBuilder.append(nextLine).append(System.lineSeparator());
             }
        } catch (IOException e) {
            showIncorrectURLAlert();
        }

        return stringBuilder.toString();
    }

    private void showIncorrectURLAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);

        alert.setHeaderText("Incorrect URL");
        alert.setContentText("Incorrect URL address. Please type again!");

        alert.showAndWait();
    }

}
