import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class SearchExactCover {

    public static boolean flag = true;
    public String solution = ""; // TODO

    public static UI ui;
    public static int[][] field;

    public static void main(String[] args) {
        int[][] Matrix = loadMatrix("matrix.csv");
        //setup();
        exactCover(Matrix, 0, 0, setOldMatrix(Matrix), 0, 0);
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

    public static int[][] setOldMatrix(int[][] Matrix) {
        int[][] oldMatrix = new int[Matrix.length][Matrix[0].length]; //
        for(int i = 0; i < Matrix.length; i++) {
            for(int k = 0; k < Matrix[i].length; k++) {
                oldMatrix[i][k] = Matrix[i][k];
            }
        }
        return oldMatrix;
    }

    public static void exactCover(int[][] Matrix, int currentColumn, int currentRow, int[][] oldMatrix, int branchColumn, int branchRow) { // doesnt work for 3 deep problems because it is not fully recursive (mostlikely if(currentColumn < Matrix[0].length-1))
        if(flag) {
            currentColumn = 0;
            currentRow = 0;
            oldMatrix = new int[Matrix.length][Matrix[0].length]; //
            for(int i = 0; i < Matrix.length; i++) {
                for(int k = 0; k < Matrix[i].length; k++) {
                    oldMatrix[i][k] = Matrix[i][k];
                }
            }
            currentColumn = currentColumn + branchColumn;
            currentRow = currentRow + branchRow;
            flag = false;
        }
        int selectedRowNum = selectRow(currentColumn, currentRow, Matrix);
        int[] selectedRow = getSelectedRow(selectedRowNum, Matrix);
        int[] columnsToDelete = getSColumns(selectedRow, Matrix);
        int[] rowsToDelete = addColumns(columnsToDelete, Matrix); 

        int[][] newMatrix = new int[getNewHeight(rowsToDelete, Matrix)][getNewWidth(columnsToDelete, Matrix)];
        newMatrix = populateMatrix(rowsToDelete, columnsToDelete, Matrix, newMatrix);
        System.out.print(currentColumn + "," + currentRow + "(" + Arrays.toString(Matrix[currentRow]) + ") > ");
        checkSolution(currentColumn, currentRow, Matrix, newMatrix, branchColumn, branchRow, oldMatrix);
    }

    public static void checkSolution(int currentColumn, int currentRow, int[][] Matrix, int[][] newMatrix, int branchColumn, int branchRow, int[][] oldMatrix) {
        if(newMatrix.length == 0) {
            System.out.println();
            //System.out.println("Invalid solution - 0 rows left");
            int nextSelectedRowNum = selectRow(currentColumn, (currentRow+1), Matrix);
            if(nextSelectedRowNum == -1) {
                currentColumn++;
                currentRow = 0;
            } else {
                if(currentColumn < Matrix[0].length-1) {
                    //currentColumn = 0;
                    currentRow++;
                } else {
                    System.out.println("Dead branch");
                    flag = true;
                    if(branchRow < oldMatrix.length-1) {
                        branchRow++;
                    } 
                    int onesPerColumn = getOnesPerColumn(branchColumn, oldMatrix);
                    if(branchRow == onesPerColumn) {
                        branchRow = 0;
                        branchColumn++;
                    }

                    if(branchColumn == oldMatrix.length-1) {
                        System.out.println("YEAH");
                    }
                    
                    exactCover(oldMatrix, currentColumn, currentRow, oldMatrix, branchColumn, branchRow);
                }
            }
            exactCover(Matrix, currentColumn, currentRow, oldMatrix, branchColumn, branchRow);
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
                exactCover(Matrix, currentColumn, currentRow, oldMatrix, branchColumn, branchRow);          //  Check if it going to the next column or if I need to use the old matrix
            }
        } else { // Go to next level of depth
            //System.out.println(Arrays.deepToString(newMatrix));
            exactCover(newMatrix, 0, 0, oldMatrix, branchColumn, branchRow);
        }
    }

    public static int getOnesPerColumn(int column, int[][] Matrix) {
        int onesPerColumn = 0;
        for(int i = 0; i < Matrix.length; i++) {
            if(Matrix[i][column] == 1) {
                onesPerColumn++;  
            }
        }
        return onesPerColumn;
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