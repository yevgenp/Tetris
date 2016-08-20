package tetris;

import java.util.HashSet;
import java.util.Set;

abstract class MoveStrategy {
	private int dX = 0, dY = 0;
	private Piece piece;
	
	MoveStrategy(Piece piece) {
		this.piece = piece;
	}
	
	public int getdX() {
		return dX;
	}
	public void setdX(int dX) {
		this.dX = dX;
	}
	public int getdY() {
		return dY;
	}
	public void setdY(int dY) {
		this.dY = dY;
	}
	
	abstract boolean move();
	
	protected boolean doMove() {
		Set<Cell> newCells = new HashSet<>(); 
		for (Cell cell : piece.getCells()) 
			newCells.add(new Cell(cell.getX() + dX, cell.getY() + dY));			
		if(!piece.isSpaceAvailable(newCells)) {
			return false;
		}
		piece.changeModelAndCells(newCells);
		Cell base = piece.getBase();
		piece.setBase((new Cell(base.getX() + dX, base.getY() + dY)));
		return true;
	}
}
