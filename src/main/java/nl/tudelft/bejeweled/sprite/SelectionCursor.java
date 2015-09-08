package nl.tudelft.bejeweled.sprite;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class SelectionCursor extends Sprite {
	private double xPos,yPos;
	
	public SelectionCursor(double x, double y){
		xPos= x;
		yPos =y;
		
        ImageView cursorImageView = new ImageView();
        cursorImageView.setImage(new Image(SelectionCursor.class.getResourceAsStream("/selection.png")));
        cursorImageView.setStyle("-fx-background-color:transparent;");

        node = cursorImageView;
        
	}
	
	public void update() {
        node.setLayoutX(xPos);
        node.setLayoutY(yPos);
        

        
	}
	


}
