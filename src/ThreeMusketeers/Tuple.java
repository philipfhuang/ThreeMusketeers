package ThreeMusketeers;

public class Tuple implements Comparable<Tuple>{

    public final String name;
    public final int steps;

    public Tuple (String name, int steps) {
        this.name = name;
        this.steps = steps;
    }

    @Override
    public int compareTo(Tuple o) {
        if (this.steps == o.steps) {
            return 0;

        }
        else if (this.steps > o.steps) {
            return 1;
        }
        return -1;
    }
}