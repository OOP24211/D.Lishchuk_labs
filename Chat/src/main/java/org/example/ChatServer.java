package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.InetSocketAddress;

public class ChatServer extends WebSocketServer {
    public ChatServer(int port) {
        super(new InetSocketAddress(port));
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        System.out.println("Новый клиент: " + conn.getRemoteSocketAddress());
        conn.send("Добро пожаловать!");
    }

    final private ObjectMapper mapper = new ObjectMapper();
    @Override
    public void onMessage(WebSocket conn, String message) {
        try {
            ChatMessage msg = mapper.readValue(message, ChatMessage.class);
            System.out.println("[" + msg.user + "]: " + msg.text);
            broadcast(message); // рассылаем всем
        } catch (Exception e) {
            System.out.println("Получено не-JSON сообщение: " + message);
        }
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println("Клиент отключился: " + conn.getRemoteSocketAddress());
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