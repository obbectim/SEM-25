package nl.tudelft.bejeweled.board;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import javafx.collections.ObservableList;
import nl.tudelft.bejeweled.jewel.BasicJewel;
import nl.tudelft.bejeweled.jewel.Jewel;
import nl.tudelft.bejeweled.sprite.SpriteStore;

/**
 * Various tests for the Board class.
 * Tests selection.
 * @author Pim Veldhuisen
 */
public class BoardTest {
	private static final int TEST_RESULT_SIZE_1 = 1;
	private static final int TEST_RESULT_SIZE_2 = 0;
	
	private BoardFactory boardFactory;
	private javafx.scene.Group mockGroup;
	private Board selectionBoard;
	
	/**
	 * Creates the board to test.
	 */
	@Before
	public void setUp() {
		SpriteStore mockSpriteStore = mock(SpriteStore.class);
		boardFactory = new BoardFactory(mockSpriteStore);
		mockGroup = mock(javafx.scene.Group.class);
		when(mockGroup.getChildren()).thenReturn(mock(ObservableList.class));
		selectionBoard = boardFactory.generateBoard(mockGroup);
	}
	
	/**
	 * Test the increase and reset of the selection size when adding jewels.
	 */
	@Test
	public void testSelectionSize() {
		Jewel jewel1 = new BasicJewel(0, 0, 0, 0, 0);
		selectionBoard.addSelection(jewel1);
		assertEquals(TEST_RESULT_SIZE_1, selectionBoard.getSelection().size());
		Jewel jewel2 = mock(Jewel.class);
		selectionBoard.addSelection(jewel2);
		assertEquals(TEST_RESULT_SIZE_2, selectionBoard.getSelection().size());
	}

	/**
	 * Test if jewels are correctly stored and retrieved.
	 */
	@Test
	public void retriveSelection() {
		Jewel jewel = new BasicJewel(0, 0, 0, 0, 0);
		selectionBoard.addSelection(jewel);
		assertEquals(jewel, selectionBoard.getSelection().get(0));
	}
	
	/**
	 * Test if a selection cursor is created.
	 */
	@Test
	public void selectionCursorCreation() {
		assertNull(selectionBoard.getSelectionCursor());
		Jewel jewel = new BasicJewel(0, 0, 0, 0, 0);
		selectionBoard.addSelection(jewel);
		assertNotNull(selectionBoard.getSelectionCursor());
	}
	
}
