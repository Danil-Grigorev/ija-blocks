package Elements;

import java.io.IOException;

public class inputPort extends Port {

    public inputPort(Block parent) {
        this.parent = parent;
        this.conTo = null;
        this.value = 0.0;
    }

    // TODO: rewrite to return "dataType"
    public double setValue(double val) throws IOException {
       // TODO
    }
}
