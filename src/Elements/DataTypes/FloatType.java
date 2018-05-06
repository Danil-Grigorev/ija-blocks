package Elements.DataTypes;

import java.text.DecimalFormat;

/**
 * @author xgrigo02
 */
public class FloatType extends DataType {
    /**
     * FloatType constructor without value.
     *
     */
    public FloatType() {
        this.value = 0.0;
        this.isDefined = false;
        this.format = new DecimalFormat("#.#");
        this.type = "Float";
    }

    /**
     * FloatType constructor with value.
     *
     * @param value double value to set
     */
    public FloatType(double value) {
        this();
        this.value = Math.floor(value * 10) / 10;
        this.isDefined = true;
    }

    /**
     * Sets FloatType value
     *
     * @param val double value to set
     */
    public void setValue(double val) {
        this.value = Math.floor(val * 10) / 10;
        if (!this.isDefined) {
            this.isDefined = true;
        }
    }


}
