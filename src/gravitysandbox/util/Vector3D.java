package gravitysandbox.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

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
        this.x = this.y = this.z = new BigDecimal(BigInteger.ONE);
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
        return new Vector3D(getY().multiply(vector.getZ()).subtract(getZ().multiply(vector.getY())),
                getZ().multiply(vector.getX()).subtract(getX().multiply(vector.getZ())),
                getX().multiply(vector.getY()).subtract(getY().multiply(vector.getX())));
    }

    /**
     * Adds the given vector to this one.
     *
     * @param vector The vector to be added.
     */
    public void add(Vector3D vector) {
        setX(getX().add(vector.getX()));
        setY(getY().add(vector.getY()));
        setZ(getZ().add(vector.getZ()));
    }

    /**
     * Subtracts the given vector from this one.
     *
     * @param vector The vector to be subtracted.
     */
    public void subtract(Vector3D vector) {
        setX(getX().subtract(vector.getX()));
        setY(getY().subtract(vector.getY()));
        setZ(getZ().subtract(vector.getZ()));
    }

    /**
     * Calculates the scalar product of this and given vector.
     *
     * @param vector The second vector for the scalar product.
     * @return The value of the scalar product.
     */
    public BigDecimal scalarProduct(Vector3D vector) {
        return getX().multiply(vector.getX())
                .add(getY().multiply(vector.getY()))
                .add(getZ().multiply(vector.getZ()));
    }

    /**
     * Calculates the length of the vector.
     *
     * @return The length of the vector.
     */
    public BigDecimal length() {
        return sqrt(new BigDecimal(BigInteger.ZERO)
                .add(x.pow(2))
                .add(y.pow(2))
                .add(z.pow(2))
        );
    }

    /**
     * Scales the vector to a length of 1.
     */
    public void unify() {
        BigDecimal length = length();
        setX(getX().divide(length, RoundingMode.HALF_UP));
        setY(getY().divide(length, RoundingMode.HALF_UP));
        setZ(getZ().divide(length, RoundingMode.HALF_UP));
    }

    /**
     * Scales the vector by given scalar.
     *
     * @param scalar The scalar used for scaling.
     */
    public void scale(BigDecimal scalar) {
        setX(getX().multiply(scalar));
        setY(getY().multiply(scalar));
        setZ(getZ().multiply(scalar));
    }

    /**
     * Setter for x.
     *
     * @param x
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
     * @param y
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
     * @param z
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
     * Calculate the square root of the given {@link BigDecimal}.
     * <p>
     * It uses the babylonian method (https://en.wikipedia.org/wiki/Methods_of_computing_square_roots#Babylonian_method) for calculating the square root.
     * Source: http://stackoverflow.com/a/19743026 (accessed 2016-01-11)
     *
     * @param A The {@link BigDecimal} of which the square root will be calculated.
     * @return The square root of A.
     * @author barwnikk
     */
    private BigDecimal sqrt(BigDecimal A) {
        BigDecimal x0 = new BigDecimal("0");
        BigDecimal x1 = new BigDecimal(Math.sqrt(A.doubleValue()));
        BigDecimal TWO = new BigDecimal(2);
        while (!x0.equals(x1)) {
            x0 = x1;
            x1 = A.divide(x0, RoundingMode.HALF_UP);
            x1 = x1.add(x0);
            x1 = x1.divide(TWO, RoundingMode.HALF_UP);
        }
        return x1;
    }

}