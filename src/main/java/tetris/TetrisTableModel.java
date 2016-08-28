package tetris;

import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import static tetris.Axis.X;
import static tetris.Axis.Y;

@SuppressWarnings("serial")
public class TetrisTableModel extends AbstractTableModel implements TetrisMatrix {

    static final Color EMPTY_CELL_COLOR = Color.WHITE;

    private final Color[][] values;

    public TetrisTableModel() {
        values = new Color[X.max()][Y.max()];
    }

    @Override
    public int getRowCount() {
        return values.length;
    }

    @Override
    public int getColumnCount() {
        return values[0].length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return null;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return Color.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return values[rowIndex][columnIndex];
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        values[rowIndex][columnIndex] = (Color) aValue;
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    @Override
    public void put(Cell c) {
        throw new RuntimeException("Unimplemented.");
    }

    @Override
    public void reset() {
        for (int i = 0; i < getRowCount(); i++)
            for (int j = 0; j < getColumnCount(); j++) {
                setValueAt(EMPTY_CELL_COLOR, i, j);
            }
        fireTableDataChanged();
    }

    @Override
    public void remove(Cell cell) {
        setValueAt(EMPTY_CELL_COLOR, cell.get(X), cell.get(Y));
    }

    @Override
    public boolean putOnModel(Piece piece) {
        for (Cell cell : piece.getCells())
            if (isPresent(cell)) {
                return false;
            } else {
                setValueAt(piece.getColor(), cell.get(X), cell.get(Y));
            }
        return true;
    }

    @Override
    public boolean isPresent(Cell c) {
        return values[c.get(X)][c.get(Y)] != EMPTY_CELL_COLOR;
    }

    @Override
    public void changeModelAndCells(Piece piece, Set<Cell> newCells) {
        Set<Cell> oldCells = new HashSet<>(piece.getCells());
        newCells.forEach(oldCells::remove);
        oldCells.forEach(this::remove);
        newCells.forEach((newCell) -> setValueAt(piece.getColor(), newCell.get(X), newCell.get(Y)));
        piece.setCells(newCells);
    }

    @Override
    public int addPieceAndShift(Piece piece) {
        int topX = piece.getTopX();
        int lowX = piece.getLowX();
        TreeSet<Integer> toErase = new TreeSet<>();
        //Find full layer to erase
        for (int i = lowX; i >= topX; i--) {
            boolean fullLayer = true;
            for (int j = Y.max() - 1; j >= 0; j--) {
                if (!isPresent(new Cell(i, j))) {
                    fullLayer = false;
                    break;
                }
            }
            if (fullLayer) {
                toErase.add(i);
            }
        }
        //Find first empty layer
        for (int i = topX; i >= 0; i--) {
            boolean emptyLine = true;
            for (int j = Y.max() - 1; j >= 0; j--) {
                if (getValueAt(i, j) != EMPTY_CELL_COLOR) {
                    emptyLine = false;
                    break;
                }
            }
            if (emptyLine) {
                topX = i + 1;
                break;
            }
        }
        //Erase full and shift all lines above
        if (toErase.size() != 0) {
            int jump = 1;
            for (int i = toErase.last() - 1; i >= topX - jump; i--) {
                if (toErase.contains(i)) {
                    jump++;
                } else {
                    for (int j = 0; j < Y.max(); j++) {
                        Color color = (Color) getValueAt(i, j);
                        setValueAt(color, i + jump, j);
                    }
                }
            }
        }
        return toErase.size();
    }
}