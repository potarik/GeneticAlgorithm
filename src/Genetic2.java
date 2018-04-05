
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Genetic2 {
    private int popSize = 100;
    private int numOfGenerations = 100;
    private int tournamentSize = 50;
    private double crossoverProbability = 0.7;
    private double mutationProbability = 0.01;

    private int matrixSize;
    private Individual population[];
    private Individual bestIndividual;
    private Individual actualBestIndividual;
    private Individual worstIndividual;
    private double avarageIndividual;
    private ArrayList<Integer> costsInPopulation;
    private MatrixClass matrixData;

    public Genetic2(MatrixClass matrixClass){
        matrixData = matrixClass;
        matrixSize = matrixData.size;
    }

    public void run(){
        int actualGeneration = 0;
        generatePop();
        bestIndividual = population[0];
        evaluate();
        toConsole(actualGeneration++);

        while (actualGeneration < numOfGenerations){
            tournamentSelection();
            crossover();
            mutation();
            evaluate2();
            toConsole(actualGeneration++);
        }
    }


    public void generatePop(){
        population = new Individual[popSize];

        for (int i = 0; i < popSize; i++){
            Individual tmp = new Individual();
            tmp.generatePermutation();
            population[i] = tmp;
        }
    }

    public void evaluate(){
        double sumOfCosts = 0;
        worstIndividual = population[0];
        actualBestIndividual = population[0];
        costsInPopulation = new ArrayList<>();

        for (int i = 0; i < popSize; i++){
            int costOfPermutation = getCost(population[i].getPermutationArray());
            population[i].setCost(costOfPermutation);
            costsInPopulation.add(costOfPermutation);
            if (population[i].getCost() < bestIndividual.getCost())
                bestIndividual = population[i];
            if (population[i].getCost() < actualBestIndividual.getCost())
                actualBestIndividual = population[i];
            if (population[i].getCost() > worstIndividual.getCost())
                worstIndividual = population[i];
            sumOfCosts += population[i].getCost();
        }



        avarageIndividual = sumOfCosts / popSize;


    }

    public void evaluate2(){
        double sumOfCosts = 0;
        worstIndividual = population[0];
        actualBestIndividual = population[0];
        costsInPopulation = new ArrayList<>();

        for (int i = 0; i < popSize; i++){
            int costOfCur = population[i].getCost();
            costsInPopulation.add(costOfCur);
            if (costOfCur < bestIndividual.getCost())
                bestIndividual = population[i];
            if (costOfCur < actualBestIndividual.getCost())
                actualBestIndividual = population[i];
            if (costOfCur > worstIndividual.getCost())
                worstIndividual = population[i];
            sumOfCosts += costOfCur;
        }

        avarageIndividual = sumOfCosts / popSize;
    }

    public void tournamentSelection(){
        Individual[] tmpPopulation = new Individual[popSize];
        int sizeOfNewGeneration = 0;

        while (sizeOfNewGeneration < popSize){
            ArrayList<Individual> roundArray = new ArrayList<>();
            for (int i = 0; i < tournamentSize; i++){
                int tmp = ThreadLocalRandom.current().nextInt(0, popSize);
                roundArray.add(population[tmp]);
            }
            Individual bestIndividualInRound = roundArray.get(0);

            for (int q = 1; q < tournamentSize; q++){
                if (roundArray.get(q).getCost() < bestIndividualInRound.getCost())
                    bestIndividualInRound = roundArray.get(q);
            }

            tmpPopulation[sizeOfNewGeneration] = bestIndividualInRound.clone();
            sizeOfNewGeneration++;
        }

        for (int i = 0; i < popSize; i++){
            population[i] = tmpPopulation[i];
        }

    }

    public void rouletteSelection(){
        int summaryCostOfPop = 0;
        int sizeOfNewPop = 0;
        Individual[] selectedPopulation = new Individual[popSize];

        for (int i = 0; i < popSize; i++){
            summaryCostOfPop += population[i].getCost();
        }

        int rand = ThreadLocalRandom.current().nextInt(0, summaryCostOfPop);
        int partialSum = 0;

        while (sizeOfNewPop < popSize){
            for (int i = 0; i < popSize; i++){
                partialSum += population[i].getCost();
                if (sizeOfNewPop == popSize)
                    break;
                if (partialSum >= rand)
                    selectedPopulation[sizeOfNewPop++] = population[i];
                if (i == popSize - 1)
                    i = 0;
            }
        }

    }

    private void crossover(){
        Individual[] populationAfterCrossing = new Individual[popSize];
        int sizeOfNewPopulation = 0;

        while (sizeOfNewPopulation < popSize){
            ArrayList<Individual> crossoverArrayList = new ArrayList<>();
            int tmp1 = ThreadLocalRandom.current().nextInt(0, popSize);
            int tmp2 = ThreadLocalRandom.current().nextInt(0, popSize);
            double percent = ThreadLocalRandom.current().nextDouble(0, 1);

            if (crossoverProbability >= percent){
                crossoverArrayList.add(population[tmp1].clone());
                crossoverArrayList.add(population[tmp2].clone());

                cross(crossoverArrayList);
                populationAfterCrossing[sizeOfNewPopulation++] = crossoverArrayList.get(0);
                populationAfterCrossing[sizeOfNewPopulation++] = crossoverArrayList.get(1);
            }

            evaluate();
        }

        for (int i = 0; i < popSize; i++)
            population[i] = populationAfterCrossing[i];
    }

    private void cross(ArrayList<Individual> pairList){
        int pivot = ThreadLocalRandom.current().nextInt(2, matrixSize - 1);

        Individual parent1 = pairList.get(0);
        Individual parent2 = pairList.get(1);

        Individual child1 = new Individual();
        Individual child2 = new Individual();

        for (int i = 0; i < matrixSize; i++){
            if (i < pivot){
                child1.getPermutationArray()[i] = parent2.getPermutationArray()[i];
                child2.getPermutationArray()[i] = parent1.getPermutationArray()[i];
            }else{
                child1.getPermutationArray()[i] = parent1.getPermutationArray()[i];
                child2.getPermutationArray()[i] = parent2.getPermutationArray()[i];
            }
        }


        fixOfReapiting(child1);
        fixOfReapiting(child2);

        pairList.set(0, child1);
        pairList.set(1, child2);

    }

    private void fixOfReapiting(Individual individual){
        ArrayList<Integer> arrayListToCheck = new ArrayList<>();
        ArrayList<Integer> indexesOfReapitingGenes = new ArrayList<>();
        ArrayList<Integer> absentGenes = new ArrayList<>();

        for (int i = 0; i < matrixSize; i++){
            if (arrayListToCheck.contains(individual.getPermutationArray()[i])){
                indexesOfReapitingGenes.add(i);
                arrayListToCheck.add(individual.getPermutationArray()[i]);
            }else
                arrayListToCheck.add(individual.getPermutationArray()[i]);
        }

        if (!indexesOfReapitingGenes.isEmpty()){
            for (int i = 1; i <= matrixSize; i++){
                if (!arrayListToCheck.contains(i))
                    absentGenes.add(i);
            }

            for (int i = 0; i < indexesOfReapitingGenes.size(); i++){
                individual.getPermutationArray()[indexesOfReapitingGenes.get(i)] = absentGenes.get(i);
            }
        }

    }

    private void mutation(){
        int sizeOfMutatedPopulation = 0;
        Individual[] mutatedPopulation = new Individual[popSize];
        int[] permutationToMutate;

        while (sizeOfMutatedPopulation < popSize){
            int currentIndividualIndex = ThreadLocalRandom.current().nextInt(0, popSize);
            if (population[currentIndividualIndex].getMutat() != -1) {
                double in = ThreadLocalRandom.current().nextDouble(0, 1);
                population[currentIndividualIndex].setMutat(in);
            }


            if (population[currentIndividualIndex].getMutat() <= mutationProbability){
                int firstGeneIndex = ThreadLocalRandom.current().nextInt(0, matrixSize);
                int secondGeneIndex = ThreadLocalRandom.current().nextInt(0, matrixSize);
                permutationToMutate = population[currentIndividualIndex].getPermutationArray();
                int firstGene = permutationToMutate[firstGeneIndex];
                permutationToMutate[firstGeneIndex] = permutationToMutate[secondGeneIndex];
                permutationToMutate[secondGeneIndex] = firstGene;
                Individual individual = new Individual(permutationToMutate);

                mutatedPopulation[sizeOfMutatedPopulation] = individual;
            }
            sizeOfMutatedPopulation++;
        }

        for (int i = 0; i < popSize; i++){
            population[i] = mutatedPopulation[i];
        }

        evaluate();
    }

    private int getCost(int[] perm){
        int cost = 0;
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                cost = cost + matrixData.locations[i][j] * matrixData.flows [perm[i]-1] [perm[j]-1];
            }
        }
        return cost;
    }

    public void toConsole(int actualGeneration){
        System.out.println("Generation " + actualGeneration++ + ": Best [" + bestIndividual.getCost() + "] BestOfGeneration [" + actualBestIndividual.getCost() + "]");

    }

    public MatrixClass getMatrixData(){
        return matrixData;
    }
}
