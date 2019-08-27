package net.jfabricationgames.go_server.game;

import java.util.ArrayList;
import java.util.List;

import net.jfabricationgames.go_server.db.DatabaseConnection;

public class GameManager {
	
	private static GameManager instance;
	
	private List<Game> games;//games that are loaded because they were used recently
	private DatabaseConnection dbConnection;
	
	private GameManager() {
		games = new ArrayList<Game>();
		dbConnection = new DatabaseConnection();
	}
	
	public static synchronized GameManager getInstance() {
		if (instance == null) {
			instance = new GameManager();
		}
		return instance;
	}
	
	public void startGame(Player playerBlack, Player playerWhite, int stones, double comi) {
		
	}
}