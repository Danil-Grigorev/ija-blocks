package Elements;

import java.io.IOException;

public class Connection {

    private outputPort portIn;
    private inputPort portOut;

    public Connection() {
        this.portIn = null;
        this.portOut = null;
    }

    public void setPortIn(outputPort port) {
        this.portIn = port;
    }

    public void setPortOut(inputPort port) {
        this.portOut = port;
    }

    // TODO: rewrite to "dataType"
    public double getValue(Port caller) throws IOException {
        if (this.portIn == null || this.portOut == null) {
            throw new IOException();
        }
        else {
            return this.portIn.getValue();
        }
    }
}
