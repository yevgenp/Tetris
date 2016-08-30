package tetris;


public class PieceFactory {

	private final TetrisMatrix model;

	public PieceFactory(TetrisMatrix model) {
		this.model = model;
	}

	Piece getPiece(PieceType type, Cell base) {
		Piece piece = type.get();
		piece.setModel(model);
		piece.setBase(base);
		return piece;
	}

	public Piece getRandomPiece() {
		int index = (int) (PieceType.values().length * Math.random());
		return getPiece(PieceType.values()[index], Piece.getDefaultCell());
	}
}
