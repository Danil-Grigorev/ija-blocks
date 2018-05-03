package Elements.Ports;

import Elements.Blocks.Block;
import Elements.Containers.ItemContainer;
import Elements.Containers.PortSave;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import Logic.Logic;

import java.io.IOException;

public class InputPort extends Port {

    public InputPort(Block parent, Pane stack, Logic logic) {
        this.logic = logic;
        this.stack = stack;
        this.parent = parent;
        this.conTo = null;
        this.layoutX = 0.0;
        this.layoutY = 0.0;
        this.id = logic.generateId();
        this.blocked = false;
    }

    public void createSave(ItemContainer container) {
        if (isConnected()) {
            this.conTo.createSave(container);
        }
        container.addPort(new PortSave(this));
    }

    @Override
    public void setConnection(Connection con) throws IOException {
        if (this.conTo != null) throw new IOException();
        con.setPortOut(this);
        this.conTo = con;
        popupUpdate();
    }
}
