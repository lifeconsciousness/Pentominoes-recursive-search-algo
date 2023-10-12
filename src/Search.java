import java.util.Arrays;

public class Search {
	public static int horizontalGridSize = 10;
	public static int verticalGridSize = 6;
	public static char[] input = { 'X', 'I', 'Z', 'T', 'U', 'V', 'W', 'Y', 'L', 'P', 'N', 'F' };
	public static boolean solutionFound = false;
	public static UI ui;

	public static void setup() {
		ui = new UI(horizontalGridSize, verticalGridSize, 50);
	}

	public static void search() {
		int[][] field = new int[horizontalGridSize][verticalGridSize];
		solve(field, 0, 0);
	}

	public static void solve(int[][] field, int x, int y) {
		if (y == verticalGridSize) {
			// Reached the end of the grid (found a solution)
			solutionFound = true;
			ui.setState(field);
			return;
		}

		int nextX = (x + 1) % horizontalGridSize;
		int nextY = y + (x + 1) / horizontalGridSize;

		if (field[x][y] == -1) {
			for (char piece : input) {
				int pentID = characterToID(piece);
				for (int mutation = 0; mutation < PentominoDatabase.data[pentID].length; mutation++) {
					int[][] pieceToPlace = PentominoDatabase.data[pentID][mutation];
					if (canPlacePiece(field, pieceToPlace, x, y)) {
						placePiece(field, pieceToPlace, pentID, x, y);
						solve(field, nextX, nextY);
						if (solutionFound) {
							return;
						}
						removePiece(field, pieceToPlace, x, y);
					}
				}
			}
		} else {
			solve(field, nextX, nextY);
		}
	}

	public static boolean canPlacePiece(int[][] field, int[][] piece, int x, int y) {
		for (int i = 0; i < piece.length; i++) {
			for (int j = 0; j < piece[i].length; j++) {
				if (piece[i][j] == 1) {
					int newX = x + i;
					int newY = y + j;
					if (newX < 0 || newX >= horizontalGridSize || newY < 0 || newY >= verticalGridSize
							|| field[newX][newY] != -1) {
						return false;
					}
				}
			}
		}
		return true;
	}

	public static void placePiece(int[][] field, int[][] piece, int pieceID, int x, int y) {
		for (int i = 0; i < piece.length; i++) {
			for (int j = 0; j < piece[i].length; j++) {
				if (piece[i][j] == 1) {
					field[x + i][y + j] = pieceID;
				}
			}
		}
	}

	public static void removePiece(int[][] field, int[][] piece, int x, int y) {
		for (int i = 0; i < piece.length; i++) {
			for (int j = 0; j < piece[i].length; j++) {
				if (piece[i][j] == 1) {
					field[x + i][y + j] = -1;
				}
			}
		}
	}

	public static int characterToID(char character) {
		return Arrays.binarySearch(input, character);
	}

	public static void main(String[] args) {
		setup();
		search();
	}
}
