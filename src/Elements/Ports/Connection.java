package Elements.Ports;

import Elements.Blocks.Block;
import Elements.Containers.ConnectionSave;
import Elements.Containers.ItemContainer;
import Logic.Logic;
import javafx.application.Platform;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author xgrigo02
 */
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

    /**
     * Connection constructor.
     *
     * @param logic  scheme Logic instance
     * @param scheme scheme pane
     */
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

    /**
     * Set Input port for connection.
     *
     * @param port          port to set.
     * @throws IOException  trows when cycle was detected or connected with Input port.
     */
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

    /**
     * Set Output port for connection.
     *
     * @param port          port to set.
     * @throws IOException  trows when cycle was detected or connected with Output port.
     */
    public void setPortOut(InputPort port) throws IOException {
        if (this.to != null) throw new IOException();
        this.to = port;

        if (this.from != null && this.to.getParent().cycleCheck(this.from.getParent().getId())) {
            this.to = null;
            throw new IOException();
        }
        popupUpdate();
    }

    /**
     * Creates new joint on connection and makes it's setup.
     *
     * @param toLine    to which line showld it depend
     * @param X         On position X
     * @param Y         On position Y
     */
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

    /**
     * Drags joint to specified position X, Y.
     *
     * @param joint     reference to joint, which should be dragged.
     * @param X         on position X
     * @param Y         on position Y
     */
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

    /**
     * Removes joint from schema and connection.

     * @param joint reference to joint
     */
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

    /**
     * Sets joint active when new value on block was detected, and repaints it.
     */
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

    /**
     * Sets joint inactive when value disappears, and repaints it.
     */
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

    /**
     * Returns specified line form lines list.
     *
     * @param lineNum   number of line to be returned
     * @return          Line found or null
     */
    public Line getLine(int lineNum) {
        if (this.lines.size() == 0) { return null; }
        if (lineNum < -1 || lineNum > this.lines.size() - 1) { return null; }
        if (lineNum == -1) { return this.lines.get(this.lines.size() - 1); }
        else { return this.lines.get(lineNum); }
    }

    /**
     * Sets starting point of specified line.
     *
     * @param lineNumber    number of line
     * @param X             new X
     * @param Y             new Y
     */
    public void setStartPoint(int lineNumber, double X, double Y) {
        assert lineNumber < this.lines.size() && lineNumber >= -1: "Line number out of range";
        Line tmp;
        if (lineNumber == -1) tmp = this.lines.get(this.lines.size() - 1);
        else tmp = this.lines.get(lineNumber);
        tmp.setStartX(X);
        tmp.setStartY(Y);
        tmp.toBack();
    }

    /**
     * Sets ending point of specified line.
     *
     * @param lineNumber    number of line
     * @param X             new X
     * @param Y             new Y
     */
    public void setEndPoint(int lineNumber, double X, double Y) {
        assert lineNumber < this.lines.size() && lineNumber >= -1: "Line number out of range";
        Line tmp;
        if (lineNumber == -1) tmp = this.lines.get(this.lines.size() - 1);
        else tmp = this.lines.get(lineNumber);
        tmp.setEndX(X);
        tmp.setEndY(Y);
        tmp.toBack();
    }

    /**
     * New line setup and adding to the pane.
     */
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

    /**
     * Getter for line set state.
     *
     * @return boolean, was line set
     */
    public boolean isSet() {
        return this.isSet;
    }

    /**
     * Getter for block, which is connected to the end port of connection.
     *
     * @return Block, next block
     */
    public Block getNext() {
        return this.to.getParent();
    }

    /**
     * Getter for Output port "from".
     *
     * @return Port, starting one
     */
    public Port getFrom() {
        return this.from;
    }

    /**
     * Getter for Input port "to".
     *
     * @return Port, ending one
     */
    public Port getTo() {
        return this.to;
    }

    /**
     * Getter for Connection ID.
     *
     * @return int ID of connection
     */
    public int getId() {
        return id;
    }

    /**
     * Setter for Connection ID.
     *
     * @param id new ID to be set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Getter for connection joints as list.
     *
     * @return ArrayList of joints
     */
    public ArrayList<Rectangle> getJoints() {
        return this.joints;
    }

    /**
     * Removes connection from pane and data structures.
     */
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

    /**
     * Moves to new position, dictated by source port, which was moved.
     *
     * @param caller    reference to the source port
     */
    public void reposition(Port caller) {
        if (this.from == caller) {
            setStartPoint(0, caller.getCenterX(), caller.getCenterY() );
        }
        else {
            setEndPoint(-1, caller.getCenterX(), caller.getCenterY() );
        }
    }

    /**
     * Updates popup message window.
     */
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

    /**
     * Creates save for this Connection.
     *
     * @param container ItemContainer to put save in
     */
    public void createSave(ItemContainer container) {
        container.addConnection(new ConnectionSave(this));
    }
}
