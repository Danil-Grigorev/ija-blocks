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
    }

    @Override
    public void calculate() {
        if (this.data == null) { return; }

        if (!this.typeIn) {
            if (this.getOnlyPort().isActive()) {
                this.data = this.getOnlyPort().getData();
            }
        }
    }

    public Port getOnlyPort() {
        if (this.typeIn) {
            return this.outputPorts.get(0);
        }
        else {
            return this.inputPorts.get(0);
        }
    }

    public void dataAccepted() {
        super.dataAccepted();
    }

}
