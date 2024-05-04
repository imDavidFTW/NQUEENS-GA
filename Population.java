import java.util.ArrayList;

public class Population {
    private ArrayList<Chromosome> population;
    public Population(int populationSize, int chromosomeLength){
        initializePopulation(populationSize, chromosomeLength);
    }

    public void initializePopulation(int populationSize, int chromosomeLength){
        population = new ArrayList<>();
        for(int i = 0; i < populationSize; i++){
            Chromosome c = new Chromosome(chromosomeLength);
            population.add(c);
        }
        reShuffle();
    }

    // for the initial population I want to start with a unique 
    //population, so I shuffle till the values are unique
    public void reShuffle(){
        boolean check = false;
        for(int i = 0; i < population.size(); i++){
            for(int j = i + 1; j < population.size(); j++){
                if(population.get(i).isEqual(population.get(j))){
                    population.get(j).reShuffle();
                    check = true;
                }
            }
        }
        if(check){
            reShuffle();
        }
        return;
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
//cycle x iterations