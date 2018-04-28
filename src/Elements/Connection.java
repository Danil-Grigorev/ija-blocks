package Elements;

import Logic.Logic;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import javafx.application.Platform;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
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

    public Color stColor = Color.GRAY;

    public Connection(Logic logic, AnchorPane schema) {
        this.logic = logic;
        this.schema = schema;
        this.from = null;
        this.to = null;
        this.lines = new ArrayList<Line>();
        this.joints = new ArrayList<Rectangle>();
        this.isSet = false;

        Line tmp = new Line();
        tmp.setStrokeWidth(3);
        tmp.setStroke(stColor);
        this.lines.add(tmp);
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

    public void addJoint(Line toLine, double X, double Y) {
        int index = this.lines.indexOf(toLine);

        Line next = new Line();
        next.setStrokeWidth(3);
        next.setStroke(stColor);
        next.setOnMouseClicked(e -> this.logic.connectionClick(this, e));
        next.setOnMouseEntered(e -> this.logic.elementHover(e));
        next.setOnMouseExited(e -> this.logic.elementHover(e));

        Rectangle joint = new Rectangle(10,10, Color.GREEN);
        joint.setStroke(stColor);
        joint.setStrokeWidth(2);
        joint.setLayoutX(X - joint.getWidth() / 2);
        joint.setLayoutY(Y - joint.getHeight() / 2);
        joint.setArcWidth(5);
        joint.setArcHeight(5);
        joint.setOnMouseClicked(e -> this.logic.jointClick(this, e));
        joint.setOnMouseDragged(e -> this.logic.jointDrag(this, e));
        joint.setOnMouseEntered(e -> this.logic.elementHover(e));
        joint.setOnMouseExited(e -> this.logic.elementHover(e));


        next.setEndX(toLine.getEndX());
        next.setEndY(toLine.getEndY());

        next.setStartX(X);
        next.setStartY(Y);

        toLine.setEndX(X);
        toLine.setEndY(Y);

        this.lines.set(index, next);
        this.lines.add(index, toLine);
        this.joints.add(index, joint);
        this.schema.getChildren().addAll(next, joint);
    }

    public void repositionJoint(Rectangle joint, double X, double Y) {
        int index = this.joints.indexOf(joint);

        Line tmp = this.lines.get(index);
        tmp.setEndX(X + joint.getWidth() / 2);
        tmp.setEndY(Y + joint.getHeight() / 2);

        tmp = this.lines.get(index + 1);
        tmp.setStartX(X + joint.getWidth() / 2);
        tmp.setStartY(Y + joint.getHeight() / 2);

        joint.setLayoutX(X);
        joint.setLayoutY(Y);

        if (index > 0) {
            this.joints.get(index - 1).toFront();
        }
        else {
            this.from.getVisuals().toFront();
        }

        if (index < this.joints.size() - 1) {
            this.joints.get(index + 1).toFront();
        }
        else {
            this.to.getVisuals().toFront();
        }
    }

    public void removeJoint(Rectangle joint) {
        int index = this.joints.indexOf(joint);
        Line lineLeft = this.lines.get(index);
        Line next = this.lines.get(index + 1);

        lineLeft.setEndX(next.getEndX());
        lineLeft.setEndY(next.getEndY());

        this.lines.remove(index + 1);
        this.joints.remove(index);
        this.schema.getChildren().removeAll(joint, next);
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
        Line tmp;
        tmp = this.lines.get(0);
        tmp.setStroke(stColor);
        tmp.setOnMouseClicked(e -> this.logic.connectionClick(this, e));
        tmp.setOnMouseEntered(e -> this.logic.elementHover(e));
        tmp.setOnMouseExited(e -> this.logic.elementHover(e));

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

    public void reposition(Port caller) {
        if (this.from == caller) {
            setStartPoint(0, caller.getCenterX() + caller.getVisuals().getWidth() / 2, caller.getCenterY() );
        }
        else {
            setEndPoint(-1, caller.getCenterX() - caller.getVisuals().getWidth() / 2, caller.getCenterY() );
        }
    }
}
