package tetris;

public class MoveRightStrategy extends MoveStrategy {

	public MoveRightStrategy(Piece piece) {
		super(piece);
		super.setdY(1);
	}

	@Override
	boolean move() {
		return super.doMove();
	}

}
