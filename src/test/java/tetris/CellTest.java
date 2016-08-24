package tetris;

import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class CellTest {
    @Test
    public void shouldClone() throws Exception {
        //given
        Cell cell = new Cell(25, 30);
        //when
        Cell clone = cell.clone();
        //then
        assertTrue(clone.get('x') == 25 && clone.get('y') == 30);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldGetUnknownCoordinate() throws Exception {
        //given
        Cell cell = new Cell(0, 0);
        //when
        cell.get('z');
        //then
        fail("IllegalArgumentException shoud have been thrown.");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldSetUnknownCoordinate() throws Exception {
        //given
        Cell cell = new Cell(0, 0);
        //when
        cell.set('z', 1);
        //then
        fail("IllegalArgumentException shoud have been thrown.");
    }

    @Test
    public void shouldClonedCellHaveSameHashCode() throws Exception {
        //given
        Cell cell = new Cell(-10, -11);
        //when
        Cell clone = cell.clone();
        //then
        assertTrue(cell.hashCode() == clone.hashCode());
    }

    @Test
    public void shouldCellsWithSameCoordinatesBeEqual() throws Exception {
        //given
        Cell cell = new Cell(-3, 17);
        //when
        Cell anotherCell = new Cell(-3, 17);
        //then
        assertTrue(cell.equals(anotherCell));
    }
}