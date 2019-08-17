package net.jfabricationgames.go_server.service;

/**
 * This class contains the possible methods that can be called (as RPC methods). <br>
 * The methods then call the needed methods to process the request.
 */
public class GoServiceProvider {
	
	/**
	 * Login a user with his username and password.
	 */
	public void login(Object parameters) {
		//TODO implement method
	}
	
	/**
	 * Logout a user with the session id.
	 */
	public void logout(Object parameters) {
		//TODO implement method
	}
	
	/**
	 * Start a new game with two users.
	 */
	public void startNewGame(Object parameters) {
		//TODO implement method
	}
	
	/**
	 * Continue a game that is saved.
	 */
	public void continueGame(Object parameters) {
		//TODO implement method
	}
	
	/**
	 * Load a game (that can be active or finished)
	 */
	public void loadGame(Object parameters) {
		//TODO implement method
	}
	
	/**
	 * Show all games the logged in user is participating in.
	 */
	public void showGames(Object parameters) {
		//TODO implement method
	}
	
	/**
	 * Make a move in the game the user is playing at the moment.
	 */
	public void makeMove(Object parameters) {
		//TODO implement method
	}
	
	/**
	 * Send a request of how many points every user has made after the game has ended.
	 */
	public void makePointCountRequest(Object parameters) {
		//TODO implement method
	}
}