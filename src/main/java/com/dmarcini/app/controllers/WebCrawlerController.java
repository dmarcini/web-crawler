package com.dmarcini.app.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

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

    public WebCrawlerController() { }

    @FXML
    private void initialize() {
        runButton.setOnAction(actionEvent -> getHTML());

        HTMLTextArea.setEditable(false);

        Platform.runLater(() -> URLTextField.requestFocus());
    }

    @FXML
    private void getHTML() {
        HTMLTextArea.setText(readHTML(URLTextField.getText()));
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
