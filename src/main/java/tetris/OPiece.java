package tetris;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

import static tetris.Axis.X;
import static tetris.Axis.Y;

final class OPiece extends Piece {

	OPiece(TetrisMatrix model, Cell base) {
		super(model, base);
		setColor(Color.YELLOW);
	}

	public void init() {
		int baseX = getBase().get(X);
		int baseY = getBase().get(Y);
		Set<Cell> iniCells = new HashSet<>();
		iniCells.add(new Cell(baseX, baseY));
		iniCells.add(new Cell(baseX, baseY - 1));
		iniCells.add(new Cell(baseX + 1, baseY));
		iniCells.add(new Cell(baseX + 1, baseY - 1));
		setCells(iniCells);
	}

	@Override
	public boolean rotate(RotateStrategy strategy, Axis x, Axis y) {
		return true;
	}
}