import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class SearchExactCover {
    public static void main(String[] args) {
        int[][] data = loadData("matrix.csv");
        exactCover(data);
        //System.out.println(Arrays.toString(data));
    }

    public static int[][] exactCover(int[][] data) {
        int selectedColumn = sumColumns(data);
        int selectedRow = sumRows(selectedColumn, data);
        int[] selectedColumns = getSelectedColumns(selectedRow, data);
        if(selectedRow == -1) { 
            cutBranch();
        } else {

        }

        int[][] newData = data;
        return newData;
    }

    public static int[] getSelectedColumns(int selectedRow, int[][] data) {
        int[] selectedColumns = new int[1];
        System.out.println(Arrays.toString(data[selectedRow]));
        for(int i = 0; i < data[selectedRow].length; i++) {
            for(int k = 0; k < data.length; k++) {
                    if(data[selectedRow][i] == 1) {
                    System.out.println(data[k][i]);
                } 
            }
        }
        return selectedColumns;
    }

    public static int sumColumns(int[][] data) {
        int lowColumn = 0; // TODO look for least 1s
        int[] sums = new int[data[0].length];
        for(int i = 0; i < data[0].length; i++) {
            for(int k = 0; k < data.length; k++) {
                sums[i] = sums[i] + data[k][i];
            }
        }
        //System.out.println(Arrays.toString(sums));
        return lowColumn;
    }

    public static int sumRows(int selectedColumn, int[][] data) {
        int lowRow = 0;
        for(int i = 0; i < data.length; i++){  
            if(data[i][selectedColumn] == 1) {
                lowRow = i;
                break;
            } else { lowRow = -1; }
        }
        //System.out.println(lowRow);
        return lowRow;
    }

    public static void cutBranch() {
        // TODO implement
    }

    private static int[][] loadData(String fileName) {
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
            System.out.println(Arrays.deepToString(matrix));
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