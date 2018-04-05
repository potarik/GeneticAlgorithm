import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Individual {
    private int[] permutationArray;
    private int cost;
    private int permutationSize;
    private MatrixClass matrixData = Main.matrixData;
    private double cross;
    private double mutat;

    public Individual (){
        permutationArray = new int[matrixData.size];
        this.permutationSize = matrixData.size;
        cross = -1;
        mutat = -1;
    }

    public Individual (int[] permutation){
        this.permutationArray = permutation;
        this.permutationSize = permutation.length;
        //evaluate();
        cross = -1;
        mutat = -1;
    }


    public Individual clone(){
        int[] array = new int[permutationSize];
        for (int i = 0; i < permutationSize; i++){
            array[i] = this.permutationArray[i];
        }
        Individual individualCloned = new Individual(array);
        return individualCloned;
    }


    public void generatePermutation(){
        ArrayList<Integer> arrayList = new ArrayList<>();
        int tmpNumber;
        while (arrayList.size() < permutationSize){
            tmpNumber = ThreadLocalRandom.current().nextInt(1, permutationSize + 1);
            if (!arrayList.contains(tmpNumber)){
                arrayList.add(tmpNumber);
            }
        }
        permutationArray = arrayList.stream().mapToInt(j -> j).toArray();
    }

    private void evaluate(){
        int tmp = 0;
        for (int i = 0; i < permutationSize; i++) {
            for (int j = 0; j < permutationSize; j++) {
                tmp = tmp + matrixData.locations[i][j] * matrixData.flows [permutationArray[i]-1] [permutationArray[j]-1];
            }
        }
        cost = tmp;
    }

    public int[] getPermutationArray() {
        return permutationArray;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int costToSet) {
        cost = costToSet;
    }

    public String toString(){
        String text = "";
        for (int i = 0; i < permutationSize; i++)
            text += "[" + permutationArray[i] + "] ";
        return text;
    }

    public double getMutat() {
        return mutat;
    }

    public void setMutat(double mutat) {
        this.mutat = mutat;
    }

    public double getCross() {
        return cross;
    }

    public void setCross(double cross) {
        this.cross = cross;
    }
}
