package tetris;

import java.awt.*;
import java.util.TreeSet;

import static tetris.Constants.TABLE_COL_NUM;

public interface TetrisMatrix {

    Color getValueAt(Cell c);

    void setValueAt(Color color, Cell c);

    Color getEmptyCellColor();

    void reset();

    default int addPieceAndShift(Piece piece) {
        int topX = piece.getTopX();
        int lowX = piece.getLowX();
        TreeSet<Integer> linesToErase = new TreeSet<>();
        //Find full lines to erase
        for (int i = lowX; i >= topX; i--) {
            boolean fullLine = true;
            for (int j = TABLE_COL_NUM - 1; j >= 0; j--) {
                if (getValueAt(new Cell(i, j)) == getEmptyCellColor()) {
                    fullLine = false;
                    break;
                }
            }
            if (fullLine) {
                linesToErase.add(i);
            }
        }
        //Find first empty line
        for (int i = topX; i >= 0; i--) {
            boolean emptyLine = true;
            for (int j = TABLE_COL_NUM - 1; j >= 0; j--) {
                if (getValueAt(new Cell(i, j)) != getEmptyCellColor()) {
                    emptyLine = false;
                    break;
                }
            }
            if (emptyLine) {
                topX = i + 1;
                break;
            }
        }
        //Erase full and shift all lines above
        if (linesToErase.size() != 0) {
            int jump = 1;
            for (int i = linesToErase.last() - 1; i >= topX - jump; i--) {
                if (linesToErase.contains(i)) {
                    jump++;
                } else {
                    for (int j = 0; j < TABLE_COL_NUM; j++) {
                        Color color = getValueAt(new Cell(i, j));
                        setValueAt(color, new Cell(i + jump, j));
                    }
                }
            }
        }
        return linesToErase.size();
    }
}