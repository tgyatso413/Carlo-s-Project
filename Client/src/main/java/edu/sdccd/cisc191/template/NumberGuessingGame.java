package edu.sdccd.cisc191.template;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * This class represents a Number Guessing Game.
 */
class Node {
    int value;
    Node next;

    /**
     * Creates a new Node with the given value.
     *
     * @param value The value of the node.
     */
    public Node(int value) {
        this.value = value;
        this.next = null;
    }
}

/**
 * This class represents a User in the game.
 */
class User {
    private String name;
    private int score;

    /**
     * Creates a new User with the given name and score.
     *
     * @param name  The name of the user.
     * @param score The score of the user.
     */
    public User(String name, int score) {
        this.name = name;
        this.score = score;
    }

    /**
     * Gets the name of the user.
     *
     * @return The name of the user.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the score of the user.
     *
     * @return The score of the user.
     */
    public int getScore() {
        return score;
    }

    /**
     * Simulated network communication method.
     * In a real application, this would send data to a server.
     */
    public void sendMessageToServer() {
        // Simulate sending user data to a server (network communication)
        System.out.println("Sending user data to server: " + this);
    }

    @Override
    public String toString() {
        return "User{name='" + name + "', score=" + score + '}';
    }
}

/**
 * The main class for the Number Guessing Game.
 */
public class NumberGuessingGame {

    Map<String, User> userMap = new HashMap<>();
    private ArrayList<Integer> highScores = new ArrayList<>();

    // Network communication
    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;

    public NumberGuessingGame() {
        try {
            // Establish a connection to the server
            socket = new Socket("localhost", 3000);
            writer = new PrintWriter(socket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        NumberGuessingGame game = new NumberGuessingGame();
        game.startGame();
    }

    public void startGame() {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        // Generate a linked list for the secret number
        Node secretNumberList = generateSecretNumberList(random.nextInt(100) + 1);
        int attempts = 0;

        // Load high scores from a file
        highScores = loadHighScores();

        System.out.println("Welcome to the Number Guessing Game!");
        System.out.println("Try to guess the secret number between 1 and 100.");

        while (true) {
            System.out.print("Enter your guess: ");
            int guess = scanner.nextInt();
            attempts++;

            if (guess < secretNumberList.value) {
                System.out.println("Try higher!");
            } else if (guess > secretNumberList.value) {
                System.out.println("Try lower!");
            } else {
                System.out.println("Congratulations! You guessed the number in " + attempts + " attempts.");
                highScores.add(attempts);
                Collections.sort(highScores); // Sort high scores

                // Save high scores to a file
                saveHighScores(highScores);

                System.out.print("Enter your name: ");
                String playerName = scanner.next();
                User user = new User(playerName, attempts);
                userMap.put(user.getName(), user);

                // Simulate sending user data to server (network communication)
                user.sendMessageToServer();

                System.out.print("Play again? (yes/no): ");
                String playAgain = scanner.next();

                if ("no".equalsIgnoreCase(playAgain)) {
                    System.out.println("Thanks for playing! Goodbye.");
                    break;
                } else if ("yes".equalsIgnoreCase(playAgain)) {
                    secretNumberList = generateSecretNumberList(random.nextInt(100) + 1);
                    attempts = 0;
                }
            }
        }

        scanner.close();
    }

    /**
     * Generates a linked list with a random secret number.
     *
     * @param secretNumber The secret number.
     * @return The head of the linked list.
     */
    public static Node generateSecretNumberList(int secretNumber) {
        Node head = new Node(secretNumber);
        Node current = head;

        for (int i = 0; i < secretNumber; i++) {
            int randomValue = new Random().nextInt(100) + 1;
            Node newNode = new Node(randomValue);
            current.next = newNode;
            current = newNode;
        }

        return head;
    }

    /**
     * Loads the high scores from a file.
     *
     * @return An ArrayList containing the high scores.
     */
    static ArrayList<Integer> loadHighScores() {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("highscores.ser"))) {
            return (ArrayList<Integer>) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            // If there's an error, return an empty list
            return new ArrayList<>();
        }
    }

    /**
     * Saves the high scores to a file.
     *
     * @param highScores An ArrayList containing the high scores.
     */
    public static void saveHighScores(ArrayList<Integer> highScores) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("highscores.ser"))) {
            outputStream.writeObject(highScores);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean notifyAll(Node head, int i) {
        return false;
    }

    // Close the socket and resources when the game ends
    public void closeSocket() {
        try {
            writer.close();
            reader.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
