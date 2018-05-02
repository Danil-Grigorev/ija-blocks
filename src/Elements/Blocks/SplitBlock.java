package Elements.Blocks;

import Elements.Ports.InputPort;
import Elements.Ports.OutputPort;
import Logic.Logic;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.ArrayList;

public class SplitBlock extends Block {

    public SplitBlock(Logic logic, AnchorPane scheme) {
        this.scheme = scheme;
        this.logic = logic;
        this.name = "-<";
        this.maxInPorts = 1;
        this.maxOutPorts = 2;
        this.id = this.logic.generateId();
        this.layoutX = 0.0;
        this.layoutY = 0.0;
        this.valDefined = false;
        this.value = 0.0;
        this.inputPorts = new ArrayList<InputPort>();
        this.outputPorts = new ArrayList<OutputPort>();
    }

    @Override
    public void execute() {
        // Value reset
        if (!this.valDefined) {
            this.value = 0.0;
        }

        if (this.maxInPorts != this.inputPorts.size()) { return;}
        try {
            this.value = this.inputPorts.get(0).getValue();
            this.valDefined = true;
        } catch (IOException e) {
            this.value = 0.0;
            this.valDefined = false;
        }
    }
}
