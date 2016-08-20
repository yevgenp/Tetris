package tetris;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class AppTests {
	
	TetrisPanel panel = new TetrisPanel();
	TetrisTabelModel model = new TetrisTabelModel();
	
	public void setup() {
		panel.resetPanelModel(model);
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
		Piece zPiece = new ZPiece(model, new Cell(16,7));
		//then
		assertTrue(zPiece.putOnModel());
	}

	@Test
	public void shouldPieceThatOverlapOtherPutOnModel() {
		//given
		setup();
		//when
		Piece wrongPiece = new LPiece(model, new Cell(18,1));
		//then
		assertFalse(wrongPiece.putOnModel());
	}

	@Test
	public void shouldPieceInitProperly() {
		//given
		Piece oPiece, sPiece;
		Cell base = new Cell(4,7);
		//when
		oPiece = new OPiece(model, base);
		sPiece = new SPiece(model, base);
		//then
		assertTrue(oPiece.getCells().contains(base));
		assertTrue(oPiece.getCells().contains(new Cell(5,7)));
		assertTrue(oPiece.getCells().contains(new Cell(5,6)));
		assertTrue(oPiece.getCells().contains(new Cell(4,6)));
		assertTrue(sPiece.getCells().contains(base));
		assertTrue(sPiece.getCells().contains(new Cell(4,8)));
		assertTrue(sPiece.getCells().contains(new Cell(5,7)));
		assertTrue(sPiece.getCells().contains(new Cell(5,6)));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void shouldPieceWithAbnormalCellsNumberSetCells() {
		//given
		Set<Cell> cells = new HashSet<>();
		for (int i = 0; i < Piece.PIECE_LENGTH - 1; i++) {
			cells.add(new Cell(i,0));
		}
		//when
		Piece iPiece = new IPiece(model, new Cell(1,15));
		//then
		iPiece.setCells(cells);
		fail("IllegalArgumentException shoud have been thrown.");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void shouldPieceWithBaseOutOfModelSetBase() {
		//given
		Cell outOfModelCell = new Cell(15,10);
		//when
		new SPiece(model, outOfModelCell);
		//then
		fail("IllegalArgumentException shoud have been thrown.");
	}
	
	@Test
	public void shouldGetOutOfModelWhenCellSetWithIllegalCoordinates() {
		//given
		Cell cell = new Cell(19, 10);
		//when
		boolean result = Piece.isOutOfModel(cell);
		//then
		assertTrue(result);
	}
	
	@Test
	public void shouldSpaceBeAvailableWhenPieceTriesNewOccupiedPosition() {
		//given
		setup();
		Piece piece = new JPiece(model, new Cell(16,6));
		//when
		Set<Cell> newCells = new HashSet<>();
		newCells.add(new Cell(16,5));
		newCells.add(new Cell(16,4));
		newCells.add(new Cell(16,3));
		newCells.add(new Cell(17,5));
		//then
		assertFalse(piece.isSpaceAvailable(newCells));
	}
	
	@Test
	public void shouldPieceMove() {
		//given
		setup();
		Piece sPiece = new SPiece(model, new Cell(15,4));
		//when
		boolean moveLeft = sPiece.move(new MoveLeftStrategy(sPiece));
		boolean moveDownThen = sPiece.move(new MoveDownStrategy(sPiece));
		boolean moveRightThen = sPiece.move(new MoveRightStrategy(sPiece));
		boolean moveRightAgain = sPiece.move(new MoveRightStrategy(sPiece));
		//then
		assertTrue(moveLeft);
		assertTrue(moveDownThen);
		assertTrue(moveRightThen);
		assertFalse(moveRightAgain);
	}
	
	@Test
	public void shouldPieceRotate() {
		//given
		setup();
		Set<Cell> cells1, cells2, newCells;
		Piece piece = new IPiece(model, new Cell(14,5));
		cells1 = piece.getCells();
		//when
		piece.rotate(new RotateRightStrategy(piece));
		cells2 = piece.getCells();
		cells2.removeAll(cells1);
		newCells = new HashSet<Cell>();
		newCells.add(new Cell(13,5));
		newCells.add(new Cell(15,5));
		newCells.add(new Cell(16,5));
		//then
		assertTrue(cells2.size() == 3);
		assertTrue(newCells.containsAll(cells2));
	}
	
	@Test
	public void shouldGetPieceAlwaysReturnOneofPieceType() {
		//given
		Piece piece;
		Cell base = new Cell(3,4);
		for (PieceType type: PieceType.values()) {
			//when
			piece = null;
			piece = panel.getPiece(type, base);
			//then
			assertNotNull(piece);
		}
	}
	
	@Test
	public void shouldShiftMatrixEraseLinesAndReturnTheirNumber() {
		//given
		int lowX = TetrisPanel.TABLE_ROW_NUM - 1;
		int topX = lowX - Piece.PIECE_LENGTH;
		int lowY = TetrisPanel.TABLE_COL_NUM - 1;
		panel.setModel(model);
		panel.resetPanelModel(model);
		for (int i = lowX; i > topX; i--) {
			for (int j = lowY; j >= 0; j--) {
				model.setValueAt(Color.BLACK, i, j);
			}
		}
		model.setValueAt(Color.WHITE, 18, 2);
		model.setValueAt(Color.CYAN, 18, 5);
		model.setValueAt(Color.YELLOW, 17, 1);
		model.setValueAt(Color.GRAY, 15, 8);
		//when
		int erasedLines = panel.shiftMatrix();
		//then
		assertTrue(erasedLines == 3);
		assertTrue((Color)model.getValueAt(19, 2) == Color.WHITE);
		assertTrue((Color)model.getValueAt(15, 8) != Color.GRAY);
		assertTrue((Color)model.getValueAt(18, 8) == Color.GRAY);
		assertTrue((Color)model.getValueAt(19, 5) == Color.CYAN);
		assertTrue((Color)model.getValueAt(17, 1) == Color.WHITE);
		
	}
}
