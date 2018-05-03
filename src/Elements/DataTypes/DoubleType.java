package Elements.DataTypes;

import java.text.DecimalFormat;

public class DoubleType extends DataType {
    public DoubleType() {
        this.value = 0.0;
        this.isDefined = false;
        this.format = new DecimalFormat("#.####");
    }

    public DoubleType(double value) {
        this();
        this.value = Math.floor(value * 10000) / 10000;
        this.isDefined = true;
    }

    public double getValue() {
        return this.value;
    }

    public void setValue(double val) {
        this.value = Math.floor(val * 10000) / 10000;
        if (!this.isDefined) {
            this.isDefined = true;
        }
    }

    public String getType() {
        return "Double";
    }

    public String getStrValue() {
        return this.format.format(this.value);
    }
}
