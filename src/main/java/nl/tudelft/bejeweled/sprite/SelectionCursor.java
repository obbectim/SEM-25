package nl.tudelft.bejeweled.sprite;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Created by Pim on 8-9-2015.
 * Class to define the cursor that indicates the currently selected jewel in the game.
 */
public class SelectionCursor extends Sprite {
	private double xPos, yPos;
	
    /**
     * Constructor for SelectionCursor class.
     * @param x The X position of this SelectionCursor on the board grid (in number of squares).
     * @param y The Y position of this SelectionCursor on the board grid (in number of squares).
     */
	public SelectionCursor(double x, double y) {
		xPos = x;
		yPos = y;
		
        ImageView cursorImageView = new ImageView();
        Image cursorImage = new Image(SelectionCursor.class.getResourceAsStream("/selection.png"));
        cursorImageView.setImage(cursorImage);
        cursorImageView.setStyle("-fx-background-color:transparent;");

        setNode(cursorImageView);
	}
	
    /**
     * Updates the SelectionCursor graphics.
     */
	public void update() {
        getNode().setLayoutX(xPos);
        getNode().setLayoutY(yPos);
	}
	


}
