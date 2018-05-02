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

public class DivBlock extends Block {

	public DivBlock(Logic logic, AnchorPane scheme) {
        this.scheme = scheme;
        this.logic = logic;
		this.id = this.logic.generateId();

		this.data = null;

		this.name = "/";
		this.maxInPorts = 2;
		this.maxOutPorts = 1;
		this.layoutX = 0.0;
		this.layoutY = 0.0;
		this.inputPorts = new ArrayList<InputPort>();
        this.outputPorts = new ArrayList<OutputPort>();
	}

    @Override
    public void calculate() {
        int port_num;
        if (this.data == null) {
            this.data = this.inputPorts.get(0).getData();
            port_num = 1;
        }
        else {
            port_num = 0;
        }

        double value = this.data.getValue();
        for (InputPort port = this.inputPorts.get(port_num);
             port_num < this.getMaxInPorts() - 1;
             port = this.inputPorts.get(++port_num)) {
            DataType newData = port.getData();

            // Retyping
            switch (newData.getClass().getName()) {
                case "DoubleType":
                    if (!(this.data instanceof DoubleType)) {
                        this.data = new DoubleType(this.data.getValue());
                    }
                    break;
                case "FloatType":
                    if (this.data instanceof IntType) {
                        this.data = new FloatType(this.data.getValue());
                    }
                    break;
            }

            // Executing
            value /= newData.getValue();
            port.dataAccepted();
        }
        this.data.setValue(value);
        setActive();
        popupUpdate();
	}

    public void dataAccepted() {
        super.dataAccepted();
    }
}
