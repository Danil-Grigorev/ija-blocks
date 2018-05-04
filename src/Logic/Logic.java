package Logic;

import Elements.Blocks.*;
import Elements.Containers.ItemContainer;
import Elements.DataTypes.DataType;
import Elements.DataTypes.IntType;
import Elements.Ports.Connection;
import Elements.Ports.InputPort;
import Elements.Ports.OutputPort;
import Elements.Ports.Port;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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



    // -----------------------
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

    }

    public State getSchemeState() {
        return schemeState;
    }

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
        // TODO: check if is zero length and stop execution
        for (Block block : blocksToExecute) {
            System.out.println("Calculating " + block.getId() + " name " + block.getName());
            block.calculate();
            for (OutputPort port : block.getOutputPorts()) {
                port.block();
            }
        }
        for (Block block : blocksToExecute) {
            for (OutputPort port : block.getOutputPorts()) {
                port.unblock();
            }
        }
    }

    private void openSetInWind(Block caller) {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Interface/InBlockSetup.fxml"));
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
        setInStage.show();

    }

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

    public void portClick(Port caller, MouseEvent e) {
        switch (getSchemeState()) {
            case REMOVE:
                caller.getParent().remove();
                elementContainer.remove(caller);
                break;
            case ADD_CON_2:
                try {
                    caller.setConnection(tmpCon);
                    System.out.println("Connected second");
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
                    System.out.println("Connected first");
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
                Platform.exit();
                System.exit(99);
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

    public void save(File name) {
        try {
            FileOutputStream file = new FileOutputStream(name);
            ObjectOutputStream byteObj = new ObjectOutputStream(file);
            byteObj.writeObject(elementContainer);
            byteObj.close();
            file.close();
        } catch (IOException e) {
            System.err.println("Error while writing scheme to file");
            e.printStackTrace();
            Platform.exit();
            System.exit(99);
        }
        schemeName = name;
    }

    public void load(File file) {
        try {
            FileInputStream fileIn = new FileInputStream(file);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            elementContainer = (ItemContainer) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException e) {
            System.out.println("Error reading class from file " + file);
            return;
        } catch (ClassNotFoundException c) {
            System.out.println("Error reading class from file " + file);
            return;
        }

        schemePane.getChildren().clear();
        blocks.clear();
        elementContainer.restore(this, schemePane);

    }

    public int generateId() {
        return elementContainer.generateId();
    }

    public void tmpReset() {
        if (tmpCon != null && !tmpCon.isSet()) {
            tmpCon.remove();
        }
        opNode = null;
        accNode = true;
        tmpCon = null;
    }

    public void reset() {
        schemePane.getChildren().clear();
        blocks.clear();
        elementContainer = new ItemContainer();
        setSchemeState(State.DEFAULT);
    }

    public ArrayList<Block> getBlocks() {
        return blocks;
    }

    public File getSchemeName() {
        return this.schemeName;
    }

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
