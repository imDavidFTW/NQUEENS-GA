import java.util.ArrayList;
import java.util.Collections;
//Chromosome 

public class Chromosome{
    private ArrayList<Integer> c;
    private int fitness;
    private int n;

    public Chromosome(int n){
        this.n = n;
        initializeChromosome();
        calculateFitness();
    }

    public Chromosome(int n, ArrayList<Integer> c){
        this.n = n;
        this.c = c;
        calculateFitness();
    }

    public int getFitness(){
        return fitness;
    }

    public ArrayList<Integer> getC(){
        return c;
    }

    public void initializeChromosome(){
        c = new ArrayList<Integer>();
        for(int i = 0; i < n; i++){
            c.add(i);
        }
        Collections.shuffle(c);
    }

    public void reShuffle(){
        Collections.shuffle(c);
        calculateFitness();
    }

    public void calculateFitness(){
        // fitness = n(n-1)/2 maximum intersections of queens - intersecting
        //1302
        //row 0 col 1 , row 1 col 3, ...
        fitness = n*(n-1)/2;
        int intersecting = 0;
        for(int i = 0; i < n; i++){
            for(int j = i + 1; j < n; j++){
                if(intersects(c.get(i), i, c.get(j), j)){
                    intersecting+=1;
                }
            }
        }
        fitness -= intersecting;
    }

    public boolean intersects(int x1, int y1, int x2, int y2){
        if(Math.abs((y2-y1)/(x2-x1)) == 1){
            return true;
        }
        return false;
    }

    public boolean isEqual(Chromosome c2){
        int count = 0;
        for(int i = 0; i < c.size(); i++){
            if(c.get(i) == c2.c.get(i)){
                count+=1;
            }
        }
        return count == c.size() && c2.c.size() == c.size();
    }

    @Override
    public String toString(){
        StringBuilder s = new StringBuilder();
        for(int i = 0; i < n; i++){
            s.append("" + c.get(i));
        }
        s.append(": " + fitness);
        return s.toString();
    }
}