package tetris;

import org.junit.Test;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertTrue;

public class TetrisMapModelTest {

    private TetrisMapModel model = new TetrisMapModel();

    @Test
    public void shouldAddPieceAndShift() {
        //given
        int lowX = Constants.TABLE_ROW_NUM - 1;
        int topX = lowX - Piece.PIECE_LENGTH;
        int lowY = Constants.TABLE_COL_NUM - 1;
        model.reset();
        for (int i = lowX; i > topX; i--) {
            for (int j = lowY; j >= 0; j--) {
                model.setValueAt(Color.BLACK, new Cell(i, j));
            }
        }
        model.setValueAt(model.getEmptyCellColor(), new Cell(18, 2));
        model.setValueAt(Color.CYAN, new Cell(18, 4));
        model.setValueAt(Color.YELLOW, new Cell(17, 1));
        model.setValueAt(Color.GRAY, new Cell(15, 8));
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
        assertTrue(model.getValueAt(new Cell(19, 2)) == model.getEmptyCellColor());
        assertTrue(model.getValueAt(new Cell(15, 8)) != Color.GRAY);
        assertTrue(model.getValueAt(new Cell(18, 8)) == Color.GRAY);
        assertTrue(model.getValueAt(new Cell(19, 4)) == Color.CYAN);
        assertTrue(model.getValueAt(new Cell(17, 1)) == model.getEmptyCellColor());
    }
}