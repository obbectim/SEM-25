package nl.tudelft.bejeweled.sprite;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Various tests for the Jewel class.
 * Tests selection.
 * @author Pim Veldhuisen
 */


public class JewelTest {
	private static final int TYPE = 3;
	private static final int GRID_I = 4;
	private static final int GRID_J = 5;
	private static final int GRID_I_NEW = 6;
	private static final int GRID_J_NEW = 7;
	private static final double SMALL_V_X = .2;
	private static final double SMALL_V_Y = .01;
	private static final double LARGE_V_X = 10000;
	private static final double LARGE_V_Y = 20;
	private static final double DELTA = 0.00001;


	
	Jewel jewel;
	@Before
	public void setUp() {
		jewel = new Jewel(TYPE, GRID_I, GRID_J);
	}
	
	@Test
	public void getTypeTest() {
		assertEquals(TYPE, jewel.getType());
	}
	
	@Test
	public void getPositionTest() {
		assertEquals(GRID_I, jewel.getBoardX());
		assertEquals(GRID_J, jewel.getBoardY());
	}
	
	@Test
	public void setPositionTest() {
		jewel.setBoardX(GRID_I_NEW);
		jewel.setBoardY(GRID_J_NEW);
		assertEquals(GRID_I_NEW, jewel.getBoardX());
		assertEquals(GRID_J_NEW, jewel.getBoardY());
	}
	
	@Test
	public void velocitySmallTest() {
		jewel.getNode().setTranslateX(SMALL_V_X);
		jewel.getNode().setTranslateY(SMALL_V_Y);
    	jewel.update();
		assertEquals(SMALL_V_X, jewel.getvX(),DELTA);
		assertEquals(SMALL_V_Y, jewel.getvY(),DELTA);
		assertEquals(0, jewel.getNode().getTranslateX(),DELTA);
		assertEquals(0, jewel.getNode().getTranslateY(),DELTA);

	}
	
	@Test
	public void velocityMaxTest() {
		jewel.getNode().setTranslateX(LARGE_V_X);
		jewel.getNode().setTranslateY(LARGE_V_Y);
    	jewel.update();
		assertEquals(Jewel.MAX_SPEED_X, jewel.getvX(),DELTA);
		assertEquals(Jewel.MAX_SPEED_Y, jewel.getvY(),DELTA);
		assertEquals(LARGE_V_X - Jewel.MAX_SPEED_X, jewel.getNode().getTranslateX(),DELTA);
		assertEquals(LARGE_V_Y - Jewel.MAX_SPEED_Y, jewel.getNode().getTranslateY(),DELTA);
	}
	
	@Test
	public void velocitySmallNegativeTest() {
		jewel.getNode().setTranslateX(-SMALL_V_X);
		jewel.getNode().setTranslateY(-SMALL_V_Y);
    	jewel.update();
		assertEquals(-SMALL_V_X, jewel.getvX(),DELTA);
		assertEquals(-SMALL_V_Y, jewel.getvY(),DELTA);
		assertEquals(0, jewel.getNode().getTranslateX(),DELTA);
		assertEquals(0, jewel.getNode().getTranslateY(),DELTA);

	}
	
	@Test
	public void velocityMaxNegativeTest() {
		jewel.getNode().setTranslateX(-LARGE_V_X);
		jewel.getNode().setTranslateY(-LARGE_V_Y);
    	jewel.update();
		assertEquals(-Jewel.MAX_SPEED_X, jewel.getvX(),DELTA);
		assertEquals(-Jewel.MAX_SPEED_Y, jewel.getvY(),DELTA);
		assertEquals(-(LARGE_V_X - Jewel.MAX_SPEED_X), jewel.getNode().getTranslateX(),DELTA);
		assertEquals(-(LARGE_V_Y - Jewel.MAX_SPEED_Y), jewel.getNode().getTranslateY(),DELTA);
	}
	
	@Test
	public void ImplodeVelocityTest() {
		jewel.getNode().setTranslateX(LARGE_V_X);
		jewel.getNode().setTranslateY(LARGE_V_Y);
    	jewel.update();
    	jewel.implode(null);
		assertEquals(0, jewel.getvX(),DELTA);
		assertEquals(0, jewel.getvY(),DELTA);
	}
	
	
}
