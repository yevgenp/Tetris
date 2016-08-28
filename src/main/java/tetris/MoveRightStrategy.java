package tetris;

public class MoveRightStrategy extends MoveStrategy {

	public MoveRightStrategy(Piece piece) {
		super(piece);
	}

	@Override
    boolean move(Axis axis) {
        super.set(axis, 1);
        return super.doMove(axis);
    }

}
