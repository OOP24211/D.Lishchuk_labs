package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sun.nio.sctp.SctpChannel;
import javafx.application.Platform;
import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.handshake.ServerHandshake;
import com.fasterxml.jackson.databind.ObjectMapper;


import javafx.scene.control.ListView;

import java.awt.dnd.InvalidDnDOperationException;
import java.net.URI;

public class ChatClient extends WebSocketClient {
    private ListView<String> messageList;
    private ListView<String> usersList;
    final private ObjectMapper mapper = new ObjectMapper();
    final private String login;

    public ChatClient(URI serverUri, ListView<String> messageList, ListView<String> usersList,  String login) {
        super(serverUri);
        this.messageList = messageList;
        this.usersList = usersList;
        this.login = login;
    }

    @Override
    public void onOpen(ServerHandshake handshake) {
        ChatMessage loginMessage = new ChatMessage();
        loginMessage.user = login;
        loginMessage.text = "";
        loginMessage.type = "login";
        String json = null;
        try {
            json = mapper.writeValueAsString(loginMessage);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
        send(json);
    }

    @Override
    public void onMessage(String message) {
        try {
            ChatMessage msg = mapper.readValue(message, ChatMessage.class);
            switch (msg.type) {
                case "message":
                    Platform.runLater(() -> {
                        messageList.getItems().add("[" + msg.user + "]: " + msg.text);
                    });
                    break;

                case "join":
                    Platform.runLater(() -> {
                        usersList.getItems().add(msg.user);
                    });
                    break;
                case "leave":
                    Platform.runLater(() -> {
                        usersList.getItems().remove(msg.user);
                    });
                    break;
                case "userList":
                    Platform.runLater(() -> {
                        usersList.getItems().addAll(msg.userList);
                    });
                    break;
                default:
                    throw new InvalidDnDOperationException("Invalid message type");
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onError(Exception ex) {
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
    }

}
