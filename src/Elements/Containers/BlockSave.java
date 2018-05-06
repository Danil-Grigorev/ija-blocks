package Elements.Containers;

import Elements.Blocks.*;
import Elements.Ports.InputPort;
import Elements.Ports.OutputPort;
import Logic.Logic;
import javafx.scene.layout.AnchorPane;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * @author xgrigo02
 */
public class BlockSave implements Serializable {
    private double layoutX;
    private double layoutY;
    private String name;
    private int id;
    private ArrayList<Integer> InPorts;
    private ArrayList<Integer> OutPorts;

    /**
     * BlockSave constructor
     * @param block block for save creation
     */
    public BlockSave(Block block) {
        this.id = block.getId();
        this.name = block.getName();

        this.layoutX = block.getLayoutX();
        this.layoutY = block.getLayoutY();

        ArrayList<InputPort> blockInputPorts = block.getInputPorts();
        this.InPorts = new ArrayList<>();
        for (InputPort in : blockInputPorts) {
            this.InPorts.add(in.getId());
        }

        ArrayList<OutputPort> blockOutputPorts = block.getOutputPorts();
        this.OutPorts = new ArrayList<>();
        for (OutputPort out : blockOutputPorts) {
            this.OutPorts.add(out.getId());
        }
    }


    /**
     * Getter for BlockSave's layout x
     * @return int X
     */
    public double getLayoutX() {
        return this.layoutX;
    }

    /**
     * Getter for BlockSave's layout y
     * @return int Y
     */
    public double getLayoutY() {
        return this.layoutY;
    }

    /**
     * Getter for BlockSave's name
     * @return String name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Getter for blockSave's ID
     * @return int ID
     */
    public int getId() {
        return this.id;
    }

    /**
     * Getter for blockSave's input ports
     * @return ArrayList of ports
     */
    public ArrayList<Integer> getInPorts() {
        return this.InPorts;
    }

    /**
     * Getter for blockSave's output ports
     * @return ArrayList of ports
     */
    public ArrayList<Integer> getOutPorts() {
        return this.OutPorts;
    }

    /**
     * Restore Block from BlockSave on scheme and adds it to scheme pane.
     * @param logic     Scheme Logic
     * @param scheme    scheme Pane
     * @return          Block created
     */
    public Block restore(Logic logic, AnchorPane scheme) {
        Block newBl;
        switch (this.name) {
            case "add":
                newBl = new AddBlock(logic, scheme);
                break;
            case "sub":
                newBl = new SubBlock(logic, scheme);
                break;
            case "mul":
                newBl = new MulBlock(logic, scheme);
                break;
            case "div":
                newBl = new DivBlock(logic, scheme);
                break;
            case "split":
                newBl = new SplitBlock(logic, scheme);
                break;
            case "In":
                newBl = new InOutBlock(logic, scheme, true);
                break;
            case "Out":
                newBl = new InOutBlock(logic, scheme, false);
                break;
            default:
                newBl = null;
                break;
        }

        newBl.setId(this.id);
        newBl.setVisuals(this.layoutX, this.layoutY);

        newBl.set();
        return newBl;
    }

    /**
     * Compares BlockSave object with other one.
     * @param o object to compare
     * @return boolean result
     */
    @Override
    public boolean equals(Object o) {
        if (o == null) { return false; }
        if (!(o instanceof BlockSave)) { return false; }
        if (o == this) { return true; }
        return this.getId() == ((BlockSave) o).getId();
    }

    /**
     * Hash code generator for BlockSave
     * @return
     */
    @Override
    public int hashCode() {
        return this.getId();
    }

}
