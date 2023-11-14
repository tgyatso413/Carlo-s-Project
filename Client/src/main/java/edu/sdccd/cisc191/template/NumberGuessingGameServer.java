package edu.sdccd.cisc191.template;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;

public class NumberGuessingGameServer {
    private static ArrayList<Integer> highScores = new ArrayList<>();

    public static void main(String[] args) {
        int port = 3000;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                Thread clientThread = new Thread(() -> handleClient(clientSocket));
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (
                ObjectInputStream objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
        ) {
            Message message = (Message) objectInputStream.readObject();

            if ("HighScore".equals(message.getType())) {
                int score = Integer.parseInt(message.getContent());
                highScores.add(score);
                Collections.sort(highScores);
                objectOutputStream.writeObject(new Message("Response", "High score updated."));
            } else {
                objectOutputStream.writeObject(new Message("Response", "Received: " + message.getContent()));
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    static class Message implements Serializable {
        private String type;
        private String content;

        public Message(String type, String content) {
            this.type = type;
            this.content = content;
        }

        public String getType() {
            return type;
        }

        public String getContent() {
            return content;
        }

        @Override
        public String toString() {
            return "Message{" +
                    "type='" + type + '\'' +
                    ", content='" + content + '\'' +
                    '}';
        }
    }
}

