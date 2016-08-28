package tetris;

import java.util.Set;

public class RotateLeftStrategy extends RotateStrategy {

	RotateLeftStrategy(Piece piece) {
		super(piece);
		}

	@Override
	void applyRotation(Set<Cell> cells, Axis x, Axis y) {
		for (Cell cell : cells) {
			int temp = cell.get(x);
			cell.set(x, cell.get(y));
			cell.set(y, -temp);
		}
	}
}
