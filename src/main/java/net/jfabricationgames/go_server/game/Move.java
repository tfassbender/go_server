package net.jfabricationgames.go_server.game;

public class Move {
	
	private FieldPosition pos;
	private PlayerColor color;
	
	private boolean pass;
	
	public Move(int row, int col, PlayerColor color) {
		this.pos = new FieldPosition(row, col);
		this.color = color;
	}
	
	public static Move getPassMove(PlayerColor color) {
		Move pass = new Move(-1, -1, color);
		pass.pass = true;
		return pass;
	}
	
	@Override
	public String toString() {
		return "Move [pos=" + pos + ", color=" + color + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((color == null) ? 0 : color.hashCode());
		result = prime * result + ((pos == null) ? 0 : pos.hashCode());
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
		Move other = (Move) obj;
		if (color != other.color)
			return false;
		if (pos == null) {
			if (other.pos != null)
				return false;
		}
		else if (!pos.equals(other.pos))
			return false;
		return true;
	}
	
	public int getRow() {
		return pos.getRow();
	}
	
	public int getCol() {
		return pos.getCol();
	}
	
	public boolean isPass() {
		return pass;
	}

	public FieldPosition getPos() {
		return pos;
	}
	public PlayerColor getColor() {
		return color;
	}
}