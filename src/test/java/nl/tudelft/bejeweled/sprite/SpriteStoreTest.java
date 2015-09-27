package nl.tudelft.bejeweled.sprite;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

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
        Jewel sprite = mock(Jewel.class);

        spriteStore.addSprites(sprite);
        assertTrue(spriteStore.getAllSprites().contains(sprite));
    }

    /**
     * Test adding multiple sprites.
     */
    @Test
    public void addMultipleSprites() {
        Jewel sprite1 = mock(Jewel.class);
        Jewel sprite2 = mock(Jewel.class);
        Jewel sprite3 = mock(Jewel.class);

        spriteStore.addSprites(sprite1);
        spriteStore.addSprites(sprite2);
        spriteStore.addSprites(sprite3);
        assertTrue(spriteStore.getAllSprites().contains(sprite1));
        assertTrue(spriteStore.getAllSprites().contains(sprite2));
        assertTrue(spriteStore.getAllSprites().contains(sprite3));
    }

    /**
     * Test removing 1 sprite.
     */
    @Test
    public void removeSprite() {
        Jewel sprite = mock(Jewel.class);

        spriteStore.addSprites(sprite);
        assertTrue(spriteStore.getAllSprites().contains(sprite));

        spriteStore.removeSprite(sprite);
        assertFalse(spriteStore.getAllSprites().contains(sprite));
    }

    /**
     * Test removing multiple sprites.
     */
    @Test
    public void removeAll() {
        Jewel sprite1 = mock(Jewel.class);
        Jewel sprite2 = mock(Jewel.class);
        Jewel sprite3 = mock(Jewel.class);

        spriteStore.addSprites(sprite1);
        spriteStore.addSprites(sprite2);
        spriteStore.addSprites(sprite3);
        
        spriteStore.removeAllSprites();
        assertFalse(spriteStore.getAllSprites().contains(sprite1));
        assertFalse(spriteStore.getAllSprites().contains(sprite2));
        assertFalse(spriteStore.getAllSprites().contains(sprite3));
    }
}
