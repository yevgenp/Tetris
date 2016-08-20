package tetris;

import java.awt.Color;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class TetrisTabelModel extends AbstractTableModel {

	private Color[][] values;

	public TetrisTabelModel() {
		values = new Color[TetrisPanel.TABLE_ROW_NUM][TetrisPanel.TABLE_COL_NUM];
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

	public Color getValueAt(Cell c) {
		return values[c.getX()][c.getY()];
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		values[rowIndex][columnIndex] = (Color) aValue;
		fireTableCellUpdated(rowIndex, columnIndex);
	}

	public void setValueAt(Color color, Cell c) {
		values[c.getX()][c.getY()] = color;
		fireTableCellUpdated(c.getX(), c.getY());
	}
}