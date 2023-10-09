import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class MatixGen {
    public static void main(String[] args) {
        int[][] matrix = new int[72][5];
        
        //ran(matrix);
        try {
            toCSV(matrix);
        } catch(Exception e) {
            e.getStackTrace();
        }
    }
    
    public static void toCSV(int[][] matrix) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter("matrix.csv", "UTF-8");
        for(int i = 0; i < matrix[i].length; i++) {
            for(int j = 0; j < matrix.length; j++) {
                writer.print(matrix[j][i] + " ");
            }
            writer.println();
        }
        writer.close();
    }

    public static void ran(int[][] matrix) {
        for(int i = 0; i < matrix.length; i++) {
            for(int j = 0; j < matrix[i].length; j++) {
                matrix[i][j] = (int) Math.round(Math.random());
            }
        }
    }
}
