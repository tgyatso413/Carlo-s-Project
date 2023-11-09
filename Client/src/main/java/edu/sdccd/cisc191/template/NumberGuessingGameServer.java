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

                // Create a new thread to handle client communication
                Thread clientThread = new Thread(() -> handleClient(clientSocket));
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (
                InputStream inputStream = clientSocket.getInputStream();
                OutputStream outputStream = clientSocket.getOutputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                PrintWriter writer = new PrintWriter(outputStream, true)
        ) {
            String clientMessage;

            // Read messages from the client
            while ((clientMessage = reader.readLine()) != null) {
                System.out.println("Received from client: " + clientMessage);

                // Check if the client message is a high score update
                if (clientMessage.startsWith("HighScore:")) {
                    int score = Integer.parseInt(clientMessage.substring(11));
                    highScores.add(score);
                    Collections.sort(highScores); // Sort high scores
                    writer.println("High score updated.");
                } else {
                    // Handle other types of messages if needed
                    writer.println("Server received: " + clientMessage);
                }
            }

            // Close the client socket when done
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
