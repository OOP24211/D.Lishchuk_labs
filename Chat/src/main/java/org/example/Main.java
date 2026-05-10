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
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import java.net.URI;

public class Main extends  Application {
    private ListView<String> messageList = new ListView<>();
    private ListView<String> usersList = new ListView<>();
    private ChatClient client;
    private Scene loginSceneCreator(Stage stage) {
        BorderPane layoutForLoginPage = new BorderPane();
        TextField inputLoginField = new TextField();
        inputLoginField.setPromptText("Логин");
        Button signInBottom = new Button("Войти");

        VBox loginVBox = new VBox(10,inputLoginField,signInBottom);
        layoutForLoginPage.setCenter(loginVBox);
        Scene loginScene = new Scene(layoutForLoginPage, 400, 300);

        signInBottom.setOnAction(e -> {
            String login = inputLoginField.getText();
            if (login.isEmpty()) {
                Text emptyLoginFieldErrorMessage = new Text("Empty Login Field");
                emptyLoginFieldErrorMessage.setFont(Font.font("Palatino Linotype", FontWeight.BOLD, 24));
                emptyLoginFieldErrorMessage.setFill(Color.RED);
                loginVBox.getChildren().add(emptyLoginFieldErrorMessage);
            }
            else {
                client = new ChatClient(
                        URI.create("ws://localhost:8080"),
                        messageList,
                        usersList,
                        login
                );
                client.connect();
                stage.setScene(roomListSceneCreator(client, stage, login));
            }
        });
        stage.setScene(loginScene);
        stage.show();
        return loginScene;
    }
    private Scene roomListSceneCreator(ChatClient client, Stage stage, String login) {
        BorderPane layoutForRoomListPage = new BorderPane();
            TextField inputFieldIdRoom = new TextField();
        inputFieldIdRoom.setPromptText("ID комнаты");
        Button signInBottom = new Button("Подключиться");
        HBox joinRoomHbox = new HBox(10,inputFieldIdRoom,signInBottom);
        layoutForRoomListPage.setCenter(joinRoomHbox);
        Scene roomListScene = new Scene(layoutForRoomListPage, 400, 300);
        ObjectMapper mapper = new ObjectMapper();
        signInBottom.setOnAction(e -> {
            String idRoom = inputFieldIdRoom.getText();
            inputFieldIdRoom.clear();
            if (idRoom.isEmpty()) {
                Text emptyLoginFieldErrorMessage = new Text("Empty Field Id Room");
                emptyLoginFieldErrorMessage.setFont(Font.font("Palatino Linotype", FontWeight.BOLD, 24));
                emptyLoginFieldErrorMessage.setFill(Color.RED);
                layoutForRoomListPage.setBottom(emptyLoginFieldErrorMessage);
            }
            else {
                stage.setScene(chatListSceneCreator(client, stage, login));
                ChatMessage roomIdMsg =  new ChatMessage();
                roomIdMsg.user = login;
                roomIdMsg.type = "roomID";
                roomIdMsg.text = idRoom;
                String json = null;
                try {
                    json = mapper.writeValueAsString(roomIdMsg);
                } catch (JsonProcessingException ex) {
                    throw new RuntimeException(ex);
                }
                client.send(json);
               //как=то обрабатывать подключение
            }
        });
        stage.setScene(roomListScene);
        stage.show();
        return roomListScene;

    }
    private Scene chatListSceneCreator(ChatClient client, Stage stage, String login) {
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
                msg.user = login;
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

        Scene chatListScene = new Scene(layoutForChat, 400, 300);
        return chatListScene;

    }
    public  void stop(){
        if(client != null){
            client.close();
        }
    }
    @Override
    public void start(Stage stage) {
        stage.setTitle("Чат");
        loginSceneCreator(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}