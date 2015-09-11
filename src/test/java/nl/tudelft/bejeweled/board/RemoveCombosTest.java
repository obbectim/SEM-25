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
	private static final int TEST_RESULT_3 = 3;
	private static final int TEST_RESULT_4 = 4;
	private static final int TEST_RESULT_5 = 5;
	private static final int TEST_RESULT_9 = 9;
	private static final int TEST_RESULT_MULTIPLE = 25;

		
	private BoardFactory boardFactory;
	private javafx.scene.Group mockGroup;
	
	/**
	 *  Setup the board for testing.
	 */
	@Before
	public void setUp() {
		SpriteStore mockSpriteStore = mock(SpriteStore.class);
		boardFactory = new BoardFactory(mockSpriteStore);
		mockGroup = mock(javafx.scene.Group.class);
		when(mockGroup.getChildren()).thenReturn(mock(ObservableList.class));

	}
	
	/**
	 *  Testcase for a board with no jewels.
	 */
	@Test
	public void verifyRemovedEmptyBoard() {
		String boardFile = "/boards/Empty.txt";
		Board empty = boardFactory.fromTextGenerateBoard(boardFile, mockGroup);
		int removed = empty.checkBoardCombos();
       	assertEquals(0, removed);

	}
	
	/**
	 *  Testcase for a board with no combos.
	 */
	@Test
	public void verifyRemovedRandomNoCombos() {
		String boardFile = "/boards/RandomNoCombos.txt";
		Board randomNoCombos = boardFactory.fromTextGenerateBoard(boardFile, mockGroup);
		int removed = randomNoCombos.checkBoardCombos();
       	assertEquals(0, removed);

	}
	
	
	/**
	 *  Testcase for a board with 1 combo of 3.
	 */
	@Test
	public void verifyRemovedBoard1LineOf3() {
		String boardFile = "/boards/1LineOf3.txt";
        Board board1LineOf3 = boardFactory.fromTextGenerateBoard(boardFile, mockGroup);
		int removed = board1LineOf3.checkBoardCombos();
       	assertEquals(TEST_RESULT_3, removed);

	}
	
	/**
	 *  Testcase for a board with 1 combo of 4.
	 */
	@Test
	public void verifyRemovedBoard1LineOf4() {
		String boardFile = "/boards/1LineOf4.txt";
        Board board1LineOf4 = boardFactory.fromTextGenerateBoard(boardFile, mockGroup);
		int removed = board1LineOf4.checkBoardCombos();
       	assertEquals(TEST_RESULT_4, removed);

	}
	
	/**
	 *  Testcase for a board with 1 combo of 5.
	 */
	@Test
	public void verifyRemovedBoard1LineOf5() {
		String boardFile = "/boards/1LineOf5.txt";
        Board board1LineOf5 = boardFactory.fromTextGenerateBoard(boardFile, mockGroup);
		int removed = board1LineOf5.checkBoardCombos();
       	assertEquals(TEST_RESULT_5, removed);

	}
	
	/**
	 *  Testcase for a board with 3 combos of 3.
	 */
	@Test
	public void verifyRemovedBoard3LinesOf3() {
		String boardFile = "/boards/3LinesOf3.txt";
		Board board3LinesOf3 = boardFactory.fromTextGenerateBoard(boardFile, mockGroup);
		int removed = board3LinesOf3.checkBoardCombos();
       	assertEquals(TEST_RESULT_9, removed);

	}
	
	/**
	 *  Testcase for a board with a T-Shape combo.
	 */
	@Test
	public void verifyRemovedTShape() {
		String boardFile = "/boards/TShape.txt";
		Board tShape = boardFactory.fromTextGenerateBoard(boardFile, mockGroup);
		int removed = tShape.checkBoardCombos();
       	assertEquals(TEST_RESULT_5, removed);

	}

	/**
	 *  Testcase for a board with a L-Shape combo.
	 */
	@Test
	public void verifyRemovedLShape() {
		String boardFile = "/boards/LShape.txt";
		Board lShape = boardFactory.fromTextGenerateBoard(boardFile, mockGroup);
		int removed = lShape.checkBoardCombos();
       	assertEquals(TEST_RESULT_5, removed);

	}
	
	/**
	 *  Testcase for a board with a combo in the top row.
	 */
	@Test
	public void verifyRemovedTopRow() {
		String boardFile = "/boards/TopRow.txt";
		Board topRow = boardFactory.fromTextGenerateBoard(boardFile, mockGroup);
		int removed = topRow.checkBoardCombos();
       	assertEquals(TEST_RESULT_3, removed);

	}
	
	/**
	 *  Testcase for a board with mulitple combos.
	 */
	@Test
	public void verifyRemovedMultiple() {
		String boardFile = "/boards/Multiple.txt";
		Board multiple = boardFactory.fromTextGenerateBoard(boardFile, mockGroup);
		int removed = multiple.checkBoardCombos();
       	assertEquals(TEST_RESULT_MULTIPLE, removed);

	}
}
