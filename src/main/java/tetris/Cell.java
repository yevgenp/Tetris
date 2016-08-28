package tetris;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
final class Cell implements AbstractCell {

    final private Map<Axis, Integer> coord = new HashMap<>();

    public Cell() {
    }

    public Cell(int... coord) {
        if (coord.length > Axis.values().length) {
            throw new IllegalArgumentException("Too much arguments for Cell");
        }
        int i = 0;
        while (i < coord.length) {
            set(Axis.values()[i], coord[i]);
            i++;
        }
        for (int j = i; j < Axis.values().length; j++) {
            set(Axis.values()[j], 0);
        }
    }

    @Override
    public Cell clone() {
        Cell cell = new Cell();
        for (Axis axis : Axis.values()) {
            cell.set(axis, get(axis));
        }
        return cell;
    }

    @Override
    public int get(Axis axis) {
        Integer value = coord.get(axis);
        if (value == null) {
            throw new IllegalArgumentException("Axis of supplied type is not set.");
        }
        return value;
    }

    @Override
    public void set(Axis axis, int value) {
        coord.put(axis, value);
    }
}
