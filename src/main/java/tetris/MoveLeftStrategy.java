package tetris;

public class MoveLeftStrategy extends MoveStrategy {

	public MoveLeftStrategy(Piece piece) {
		super(piece);
		super.setdY(-1);
	}

	@Override
	boolean move() {
		return super.doMove();
	}

}
