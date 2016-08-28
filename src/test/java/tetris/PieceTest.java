package tetris;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static tetris.Axis.X;
import static tetris.Axis.Y;

public class PieceTest {

    private final TetrisMatrix model = new TetrisMapModel();

    private void setup() {
        model.reset();
        model.put(new Cell(19, 1));
        model.put(new Cell(19, 2));
        model.put(new Cell(19, 3));
        model.put(new Cell(18, 2));
        model.put(new Cell(19, 5));
        model.put(new Cell(18, 5));
        model.put(new Cell(17, 5));
        model.put(new Cell(17, 6));
    }

    @Test
    public void shouldGetTopX() throws Exception {
        //given
        Piece ZPiece = new ZPiece(model, new Cell(0, 5));
        Piece JPiece = new JPiece(model, new Cell(X.max() - 2, 4));
        //when
        int ZTopX = ZPiece.getTopX();
        int JTopX = JPiece.getTopX();
        //then
        assertTrue(ZTopX == 0);
        assertTrue(JTopX == X.max() - 2);
    }

    @Test
    public void shouldGetLowX() throws Exception {
        //given
        Piece SPiece = new SPiece(model, new Cell(0, 5));
        Piece OPiece = new OPiece(model, new Cell(X.max() - 2, 4));
        //when
        int ZLowX = SPiece.getLowX();
        int OLowX = OPiece.getLowX();
        //then
        assertTrue(ZLowX == 1);
        assertTrue(OLowX == X.max() - 1);
    }

    @Test
    public void shouldPieceInitProperly() {
        //given
        Piece oPiece, sPiece;
        Cell base = new Cell(4, 7);
        //when
        oPiece = new OPiece(model, base);
        sPiece = new SPiece(model, base);
        //then
        assertTrue(oPiece.getCells().contains(base));
        assertTrue(oPiece.getCells().contains(new Cell(5, 7)));
        assertTrue(oPiece.getCells().contains(new Cell(5, 6)));
        assertTrue(oPiece.getCells().contains(new Cell(4, 6)));
        assertTrue(sPiece.getCells().contains(base));
        assertTrue(sPiece.getCells().contains(new Cell(4, 8)));
        assertTrue(sPiece.getCells().contains(new Cell(5, 7)));
        assertTrue(sPiece.getCells().contains(new Cell(5, 6)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldPieceWithAbnormalCellsNumberSetCells() {
        //given
        Set<Cell> cells = new HashSet<>();
        for (int i = 0; i < Piece.PIECE_LENGTH - 1; i++) {
            cells.add(new Cell(i, 0));
        }
        //when
        Piece iPiece = new IPiece(model, new Cell(1, 15));
        //then
        iPiece.setCells(cells);
        fail("IllegalArgumentException should have been thrown.");
    }

    @Test
    public void shouldGetDefaultCellReturnAlwaysNewCell() {
        //given
        Cell newCell_1, newCell_2, newCell_3;
        //when
        newCell_1 = Piece.getDefaultCell();
        newCell_2 = Piece.getDefaultCell();
        newCell_3 = Piece.getDefaultCell();
        newCell_3.set(X, 17);
        //then
        assertFalse(newCell_1 == newCell_2);
        assertTrue(newCell_1.equals(newCell_2));
        assertTrue(newCell_1.equals(Piece.getDefaultCell()));
        assertFalse(newCell_1 == newCell_3);
        assertFalse(newCell_1.equals(newCell_3));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldPieceWithBaseOutOfModelSetBase() {
        //given
        Cell outOfModelCell = new Cell(15, 10);
        //when
        new SPiece(model, outOfModelCell);
        //then
        fail("IllegalArgumentException should have been thrown.");
    }

    @Test
    public void shouldSpaceBeAvailableWhenPieceTriesNewOccupiedPosition() {
        //given
        setup();
        Piece piece = new JPiece(model, new Cell(16, 6));
        //when
        Set<Cell> newCells = new HashSet<>();
        newCells.add(new Cell(16, 5));
        newCells.add(new Cell(16, 4));
        newCells.add(new Cell(16, 3));
        newCells.add(new Cell(17, 5));
        //then
        assertFalse(piece.isSpaceAvailable(newCells));
    }

    @Test
    public void shouldPieceMove() {
        //given
        setup();
        Piece sPiece = new SPiece(model, new Cell(15, 4));
        //when
        boolean moveLeft = sPiece.move(new MoveLeftStrategy(sPiece), Y);
        boolean moveDownThen = sPiece.move(new MoveDownStrategy(sPiece), X);
        boolean moveRightThen = sPiece.move(new MoveRightStrategy(sPiece), Y);
        boolean moveRightAgain = sPiece.move(new MoveRightStrategy(sPiece), Y);
        //then
        assertTrue(moveLeft);
        assertTrue(moveDownThen);
        assertTrue(moveRightThen);
        assertFalse(moveRightAgain);
    }

    @Test
    public void shouldPieceRotateRight() {
        //given
        setup();
        Set<Cell> cells1, cells2, newCells;
        Piece piece = new IPiece(model, new Cell(14, 5));
        cells1 = piece.getCells();
        //when
        piece.rotate(new RotateRightStrategy(piece), X, Y);
        cells2 = piece.getCells();
        cells2.removeAll(cells1);
        newCells = new HashSet<>();
        newCells.add(new Cell(13, 5));
        newCells.add(new Cell(15, 5));
        newCells.add(new Cell(16, 5));
        //then
        assertTrue(cells2.size() == 3);
        assertTrue(newCells.containsAll(cells2));
    }

    @Test
    public void shouldPieceRotateLeft() {
        //given
        setup();
        Set<Cell> cells1, cells2, newCells;
        Piece piece = new IPiece(model, new Cell(14, 5));
        cells1 = piece.getCells();
        //when
        piece.rotate(new RotateLeftStrategy(piece), X, Y);
        cells2 = piece.getCells();
        cells2.removeAll(cells1);
        newCells = new HashSet<>();
        newCells.add(new Cell(12, 5));
        newCells.add(new Cell(13, 5));
        newCells.add(new Cell(15, 5));
        //then
        assertTrue(cells2.size() == 3);
        assertTrue(newCells.containsAll(cells2));
    }
}