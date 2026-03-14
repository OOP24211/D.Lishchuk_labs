package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import java.net.URI;

public class Main extends  Application {
    @Override
    public void start(Stage stage) {
        ListView<String> messageList = new ListView<>();
        ListView<String> usersList = new ListView<>();

        ChatClient client = new ChatClient(
                URI.create("ws://localhost:8080"),
                messageList,
                usersList
        );
        client.connect();

        TextField inputField = new TextField();
        HBox.setHgrow(inputField, Priority.ALWAYS);
        inputField.setPromptText("Введите сообщение...");

        ObjectMapper mapper = new ObjectMapper();
        Button sendButton = new Button("Отправить");
        sendButton.setOnAction(e -> {
            String text = inputField.getText();
            if (!text.isEmpty()) {
                messageList.getItems().add("Я: " + text);
                ChatMessage msg = new ChatMessage();
                msg.user = "Danya";
                msg.text = text;
                msg.type = "message";
                String json = null;
                try {
                    json = mapper.writeValueAsString(msg);
                } catch (JsonProcessingException ex) {
                    throw new RuntimeException(ex);
                }
                client.send(json);
                inputField.clear();
            }
        });

        BorderPane layoutForChat = new BorderPane();
        HBox bottomPanel = new HBox(10, inputField, sendButton);

        layoutForChat.setCenter(messageList);
        layoutForChat.setBottom(bottomPanel);
        layoutForChat.setRight(usersList);

        Scene scene = new Scene(layoutForChat, 400, 300);

        stage.setTitle("Чат");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}