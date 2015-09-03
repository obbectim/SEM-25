package nl.tudelft.bejeweled.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.AnchorPane;
import nl.tudelft.bejeweled.game.Game;

/**
 * Created by Jeroen on 3-9-2015.
 * Class that initialises the GUI for the Bejeweled game.
 */
public class BejeweledGui {

    private BejeweledGuiController bejeweledController;

    public BejeweledGui(Game game, Stage stage) {
        initGui(game, stage);
    }

    public void initGui(Game game, Stage stage) {
        try {
            // load fxml file from resource and create a new stage for the dialog
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(BejeweledGui.class.getResource("/bejeweled_gui.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Scene scene = new Scene(page);
            stage.setScene(scene);
            stage.setTitle("Bejeweled");
            stage.show();

            bejeweledController = loader.getController();
            bejeweledController.setGame(game);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
