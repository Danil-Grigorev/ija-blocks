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

        this.name = "mul";
        this.maxInPorts = 2;
        this.maxOutPorts = 1;
        this.layoutX = 0.0;
        this.layoutY = 0.0;
        this.inputPorts = new ArrayList<InputPort>();
        this.outputPorts = new ArrayList<OutputPort>();
        this.accepted = 0;
    }

    @Override
    public void calculate() {
        int port_num;
        double value;
        if (this.data == null) {
            value = this.inputPorts.get(0).getData().getValue();
            this.data = new IntType(value);
            port_num = 1;
        }
        else {
            value = this.data.getValue();
            port_num = 0;
        }

        for (; port_num < getMaxInPorts(); port_num++) {

            InputPort port = this.inputPorts.get(port_num);
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
        }
        this.data.setValue(value);

        for (InputPort prt : this.inputPorts) {
            prt.dataAccepted();
        }

        setActive();
        popupUpdate(this.shape);
    }

}
