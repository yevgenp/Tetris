package tetris;

import lombok.Setter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Setter
abstract class MoveStrategy {

	final private Map<Axis, Integer> delta = new HashMap<>();
	private final Piece piece;
	
	MoveStrategy(Piece piece) {
		this.piece = piece;
	}

	abstract boolean move(Axis axis);

	protected void set(Axis axis, Integer d) {
		delta.put(axis, d);
	}


	protected boolean doMove(Axis axis) {
		Set<Cell> newCells = new HashSet<>();
		for (Cell cell : piece.getCells()) {
			Cell newCell = cell.clone();
			newCell.set(axis, cell.get(axis) + delta.get(axis));
			newCells.add(newCell);
		}
		if(!piece.isSpaceAvailable(newCells)) {
			return false;
		}
		piece.getModel().changeModelAndCells(piece, newCells);
		Cell base = piece.getBase();
		base.set(axis, base.get(axis) + delta.get(axis));
		return true;
	}
}
