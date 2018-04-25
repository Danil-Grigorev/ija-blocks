package Elements;

import Logic.Logic;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import javafx.application.Platform;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.util.ArrayList;

public class Connection {

    private OutputPort from;
    private InputPort to;

    private ArrayList<Line> lines;
    private ArrayList<Rectangle>  joints;

    private Logic logic;
    private AnchorPane schema;
    private boolean isSet;

    public Connection(Logic logic, AnchorPane schema) {
        this.logic = logic;
        this.schema = schema;
        this.from = null;
        this.to = null;
        this.lines = new ArrayList<Line>();
        this.joints = new ArrayList<Rectangle>();
        this.isSet = false;
        this.lines.add(new Line());
    }

    public void setPortIn(OutputPort port) throws IOException {
        if (this.from != null)
            throw new IOException();
        this.from = port;

        if (this.to != null && this.to.getParent().cycleCheck(this.from.getParent().getId())) {
            this.from = null;
            throw new IOException();
        }

    }

    public void setPortOut(InputPort port) throws IOException {
        if (this.to != null) throw new IOException();
        this.to = port;

        if (this.from != null && this.to.getParent().cycleCheck(this.from.getParent().getId())) {
            this.to = null;
            throw new IOException();
        }
    }

    // TODO: rewrite to "dataType"
    public double getValue() throws IOException {
        if (this.from == null || this.to == null) {
            throw new IOException();
        }
        else {
            return this.from.getValue();
        }
    }

    public void setStartPoint(int lineNumber, double X, double Y) {
        assert lineNumber < this.lines.size() && lineNumber >= -1: "Line number out of range";
        Line tmp;
        if (lineNumber == -1) tmp = this.lines.get(this.lines.size() - 1);
        else tmp = this.lines.get(lineNumber);
        tmp.setStartX(X);
        tmp.setStartY(Y);
    }

    public void setEndPoint(int lineNumber, double X, double Y) {
        assert lineNumber < this.lines.size() && lineNumber >= -1: "Line number out of range";
        Line tmp;
        if (lineNumber == -1) tmp = this.lines.get(this.lines.size() - 1);
        else tmp = this.lines.get(lineNumber);
        tmp.setEndX(X);
        tmp.setEndY(Y);
    }

    public void set() {
        this.isSet = true;
        Platform.runLater(() -> {
            this.schema.getChildren().addAll(this.lines);
            this.schema.getChildren().addAll(this.joints);
        });
    }

    public boolean isSet() {
        return this.isSet;
    }

    public Block getNext() {
        return this.to.getParent();
    }

    public void remove() {
        if (this.from   != null) { this.from.removeConnection(); }
        if (this.to     != null) { this.to.removeConnection(); }
        Platform.runLater(() -> {
            this.schema.getChildren().removeAll(this.lines);
            this.schema.getChildren().removeAll(this.joints);
        });
    }

    public void reposition(Port caller, double diffX, double diffY) {
        Line tmp;
        if (this.from == caller) {
            tmp = this.lines.get(0);
            setStartPoint(0, tmp.getStartX() + diffX, tmp.getStartY() + diffY );
        }
        else {
            tmp = this.lines.get(this.lines.size() - 1);
            setEndPoint(-1, tmp.getStartX() + diffX, tmp.getStartY() + diffY );
        }
    }
}
