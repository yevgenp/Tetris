package tetris;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

enum Direction {
	DOWN, LEFT, RIGHT;
}

enum PieceType {
	IPiece, JPiece, LPiece, OPiece, 
	SPiece, TPiece, ZPiece;
}

abstract class Piece {
	static final int PIECE_LENGTH = 4;
	static final Cell DEFAULT_BASE_CELL = new Cell(0, TetrisPanel.TABLE_COL_NUM/2 - 1);
	private Color color;
	private Cell base = DEFAULT_BASE_CELL;
	private Set<Cell> cells;
	private TetrisTabelModel model;

	public Piece(TetrisTabelModel model, Cell base) {
		setModel(model);
		setBase(base);
		init();
	}

	public abstract void init();

	public Set<Cell> getCells() {
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
		if(base.getX() < 0 | base.getX() >= TetrisPanel.TABLE_ROW_NUM 
				| base.getY() < 0 | base.getY() >= TetrisPanel.TABLE_COL_NUM) {
			throw new IllegalArgumentException("Base cell coordinates are illegal.");
		}
		else {
			this.base = base;
		}
	}

	public TetrisTabelModel getModel() {
		return model;
	}

	public void setModel(TetrisTabelModel model) {
		this.model = model;
	}

	public Color getColor() {
		return this.color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public boolean putOnModel() {
		for (Cell cell: cells) 
			if(model.getValueAt(cell) != Color.WHITE) {
				return false;
			}
			else {
				model.setValueAt(this.getColor(), cell);
			}
		return true;
	};

	public boolean move(MoveStrategy strategy) {
		return strategy.move();
	}

	public boolean rotate(RotateStrategy strategy) {
		return strategy.rotate();
	}

	public boolean isSpaceAvailable(Set<Cell> newCells) {
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
			if(model.getValueAt(cell) != Color.WHITE) {
				return false;
			}
		}
		return true;
	}

	public static boolean isOutOfModel(Cell cell) {
		return cell.getX() < 0 | cell.getY() < 0 
				| cell.getX() > (TetrisPanel.TABLE_ROW_NUM -1) 
				| cell.getY() > (TetrisPanel.TABLE_COL_NUM -1);
	}

	public void changeModelAndCells(Set<Cell> newCells) {
		Set<Cell> temp = new HashSet<Cell>(this.cells);
		for (Cell cell : newCells) {
			temp.remove(cell);
		}
		for (Cell cell : temp)
			model.setValueAt(Color.WHITE, cell);
		for (Cell newCell : newCells)
			model.setValueAt(this.getColor(), newCell);
		setCells(newCells);
	};
}