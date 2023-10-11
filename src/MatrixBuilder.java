import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class MatrixBuilder {
    public static int horizontalGridSize = 1; // 10
    public static int verticalGridSize = 2; // 6
    public static char[] input = { 'R', 'R'}; //{'X', 'I', 'Z', 'T', 'U', 'V', 'W', 'Y', 'L', 'P', 'N', 'F'}; // XIZ TUV WYL PNF
    public static int totalPossibilities = 0;
    public static int cRow = 0;
    public static int listPos = 0;
    public static int[][] matrix;

    public static void main(String[] args) {
        //setup();
        totalPossibilities = getPossibilities(input);
        matrix = new int[input.length+(horizontalGridSize*verticalGridSize)][totalPossibilities];
        tryPentominoes(input);
        try {
            toCSV(matrix);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void setup() {
		Scanner scanner = new Scanner(System.in);
		// Width
		System.out.print("Width of the canvas (10): ");
		horizontalGridSize = scanner.nextInt();
		// Height
		System.out.print("Height of the canvas (6): ");
		verticalGridSize = scanner.nextInt();
		// Pentominoes to use
		System.out.print("Input array (XIZTUVWYLPNF): ");
		input = scanner.next().toCharArray();
	}

    public static int getPossibilities(char[] input) {
        int possibilities = 0;
        for(int i = 0; i < input.length; i++) {
            int pentID = characterToID(input[i]);
            for(int j = 0; j < PentominoDatabase.data[pentID].length; j++) {
                int[][] pieceToPlace = PentominoDatabase.data[pentID][j];
                for(int x = 0; x <= horizontalGridSize; x++) {
                    for(int y = 0; y <= verticalGridSize; y++) {
                        if (((x + pieceToPlace[0].length) <= horizontalGridSize) && ((y + pieceToPlace.length) <= verticalGridSize)) {
                            possibilities++;
                        }
                    }
                }
            }
        }
        System.out.println("Total possibilities: " + possibilities);
        return possibilities;
    }

    public static void tryPentominoes(char[] input) {
        for(int i = 0; i < input.length; i++) {
            int pentID = characterToID(input[i]);
            for(int j = 0; j < PentominoDatabase.data[pentID].length; j++) {
                int[][] pieceToPlace = PentominoDatabase.data[pentID][j];
                for(int y = 0; y < verticalGridSize; y++) {
                    for(int x = 0; x < horizontalGridSize; x++) {
                        if(((x + pieceToPlace.length) <= horizontalGridSize) && ((y + pieceToPlace[0].length) <= verticalGridSize)) {
                            addToArray(pieceToPlace, x, y);
                        }
                    }
                }
            }
            listPos++;
        }
    }

    public static void addToArray(int[][] pieceToPlace, int x, int y) {
        matrix[listPos][cRow] = 1;
        //System.out.println(cRow);
        //System.out.println("x: " + x + "(" + ((pieceToPlace.length)+x) + ") y: " + y + "(" + ((pieceToPlace[0].length)+y) + ") ");
        for(int i = 0; i < pieceToPlace.length; i++) {
            for(int j = 0; j < pieceToPlace[i].length; j++) {
                int tmp = (i+(j*horizontalGridSize)) + (x+(y*horizontalGridSize)) + input.length;
                matrix[tmp][cRow] = pieceToPlace[i][j];
            }
        }
        cRow++;
    }

    public static void toCSV(int[][] matrix) throws IOException {
        FileWriter writer = new FileWriter("matrix.csv");
        for(int i = 0; i < totalPossibilities; i++) {
            for(int j = 0; j < matrix.length; j++) {
                writer.append(matrix[j][i] + ",");
            } 
            writer.flush();
            writer.append("\n");
        }
        writer.close();
        toReadableCSV(matrix);
    }

    public static void toReadableCSV(int[][] matrix) throws IOException {
        FileWriter writer = new FileWriter("matrixReadable.csv");
        String inputList = "";
        String numberPerRow = "";
        String allRows = "";
        for(int i = 0; i <input.length; i++) {
            inputList = inputList + input[i] + " ";
        }
        for(int i = 0; i < verticalGridSize; i++) {
            for(int j = 0; j < horizontalGridSize; j++) {
                numberPerRow = numberPerRow + j + " ";
            }
            allRows = allRows + "[ " + numberPerRow + "] ";
            numberPerRow = "";
        }
        writer.append(inputList + "| " + allRows + "\n");
        writer.flush();
        //System.out.println(Arrays.deepToString(matrix));
        for(int i = 0; i < totalPossibilities; i++) {
            for(int j = 0; j < matrix.length; j++) {
                if(j < input.length) {
                    writer.append(matrix[j][i] + " ");
                    if(j == input.length-1) { writer.append("| [ "); }
                } else {
                    writer.append(matrix[j][i] + " ");
                    for(int k = 0; k < verticalGridSize; k++) { 
                        if(j == (k*horizontalGridSize)+input.length-1) { writer.append("[ "); }
                        if(j == horizontalGridSize+(k*horizontalGridSize)+input.length-1) { writer.append("] "); }
                    }
                }
            } 
            writer.flush();
            writer.append('\n');
        }
        writer.close();
    }

    private static int characterToID(char character) {
		int pentID = -1;
		switch(character) {
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
            case 'R': // for testing 1x1 pentomino // REMOVE
                pentID = 12;
				break;
			default:
				pentID = -1;
		}
    	return pentID;
    }
}
