package tetris;

import lombok.Data;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

import static tetris.Axis.*;
import static tetris.TetrisMatrix.isOutOfModel;

@Data
abstract class Piece {
	static final int PIECE_LENGTH = 4;

	private Color color;
	private Cell base = getDefaultCell();
	private Set<Cell> cells;
	private TetrisMatrix model;

	public Piece(TetrisMatrix model, Cell base) {
		setModel(model);
		setBase(base);
		init();
	}

	public abstract void init();

	static public Cell getDefaultCell() {
		return new Cell(0, Y.max() / 2 - 1);
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

	public void setBase(Cell base) {
		if (isOutOfModel(base)) {
			throw new IllegalArgumentException("Base cell coordinates are illegal.");
		}
		else {
			this.base = base;
		}
	}

	boolean move(MoveStrategy strategy, Axis axis) {
		return strategy.move(axis);
	}

	public boolean rotate(RotateStrategy strategy, Axis x, Axis y) {
		return strategy.rotate(x, y);
	}

	boolean isSpaceAvailable(Set<Cell> newCells) {
		if(newCells.size() != PIECE_LENGTH) {
			throw new IllegalArgumentException("Illegal piece length.");
		}
		Set<Cell> temp = new HashSet<>(newCells);
		cells.forEach(temp::remove);
		for (Cell cell : temp) {
			if (isOutOfModel(cell) || model.isPresent(cell)) {
				return false;
			}
		}
		return true;
	}

	int getTopX() {
		int topX = X.max();
		for (Cell cell : getCells()) {
			topX = Math.min(topX, cell.get(X));
		}
		return topX;
	}

	int getLowX() {
		int lowX = 0;
		for (Cell cell : getCells()) {
			lowX = Math.max(lowX, cell.get(X));
		}
		return lowX;
	}
}