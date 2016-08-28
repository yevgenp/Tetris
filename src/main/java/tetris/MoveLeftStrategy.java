package tetris;

public class MoveLeftStrategy extends MoveStrategy {

	public MoveLeftStrategy(Piece piece) {
		super(piece);
	}

	@Override
    boolean move(Axis axis) {
        super.set(axis, -1);
        return super.doMove(axis);
    }

}
