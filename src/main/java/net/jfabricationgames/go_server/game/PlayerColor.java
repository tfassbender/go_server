package net.jfabricationgames.go_server.game;

public enum PlayerColor {
	
	EMPTY(0), //
	BLACK(1), //
	WHITE(2); //
	
	private final int code;
	
	private PlayerColor(int code) {
		this.code = code;
	}
	
	public int getCode() {
		return code;
	}
	
	public static PlayerColor ofCode(int code) {
		for (PlayerColor color : PlayerColor.values()) {
			if (color.getCode() == code) {
				return color;
			}
		}
		return null;
	}
	
	public static PlayerColor getOpposizeColor(PlayerColor color) {
		switch (color) {
			case BLACK:
				return WHITE;
			case WHITE:
				return BLACK;
			default:
				return EMPTY;
		}
	}
}