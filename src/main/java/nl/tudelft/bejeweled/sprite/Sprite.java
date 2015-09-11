package nl.tudelft.bejeweled.sprite;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Node;

/**
 * Created by Jeroen on 4-9-2015.
 * Class to hold basic Sprite information in the game.
 */
public abstract class Sprite {
    /** Animation for the node. */
    protected List animations = new ArrayList<>();

    /** Current display node. */
    private Node node;

    /** X  screen position. */
    private double xPos;

    /** Y screen position. */
    private double yPos;

    /** velocity vector x direction. */
    protected double vX = 0;

    /** velocity vector y direction. */
    protected double vY = 0;

    protected Boolean isMoving;

    /** Status variable, true if the sprite should be removed */
    private boolean isDead = false;

    /**
     * Updates this sprite object's velocity, or animations.
     */
    public abstract void update();

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public double getxPos() {
		return xPos;
	}

	public void setxPos(double xPos) {
		this.xPos = xPos;
	}

	public double getyPos() {
		return yPos;
	}

	public void setyPos(double yPos) {
		this.yPos = yPos;
	}

	public boolean isDead() {
		return isDead;
	}

	public void setDead(boolean isDead) {
		this.isDead = isDead;
	}
}
