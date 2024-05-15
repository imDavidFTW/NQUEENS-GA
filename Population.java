import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Collections;

public class Population {
    private ArrayList<Chromosome> population;
    private int tf; //total fitness
    private double af; //average fitness

    public Population(int populationSize, int chromosomeLength){
        initializePopulation(populationSize, chromosomeLength);
    }

    public void initializePopulation(int populationSize, int chromosomeLength){
        population = new ArrayList<Chromosome>(populationSize);
        for(int i = 0; i < populationSize; i++){
            Chromosome c = new Chromosome(chromosomeLength);
            population.add(i, c);
        }
        reShuffle();
        tf = 0;
        for(Chromosome c : population){
            tf += c.getFitness();
        }
        af = (double) tf / populationSize;
    }

    // for the initial population I want to start with a unique 
    //population, so I shuffle till the values are unique
    public void reShuffle(){
        boolean all_unique = true;
        for(int i = 0; i < population.size(); i++){
            for(int j = i + 1; j < population.size(); j++){
                if(population.get(i).isEqual(population.get(j))){
                    population.get(j).reShuffle();
                    all_unique = false;
                }
            }
        }
        if(!all_unique){
            reShuffle();
        }
        return;
    }

    // mating pool being found by using deterministic algorithm
    // keeps population size the same and shuffles the result
    public ArrayList<Chromosome> deterministicMatingPool(){
        // first calculate expexcted values
        int n = population.size();
        int i = 0;
        double[] expVal = new double[population.size()];
        for(Chromosome c : population){
            expVal[i] = ((double) c.getFitness() * n) / tf;
            i+=1;
        }

        //next we calculate the mating pool (mp)
        ArrayList<Chromosome> mp = new ArrayList<>(n);
        i = 0;
        int maxIdx;
        while(i < n){
            maxIdx = max(expVal);
            mp.add(population.get(maxIdx));
            expVal[maxIdx] = expVal[maxIdx] >= 1 ? expVal[maxIdx] - 1 : 0;
            i+=1;
        }
        Collections.shuffle(mp);
        return mp;
    }

    public ArrayList<Chromosome> tournamentMatingPool(double tournamentSize){
        ArrayList<Integer> indexes = new ArrayList<Integer>();
        for(int i = 0; i < population.size(); i++){
            indexes.add(i);
        }
        Collections.shuffle(indexes);
        ArrayList<Chromosome> mp = new ArrayList<>();
        while(mp.size() < population.size()){
            Chromosome c = null;
            int maximum = 0;
            for(int i = 0; i < (int)(population.size() * tournamentSize); i++){
                Chromosome temp = population.get(indexes.get(i));
                int fitnessTemp = temp.getFitness();
                if(fitnessTemp > maximum){
                    maximum = fitnessTemp;
                    c = temp;
                }
            }
            mp.add(c);
        }
        return mp;
    }

    public void mateAndReplace(ArrayList<Chromosome> mp){
        ArrayList<Chromosome> newPopulation = new ArrayList<Chromosome>(Arrays.asList(new Chromosome[mp.size()]));
        for(int i = 0; i < mp.size(); i+=2){
            if(i == mp.size() - 1){
                Chromosome[] children = crossOver(mp.get(i), mp.get(0));
                newPopulation.set(i, children[0]);
            }
            else{
                Chromosome[] children = crossOver(mp.get(i), mp.get(i+1));
                newPopulation.set(i, children[0]);
                newPopulation.set(i + 1, children[1]);
            }
        }

        for(Chromosome c : newPopulation){
            mutation(c);
        }

        population = newPopulation;
    }

    private int max(double[] expVal) {
        double maximum = 0;
        int idx = 0;
        for(int i = 0; i < population.size(); i++){
            if(expVal[i] > maximum){
                maximum = expVal[i];
                idx = i;
            } 
        }
        return idx;
    }

    // for now we do 2 points for k-point crossover
    public Chromosome[] crossOver(Chromosome c1, Chromosome c2){
        int n = c1.getC().size();
        int min = 0; // Minimum value of range
        int max = n - 1; // Maximum value of range
        ArrayList<Integer> newC1 = new ArrayList<Integer>(Arrays.asList(new Integer[n]));
        ArrayList<Integer> newC2 = new ArrayList<Integer>(Arrays.asList(new Integer[n]));
        HashSet<Integer> setC1 = new HashSet<>();
        HashSet<Integer> setC2 = new HashSet<>();
        int point1 = (int)Math.floor(Math.random() * ((max - 1) - min + 1) + min);
        int point2 = (int)Math.floor(Math.random() * (max - point1 + 1) + point1);
        while(point2 == point1){
            point2 = (int)Math.floor(Math.random() * (max - point1 + 1) + point1);
        }
        for(int i = point1; i <= point2; i++){
            newC1.set(i, c2.getC().get(i));
            newC2.set(i, c1.getC().get(i));
            setC1.add(c2.getC().get(i));
            setC2.add(c1.getC().get(i));
        }
        int idx1 = 0;
        int idx2 = 0;
        for(int i = 0; i < n; i++){
            int v1 = c1.getC().get(i);
            int v2 = c2.getC().get(i);
            idx1 = idx1 == point1 ? point2 + 1 : idx1;
            idx2 = idx2 == point1 ? point2 + 1 : idx2;
            if(!setC1.contains(v1)){
                newC1.set(idx1, v1);
                idx1++;
            }
            if(!setC2.contains(v2)){
                newC2.set(idx2, v2);
                idx2++;
            }
        }
        return new Chromosome[] {new Chromosome(n, newC1), new Chromosome(n, newC2)};
    }

    public void mutation(Chromosome c){
        if(Math.random() <= 0.01){
            ArrayList<Integer> arr = c.getC();
            int point1 = (int)Math.floor(Math.random() * ((arr.size() - 1) + 1));
            int point2 = (int)Math.floor(Math.random() * ((arr.size() - 1) + 1));
            while(point2 == point1){
                point2 = (int)Math.floor(Math.random() * ((arr.size() - 1) + 1));
            }
            int temp = arr.get(point1);
            arr.set(point1, arr.get(point2));
            arr.set(point2, temp);
            c.setFitness(c.calculateFitness());
        }
    }

    public int findMaxFitness(){
        int maximum = 0;
        for(Chromosome c: population){
            if(c.getFitness() > maximum){
                maximum = c.getFitness();
            }
        }
        return maximum;
    }

    @Override
    public String toString(){
        StringBuilder s = new StringBuilder();
        for(int i = 0; i < population.size(); i++){
            s.append(population.get(i).toString());
            s.append("\n");
        }
        return s.toString();
    }
}

//method to determine gene pool to choose from
//use gene pool for crossover
//add crossover function of some sort
//add mutation
//cycle x iterations