package tetris;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Enumeration;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

@SuppressWarnings("serial")
public class TetrisPanel extends JPanel implements ActionListener, KeyListener {

	static final int TABLE_ROW_NUM = 20;
	static final int TABLE_COL_NUM = 10;
	static final int TABLE_COL_WIDTH = 20;
	private static final String scoreLabelName = "Score: ";
	private static final String useLabelName = "<html>Use arrow ctrls<br>to shift "
			+ "and Z <br>to rotate</html>";
	private static final String GAME_OVER_MESSAGE = "Game is over. You've lost.\nTry again.";
	private final double SCORE_BONUS = 1.5;
	private final int LINE_SCORE = 100;
	private JTable table;
	private TetrisTabelModel model;
	private JLabel scoreLabel;
	private JLabel useLabel;
	private JButton startButton, pauseButton;
	private int score;
	private int speed = 2;
	private Piece piece = null;
	private Timer timer;

	public TetrisPanel() {
		model = new TetrisTabelModel();
		table = new JTable(model);
		table.setDefaultRenderer(Color.class, new TetrisTabelCellRenderer());
		table.setRowHeight(TABLE_ROW_NUM);
		setColumnsWidth(TABLE_COL_WIDTH);
		JPanel controlsPanel = createControlsPanel();
		setLayout(new GridBagLayout());
		table.addKeyListener(this);
		add(table);
		add(controlsPanel);
	}
	
	public void setModel(TetrisTabelModel model) {
		this.model = model;
	}

	private void setColumnsWidth(int colWidth) {
		Enumeration<TableColumn> columns = table.getColumnModel().getColumns();
		while (columns.hasMoreElements()) {
			TableColumn column = columns.nextElement();
			column.setPreferredWidth(colWidth);
			column.setMinWidth(colWidth);
			column.setMaxWidth(colWidth);
			column.setWidth(colWidth);
		}
	}

	private JPanel createControlsPanel() {
		final Dimension defCompSize = new Dimension(100,50);
		scoreLabel = (new labelBuilder()).setText(scoreLabelName)
				.setSize(defCompSize).build();
		useLabel =  (new labelBuilder()).setText(useLabelName)
				.setAlignment(SwingConstants.CENTER)
				.setSize(defCompSize).build();
		startButton = (new buttonBuilder()).setSize(defCompSize).build();
		pauseButton = (new buttonBuilder()).setSize(defCompSize).build();
		startButton.setFocusable(false);
		pauseButton.setFocusable(false);
		setButtonsInitialState();
		startButton.addActionListener(this);
		pauseButton.addActionListener(this);
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(7,7,7,7));
		panel.add(scoreLabel);
		panel.add(Box.createRigidArea(new Dimension(0,100)));
		panel.add(startButton);
		panel.add(pauseButton);
		panel.add(Box.createRigidArea(new Dimension(0,100)));
		panel.add(useLabel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		return panel;
	}

	private void setButtonsInitialState() {
		startButton.setText("Start");
		startButton.setActionCommand("start");
		pauseButton.setText("Pause");
		pauseButton.setActionCommand("pause");
		pauseButton.setEnabled(false);
	}

	public class TetrisTabelCellRenderer extends DefaultTableCellRenderer {

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
				boolean hasFocus, int row, int column) {
			setOpaque(true);
			setBackground((Color) value);
			return this;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case "start":
			startButton.setText("Stop");
			startButton.setActionCommand("stop");
			pauseButton.setEnabled(true);
			int delay = 500/speed;
			timer = new Timer(delay, this);
			timer.setActionCommand("timer");
			resetPanelModel(model);
			score = 0;
			updateScore(score);
			timer.start();
			break;
		case "resume":
			pauseButton.setText("Pause");
			pauseButton.setActionCommand("pause");
			timer.start();
			break;
		case "stop":
			stopGame();
			break;
		case "pause":
			timer.stop();
			pauseButton.setText("Resume");
			pauseButton.setActionCommand("resume");
			break;
		case "timer":
			if(piece == null) {
				piece = getRandomPiece();
				if(!piece.putOnModel()){
					stopGame();
					JOptionPane.showMessageDialog(this, GAME_OVER_MESSAGE);
				}
			}
			else 
				if(!piece.move(new MoveDownStrategy(piece))){
					score += calculateScore(piece);
					updateScore(score);
					piece = null;
				}
			break;
		default:
			break;
		}
	}

	private void stopGame() {
		timer.stop();
		piece = null;
		setButtonsInitialState();
	}

	public void resetPanelModel(TetrisTabelModel model) {
		for (int i = 0; i < model.getRowCount(); i++)
			for (int j = 0; j < model.getColumnCount(); j++) {
				model.setValueAt(Color.WHITE, i, j);
			}
		model.fireTableDataChanged();
	}

	private void updateScore(int score) {
		scoreLabel.setText(scoreLabelName + score);
	}

	public Piece getRandomPiece() {
		int index = (int) (PieceType.values().length * Math.random());
		return getPiece( PieceType.values()[index], Piece.DEFAULT_BASE_CELL);
	}

	public Piece getPiece(PieceType type, Cell base) {
		switch (type) {
		case IPiece:
			return new IPiece(model, base);
		case JPiece:
			return new JPiece(model, base);
		case LPiece:
			return new LPiece(model, base);
		case OPiece:
			return new OPiece(model, base);
		case SPiece:
			return new SPiece(model, base);
		case TPiece:
			return new TPiece(model, base);
		case ZPiece:
			return new ZPiece(model, base);
		default:
			throw new IllegalArgumentException("Unknown piece type");
		}
	}

	private int calculateScore(Piece addPiece) {
		int linesNumber = shiftMatrix();
		return (int) (linesNumber*LINE_SCORE*SCORE_BONUS);
	}

	public int shiftMatrix() {
		int lastLine = TABLE_ROW_NUM - 1;
		TreeSet<Integer> linesToErase = new TreeSet<>();
		for( int i = TABLE_ROW_NUM - 1; i > 0; i--) {
			boolean fullLine = true;
			boolean emptyLine = true;
			for( int j = TABLE_COL_NUM - 1; j >= 0; j--) {
				if(model.getValueAt(i, j) == Color.WHITE) {
					fullLine = false;
				}
				else {
					emptyLine = false;
				}
			}
			if(emptyLine) {
				lastLine = i - 1;
				break;
			}
			if(fullLine) {
				linesToErase.add(i);
			}
		}
		if(linesToErase.size() != 0) {
			int jump = 1;
			for (int i = linesToErase.last() - 1; i >= lastLine - jump; i--) {
				if(linesToErase.contains(i)) {
					jump++;
				}
				else{
					for(int j = 0; j < TABLE_COL_NUM; j++) {
						Color color = (Color) model.getValueAt(i, j);
						model.setValueAt(color, i + jump, j);
					}
				}
			}
		}
		return linesToErase.size();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(timer.isRunning() && piece != null) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
			case KeyEvent.VK_KP_LEFT:
				piece.move(new MoveLeftStrategy(piece));
				break;
			case KeyEvent.VK_RIGHT:
			case KeyEvent.VK_KP_RIGHT:				 
				piece.move(new MoveRightStrategy(piece));
				break;
			case KeyEvent.VK_DOWN:
			case KeyEvent.VK_KP_DOWN:
				piece.move(new MoveDownStrategy(piece));
				break;
			case KeyEvent.VK_Z:
				piece.rotate(new RotateRightStrategy(piece));
			default:
				return;
			}
		}	
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	public static class buttonBuilder {
		private JButton instance = new JButton();

		public JButton build() {
			return instance;
		}

		public buttonBuilder setText(String text) {
			instance.setText(text);
			return this;
		}

		public buttonBuilder setSize(Dimension d) {
			instance.setPreferredSize(d);
			instance.setMaximumSize(d);
			return this;
		}
	}

	public static class labelBuilder {
		private JLabel instance = new JLabel();

		public JLabel build() {
			return instance;
		}

		public labelBuilder setText(String text) {
			instance.setText(text);
			return this;
		}

		public labelBuilder setAlignment(int align) {
			instance.setHorizontalAlignment(align);;
			return this;
		}

		public labelBuilder setSize(Dimension d) {
			instance.setPreferredSize(d);
			instance.setMaximumSize(d);
			return this;
		}
	}
}