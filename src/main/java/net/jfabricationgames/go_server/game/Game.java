package net.jfabricationgames.go_server.game;

import java.util.List;

public class Game {
	
	private List<Move> moves;
	private Player[] players;
	private boolean active;
	private double points;
	private double comi;
	private int stones;
	private int size;
	
	private Referee referee;
	
	protected Game(List<Move> moves, Player playerBlack, Player playerWhite, int size, int stones, double comi, double points, boolean active) {
		this.moves = moves;
		this.players = new Player[] {playerBlack, playerWhite};
		this.active = active;
		this.points = points;
		this.comi = comi;
		this.stones = stones;
		this.size = size;
		this.referee = new Referee(this);
	}
	
	public void makeMove(Move move) {
		if (referee.isValidMove(move)) {
			referee.addMove(move);
			moves.add(move);
		}
	}
	
	public void save() {
		//TODO save to database
	}
	
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public double getPoints() {
		return points;
	}
	public void setPoints(double points) {
		this.points = points;
	}
	
	public double getComi() {
		return comi;
	}
	public void setComi(double comi) {
		this.comi = comi;
	}
	
	public int getStones() {
		return stones;
	}
	public void setStones(int stones) {
		this.stones = stones;
	}
	
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	
	public List<Move> getMoves() {
		return moves;
	}
	
	public Player[] getPlayers() {
		return players;
	}
}