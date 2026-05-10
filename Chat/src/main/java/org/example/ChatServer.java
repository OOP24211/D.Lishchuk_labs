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
    static HashMap<String, LinkedHashSet> usersList = new HashMap<String, LinkedHashSet>();
    private HashMap<WebSocket, String> socketAddressToLoginMap = new HashMap();
    private HashMap<String, Set<WebSocket>> roomsList = new HashMap<>();
    private HashMap<WebSocket, String> connectionToRoomList = new HashMap<>();

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
                    String roomId = connectionToRoomList.get(conn);
                    for (WebSocket user : roomsList.get(roomId)) {
                        user.send(message);
                    }
                    break;
                case "login":
                    String login = msg.user;
                    socketAddressToLoginMap.put(conn, login);
                    System.out.println("Новый клиент: " + login);
                    break;
                case "roomID":
                    String roomID = msg.text;
                    if (roomsList.containsKey(roomID)) {
                        roomsList.get(roomID).add(conn);
                    } else {
                        roomsList.put(roomID, new LinkedHashSet<>());
                        roomsList.get(roomID).add(conn);
                        usersList.put(roomID, new LinkedHashSet());

                    }

                    connectionToRoomList.put(conn, roomID);
                    ChatMessage userListMsg = new ChatMessage();
                    userListMsg.type = "userList";
                    userListMsg.userList = usersList.get(roomID).stream().toList();

                    String json;
                    try {
                        json = this.mapper.writeValueAsString(userListMsg);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }

                    conn.send(json);

                    ChatMessage joinUserMessage = new ChatMessage();
                    joinUserMessage.type = "join";
                    login = socketAddressToLoginMap.get(conn);
                    joinUserMessage.user = login;
                    try {
                        json = this.mapper.writeValueAsString(joinUserMessage);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }

                    socketAddressToLoginMap.put(conn, login);
                    usersList.get(roomID).add(login);
                    for (WebSocket user : roomsList.get(roomID)) {
                        user.send(json);
                    }

            }
        } catch (Exception e) {
            System.out.println("Получено не-JSON сообщение: " + message);
        }
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        String login = socketAddressToLoginMap.get(conn);
        String roomID = connectionToRoomList.get(conn);

        System.out.println("Клиент отключился: " + login);

        socketAddressToLoginMap.remove(conn);

        if (roomID != null) {
            usersList.get(roomID).remove(login);
            String json;
            ChatMessage msg = new ChatMessage();
            msg.type = "leave";
            msg.user = login;
            try {
                json = this.mapper.writeValueAsString(msg);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            roomsList.get(roomID).remove(conn);
            connectionToRoomList.remove(conn);
            if (roomsList.get(roomID).isEmpty()) {
                roomsList.remove(roomID);
            } else {
                System.out.println("sadasd");
                for (WebSocket user : roomsList.get(roomID)) {
                    user.send(json);
                }
            }
        }
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