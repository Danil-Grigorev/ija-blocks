package Elements.DataTypes;

import java.text.DecimalFormat;

public class IntType extends DataType  {
    public IntType() {
        this.value = 0.0;
        this.isDefined = false;
        this.format = new DecimalFormat("#");
    }

    public IntType(double value) {
        this();
        this.value = Double.parseDouble(this.format.format(value));
        this.isDefined = true;
    }

    public double getValue() {
        return this.value;
    }

    public void setValue(double val) {
        this.value = Double.parseDouble(this.format.format(val));
        if (!this.isDefined) {
            this.isDefined = true;
        }
    }

    public String getType() {
        return "Int";
    }

    public String getStrValue() {
        return this.format.format(this.value);
    }

}
