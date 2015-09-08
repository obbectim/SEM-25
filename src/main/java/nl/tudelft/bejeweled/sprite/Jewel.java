package nl.tudelft.bejeweled.sprite;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import nl.tudelft.bejeweled.game.Game;
import javafx.animation.FadeTransitionBuilder;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

/**
 * Created by Jeroen on 5-9-2015.
 * Jewel class that holds all sprite information.
 */
public class Jewel extends Sprite {

    private final int type;

    private int boardX, boardY;

    private Image jewelImage;

    /**
     * Constructor for Jewel class
     * @param type The type of Jewel created.
     * @param boardX The X position of this Jewel on the board grid.
     * @param boardY The Y position of this Jewel on the board grid.
     */
    public Jewel(int type, int boardX, int boardY) {
        this.type = type;
        this.boardX = boardX;
        this.boardY = boardY;

        //TODO This can be done better by constructing the file directory string on the fly
        switch(type) {
            case 1:
                jewelImage = new Image(Jewel.class.getResourceAsStream("/1.png"));
                break;
            case 2:
                jewelImage = new Image(Jewel.class.getResourceAsStream("/2.png"));
                break;
            case 3:
                jewelImage = new Image(Jewel.class.getResourceAsStream("/3.png"));
                break;
            case 4:
                jewelImage = new Image(Jewel.class.getResourceAsStream("/4.png"));
                break;
            case 5:
                jewelImage = new Image(Jewel.class.getResourceAsStream("/5.png"));
                break;
            case 6:
                jewelImage = new Image(Jewel.class.getResourceAsStream("/6.png"));
                break;
            case 7:
                jewelImage = new Image(Jewel.class.getResourceAsStream("/7.png"));
                break;
        }

        ImageView jewelImageView = new ImageView();
        jewelImageView.setImage(jewelImage);
        jewelImageView.setStyle("-fx-background-color:transparent;");

        node = jewelImageView;
    }

    /**
     * Updates the Jewel graphics.
     */
    @Override
    public void update() {
    	updateVelocity();
        node.setTranslateX(node.getTranslateX() + vX);
        node.setTranslateY(node.getTranslateY() + vY);
        node.setLayoutX(xPos);
        node.setLayoutY(yPos);
    }
    
    /**
     * Updates the Jewels velocity based on its current position and desired position.
     */
    private void updateVelocity(){
    	//Update x velocity
    	if(node.getTranslateX() > 4){
    		vX = -4;
    	}else{
    		if(node.getTranslateX() < -4){
        		vX = 4;
        	}else{
        		vX=-node.getTranslateX();
        	}
    	}

    	//Update x velocity
    	if(node.getTranslateY() > 4){
    		vY = -4;
    	}else{
    		if(node.getTranslateY() < -4){
        		vY = 4;
        	}else{
        		vY=-node.getTranslateY();
        	}
    	}
    	
    }
    
    
    /**
     * Getter method for boardX.
     * @return The X position of the Jewel on the board's grid.
     */
    public int getBoardX() {
        return boardX;
    }

    /**
     * Getter method for boardY.
     * @return The Y position of the Jewel on the board's grid.
     */
    public int getBoardY() {
        return boardY;
    }

    /**
     * Getter method for Jewel type
     * @return The type identifier for this Jewel.
     */
    public int getType() {
        return type;
    }

    /**
     * Setter method for boardX.
     * @param boardX Jewel's horizontal coordinate on the boards grid.
     */
    public void setBoardX(int boardX) {
        this.boardX = boardX;
    }

    /**
     * Setter method for boardY.
     * @param boardY Jewel's vertical coordinate on the boards grid.
     */
    public void setBoardY(int boardY) {
        this.boardY = boardY;
    }

    /**
     * Animate an implosion. Once done remove from the game world
     * @param sceneGroup Game scene group to remove the Jewel from.
     */
    public void implode(Group sceneGroup) {
        vX = vY = 0;
        FadeTransitionBuilder.create()
                .node(node)
                .duration(Duration.millis(300))
                .fromValue(node.getOpacity())
                .toValue(0)
                .onFinished( new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent arg0) {
                        isDead = true;
                        sceneGroup.getChildren().remove(node);
                    }
                })
                .build()
                .play();
    }

    /**
     * Simple version of implode.
     * Just sets the isdead to true so the update() method handles
     * not showing the sprite anymore.
     */
    public void simpleImplode() {
        isDead = true;
    }
}
