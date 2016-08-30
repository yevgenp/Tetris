package tetris;


enum PieceType {
	IPiece {
		@Override
		Piece get() {
			return new IPiece(null, Piece.getDefaultCell());
		}
	},
	JPiece {
		@Override
		Piece get() {
			return new JPiece(null, Piece.getDefaultCell());
		}
	},
	LPiece {
		@Override
		Piece get() {
			return new LPiece(null, Piece.getDefaultCell());
		}
	},
	OPiece {
		@Override
		Piece get() {
			return new OPiece(null, Piece.getDefaultCell());
		}
	},
	SPiece {
		@Override
		Piece get() {
			return new SPiece(null, Piece.getDefaultCell());
		}
	},
	TPiece {
		@Override
		Piece get() {
			return new TPiece(null, Piece.getDefaultCell());
		}
	},
	ZPiece {
		@Override
		Piece get() {
			return new ZPiece(null, Piece.getDefaultCell());
		}
	};

	abstract Piece get();
}
