import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class MatrixGen {
    public static int horizontalGridSize = 10; // 10
    public static int verticalGridSize = 6; // 6
    public static char[] input = {'X', 'I', 'Z', 'T', 'U', 'V', 'W', 'Y', 'L', 'P', 'N', 'F'}; // XIZ TUV WYL PNF
    public static int totalPossibilities = 0;
    public static int cRow = 0;
    public static int displacement = 0;
    public static int[][] matrix;

    public static void main(String[] args) {
        //setup();
        totalPossibilities = getRows(input);
        matrix = new int[horizontalGridSize*verticalGridSize+input.length][totalPossibilities];
        tryPentominoes(input);

        //ran(matrix);
        try {
            toCSV(matrix);
        } catch(Exception e) {
            e.getStackTrace();
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

        //field = new int[horizontalGridSize][verticalGridSize];
	}

    public static int getRows(char[] input) {
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
    } public static int getRows(int input) {
        int possibilities = 0;
        for(int j = 0; j < PentominoDatabase.data[input].length; j++) {
            int[][] pieceToPlace = PentominoDatabase.data[input][j];
            for(int x = 0; x <= horizontalGridSize; x++) {
                for(int y = 0; y <= verticalGridSize; y++) {
                    if (((x + pieceToPlace[0].length) <= horizontalGridSize) && ((y + pieceToPlace.length) <= verticalGridSize)) {
                        possibilities++;
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
                for(int x = 0; x <= horizontalGridSize; x++) {
                    for(int y = 0; y <= verticalGridSize; y++) {
                        if (((x + pieceToPlace[0].length) <= horizontalGridSize) && ((y + pieceToPlace.length) <= verticalGridSize)) {
                            addToArray(pentID, pieceToPlace, x, y);
                        }
                    }
                }
                displacement = 0;
            }
        }
    }

    public static void addToArray(int pentID, int[][] pieceToPlace, int x, int y) {
        matrix[pentID][cRow] = 1;
        for(int i = 0; i < pieceToPlace.length; i++) {
            for(int j = 0; j < pieceToPlace[i].length; j++) {
                if(pieceToPlace[i][j] != 0) {
                    try { 
                        int tmp = 12+i+(j*10); //+displacement; //implement funciton that turns pento into [ 0 1 0 0 0 0 0 0 0 0 ] [ 1 1 1 0 0 0 0 0 0 0 ] [ 0 1 0 0 0 0 0 0 0 0 ] [ 0 0 0 0 0 0 0 0 0 0 ] [ 0 0 0 0 0 0 0 0 0 0 ] [ 0 0 0 0 0 0 0 0 0 0 ]
                        matrix[tmp][cRow] = 1;
                    } catch(Exception e) {
                        e.getStackTrace();
                    }
                }
            }
        }
        displacement++;
        cRow++;
    }
    
    public static void toCSV(int[][] matrix) throws IOException {
        FileWriter writer = new FileWriter("matrix.csv");
        writer.append("X I Z T U V W Y L P N F |   0 1 2 3 4 5 6 7 8 9     0 1 2 3 4 5 6 7 8 9     0 1 2 3 4 5 6 7 8 9     0 1 2 3 4 5 6 7 8 9     0 1 2 3 4 5 6 7 8 9     0 1 2 3 4 5 6 7 8 9\n");
        writer.flush();
        System.out.println(matrix.length);
        for(int i = 0; i < totalPossibilities; i++) {
            for(int j = 0; j < matrix.length; j++) {
                writer.append(matrix[j][i] + " ");
                if(j == 11) { writer.append("| [ ");}
                else if(j == 21) { writer.append("] [ "); }
                else if(j == 31) { writer.append("] [ "); }
                else if(j == 41) { writer.append("] [ "); }
                else if(j == 51) { writer.append("] [ "); }
                else if(j == 61) { writer.append("] [ "); }
                else if(j == 71) { writer.append("]"); }
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
			default:
				pentID = -1;
		}
    	return pentID;
    }

    public static void ran(int[][] matrix) {
        for(int i = 0; i < matrix.length; i++) {
            for(int j = 0; j < matrix[i].length; j++) {
                matrix[i][j] = (int) Math.round(Math.random()); //(int) Math.round(Math.random());
            }
        }
    }
}
