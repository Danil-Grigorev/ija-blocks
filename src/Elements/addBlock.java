package Elements;

import java.io.IOException;

public class addBlock extends Block {

	public addBlock(String name) {
		this.name = name;
		this.maxInPorts = 2;
		this.maxOutPorts = 1;
		this.valDefined = false;
		this.value = 0.0;
	}

	@Override
	public void execute() {
	    // Value reset
        this.valDefined = false;
	    this.value = 0.0;

        if (this.maxInPorts != this.inputPorts.size()) { return;}
        for (Port port : this.inputPorts) {
            try {
                this.value += port.getValue();
            } catch (IOException) { return;}
        }
        this.valDefined = true;
	}

	// TODO
    @Override
    public void remove() {

    }

    // TODO
    @Override
    public void setVisuals() {

    }


}
