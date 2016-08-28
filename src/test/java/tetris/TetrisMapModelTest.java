package tetris;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static tetris.Axis.X;
import static tetris.Axis.Y;
import static tetris.TetrisMatrix.isOutOfModel;

public class TetrisMapModelTest {
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
    public void shouldIsOccupiedBehaveProperly() {
        //given
        Cell cell = new Cell(1, 2);
        //when
        model.put(cell);
        //then
        assertTrue(model.isPresent(cell));
        assertFalse(model.isPresent(new Cell(4, 8)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldGetExceptionWhenCellIsOutOfModel() {
        //given
        Cell outOfModelCell = new Cell(10, 14);
        //when
        model.put(outOfModelCell);
        //then
        fail("IllegalArgumentException should have been thrown.");
    }

    @Test
    public void shouldPutCellWithLegalCoordinates() {
        //given
        Cell cell = new Cell(10, 8);
        //when
        model.put(cell);
        //then
        assertTrue(model.isPresent(cell));
    }

    @Test
    public void shouldChangeModelAndCellsBeConsistent() {
        //given
        Piece piece = new TPiece(model, new Cell(6, 5));
        Piece movedLeftPiece = new TPiece(model, new Cell(6, 4));
        Set<Cell> oldCells = piece.getCells();
        Set<Cell> newCells = movedLeftPiece.getCells();
        //when
        model.changeModelAndCells(piece, newCells);
        //then
        newCells.forEach(oldCells::remove);
        oldCells.forEach((cell) -> assertFalse(model.isPresent(cell)));
        newCells.forEach((cell) -> assertTrue(model.isPresent(cell)));
    }

    @Test
    public void shouldPutAndRemoveCell() {
        //given
        Cell cell = new Cell(3, 7);
        //when
        model.put(cell);
        boolean isAdded = model.isPresent(cell);
        model.remove(cell);
        boolean isRemoved = !model.isPresent(cell);
        //then
        assertTrue(isAdded);
        assertTrue(isRemoved);
    }

    @Test
    public void shouldGetOutOfModelWhenCellSetWithIllegalCoordinates() {
        //given
        int lowX = X.max() - 1;
        Cell cell = new Cell(lowX, Y.max());
        //when
        boolean result = isOutOfModel(cell);
        //then
        assertTrue(result);
    }

    @Test
    public void shouldAddPieceAndShift2D() {
        if (Axis.values().length != 2) {
            System.out.println("shouldAddPieceAndShift2D skipped: wrong dimension quantity.");
            return;
        }
        //given
        int lowX = X.max() - 1;
        int topX = lowX - Piece.PIECE_LENGTH;
        int lowY = Y.max() - 1;
        model.reset();
        for (int i = lowX; i > topX; i--) {
            for (int j = lowY; j >= 0; j--) {
                model.put(new Cell(i, j));
            }
        }
        model.remove(new Cell(lowX, 2));
        model.remove(new Cell(lowX - 3, 7));

        Piece iPiece = new IPiece(model, new Cell(lowX - 2, 6));
        Set<Cell> cells = new HashSet<>();
        cells.add(new Cell(lowX - 3, 6));
        cells.add(new Cell(lowX - 2, 6));
        cells.add(new Cell(lowX - 1, 6));
        cells.add(new Cell(lowX, 6));
        iPiece.setCells(cells);
        cells.forEach(model::remove);
        //when
        int erasedLines = model.addPieceAndShift(iPiece);
        //then
        assertTrue(erasedLines == 2);
        assertFalse(model.isPresent(new Cell(lowX, 2)));
        assertFalse(model.isPresent(new Cell(lowX - 1, 7)));
    }

    @Test
    public void shouldAddPieceAndShift3D() {
        if (Axis.values().length != 3) {
            System.out.println("shouldAddPieceAndShift3D skipped: wrong dimension quantity.");
            return;
        }
        //given
        int lowX = X.max() - 1;
        int topX = lowX - Piece.PIECE_LENGTH;
        int lowY = Y.max() - 1;
        int lowZ = 9;
        model.reset();
        for (int x = lowX; x > topX; x--) {
            for (int y = lowY; y >= 0; y--) {
                for (int z = lowZ; z >= 0; z--) {
                    model.put(new Cell(x, y, z));
                }
            }
        }
        model.remove(new Cell(lowX, 2));
        model.remove(new Cell(lowX - 3, 7));

        Piece iPiece = new IPiece(model, new Cell(lowX - 2, 6));
        Set<Cell> cells = new HashSet<>();
        cells.add(new Cell(lowX - 3, 6));
        cells.add(new Cell(lowX - 2, 6));
        cells.add(new Cell(lowX - 1, 6));
        cells.add(new Cell(lowX, 6));
        iPiece.setCells(cells);
        cells.forEach(model::remove);
        //when
        int erasedLines = model.addPieceAndShift(iPiece);
        //then
        assertTrue(erasedLines == 2);
        assertFalse(model.isPresent(new Cell(lowX, 2)));
        assertFalse(model.isPresent(new Cell(lowX - 1, 7)));
    }
}