package gravitysandbox.util;

import java.math.BigDecimal;
import java.math.BigInteger;

import static java.math.RoundingMode.HALF_UP;

// TODO: Documentation

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
        this.x = this.y = this.z = new BigDecimal(BigInteger.ZERO);
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
     * Calculates the vector or cross product of this and given vector.
     *
     * @param vector The second vector for the vector product.
     * @return The cross product vector.
     */
    public Vector3D vectorProduct(Vector3D vector) {
        return new Vector3D(
                y.multiply(vector.getZ()).subtract(
                        z.multiply(vector.getY())
                ).setScale(z.scale() > vector.getY().scale() ? vector.getY().scale() : z.scale(), HALF_UP),
                z.multiply(vector.getX()).subtract(
                        x.multiply(vector.getZ())
                ).setScale(x.scale() > vector.getZ().scale() ? vector.getZ().scale() : x.scale(), HALF_UP),
                x.multiply(vector.getY()).subtract(
                        y.multiply(vector.getX())
                ).setScale(y.scale() > vector.getX().scale() ? vector.getX().scale() : y.scale(), HALF_UP)
        );
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
     * Calculates the scalar product of this and given vector.
     *
     * @param vector The second vector for the scalar product.
     * @return The value of the scalar product.
     */
    public BigDecimal scalarProduct(Vector3D vector) {
        return x.multiply(vector.getX()).add(
                y.multiply(vector.getY()).add(
                        z.multiply(vector.getZ())
                )
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
     * Scales the vector to a length of 1.
     */
    public Vector3D unify() {
        BigDecimal length = length();
        return new Vector3D(
                x.divide(length, x.scale() - length.scale(), HALF_UP),
                y.divide(length, y.scale() - length.scale(), HALF_UP),
                z.divide(length, z.scale() - length.scale(), HALF_UP)
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

    public Vector3D stripTrailingZeros() {
        return new Vector3D(x.stripTrailingZeros(),
                y.stripTrailingZeros(),
                z.stripTrailingZeros()
        );
    }

    public int getBDScale() {
        return BigDecimalMath.minScale(x, y, z);
    }

    @Override
    public String toString() {
        return this.x + ", " + this.y + ", " + this.z;
    }

    public String toEngineeringString() {return "(" + this.x.toEngineeringString() + ", " + this.y.toEngineeringString() + ", " + this.z.toEngineeringString() + ")";}
}