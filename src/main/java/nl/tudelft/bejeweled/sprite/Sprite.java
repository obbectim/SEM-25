package nl.tudelft.bejeweled.sprite;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Node;

/**
 * Created by Jeroen on 4-9-2015.
 * Class to hold basic Sprite information in the game.
 */
public abstract class Sprite {
    /** Animation for the node */
    public List animations = new ArrayList<>();

    /** Current display node */
    public Node node;

    /** X  screen position */
    public double xPos;

    public double finalXPos;

    /** Y screen position */
    public double yPos;

    public double finalYPos;

    /** velocity vector x direction */
    public double vX = 0;

    /** velocity vector y direction */
    public double vY = 0;

    public Boolean isMoving;

    /** exploded? */
    public boolean isDead = false;

    /**
     * Updates this sprite object's velocity, or animations.
     */
    public abstract void update();
}
