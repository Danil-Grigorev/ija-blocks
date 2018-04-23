package Elements;

import java.util.ArrayList;

public abstract class Block {

	protected int id;
	protected ArrayList<InputPort> inputPorts;
	protected ArrayList<OutputPort> outputPorts;
	protected int maxInPorts;
	protected int maxOutPorts;
	protected String name;

    // TODO: rewrite to "dataType"
    protected boolean valDefined;
    protected double value;

	protected int sizeX = 120;
	protected int sizeY = 40;

	public int getId() {
		return this.id;
	}

	public String getName() {
	    return this.name;
    }

	public int getMaxInPorts() {
	    return this.maxInPorts;
    }

    public int getMaxOutPorts() {
	    return this.maxOutPorts;
    }

	public InputPort getInPort(int num) {
	    assert num < getMaxInPorts() : "Accessing port out of range";
		return this.inputPorts.get(num);
	}

	public void setInPort(InputPort inputPort, int num) {
        assert num < getMaxInPorts() : "Accessing port out of range";
        this.inputPorts.set(num, inputPort);
	}

	public OutputPort getOutputPort(int num) {
        assert num < this.getMaxOutPorts() : "Accessing port out of range";
		return this.outputPorts.get(num);
	}

	public void setOutputPort(OutputPort outputPort, int num) {
        assert num < getMaxInPorts() : "Accessing port out of range";
        this.outputPorts.set(num, outputPort);
	}

	// TODO: connect to "dataType"
	public boolean isValDefined() {
	    return this.valDefined;
    }

    // TODO: rewrite to "dataType"
    public double getValue() {
	    return this.value;
    }

	public abstract void execute();
	public abstract void remove();
	public abstract void setVisuals();
}
