package net.jfabricationgames.go_server.game;

import java.util.Collection;
import java.util.List;

public class Referee {
	
	private Game game;
	
	private int[][] board;
	private int[][] previousBoard;
	
	private PlayerColor lastMove;
	
	public Referee(Game game) {
		this.game = game;
		board = new int[game.getSize()][game.getSize()];
		
		if (game.getStones() == 0) {
			lastMove = PlayerColor.WHITE;
		}
		else {
			lastMove = PlayerColor.BLACK;
		}
		
		fillBoard(game.getMoves());
	}
	
	private void fillBoard(List<Move> moves) {
		for (Move move : moves) {
			if (isValidMove(move)) {
				addMove(move);
			}
		}
	}
	
	private int[][] getBoardCopy() {
		int[][] newBoard = new int[getBoardSize()][getBoardSize()];
		for (int i = 0; i < getBoardSize(); i++) {
			for (int j = 0; j < getBoardSize(); j++) {
				newBoard[i][j] = board[i][j];
			}
		}
		return newBoard;
	}
	
	public boolean isValidMove(Move move) {
		if (move.getColor() != PlayerColor.getOpposizeColor(lastMove)) {
			//wrong color
			return false;
		}
		if (move.isPass()) {
			//if color is correct pass is always valid
			return true;
		}
		else {//no passing move
			if (!move.getPos().exists(getBoardSize())) {
				//position doesn't exist
				return false;
			}
			if (getStoneColor(move.getPos()) != PlayerColor.EMPTY) {
				//field not empty
				return false;
			}
			
			//execute the move and check whether the state is valid
			int[][] tmpBoard = getBoardCopy();//keep a copy of the board for a rollback
			//make the move
			board[move.getRow()][move.getCol()] = move.getColor().getCode();
			removeBeaten(move);
			
			//check the group of the new stone
			Group group = Group.findGroup(this, move.getPos());
			if (group.isBeaten(this)) {
				//added stone is directly beaten
				board = tmpBoard;
				return false;
			}
			
			if (boardsEqual(board, previousBoard)) {
				//restores the board from the last move (ko rule violated)
				board = tmpBoard;
				return false;
			}
			
			board = tmpBoard;
		}
		return true;
	}
	
	private static boolean boardsEqual(int[][] board, int[][] previousBoard) {
		boolean equal = true;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				equal &= board[i][j] == previousBoard[i][j];
			}
		}
		return equal;
	}
	
	public void addMove(Move move) {
		//copy the board to the previous board field
		previousBoard = getBoardCopy();
		
		//set the new stone
		board[move.getRow()][move.getCol()] = move.getColor().getCode();
		//remove beaten stones (if any)
		removeBeaten(move);
		
		//set the last move color
		lastMove = move.getColor();
	}
	
	private void removeBeaten(Move move) {
		//find the fields near to the move field
		List<FieldPosition> near = move.getPos().getFieldsNear(game.getSize());
		for (FieldPosition pos : near) {
			//check whether the field has a stone of the different color on it
			if (getStoneColor(pos) == PlayerColor.getOpposizeColor(move.getColor())) {
				Group group = Group.findGroup(this, pos);
				if (group.isBeaten(this)) {
					removeStones(group.getStones());
				}
			}
		}
	}
	
	private void removeStones(Collection<FieldPosition> stones) {
		for (FieldPosition pos : stones) {
			board[pos.getRow()][pos.getCol()] = PlayerColor.EMPTY.getCode();
		}
	}
	
	public boolean isFieldEmpty(FieldPosition pos) {
		return board[pos.getRow()][pos.getCol()] == PlayerColor.EMPTY.getCode();
	}
	
	public PlayerColor getStoneColor(FieldPosition pos) {
		return PlayerColor.ofCode(board[pos.getRow()][pos.getCol()]);
	}
	public int getBoardSize() {
		return game.getSize();
	}
}