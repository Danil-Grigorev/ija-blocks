package Elements;

import Logic.Logic;
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

    public Connection(Logic logic, AnchorPane schema) {
        this.logic = logic;
        this.schema = schema;
        this.from = null;
        this.to = null;
        this.lines = new ArrayList<Line>();
        this.joints = new ArrayList<Rectangle>();
    }

    public void setPortIn(OutputPort port) {
        this.from = port;
    }

    public void setPortOut(InputPort port) {
        this.to = port;
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

    public ArrayList<Line> getLines() {
        return this.lines;
    }
    public ArrayList<Rectangle> getJoints() { return joints; }

    public void setStartPoint(double X, double Y) {
        Line tmp = new Line();
        tmp.setStartX(X);
        tmp.setStartY(Y);
        this.lines.add(tmp);
    }

    public void setEndPoint(double X, double Y) {
        Line tmp = this.lines.get(0);
        tmp.setEndX(X);
        tmp.setEndY(Y);
    }

    public void set() {
        Platform.runLater(() -> {
            this.schema.getChildren().addAll(this.lines);
            this.schema.getChildren().addAll(this.joints);
        });
    }

    public void remove() {
        Platform.runLater(() -> {
            this.schema.getChildren().removeAll(this.lines);
            this.schema.getChildren().removeAll(this.joints);
        });
    }
}
