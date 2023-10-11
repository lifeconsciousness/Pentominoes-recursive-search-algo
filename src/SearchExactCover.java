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
        int[][] newMatrix;
        int columnNum = currentColumn;
        int selectedRowNum = selectRow(columnNum, currentRow, Matrix);
        if(selectedRowNum == -1) {
            System.out.println("WAAAAAAAAAAAAAAAA");
        } else {
            int[] selectedRow = getSelectedRow(selectedRowNum, Matrix);
            int[] columnsToDelete = getSColumns(selectedRow, Matrix);
            int[] rowsToDelete = addColumns(columnsToDelete, Matrix);

            newMatrix = new int[][];
            System.out.println(Arrays.toString(columnsToDelete));
        }
        
        
        //int[] columnsToDelete = getcolumnsToDelete(selectedRow, Matrix);

        
        return newMatrix;
    }

    public static int[] getNewHeight() {
        
    }

    public static int[] getNewWidth() {
        
    }

    public static int[] addColumns(int[] columnsToDelete, int[][] Matrix) {
        int newLength = columnsToDelete.length;
        for(int i = 0; i < columnsToDelete.length; i++) {
            if(columnsToDelete[i] == -1) {
                newLength--;
            }
        }
        int[] solution = new int[Matrix.length];
        for(int i = 0; i < Matrix[0].length; i++) {
            for(int k = 0; k < solution.length; k++) {
                if(columnsToDelete[i] >= 0) {
                    solution[k] = solution[k] + getSelectedColumn(i, Matrix)[k];
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
        int column[] = new int[Matrix[0].length];
        for(int i = 0; i < Matrix[columnN].length; i++) { 
            column[i] = Matrix[i][columnN];
        }
        return column;
    }

    public static int[] getSelectedRow(int rowN, int[][] Matrix) {
        int row[] = new int[Matrix[0].length];
        for(int i = 0; i < Matrix.length; i++) { 
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