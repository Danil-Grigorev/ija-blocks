package Elements.Containers;

import Elements.Blocks.Block;
import Elements.Ports.InputPort;
import Elements.Ports.OutputPort;
import Elements.Ports.Port;
import Logic.Logic;

import java.io.Serializable;

public class PortSave implements Serializable {

    private double layoutX;
    private double layoutY;
    private int blockId;
    private int id;
    private int conToId;
    private String type;

    public PortSave (Port port) {
        this.layoutX = port.getLayoutX();
        this.layoutY = port.getLayoutY();
        this.blockId = port.getParent().getId();
        if (port.isConnected()) {
            this.conToId = port.getConTo().getId();
        }
        else { this.conToId = -1; }

        if (port instanceof InputPort) {
            this.type = "in";
        }
        else {
            this.type = "out";
        }
        this.id = port.getId();
    }

    public double getLayoutX() {
        return this.layoutX;
    }

    public double getLayoutY() {
        return this.layoutY;
    }

    public int getBlockId() {
        return this.blockId;
    }

    public int getId() {
        return this.id;
    }

    public String getType() {
        return this.type;
    }

    public int getConToId() {
        return this.conToId;
    }

    public Port restore(Logic logic, Block parent) {
        Port newPort;
        if (this.type.equals("in")) {
            newPort = new InputPort(parent, parent.getVisuals(), logic);
        }
        else {
            newPort = new OutputPort(parent, parent.getVisuals(), logic);
        }
        newPort.setId(this.id);
        newPort.setVisuals(this.layoutX, this.layoutY);
        newPort.set();
        return newPort;

    }

    @Override
    public boolean equals(Object o) {
        if (o == null) { return false; }
        if (!(o instanceof PortSave)) { return false; }
        if (o == this) { return true; }
        return this.getId() == ((PortSave) o).getId();
    }

    @Override
    public int hashCode() {
        return this.getId();
    }
}
