package Elements.Ports;

import Elements.Blocks.Block;
import Elements.Containers.ItemContainer;
import Elements.Containers.PortSave;
import Logic.Logic;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class OutputPort extends Port {

	public OutputPort(Block parent, Pane stack, Logic logic) {
        this.logic = logic;
        this.stack = stack;
		this.parent = parent;
        this.conTo = null;
        this.id = logic.generateId();
        this.layoutX = 0.0;
        this.layoutY = 0.0;
	}

    public void createSave(ItemContainer container) {
        if (isConnected()) {
            this.conTo.createSave(container);
        }
        container.addPort(new PortSave(this));
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
