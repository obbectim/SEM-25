package nl.tudelft.bejeweled.board;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javafx.collections.ObservableList;
import nl.tudelft.bejeweled.sprite.SpriteStore;

/**
 * Test if the board removes jewels if the are part of a combo.
 * 
 * @author Pim Veldhuisen
 */

public class RemoveCombosTest {
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
	public void verifyRemovedEmptyBoard() {
		Board empty = boardFactory.fromTextGenerateBoard("/boards/Empty.txt", mockGroup, GRID_WIDTH, GRID_HEIGHT, SPRITE_WIDTH, SPRITE_HEIGHT);
		int removed = empty.checkBoardCombos();
       	assertEquals(0, removed);

	}
	
	@Test
	public void verifyRemovedRandomNoCombos() {
		Board randomNoCombos = boardFactory.fromTextGenerateBoard("/boards/RandomNoCombos.txt", mockGroup, GRID_WIDTH, GRID_HEIGHT, SPRITE_WIDTH, SPRITE_HEIGHT);
		int removed = randomNoCombos.checkBoardCombos();
       	assertEquals(0, removed);

	}
	
	
	@Test
	public void verifyRemovedBoard1LineOf3() {
        Board board1LineOf3 = boardFactory.fromTextGenerateBoard("/boards/1LineOf3.txt", mockGroup, GRID_WIDTH, GRID_HEIGHT, SPRITE_WIDTH, SPRITE_HEIGHT);
		int removed = board1LineOf3.checkBoardCombos();
       	assertEquals(3, removed);

	}
	
	@Test
	public void verifyRemovedBoard1LineOf4() {
        Board board1LineOf4 = boardFactory.fromTextGenerateBoard("/boards/1LineOf4.txt", mockGroup, GRID_WIDTH, GRID_HEIGHT, SPRITE_WIDTH, SPRITE_HEIGHT);
		int removed = board1LineOf4.checkBoardCombos();
       	assertEquals(4, removed);

	}
	
	@Test
	public void verifyRemovedBoard1LineOf5() {
        Board board1LineOf5 = boardFactory.fromTextGenerateBoard("/boards/1LineOf5.txt", mockGroup, GRID_WIDTH, GRID_HEIGHT, SPRITE_WIDTH, SPRITE_HEIGHT);
		int removed = board1LineOf5.checkBoardCombos();
       	assertEquals(5, removed);

	}
	
	
	
	@Test
	public void verifyRemovedBoard3LinesOf3() {
		Board board3LinesOf3 = boardFactory.fromTextGenerateBoard("/boards/3LinesOf3.txt", mockGroup, GRID_WIDTH, GRID_HEIGHT, SPRITE_WIDTH, SPRITE_HEIGHT);
		int removed = board3LinesOf3.checkBoardCombos();
       	assertEquals(9, removed);

	}
	
	@Test
	public void verifyRemovedTShape() {
		Board tShape = boardFactory.fromTextGenerateBoard("/boards/TShape.txt", mockGroup, GRID_WIDTH, GRID_HEIGHT, SPRITE_WIDTH, SPRITE_HEIGHT);
		int removed = tShape.checkBoardCombos();
       	assertEquals(5, removed);

	}

	@Test
	public void verifyRemovedLShape() {
		Board lShape = boardFactory.fromTextGenerateBoard("/boards/LShape.txt", mockGroup, GRID_WIDTH, GRID_HEIGHT, SPRITE_WIDTH, SPRITE_HEIGHT);
		int removed = lShape.checkBoardCombos();
       	assertEquals(5, removed);

	}
	
	@Test
	public void verifyRemovedTopRow() {
		Board topRow = boardFactory.fromTextGenerateBoard("/boards/TopRow.txt", mockGroup, GRID_WIDTH, GRID_HEIGHT, SPRITE_WIDTH, SPRITE_HEIGHT);
		int removed = topRow.checkBoardCombos();
       	assertEquals(3, removed);

	}
	
	@Test
	public void verifyRemovedMultiple() {
		Board multiple = boardFactory.fromTextGenerateBoard("/boards/Multiple.txt", mockGroup, GRID_WIDTH, GRID_HEIGHT, SPRITE_WIDTH, SPRITE_HEIGHT);
		int removed = multiple.checkBoardCombos();
       	assertEquals(25, removed);

	}
}
