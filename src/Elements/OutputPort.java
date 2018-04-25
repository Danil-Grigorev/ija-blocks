package Elements;

import Logic.Logic;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

import java.io.IOException;

public class OutputPort extends Port {

	public OutputPort(Block parent, Pane stack, Logic logic) {
        this.logic = logic;
        this.stack = stack;
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

    @Override
    public void setConnection(Connection con) throws IOException {
	    if (this.conTo != null) throw new IOException();
	    con.setPortIn(this);
        this.conTo = con;
    }

}
