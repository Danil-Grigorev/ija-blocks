package Elements;

import Logic.Logic;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;

import java.io.IOException;

public class OutputPort extends Port {

	public OutputPort(Block parent, AnchorPane schema, Logic logic) {
        this.logic = logic;
        this.schema = schema;
		this.parent = parent;
        this.conTo = null;
        // TODO
	}

    // TODO: rewrite to return "dataType"
    public double getValue() throws IOException {
        if (!isConnected()) {
            throw new IOException();
        }
        else if (!this.parent.isValDefined()) {
            throw new IOException();
        }
        else {
            return this.parent.getValue();
        }
    }


}
