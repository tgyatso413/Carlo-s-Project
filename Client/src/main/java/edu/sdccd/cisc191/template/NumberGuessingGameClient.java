package edu.sdccd.cisc191.template;

import java.io.*;
import java.net.*;
import java.util.Scanner;

class NumberGuessingGameClient {
    public static void main(String[] args) {
        String serverAddress = "localhost"; // Change to the server's IP address or hostname
        int serverPort = 3000;

        try (Socket socket = new Socket(serverAddress, serverPort);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Connected to the server.");

            while (true) {
                System.out.println("1. Play the game");
                System.out.println("2. Get High Scores");
                System.out.println("3. Exit");
                System.out.print("Choose an option: ");

                // Add error handling for non-integer input
                int choice;
                try {
                    choice = scanner.nextInt();
                } catch (java.util.InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a valid integer.");
                    scanner.nextLine(); // Consume the invalid input
                    continue; // Continue the loop to prompt for input again
                }

                if (choice == 1) {
                    // Play the game logic here
                    System.out.println("Playing the game...");
                    // When a player achieves a high score, send it to the server
                    int highScore = 42; // Replace with the actual high score
                    writer.println("HighScore:" + highScore);
                    System.out.println("High score sent to the server.");
                } else if (choice == 2) {
                    // Request high scores from the server
                    writer.println("GetHighScores");
                    String response = reader.readLine();
                    System.out.println("High Scores: " + response);
                } else if (choice == 3) {
                    System.out.println("Exiting the game.");
                    break;
                } else {
                    System.out.println("Invalid choice. Please choose a valid option.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
