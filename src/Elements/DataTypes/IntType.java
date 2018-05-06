package Elements.DataTypes;

import java.text.DecimalFormat;

/**
 * @author xgrigo02
 */
public class IntType extends DataType  {
    /**
     * IntType constructor without value.
     *
     */
    public IntType() {
        this.value = 0.0;
        this.isDefined = false;
        this.format = new DecimalFormat("#");
        this.type = "Int";
    }

    /**
     * IntType constructor with value.
     *
     * @param value double value to set
     */
    public IntType(double value) {
        this();
        this.value = Math.floor(value);
        this.isDefined = true;
    }

    /**
     * Sets IntType value
     *
     * @param val double value to set
     */
    public void setValue(double val) {
        this.value = Math.floor(val);
        if (!this.isDefined) {
            this.isDefined = true;
        }
    }

}
