
/**
 * @author Department of Data Science and Knowledge Engineering (DKE)
 * @version 2022.0
 */

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

/**
 * This class includes the methods to support the search of a solution.
 */
public class SearchNew {
    // Global variables
    public static int horizontalGridSize = 5; // 10
    public static int verticalGridSize = 5; // 6
    public static char[] input; // XIZTUVWYLPN
    public static boolean stopAttempt; // false
    public static boolean solutionFound; // false
    public static UI ui;

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
        // input = new char[] { 'W', 'Y', 'I', 'T', 'Z', 'L', 'P', 'N', 'F' };
        input = new char[] { 'I' };

        // UI class to display the board
        ui = new UI(horizontalGridSize, verticalGridSize, 50);
    }

    /**
     * Helper function which starts a basic search algorithm
     */
    public static void search() {
        // Initialize an empty board
        // int[][] field = new int[horizontalGridSize][verticalGridSize];
        int[][] field = {
                { 6, -1, -1, -1, -1 },
                { 6, 6, -1, -1, -1 },
                { -1, 6, 6, -1, -1 },
                { -1, -1, -1, -1, -1 },
                { -1, -1, -1, -1, -1 }
        };

        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                // -1 in the state matrix corresponds to empty square
                // Any positive number identifies the ID of the pentomino
                field[i][j] = -1;
            }
        }
        basicSearch(field); // Start the basic search
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
     */
    private static void basicSearch(int[][] field) {
        Random random = new Random();
        solutionFound = false;
        long solutionCounter = 0;

        while (!solutionFound) {
            // DELETED Empty board again to find a solution

            stopAttempt = false;
            // Put all pentominoes with random rotation/flipping on a random position on the
            // board
            for (int k = 0; k < input.length; k++) {
                // Choose a pentomino and randomly rotate/flip it
                int pentID = characterToID(input[k]);
                // int mutation = random.nextInt(PentominoDatabase.data[pentID].length);
                // int[][] pieceToPlace = PentominoDatabase.data[pentID][mutation];

                // Randomly generate a position to put the pentomino on the board

                for (int x = 0; x < field.length; x++) { // loop over x position of pentomino
                    for (int y = 0; y < field[0].length; y++) { // loop over y position of pentomino
                        for (int mut = 0; mut < PentominoDatabase.data[pentID].length; mut++) {
                            int[][] pieceToPlace = PentominoDatabase.data[pentID][mut];

                            if (canAdd(field, pieceToPlace, x, y)) {
                                addPiece(field, pieceToPlace, mut, x, y);
                                solutionFound = true;
                            } else {
                                solutionFound = false;
                            }

                        }
                    }
                }

                // // If there is a possibility to place the piece on the field, do it
                // if (x >= 0 && y >= 0) {
                // solutionFound = true;
                // addPiece(field, pieceToPlace, pentID, x, y);
                // k++;
                // } else {
                // solutionFound = false;
                // }
            }

            if (!solutionFound) {
                ui.setState(field); // display the field
                System.out.println("Invalid Solution " + solutionCounter++ + " | overlap: " + stopAttempt);
            } else {
                ui.setState(field); // display the field
                System.out.println("Solution found " + solutionCounter);
                break;
            }
        }
    }

    /**
     * Adds a pentomino to the position on the field (overriding current board at
     * that position)
     * 
     * @param field   a matrix representing the board to be fulfilled with
     *                pentominoes
     * @param piece   a matrix representing the pentomino to be placed in the board
     * @param pieceID ID of the relevant pentomino
     * @param x       x position of the pentomino
     * @param y       y position of the pentomino
     */
    public static boolean canAdd(int[][] field, int[][] piece, int x, int y) {
        boolean fits = true;

        if (y + piece[0].length > horizontalGridSize || x + piece.length > verticalGridSize) {
            fits = false;
        }
        if (fits) {
            for (int k = 0; k < piece.length; k++) { // loop over x position of pentomino
                for (int l = 0; l < piece[k].length; l++) { // loop over y position of pentomino
                    if (piece[k][l] == 1) {

                        if (field[x + k][y + l] != -1) {
                            fits = false;
                        }

                    }
                }
            }
        }
        return fits;
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
     */
    public static void main(String[] args) {
        int[][] field = {
                { 6, -1, -1, -1, -1 },
                { 6, 6, -1, -1, -1 },
                { -1, 6, 6, -1, -1 },
                { -1, -1, -1, -1, -1 },
                { -1, -1, -1, -1, -1 }
        };

        int[][] piece = {
                { 0, 0, 1 },
                { 0, 0, 1 },
                { 1, 1, 1 },
        };

        System.out.println(canAdd(field, piece, 3, 3));
        addPiece(field, piece, 3, 2, 2);

        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                System.out.print(field[i][j] + " ");
            }
            System.out.println();
        }
    }
}