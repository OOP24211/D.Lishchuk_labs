package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import javafx.application.Platform;
import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.handshake.ServerHandshake;
import com.fasterxml.jackson.databind.ObjectMapper;


import javafx.scene.control.ListView;
import java.net.URI;

public class ChatClient extends WebSocketClient {
    private ListView<String> messageList;
    final private ObjectMapper mapper = new ObjectMapper();

    public ChatClient(URI serverUri, ListView<String> messageList) {
        super(serverUri);
        this.messageList = messageList;
    }
    @Override
    public void onOpen(ServerHandshake handshake){}
    @Override
    public void onMessage(String message){
        try {
            ChatMessage msg = mapper.readValue(message, ChatMessage.class);
            Platform.runLater(()->{
               messageList.getItems().add("[" + msg.user + "]: " + msg.text);
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void onError(Exception ex){}
    @Override
    public void onClose(int code, String reason, boolean remote){}

}
