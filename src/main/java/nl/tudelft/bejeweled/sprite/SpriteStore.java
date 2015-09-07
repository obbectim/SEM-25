package nl.tudelft.bejeweled.sprite;

import java.util.*;

/**
 * Created by Jeroen on 4-9-2015.
 * Class that manages all the sprites/actors in the game.
 */
public class SpriteStore {

    /** All the sprite objects currently in play */
    private final static List listActors = new ArrayList<>();

    /** A global single threaded set used to cleanup or remove sprite objects
     * in play.
     */
    private final static Set listDeadSprites = new HashSet<>();

    /**
     * Get the list of all currently active sprites in the game.
     * @return List of all actors.
     */
    public List getAllSprites() {
        return listActors;
    }

    /**
     * VarArgs of sprite objects to be added to the game.
     * @param sprite Sprite object to add to the list.
     */
    public void addSprites(Sprite sprite) {
        listActors.add(sprite);
    }

    /**
     * Remove one single sprite in the store from the game.
     * @param sprite The sprite to be removed.
     */
    public void removeSprite(Sprite sprite) {
        listActors.remove(sprite);
    }

    /**
     * Removes all the sprites in the store from the game.
     */
    public void removeAllSprites() {
        listActors.clear();
    }

    /** Returns a set of sprite objects to be removed from the listActors.
     * @return listDeadSprites
     */
    public Set getSpritesToBeRemoved() {
        return listDeadSprites;
    }

    /**
     * Adds sprite objects to be removed
     * @param sprites varargs of sprite objects.
     */
    public void addSpritesToBeRemoved(Sprite... sprites) {
        if (sprites.length > 1) {
            listDeadSprites.addAll(Arrays.asList((Sprite[]) sprites));
        } else {
            listDeadSprites.add(sprites[0]);
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
        listActors.removeAll(listDeadSprites);

        // reset the clean up sprites
        listDeadSprites.clear();
    }
}