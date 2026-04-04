package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.control.ListView;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

public class ChatServer extends WebSocketServer {
    final private ObjectMapper mapper = new ObjectMapper();
    static Set<String> usersList = new LinkedHashSet<String>();
    private HashMap<WebSocket, String> socketAddressToLoginMap = new HashMap();
    public ChatServer(int port) {
        super(new InetSocketAddress(port));
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {

    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        try {
            ChatMessage msg = this.mapper.readValue(message, ChatMessage.class);
            switch (msg.type) {
                case "message":
                    System.out.println("[" + msg.user + "]: " + msg.text);
                    broadcast(message);
                    break;
                    case "login":
                        String login = msg.user;
                        System.out.println("Новый клиент: " + login);

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

                        ChatMessage joinUserMessage = new ChatMessage();
                        joinUserMessage.type = "join";
                        joinUserMessage.user = login;

                        try {
                            json = this.mapper.writeValueAsString(joinUserMessage);
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }

                        socketAddressToLoginMap.put(conn, login);
                        usersList.add(joinUserMessage.user);
                        broadcast(json);

            }
        } catch (Exception e) {
            System.out.println("Получено не-JSON сообщение: " + message);
        }
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println("Клиент отключился: " + conn.getRemoteSocketAddress());
        String login = socketAddressToLoginMap.get(conn);
        socketAddressToLoginMap.remove(conn);
        usersList.remove(login);

        ChatMessage msg = new ChatMessage();
        msg.type = "leave";
        msg.user = login;
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