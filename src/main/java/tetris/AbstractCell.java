package tetris;

public interface AbstractCell {

    int get(char coord);

    void set(char coord, int value);

    int hashCode();

    boolean equals(Object o);
}
