package Elements.Containers;

import Elements.Ports.Connection;
import Logic.Logic;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author xgrigo02
 */
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

    /**
     * Constructor for ConnectionSave
     * @param con connection to create save for
     */
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

    /**
     * Getter for ConnectionSave's ID
     * @return int ID
     */
    public int getId() {
        return this.id;
    }

    /**
     * Getter for first ConnectionSave's port ID
     * @return int ID
     */
    public int getFromPortId() {
        return this.fromPortId;
    }

    /**
     * Getter for first ConnectionSave's port ID
     * @return int ID
     */
    public int getToPortId() {
        return this.toPortId;
    }

    /**
     * Getter for ConnectionSave's joints X coordinates
     * @return ArrayList joints X
     */
    public ArrayList<Double> getJointsX() {
        return this.jointsX;
    }

    /**
     * Getter for ConnectionSave's joints Y coordinates
     * @return ArrayList joints Y
     */
    public ArrayList<Double> getJointsY() {
        return this.jointsY;
    }

    /**
     * Restores Connection from Connection save, and puts it on scheme
     * @param logic     Logic for scheme
     * @param scheme    scheme pane to put connection on
     * @return          Connection restored one
     */
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

    /**
     * Compares ConnectionSave object with other one.
     * @param o object to compare
     * @return boolean result
     */
    @Override
    public boolean equals(Object o) {
        if (o == null) { return false; }
        if (!(o instanceof ConnectionSave)) { return false; }
        if (o == this) { return true; }
        return this.getId() == ((ConnectionSave) o).getId();
    }

    /**
     * Hash code generator for ConnectionSave
     * @return int Hash save
     */
    @Override
    public int hashCode() {
        return this.getId();
    }
}
