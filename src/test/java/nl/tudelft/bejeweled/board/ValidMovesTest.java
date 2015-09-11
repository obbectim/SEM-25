package nl.tudelft.bejeweled.board;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javafx.collections.ObservableList;
import nl.tudelft.bejeweled.sprite.SpriteStore;

/**
 * Test if certain moves are valid.
 * 
 * @author Pim Veldhuisen
 */

public class ValidMovesTest {
	private static final int GRID_WIDTH = 8;
	private static final int GRID_HEIGHT = 8;
	private static final int SPRITE_WIDTH = 64;
	private static final int SPRITE_HEIGHT = 64;
		
	private BoardFactory boardFactory;
	private javafx.scene.Group mockGroup;
	
	@Before
	public void setUp() {
		SpriteStore mockSpriteStore = mock(SpriteStore.class);
		boardFactory = new BoardFactory(mockSpriteStore);
		mockGroup = mock(javafx.scene.Group.class);
		when(mockGroup.getChildren()).thenReturn(mock(ObservableList.class));

	}
	
		
	@Test
	public void verifyNoValidMoves() {
		Board board = boardFactory.fromTextGenerateBoard("/boards/NoMoves.txt", mockGroup, GRID_WIDTH, GRID_HEIGHT, SPRITE_WIDTH, SPRITE_HEIGHT);
		assertTrue(board.outOfMoves());

	}
	
	@Test
	public void verifyCombosOnBoard() {
		Board board = boardFactory.fromTextGenerateBoard("/boards/1LineOf3.txt", mockGroup, GRID_WIDTH, GRID_HEIGHT, SPRITE_WIDTH, SPRITE_HEIGHT);
		assertFalse(board.outOfMoves());

	}
	
	@Test
	public void verifyNoCombosButMoves() {
		Board board = boardFactory.fromTextGenerateBoard("/boards/NoCombosButMoves.txt", mockGroup, GRID_WIDTH, GRID_HEIGHT, SPRITE_WIDTH, SPRITE_HEIGHT);
		assertFalse(board.outOfMoves());

	}
	
	
}
