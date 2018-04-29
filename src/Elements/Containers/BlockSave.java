package Elements.Containers;

import Elements.Block;

import java.io.ObjectOutputStream;
import java.io.Serializable;

public class BlockSave implements Serializable {
    private double layoutX;
    private double layoutY;
    private String name;
    private int id;
    private double maxInPorts;
    private double maxOutPorts;

    public BlockSave(Block block) {
        this.id = block.getId();
        this.name = block.getName();

        this.layoutX = block.getLayoutX();
        this.layoutY = block.getLayoutY();

        this.maxInPorts = block.getMaxInPorts();
        this.maxOutPorts = block.getMaxOutPorts();
    }


    public double getLayoutX() {
        return this.layoutX;
    }

    public double getLayoutY() {
        return this.layoutY;
    }

    public String getName() {
        return this.name;
    }

    public int getId() {
        return this.id;
    }

    public double getMaxInPorts() {
        return this.maxInPorts;
    }

    public double getMaxOutPorts() {
        return this.maxOutPorts;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) { return false; }
        if (!(o instanceof BlockSave)) { return false; }
        if (o == this) { return true; }
        return this.getId() == ((BlockSave) o).getId();
    }

    @Override
    public int hashCode() {
        return this.getId();
    }
}
