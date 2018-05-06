package Elements.Blocks;

import Elements.Ports.InputPort;
import Elements.Ports.OutputPort;
import Elements.Ports.Port;
import Logic.Logic;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;

public class InOutBlock extends Block {

    private boolean typeIn;
    private Label valLab;

    /**
     * IN or OUt block constructor.
     *
     * @param logic     Logic of current scheme
     * @param scheme    Pane to put block on
     * @param typeIn    specifies whether is IN type or not.
     */
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
        this.valLab.setLayoutX(7);
        this.valLab.setLayoutY(18);
        this.valLab.setMaxWidth(this.shape.getWidth() - 14);
        this.valLab.setOnMouseEntered(e -> this.logic.elementHover(e));
        this.valLab.setOnMouseExited(e -> this.logic.elementHover(e));
        getVisuals().getChildren().add(valLab);
        this.valLab.setVisible(false);
        popupUpdate(this.shape);
    }

    /**
     * Makes calculations.
     */
    @Override
    public void calculate() {

        if (!this.typeIn) {
            if (this.getOnlyPort().isActive()) {
                this.data = this.getOnlyPort().getData();
                this.getOnlyPort().dataAccepted();
                setActive();
            }
        }
        popupUpdate(this.shape);
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
    public void popupUpdate(Node nd) {
        super.popupUpdate(nd);
        if (this.valLab != null) {
            String value = this.data != null && this.data.getStrValue() != null ? this.data.getStrValue() : "?";
            this.valLab.setText(value);
            super.popupUpdate(this.valLab);
        }
    }

    /**
     * Sets value invisible when scheme not in execution state.
     *
     * @param bool bool to set state
     */
    public void hideVal(boolean bool) {
        if (bool) {
            this.valLab.setVisible(false);
        }
        else {
            this.valLab.setVisible(true);
        }
    }
}
