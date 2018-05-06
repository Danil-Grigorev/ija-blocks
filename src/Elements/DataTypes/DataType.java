package Elements.DataTypes;

import java.text.DecimalFormat;

/**
 * @author xgrigo02
 */
public abstract class DataType {

    protected double value;
    protected boolean isDefined;
    protected DecimalFormat format;
    protected String type;

    /**
     * Compares two data types
     *
     * @param obj object to compare
     * @return <code>true</code> if objects are equal, else <code>false</code>
     */
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

    /**
     *  Generates hash code
     *
     * @return <code>int</code> hash for DataType
     */
    @Override
    public int hashCode() {
        return (int) this.value;
    }

    /**
     * Returns DataType value
     *
     * @return double DataType value
     */
    public double getValue() {
        return this.value;
    }

    /**
     * Sets DataType value
     *
     * @param value values to set
     */
    public abstract void setValue(double value);

    /**
     * Type getter
     *
     * @return String representation of DataType type.
     */
    public String getType() {
        return this.type;
    }

    /**
     * Getter for string representation of value.
     *
     * @return String representation of DataType value
     */
    public String getStrValue() {
        return this.format.format(this.value);
    }
}
