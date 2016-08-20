package tetris;

public class MoveDownStrategy extends MoveStrategy {

	public MoveDownStrategy(Piece piece) {
		super(piece);
		super.setdX(1);
	}

	@Override
	boolean move() {
		return super.doMove();
	}

}
