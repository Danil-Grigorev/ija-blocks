package Elements.Blocks;

import Elements.DataTypes.DataType;
import Elements.DataTypes.DoubleType;
import Elements.DataTypes.FloatType;
import Elements.DataTypes.IntType;
import Elements.Ports.InputPort;
import Elements.Ports.OutputPort;
import Elements.Ports.Port;
import Logic.Logic;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.ArrayList;

public class MulBlock extends Block {

	public MulBlock(Logic logic, AnchorPane scheme) {
        this.scheme = scheme;
        this.logic = logic;
        this.id = this.logic.generateId();

        this.data = null;

        this.name = "*";
        this.maxInPorts = 2;
        this.maxOutPorts = 1;
        this.layoutX = 0.0;
        this.layoutY = 0.0;
        this.inputPorts = new ArrayList<InputPort>();
        this.outputPorts = new ArrayList<OutputPort>();
	}

    @Override
    public void calculate() {
        if (this.data == null) {
            this.data = new IntType(1.0);
        }

        double value = this.data.getValue();
        for (InputPort port : this.inputPorts) {
            DataType newData = port.getData();

            // Retyping
            switch (newData.getType()) {
                case "Double":
                    if (!this.data.getType().equals("Double")) {
                        this.data = new DoubleType(this.data.getValue());
                    }
                    break;
                case "Float":
                    if (this.data.getType().equals("Int")) {
                        this.data = new FloatType(this.data.getValue());
                    }
                    break;
            }

            // Executing
            value *= newData.getValue();
            port.dataAccepted();
        }
        this.data.setValue(value);
        setActive();
    }

    public void dataAccepted() {
        super.dataAccepted();
    }

}
