package Elements.DataTypes;

import java.text.DecimalFormat;

/**
 * @author xgrigo02
 */
public class DoubleType extends DataType {

    /**
     * DoubleType constructor without value.
     *
     */
    public DoubleType() {
        this.value = 0.0;
        this.isDefined = false;
        this.format = new DecimalFormat("#.####");
        this.type = "Double";
    }

    /**
     * DoubleType constructor with value.
     *
     * @param value double value to set
     */
    public DoubleType(double value) {
        this();
        this.value = Math.floor(value * 10000) / 10000;
        this.isDefined = true;
    }

    /**
     * Sets DoubleType value
     *
     * @param val double value to set
     */
    public void setValue(double val) {
        this.value = Math.floor(val * 10000) / 10000;
        if (!this.isDefined) {
            this.isDefined = true;
        }
    }

}
