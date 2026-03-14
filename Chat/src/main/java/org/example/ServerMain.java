package org.example;


public class ServerMain {
    public static void main(String[] args) throws InterruptedException {
        ChatServer server = new ChatServer(8080);
        server.start();
    }
}