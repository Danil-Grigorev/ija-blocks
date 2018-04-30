package Elements.Containers;

import Elements.Connection;
import Elements.InputPort;
import Elements.OutputPort;
import Logic.Logic;
import javafx.application.Platform;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

public class ConnectionSave implements Serializable {

    private int id;
    private int fromPortId;
    private int toPortId;
    private double startX;
    private double startY;
    private double endX;
    private double endY;
    private ArrayList<Double> jointsX;
    private ArrayList<Double> jointsY;

    public ConnectionSave(Connection con) {
        this.id = con.getId();
        this.fromPortId = con.getFrom().getId();
        this.toPortId = con.getTo().getId();

        Line tmp = con.getLine(0);
        this.startX = tmp.getStartX();
        this.startY = tmp.getStartY();

        tmp = con.getLine(-1);
        this.endX = tmp.getEndX();
        this.endY = tmp.getEndY();

        this.jointsX = new ArrayList<Double>();
        this.jointsY = new ArrayList<Double>();
        for (Rectangle joint : con.getJoints()) {
            this.jointsX.add(joint.getLayoutX() + joint.getWidth()  / 2);
            this.jointsY.add(joint.getLayoutY() + joint.getHeight() / 2);
        }
    }

    public int getId() {
        return this.id;
    }

    public int getFromPortId() {
        return this.fromPortId;
    }

    public int getToPortId() {
        return this.toPortId;
    }

    public ArrayList<Double> getJointsX() {
        return this.jointsX;
    }

    public ArrayList<Double> getJointsY() {
        return this.jointsY;
    }

    public Connection restore(Logic logic, AnchorPane scheme) {
        Connection newCon = new Connection(logic, scheme);
        newCon.setId(this.id);


        newCon.setStartPoint(0, this.startX, this.startY);
        newCon.setEndPoint(-1, this.endX, this.endY);
        newCon.set();

        for (int i = 0; i < this.jointsX.size(); i++) {
            newCon.addJoint(newCon.getLine(-1), this.jointsX.get(i), this.jointsY.get(i));
        }

        return newCon;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) { return false; }
        if (!(o instanceof ConnectionSave)) { return false; }
        if (o == this) { return true; }
        return this.getId() == ((ConnectionSave) o).getId();
    }

    @Override
    public int hashCode() {
        return this.getId();
    }
}
