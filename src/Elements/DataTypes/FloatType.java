package Elements.DataTypes;

import java.text.DecimalFormat;

public class FloatType extends DataType {
    public FloatType() {
        this.value = 0.0;
        this.isDefined = false;
        this.format = new DecimalFormat("#.#");
    }

    public FloatType(double value) {
        this();
        this.value = Math.floor(value * 10) / 10;
        this.isDefined = true;
    }

    public double getValue() {
        return this.value;
    }

    public void setValue(double val) {
        this.value = Math.floor(val * 10) / 10;
        if (!this.isDefined) {
            this.isDefined = true;
        }
    }

    public String getType() {
        return "Float";
    }

    public String getStrValue() {
        return this.format.format(this.value);
    }
}
