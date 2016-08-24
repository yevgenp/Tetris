package tetris;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

import static tetris.Constants.TABLE_COL_NUM;
import static tetris.Constants.TABLE_ROW_NUM;

enum PieceType {
	IPiece, JPiece, LPiece, OPiece,
	SPiece, TPiece, ZPiece
}

abstract class Piece {
	static final int PIECE_LENGTH = 4;
	static final Cell DEFAULT_BASE_CELL = new Cell(0, TABLE_COL_NUM / 2 - 1);

	private Color color;
	private Cell base = DEFAULT_BASE_CELL;
	private Set<Cell> cells;
	private TetrisMatrix model;

	public Piece(TetrisMatrix model, Cell base) {
		setModel(model);
		setBase(base);
		init();
	}

	public abstract void init();

	Set<Cell> getCells() {
		return cells;
	}

	protected void setCells(Set<Cell> cells) {
		if(cells.size() != PIECE_LENGTH) {
			throw new IllegalArgumentException("Piece should be " 
					+ PIECE_LENGTH + " pieces long.");
		}
		else {
			this.cells = cells;
		}
	}

	public Cell getBase() {
		return base;
	}

	public void setBase(Cell base) {
		if (base.getX() < 0 | base.getX() >= TABLE_ROW_NUM
				| base.getY() < 0 | base.getY() >= TABLE_COL_NUM) {
			throw new IllegalArgumentException("Base cell coordinates are illegal.");
		}
		else {
			this.base = base;
		}
	}

	public TetrisMatrix getModel() {
		return model;
	}

	public void setModel(TetrisMatrix model) {
		this.model = model;
	}

	public Color getColor() {
		return this.color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	boolean putOnModel() {
		for (Cell cell : cells)
			if (model.getValueAt(cell) != model.getEmptyCellColor()) {
				return false;
			}
			else {
				model.setValueAt(getColor(), cell);
			}
		return true;
	}

	boolean move(MoveStrategy strategy) {
		return strategy.move();
	}

	public boolean rotate(RotateStrategy strategy) {
		return strategy.rotate();
	}

	boolean isSpaceAvailable(Set<Cell> newCells) {
		if(newCells.size() != PIECE_LENGTH) {
			throw new IllegalArgumentException("Illegal piece length.");
		}
		Set<Cell> temp = new HashSet<>(newCells);
		for (Cell cell : cells) {
			temp.remove(cell);
		}
		for (Cell cell : temp) {
			if(isOutOfModel(cell)) {
				return false;
			}
			if (model.getValueAt(cell) != model.getEmptyCellColor()) {
				return false;
			}
		}
		return true;
	}

	static boolean isOutOfModel(Cell cell) {
		return cell.getX() < 0 | cell.getY() < 0
				| cell.getX() > (TABLE_ROW_NUM - 1)
				| cell.getY() > (TABLE_COL_NUM - 1);
	}

	void changeModelAndCells(Set<Cell> newCells) {
		Set<Cell> temp = new HashSet<>(this.cells);
		for (Cell cell : newCells) {
			temp.remove(cell);
		}
		for (Cell cell : temp)
			model.setValueAt(model.getEmptyCellColor(), cell);
		for (Cell newCell : newCells)
			model.setValueAt(this.getColor(), newCell);
		setCells(newCells);
	}

	int getTopX() {
		int topX = TABLE_ROW_NUM;
		for (Cell cell : getCells()) {
			topX = Math.min(topX, cell.getX());
		}
		return topX;
	}

	int getLowX() {
		int lowX = 0;
		for (Cell cell : getCells()) {
			lowX = Math.max(lowX, cell.getX());
		}
		return lowX;
	}
}