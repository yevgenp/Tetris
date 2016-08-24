package tetris;

final class Cell implements AbstractCell {
	//x - model row, y - model column
	private int x,y;

	public Cell(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	@Override
	public Cell clone() {
		return new Cell(this.x, this.y);
	}

	@Override
	public int get(char coord) {
		switch (coord) {
			case 'x':
			case 'X':
				return this.x;
			case 'y':
			case 'Y':
				return this.y;
			default:
				throw new IllegalArgumentException("Unknown coordinate.");
		}
	}

	@Override
	public void set(char coord, int value) {
		switch (coord) {
			case 'x':
			case 'X':
				this.x = value;
				break;
			case 'y':
			case 'Y':
				this.y = value;
				break;
			default:
				throw new IllegalArgumentException("Unknown coordinate.");
		}
	}

	@Override
	public int hashCode() {
		final int prime = 227;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cell other = (Cell) obj;
		return x == other.x && y == other.y;
	}

	@Override
	public String toString() {
		return "[" + this.x + "," + this.y + "]";
	}
}