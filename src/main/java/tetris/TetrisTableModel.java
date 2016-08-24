package tetris;

import javax.swing.table.AbstractTableModel;
import java.awt.*;

import static tetris.Constants.TABLE_COL_NUM;
import static tetris.Constants.TABLE_ROW_NUM;

@SuppressWarnings("serial")
public class TetrisTableModel extends AbstractTableModel implements TetrisMatrix {

    final private Color EMPTY_CELL_COLOR = Color.WHITE;

    private Color[][] values;

    public TetrisTableModel() {
        values = new Color[TABLE_ROW_NUM][TABLE_COL_NUM];
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
    public Class<?> getColumnClass(int columnIndex) {
        return Color.class;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return values[rowIndex][columnIndex];
    }

    @Override
    public Color getValueAt(Cell c) {
        return values[c.getX()][c.getY()];
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        values[rowIndex][columnIndex] = (Color) aValue;
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    @Override
    public void setValueAt(Color color, Cell c) {
        values[c.getX()][c.getY()] = color;
        fireTableCellUpdated(c.getX(), c.getY());
    }

    @Override
    public Color getEmptyCellColor() {
        return EMPTY_CELL_COLOR;
    }

    @Override
    public void reset() {
        for (int i = 0; i < getRowCount(); i++)
            for (int j = 0; j < getColumnCount(); j++) {
                setValueAt(getEmptyCellColor(), new Cell(i, j));
            }
        fireTableDataChanged();
    }
}