package nl.tudelft.bejeweled.board;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javafx.collections.ObservableList;
import nl.tudelft.bejeweled.sprite.SpriteStore;

/**
 * Test if certain moves are valid.
 * @author Pim Veldhuisen
 */

public class ValidMovesTest {
	private BoardFactory boardFactory;
	private javafx.scene.Group mockGroup;
	
	
	/**
	 * Creates the board to test with.
	 */
	@Before
	public void setUp() {
		SpriteStore mockSpriteStore = mock(SpriteStore.class);
		boardFactory = new BoardFactory(mockSpriteStore);
		mockGroup = mock(javafx.scene.Group.class);
		when(mockGroup.getChildren()).thenReturn(mock(ObservableList.class));

	}
	
	/**
	 * Test out of moves if there are no moves.
	 */	
	@Test
	public void verifyNoValidMoves() {
		Board board = boardFactory.fromTextGenerateBoard("/boards/NoMoves.txt", mockGroup);
		assertTrue(board.outOfMoves());

	}
	
	/**
	 * Test not out of moves if there is a combo on the board.
	 */	
	@Test
	public void verifyCombosOnBoard() {
		Board board = boardFactory.fromTextGenerateBoard("/boards/1LineOf3.txt", mockGroup);
		assertFalse(board.outOfMoves());

	}
	
	/**
	 * Test not out of moves if there is no combo on the board, but there is a move possible.
	 */	
	@Test
	public void verifyNoCombosButMoves() {
		Board board = boardFactory.fromTextGenerateBoard("/boards/NoCombosButMoves.txt", mockGroup);
		assertFalse(board.outOfMoves());

	}
	
	
}
