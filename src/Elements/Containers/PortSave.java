package Elements.Containers;

import Elements.Blocks.Block;
import Elements.Ports.InputPort;
import Elements.Ports.OutputPort;
import Elements.Ports.Port;
import Logic.Logic;

import java.io.Serializable;

/**
 * @author xgrigo02
 */
public class PortSave implements Serializable {

    private double layoutX;
    private double layoutY;
    private int blockId;
    private int id;
    private int conToId;
    private String type;

    /**
     * Constructor for PortSave
     * @param port Port to create save for
     */
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

    /**
     * Getter for PortSave's layout x
     * @return int X
     */
    public double getLayoutX() {
        return this.layoutX;
    }

    /**
     * Getter for PortSave's layout y
     * @return int Y
     */
    public double getLayoutY() {
        return this.layoutY;
    }

    /**
     * Getter for PortSave's block ID
     * @return int ID
     */
    public int getBlockId() {
        return this.blockId;
    }

    /**
     * Getter for PortSave's ID
     * @return int ID
     */
    public int getId() {
        return this.id;
    }

    /**
     * Getter for PorsSave's type
     * @return Staring type
     */
    public String getType() {
        return this.type;
    }

    /**
     * Restores Port from PortSave, and puts it on scheme
     * @param logic     Logic for scheme
     * @return          Port restored one
     */
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

    /**
     * Compares PortSave object with other one.
     * @param o object to compare
     * @return boolean result
     */
    @Override
    public boolean equals(Object o) {
        if (o == null) { return false; }
        if (!(o instanceof PortSave)) { return false; }
        if (o == this) { return true; }
        return this.getId() == ((PortSave) o).getId();
    }

    /**
     * Hash code generator for PortSave
     * @return
     */
    @Override
    public int hashCode() {
        return this.getId();
    }
}
