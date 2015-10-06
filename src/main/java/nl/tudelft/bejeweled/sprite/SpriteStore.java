package nl.tudelft.bejeweled.sprite;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Jeroen on 4-9-2015.
 * Class that manages all the sprites/actors in the game.
 */
public class SpriteStore implements Serializable {

    /** All the sprite objects currently in play. */
    private static final List LIST_ACTORS = new ArrayList<>();

    /** A global single threaded set used to cleanup or remove sprite objects
     * in play.
     */
    private static final Set LIST_DEAD_SPRITES = new HashSet<>();

    /**
     * Get the list of all currently active sprites in the game.
     * @return List of all actors.
     */
    public List getAllSprites() {
        return LIST_ACTORS;
    }

    /**
     * VarArgs of sprite objects to be added to the game.
     * @param sprite Sprite object to add to the list.
     */
    public void addSprites(Sprite sprite) {
        LIST_ACTORS.add(sprite);
    }

    /**
     * Remove one single sprite in the store from the game.
     * @param sprite The sprite to be removed.
     */
    public void removeSprite(Sprite sprite) {
        LIST_ACTORS.remove(sprite);
    }

    /**
     * Removes all the sprites in the store from the game.
     */
    public void removeAllSprites() {
        LIST_ACTORS.clear();
    }

    /** Returns a set of sprite objects to be removed from the listActors.
     * @return listDeadSprites
     */
    public Set getSpritesToBeRemoved() {
        return LIST_DEAD_SPRITES;
    }

    /**
     * Adds sprite objects to be removed.
     * @param sprites varargs of sprite objects.
     */
    public void addSpritesToBeRemoved(Sprite... sprites) {
        if (sprites.length > 1) {
            LIST_DEAD_SPRITES.addAll(Arrays.asList((Sprite[]) sprites));
        } else {
            LIST_DEAD_SPRITES.add(sprites[0]);
        }
    }

    /**
     * Removes sprite objects and nodes from all
     * temporary collections such as:
     * listDeadSprites.
     * The sprite to be removed will also be removed from the
     * list of all sprite objects called (listActors).
     */
    public void cleanupSprites() {

        // remove from actors list
        LIST_ACTORS.removeAll(LIST_DEAD_SPRITES);

        // reset the clean up sprites
        LIST_DEAD_SPRITES.clear();
    }
}