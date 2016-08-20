package tetris;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

final class IPiece extends Piece {

	IPiece(TetrisTabelModel model, Cell base) {
		super(model, base);
		setColor(Color.CYAN);
	}

	public void init() {
		int baseX = getBase().getX();
		int baseY = getBase().getY();
		Set<Cell> iniCells = new HashSet<>();
		iniCells.add(new Cell(baseX, baseY - 1));
		iniCells.add(new Cell(baseX, baseY));
		iniCells.add(new Cell(baseX, baseY + 1));
		iniCells.add(new Cell(baseX, baseY + 2));
		setCells(iniCells);
	}
}