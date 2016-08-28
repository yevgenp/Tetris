package tetris;

import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static tetris.Axis.X;
import static tetris.Axis.Y;

public class CellTest {

    @Test(expected = IllegalArgumentException.class)
    public void shouldCellConstructWithVariousCoordinates() {
        //given
        Cell cell;
        //when
        cell = new Cell(77);
        //then
        assertTrue(cell.get(X) == 77);
        for (int a = 1; a < Axis.values().length; a++) {
            assertTrue(cell.get(Axis.values()[a]) == 0);
        }
        if (Axis.values().length <= 9) {
            cell = new Cell(1, 2, 3, 4, 5, 6, 7, 8, 9);
            fail("IllegalArgumentException should have been thrown.");
        } else throw new IllegalArgumentException();
    }

    @Test
    public void shouldClone() {
        //given
        Cell cell = new Cell(25, 30);
        //when
        Cell clone = cell.clone();
        //then
        assertTrue(clone.equals(cell));
    }

    @Test
    public void shouldClonedCellHaveSameHashCode() {
        //given
        Cell cell = new Cell(-10, -11);
        //when
        Cell clone = cell.clone();
        //then
        assertTrue(cell.hashCode() == clone.hashCode());
    }

    @Test
    public void shouldCellsWithSameCoordinatesBeEqual() {
        //given
        Cell cell = new Cell(-3, 17);
        //when
        Cell anotherCell = new Cell(-3, 17);
        //then
        assertTrue(cell.equals(anotherCell));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldGetCoordinateWhenAxisIsNotSet() {
        //given
        Cell cell = new Cell();
        //when
        cell.set(X, 44);
        cell.get(Y);
        //then
        fail("IllegalArgumentException should have been thrown.");

    }
}