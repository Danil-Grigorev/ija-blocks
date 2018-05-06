package Logic;

import Elements.Blocks.Block;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * @author xgrigo02
 */
public class Controller implements Initializable {

    // Top menu buttons
    public MenuItem newBtn1;
    public MenuItem svBtn1;
    public MenuItem svAsBtn1;
    public MenuItem openBtn;
    public MenuItem exitBtn1;

    // Down menu buttons
    public Button strtStpBtn1; // Start or stop execution button
    public Button stpBtn1;     // Next button

    // Block creating buttons
    public MenuItem addBlBtn1;
    public MenuItem subBlBtn1;
    public MenuItem mulBlBtn1;
    public MenuItem divBlBtn;
    public MenuItem splBlBtn1;
    public MenuItem outBlBtn1;
    public MenuItem inBlBtn1;

    public Button remBtn1;  // Remove button

    // Elements with scheme content
    public AnchorPane displayPane;
    public ScrollPane dispParent;
    public AnchorPane leftMenu;
    public MenuBar topMenu;
    public BorderPane borderLayout;

    private Logic appL;
    private ArrayList<Node> leftMenuNodes;

    private Image startImg;
    private Image stopImg;

    /**
     *
     * @param url               URL for element initialization.
     * @param resourceBundle    other resources.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        appL = new Logic(displayPane, leftMenu.getPrefWidth(), topMenu.getPrefHeight());
        appL.setSchemeState(Logic.State.DEFAULT);
        this.leftMenuNodes = new ArrayList<>();
        startImg = new Image("images/start.png");
        stopImg  = new Image("images/stop.png");
    }

    /**
     * Initializes key listeners for keyboard actions.
     */
    public void initKeyListeners() {
        this.getDisplayPane().getScene().addEventFilter(KeyEvent.KEY_PRESSED,
                event -> {
                    switch (event.getCode()) {
                        case ESCAPE:
                            appL.setSchemeState(Logic.State.DEFAULT);
                            break;
                    }
                });
    }

    // Top menu actions

    /**
     * Provides save scheme on-click functionality.
     *
     * @param actionEvent   mouse click
     */
    public void saveAsScheme(ActionEvent actionEvent) {
        File schemaSave;
        Stage stage = new Stage();
        FileChooser saver = new FileChooser();
        saver.setTitle("Save schema file");

        // Setting extension
        FileChooser.ExtensionFilter extF =
                new FileChooser.ExtensionFilter("SCM files (*.scm)", "*.scm");
        saver.getExtensionFilters().add(extF);

        schemaSave = saver.showSaveDialog(stage);
        if (schemaSave != null) {
            String abs_path = schemaSave.getAbsolutePath();
            if (!abs_path.endsWith(".scm")) {
                schemaSave = new File(abs_path + ".scm");
            }
            appL.save(schemaSave);
            appL.setSchemeName(schemaSave);
        }
    }

    /**
     * Provides saveAs scheme on-click functionality.
     *
     * @param actionEvent   mouse click
     */
    public void saveScheme(javafx.event.ActionEvent actionEvent) {
        if (this.appL.getSchemeName() != null) {
            this.appL.save(this.appL.getSchemeName());
        }
        else {
            saveAsScheme(actionEvent);
        }
    }

    /**
     * Allows to load scheme from file.
     *
     * @param actionEvent   mouse click
     */
    public void openScheme(javafx.event.ActionEvent actionEvent) {
        File schemaName;
        Stage stage = new Stage();
        FileChooser opener = new FileChooser();
        opener.setTitle("Open schema file");

        // Setting extension
        FileChooser.ExtensionFilter extF =
                new FileChooser.ExtensionFilter("SCM files (*.scm)", "*.scm");
        opener.getExtensionFilters().add(extF);

        schemaName = opener.showOpenDialog(stage);
        if (schemaName != null) {
            appL.load(schemaName);
            appL.setSchemeName(schemaName);
        }
    }

    /**
     * Remove button click function
     *
     * @param actionEvent   mouse click
     */
    public void remClick(javafx.event.ActionEvent actionEvent) {
        if (appL.getSchemeState() == Logic.State.REMOVE) {
            appL.setSchemeState(Logic.State.DEFAULT);
        }
        else {
            appL.setSchemeState(Logic.State.REMOVE);
        }
    }

    /**
     * Creates split block.
     *
     * @param actionEvent   mouse click
     */
    public void splitBlockCreate(javafx.event.ActionEvent actionEvent) {
        appL.initBl("split");
        appL.setSchemeState(Logic.State.PUT_BLOCK);
    }

    /**
     * Creates add block.
     *
     * @param actionEvent   mouse click
     */
    public void addBlockCreate(javafx.event.ActionEvent actionEvent) {
        appL.initBl("add");
        appL.setSchemeState(Logic.State.PUT_BLOCK);
    }

    /**
     * Creates sub block.
     *
     * @param actionEvent   mouse click
     */
    public void subBlockCreate(javafx.event.ActionEvent actionEvent) {
        appL.initBl("sub");
        appL.setSchemeState(Logic.State.PUT_BLOCK);
    }

    /**
     * Creates mul block.
     *
     * @param actionEvent   mouse click
     */
    public void mulBlockCreate(javafx.event.ActionEvent actionEvent) {
        appL.initBl("mul");
        appL.setSchemeState(Logic.State.PUT_BLOCK);
    }

    /**
     * Creates div block.
     *
     * @param actionEvent   mouse click
     */
    public void divBlockCreate(javafx.event.ActionEvent actionEvent) {
        appL.initBl("div");
        appL.setSchemeState(Logic.State.PUT_BLOCK);
    }

    /**
     * Creates in block.
     *
     * @param actionEvent   mouse click
     */
    public void inBlCreate(ActionEvent actionEvent) {
        appL.initBl("in");
        appL.setSchemeState(Logic.State.PUT_BLOCK);
    }

    /**
     * Creates out block.
     *
     * @param actionEvent   mouse click
     */
    public void outBlCreate(ActionEvent actionEvent) {
        appL.initBl("out");
        appL.setSchemeState(Logic.State.PUT_BLOCK);
    }

    // Scheme area action

    /**
     * Make scheme do some action after click.
     *
     * @param mouseEvent mouse click
     */
    public void schemeAct(MouseEvent mouseEvent) {
        appL.schemeAct(mouseEvent);
    }

    /**
     * Makes scheme reset it's state after escape click.
     *
     * @param keyEvent key clicked info variable.
     */
    public void schemeKeyAct(KeyEvent keyEvent) {
        switch (keyEvent.getCode()) {
            case ESCAPE:
                appL.setSchemeState(Logic.State.DEFAULT);
                break;
        }
    }

    /**
     * Closes application.
     *
     * @param actionEvent mouse click info.
     */
    public void exitApp(javafx.event.ActionEvent actionEvent) {
        Platform.exit();
    }

    /**
     * Getter for scheme pane.
     *
     * @return AnchorPane   scheme pane
     */
    public AnchorPane getDisplayPane() {
        return displayPane;
    }

    /**
     * Resets scheme after new click.
     *
     * @param actionEvent mouse click
     */
    public void newScheme(ActionEvent actionEvent) {
        appL.reset();
    }

    // Schema execution buttons handler

    /**
     * Executes all blocks after next click.
     *
     * @param actionEvent mouse click
     */
    public void nxClick(javafx.event.ActionEvent actionEvent) {
        appL.executeAll();
    }

    /**
     * Starts or stops execution after start/stop button click
     *
     * @param actionEvent mouse click
     */
    public void startStopClick(ActionEvent actionEvent) {
        if (appL.getSchemeState() != Logic.State.EXECUTE) {
            newBtn1.setDisable(true);
            svBtn1.setDisable(true);
            svAsBtn1.setDisable(true);
            openBtn.setDisable(true);

            leftMenu.setDisable(true);
            leftMenu.setMaxWidth(0);
            for (Node nd : leftMenu.getChildren()) {
                this.leftMenuNodes.add(nd);
            }
            leftMenu.getChildren().clear();

            ImageView tmp = (ImageView) strtStpBtn1.getGraphic();
            tmp.setImage(stopImg);

            stpBtn1.setVisible(true);
            appL.setSchemeState(Logic.State.EXECUTE);
        }
        else {
            newBtn1.setDisable(false);
            svBtn1.setDisable(false);
            svAsBtn1.setDisable(false);
            openBtn.setDisable(false);

            leftMenu.setDisable(false);
            leftMenu.setMaxWidth(160.0);
            for (Node nd : this.leftMenuNodes) {
                leftMenu.getChildren().add(nd);
            }
            this.leftMenuNodes.clear();

            ImageView tmp = (ImageView) strtStpBtn1.getGraphic();
            tmp.setImage(startImg);
            for (Block bl : appL.getBlocks()) {
                bl.setData(null);
                bl.setInactive();
            }

            stpBtn1.setVisible(false);
            appL.setSchemeState(Logic.State.DEFAULT);
        }

    }

}

