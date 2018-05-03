package Elements.Blocks;

import Elements.Containers.BlockSave;
import Elements.Ports.InputPort;
import Elements.Ports.OutputPort;
import Elements.Ports.Port;
import Logic.Logic;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.io.IOException;
import java.util.ArrayList;

public class InOutBlock extends Block {

    private boolean typeIn;
    private Label valLab;

    public InOutBlock(Logic logic, AnchorPane scheme, boolean typeIn) {
        this.scheme = scheme;
        this.logic = logic;

        if (typeIn) {
            this.name = "In";
            this.maxInPorts = 0;
            this.maxOutPorts = 1;
            this.typeIn = true;
        }
        else {
            this.name = "Out";
            this.maxInPorts = 1;
            this.maxOutPorts = 0;
            this.typeIn = false;
        }

        this.sizeX = 80;
        this.sizeY = 40;

        this.id = this.logic.generateId();
        this.layoutX = 0.0;
        this.layoutY = 0.0;

        this.data = null;

        this.inputPorts = new ArrayList<InputPort>();
        this.outputPorts = new ArrayList<OutputPort>();

        this.valLab = null;
        this.accepted = 0;
    }

    public void setVisuals(double X, double Y) {
        super.setVisuals(X, Y);

        this.valLab = new Label();
        this.valLab.setLayoutX(5);
        this.valLab.setLayoutY(5);
        getVisuals().getChildren().add(valLab);
        popupUpdate();
    }

    @Override
    public void calculate() {
        if (this.data == null) { return; }

        if (!this.typeIn) {
            if (this.getOnlyPort().isActive()) {
                this.data = this.getOnlyPort().getData();
                this.getOnlyPort().dataAccepted();
                setActive();
            }
        }
        popupUpdate();
    }

    public Port getOnlyPort() {
        if (this.typeIn) {
            return this.outputPorts.get(0);
        }
        else {
            return this.inputPorts.get(0);
        }
    }

    @Override
    public void popupUpdate() {
        super.popupUpdate();
        if (this.valLab != null) {
            String value = this.data != null && this.data.getStrValue() != null ? this.data.getStrValue() : "?";
            this.valLab.setText(value);
        }
    }
}
