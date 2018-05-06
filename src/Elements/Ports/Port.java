package Elements.Ports;

import Elements.Blocks.Block;
import Elements.Containers.ItemContainer;
import Elements.DataTypes.DataType;
import Logic.Logic;
import javafx.application.Platform;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.io.Serializable;

/**
 * @author xgrigo02
 */
public abstract class Port implements Serializable {

    Connection conTo;
    Block parent;
    Pane stack;
    protected Logic logic;
    protected int id;
    boolean blocked;
    boolean selected;

    private int sizeX = 10;
    private int sizeY = 10;
    protected double layoutX;
    protected double layoutY;
    private Rectangle shape;

    private Color stColor = Color.GRAY;
    private Color actColor = Color.CYAN.darker();
    private Color selColor = Color.RED;

    /**
     * Getter for port connection state
     * @return bool is connected
     */
    public boolean isConnected() {
	    return this.conTo != null;
    }

    /**
     * Getter for port been selected for new connection.
     * @return bool is selected
     */
    public boolean isSelected() {
        return this.selected;
    }

    /**
     * Makes port selected as a first port in new connection.
     * @param bool boolean to set
     */
    public void makeSelected(boolean bool) {
        double strokeWidth = this.shape.getStrokeWidth();
        if (bool) {
            if (!isSelected()) {
                this.shape.setStroke(selColor);
                this.selected = true;
            }
        }
        else{
            if (isSelected()) {
                this.shape.setStroke(stColor);
                this.selected = false;
            }
        }
        this.shape.setStrokeWidth(strokeWidth);
    }

    /**
     * Abstract setter for new port connection.
     *
     * @param con           connection to set
     * @throws IOException  throws when cycle was detected, or port is unexpected type.
     */
	abstract public void setConnection(Connection con) throws IOException;

    /**
     * Disconnects port form Connection.
     */
    public void removeConnection() {
        this.conTo = null;
    }

    /**
     * Shows if there is value on input port's block.
     * @return bolean is value
     */
    public boolean isActive() {
        if (this.conTo == null) { return false; }
        else if (this.blocked) { return false; }
        Block from = this.conTo.getFrom().getParent();
        return from.isActive();
    }

    /**
     * Returns data from input port's block, if is connected.
     * @return DataType data
     */
    public DataType getData() {
        assert (this instanceof InputPort) : "Can getData only from Output ports";
        if (this.conTo == null) { return null; }
        else if (this.blocked) { return null; }
        Block from = this.conTo.getFrom().getParent();
        return from.getData();
    }

    /**
     * Accepts data for input port's block.
     */
    public void dataAccepted() {
        assert (this instanceof InputPort) : "Can accept data only from Output port";
        this.conTo.getFrom().getParent().dataAccepted();
    }

    /**
     * Sets port active when new data appears.
     */
    public void setActive() {
        if (this instanceof OutputPort &&  getConTo() != null) {
            this.conTo.setActive();
        }
        double strokeWidth = this.shape.getStrokeWidth();
        this.shape.setStroke(actColor);
        this.shape.setStrokeWidth(strokeWidth);
        popupUpdate();
    }

    /**
     * Sets port inactive when data disappears.
     */
    public void setInactive() {
        if (this instanceof OutputPort &&  getConTo() != null) {
            this.conTo.setInactive();
        }
        double strokeWidth = this.shape.getStrokeWidth();
        this.shape.setStroke(stColor);
        this.shape.setStrokeWidth(strokeWidth);
        popupUpdate();
    }

    /**
     * Getter for ports rectangle.
     * @return  Rectangle
     */
    public Rectangle getVisuals() {
        return this.shape;
    }

    /**
     * Setups visuals for this port on coordinates X, Y.
     * @param X     X coordinate
     * @param Y     Y coordinate
     */
    public void setVisuals(double X, double Y) {
        this.shape = new Rectangle(this.sizeX, this.sizeY, Color.GOLD);
        this.shape.setStroke(stColor);
        this.shape.setStrokeWidth(2);

        this.layoutX = X;
        this.layoutY = Y;

        this.shape.setX(X - this.shape.getWidth() / 2);
        this.shape.setY(Y - this.shape.getHeight() / 2);

        this.shape.setOnMouseClicked(e -> this.logic.portClick(this, e));
        this.shape.setOnMouseEntered(e -> this.logic.elementHover(e));
        this.shape.setOnMouseExited(e -> this.logic.elementHover(e));
        popupUpdate();
    }

    /**
     * Getter for port connection.
     * @return
     */
    public Connection getConTo() {
        return this.conTo;
    }

    /**
     * Sets port on init, and adds it to scheme.
     */
    public void set() {
        this.stack.getChildren().add(this.shape);
    }

    /**
     * Removes port from scheme and all data structures;
     */
    public void remove() {
        if (isConnected()) this.conTo.remove();
        Platform.runLater(() -> this.stack.getChildren().clear());
    }

    /**
     * Getter for ports parent block.
     * @return Block
     */
    public Block getParent() {
        return this.parent;
    }

    /**
     * Getter for ports center X
     * @return double X
     */
    public double getCenterX() {
        return this.parent.getVisuals().getLayoutX() + this.shape.getX() + this.shape.getWidth() / 2;
    }

    /**
     * Getter for ports center Y
     * @return double Y
     */
    public double getCenterY() {
        return this.parent.getVisuals().getLayoutY() + this.shape.getY() + this.shape.getHeight() / 2;
    }

    /**
     * Getter for ports layout X
     * @return double X
     */
    public double getLayoutX() {
        return layoutX;
    }

    /**
     * Getter for ports layout Y
     * @return double Y
     */
    public double getLayoutY() {
        return layoutY;
    }

    /**
     * Getter for ports ID
     * @return int ID
     */
    public int getId() {
        return id;
    }

    /**
     * Setter for ports ID.
     * @param id int new ID to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Enable dragging for ports connection.
     */
    public void reposition() {
        if (isConnected()) this.conTo.reposition(this);
    }

    /**
     * Blocks port from value propagation after execution
     */
    public void block() {
        this.blocked = true;
    }

    /**
     * Unblocks port after step finished.
     */
    public void unblock() {
        this.blocked = false;
    }

    /**
     * Creates popup window on ports shape
     */
    public void popupUpdate() {
        String info = "";
        info += "ID: " + getId() + "\n";
        info += "Block ID: " + getParent().getId() + "\n";
        info += "Connected: " + isConnected();
        Tooltip popupMsg = new Tooltip(info);
        Tooltip.install(this.shape, popupMsg);
    }

    /**
     * Abstract saver for port
     * @param container ItemContainer to put save in
     */
    public abstract void createSave(ItemContainer container);

}
