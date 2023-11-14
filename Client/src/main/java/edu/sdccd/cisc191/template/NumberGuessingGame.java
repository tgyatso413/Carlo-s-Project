package edu.sdccd.cisc191.template;

import java.io.*;
import java.net.*;
import java.util.*;

class Node {
    int value;
    Node next;

    public Node(int value) {
        this.value = value;
        this.next = null;
    }

    public void printNodesRecursively() {
        System.out.print(this.value + " ");
        if (this.next != null) {
            this.next.printNodesRecursively();
        }
    }

    public boolean search(int target) {
        Node current = this;
        while (current != null) {
            if (current.value == target) {
                return true;
            }
            current = current.next;
        }
        return false;
    }
}

class User {
    private String name;
    private int score;

    public User(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public void sendMessageToServer() {
        // Adjust this method to actually send data to the server
        // For now, it just prints a message
        System.out.println("Sending user data to server: " + this);
    }

    @Override
    public String toString() {
        return "User{name='" + name + "', score=" + score + '}';
    }
    public class Message implements Serializable {
        private String type;
        private String content;

        public Message(String type, String content) {
            this.type = type;
            this.content = content;
        }

        // Standard getters and setters
        public String getType() { return type; }
        public String getContent() { return content; }
        public void setType(String type) { this.type = type; }
        public void setContent(String content) { this.content = content; }

        @Override
        public String toString() {
            return "Message{" +
                    "type='" + type + '\'' +
                    ", content='" + content + '\'' +
                    '}';
        }
    }
}

public class NumberGuessingGame {

    Map<String, User> userMap = new HashMap<>();
    private ArrayList<Integer> highScores = new ArrayList<>();

    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;

    public NumberGuessingGame() {
        try {
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

        head.printNodesRecursively(); // Print the list recursively
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
    public static void bubbleSortHighScores(ArrayList<Integer> scores) {
        int n = scores.size();
        for (int i = 0; i < n-1; i++) {
            for (int j = 0; j < n-i-1; j++) {
                if (scores.get(j) > scores.get(j+1)) {
                    Integer temp = scores.get(j);
                    scores.set(j, scores.get(j+1));
                    scores.set(j+1, temp);
                }
            }
        }
    }

    // Close the socket and resources when the game ends
    public void closeSocket() {
        try {
            if (writer != null) writer.close();
            if (reader != null) reader.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}