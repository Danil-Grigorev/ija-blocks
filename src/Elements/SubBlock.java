package Elements;

import Logic.Logic;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.util.ArrayList;

public class SubBlock extends Block{

	public SubBlock(Logic logic, AnchorPane scheme) {
        this.scheme = scheme;
        this.logic = logic;
        this.name = "-";
        this.maxInPorts = 2;
        this.maxOutPorts = 1;
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
        } catch (IOException e) {
            this.value = 0.0;
            this.valDefined = false;
            return;
        }
		for (int i = 1; i < maxInPorts; i++) {
            try {
                this.value -= this.inputPorts.get(i).getValue();
            } catch (IOException e) {
                this.value = 0.0;
                this.valDefined = false;
                return;
            }
        }
		this.valDefined = true;
	}

}
