
public abstract class Block {

    private String name;
    private InputPort port1;
    private InputPort port2;
    private OutputPort outputPort;




    public Block(String name, InputPort port1, InputPort port2) {
        super();
        this.name = name;
        this.port1 = port1;
        this.port2 = port2;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public InputPort getPort1() {
        return port1;
    }
    public void setPort1(InputPort port1) {
        this.port1 = port1;
    }
    public InputPort getPort2() {
        return port2;
    }
    public void setPort2(InputPort port2) {
        this.port2 = port2;
    }
    public OutputPort getOutputPort() {
        return outputPort;
    }
    public void setOutputPort(OutputPort outputPort) {
        this.outputPort = outputPort;
    }

    public abstract void calculation();
}
