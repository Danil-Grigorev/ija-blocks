package Elements;

import Elements.Containers.ItemContainer;
import Elements.Containers.PortSave;
import Interface.SingConElm;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import Logic.Logic;
import java.io.IOException;
import java.io.Serializable;

public abstract class Port implements SingConElm, Serializable {

    // TODO: rewrite to "dataType"
    protected Connection conTo;
    protected Block parent;
    protected Pane stack;
    protected Logic logic;
    protected int id;

    private int sizeX = 10;
    private int sizeY = 10;
    protected double layoutX;
    protected double layoutY;
    private Rectangle shape;

    private Color stColor = Color.GRAY;

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
        this.shape.setStroke(stColor);
        this.shape.setStrokeWidth(2);

        this.layoutX = X;
        this.layoutY = Y;

        this.shape.setX(X - this.shape.getWidth() / 2);
        this.shape.setY(Y - this.shape.getHeight() / 2);

        this.shape.setOnMouseClicked(e -> this.logic.portClick(this, e));
        this.shape.setOnMouseEntered(e -> this.logic.elementHover(e));
        this.shape.setOnMouseExited(e -> this.logic.elementHover(e));

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

    public double getLayoutX() {
        return layoutX;
    }

    public double getLayoutY() {
        return layoutY;
    }

    public int getId() {
        return this.id;
    }

    public void reposition() {
        if (isConnected()) this.conTo.reposition(this);
    }

    public void createSave(ItemContainer container) {
        if (isConnected()) {
            this.conTo.createSave(container);
        }
        container.addPort(new PortSave(this));
    }

}
