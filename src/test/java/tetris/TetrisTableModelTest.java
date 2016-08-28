package tetris;

import org.junit.Test;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static tetris.Axis.X;
import static tetris.Axis.Y;
import static tetris.TetrisTableModel.EMPTY_CELL_COLOR;

public class TetrisTableModelTest {
    private final TetrisTableModel model = new TetrisTableModel();

    private void setup() {
        model.reset();
        model.setValueAt(Color.MAGENTA, 19, 1);
        model.setValueAt(Color.MAGENTA, 19, 2);
        model.setValueAt(Color.MAGENTA, 19, 3);
        model.setValueAt(Color.MAGENTA, 18, 2);
        model.setValueAt(Color.BLUE, 19, 5);
        model.setValueAt(Color.BLUE, 18, 5);
        model.setValueAt(Color.BLUE, 17, 5);
        model.setValueAt(Color.BLUE, 17, 6);
    }

    @Test
    public void shouldPiecePutOnModel() {
        //given
        setup();
        //when
        Piece zPiece = new ZPiece(model, new Cell(16, 7));
        //then
        assertTrue(model.putOnModel(zPiece));
    }

    @Test
    public void shouldPieceThatOverlapOtherPutOnModel() {
        //given
        setup();
        //when
        Piece wrongPiece = new LPiece(model, new Cell(18, 1));
        //then
        assertFalse(model.putOnModel(wrongPiece));
    }

    @Test
    public void shouldAddPieceAndShift() {
        //given
        model.reset();
        int lowX = X.max() - 1;
        int topX = lowX - Piece.PIECE_LENGTH;
        int lowY = Y.max() - 1;
        for (int i = lowX; i > topX; i--) {
            for (int j = lowY; j >= 0; j--) {
                model.setValueAt(Color.BLACK, i, j);
            }
        }
        model.setValueAt(EMPTY_CELL_COLOR, 18, 2);
        model.setValueAt(Color.CYAN, 18, 4);
        model.setValueAt(Color.YELLOW, 17, 1);
        model.setValueAt(Color.GRAY, 15, 8);
        Piece iPiece = new IPiece(model, new Cell(17, 6));
        Set<Cell> cells = new HashSet<>();
        cells.add(new Cell(16, 6));
        cells.add(new Cell(17, 6));
        cells.add(new Cell(18, 6));
        cells.add(new Cell(19, 6));
        iPiece.setCells(cells);
        //when
        int erasedLines = model.addPieceAndShift(iPiece);
        //then
        assertTrue(erasedLines == 3);
        assertTrue(model.getValueAt(19, 2) == EMPTY_CELL_COLOR);
        assertTrue(model.getValueAt(15, 8) != Color.GRAY);
        assertTrue(model.getValueAt(18, 8) == Color.GRAY);
        assertTrue(model.getValueAt(19, 4) == Color.CYAN);
        assertTrue(model.getValueAt(17, 1) == EMPTY_CELL_COLOR);
    }
}
