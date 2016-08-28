package tetris;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class TetrisPanelTest {
    private final TetrisPanel panel = new TetrisPanel();

    @Test
    public void shouldGetPieceAlwaysReturnOneOfPieceType() {
        //given
        Piece piece;
        Cell base = new Cell(3, 4);
        for (PieceType type : PieceType.values()) {
            //when
            piece = null;
            piece = panel.getPiece(type, base);
            //then
            assertNotNull(piece);
        }
    }
}