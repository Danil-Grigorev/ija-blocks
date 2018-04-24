package Elements;

import Interface.DoubleConElm;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;

import java.io.IOException;

public class Connection implements DoubleConElm{

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

    @Override
    public Line getVisuals() {
        return null;
    }

    @Override
    public void setVisualsA(double X, double Y) {

    }

    @Override
    public void setVisualsB(double X, double Y) {

    }

    @Override
    public void set() {

    }

    @Override
    public void remove() {

    }
}
