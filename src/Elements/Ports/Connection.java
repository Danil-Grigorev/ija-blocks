package Elements.Ports;

import Elements.Blocks.Block;
import Elements.Containers.ConnectionSave;
import Elements.Containers.ItemContainer;
import Elements.Ports.InputPort;
import Elements.Ports.OutputPort;
import Elements.Ports.Port;
import Logic.Logic;
import javafx.application.Platform;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.util.ArrayList;

public class Connection {

    private OutputPort from;
    private InputPort to;
    private int id;

    private ArrayList<Line> lines;
    private ArrayList<Rectangle>  joints;

    private Logic logic;
    private AnchorPane scheme;
    private boolean isSet;

    public Color stColor = Color.GRAY;
    private Color actColor = Color.CYAN.darker();


    public Connection(Logic logic, AnchorPane scheme) {
        this.logic = logic;
        this.scheme = scheme;
        this.from = null;
        this.to = null;
        this.lines = new ArrayList<Line>();
        this.joints = new ArrayList<Rectangle>();
        this.isSet = false;
        this.id = logic.generateId();

        Line tmp = new Line();
        tmp.setStrokeWidth(3);
        tmp.setStroke(stColor);
        this.lines.add(tmp);
    }

    public void setPortIn(OutputPort port) throws IOException {
        if (this.from != null) {
            throw new IOException();
        }
        this.from = port;

        if (this.to != null && this.to.getParent().cycleCheck(this.from.getParent().getId())) {
            this.from = null;
            throw new IOException();
        }
        popupUpdate();
    }

    public void setPortOut(InputPort port) throws IOException {
        if (this.to != null) throw new IOException();
        this.to = port;

        if (this.from != null && this.to.getParent().cycleCheck(this.from.getParent().getId())) {
            this.to = null;
            throw new IOException();
        }
        popupUpdate();
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
        this.scheme.getChildren().addAll(next, joint);
        next.toBack();
        popupUpdate();
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
        lineLeft.toBack();

        this.lines.remove(index + 1);
        this.joints.remove(index);
        this.scheme.getChildren().removeAll(joint, next);
    }

    public void setActive() {
        double strokeWidth;
        for (Line ln : this.lines) {
            strokeWidth = ln.getStrokeWidth();
            ln.setStroke(actColor);
            ln.setStrokeWidth(strokeWidth);
        }
        for (Rectangle joint : this.joints) {
            strokeWidth = joint.getStrokeWidth();
            joint.setStroke(actColor);
            joint.setStrokeWidth(strokeWidth);
        }
        this.to.setActive();
        popupUpdate();
    }

    public void setInactive() {
        double strokeWidth;
        if (this.from == null || this.to == null) { return; }
        for (Line ln : this.lines) {
            strokeWidth = ln.getStrokeWidth();
            ln.setStroke(stColor);
            ln.setStrokeWidth(strokeWidth);
        }
        for (Rectangle joint : this.joints) {
            strokeWidth = joint.getStrokeWidth();
            joint.setStroke(stColor);
            joint.setStrokeWidth(strokeWidth);
        }
        this.to.setInactive();
        popupUpdate();
    }

    public Line getLine(int lineNum) {
        if (this.lines.size() == 0) { return null; }
        if (lineNum < -1 || lineNum > this.lines.size() - 1) { return null; }
        if (lineNum == -1) { return this.lines.get(this.lines.size() - 1); }
        else { return this.lines.get(lineNum); }
    }

    public void setStartPoint(int lineNumber, double X, double Y) {
        assert lineNumber < this.lines.size() && lineNumber >= -1: "Line number out of range";
        Line tmp;
        if (lineNumber == -1) tmp = this.lines.get(this.lines.size() - 1);
        else tmp = this.lines.get(lineNumber);
        tmp.setStartX(X);
        tmp.setStartY(Y);
        tmp.toBack();
    }

    public void setEndPoint(int lineNumber, double X, double Y) {
        assert lineNumber < this.lines.size() && lineNumber >= -1: "Line number out of range";
        Line tmp;
        if (lineNumber == -1) tmp = this.lines.get(this.lines.size() - 1);
        else tmp = this.lines.get(lineNumber);
        tmp.setEndX(X);
        tmp.setEndY(Y);
        tmp.toBack();
    }

    public void set() {
        this.isSet = true;
        Line tmp;
        tmp = this.lines.get(0);
        tmp.setStroke(stColor);
        tmp.setOnMouseClicked(e -> this.logic.connectionClick(this, e));
        tmp.setOnMouseEntered(e -> this.logic.elementHover(e));
        tmp.setOnMouseExited(e -> this.logic.elementHover(e));

        this.scheme.getChildren().addAll(this.lines);
        for (Line ln : this.lines) {
            ln.toBack();
        }
        if (this.from != null && this.from.getParent().isActive()) {
            setActive();
        }

        if (this.from != null) {
            this.from.makeSelected(false);
        }
        if (this.to != null) {
            this.to.makeSelected(false);
        }
        popupUpdate();
    }

    public boolean isSet() {
        return this.isSet;
    }

    public Block getNext() {
        return this.to.getParent();
    }

    public Port getFrom() {
        return this.from;
    }

    public Port getTo() {
        return this.to;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Rectangle> getJoints() {
        return this.joints;
    }

    public void remove() {
        if (this.from != null) {
            this.from.makeSelected(false);
            this.from.removeConnection();
        }
        if (this.to != null) {
            this.to.makeSelected(false);
            this.to.removeConnection();
        }
        Platform.runLater(() -> {
            this.scheme.getChildren().removeAll(this.lines);
            this.scheme.getChildren().removeAll(this.joints);
        });
    }

    public void reposition(Port caller) {
        if (this.from == caller) {
            setStartPoint(0, caller.getCenterX(), caller.getCenterY() );
        }
        else {
            setEndPoint(-1, caller.getCenterX(), caller.getCenterY() );
        }
    }

    public void popupUpdate() {
        String info = "";
        info += "ID: " + getId() + "\n";
        if (this.from == null || this.to == null) return;
        info += "From block: " + getFrom().getParent().getId()
                + " To: " + getTo().getParent().getId() + "\n";
        if (!this.from.isActive()) {
            info += "Propagated value: ?";
        }
        else {
            info += "Propagated value: " + this.from.getParent().getData().getValue()
                    + "\tType: " + this.from.getParent().getData().getType() + "\n";
        }
        Tooltip popupMsg = new Tooltip(info);
        for (Line ln : this.lines) {
            Tooltip.install(ln, popupMsg);
        }
        for (Rectangle joint : this.joints) {
            Tooltip.install(joint, popupMsg);
        }

    }

    public void createSave(ItemContainer container) {
        container.addConnection(new ConnectionSave(this));
    }
}
