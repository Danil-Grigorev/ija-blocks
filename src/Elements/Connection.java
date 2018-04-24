package Elements;

import java.io.IOException;

public class Connection {

    private OutputPort portIn;
    private InputPort portOut;

    public Connection() {
        this.portIn = null;
        this.portOut = null;
    }

    public void setPortIn(OutputPort port) {
        this.portIn = port;
    }

    public void setPortOut(InputPort port) {
        this.portOut = port;
    }

    // TODO: rewrite to "dataType"
    public double getValue() throws IOException {
        if (this.portIn == null || this.portOut == null) {
            throw new IOException();
        }
        else {
            return this.portIn.getValue();
        }
    }
}
