/**
 * Created by Jeroen on 1-9-2015.
 */

import javafx.application.Application;
import javafx.stage.Stage;

public class FXapp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage theStage) {
        theStage.setTitle("Hello, World!");
        theStage.show();
    }
}
