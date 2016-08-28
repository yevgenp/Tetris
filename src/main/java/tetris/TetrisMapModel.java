package tetris;

import java.util.*;

import static tetris.Axis.X;
import static tetris.TetrisMatrix.isValid;

public class TetrisMapModel implements TetrisMatrix {

    private final int MAX_NUMBER_OF_CELLS_IN_PLANE;

    //Map of X plane as a key, and its cells as a value
    private SortedMap<Integer, Set<Cell>> model;

    public TetrisMapModel() {
        model = new TreeMap<>(Collections.reverseOrder());

        int num = 1;
        for (int i = 1; i < Axis.values().length; i++) {
            num *= Axis.values()[i].max();
        }
        MAX_NUMBER_OF_CELLS_IN_PLANE = num;
    }

    @Override
    public void reset() {
        model = new TreeMap<>(Collections.reverseOrder());
    }

    @Override
    public void put(Cell cell) {
        isValid(cell);
        Integer x = cell.get(X);
        if (!model.containsKey(x)) {
            model.put(x, new HashSet<>());
        }
        model.get(x).add(cell);
    }

    @Override
    public void remove(Cell cell) {
        isValid(cell);
        Integer x = cell.get(X);
        if (model.containsKey(x)) {
            model.get(x).remove(cell);
            if (model.get(x).isEmpty()) {
                model.remove(x);
            }
        }
    }

    @Override
    public boolean putOnModel(Piece piece) {
        for (Cell cell : piece.getCells())
            if (isPresent(cell)) {
                return false;
            } else {
                put(cell);
            }
        return true;
    }

    @Override
    public boolean isPresent(Cell cell) {
        isValid(cell);
        Integer x = cell.get(X);
        return model.containsKey(x) && model.get(x).contains(cell);
    }

    @Override
    public int addPieceAndShift(Piece piece) {
        piece.getCells().forEach(this::put);
        //Find full plane, erase it, shift planes above
        int planesErased = 0;
        SortedMap<Integer, Set<Cell>> newPlanes = new TreeMap<>();
        for (Iterator<Map.Entry<Integer, Set<Cell>>> it = model.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<Integer, Set<Cell>> entry = it.next();
            Integer x = entry.getKey();
            if ((x <= piece.getLowX() || x >= piece.getTopX())
                    && entry.getValue().size() == MAX_NUMBER_OF_CELLS_IN_PLANE) {
                it.remove();
                planesErased++;
            } else if (planesErased > 0) {
                Integer newX = x + planesErased;
                Set<Cell> newCells = new HashSet<>();
                for (Cell cell : entry.getValue()) {
                    Cell newCell = cell.clone();
                    newCell.set(X, newX);
                    newCells.add(newCell);
                }
                it.remove();
                newPlanes.put(newX, newCells);
            }
        }
        model.putAll(newPlanes);
        return planesErased;
    }
}