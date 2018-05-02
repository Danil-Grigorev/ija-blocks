package Logic;

import Elements.Blocks.*;
import Elements.Containers.ItemContainer;
import Elements.Ports.Connection;
import Elements.Ports.InputPort;
import Elements.Ports.Port;
import javafx.application.Platform;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.io.*;


public class Logic {
    public State schemeState;
    private AnchorPane schemePane;
    private ItemContainer elementContainer;

    // Synchronized block operations
    // -----------------------
    private boolean accNode;
    private Block opNode;

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
    }

    public State getSchemeState() {
        return schemeState;
    }

    public void setSchemeState(State schemeState) {
        if (schemeState == State.DEFAULT && this.schemeState != State.DEFAULT) {
            tmpReset();
        }
        this.schemeState = schemeState;
    }

    public void schemeAct(MouseEvent mouseEvent) {
        switch (getSchemeState()) {
            case DEFAULT:
                System.out.println("DEFAULT block state");
                if (this.tmpCon != null) {
                    tmpCon = null;
                }
                break;
            case PUT_BLOCK:
                System.out.println("PUT block state");
                putBl(mouseEvent.getX(), mouseEvent.getY());
                schemeState = State.DEFAULT;
                break;
            case REMOVE:
                System.out.println("Remove -> default block state");
                setSchemeState(State.DEFAULT);
                break;
            case ADD_CON_2:
                System.out.println("ADD_CON_2 block state");
                break;
            default:
                System.out.println("Unknown state");
                break;
        }
        mouseEvent.consume();
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
                case "custom":
                    // TODO
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
            opNode.setVisuals(X,Y);
            opNode.setupPorts();
            opNode.set();
            opNode.createSave(elementContainer);
            opNode = null;
            accNode = true;
        });
    }

    public void blockClick(Block caller, MouseEvent e) {
        switch (getSchemeState()) {
            case REMOVE:
                caller.remove();
                elementContainer.remove(caller);
                break;
            case ADD_CON_2:
                setSchemeState(State.DEFAULT);
            case DEFAULT:
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
                caller.remove();
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
        Shape object = (Shape) e.getSource();
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
    }
}
