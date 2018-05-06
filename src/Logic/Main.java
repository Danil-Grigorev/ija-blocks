package Logic;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;



public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("FXML_src/interface.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Block scheme application");
        primaryStage.setScene(new Scene(root, 1280, 720));
        primaryStage.show();
        ((Controller) loader.getController()).initKeyListeners();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
