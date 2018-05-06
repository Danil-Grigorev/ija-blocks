package Elements.Blocks;

import Elements.DataTypes.DataType;
import Elements.Ports.InputPort;
import Elements.Ports.OutputPort;
import Logic.Logic;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;

/**
 * @author xgrigo02
 */
public class SplitBlock extends Block {

    /**
     * SPLIT block constructor.
     *
     * @param logic     Logic of current scheme
     * @param scheme    Pane to put block on
     */
    public SplitBlock(Logic logic, AnchorPane scheme) {
        this.scheme = scheme;
        this.logic = logic;
        this.id = this.logic.generateId();

        this.data = null;

        this.name = "split";
        this.maxInPorts = 1;
        this.maxOutPorts = 2;
        this.layoutX = 0.0;
        this.layoutY = 0.0;
        this.inputPorts = new ArrayList<InputPort>();
        this.outputPorts = new ArrayList<OutputPort>();
        this.accepted = 0;
    }

    /**
     * Makes calculations.
     */
    @Override
    public void calculate() {
        InputPort prt = this.inputPorts.get(0);
        if (prt.isActive()) {
            this.data = prt.getData();
            prt.dataAccepted();
            setActive();
        }
        popupUpdate(this.shape);
    }

}
