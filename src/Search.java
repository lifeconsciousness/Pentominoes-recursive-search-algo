
/**
 * @author Department of Data Science and Knowledge Engineering (DKE)
 * @version 2022.0
 */

import java.util.ArrayList;
import java.util.Random;

/**
 * This class includes the methods to support the search of a solution.
 */
public class Search {
	public static final int horizontalGridSize = 5;
	public static final int verticalGridSize = 6;
	public static int calls = 0;
	public static int depth = 0;

	public static final char[] input = { 'W', 'Y', 'I', 'T', 'Z', 'L', 'P', 'N', 'F' };
	public static final int[] inputIds = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 };

	// Static UI class to display the board
	public static UI ui = new UI(horizontalGridSize, verticalGridSize, 50);

	/**
	 * Helper function which starts a basic search algorithm
	 */
	public static void search() {
		// Initialize an empty board
		int[][] field = new int[horizontalGridSize][verticalGridSize];

		for (int i = 0; i < field.length; i++) {
			for (int j = 0; j < field[i].length; j++) {
				// -1 in the state matrix corresponds to empty square
				// Any positive number identifies the ID of the pentomino
				field[i][j] = -1;
			}
		}
		// Start the basic search
		// basicSearch(field);
		// recursiveSearch(field, 0);
		recursiveSearch(field, inputIds, 0);
	}

	public static void recursiveSearch(int[][] field, int[] input, int depth) {
		calls++;
		// System.out.println(calls);

		// go through all pentominoes
		for (int i = 0; i < inputIds.length; i++) {
			// go through all permutations of a pentomino
			for (int j = 0; j < PentominoDatabase.data[i].length; j++) {

				// iterate over all positions on the field
				for (int boardX = 0; boardX < horizontalGridSize; boardX++) {
					for (int boardY = 0; boardY < verticalGridSize; boardY++) {

						// copy the field
						int[][] copiedField = new int[horizontalGridSize][verticalGridSize];
						for (int x = 0; x < horizontalGridSize; x++) {
							for (int y = 0; y < verticalGridSize; y++) {
								copiedField[x][y] = field[x][y];
							}
						}

						// try to place pentomino
						boolean placedPiece = placePiece(boardX, boardY, i, PentominoDatabase.data[i][j], copiedField);

						if (placedPiece) {
							System.out.println(placedPiece);
							int[] newPieces = new int[inputIds.length - 1];
							int indexCounter = 0;

							// create a new int[] containing all pieces other than the one that was just
							// placed
							for (int k = 0; k < inputIds.length; k++) {
								if (inputIds[k] != inputIds[i]) {
									newPieces[indexCounter] = inputIds[k];
									indexCounter++;
								}
							}

							// System.out.println(newPieces.length);
							// if newPieces is empty -- no pieces remain
							if (newPieces.length == 0) {
								ui.setState(copiedField);
								System.out.println("Solution found");
								break;
							}
							// recursion
							else {
								ui.setState(copiedField);
								recursiveSearch(copiedField, newPieces, depth + 1);
							}
						}
					}
				}
			}

		}
	}

	public static boolean placePiece(int boardX, int boardY, int currentPiece,
			int[][] currentPerm, int[][] currentBoard) {

		// for each point in the piece
		for (int pieceX = 0; pieceX < currentPerm[0].length - 1; pieceX++) {
			for (int pieceY = 0; pieceY < currentPerm.length - 1; pieceY++) {

				// if the piece has a filled square
				if (currentPerm[pieceX][pieceY] != 0) {

					int x = boardX + pieceX; // 2

					// check x boundary
					if (x >= currentPerm[0].length) {
						return false;
					}
					int y = boardY + pieceY; // 1

					// check y boundary
					if (y >= currentPerm.length) {
						return false;
					}

					// check if board has empty spot
					if (currentBoard[x][y] != -1) {
						return false;
					}

					currentBoard[x][y] = currentPiece;
				}
			}
		}
		return true;
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
		if (character == 'X') {
			pentID = 0;
		} else if (character == 'I') {
			pentID = 1;
		} else if (character == 'Z') {
			pentID = 2;
		} else if (character == 'T') {
			pentID = 3;
		} else if (character == 'U') {
			pentID = 4;
		} else if (character == 'V') {
			pentID = 5;
		} else if (character == 'W') {
			pentID = 6;
		} else if (character == 'Y') {
			pentID = 7;
		} else if (character == 'L') {
			pentID = 8;
		} else if (character == 'P') {
			pentID = 9;
		} else if (character == 'N') {
			pentID = 10;
		} else if (character == 'F') {
			pentID = 11;
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
		boolean solutionFound = false;
		long solutionCounter = 0;

		while (!solutionFound) {
			solutionFound = true;
			// Empty board again to find a solution
			for (int i = 0; i < field.length; i++) {
				for (int j = 0; j < field[i].length; j++) {
					field[i][j] = -1;
				}
			}

			// Put all pentominoes with random rotation/flipping on a random position on the
			// board
			for (int i = 0; i < input.length; i++) {

				// Choose a pentomino and randomly rotate/flip it
				int pentID = characterToID(input[i]);
				int mutation = random.nextInt(PentominoDatabase.data[pentID].length);
				int[][] pieceToPlace = PentominoDatabase.data[pentID][mutation];

				// Randomly generate a position to put the pentomino on the board
				int x;
				int y;
				if (horizontalGridSize < pieceToPlace.length) {
					// this particular rotation of the piece is too long for the field
					x = -1;
				} else if (horizontalGridSize == pieceToPlace.length) {
					// this particular rotation of the piece fits perfectly into the width of the
					// field
					x = 0;
				} else {
					// there are multiple possibilities where to place the piece without leaving the
					// field
					x = random.nextInt(horizontalGridSize - pieceToPlace.length + 1);
				}

				if (verticalGridSize < pieceToPlace[0].length) {
					// this particular rotation of the piece is too high for the field
					y = -1;
				} else if (verticalGridSize == pieceToPlace[0].length) {
					// this particular rotation of the piece fits perfectly into the height of the
					// field
					y = 0;
				} else {
					// there are multiple possibilities where to place the piece without leaving the
					// field
					y = random.nextInt(verticalGridSize - pieceToPlace[0].length + 1);
				}

				// If there is a possibility to place the piece on the field, do it
				if (x >= 0 && y >= 0) {
					addPiece(field, pieceToPlace, pentID, x, y);
				}
			}

			for (int i = 0; i < field.length; i++) {
				for (int j = 0; j < field[i].length; j++) {
					if (field[i][j] == -1) {
						solutionFound = false;
					}
				}
			}

			if (solutionFound) {
				// display the field
				ui.setState(field);
				System.out.println("Solution found");
				break;
			} else {
				ui.setState(field);
				System.out.println("Invalid Solution - " + solutionCounter++);
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
	public static void addPiece(int[][] field, int[][] piece, int pieceID, int x, int y) {
		for (int i = 0; i < piece.length; i++) // loop over x position of pentomino
		{
			for (int j = 0; j < piece[i].length; j++) // loop over y position of pentomino
			{
				if (piece[i][j] == 1) {
					// Add the ID of the pentomino to the board if the pentomino occupies this
					// square
					field[x + i][y + j] = pieceID;
				}
			}
		}
	}

	/**
	 * Main function. Needs to be executed to start the basic search algorithm
	 */
	public static void main(String[] args) {
		search();
	}
}