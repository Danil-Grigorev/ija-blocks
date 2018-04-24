package Elements;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.io.IOException;
import java.util.ArrayList;

import Logic.Logic;
import javafx.scene.text.Text;

public class AddBlock extends Block {

	public AddBlock(Logic logic, AnchorPane schema) {
	    this.schema = schema;
	    this.logic = logic;
		this.name = "+";
        this.maxInPorts = 2;
        this.maxOutPorts = 1;
        this.valDefined = false;
        this.value = 0.0;
        this.id = Math.abs(rand.nextInt());
        System.out.println("Add block " + this.id + " created.");
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
                this.value += this.inputPorts.get(i).getValue();
            } catch (IOException e) {
                this.value = 0.0;
                this.valDefined = false;
                return;
            }
        }
        this.valDefined = true;
    }
}
