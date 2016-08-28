package tetris;

public interface AbstractCell {

    int get(Axis axis);

    void set(Axis axis, int value);

    int hashCode();

    boolean equals(Object o);
}

enum Axis {
    X {
        @Override
        int max() {
            return 20;
        }
    },
    Y {
        @Override
        int max() {
            return 10;
        }
    },
    Z {
        @Override
        int max() {
            return 10;
        }
    };

    abstract int max();
}
