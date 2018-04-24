package Logic;

import Elements.*;
import javafx.application.Platform;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;


public class Logic {
    public State schemaState;
    private AnchorPane schemaPane;
    // Synchronized block operations
    // -----------------------
    private boolean accNode;
    private Block opNode;

    private Rectangle tmp;
    private Line tmpLn;

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
                case "add":     opNode = new AddBlock(this);
                    break;
                case "sub":     opNode = new SubBlock(this);
                    break;
                case "mul":     opNode = new MulBlock(this);
                    break;
                case "div":     opNode = new DivBlock(this);
                    break;
                case "split":   opNode = new SplitBlock(this);
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
            opNode.set(schemaPane);
            opNode = null;
            accNode = true;
        });
    }

    public void blockOp(Block caller, MouseEvent e) {
        switch (getSchemaState()) {
            case REMOVE_BLOCK:
                caller.remove(schemaPane);
                setSchemaState(State.DEFAULT);
                break;
            case ADD_CON_1:
                tmp = caller.getVisuals();
                tmpLn = new Line();
                tmpLn.setStartX(tmp.getX() + tmp.getWidth());
                tmpLn.setStartY(e.getY());
                setSchemaState(State.ADD_CON_2);
                break;
            case ADD_CON_2:
                tmp = caller.getVisuals();
                tmpLn.setEndX(tmp.getX());
                tmpLn.setEndY(e.getY());
                Platform.runLater(() -> {
                    schemaPane.getChildren().add(tmpLn);
                    tmpLn = null;
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
