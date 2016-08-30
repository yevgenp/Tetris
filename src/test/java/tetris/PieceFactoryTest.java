package tetris;

import org.junit.Test;

import static org.junit.Assert.*;

public class PieceFactoryTest {

    private final PieceFactory pieceFactory = new PieceFactory(new TetrisTableModel());

    @Test
    public void shouldGetPieceAlwaysReturnOneOfPieceType() {
        //given
        Piece piece;
        Cell base = new Cell(3, 4);
        for (PieceType type : PieceType.values()) {
            //when
            piece = null;
            piece = pieceFactory.getPiece(type, base);
            //then
            assertNotNull(piece);
        }
    }

    @Test
    public void shouldGetPieceReturnCertainPieceType() {
        //given
        Piece piece;
        Cell base = new Cell(4, 3);
        //when
        piece = pieceFactory.getPiece(PieceType.ZPiece, base);
        //then
        assertFalse(piece instanceof SPiece);
        assertTrue(piece instanceof ZPiece);
    }
}