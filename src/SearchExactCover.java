import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class SearchExactCover {


    public static int currentColumn = 0;
    public static int currentRow = 0;
    public String solution = "";
    public static void main(String[] args) {
        int[][] Matrix = loadMatrix("matrix.csv");
        exactCover(Matrix);
        //System.out.println(Arrays.toString(Matrix));
    }

    public static int[][] exactCover(int[][] Matrix) {
        int[][] FinalMatrix = new int[1][1];
        int columnNum = currentColumn;
        int selectedRowNum = selectRow(columnNum, currentRow, Matrix);
        if(selectedRowNum == -1) {
            System.out.println("WAAAAAAAAAAAAAAAA");
        } else {
            int[] selectedRow = getSelectedRow(selectedRowNum, Matrix);
            int[] columnsToDelete = getSColumns(selectedRow, Matrix);
            int[] rowsToDelete = addColumns(columnsToDelete, Matrix); // selected wrong need fix 

            int[][] newMatrix = new int[getNewHeight(rowsToDelete, Matrix)][getNewWidth(columnsToDelete, Matrix)]; // wrong size should be 2 4
            newMatrix = populateMatrix(rowsToDelete, columnsToDelete, Matrix, newMatrix);
            System.out.println(Arrays.deepToString(newMatrix));
        }
        
        
        //int[] columnsToDelete = getcolumnsToDelete(selectedRow, Matrix);

        
        return FinalMatrix;
    }

    public static int[][] populateMatrix(int[] rowsToDelete, int[] columnsToDelete, int[][] Matrix, int[][] newMatrix) {
        int rowCount = 0;
        int columnCount = 0;
        for(int i = 0; i < Matrix.length; i++) {
            if(rowsToDelete[i] == 0) {
                for(int k = 0; k < Matrix[i].length; k++) {
                    if(columnsToDelete[k] == -1) {
                        newMatrix[rowCount][columnCount] = Matrix[i][k];
                        columnCount++;
                    }
                }
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
        System.out.println(newHeight);
        return newHeight;
    }

    public static int getNewWidth(int[] columnsToDelete, int[][] Matrix) {
        int newWidth = Matrix[0].length;
        for(int i = 0; i < columnsToDelete.length; i++) {
            if(columnsToDelete[i] == -1) {
                newWidth--;
            }
        }
        System.out.println(newWidth);
        return newWidth;
    }

    public static int[] addColumns(int[] columnsToDelete, int[][] Matrix) {
        int[] solution = new int[Matrix.length];
        for(int i = 0; i < columnsToDelete.length; i++) {
            if(columnsToDelete[i] >= 0) {
                for(int k = 0; k < solution.length; k++) {
                    

                    int[] tempColumn = getSelectedColumn(i, Matrix); //keep looking
                    solution[k] = solution[k] + tempColumn[k];
                }
            }
        }
        System.out.println(Arrays.toString(solution));
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
        for(int i = 0; i < Matrix[0].length; i++) {
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
                }
            } else { row = -1; }
        }
        //System.out.println(row);
        return row;
    }

    public static void cutBranch() {
        // TODO implement
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