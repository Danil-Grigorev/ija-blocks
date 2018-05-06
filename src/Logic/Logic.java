package Logic;

import Elements.Blocks.*;
import Elements.Containers.ItemContainer;
import Elements.Ports.Connection;
import Elements.Ports.InputPort;
import Elements.Ports.OutputPort;
import Elements.Ports.Port;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;

/**
 * Logic class for scheme execution.
 *
 * @author xgrigo02
 */
public class Logic {
    public State schemeState;
    private AnchorPane schemePane;
    private ItemContainer elementContainer;

    // Synchronized block operations
    // -----------------------
    private boolean accNode;
    private Block opNode;
    private ArrayList<Block> blocks;

    private Connection tmpCon;

    private double indentX;
    private double indentY;

    private File schemeName;

    private boolean blockNewSetInWind;

    // -----------------------

    /**
     * Logic initializer, sets connection with main window, and indents for correct block positioning
     *
     * @param displayPane   Main window pane
     * @param indentX       x offset for block positioning
     * @param indentY       y offset for block positioning
     */
    public Logic(AnchorPane displayPane, double indentX, double indentY) {
        this.indentX = indentX;
        this.indentY = indentY;
        schemeName = null;
        elementContainer = new ItemContainer();
        schemePane = displayPane;
        opNode = null;
        accNode = true;
        tmpCon = null;
        blocks = new ArrayList<>();
        blockNewSetInWind = false;
    }

    /**
     * Schema state getter.
     *
     * @return State    Schema actual state:
     *
     *                  <code>DEFAULT</code>    Waits for any operation, movement or connection seting.
     *                  <code>PUT_BLOCK</code>  Putting block.
     *                  <code>REMOVE</code>     Removing elements
     *                  <code>ADD_CON_2</code>  Waits for setting second connection.
     *                  <code>EXECUTE</code>    Schema is executing.
     */
    public State getSchemeState() {
        return schemeState;
    }

    /**
     * Setting new scheme state and clears buffers for unfinished operations.
     *
     * @param schemeState   New <code>State</code> for scheme.
     */
    public void setSchemeState(State schemeState) {
        Platform.runLater( () -> {
            if (schemeState == State.DEFAULT) {
                if (this.schemeState != State.DEFAULT) {
                    tmpReset();
                }
                if (this.schemeState == State.EXECUTE) {
                    for (Block bl : this.blocks) {
                        if (bl instanceof InOutBlock) {
                            ((InOutBlock) bl).hideVal(true);
                        }
                    }
                }
            }
            else if (schemeState == State.EXECUTE) {
                if (this.schemeState != State.EXECUTE) {
                    for (Block bl : this.blocks) {
                        if (bl instanceof InOutBlock) {
                            ((InOutBlock) bl).hideVal(false);
                        }
                    }
                }
            }
            this.schemeState = schemeState;
        });
    }

    /**
     * Handles all scheme operations.
     *
     * @param mouseEvent    Depending on scheme state, will do operation on clicked position.
     */
    public void schemeAct(MouseEvent mouseEvent) {
        Platform.runLater( () -> {

            switch (getSchemeState()) {
                case DEFAULT:
                    break;
                case PUT_BLOCK:
                    putBl(mouseEvent.getX(), mouseEvent.getY());
                    setSchemeState(State.DEFAULT);
                    break;
                case REMOVE:
                    setSchemeState(State.DEFAULT);
                    break;
                case ADD_CON_2:
                    break;
                case EXECUTE:
                    break;
                default:
                    System.out.println("Unknown state");
                    break;
            }
            mouseEvent.consume();
        });
    }

    /**
     *  Creates block skeleton of specified type.
     *
     * @param type  <code>String</code>, which specifies type of block to create.
     *
     *              <code>add</code>    ADD block.
     *              <code>sub</code>    SUB block.
     *              <code>mul</code>    MUL block.
     *              <code>div</code>    DIV block.
     *              <code>split</code>  SPLIT block.
     *              <code>in</code>     IN block.
     *              <code>out</code>    OUT block.
     */
    public synchronized void initBl(String type) {
        Platform.runLater(() -> {
            if (accNode == false) {
                try {
                    wait();
                } catch (InterruptedException e) {
                }
            }
            accNode = false;
            switch (type) {
                case "add":     opNode = new AddBlock(this, schemePane);
                    break;
                case "sub":     opNode = new SubBlock(this, schemePane);
                    break;
                case "mul":     opNode = new MulBlock(this, schemePane);
                    break;
                case "div":     opNode = new DivBlock(this, schemePane);
                    break;
                case "split":   opNode = new SplitBlock(this, schemePane);
                    break;
                case "in":      opNode = new InOutBlock(this, schemePane, true);
                    break;
                case "out":     opNode = new InOutBlock(this, schemePane, false);
                    break;
                default:
                    System.err.println("Unknown block type for init");
                    Platform.exit();
                    System.exit(99);
                    break;
            }
            accNode = true;
        });
    }

    /**
     * Puts initialized block on specified coordinates.
     *
     * @param X x coordinates for block creation.
     * @param Y y coordinates for block creation.
     */
    private synchronized void putBl(double X, double Y) {
        Platform.runLater(() -> {
            if (accNode == false) {
                try {
                    wait();
                } catch (InterruptedException e) {
                }
            }
            accNode = false;
            assert opNode != null : "Block was not initialised";
            opNode.setVisuals(X, Y);
            opNode.setupPorts();
            opNode.set();
            opNode.createSave(elementContainer);
            blocks.add(opNode);
            accNode = true;
        });
    }

    /**
     * Executes all blocks, which are ready for calculation and have all input ports set
     * and have data.
     */
    public void executeAll() {
        boolean doCalc;
        ArrayList<Block> blocksToExecute = new ArrayList<>();
        for (Block block: blocks) {
            doCalc = true;
            for (InputPort in : block.getInputPorts()) {
                if (!in.isActive()) {
                    doCalc = false;
                    break;
                }
            }
            if (block.getMaxInPorts() == 0 && block.getData() == null) {
                continue;
            }
            if (doCalc) {
                blocksToExecute.add(block);
            }
        }
        for (Block block : blocksToExecute) {
            for (OutputPort port : block.getOutputPorts()) {
                port.block();
            }
            block.calculate();
        }
        for (Block block : blocksToExecute) {
            for (OutputPort port : block.getOutputPorts()) {
                port.unblock();
            }
        }
    }

    /**
     * While scheme is executing, will open new window for value specification after IN block click.
     *
     * @param caller    Source block reference.
     */
    private void openSetInWind(Block caller) {
        if (!blockNewSetInWind) {
            blockNewSetInWind = true;
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("FXML_src/InBlockSetup.fxml"));
            Scene newScene;
            try {
                newScene = new Scene(loader.load());
            } catch (IOException e) {
                System.err.println("Cant create new window");
                e.printStackTrace();
                return;
            }

            InBlockSetup setup = loader.getController();
            setup.init(caller);

            Stage setInStage = new Stage();
            setInStage.setTitle("Set value for ID: " + caller.getId());
            setInStage.setScene(newScene);
            setInStage.showAndWait();
            blockNewSetInWind = false;
        }
    }

    /**
     * Depending on scheme <code>State</code> will do expected operation.
     *
     * @param caller    <code>Block</code> Source block reference.
     * @param e         <code>MouseEvent</code> for additional actions
     */
    public void blockClick(Block caller, MouseEvent e) {
        switch (getSchemeState()) {
            case REMOVE:
                caller.remove();
                elementContainer.remove(caller);
                blocks.remove(caller);
                break;
            case ADD_CON_2:
                setSchemeState(State.DEFAULT);
            case DEFAULT:
                break;
            case EXECUTE:
                if (caller instanceof InOutBlock && caller.getName().equals("In")) {
                    openSetInWind(caller);
                }
                break;
        }
        e.consume();
    }

    /**
     * Allows blocks to be draggable.
     *
     * @param caller    <code>Block</code> Source block reference.
     * @param e         <code>MouseEvent</code> for additional actions
     */
    public void blockDrag(Block caller, MouseEvent e) {
        switch (getSchemeState()) {
            case DEFAULT:
                caller.reposition(
                        e.getSceneX() - this.indentX,
                        e.getSceneY() - this.indentY);
                caller.createSave(elementContainer);
                break;
        }
        e.consume();
    }

    /**
     * Does operations like creating new connection or block removing after port click.
     *
     * @param caller    <code>Port</code> Source port reference.
     * @param e         <code>MouseEvent</code> for additional actions
     */
    public void portClick(Port caller, MouseEvent e) {
        switch (getSchemeState()) {
            case REMOVE:
                caller.getParent().remove();
                elementContainer.remove(caller);
                break;
            case ADD_CON_2:
                try {
                    caller.setConnection(tmpCon);
                } catch (IOException ex) { break; }
                if (caller instanceof InputPort) {
                    tmpCon.setEndPoint(0,
                            caller.getCenterX(),
                            caller.getCenterY());
                }
                else {
                    tmpCon.setStartPoint(0,
                            caller.getCenterX(),
                            caller.getCenterY());
                }
                tmpCon.set();
                caller.createSave(elementContainer);
                setSchemeState(State.DEFAULT);
                break;
            case DEFAULT:
                if (tmpCon == null) {
                    tmpCon = new Connection(this, schemePane);
                }
                try {
                    caller.setConnection(tmpCon);
                    caller.makeSelected(true);
                } catch (IOException ex) { break; }
                if (caller instanceof InputPort) {
                    tmpCon.setEndPoint(0,
                            caller.getCenterX(),
                            caller.getCenterY());
                }
                else {
                    tmpCon.setStartPoint(0,
                            caller.getCenterX(),
                            caller.getCenterY());
                }
                setSchemeState(State.ADD_CON_2);
                break;
        }
        e.consume();
    }

    /**
     * Provide connection click functionality, like creating new joint or deletion.
     *
     * @param caller    <code>Connection</code> Source connection reference.
     * @param e         <code>MouseEvent</code> for additional actions
     */
    public void connectionClick(Connection caller, MouseEvent e) {
        switch (getSchemeState()) {
            case DEFAULT:
                caller.addJoint((Line) e.getSource(),
                        e.getSceneX() - this.indentX,
                        e.getSceneY() - this.indentY);
                caller.createSave(elementContainer);
                break;
            case REMOVE:
                caller.remove();
                break;
        }
        e.consume();
    }

    /**
     * Makes joints draggable.
     *
     * @param caller    <code>Connection</code> Source connection reference.
     * @param e         <code>MouseEvent</code> for additional actions
     */
    public void jointDrag(Connection caller, MouseEvent e) {
        Rectangle joint = (Rectangle) e.getSource();
        switch (getSchemeState()) {
            case DEFAULT:
                caller.repositionJoint(joint, e.getSceneX() - this.indentX, e.getSceneY() - this.indentY);
                caller.createSave(elementContainer);
                break;
        }
        e.consume();
    }

    /**
     * Provides joints functionality on click, like remove.
     *
     * @param caller    <code>Connection</code> Source connection reference.
     * @param e         <code>MouseEvent</code> for additional actions
     */
    public void jointClick(Connection caller, MouseEvent e) {
        Rectangle joint = (Rectangle) e.getSource();
        switch (getSchemeState()) {
            case REMOVE:
                caller.removeJoint(joint);
                caller.createSave(elementContainer);
                break;
        }
        e.consume();
    }

    /**
     * Provides scheme element highlighting for all elements.
     *
     * @param e     <code>MouseEvent</code> contains information about source element.
     */
    public void elementHover(MouseEvent e) {
        Shape object = null;
        if (e.getSource() instanceof Label) {
            Pane tmp = (Pane) ((Label) e.getSource()).getParent();
            for (Node nd : tmp.getChildren()) {
                if (nd instanceof Rectangle) {
                    object = (Shape) nd;
                    break;
                }
            }
            if (object == null) {
                e.consume();
                return;
            }
        }
        else {
            object = (Shape) e.getSource();
        }
        Color color = (Color) object.getStroke();

        if (e.getEventType() == MouseEvent.MOUSE_ENTERED) {
            object.setStroke(color.brighter());
        }
        else {
            object.setStroke(color.darker());
        }
        e.consume();
    }

    /**
     * Allows current scheme to be saved.
     *
     * @param name  <code>File</code> new .scm file name for saving current scheme.
     */
    public void save(File name) {
        try {
            FileOutputStream file = new FileOutputStream(name);
            ObjectOutputStream byteObj = new ObjectOutputStream(file);
            byteObj.writeObject(elementContainer);
            byteObj.close();
            file.close();
            schemeName = name;
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Can't save scheme");
            alert.setContentText("An error occurred while saving scheme");
            alert.showAndWait();
        }
    }

    /**
     * Allows scheme to be loaded.
     *
     * @param file <code>File</code> .scm file for scheme loading.
     */
    public void load(File file) {
        try {
            FileInputStream fileIn = new FileInputStream(file);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            elementContainer = (ItemContainer) in.readObject();
            in.close();
            fileIn.close();
            schemePane.getChildren().clear();
            blocks.clear();
            elementContainer.restore(this, schemePane);
        } catch (Exception x) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Can't open scheme");
            alert.setContentText("An error occurred while opening scheme");
            alert.showAndWait();
        }


    }

    /**
     * Decorator for new element id creation, which returns generated ID from <code>elementContainer</code>.
     *
     * @return <code>int</code> new element ID.
     */
    public int generateId() {
        return elementContainer.generateId();
    }

    /**
     * Resets all buffers after all scheme operations, (particularly unfinished).
     */
    public void tmpReset() {
        if (tmpCon != null && !tmpCon.isSet()) {
            tmpCon.remove();
        }
        opNode = null;
        accNode = true;
        tmpCon = null;
    }

    /**
     * Resets whole scheme and clears it's pane.
     */
    public void reset() {
        schemePane.getChildren().clear();
        blocks.clear();
        elementContainer = new ItemContainer();
        this.schemeName = null;
        setSchemeState(State.DEFAULT);
    }

    /**
     * Getter for current scheme blocks as <code>ArrayList</code>.
     * @return  <code>ArrayList</code> <code>Block</code>s
     */
    public ArrayList<Block> getBlocks() {
        return blocks;
    }

    /**
     * Getter for current scheme file location, or <code>null</code>.
     *
     * @return  <code>File</code> scheme file
     */
    public File getSchemeName() {
        return this.schemeName;
    }


    /**
     * Setter for current scheme file location.
     *
     * @param schemeName <code>File</code> sets scheme location.
     */
    public void setSchemeName(File schemeName) {
        this.schemeName = schemeName;
    }

    // State logic
    public enum State {
        DEFAULT,
        PUT_BLOCK,
        REMOVE,
        ADD_CON_2,
        EXECUTE
    }
}
