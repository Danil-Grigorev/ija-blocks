package Elements.Containers;

import Elements.Connection;
import javafx.scene.shape.Rectangle;

import java.io.Serializable;
import java.util.*;

public class ConnectionSave implements Serializable {

    private int id;
    private int fromPortId;
    private int toPortId;
    private ArrayList<Double> jointsX;
    private ArrayList<Double> jointsY;

    public ConnectionSave(Connection con) {
        this.id = con.getId();
        this.fromPortId = con.getFrom().getId();
        this.toPortId = con.getTo().getId();

        this.jointsX = new ArrayList<Double>();
        this.jointsY = new ArrayList<Double>();
        for (Rectangle joint : con.getJoints()) {
            this.jointsX.add(joint.getLayoutX() - joint.getWidth() / 2);
            this.jointsY.add(joint.getLayoutY() - joint.getHeight() / 2);
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
