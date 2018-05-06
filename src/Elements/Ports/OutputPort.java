package Elements.Ports;

import Elements.Blocks.Block;
import Elements.Containers.ItemContainer;
import Elements.Containers.PortSave;
import Logic.Logic;
import javafx.scene.layout.Pane;

import java.io.IOException;

/**
 * @author xgrigo02
 */
public class OutputPort extends Port {

    /**
     * Output port constructor.
     *
     * @param parent    block to connect with
     * @param stack     stack to put on
     * @param logic     Logic class of scheme
     */
	public OutputPort(Block parent, Pane stack, Logic logic) {
        this.logic = logic;
        this.stack = stack;
		this.parent = parent;
        this.conTo = null;
        this.id = logic.generateId();
        this.layoutX = 0.0;
        this.layoutY = 0.0;
        this.blocked = false;
        this.selected = false;
    }

    /**
     * Creates save of this port to ItemContainer.
     *
     * @param container ItemContainer to put save in
     */
    public void createSave(ItemContainer container) {
        if (isConnected()) {
            this.conTo.createSave(container);
        }
        container.addPort(new PortSave(this));
    }

    /**
     * Connects port with some connection.
     *
     * @param con           Connection to connect
     * @throws IOException  raises when cycle was detected.
     */
    @Override
    public void setConnection(Connection con) throws IOException {
	    if (this.conTo != null) throw new IOException();
	    con.setPortIn(this);
        this.conTo = con;
        popupUpdate();
    }

}
