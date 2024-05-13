import java.util.ArrayList;

public class Main {
    public static void main(String[] args){
        Population p = new Population(1000, 100);
        System.out.println(p);
        for(int i = 0; i < 1000; i++){
            ArrayList<Chromosome> matingPool = p.findMatingPool();
            p.mateAndReplace(matingPool);
            // System.out.println(p);
            System.out.println("max fitness at iteration " + i + ": " + p.findMaxFitness());
        }
    }
}
