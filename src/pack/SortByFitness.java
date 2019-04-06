package pack;

import java.util.Comparator;

public class SortByFitness implements Comparator<Monk> {

    public int compare(Monk a, Monk b){
        return b.fitness - a.fitness;
    }
}
