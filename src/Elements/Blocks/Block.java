package Elements.Blocks;

import Elements.Containers.BlockSave;
import Elements.Containers.ItemContainer;
import Elements.DataTypes.DataType;
import Elements.Ports.InputPort;
import Elements.Ports.OutputPort;
import Elements.Ports.Port;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import Logic.Logic;


import java.util.ArrayList;

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

    public void addInPort(InputPort port) {
        this.inputPorts.add(port);
    }

    public void addOutPort(OutputPort port) {
        this.outputPorts.add(port);
    }

    public ArrayList<OutputPort> getOutputPorts() {
        return this.outputPorts;
    }

    public ArrayList<InputPort> getInputPorts() {
        return this.inputPorts;
    }

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

    public boolean isActive() {
        return this.data != null;
    }

    public DataType getData() {
        return this.data;
    }

    public void setData(DataType data) {
        this.data = data;
        this.accepted = 0;
        setActive();
    }

    public void setActive() {
        double strokeWidth = this.shape.getStrokeWidth();
        this.shape.setStroke(actColor);
        this.shape.setStrokeWidth(strokeWidth);
        for (OutputPort port : this.outputPorts) {
            port.setActive();
        }
        popupUpdate(this.shape);
    }

    public void setInactive() {
        double strokeWidth = this.shape.getStrokeWidth();
        this.shape.setStroke(stColor);
        this.shape.setStrokeWidth(strokeWidth);
        for (OutputPort port : this.outputPorts) {
            port.setInactive();
        }
        popupUpdate(this.shape);
    }

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

    public void set() {
        Pane stack = getVisuals();
        this.scheme.getChildren().add(stack);
    }

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

    public Pane getVisuals() {
        return this.stack;
    }

    public double getLayoutX() {
        return this.layoutX;
    }

    public double getLayoutY() {
        return this.layoutY;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public int getMaxInPorts() {
        return this.maxInPorts;
    }

    public int getMaxOutPorts() {
        return this.maxOutPorts;
    }

    public void createSave(ItemContainer container) {
        for (Port port : this.inputPorts) {
            port.createSave(container);
        }
        for (Port port : this.outputPorts) {
            port.createSave(container);
        }
        container.addBlock(new BlockSave(this));
    }

    public void dataAccepted(Port from) {
        int acceptedNumExpect = 0;
        for (OutputPort port : getOutputPorts()) {
            if (port.isConnected()) acceptedNumExpect++;
        }

        this.accepted++;
        if (acceptedNumExpect <= this.accepted && this.data != null) {
            this.data = null;
            setInactive();
        }
    }


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

    public abstract void calculate();

}
