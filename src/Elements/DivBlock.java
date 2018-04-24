package Elements;

import Logic.Logic;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;

import java.io.IOException;

public class DivBlock extends Block {

	public DivBlock(Logic parent) {
        this.parent = parent;
		this.name = "/";
		this.maxInPorts = 2;
		this.maxOutPorts = 1;
		this.valDefined = false;
		this.value = 0.0;
		this.id = (int) Math.random();
        System.out.println("Div block " + this.id + " created.");
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
			this.valDefined = false;
			return;
		}
		for (int i = 1; i < maxInPorts; i++) {
			try {
				this.value /= this.inputPorts.get(i).getValue();
			} catch (IOException e) {
				this.valDefined = false;
				return;
			}
		}
		this.valDefined = true;
	}
}
