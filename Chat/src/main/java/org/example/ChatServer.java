package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.control.ListView;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class ChatServer extends WebSocketServer {
    final private ObjectMapper mapper = new ObjectMapper();
    static Set<String> usersList = new LinkedHashSet<String>();

    public ChatServer(int port) {
        super(new InetSocketAddress(port));
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        System.out.println("Новый клиент: " + conn.getRemoteSocketAddress());

        ChatMessage userListMsg = new ChatMessage();
        userListMsg.type = "userList";
        userListMsg.userList = usersList.stream().toList();

        String json;
        try {
            json = this.mapper.writeValueAsString(userListMsg);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        conn.send(json);

        ChatMessage msg = new ChatMessage();
        msg.type = "join";
        msg.user = conn.getRemoteSocketAddress().toString();

        try {
            json = this.mapper.writeValueAsString(msg);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        usersList.add(msg.user);
        broadcast(json);
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        try {
            ChatMessage msg = this.mapper.readValue(message, ChatMessage.class);
            System.out.println("[" + msg.user + "]: " + msg.text);
            broadcast(message); // рассылаем всем
        } catch (Exception e) {
            System.out.println("Получено не-JSON сообщение: " + message);
        }
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println("Клиент отключился: " + conn.getRemoteSocketAddress());

        usersList.remove(conn.getRemoteSocketAddress().toString());

        ChatMessage msg = new ChatMessage();
        msg.type = "leave";
        msg.user = conn.getRemoteSocketAddress().toString();
        String json;
        try {
            json = this.mapper.writeValueAsString(msg);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        broadcast(json);
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        ex.printStackTrace();
    }

    @Override
    public void onStart() {
        System.out.println("Сервер запущен на порту " + getPort());
    }

}