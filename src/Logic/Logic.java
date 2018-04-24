package Logic;

import Elements.*;
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
    private Line tmpLn;
    private Connection tmpCon;

    // -----------------------
    public Logic(AnchorPane displayPane) {
        schemaPane = displayPane;
        opNode = null;
        accNode = true;
        tmpLn = null;
    }

    public State getSchemaState() {
        return schemaState;
    }

    public void setSchemaState(State schemaState) {
        this.schemaState = schemaState;
    }

    public void schemaAct(MouseEvent mouseEvent) {
        switch (getSchemaState()) {
            case DEFAULT:
                System.out.println("DEFAULT block state");
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

    public void blockOp(Block caller, MouseEvent e) {
        switch (getSchemaState()) {
            case REMOVE_BLOCK:
                caller.remove();
                setSchemaState(State.DEFAULT);
                break;
            case DEFAULT:
                break;
        }
    }

    public void portOp(Port caller, MouseEvent e) {
        switch (getSchemaState()) {
            case REMOVE_BLOCK:
                caller.remove();
                setSchemaState(State.DEFAULT);
                break;
            case ADD_CON_1:
                if (tmpCon == null) {
                    tmpCon = new Connection(this, schemaPane);
                    tmpCon.setStartPoint(caller.getCenterX(), caller.getCenterY());
                }
                try {
                    caller.setConnection(tmpCon);
                } catch (IOException ex) { break; }
                setSchemaState(State.ADD_CON_2);
                break;
            case ADD_CON_2:
                try {
                    caller.setConnection(tmpCon);
                } catch (IOException ex) { break; }
                tmpCon.setEndPoint(caller.getCenterX(), caller.getCenterY());
                Platform.runLater(() -> {
                    tmpCon.set();
                    tmpCon = null;
                });
                setSchemaState(State.DEFAULT);
            case DEFAULT:
                break;
        }
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
