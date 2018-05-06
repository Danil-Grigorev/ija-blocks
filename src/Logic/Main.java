package Logic;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 * @author xgrigo02
 */
public class Main extends Application {

    /**
     * Starts new window and initializes app interface with key listeners
     *
     * @param primaryStage  Stage, used for main window creation.
     * @throws Exception    Throws when an error occured while creating main window.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("FXML_src/interface.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Block scheme application");
        primaryStage.setScene(new Scene(root, 1280, 720));
        primaryStage.show();
        ((Controller) loader.getController()).initKeyListeners();

    }

    /**
     * Application starter.
     *
     * @param args input arguments as <code>String</code>.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
