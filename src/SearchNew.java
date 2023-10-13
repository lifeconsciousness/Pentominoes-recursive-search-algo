
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
    public static long calls;
    public static long solutionCounter = 0;

    /**
     * Helper function which request the parameters to the user
     */
    public static void setup() {
        // Scanner scanner = new Scanner(System.in);

        //// Width
        // System.out.print("Width of the canvas (10): ");
        // horizontalGridSize = scanner.nextInt();

        //// Height
        // System.out.print("Height of the canvas (6): ");
        // verticalGridSize = scanner.nextInt();

        // Pentominoes to use
        // System.out.print("Input array (XIZTUVWYLPNF): ");
        // input = scanner.next().toCharArray();

        verticalGridSize = 5;
        horizontalGridSize = 6;
        inputMain = new char[] { 'T', 'U', 'I', 'Z', 'L', 'P', 'N', 'F', 'V', 'X', 'W', 'Y' };

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
        recursiveSearch(field, inputMain); // Start the basic search
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

    // algorithm that recursively find the combination of pentominoes which fits in
    // the field, without having any empty points. Function uses branching in order
    // to try all the different possible combinations of pentominoes
    private static void recursiveSearch(int[][] field, char[] input) throws InterruptedException {
        calls++;

        // loop through all pentominoes
        for (int currentPent = 0; currentPent < input.length; currentPent++) {
            // Choose a pentomino id depending on currentPent
            int pentID = characterToID(input[currentPent]);

            // loop through all permutation of given pent
            for (int mut = 0; mut < PentominoDatabase.data[pentID].length; mut++) {

                // loop through every position on the field
                for (int x = 0; x < field[0].length; x++) {
                    for (int y = 0; y < field.length; y++) {

                        // check if solution was found
                        if (checkField(field)) {
                            System.out.println("Solution found");
                            System.out.println("Function was called " + calls + " times");
                            solutionCounter++;
                            ui.setState(field);
                            Thread.sleep(20000);

                            break;
                        }

                        // choose mutation of the current pentomino
                        int[][] pentToPlace = PentominoDatabase.data[pentID][mut];

                        // copy field in order to use it for branching
                        int[][] copiedField = new int[field.length][field[0].length];
                        for (int yField = 0; yField < field.length; yField++) {
                            for (int xField = 0; xField < field[0].length; xField++) {
                                copiedField[yField][xField] = field[yField][xField];
                            }
                        }

                        // check if current permutation of pentomino can be added on the y,x
                        // coordinates of the field
                        if (canAdd(copiedField, pentToPlace, x, y)) {
                            // add pentomino to the copied field
                            addpent(copiedField, pentToPlace, pentID, x, y);
                            ui.setState(copiedField); // display the field

                            // includes all pents except for the current one
                            char[] filteredPents = new char[input.length - 1];
                            int newArrCounter = 0;

                            for (int i = 0; i < input.length; i++) {
                                if (input[i] != input[currentPent]) {
                                    filteredPents[newArrCounter] = input[i];
                                    newArrCounter++;
                                }
                            }

                            // if array of available pentominoes is not exhausted, recursively call the
                            // function again
                            if (filteredPents.length != 0) {
                                recursiveSearch(copiedField, filteredPents);
                            }
                        }
                    }
                }
            }
        }
    }

    // check if field contains empty spots, if so return false
    // in other case solution is found
    public static boolean checkField(int[][] field) {
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                if (field[i][j] == -1) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean canAdd(int[][] field, int[][] pent, int x, int y) {
        // check if pentomino will get out of bounds of the field
        if (x + pent[0].length > verticalGridSize || y + pent.length > horizontalGridSize) {
            return false;
        }
        // check if there anyhting except for empty spot on the field
        for (int k = 0; k < pent.length; k++) { // loop over x position of pentomino
            for (int l = 0; l < pent[k].length; l++) { // loop over y position of pentomino
                if (pent[k][l] == 1) {
                    if (field[y + k][x + l] != -1) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    // add pentomino to the field
    public static void addpent(int[][] field, int[][] pent, int pentId, int x, int y) {
        for (int k = 0; k < pent.length; k++) { // loop over x position of pentomino
            for (int l = 0; l < pent[k].length; l++) { // loop over y position of pentomino
                if (pent[k][l] == 1) {
                    field[y + k][x + l] = pentId;
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