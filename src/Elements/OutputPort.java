package Elements;

import java.io.IOException;

public class OutputPort extends Port {

	public OutputPort(Block parent) {
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
