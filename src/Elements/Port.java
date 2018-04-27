package Elements;

import Interface.SingConElm;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import Logic.Logic;
import java.io.IOException;

public abstract class Port implements SingConElm {

    // TODO: rewrite to "dataType"
    protected Connection conTo;
    protected Block parent;
    protected Pane stack;
    protected Logic logic;

    private int sizeX = 10;
    private int sizeY = 10;
    private Rectangle shape;

    public boolean isConnected() {
	    return this.conTo != null;
    }

	abstract public void setConnection(Connection con) throws IOException;

    public void removeConnection() {
        this.conTo = null;
    }

    public Rectangle getVisuals() {
        return this.shape;
    }

    public void setVisuals(double X, double Y) {
        this.shape = new Rectangle(sizeX, sizeY, Color.GOLD);
        this.shape.setStroke(Color.BLACK);

        this.shape.setX(X - this.shape.getWidth() / 2);
        this.shape.setY(Y - this.shape.getHeight() / 2);


        // TODO: add all mouse handlers
        this.shape.setOnMouseClicked(e -> this.logic.portClick(this, e));

    }

    public Connection getConTo() {
        return this.conTo;
    }

    public void set() {
        this.stack.getChildren().add(this.shape);
    }

    public void remove() {
        if (isConnected()) this.conTo.remove();
        Platform.runLater(() -> this.stack.getChildren().clear());
    }

    public Block getParent() {
        return this.parent;
    }

    public double getCenterX() {
        return this.parent.getVisuals().getLayoutX() + this.shape.getX() + this.shape.getWidth() / 2;
    }

    public double getCenterY() {
        return this.parent.getVisuals().getLayoutY() + this.shape.getY() + this.shape.getHeight() / 2;
    }

    public void reposition() {
        if (isConnected()) this.conTo.reposition(this);
    }

}
