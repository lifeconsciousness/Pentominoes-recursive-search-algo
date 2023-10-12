
/**
 * @author Department of Data Science and Knowledge Engineering (DKE)
 * @version 2022.0
 */

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.io.*;
import java.lang.Thread;

/**
 * This class includes the methods to support the search of a solution.
 */
public class SearchNew {
    // Global variables
    public static int horizontalGridSize; // 10
    public static int verticalGridSize; // 6
    public static char[] inputMain; // XIZTUVWYLPN
    public static boolean stopAttempt; // false
    public static boolean solutionFound; // false
    public static UI ui;

    public static void wait(int ms) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // System.out.println(e);
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Helper function which request the parameters to the user
     */
    public static void setup() {
        // Scanner scanner = new Scanner(System.in);
        // Width
        // System.out.print("Width of the canvas (10): ");
        horizontalGridSize = 6;
        // horizontalGridSize = scanner.nextInt();
        // Height
        // System.out.print("Height of the canvas (6): ");
        // verticalGridSize = scanner.nextInt();
        verticalGridSize = 5;
        // Pentominoes to use
        // System.out.print("Input array (XIZTUVWYLPNF): ");
        // input = scanner.next().toCharArray();
        // inputMain = new char[] { 'W', 'Y', 'I', 'T', 'Z', 'L', 'P', 'N', 'F' };
        inputMain = new char[] { 'I', 'W', 'Z', 'T', 'L', 'Y' };
        // input = new char[] { 'I' };

        // UI class to display the board
        ui = new UI(horizontalGridSize, verticalGridSize, 50);
    }

    /**
     * Helper function which starts a basic search algorithm
     * 
     * @throws InterruptedException
     */
    public static void search() throws InterruptedException {
        // Initialize an empty board
        int[][] field = new int[horizontalGridSize][verticalGridSize];

        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                // -1 in the state matrix corresponds to empty square
                // Any positive number identifies the ID of the pentomino
                field[i][j] = -1;
            }
        }
        basicSearch(field, inputMain); // Start the basic search
    }

    /**
     * Get as input the character representation of a pentomino and translate it
     * into its corresponding numerical value (ID)
     * 
     * @param character a character representing a pentomino
     * @return the corresponding ID (numerical value)
     */
    private static int characterToID(char character) {
        int pentID = -1;
        switch (character) {
            case 'X':
                pentID = 0;
                break;
            case 'I':
                pentID = 1;
                break;
            case 'Z':
                pentID = 2;
                break;
            case 'T':
                pentID = 3;
                break;
            case 'U':
                pentID = 4;
                break;
            case 'V':
                pentID = 5;
                break;
            case 'W':
                pentID = 6;
                break;
            case 'Y':
                pentID = 7;
                break;
            case 'L':
                pentID = 8;
                break;
            case 'P':
                pentID = 9;
                break;
            case 'N':
                pentID = 10;
                break;
            case 'F':
                pentID = 11;
                break;
            default:
                pentID = -1;
        }
        return pentID;
    }

    /**
     * Basic implementation of a search algorithm. It is not a brute force
     * algorithms (it does not check all the possible combinations)
     * but randomly takes possible combinations and positions to find a possible
     * solution.
     * The solution is not necessarily the most efficient one
     * This algorithm can be very time-consuming
     * 
     * @param field a matrix representing the board to be fulfilled with pentominoes
     * @throws InterruptedException
     */
    public static void clearField(int[][] field) {
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                field[i][j] = -1;
            }
        }
    }

    private static void basicSearch(int[][] field, char[] input) throws InterruptedException {
        long solutionCounter = 0;

        // Put all pentominoes with random rotation/flipping on a random position on the
        // board
        for (int k = 0; k < input.length; k++) {
            // Choose a pentomino and randomly rotate/flip it
            int pentID = characterToID(input[k]);
            boolean placementFound = false;

            for (int y = 0; y < field[0].length; y++) { // loop over y position of pentomino
                for (int x = 0; x < field.length; x++) { // loop over x position of pentomino
                    for (int mut = 0; mut < PentominoDatabase.data[pentID].length; mut++) {
                        int[][] pieceToPlace = PentominoDatabase.data[pentID][mut];

                        // System.out.println("Mutation: " + mut);
                        // System.out.println(canAdd(field, pieceToPlace, x, y));

                        if (canAdd(field, pieceToPlace, x, y)) {
                            addPiece(field, pieceToPlace, pentID, x, y);
                            ui.setState(field); // display the field
                            Thread.sleep(800);

                            // includes all pieces except for the current one
                            char[] newPieces = new char[input.length - 1];
                            int indexCounter = 0;

                            for (int i = 0; i < input.length; i++) {
                                if (input[i] != input[k]) {
                                    newPieces[indexCounter] = input[i];
                                    indexCounter++;
                                    // System.out.print(input[i] + " ");
                                }
                            }

                            if (newPieces.length == 0) {
                                ui.setState(field); // display the field
                                System.out.println("SOlution found");
                            } else {
                                basicSearch(field, newPieces);
                            }

                            placementFound = true;
                            break;
                        }
                    }
                    if (placementFound) {
                        break; // Break out of the y loop
                    }
                }
                if (placementFound) {
                    break; // Break out of the y loop
                }
            }
        }
    }

    public static boolean canAdd(int[][] field, int[][] piece, int x, int y) {
        if (y + piece[0].length > verticalGridSize || x + piece.length > horizontalGridSize) {
            return false;
        }
        for (int k = 0; k < piece.length; k++) { // loop over x position of pentomino
            for (int l = 0; l < piece[k].length; l++) { // loop over y position of pentomino
                if (piece[k][l] == 1) {
                    if (field[x + k][y + l] != -1) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static void addPiece(int[][] field, int[][] piece, int pieceId, int x, int y) {
        for (int k = 0; k < piece.length; k++) { // loop over x position of pentomino
            for (int l = 0; l < piece[k].length; l++) { // loop over y position of pentomino
                if (piece[k][l] == 1) {
                    field[x + k][y + l] = pieceId;
                }
            }
        }
    }

    /**
     * Main function. Needs to be executed to start the basic search algorithm
     * 
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        setup();
        search();
    }
}