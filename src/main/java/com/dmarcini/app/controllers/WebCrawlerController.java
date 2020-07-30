package com.dmarcini.app.controllers;

import com.dmarcini.app.htmlparser.HTMLParser;
import com.dmarcini.app.htmlparser.HTMLProperty;
import com.dmarcini.app.htmlparser.HTMLTag;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
    private Label titleLabel;
    @FXML
    private TableView<URLTitleProperty> URLTableView;
    @FXML
    private TableColumn<URLTitleProperty, String> URLTableColumn;
    @FXML
    private TableColumn<URLTitleProperty, String> titleTableColumn;

    private String titleLabelInitText;

    public WebCrawlerController() { }

    @FXML
    private void initialize() {
        runButton.setOnAction(actionEvent -> getHTML());

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

    private String readHTML(String URLAddress) {
        final StringBuilder stringBuilder = new StringBuilder();

        try {
            final var urlConnection = new URL(URLAddress).openConnection();

            if (urlConnection.getContentType().equals("text/html")) {
                final var bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(),
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
        alert.setContentText("Incorrect URL address. Please type again!");

        URLTextField.clear();

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
