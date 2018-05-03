package Elements.DataTypes;

import java.text.DecimalFormat;

public abstract class DataType {

    protected double value;
    protected boolean isDefined;
    protected DecimalFormat format;


    @Override
    public boolean equals(Object obj) {
        if (obj == null) { return false; }
        if (!(obj instanceof DataType)) { return false; }
        if (this.isDefined && ((DataType) obj).isDefined) {
            return this.getValue() == ((DataType) obj).getValue();
        }
        else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return (int) this.value;
    }

    public boolean isDefined() {
        return this.isDefined;
    }

    public abstract double getValue();

    public abstract void setValue(double value);

    public abstract String getType();

    public abstract String getStrValue();
}
