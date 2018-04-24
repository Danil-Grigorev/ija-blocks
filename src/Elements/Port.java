package Elements;

import Interface.SingConElm;
import javafx.application.Platform;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import Logic.Logic;
import java.io.IOException;

public abstract class Port implements SingConElm {

    // TODO: rewrite to "dataType"
    protected Connection conTo;
    protected Block parent;
    protected AnchorPane schema;
    protected Logic logic;

    private int sizeX = 10;
    private int sizeY = 10;
    private Rectangle shape;

    public boolean isConnected() {
	    return this.conTo != null;
    }

	abstract public void setConnection(Connection con) throws IOException;

    public Rectangle getVisuals() {
        return this.shape;
    }

    public void setVisuals(double X, double Y) {
        this.shape = new Rectangle(sizeX, sizeY, Color.GOLD);
        this.shape.setStroke(Color.BLACK);

        this.shape.setX(X - this.shape.getWidth() / 2);
        this.shape.setY(Y - this.shape.getHeight() / 2);


        // TODO: add mouse handlers
        this.shape.setOnMouseClicked(e -> this.logic.portOp(this, e));

        StackPane stack = new StackPane();
        stack.setLayoutX(X - this.shape.getWidth() / 2);
        stack.setLayoutY(Y - this.shape.getHeight() / 2);
        stack.getChildren().add(this.shape);
    }


    public void set() {
        StackPane stack = (StackPane) getVisuals().getParent();
        this.schema.getChildren().add(stack);
    }

    public void remove() {
        // TODO: remove connections
        StackPane stack = (StackPane) getVisuals().getParent();
        Platform.runLater(() -> {
            stack.getChildren().removeAll();
            this.schema.getChildren().remove(stack);
        });
    }

    public Block getParent() {
        return this.parent;
    }

    public double getCenterX() {
        return getVisuals().getX() + sizeX / 2;
    }

    public double getCenterY() {
        return getVisuals().getY() + sizeY / 2;
    }

}
