import java.util.ArrayList;

public class Main {
    public static void main(String[] args){
        Population p = new Population(1000, 10);
        int maximum = 0;
        long startTime = System.currentTimeMillis();
        int i = 0;
        while(maximum != 45){
            ArrayList<Chromosome> matingPool = p.tournamentMatingPool(0.03);
            // ArrayList<Chromosome> matingPool = p.deterministicMatingPool();
            p.mateAndReplace(matingPool);
            // System.out.println(p);
            maximum = Math.max(maximum, p.findMaxFitness());
            System.out.println(maximum + " : "+ i);
            i++;
        }
        // Code to be measured
        long endTime = System.currentTimeMillis();

        // Calculate execution time in milliseconds
        long executionTime = endTime - startTime;
        System.out.println("Execution time: " + (executionTime) / 1000 + " seconds");

        System.out.println("Max fitness at iteration " + 10000 + ": " + maximum);
    }
}
