package Elements;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import Logic.Logic;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Random;

public abstract class Block {

    protected int id;
    protected Random rand = new Random();

    protected ArrayList<InputPort> inputPorts;
    protected ArrayList<OutputPort> outputPorts;
    protected int maxInPorts;
    protected int maxOutPorts;
    protected String name;
    protected Logic logic;
    protected AnchorPane schema;

    // TODO: rewrite to "dataType"
    protected boolean valDefined;
    protected double value;

    protected int sizeX = 120;
    protected int sizeY = 60;
    protected Rectangle shape;
    private Pane stack;

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

    public ArrayList<OutputPort> getOutputPorts() {
        return this.outputPorts;
    }

//    public InputPort getInPort(int num) {
//        assert num < this.getMaxInPorts() : "Accessing port out of range";
//        if (this.inputPorts.size() <= num) {
//            return null;
//        }
//        else {
//            return this.inputPorts.get(num);
//        }
//    }
//
//    public void setInPort(InputPort InputPort, int num) {
//        assert num < this.getMaxInPorts();
//        this.inputPorts.set(num, InputPort);
//    }
//
//    public OutputPort getOutputPort(int num) {
//        assert num < this.getMaxOutPorts() : "Accessing port out of range";
//        if (this.outputPorts.size() <= num) {
//            return null;
//        }
//        else {
//            return this.outputPorts.get(num);
//        }
//    }
//
//    public void setOutputPort(OutputPort OutputPort, int num) {
//        assert num < getMaxOutPorts() : "Accessing port out of range";
//        this.outputPorts.set(num, OutputPort);
//    }

    public boolean cycleCheck(int id) {
        System.out.println("Checking block " + getId() + " and " + id);

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

    protected void setupPorts() {
        this.inputPorts = new ArrayList<InputPort>();
        this.outputPorts = new ArrayList<OutputPort>();
        for (int i = 0; i < getMaxInPorts(); i++) {
            InputPort tmp = new InputPort(this, getVisuals(), this.logic);
            tmp.setVisuals(
                    0,sizeY / (getMaxInPorts() + 1) * (i+1));
            tmp.set();
            this.inputPorts.add(tmp);
        }
        for (int i = 0; i < getMaxOutPorts(); i++) {
            OutputPort tmp = new OutputPort(this, getVisuals(), this.logic);
            tmp.setVisuals(
                    sizeX,
                    sizeY / (getMaxOutPorts() + 1) * (i+1));
            tmp.set();
            this.outputPorts.add(tmp);
        }
    }

    // TODO: connect to "dataType"
    public boolean isValDefined() {
        return this.valDefined;
    }

    // TODO: rewrite to "dataType"
    public double getValue() {
        return this.value;
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
            this.schema.getChildren().remove(getVisuals());
        });
    }

    public void set() {
        setupPorts();

        Pane stack = getVisuals();
        this.schema.getChildren().add(stack);
    }

    public void setVisuals(double X, double Y) {
        this.shape = new Rectangle(sizeX, sizeY, Color.TRANSPARENT);
        this.shape.setStroke(Color.BLACK);

        this.shape.setArcWidth(5);
        this.shape.setArcHeight(5);
        this.shape.setOnMouseClicked(e -> this.logic.blockOp(this, e));

        Text shText = new Text(this.name);

        this.stack = new Pane();
        this.stack.setPrefSize(this.shape.getWidth(), this.shape.getHeight());
        this.stack.setLayoutX(X - this.shape.getWidth() / 2);
        this.stack.setLayoutY(Y - this.shape.getHeight() / 2);
        this.stack.getChildren().addAll(this.shape, shText);
    }

    public Pane getVisuals() {
        return this.stack;
    }

    public abstract void execute();

}
