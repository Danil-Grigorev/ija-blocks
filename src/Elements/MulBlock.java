package Elements;

public class MulBlock extends Block{

	public MulBlock(String name, InputPort port1, InputPort port2) {
		super(name, port1, port2);
		calculation();
	}

	@Override
	public void calculation() {
		if (this.getPort1() != null && this.getPort2() != null) {
			this.setOutputPort(new OutputPort(getPort1().getValue() * this.getPort2().getValue(), "mulOutputPort"));
		}else{
			this.setOutputPort(null);
		}

	}

}
