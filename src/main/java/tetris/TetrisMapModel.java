package tetris;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class TetrisMapModel implements TetrisMatrix {

    final private Color EMPTY_CELL_COLOR = null;

    private Map<Cell, Color> model;

    public TetrisMapModel() {
        model = new HashMap<>();
    }

    @Override
    public Color getValueAt(Cell c) {
        return model.get(c);
    }

    @Override
    public void setValueAt(Color color, Cell c) {
        if (color != null) {
            model.put(c, color);
        } else model.remove(c);

    }

    @Override
    public Color getEmptyCellColor() {
        return EMPTY_CELL_COLOR;
    }

    @Override
    public void reset() {
        model = new HashMap<>();
    }
}