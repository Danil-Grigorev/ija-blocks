package Logic;

import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    // Top menu buttons
    public MenuItem exitBtn1;

    public Button rstBtn1;  // Reset button
    public Button nxBtn1;   // Next button

    public Button remBtn1;  // Remove button

    // Block creating buttons
    public MenuItem addBlBtn1;
    public MenuItem subBlBtn1;
    public MenuItem mulBlBtn1;
    public MenuItem divBlBtn;

    // Elements with scheme content
    public AnchorPane displayPane;
    public ScrollPane dispParent;
    public AnchorPane leftMenu;
    public MenuBar topMenu;

    private Logic appL;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        appL = new Logic(displayPane, leftMenu.getPrefWidth(), topMenu.getPrefHeight());
        appL.setSchemeState(Logic.State.DEFAULT);
    }

    // Top menu actions
    public void saveScheme(javafx.event.ActionEvent actionEvent) {
        appL.save("test.txt");
    }

    public void openScheme(javafx.event.ActionEvent actionEvent) {
        appL.load("test.txt");
    }

    // Button click handler function
    public void nxClick(javafx.event.ActionEvent actionEvent) {
    }

    public void rsClick(javafx.event.ActionEvent actionEvent) {
    }

    public void remClick(javafx.event.ActionEvent actionEvent) {
        if (appL.getSchemeState() == Logic.State.REMOVE) {
            appL.setSchemeState(Logic.State.DEFAULT);
        }
        else {
            appL.setSchemeState(Logic.State.REMOVE);
        }
    }

    public void splitBlockCreate(javafx.event.ActionEvent actionEvent) {
        appL.initBl("split");
        appL.setSchemeState(Logic.State.PUT_BLOCK);
    }

    public void addBlockCreate(javafx.event.ActionEvent actionEvent) {
        appL.initBl("add");
        appL.setSchemeState(Logic.State.PUT_BLOCK);
    }

    public void subBlockCreate(javafx.event.ActionEvent actionEvent) {
        appL.initBl("sub");
        appL.setSchemeState(Logic.State.PUT_BLOCK);
    }

    public void mulBlockCreate(javafx.event.ActionEvent actionEvent) {
        appL.initBl("mul");
        appL.setSchemeState(Logic.State.PUT_BLOCK);
    }

    public void divBlockCreate(javafx.event.ActionEvent actionEvent) {
        appL.initBl("div");
        appL.setSchemeState(Logic.State.PUT_BLOCK);
    }

    // TODO: add custom block

    // Main scheme area action
    public void schemeAct(MouseEvent mouseEvent) {
        appL.schemeAct(mouseEvent);
    }

    public void exitApp(javafx.event.ActionEvent actionEvent) {
        Platform.exit();
    }

    public AnchorPane getDisplayPane() {
        return displayPane;
    }
}
