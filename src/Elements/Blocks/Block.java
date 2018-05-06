package Elements.Blocks;

import Elements.Containers.BlockSave;
import Elements.Containers.ItemContainer;
import Elements.DataTypes.DataType;
import Elements.Ports.InputPort;
import Elements.Ports.OutputPort;
import Elements.Ports.Port;
import Logic.Logic;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

/**
 * @author xgrigo02
 * @author xcerve23
 */
public abstract class Block {

    protected int id;
    protected int accepted;

    protected ArrayList<InputPort> inputPorts;
    protected ArrayList<OutputPort> outputPorts;
    protected int maxInPorts;
    protected int maxOutPorts;
    protected String name;
    protected Logic logic;
    protected AnchorPane scheme;

    protected DataType data;

    // Block interface
    protected int sizeX = 120;
    protected int sizeY = 60;
    protected double layoutX;
    protected double layoutY;
    protected Rectangle shape;
    protected Pane stack;

    private Color stColor = Color.GRAY;
    private Color actColor = Color.CYAN.darker();

    /**
     * Recursive cycle checker, checks if current block ID is not equal to seeking,
     * or to other blocks, connected to output ports. Returns true, if block was found.
     *
     * @param id    int representation of block id, searched for
     * @return      boolean true if was found, else false
     */
    public boolean cycleCheck(int id) {

        if (getId() == id) { return true; }
        else {
            for (OutputPort port : this.outputPorts) {
                if (!port.isConnected()) { continue; }
                else if (port.getConTo().getNext().cycleCheck(id))
                    return true;
            }
            return false;
        }
    }

    /**
     * Adds IN port to current block.
     *
     * @param port port to be added
     */
    public void addInPort(InputPort port) {
        this.inputPorts.add(port);
    }

    /**
     * Adds OUT port to current block.
     *
     * @param port port to be added
     */
    public void addOutPort(OutputPort port) {
        this.outputPorts.add(port);
    }

    /**
     * Getter for Output ports.
     *
     * @return ArrayList of OutputPorts
     */
    public ArrayList<OutputPort> getOutputPorts() {
        return this.outputPorts;
    }

    /**
     * Getter for Input ports.
     *
     * @return ArrayList of InputPorts
     */
    public ArrayList<InputPort> getInputPorts() {
        return this.inputPorts;
    }

    /**
     * Makes initial setup for all ports.
     */
    public void setupPorts() {
        for (int i = 0; i < getMaxInPorts(); i++) {
            InputPort tmp = new InputPort(this, getVisuals(), this.logic);
            tmp.setVisuals(
                    0,this.sizeY / (getMaxInPorts() + 1) * (i+1));
            tmp.set();
            this.inputPorts.add(tmp);
        }
        for (int i = 0; i < getMaxOutPorts(); i++) {
            OutputPort tmp = new OutputPort(this, getVisuals(), this.logic);
            tmp.setVisuals(
                    this.sizeX,
                    this.sizeY / (getMaxOutPorts() + 1) * (i+1));
            tmp.set();
            this.outputPorts.add(tmp);
        }
    }

    /**
     * Returns if block has data inside.
     *
     * @return boolean
     */
    public boolean isActive() {
        return this.data != null;
    }

    /**
     * Getter for block data.
     *
     * @return DataType or null if block contains nothing
     */
    public DataType getData() {
        return this.data;
    }

    /**
     * Setter for block data.
     *
     * @param data  DataType to be set
     */
    public void setData(DataType data) {
        this.data = data;
        this.accepted = 0;
        setActive();
    }

    /**
     * Sets block active and changes it's appearance, when new data comes.
     */
    public void setActive() {
        double strokeWidth = this.shape.getStrokeWidth();
        this.shape.setStroke(actColor);
        this.shape.setStrokeWidth(strokeWidth);
        for (OutputPort port : this.outputPorts) {
            port.setActive();
        }
        popupUpdate(this.shape);
    }

    /**
     * Sets block inactive and changes it's appearance, when it's data disappears.
     */
    public void setInactive() {
        double strokeWidth = this.shape.getStrokeWidth();
        this.shape.setStroke(stColor);
        this.shape.setStrokeWidth(strokeWidth);
        for (OutputPort port : this.outputPorts) {
            port.setInactive();
        }
        popupUpdate(this.shape);
    }

    /**
     * Removes block form scheme and all data structures.
     */
    public void remove() {
        for (InputPort port: this.inputPorts) {
            port.remove();
        }
        for (OutputPort port : this.outputPorts) {
            if (port == null) continue;
            port.remove();
        }
        Platform.runLater(() -> {
            getVisuals().getChildren().clear();
            this.scheme.getChildren().remove(getVisuals());
        });
    }

    /**
     * Sets block on scheme.
     */
    public void set() {
        Pane stack = getVisuals();
        this.scheme.getChildren().add(stack);
    }

    /**
     * Sets block visual style and position on scheme.
     *
     * @param X     x coordinates.
     * @param Y     y coordinates.
     */
    public void setVisuals(double X, double Y) {
        this.shape = new Rectangle(this.sizeX, this.sizeY, Color.TRANSPARENT);
        this.shape.setStroke(stColor);
        this.shape.setStrokeWidth(2);

        this.shape.setArcWidth(5);
        this.shape.setArcHeight(5);
        this.shape.setOnMouseClicked(e -> this.logic.blockClick(this, e));
        this.shape.setOnMouseDragged(e -> this.logic.blockDrag(this, e));
        this.shape.setOnMouseEntered(e -> this.logic.elementHover(e));
        this.shape.setOnMouseExited(e -> this.logic.elementHover(e));

        Image fill = null;
        switch (getName()) {
            case "add":
                fill = new Image("images/ADD.png");
                break;
            case "sub":
                fill = new Image("images/SUB.png");
                break;
            case "mul":
                fill = new Image("images/MUL.png");
                break;
            case "div":
                fill = new Image("images/DIV.png");
                break;
            case "split":
                fill = new Image("images/SPLIT.png");
                break;
            case "In":
                fill = new Image("images/IN.png");
                break;
            case "Out":
                fill = new Image("images/OUT.png");
                break;
        }
        if (fill != null) {
            this.shape.setFill(new ImagePattern(fill));
        }

        this.layoutX = X;
        this.layoutY = Y;

        this.stack = new Pane();
        this.stack.setPrefSize(this.shape.getWidth(), this.shape.getHeight());
        this.stack.setLayoutX(X - Block.this.shape.getWidth() / 2);
        this.stack.setLayoutY(Y - Block.this.shape.getHeight() / 2);
        this.stack.getChildren().add(this.shape);
        popupUpdate(this.shape);
    }

    /**
     * Moves block to newer position.
     *
     * @param X     new X coordinates
     * @param Y     new Y coordinates
     */
    public void reposition(double X, double Y) {
        this.layoutX = X;
        this.layoutY = Y;
        getVisuals().setLayoutX(X - this.shape.getWidth() / 2);
        getVisuals().setLayoutY(Y - this.shape.getHeight() / 2);
        for (Port port : this.inputPorts) {
            port.reposition();
        }
        for (Port port : this.outputPorts) {
            port.reposition();
        }

    }

    /**
     * Getter for block visual representation.
     *
     * @return Pane stack, it's shape is in
     */
    public Pane getVisuals() {
        return this.stack;
    }

    /**
     * Getter for block X layout.
     *
     * @return double X coordinates
     */
    public double getLayoutX() {
        return this.layoutX;
    }

    /**
     * Getter for block Y layout.
     *
     * @return double Y coordinates
     */
    public double getLayoutY() {
        return this.layoutY;
    }

    /**
     * Block ID getter.
     *
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * Block ID setter.
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Getter for name of current block.
     *
     * @return String
     */
    public String getName() {
        return this.name;
    }

    /**
     * Getter for maximum number of input ports for current block type.
     *
     * @return int
     */
    public int getMaxInPorts() {
        return this.maxInPorts;
    }

    /**
     * Getter for maximum number of output ports for current block type.
     *
     * @return int
     */
    public int getMaxOutPorts() {
        return this.maxOutPorts;
    }

    /**
     * Creates save for this block in specified ItemContainer.
     *
     * @param container     ItemContainer to put save in.
     */
    public void createSave(ItemContainer container) {
        for (Port port : this.inputPorts) {
            port.createSave(container);
        }
        for (Port port : this.outputPorts) {
            port.createSave(container);
        }
        container.addBlock(new BlockSave(this));
    }

    /**
     * Counts accepted messages, and sets port inactive, if all of ports accepted new value
     */
    public void dataAccepted() {
        int acceptedNumExpect = 0;
        for (OutputPort port : getOutputPorts()) {
            if (port.isConnected()) acceptedNumExpect++;
        }

        this.accepted++;
        if (acceptedNumExpect <= this.accepted && this.data != null) {
            this.data = null;
            setInactive();
            for (OutputPort port : getOutputPorts()) {
                port.unblock();
            }
            for (InputPort port : getInputPorts()) {
                port.unblock();
            }
            this.accepted = 0;
        }
    }

    /**
     * Updates block information in popup window.
     *
     * @param nd    sets new info to be displayed on specified node
     */
    public void popupUpdate(Node nd) {
        String info = "";
        info += "ID: " + getId() + "\n";
        info += "Name: " + getName() + "\n";
        if (this.data == null) {
            info += "Value: ?";
        }
        else {
            info += "Value: " + this.data.getStrValue() + "\nType: " + this.data.getType() + "\n";
        }
        Tooltip popupMsg = new Tooltip(info);
        Tooltip.install(nd, popupMsg);
    }

    /**
     * Abstract calculation to be implemented in successors classes.
     */
    public abstract void calculate();

}
