package tetris;

import java.util.Set;

public class RotateRightStrategy extends RotateStrategy {

	public RotateRightStrategy(Piece piece) {
		super(piece);
	}

	@Override
	void applyRotation(Set<Cell> cells) {
		for (Cell cell : cells) {
			int temp = cell.getX();
			cell.setX(-cell.getY());
			cell.setY(temp);
		}
	}
}
