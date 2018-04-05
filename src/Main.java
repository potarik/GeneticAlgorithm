import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
    public static int size;
    public static int[][] locations;
    public static int[][] flows;
    public static MatrixClass matrixData;

    public static void main(String[] args) {
        matrixData = new MatrixClass("src/had18.txt");
        matrixData.read();
        size = matrixData.size;
        locations = matrixData.locations;
        flows = matrixData.flows;

        Genetic2 genetic2 = new Genetic2(matrixData);
        genetic2.run();
    }

    public static void greedySearch(int ilosc) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writerBest = new PrintWriter("greedy.txt", "UTF-8");
        PrintWriter writerAvg = new PrintWriter("greedyAvg.txt", "UTF-8");

        for (int q = 0; q < 10; q++){
            double sr = 0;
            int bestGreedy = 9999;
            for (int i = 0; i < ilosc; i++){
                ArrayList<Integer> perm = generatePermutation();
                int cost = 0;
                int curIndex = 0;


                while (curIndex < size){
                    int minCost = 99999;
                    int minNumber = -1;
                    for (int j = 0; j < size; j++) {
                        if (curIndex != j){
                            if (!perm.contains(j)){
                                cost = locations[0][j] * flows [perm.get(0)-1] [perm.get(j)-1];
                                if (cost < minCost){
                                    minCost = cost;
                                    minNumber = j;
                                }
                            }
                        }
                    }
                    perm.add(minNumber);
                    curIndex++;
                }

                if (getCost(perm) < bestGreedy)
                    bestGreedy = getCost(perm);
                sr += getCost(perm);
                System.out.println(getCost(perm));
            }
            writerBest.println(bestGreedy);
            writerAvg.println((int) (sr / ilosc));
        }



        writerBest.close();
        writerAvg.close();

    }

    public static void randomSearch(int k) throws FileNotFoundException, UnsupportedEncodingException {
        ArrayList tmpPermutation;
        ArrayList bestPermutation = generatePermutation();
        int tmpK = 0;
        PrintWriter writerBest = new PrintWriter("randomBest.txt", "UTF-8");
        PrintWriter writerRan = new PrintWriter("randomRan.txt", "UTF-8");


        while (tmpK < k){
            tmpPermutation = generatePermutation();
            writerRan.println(getCost(tmpPermutation));
            writerBest.println(getCost(bestPermutation));
            if (getCost(tmpPermutation) < getCost(bestPermutation)) {
                bestPermutation = tmpPermutation;
            }
            tmpK++;
        }
        writerBest.close();
        writerRan.close();

        System.out.println(bestPermutation);
        System.out.println(getCost(bestPermutation));
    }

    public static int getCost(ArrayList<Integer> perm){
        int cost = 0;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                cost = cost + locations[i][j] * flows [perm.get(i)-1] [perm.get(j)-1];
            }
        }

        return cost;
    }

    public static ArrayList generatePermutation (){
        ArrayList arrayList = new ArrayList();
        int actualSizeOfArray = 0;
        int tmpNumber;

        while(actualSizeOfArray != size){
            tmpNumber = ThreadLocalRandom.current().nextInt(1, size + 1);
            if (!arrayList.contains(tmpNumber)){
                arrayList.add(tmpNumber);
                ++actualSizeOfArray;
            }
        }

        return arrayList;
    }
}
