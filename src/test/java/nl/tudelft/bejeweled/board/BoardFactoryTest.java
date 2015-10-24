package nl.tudelft.bejeweled.board;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import javafx.scene.Group;
import nl.tudelft.bejeweled.jewel.Jewel;
import nl.tudelft.bejeweled.sprite.SpriteStore;

/**
 * Test for BoardFactory to verify that board 
 * with no empty fields is created.
 */
public class BoardFactoryTest {
	/**
	 * The factory under test.
	 */
	private BoardFactory factory;
	
	private SpriteStore spriteStore;
	
	/**
	 * Creates the factory object.
	 */
	@Before
	public void setUp() {
		spriteStore = mock(SpriteStore.class);
        factory = new BoardFactory(spriteStore);
	}
	
	/**
	 * Verifies that a board with no empty fields is created.
	 */
	@Test
	public void noEmptySquares() {
		// generate the jewels
		Group nodes = new Group();
        Board board = factory.generateBoard(nodes);
        Jewel[][] grid = board.getGrid();
        for (int i = 0; i < grid.length; i++) { 
			for (int j = 0; j < grid[0].length; j++) {
				assertNotNull(grid[i][j]);
			}
		}
	}

}
