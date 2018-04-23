package Elements;

import java.io.IOException;

public class outputPort extends Port {

	public outputPort(Block parent) {
		this.conTo = null;
		this.parent = parent;
		this.value = 0.0;
		// TODO
	}

    // TODO: rewrite to return "dataType"
    public double getValue() throws IOException {
        if (!isConnected()) {
            throw new IOException();
        }

        return this.conTo.getValue();
    }

}
