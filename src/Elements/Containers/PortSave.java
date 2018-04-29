package Elements.Containers;

import Elements.Port;

import java.io.Serializable;

public class PortSave implements Serializable {

    private double layoutX;
    private double layoutY;
    private int blockId;
    private int id;

    public PortSave (Port port) {
        this.layoutX = port.getLayoutX();
        this.layoutY = port.getLayoutY();
        this.blockId = port.getParent().getId();
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
