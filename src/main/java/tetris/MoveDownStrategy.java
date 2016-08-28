package tetris;

import static tetris.Axis.X;

public class MoveDownStrategy extends MoveStrategy {

	public MoveDownStrategy(Piece piece) {
		super(piece);
	}

	@Override
    boolean move(Axis axis) {
        super.set(X, 1);
        return super.doMove(X);
    }

}
