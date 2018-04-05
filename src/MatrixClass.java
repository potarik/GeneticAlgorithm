import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class MatrixClass {
    public String path;
    public int size;
    public int[][] locations;
    public int[][] flows;

    public MatrixClass(String path){
        this.path = path;
    }

    public void read(){
        try {
            File input = new File(path);
            Scanner scanner = new Scanner(input);
            size = scanner.nextInt();
            locations = new int[size][size];
            flows = new int [size][size];

            int row = 0;
            while (row != size) {
                for(int col = 0; col < size; col++) {
                    locations[row][col] = scanner.nextInt();

                }
                row++;
            }

            int row1 = 0;
            while (row1 != size) {
                for(int col = 0; col < size; col++) {
                    flows[row1][col] = scanner.nextInt();

                }
                row1++;
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error Reading File");
            e.printStackTrace();
        }
    }

    public void toConsole(int[][] matrix){
        for (int i = 0; i < size; i++){
            for (int q = 0; q < size; q++){
                System.out.print(matrix[i][q] + " ");
            }
            System.out.println();
        }
    }
}
