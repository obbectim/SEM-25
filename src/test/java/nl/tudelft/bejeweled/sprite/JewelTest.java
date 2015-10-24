package nl.tudelft.bejeweled.sprite;

import org.junit.Before;
import org.junit.Test;

import nl.tudelft.bejeweled.jewel.BasicJewel;
import nl.tudelft.bejeweled.jewel.Jewel;

import static org.junit.Assert.assertEquals;

/**
 * Various tests for the jewel.getSprite() class.
 * Tests selection.
 * @author Pim Veldhuisen
 */


public class JewelTest {
	private static final int TYPE = 3;
	private static final int GRID_I = 4;
	private static final int GRID_J = 5;
	private static final int GRID_I_NEW = 6;
	private static final int GRID_J_NEW = 7;
	private static final int SPRITE_SIZE = 64;
	private static final double SMALL_V_X = .2;
	private static final double SMALL_V_Y = .01;
	private static final double LARGE_V_X = 10000;
	private static final double LARGE_V_Y = 20;
	private static final double DELTA = 0.00001;


	
	private Jewel jewel;
	
	/**
	 * Creates the jewel.getSprite() to test with.
	 */
	@Before
	public void setUp() {
		jewel = new BasicJewel(TYPE, GRID_I, GRID_J, GRID_I*SPRITE_SIZE, GRID_J*SPRITE_SIZE );
	}
	
	/**
	 * Test the type setting and getting.
	 */
	@Test
	public void getTypeTest() {
		assertEquals(TYPE, jewel.getType());
	}
	
	/**
	 * Test the positioning.
	 */
	@Test
	public void getPositionTest() {
		assertEquals(GRID_I, jewel.getBoardX());
		assertEquals(GRID_J, jewel.getBoardY());
	}
	
	/**
	 * Test the setting of the positioning.
	 */
	@Test
	public void setPositionTest() {
		jewel.setBoardX(GRID_I_NEW);
		jewel.setBoardY(GRID_J_NEW);
		assertEquals(GRID_I_NEW, jewel.getBoardX());
		assertEquals(GRID_J_NEW, jewel.getBoardY());
	}
	
	/**
	 * Test the velocity for a small (remaining) translation.
	 */
	@Test
	public void velocitySmallTest() {
		jewel.getSprite().getNode().setTranslateX(SMALL_V_X);
		jewel.getSprite().getNode().setTranslateY(SMALL_V_Y);
    	jewel.getSprite().update();
		assertEquals(SMALL_V_X, jewel.getSprite().getvX(), DELTA);
		assertEquals(SMALL_V_Y, jewel.getSprite().getvY(), DELTA);
		assertEquals(0, jewel.getSprite().getNode().getTranslateX(), DELTA);
		assertEquals(0, jewel.getSprite().getNode().getTranslateY(), DELTA);

	}
	
	/**
	 * Test the velocity for a big translation.
	 */
	@Test
	public void velocityMaxTest() {
		jewel.getSprite().getNode().setTranslateX(LARGE_V_X);
		jewel.getSprite().getNode().setTranslateY(LARGE_V_Y);
    	jewel.update();
		assertEquals(jewel.getSprite().MAX_SPEED_X, jewel.getSprite().getvX(), DELTA);
		assertEquals(jewel.getSprite().MAX_SPEED_Y, jewel.getSprite().getvY(), DELTA);
		assertEquals(LARGE_V_X - jewel.getSprite().MAX_SPEED_X, jewel.getSprite().getNode().getTranslateX(), DELTA);
		assertEquals(LARGE_V_Y - jewel.getSprite().MAX_SPEED_Y, jewel.getSprite().getNode().getTranslateY(), DELTA);
	}
	
	/**
	 * Test the velocity for a small (remaining) translation that's negative.
	 */
	@Test
	public void velocitySmallNegativeTest() {
		jewel.getSprite().getNode().setTranslateX(-SMALL_V_X);
		jewel.getSprite().getNode().setTranslateY(-SMALL_V_Y);
    	jewel.update();
		assertEquals(-SMALL_V_X, jewel.getSprite().getvX(), DELTA);
		assertEquals(-SMALL_V_Y, jewel.getSprite().getvY(), DELTA);
		assertEquals(0, jewel.getSprite().getNode().getTranslateX(), DELTA);
		assertEquals(0, jewel.getSprite().getNode().getTranslateY(), DELTA);

	}
	
	/**
	 * Test the velocity for a big translation that's negative.
	 */
	@Test
	public void velocityMaxNegativeTest() {
		jewel.getSprite().getNode().setTranslateX(-LARGE_V_X);
		jewel.getSprite().getNode().setTranslateY(-LARGE_V_Y);
    	jewel.update();
		assertEquals(-jewel.getSprite().MAX_SPEED_X, jewel.getSprite().getvX(), DELTA);
		assertEquals(-jewel.getSprite().MAX_SPEED_Y, jewel.getSprite().getvY(), DELTA);
		assertEquals(-(LARGE_V_X - jewel.getSprite().MAX_SPEED_X), jewel.getSprite().getNode().getTranslateX(), DELTA);
		assertEquals(-(LARGE_V_Y - jewel.getSprite().MAX_SPEED_Y), jewel.getSprite().getNode().getTranslateY(), DELTA);
	}
	
	/**
	 * Test the velocity at implosion.
	 */
	@Test
	public void implodeVelocityTest() {
		jewel.getSprite().getNode().setTranslateX(LARGE_V_X);
		jewel.getSprite().getNode().setTranslateY(LARGE_V_Y);
    	jewel.update();
    	jewel.implode(null);
		assertEquals(0, jewel.getSprite().getvX(), DELTA);
		assertEquals(0, jewel.getSprite().getvY(), DELTA);
	}
	
	
}
