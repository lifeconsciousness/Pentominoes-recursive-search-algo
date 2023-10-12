import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class SearchExactCover {

    public static int branchRowNum = 0; // implement in a recursive manner
    public static int branchColumnNum = 0;
    public String solution = ""; // TODO

    public static UI ui;
    public static int[][] field;

    public static void main(String[] args) {
        int[][] Matrix = loadMatrix("matrix.csv");
        setup();
        exactCover(Matrix, 0, 0);
        //System.out.println(Arrays.toString(Matrix));
    }

    public static void setup() {
		Scanner scanner = new Scanner(System.in);
		// Width
		System.out.print("Width of the canvas (10): ");
		int horizontalGridSize = scanner.nextInt();
		// Height
		System.out.print("Height of the canvas (6): ");
		int verticalGridSize = scanner.nextInt();

		// UI class to display the board
    	ui = new UI(horizontalGridSize, verticalGridSize, 50);
        field = new int[verticalGridSize][horizontalGridSize];
	}

    public static void exactCover(int[][] Matrix, int currentColumn, int currentRow) { // doesnt work for 3 deep problems because it is not fully recursive (mostlikely if(currentColumn < Matrix[0].length-1))
        int selectedRowNum = selectRow(currentColumn, currentRow, Matrix);
        if(selectedRowNum == -1) {
            //System.out.println("No more rows valid in the column");
            currentRow = 0;
            if(currentColumn < Matrix[0].length-1) { // IDK if I have to make this recursive (works for any one that can be done in two steps)
                currentColumn++;
                exactCover(Matrix, currentColumn, currentRow);
            } else {
                System.out.println("[" + branchColumnNum + "/" + branchRowNum + "]------------------------------------------------------[" + branchColumnNum + "/" + branchRowNum + "]");
                Matrix = loadMatrix("matrix.csv");
                boolean finished = false;
                branchRowNum++;
                currentRow = branchRowNum;
                currentColumn = branchColumnNum;
                if(selectRow(currentColumn, currentRow, Matrix) == -1) {
                    branchRowNum = 0;
                    if(branchColumnNum < Matrix[0].length-1) {
                        branchColumnNum++;
                    } else {
                        finished = true; // last row for last colum may not work FUCKKKKKKKKK
                        System.out.println("Finished");
                    }
                }
                if(!finished) {
                    exactCover(Matrix, currentColumn, currentRow);
                }
            }
        } else {
            int[] selectedRow = getSelectedRow(selectedRowNum, Matrix);
            int[] columnsToDelete = getSColumns(selectedRow, Matrix);
            int[] rowsToDelete = addColumns(columnsToDelete, Matrix); 

            int[][] newMatrix = new int[getNewHeight(rowsToDelete, Matrix)][getNewWidth(columnsToDelete, Matrix)];
            newMatrix = populateMatrix(rowsToDelete, columnsToDelete, Matrix, newMatrix);
            checkSolution(currentColumn, currentRow, Matrix, newMatrix);
        }
    }

    public static void checkSolution(int currentColumn, int currentRow, int[][] Matrix, int[][] newMatrix) {
        if(newMatrix.length == 0) {
            //System.out.println("Invalid solution - 0 rows left");
            currentRow++;
            exactCover(Matrix, currentColumn, currentRow);
        } else if(newMatrix.length == 1) {
            boolean solutionCheck = true;
            for(int i= 0; i < newMatrix[0].length; i++) {
                if(newMatrix[0][i] != 1) {
                    solutionCheck = false;
                } 
            }
            if(solutionCheck) {
                System.out.print("Valid solution ");
                System.out.println(Arrays.deepToString(newMatrix));     // remove if getting more than 1 solution
            } else {
                System.out.println("Invalid solution - not full board");
                System.out.println(Arrays.deepToString(newMatrix));     // 
                currentRow++;                                           // move outside else for more results
                exactCover(Matrix, currentColumn, currentRow);          // 
            }
        } else { // Go to next level of depth
            //System.out.println(Arrays.deepToString(newMatrix));
            exactCover(newMatrix, 0, 0);
        }
    }

    public static int[][] populateMatrix(int[] rowsToDelete, int[] columnsToDelete, int[][] Matrix, int[][] newMatrix) {
        int rowCount = 0;
        int columnCount = 0;

        for(int i = 0; i < Matrix.length; i++) {
            if(rowsToDelete[i] == 0) {
                for(int k = 0; k < Matrix[0].length; k++) { // 0 change to i
                    if(columnsToDelete[k] == -1) {
                        newMatrix[rowCount][columnCount] = Matrix[i][k];
                        columnCount++;
                    }
                }
                columnCount = 0;
                rowCount++;
            }
        }
        return newMatrix;
    }

    public static int getNewHeight(int[] rowsToDelete, int[][] Matrix) {
        int newHeight = Matrix.length;
        for(int i = 0; i < rowsToDelete.length; i++) {
            if(rowsToDelete[i] > 0) {
                newHeight--;
            }
        }
        //System.out.println(newHeight);
        return newHeight;
    }

    public static int getNewWidth(int[] columnsToDelete, int[][] Matrix) {
        int newWidth = Matrix[0].length;
        for(int i = 0; i < columnsToDelete.length; i++) {
            if(columnsToDelete[i] != -1) {
                newWidth--;
            }
        }
        //System.out.println(newWidth);
        return newWidth;
    }

    public static int[] addColumns(int[] columnsToDelete, int[][] Matrix) {
        int[] solution = new int[Matrix.length];
        for(int i = 0; i < columnsToDelete.length; i++) {
            if(columnsToDelete[i] >= 0) {
                int[] tempColumn = getSelectedColumn(i, Matrix);
                for(int k = 0; k < solution.length; k++) {
                    solution[k] = solution[k] + tempColumn[k];
                }
            }
        }
        //System.out.println(Arrays.toString(solution));
        return solution;
    }

    public static int[] getSColumns(int[] row, int[][] Matrix) {
        int[] columns = new int[Matrix[0].length];
        for(int i = 0; i < row.length; i++) { 
            if(row[i] == 1) {
                columns[i] = i;
            } else {
                columns[i] = -1;
            }
        }
        //System.out.println(Arrays.toString(columns));
        return columns;
    }

    public static int[] getSelectedColumn(int columnN, int[][] Matrix) {
        int column[] = new int[Matrix.length];
        for(int i = 0; i < Matrix.length; i++) {
            column[i] = Matrix[i][columnN];
        }
        return column;
    }

    public static int[] getSelectedRow(int rowN, int[][] Matrix) {
        int row[] = new int[Matrix[0].length];
        for(int i = 0; i < Matrix[0].length; i++) { 
            row[i] = Matrix[rowN][i];
        }
        return row;
    }

    public static int selectRow(int selectedColumn, int selectedRow, int[][] Matrix) {
        int row = -1;
        int counter = 0;
        for(int i = 0; i < Matrix.length; i++) { 
            if(Matrix[i][selectedColumn] == 1) {
                row = counter;
                counter++;
                if(row == selectedRow) {
                    row = i;
                    break;
                } else { row = -1; }
            } else { row = -1; }
        }
        //System.out.println(row);
        return row;
    }

    public static void resetField() {
        for (int i = 0; i < field.length; i++)
            for (int k = 0; k < field[i].length; k++)
                field[i][k] = 0;
    }

    private static int[][] loadMatrix(String fileName) {
        int[][] matrix = new int[getRows("matrix.csv")][getColumns("matrix.csv")];

        File file = new File(fileName);
        try {
            Scanner scanner = new Scanner(file);
            int line = 0;
            while (scanner.hasNextLine()) {
                String[] valuesS = scanner.nextLine().split(",");
                int[] valuesI = new int[getColumns("matrix.csv")];
                for(int i = 0; i < valuesS.length; i++) {
                    valuesI[i] = Integer.parseInt(valuesS[i]);
                    matrix[line][i] = valuesI[i];
                }
                line++;
            }
            //System.out.println("Matrix: " + Arrays.deepToString(matrix));
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        return matrix;
    }

    public static int getColumns(String fileName) {
        int columns = 0;
        File file = new File(fileName);
        try {
            Scanner scanner = new Scanner(file);
            columns = scanner.nextLine().split(",").length;
        } catch(Exception e) {
            e.printStackTrace();
        }
        return columns;
    }

    public static int getRows(String fileName) {
        int rows = 0;
        File file = new File(fileName);
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                scanner.nextLine();
                rows++;
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return rows;    
    }
}