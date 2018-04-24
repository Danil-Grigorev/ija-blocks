package Elements;

import javafx.scene.layout.AnchorPane;

import java.awt.*;
import java.io.IOException;

public abstract class Port extends Rectangle{

    protected Connection conTo;
    protected Block parent;
    // TODO: rewrite to "dataType"
    protected double value;
    protected boolean defined;

    public boolean isConnected() {
	    return this.conTo != null;
    }

	public void setConnection(Connection con) {
		this.conTo = con;
	}

	public void remove(AnchorPane schema) {

    }

}
