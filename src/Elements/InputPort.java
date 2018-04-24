package Elements;

import java.io.IOException;

public class InputPort extends Port {

    public InputPort(Block parent) {
        this.parent = parent;
        this.conTo = null;
        this.value = 0.0;
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
}
