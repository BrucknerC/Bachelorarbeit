package gravitysandbox.util;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.nevec.rjm.BigDecimalMath.*;

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
     * Creates a new instance of Vector3D with value of (1, 1, 1).
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
                subtractRound(
                        multiplyRound(y, vector.getZ()),
                        multiplyRound(z, vector.getY())
                ),
                subtractRound(
                        multiplyRound(z, vector.getX()),
                        multiplyRound(x, vector.getZ())
                ),
                subtractRound(
                        multiplyRound(x, vector.getY()),
                        multiplyRound(y, vector.getX())
                )
        );
    }

    /**
     * Adds the given vector to this one.
     *
     * @param vector The vector to be added.
     */
    public Vector3D add(Vector3D vector) {
        return new Vector3D(
                addRound(x, vector.getX()),
                addRound(y, vector.getY()),
                addRound(z, vector.getZ()));
    }

    /**
     * Subtracts the given vector from this one.
     *
     * @param vector The vector to be subtracted.
     */
    public Vector3D subtract(Vector3D vector) {
        return new Vector3D(
                subtractRound(x, vector.getX()),
                subtractRound(y, vector.getX()),
                subtractRound(z, vector.getX())
        );
    }

    /**
     * Calculates the scalar product of this and given vector.
     *
     * @param vector The second vector for the scalar product.
     * @return The value of the scalar product.
     */
    public BigDecimal scalarProduct(Vector3D vector) {
        return addRound(
                multiplyRound(x, vector.getX()),
                addRound(
                        multiplyRound(y, vector.getY()),
                        multiplyRound(z, vector.getZ())
                )
        );

    }

    /**
     * Calculates the length of the vector.
     *
     * @return The length of the vector.
     */
    public BigDecimal length() {
        return sqrt(
                addRound(
                        powRound(x, 2),
                        addRound(
                                powRound(y, 2),
                                powRound(z, 2)
                        )
                )
        );
    }

    /**
     * Scales the vector to a length of 1.
     */
    public Vector3D unify() {
        BigDecimal length = length();
        return new Vector3D(
                divideRound(x, length),
                divideRound(y, length),
                divideRound(z, length)
        );
    }

    /**
     * Scales the vector by given scalar.
     *
     * @param scalar The scalar used for scaling.
     */
    public Vector3D scale(BigDecimal scalar) {
        return new Vector3D(
                multiplyRound(x, scalar),
                multiplyRound(y, scalar),
                multiplyRound(z, scalar)
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

    @Override
    public String toString() {
        return this.x + ", " + this.y + ", " + this.z;
    }
}