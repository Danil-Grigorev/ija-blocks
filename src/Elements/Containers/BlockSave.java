package Elements.Containers;

import Elements.Blocks.*;
import Elements.Ports.InputPort;
import Elements.Ports.OutputPort;
import javafx.application.Platform;
import javafx.scene.layout.AnchorPane;

import Logic.Logic;
import java.io.Serializable;
import java.util.ArrayList;

public class BlockSave implements Serializable {
    private double layoutX;
    private double layoutY;
    private String name;
    private int id;
    private ArrayList<Integer> InPorts;
    private ArrayList<Integer> OutPorts;

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

    public ArrayList<Integer> getInPorts() {
        return this.InPorts;
    }

    public ArrayList<Integer> getOutPorts() {
        return this.OutPorts;
    }

    public Block restore(Logic logic, AnchorPane scheme) {
        Block newBl;
        switch (this.name) {
            case "+":
                newBl = new AddBlock(logic, scheme);
                // TODO: test and remove
                if (!(newBl instanceof AddBlock)) {
                    Platform.exit();
                    System.exit(99);
                }
                break;
            case "-":
                newBl = new SubBlock(logic, scheme);
                break;
            case "*":
                newBl = new MulBlock(logic, scheme);
                break;
            case "/":
                newBl = new DivBlock(logic, scheme);
                break;
            case "-<":
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
