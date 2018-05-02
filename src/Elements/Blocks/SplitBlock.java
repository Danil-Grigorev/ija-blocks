package Elements.Blocks;

import Elements.DataTypes.DataType;
import Elements.DataTypes.DoubleType;
import Elements.DataTypes.FloatType;
import Elements.DataTypes.IntType;
import Elements.Ports.InputPort;
import Elements.Ports.OutputPort;
import Logic.Logic;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.ArrayList;

public class SplitBlock extends Block {

    public SplitBlock(Logic logic, AnchorPane scheme) {
        this.scheme = scheme;
        this.logic = logic;
        this.id = this.logic.generateId();

        this.data = null;

        this.name = "-<";
        this.maxInPorts = 1;
        this.maxOutPorts = 2;
        this.layoutX = 0.0;
        this.layoutY = 0.0;
        this.inputPorts = new ArrayList<InputPort>();
        this.outputPorts = new ArrayList<OutputPort>();
    }

    @Override
    public void calculate() {

        Block bl =  this.inputPorts.get(0).getParent();
        this.data = bl.getData();
        setActive();
        popupUpdate();
    }

    public void dataAccepted() {
        super.dataAccepted();
    }
}
