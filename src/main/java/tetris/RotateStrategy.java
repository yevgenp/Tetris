package tetris;

import java.util.HashSet;
import java.util.Set;

public abstract class RotateStrategy {
	private Piece piece;

	RotateStrategy(Piece piece) {
		this.piece = piece;
	}

	abstract void applyRotation(Set<Cell> cells);

	protected boolean rotate() {
		Set<Cell> newCells = new HashSet<>();
		for (Cell cell : piece.getCells())
			newCells.add(cell.clone());
		applyCoordinatesTranslation(newCells);
		applyRotation(newCells);
		revertCoordinatesTranslation(newCells);
		if (!piece.isSpaceAvailable(newCells))
			return false;
		piece.changeModelAndCells(newCells);
		return true;
	}

	protected void applyCoordinatesTranslation(Set<Cell> cells) {
		Cell base = piece.getBase();
		for (Cell cell : cells) {
			cell.setX(-(cell.getX() - base.getX()));
			cell.setY(cell.getY() - base.getY());
		}
	}

	protected void revertCoordinatesTranslation(Set<Cell> cells) {
		Cell base = piece.getBase();
		for (Cell cell : cells) {
			cell.setX(-cell.getX() + base.getX());
			cell.setY(cell.getY() + base.getY());
		}
	}
}
