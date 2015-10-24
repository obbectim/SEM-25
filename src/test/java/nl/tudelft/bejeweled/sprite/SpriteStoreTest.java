package nl.tudelft.bejeweled.sprite;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import nl.tudelft.bejeweled.jewel.Jewel;

/**
 * Created by Jeroen on 10-9-2015.
 * Test class for the sprites
 */
public class SpriteStoreTest {

    private SpriteStore spriteStore;

    /**
     * Setup the spritestore object to be tested.
     */
    @Before
    public void setUp() {
        spriteStore = new SpriteStore();
    }

    /**
     * Test adding 1 sprite.
     */
    @Test
    public void addSprite() {
        Jewel jewel = mock(Jewel.class);

        spriteStore.addSprite(jewel.getSprite());
        assertTrue(spriteStore.getAllSprites().contains(jewel.getSprite()));
    }

    /**
     * Test adding multiple sprites.
     */
    @Test
    public void addMultipleSprites() {
        Jewel jewel1 = mock(Jewel.class);
        Jewel jewel2 = mock(Jewel.class);
        Jewel jewel3 = mock(Jewel.class);

        spriteStore.addSprite(jewel1.getSprite());
        spriteStore.addSprite(jewel2.getSprite());
        spriteStore.addSprite(jewel3.getSprite());
        
        assertTrue(spriteStore.getAllSprites().contains(jewel1.getSprite()));
        assertTrue(spriteStore.getAllSprites().contains(jewel2.getSprite()));
        assertTrue(spriteStore.getAllSprites().contains(jewel3.getSprite()));
    }

    /**
     * Test removing 1 sprite.
     */
    @Test
    public void removeSprite() {
        Jewel jewel = mock(Jewel.class);

        spriteStore.addSprite(jewel.getSprite());
        assertTrue(spriteStore.getAllSprites().contains(jewel.getSprite()));

        spriteStore.removeSprite(jewel.getSprite());
        assertFalse(spriteStore.getAllSprites().contains(jewel.getSprite()));
    }

    /**
     * Test removing multiple sprites.
     */
    @Test
    public void removeAll() {
    	Jewel jewel1 = mock(Jewel.class);
        Jewel jewel2 = mock(Jewel.class);
        Jewel jewel3 = mock(Jewel.class);
        
        spriteStore.addSprite(jewel1.getSprite());
        spriteStore.addSprite(jewel2.getSprite());
        spriteStore.addSprite(jewel3.getSprite());
        
        spriteStore.removeAllSprites();
        assertFalse(spriteStore.getAllSprites().contains(jewel1.getSprite()));
        assertFalse(spriteStore.getAllSprites().contains(jewel2.getSprite()));
        assertFalse(spriteStore.getAllSprites().contains(jewel3.getSprite()));
    }
}
