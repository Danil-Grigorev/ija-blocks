package Elements;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import Logic.Logic;

import java.io.IOException;

public class InputPort extends Port {

    public InputPort(Block parent, Pane stack, Logic logic) {
        this.logic = logic;
        this.stack = stack;
        this.parent = parent;
        this.conTo = null;
    }

    // TODO: rewrite to return "dataType"
    public double getValue() throws IOException {
        if (!isConnected()) {
            throw new IOException();
        }
        else {
            return this.conTo.getValue();
        }
    }

    @Override
    public void setConnection(Connection con) throws IOException {
        if (this.conTo != null) throw new IOException();
        con.setPortOut(this);
        this.conTo = con;
    }
}
