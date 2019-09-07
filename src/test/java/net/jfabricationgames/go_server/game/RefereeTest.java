package net.jfabricationgames.go_server.game;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

class RefereeTest {
	
	private final int b = PlayerColor.BLACK.getCode();
	private final int w = PlayerColor.WHITE.getCode();
	private final int e = PlayerColor.EMPTY.getCode();
	
	private Referee getReferee(int[][] board, PlayerColor playersTurn) {
		Game game = mock(Game.class);
		when(game.getSize()).thenReturn(board.length);
		when(game.getMoves()).thenReturn(Collections.emptyList());
		
		Referee ref = new Referee(game);
		ref.setBoard(board);
		ref.setPreviouseBoard(ref.getBoardCopy());
		ref.setPlayersTurn(playersTurn);
		return ref;
	}
	
	private List<Move> toMoves(int[][] positions, PlayerColor startingColor) {
		List<Move> moves = new ArrayList<Move>(positions.length);
		
		PlayerColor color = startingColor;
		for (int[] pos : positions) {
			moves.add(new Move(pos[0], pos[1], color));
			color = PlayerColor.getOpposizeColor(color);
		}
		
		return moves;
	}
	
	@Test
	public void testGetBoardCopy() {
		int[][] board = new int[][] {{1, 2, 3}, {6, 7, 8}, {42, 42, 42}};//only works for quadratic boards
		
		Referee ref = getReferee(board, PlayerColor.BLACK);
		int[][] copy = ref.getBoardCopy();
		
		for (int i = 0; i < board.length; i++) {
			assertArrayEquals(board[i], copy[i]);
		}
	}
	
	@Test
	public void testGetBoardCopy_independentCopy() {
		int[][] board = new int[][] {{1, 2, 3}, {6, 7, 8}, {42, 42, 42}};
		
		Referee ref = getReferee(board, PlayerColor.BLACK);
		int[][] copy = ref.getBoardCopy();
		
		board[0][0] = 42;
		assertNotEquals(board[0][0], copy[0][0]);
	}
	
	@Test
	public void testBoardsEqual() {
		int[][] board = new int[][] {{1, 2, 3}, {6, 7, 8}, {42, 42, 42}};
		int[][] board2 = new int[][] {{1, 2, 3}, {6, 7, 8}, {42, 42, 42}};
		int[][] board3 = new int[][] {{1, 2, 3}, {6, 7, 8}, {43, 43, 43}};
		
		assertTrue(Referee.boardsEqual(board, board2));
		assertFalse(Referee.boardsEqual(board, board3));
	}
	
	@Test
	public void testGetStoneColor() {
		int[][] board = new int[][] {//
				{b, w, e},//
				{b, w, e},//
				{e, e, b},//
		};
		
		Referee ref = getReferee(board, PlayerColor.BLACK);
		
		assertEquals(PlayerColor.BLACK, ref.getStoneColor(new FieldPosition(0, 0)));
		assertEquals(PlayerColor.BLACK, ref.getStoneColor(new FieldPosition(1, 0)));
		assertEquals(PlayerColor.WHITE, ref.getStoneColor(new FieldPosition(0, 1)));
		assertEquals(PlayerColor.WHITE, ref.getStoneColor(new FieldPosition(1, 1)));
		assertEquals(PlayerColor.EMPTY, ref.getStoneColor(new FieldPosition(2, 1)));
		assertEquals(PlayerColor.EMPTY, ref.getStoneColor(new FieldPosition(1, 2)));
	}
	
	@Test
	public void testIsFieldEmpty() {
		int[][] board = new int[][] {//
				{b, w, e},//
				{b, w, e},//
				{e, e, b},//
		};
		
		Referee ref = getReferee(board, PlayerColor.BLACK);
		
		assertTrue(ref.isFieldEmpty(new FieldPosition(0, 2)));
		assertTrue(ref.isFieldEmpty(new FieldPosition(1, 2)));
		assertTrue(ref.isFieldEmpty(new FieldPosition(2, 0)));
		assertTrue(ref.isFieldEmpty(new FieldPosition(2, 1)));
		
		assertFalse(ref.isFieldEmpty(new FieldPosition(0, 0)));
		assertFalse(ref.isFieldEmpty(new FieldPosition(0, 1)));
	}
	
	@Test
	public void testRemoveStones() {
		int[][] board = new int[][] {//
				{b, w, e},//
				{b, w, e},//
				{e, e, b},//
		};
		
		Referee ref = getReferee(board, PlayerColor.BLACK);
		
		Set<FieldPosition> positionsToRemove = new HashSet<FieldPosition>();
		positionsToRemove.addAll(Arrays.asList(new FieldPosition(0, 0), new FieldPosition(0, 1), new FieldPosition(0, 2), new FieldPosition(1, 1)));
		
		ref.removeStones(positionsToRemove);
		
		int[][] expectedBoard = new int[][] {//
				{e, e, e},//
				{b, e, e},//
				{e, e, b},//
		};
		
		assertTrue(Referee.boardsEqual(expectedBoard, board));
	}
	
	@Test
	public void testRemoveBeaten_noBeatenStones() {
		int[][] board = new int[][] {//
				{b, w, b, e, e},//
				{b, w, b, e, e},//
				{e, e, e, w, w},//
				{b, w, w, b, b},//
				{b, w, w, b, e},//
		};
		int[][] expectedBoard = new int[][] {//
				{b, w, b, e, b},//
				{b, w, b, e, e},//
				{e, e, e, w, w},//
				{b, w, w, b, b},//
				{b, w, w, b, e},//
		};
		
		Referee ref = getReferee(board, PlayerColor.BLACK);
		
		//create the next move
		Move move = new Move(0, 4, PlayerColor.BLACK);
		//add the stone
		board[move.getRow()][move.getCol()] = move.getColor().getCode();
		//remove the beaten stones
		ref.removeBeaten(move);
		
		assertTrue(Referee.boardsEqual(expectedBoard, board));
	}
	
	@Test
	public void testRemoveBeaten_oneGroupBeaten() {
		int[][] board = new int[][] {//
				{b, w, b, e, e},//
				{b, w, b, e, e},//
				{e, e, e, w, w},//
				{b, w, w, b, b},//
				{b, w, w, b, e},//
		};
		int[][] expectedBoard = new int[][] {//
				{b, e, b, e, e},//
				{b, e, b, e, e},//
				{e, b, e, w, w},//
				{b, w, w, b, b},//
				{b, w, w, b, e},//
		};
		
		Referee ref = getReferee(board, PlayerColor.BLACK);
		
		//create the next move
		Move move = new Move(2, 1, PlayerColor.BLACK);
		//add the stone
		board[move.getRow()][move.getCol()] = move.getColor().getCode();
		//remove the beaten stones
		ref.removeBeaten(move);
		
		assertTrue(Referee.boardsEqual(expectedBoard, board));
	}
	
	@Test
	public void testRemoveBeaten_twoGroupsBeaten() {
		int[][] board = new int[][] {//
				{b, w, b, e, e},//
				{b, w, b, e, e},//
				{e, e, e, w, w},//
				{b, w, w, b, b},//
				{b, w, w, b, e},//
		};
		int[][] expectedBoard = new int[][] {//
				{e, w, b, e, e},//
				{e, w, b, e, e},//
				{w, e, e, w, w},//
				{e, w, w, b, b},//
				{e, w, w, b, e},//
		};
		
		Referee ref = getReferee(board, PlayerColor.WHITE);
		
		//create the next move
		Move move = new Move(2, 0, PlayerColor.WHITE);
		//add the stone
		board[move.getRow()][move.getCol()] = move.getColor().getCode();
		//remove the beaten stones
		ref.removeBeaten(move);
		
		assertTrue(Referee.boardsEqual(expectedBoard, board));
	}
	
	@Test
	public void testRemoveBeaten_noLibertiesBeforeGroupIsBeaten() {
		int[][] board = new int[][] {//
				{b, w, b, e, e},//
				{b, w, b, e, e},//
				{e, e, e, w, w},//
				{b, w, w, b, b},//
				{b, w, w, b, e},//
		};
		int[][] expectedBoard = new int[][] {//
				{b, w, b, e, e},//
				{b, w, b, e, e},//
				{e, e, e, w, w},//
				{b, w, w, e, e},//
				{b, w, w, e, w},//
		};
		
		Referee ref = getReferee(board, PlayerColor.WHITE);
		
		//create the next move
		Move move = new Move(4, 4, PlayerColor.WHITE);
		//add the stone
		board[move.getRow()][move.getCol()] = move.getColor().getCode();
		//remove the beaten stones
		ref.removeBeaten(move);
		
		assertTrue(Referee.boardsEqual(expectedBoard, board));
	}
	
	@Test
	public void testAddStone_simple() {
		int[][] board = new int[][] {//
				{b, w, b, e, e},//
				{b, w, b, e, e},//
				{e, e, e, w, w},//
				{b, w, w, b, b},//
				{b, w, w, b, e},//
		};
		int[][] expectedBoard = new int[][] {//
				{b, w, b, e, b},//
				{b, w, b, e, e},//
				{e, e, e, w, w},//
				{b, w, w, b, b},//
				{b, w, w, b, e},//
		};
		
		Referee ref = getReferee(board, PlayerColor.BLACK);
		
		//create the next move
		Move move = new Move(0, 4, PlayerColor.BLACK);
		//add the stone
		ref.addMove(move);
		
		assertTrue(Referee.boardsEqual(expectedBoard, board));
	}
	
	@Test
	public void testAddStone_groupBeaten() {
		int[][] board = new int[][] {//
				{b, w, b, e, e},//
				{b, w, b, e, e},//
				{e, e, e, w, w},//
				{b, w, w, b, b},//
				{b, w, w, b, e},//
		};
		int[][] expectedBoard = new int[][] {//
				{b, e, b, e, e},//
				{b, e, b, e, e},//
				{e, b, e, w, w},//
				{b, w, w, b, b},//
				{b, w, w, b, e},//
		};
		
		Referee ref = getReferee(board, PlayerColor.BLACK);
		
		//create the next move
		Move move = new Move(2, 1, PlayerColor.BLACK);
		//add the stone
		ref.addMove(move);
		
		assertTrue(Referee.boardsEqual(expectedBoard, board));
	}
	
	@Test
	public void testIsValidMove_valid() {
		int[][] board = new int[][] {//
				{b, b, b, e, e},//
				{b, w, w, w, e},//
				{e, b, b, e, e},//
				{b, w, w, b, b},//
				{w, w, w, b, e},//
		};
		
		Referee ref = getReferee(board, PlayerColor.BLACK);
		Move move = new Move(0, 4, PlayerColor.BLACK);
		
		assertTrue(ref.isValidMove(move));
	}
	
	@Test
	public void testIsValidMove_validPass() {
		int[][] board = new int[][] {//
				{b, b, b, e, e},//
				{b, w, w, w, e},//
				{e, b, b, e, e},//
				{b, w, w, b, b},//
				{w, w, w, b, e},//
		};
		
		Referee ref = getReferee(board, PlayerColor.BLACK);
		Move move = Move.getPassMove(PlayerColor.BLACK);
		
		assertTrue(ref.isValidMove(move));
	}
	
	@Test
	public void testIsValidMove_wrongPlayer() {
		int[][] board = new int[][] {//
				{b, b, b, e, e},//
				{b, w, w, w, e},//
				{e, b, b, e, e},//
				{b, w, w, b, b},//
				{w, w, w, b, e},//
		};
		
		Referee ref = getReferee(board, PlayerColor.BLACK);
		Move move = Move.getPassMove(PlayerColor.WHITE);
		
		assertFalse(ref.isValidMove(move));
	}
	
	@Test
	public void testIsValidMove_fieldNotEmpty() {
		int[][] board = new int[][] {//
				{b, b, b, e, e},//
				{b, w, w, w, e},//
				{e, b, b, e, e},//
				{b, w, w, b, b},//
				{w, w, w, b, e},//
		};
		
		Referee ref = getReferee(board, PlayerColor.BLACK);
		Move move = new Move(0, 0, PlayerColor.BLACK);
		
		assertFalse(ref.isValidMove(move));
	}
	
	@Test
	public void testIsValidMove_suicidalMove() {
		int[][] board = new int[][] {//
				{b, b, b, e, e},//
				{b, w, w, w, e},//
				{e, b, b, e, e},//
				{b, w, w, b, b},//
				{w, w, w, b, e},//
		};
		
		Referee ref = getReferee(board, PlayerColor.WHITE);
		Move move = new Move(4, 4, PlayerColor.WHITE);
		
		assertFalse(ref.isValidMove(move));
	}
	
	@Test
	public void testIsValidMove_koRuleViolated() {
		int[][] board = new int[][] {//
				{b, b, b, e, e},//
				{b, w, w, w, e},//
				{e, b, e, e, e},//
				{b, w, w, b, b},//
				{w, w, w, b, e},//
		};
		
		Referee ref = getReferee(board, PlayerColor.WHITE);
		Move move = new Move(2, 0, PlayerColor.WHITE);
		Move koViolatingMove = new Move(3, 0, PlayerColor.BLACK);
		
		ref.addMove(move);
		
		assertFalse(ref.isValidMove(koViolatingMove));
	}
	
	@Test
	public void testFillBoard_simpleValidMoves() {
		Referee ref = getReferee(new int[5][5], PlayerColor.BLACK);
		int[][] expectedBoard = new int[][] {//
				{b, w, e, e, e},//
				{b, w, e, e, e},//
				{e, e, b, w, e},//
				{e, e, b, w, e},//
				{b, w, e, e, e},//
		};
		List<Move> fieldPositions = toMoves(new int[][] {{0, 0}, {0, 1}, {1, 0}, {1, 1}, {2, 2}, {2, 3}, {3, 2}, {3, 3}, {4, 0}, {4, 1}},
				PlayerColor.BLACK);
		
		ref.fillBoard(fieldPositions);
		
		assertTrue(Referee.boardsEqual(expectedBoard, ref.getBoardCopy()));
	}
	
	@Test
	public void testFillBoard_validMovesBeatingGroups() {
		Referee ref = getReferee(new int[5][5], PlayerColor.BLACK);
		int[][] expectedBoard = new int[][] {//
				{e, w, e, e, e},//
				{e, w, e, e, e},//
				{w, b, e, e, e},//
				{e, e, e, e, e},//
				{e, e, e, e, e},//
		};
		List<Move> fieldPositions = toMoves(new int[][] {{0, 0}, {0, 1}, {1, 0}, {1, 1}, {2, 1}, {2, 0}}, PlayerColor.BLACK);
		
		ref.fillBoard(fieldPositions);
		
		assertTrue(Referee.boardsEqual(expectedBoard, ref.getBoardCopy()));
	}
	
	@Test
	public void testFillBoard_illegalMove() {
		Referee ref = getReferee(new int[5][5], PlayerColor.BLACK);
		List<Move> fieldPositions = toMoves(new int[][] {{0, 0}, {1, 1}, {0, 0}}, PlayerColor.BLACK);
		
		assertThrows(IllegalArgumentException.class, () -> ref.fillBoard(fieldPositions));
	}
}