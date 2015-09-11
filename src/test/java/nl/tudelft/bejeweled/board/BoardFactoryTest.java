package nl.tudelft.bejeweled.board;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import javafx.scene.Group;
import javafx.scene.layout.Pane;
import nl.tudelft.bejeweled.sprite.SpriteStore;
import nl.tudelft.bejeweled.sprite.Jewel;

public class BoardFactoryTest {
	/**
	 * The factory under test.
	 */
	private BoardFactory factory;
	
	private SpriteStore spriteStore;
	
	private Pane gamePane = new Pane();
	
	/**
	 * Creates the factory object.
	 */
	@Before
	public void setUp() {
		spriteStore = new SpriteStore();
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
			for (int j = 0; j < grid[0].length; j++){
				assertNotNull(grid[i][j]);
			}
		}
	}

}
