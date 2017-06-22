package gravitysandbox.util;

import java.math.BigDecimal;

import static java.math.RoundingMode.HALF_UP;

/**
 * A three dimensional vector using {@link BigDecimal} for arbitrary precision values with support for the most common vector operations.
 *
 * @author Christoph Bruckner
 * @version 1.0
 * @since 0.1
 */
public class Vector3D {

    /**
     * The x value of the vector.
     */
    private BigDecimal x;

    /**
     * The y value of the vector.
     */
    private BigDecimal y;

    /**
     * The z value of the vector.
     */
    private BigDecimal z;

    /**
     * Creates a new instance of Vector3D with value of (0, 0, 0).
     */
    public Vector3D() {
        this.x = this.y = this.z = BigDecimal.ZERO;
    }

    /**
     * Creates a new instance of Vector3D with the given values.
     *
     * @param x The x component of the vector.
     * @param y The y component of the vector.
     * @param z The z component of the vector.
     */
    public Vector3D(BigDecimal x, BigDecimal y, BigDecimal z) {
        setX(x);
        setY(y);
        setZ(z);
    }

    /**
     * Creates a new instance of Vector3D with the given values.
     * The {@link String} objects will be parsed by the {@link BigDecimal} constructor and set as the coordinates.
     *
     * @param x The x component of the vector.
     * @param y The y component of the vector.
     * @param z The z component of the vector.
     */
    public Vector3D(String x, String y, String z) {
        this.x = new BigDecimal(x);
        this.y = new BigDecimal(y);
        this.z = new BigDecimal(z);
    }

    /**
     * Adds the given vector to this one.
     *
     * @param vector The vector to be added.
     */
    public Vector3D add(Vector3D vector) {
        return new Vector3D(
                x.add(vector.getX()),
                y.add(vector.getY()),
                z.add(vector.getZ()));
    }

    /**
     * Subtracts the given vector from this one.
     *
     * @param vector The vector to be subtracted.
     */
    public Vector3D subtract(Vector3D vector) {
        return new Vector3D(
                x.subtract(vector.getX()),
                y.subtract(vector.getY()),
                z.subtract(vector.getZ())
        );
    }

    /**
     * Calculates the length of the vector.
     *
     * @return The length of the vector.
     */
    public BigDecimal length() {
        return BigDecimalMath.sqrt(
                x.pow(2).add(y.pow(2)).add(z.pow(2))
        );
    }

    /**
     * Scales the vector by given scalar.
     *
     * @param scalar The scalar used for scaling.
     */
    public Vector3D scale(BigDecimal scalar) {
        return new Vector3D(
                x.multiply(scalar).setScale(scalar.scale(), HALF_UP),
                y.multiply(scalar).setScale(scalar.scale(), HALF_UP),
                z.multiply(scalar).setScale(scalar.scale(), HALF_UP)
        );
    }

    /**
     * Setter for x.
     *
     * @param x The new value for x.
     */
    public void setX(BigDecimal x) {
        this.x = x;
    }

    /**
     * Getter for x.
     *
     * @return The current value of x.
     */
    public BigDecimal getX() {
        return x;
    }

    /**
     * Setter for y.
     *
     * @param y The new value for y.
     */
    public void setY(BigDecimal y) {
        this.y = y;
    }

    /**
     * Getter for z.
     *
     * @return The current value of y.
     */
    public BigDecimal getY() {
        return y;
    }

    /**
     * Setter for z.
     *
     * @param z The new value for z.
     */
    public void setZ(BigDecimal z) {
        this.z = z;
    }

    /**
     * Getter for z.
     *
     * @return The current value of z.
     */
    public BigDecimal getZ() {
        return z;
    }

    /**
     * Retrieve the minimum scale of the {@link BigDecimal} coordinates.
     *
     * @return The minimum scale.
     */
    public int getBDScale() {
        return BigDecimalMath.minScale(x, y, z);
    }

    /**
     * Returns a {@link String} representation of the vector.
     *
     * @return The vector as a {@link String}.
     */
    @Override
    public String toString() {
        return this.x + ", " + this.y + ", " + this.z;
    }

    /**
     * Returns a {@link String} representation of the vector using a engineering notation, i.e. using exponent that are multiples of 3, etc.
     *
     * @return The vector as an engineering {@link String}.
     */
    public String toEngineeringString() {
        return "(" + this.x.toEngineeringString() + ", " + this.y.toEngineeringString() + ", " + this.z.toEngineeringString() + ")";
    }
}