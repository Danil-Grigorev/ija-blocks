package Logic;

import javafx.application.Platform;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;


public class Logic {
    public State schemaState;
    private AnchorPane schemaPane;
    // Synchronized block operations
    // -----------------------
    private boolean accNode;
    private Rectangle opNode;
    // -----------------------
    public Logic(AnchorPane displayPane) {
        schemaPane = displayPane;
        opNode = null;
        accNode = true;
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
                // Some actions
                schemaState = State.ADD_CON_2;
                break;
            case ADD_CON_2:
                System.out.println("ADD_CON_1 block state");
                // Some actions
                schemaState = State.DEFAULT;
                break;
            default:
                System.out.println("Unknown state");
                break;
        }
        mouseEvent.consume();
    }

    // TODO: add block class as argument
    public synchronized void initBl() {
        Platform.runLater(() -> {
            if (accNode == false) {
                try {
                    wait();
                } catch (InterruptedException e) {
                }
            }
            accNode = false;
            opNode = new Rectangle();
            opNode.setWidth(200);
            opNode.setHeight(100);
            opNode.setArcWidth(20);
            opNode.setArcHeight(20);
            opNode.setOnMouseClicked(e -> blockOp(e));
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
            opNode.setX(X - opNode.getWidth() / 2);
            opNode.setY(Y - opNode.getHeight() / 2);
            schemaPane.getChildren().add(opNode);
            opNode = null;
            accNode = true;
        });
    }

    private void blockOp(MouseEvent e) {
        switch (getSchemaState()) {
            case REMOVE_BLOCK:
                Platform.runLater(() -> schemaPane.getChildren().remove(e.getSource()));
                setSchemaState(State.DEFAULT);
                break;
            case ADD_CON_1:
                break;
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
