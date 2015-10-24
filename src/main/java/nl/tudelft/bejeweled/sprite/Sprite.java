package nl.tudelft.bejeweled.sprite;

import javafx.scene.Node;

/**
 * Created by Jeroen on 4-9-2015.
 * Class to hold basic Sprite information in the game.
 */
public abstract class Sprite {
    /** Animation for the node. */
//    private List animations = new ArrayList<>();

    /** Current display node. */
    private Node node;

    /** X  screen position. */
    protected int xPos;

    /** Y screen position. */
    protected int yPos;

    /** velocity vector x direction. */
    private double vX = 0;

    /** velocity vector y direction. */
    private double vY = 0;

    /** Status variable, describes the current state of the sprite. */
    private SpriteState state = SpriteState.IDLE;

    /**
     * Updates this sprite object's velocity, or animations.
     */
    public abstract void update();

    /**
     * Getter method for node.
     * @return The node linked to the sprite
     */
	public Node getNode() {
		return node;
	}
	
    /**
     * Setter method for node.
     * @param node Node to be set
     */
	public void setNode(Node node) {
		this.node = node;
	}
	
    /**
     * Getter method for xPos.
     * @return the current x position in pixels
     */
	public int getxPos() {
		return xPos;
	}

    /**
     * Setter method for xPos.
     * @param xPos x position in pixels to be set
     */
	protected void setxPos(int xPos) {
		this.xPos = xPos;
	}

    /**
     * Getter method for yPos.
     * @return the current y position in pixels
     */
	public int getyPos() {
		return yPos;
	}
    /**
     * Setter method for yPos.
     * @param yPos y position in pixels to be set
     */
	protected void setyPos(int yPos) {
		this.yPos = yPos;
	}

    /**
     * Gets the current state of the sprite.
     * @return The current state of the sprite
     */
	public SpriteState getState() {
		return state;
	}
	
    /**
     * Set sprite state.
     * @param newState The new state for this sprite.
     */
	public void setState(SpriteState newState) {
		this.state = newState;
	}
	
	 /**
     * Getter method for vX.
     * @return the current x velocity in pixels/update
     */
	public double getvX() {
		return vX;
	}

	 /**
     * Setter method for vX.
     * @param vX x velocity in pixels/update to be set
     */
	protected void setvX(double vX) {
		this.vX = vX;
	}
	
	 /**
     * Getter method for vY.
     * @return the current y velocity in pixels/update
     */
	public double getvY() {
		return vY;
	}

	 /**
     * Setter method for vY.
     * @param vY y velocity in pixels/update to be set
     */
	protected void setvY(double vY) {
		this.vY = vY;
	}
	
    /**
     * Function to move the sprite to a certain position via animation.
     * @param x the new x position in pixels.
     * @param y the new y position in pixels.
     */
    public void moveTo(int x, int y) {
    	this.getNode().setTranslateX(this.getxPos() - x);
    	this.getNode().setTranslateY(this.getyPos() - y);
    	this.setxPos(x);
    	this.setyPos(y);
    }
    
    /**
     * Function to move the sprite to a relative position via animation.
     * @param deltaX the change of x position in pixels.
     * @param deltaY the change of y position in pixels.
     */
      public void relativeMoveTo(int deltaX, int deltaY) {
    	this.getNode().setTranslateX(-deltaX);
    	this.getNode().setTranslateY(-deltaY);
    	this.setxPos(this.getxPos() + deltaX);
    	this.setyPos(this.getyPos() + deltaY);
    }
    
      
	
	 /**
    * Method to check is an animation for this sprite is still in progress.
    * @return true if the animation is still active
    */
	public boolean animationActive() {
		return (state == SpriteState.ANIMATION_ACTIVE
				|| (getNode().getTranslateX() != 0
				|| getNode().getTranslateY() != 0));
	}
}
