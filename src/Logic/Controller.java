package Logic;

import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    // Top menu buttons
    public MenuItem exitBtn1;

    public Button rstBtn1;  // Reset button
    public Button nxBtn1;   // Next button

    public Button conBtn1;  // Add connection button
    public Button remBtn1;  // Remove button

    // Block creating buttons
    public Button addBlBtn1;
    public Button subBlBtn1;
    public Button mulBlBtn1;
    public Button divBlBtn;

    // Elements with scheme content
    public AnchorPane displayPane;
    public ScrollPane dispParent;

    private Logic appL;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        appL = new Logic(displayPane);
        appL.setSchemaState(Logic.State.DEFAULT);
    }

    // Button click handler function
    public void nxClick(javafx.event.ActionEvent actionEvent) {
    }

    public void rsClick(javafx.event.ActionEvent actionEvent) {
    }

    public void remClick(javafx.event.ActionEvent actionEvent) {
        appL.setSchemaState(Logic.State.REMOVE_BLOCK);
    }

    public void addConClick(javafx.event.ActionEvent actionEvent) {
        appL.setSchemaState(Logic.State.ADD_CON_1);
    }

    public void splitBlockCreate(javafx.event.ActionEvent actionEvent) {
        appL.initBl();
        appL.setSchemaState(Logic.State.PUT_BLOCK);
    }

    public void addBlockCreate(javafx.event.ActionEvent actionEvent) {
        appL.initBl();
        appL.setSchemaState(Logic.State.PUT_BLOCK);
    }

    public void subBlockCreate(javafx.event.ActionEvent actionEvent) {
        appL.initBl();
        appL.setSchemaState(Logic.State.PUT_BLOCK);
    }

    public void mulBlockCreate(javafx.event.ActionEvent actionEvent) {
        appL.initBl();
        appL.setSchemaState(Logic.State.PUT_BLOCK);
    }

    public void divBlockCreate(javafx.event.ActionEvent actionEvent) {
        appL.initBl();
        appL.setSchemaState(Logic.State.PUT_BLOCK);
    }

    // Main schema area action
    public void schemaAct(MouseEvent mouseEvent) {
        appL.schemaAct(mouseEvent);
    }

    public void exitApp(javafx.event.ActionEvent actionEvent) {
        Platform.exit();
    }

    public AnchorPane getDisplayPane() {
        return displayPane;
    }
}
