package edu.sdccd.cisc191.template;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import java.io.*;
import java.net.*;

class NumberGuessingGameTest {

    // Module 10: Test Linked Data Structures and Recursion
    @Test
    void testSearchInLinkedList() {
        Node head = new Node(10);
        for (int i = 5; i > 0; i--) {
            Node newNode = new Node(i);
            newNode.next = head;
            head = newNode;
        }
        assertTrue(head.search(10)); // Test searching in a linked list
        assertFalse(head.search(15));
    }

    // Module 9: Test Generics and Collections
    @Test
    void testBubbleSortHighScores() {
        ArrayList<Integer> scores = new ArrayList<>(Arrays.asList(5, 3, 8, 1, 2));
        NumberGuessingGame.bubbleSortHighScores(scores);
        assertEquals(Arrays.asList(1, 2, 3, 5, 8), scores); // Test sorting algorithm
    }

    // Module 7: Test I/O Streams (File Serialization)
    @Test
    void testHighScoresFileIO() throws IOException, ClassNotFoundException {
        ArrayList<Integer> scoresToSave = new ArrayList<>(Arrays.asList(30, 20, 10));
        NumberGuessingGame.saveHighScores(scoresToSave);

        ArrayList<Integer> loadedScores = NumberGuessingGame.loadHighScores();
        assertNotNull(loadedScores);
        assertEquals(scoresToSave, loadedScores); // Test file I/O operations
    }

    // Module 11: Test Additional Searching
    @Test
    void testLinearSearchInList() {
        ArrayList<Integer> numbers = new ArrayList<>(Arrays.asList(1, 3, 5, 7, 9));
        assertTrue(numbers.contains(5)); // Test linear search in a list
        assertFalse(numbers.contains(6));
    }
    class NumberGuessingGameNetworkingTest {

        private final String host = "localhost";
        private final int port = 3000;

        @Test
        void testServerConnection() {
            try {
                Socket socket = new Socket(host, port);
                assertTrue(socket.isConnected());
                socket.close();
            } catch (Exception e) {
                fail("Failed to connect to server: " + e.getMessage());
            }
        }
    }
}

