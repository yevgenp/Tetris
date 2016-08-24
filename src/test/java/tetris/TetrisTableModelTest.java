package tetris;

import org.junit.Test;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TetrisTableModelTest {

    private TetrisPanel panel = new TetrisPanel();
    private TetrisTableModel model = new TetrisTableModel();

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

    @Test
    public void shouldAddPieceAndShift() {
        //given
        model.reset();
        int lowX = Constants.TABLE_ROW_NUM - 1;
        int topX = lowX - Piece.PIECE_LENGTH;
        int lowY = Constants.TABLE_COL_NUM - 1;
        for (int i = lowX; i > topX; i--) {
            for (int j = lowY; j >= 0; j--) {
                model.setValueAt(Color.BLACK, i, j);
            }
        }
        model.setValueAt(model.getEmptyCellColor(), 18, 2);
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
        assertTrue(model.getValueAt(19, 2) == model.getEmptyCellColor());
        assertTrue(model.getValueAt(15, 8) != Color.GRAY);
        assertTrue(model.getValueAt(18, 8) == Color.GRAY);
        assertTrue(model.getValueAt(19, 4) == Color.CYAN);
        assertTrue(model.getValueAt(17, 1) == model.getEmptyCellColor());
    }
}