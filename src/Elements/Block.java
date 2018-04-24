package Elements;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import Logic.Logic;
import javafx.scene.text.Text;

import java.util.ArrayList;

public abstract class Block {

    protected int id;
    protected ArrayList<InputPort> inputPorts;
    protected ArrayList<OutputPort> outputPorts;
    protected int maxInPorts;
    protected int maxOutPorts;
    protected String name;
    protected Logic parent;

    // TODO: rewrite to "dataType"
    protected boolean valDefined;
    protected double value;

    protected int sizeX = 120;
    protected int sizeY = 40;
    protected Rectangle shape;

    public int getId() {
        return this.id;
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

    public InputPort getInPort(int num) {
        assert num < getMaxInPorts() : "Accessing port out of range";
        return this.inputPorts.get(num);
    }

    public void setInPort(InputPort InputPort, int num) {
        assert num < getMaxInPorts() : "Accessing port out of range";
        this.inputPorts.set(num, InputPort);
    }

    public OutputPort getOutputPort(int num) {
        assert num < this.getMaxOutPorts() : "Accessing port out of range";
        return this.outputPorts.get(num);
    }

    public void setOutputPort(OutputPort OutputPort, int num) {
        assert num < getMaxInPorts() : "Accessing port out of range";
        this.outputPorts.set(num, OutputPort);
    }

    // TODO: connect to "dataType"
    public boolean isValDefined() {
        return this.valDefined;
    }

    // TODO: rewrite to "dataType"
    public double getValue() {
        return this.value;
    }

    public void remove(AnchorPane schema) {
        for (InputPort port: this.inputPorts) {
            port.remove(schema);
        }
        for (OutputPort port: this.outputPorts) {
            port.remove(schema);
        }
        StackPane stack = (StackPane) getVisuals().getParent();
        stack.getChildren().removeAll();
        schema.getChildren().remove(stack);
    }

    public void set(AnchorPane schema) {
        StackPane stack = (StackPane) getVisuals().getParent();
        schema.getChildren().add(stack);
    }

    public void setVisuals(double X, double Y) {
        this.shape = new Rectangle(sizeX, sizeY, Color.TRANSPARENT);
        this.shape.setStroke(Color.BLACK);

        this.shape.setX(X - this.shape.getWidth() / 2);
        this.shape.setY(Y - this.shape.getHeight() / 2);
        this.shape.setArcWidth(5);
        this.shape.setArcHeight(5);
        this.shape.setOnMouseClicked(e -> this.parent.blockOp(this, e));

        Text shText = new Text(this.name);

        StackPane stack = new StackPane();
        stack.setLayoutX(X - this.shape.getWidth() / 2);
        stack.setLayoutY(Y - this.shape.getHeight() / 2);
        stack.getChildren().addAll(this.shape, shText);
    }

    public Rectangle getVisuals() {
        return this.shape;
    }

    public abstract void execute();

}
