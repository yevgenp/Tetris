package tetris;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Enumeration;

@SuppressWarnings("serial")
public class TetrisPanel extends JPanel implements ActionListener, KeyListener {

	private static final String scoreLabelName = "Score: ";
	private static final String useLabelName = "<html>Use arrow ctrls<br>to shift "
			+ "and Z <br>to rotate</html>";
	private static final String GAME_OVER_MESSAGE = "Game is over. You've lost.\nTry again.";
	private final double SCORE_BONUS = 1.5;
	private final int LINE_SCORE = 100;
	private JTable table;
	private TetrisTableModel model;
	private JLabel scoreLabel;
	private JLabel useLabel;
	private JButton startButton, pauseButton;
	private int score;
	private int speed = 2;
	private Piece piece = null;
	private Timer timer;

	public TetrisPanel() {
		model = new TetrisTableModel();
		table = new JTable(model);
		table.setDefaultRenderer(Color.class, new TetrisTabelCellRenderer());
		table.setRowHeight(Constants.TABLE_ROW_NUM);
		setColumnsWidth(Constants.TABLE_COL_WIDTH);
		JPanel controlsPanel = createControlsPanel();
		setLayout(new GridBagLayout());
		table.addKeyListener(this);
		add(table);
		add(controlsPanel);
	}

	public void setModel(TetrisTableModel model) {
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

	private class TetrisTabelCellRenderer extends DefaultTableCellRenderer {

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
			model.reset();
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
		int linesNumber = model.addPieceAndShift(addPiece);
		return (int) (linesNumber*LINE_SCORE*SCORE_BONUS);
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
				break;
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

	private static class buttonBuilder {
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

	private static class labelBuilder {
		private JLabel instance = new JLabel();

		public JLabel build() {
			return instance;
		}

		public labelBuilder setText(String text) {
			instance.setText(text);
			return this;
		}

		public labelBuilder setAlignment(int align) {
			instance.setHorizontalAlignment(align);
			return this;
		}

		public labelBuilder setSize(Dimension d) {
			instance.setPreferredSize(d);
			instance.setMaximumSize(d);
			return this;
		}
	}
}