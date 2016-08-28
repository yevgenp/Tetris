package tetris;

import java.util.HashSet;
import java.util.Set;

public interface TetrisMatrix {

    boolean isPresent(Cell c);

    void put(Cell c);

    void reset();

    void remove(Cell cell);

    boolean putOnModel(Piece p);

    int addPieceAndShift(Piece piece);

    static void isValid(Cell cell) {
        if (cell.getCoord().size() != Axis.values().length) {
            throw new IllegalArgumentException("Cell coordinates number is illegal for current model.");
        }
        if (isOutOfModel(cell)) {
            throw new IllegalArgumentException("Cell coordinates are out of model.");
        }
    }

    static boolean isOutOfModel(Cell cell) {
        boolean isOut = false;
        for (Axis axis : Axis.values())
            if (cell.get(axis) < 0 || cell.get(axis) >= axis.max()) {
                isOut = true;
                break;
            }
        return isOut;
    }

    default void changeModelAndCells(Piece piece, Set<Cell> newCells) {
        Set<Cell> oldCells = new HashSet<>(piece.getCells());
        newCells.forEach(oldCells::remove);
        oldCells.forEach(this::remove);
        newCells.forEach(this::put);
        piece.setCells(newCells);
    }
}