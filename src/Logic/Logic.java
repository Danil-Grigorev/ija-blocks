package Logic;

import Elements.*;
import Execute.Main;
import javafx.application.Platform;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.io.Serializable;


public class Logic {
    public State schemaState;
    private AnchorPane schemaPane;

    // Synchronized block operations
    // -----------------------
    private boolean accNode;
    private Block opNode;

    private Rectangle tmp;
    private Connection tmpCon;
    private int id;

    private double indentX;
    private double indentY;

    // -----------------------
    public Logic(AnchorPane displayPane, double indentX, double indentY) {
        this.indentX = indentX;
        this.indentY = indentY;
        schemaPane = displayPane;
        opNode = null;
        accNode = true;
        tmpCon = null;
        id = 0;
    }

    public State getSchemaState() {
        return schemaState;
    }

    public void setSchemaState(State schemaState) {
        if (schemaState == State.DEFAULT && this.schemaState != State.DEFAULT) {
            tmpReset();
        }
        this.schemaState = schemaState;
    }

    public void schemaAct(MouseEvent mouseEvent) {
        switch (getSchemaState()) {
            case DEFAULT:
                System.out.println("DEFAULT block state");
                if (this.tmpCon != null) {
                    tmpCon = null;
                }
                break;
            case PUT_BLOCK:
                System.out.println("PUT block state");
                putBl(mouseEvent.getX(), mouseEvent.getY());
                schemaState = State.DEFAULT;
                break;
            case REMOVE_BLOCK:
                System.out.println("Remove block state");
                // Some actions
                break;
            case ADD_CON_1:
                System.out.println("ADD_CON_1 block state");
                break;
            case ADD_CON_2:
                System.out.println("ADD_CON_1 block state");
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
                case "add":     opNode = new AddBlock(this, schemaPane);
                    break;
                case "sub":     opNode = new SubBlock(this, schemaPane);
                    break;
                case "mul":     opNode = new MulBlock(this, schemaPane);
                    break;
                case "div":     opNode = new DivBlock(this, schemaPane);
                    break;
                case "split":   opNode = new SplitBlock(this, schemaPane);
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
            // TODO: add to itemContainer
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
            opNode.set();
            opNode = null;
            accNode = true;
        });
    }

    public void blockClick(Block caller, MouseEvent e) {
        switch (getSchemaState()) {
            case REMOVE_BLOCK:
                caller.remove();
                setSchemaState(State.DEFAULT);
                break;
            case DEFAULT:
                break;
        }
        e.consume();
    }

    public void blockDrag(Block caller, MouseEvent e) {
        caller.reposition(e.getSceneX() - this.indentX, e.getSceneY() - this.indentY);
        e.consume();
    }

    public void portClick(Port caller, MouseEvent e) {
        switch (getSchemaState()) {
            case REMOVE_BLOCK:
                caller.remove();
                setSchemaState(State.DEFAULT);
                break;
            case ADD_CON_1:
                if (tmpCon == null) {
                    tmpCon = new Connection(this, schemaPane);
                }
                try {
                    caller.setConnection(tmpCon);
                    System.out.println("Connected first");
                } catch (IOException ex) { break; }
                if (caller instanceof InputPort) {
                    tmpCon.setEndPoint(0,
                            caller.getCenterX() - caller.getVisuals().getWidth() / 2,
                            caller.getCenterY());
                }
                else {
                    tmpCon.setEndPoint(0,
                            caller.getCenterX() + caller.getVisuals().getWidth() / 2,
                            caller.getCenterY());
                }
                setSchemaState(State.ADD_CON_2);
                break;
            case ADD_CON_2:
                try {
                    caller.setConnection(tmpCon);
                    System.out.println("Connected second");
                } catch (IOException ex) { break; }
                if (caller instanceof InputPort) {
                    tmpCon.setStartPoint(0,
                            caller.getCenterX() - caller.getVisuals().getWidth() / 2,
                            caller.getCenterY());
                }
                else {
                    tmpCon.setStartPoint(0,
                            caller.getCenterX() + caller.getVisuals().getWidth() / 2,
                            caller.getCenterY());
                }
                tmpCon.set();
                setSchemaState(State.DEFAULT);
                break;
            case DEFAULT:
                break;
        }
        e.consume();
    }


    public int generateId() {
        return id++;
    }

    public void tmpReset() {
        if (tmpCon != null && !tmpCon.isSet()) {
            tmpCon.remove();
        }
        opNode = null;
        accNode = true;
        tmpCon = null;
    }

    // State logic
    public enum State {
        DEFAULT,
        PUT_BLOCK,
        REMOVE_BLOCK,
        ADD_CON_1,
        ADD_CON_2,
    }
}
