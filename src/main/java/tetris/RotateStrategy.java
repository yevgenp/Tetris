package tetris;

import java.util.Set;
import java.util.stream.Collectors;

public abstract class RotateStrategy {
	private final Piece piece;

	RotateStrategy(Piece piece) {
		this.piece = piece;
	}

	abstract void applyRotation(Set<Cell> cells, Axis x, Axis y);

	protected boolean rotate(Axis x, Axis y) {
		Set<Cell> newCells = piece.getCells().stream().map(Cell::clone).collect(Collectors.toSet());
		applyCoordinatesTranslation(newCells, x, y);
		applyRotation(newCells, x, y);
		revertCoordinatesTranslation(newCells, x, y);
		if (!piece.isSpaceAvailable(newCells))
			return false;
		piece.getModel().changeModelAndCells(piece, newCells);
		return true;
	}

	protected void applyCoordinatesTranslation(Set<Cell> cells, Axis x, Axis y) {
		Cell base = piece.getBase();
		for (Cell cell : cells) {
			cell.set(x, -(cell.get(x) - base.get(x)));
			cell.set(y, cell.get(y) - base.get(y));
		}
	}

	protected void revertCoordinatesTranslation(Set<Cell> cells, Axis x, Axis y) {
		Cell base = piece.getBase();
		for (Cell cell : cells) {
			cell.set(x, -cell.get(x) + base.get(x));
			cell.set(y, cell.get(y) + base.get(y));
		}
	}
}
