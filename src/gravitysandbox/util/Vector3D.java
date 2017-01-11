package gravitysandbox.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
// TODO: Documentation
public class Vector3D {

    private BigDecimal x;

    private BigDecimal y;

    private BigDecimal z;

    /**
     * Creates new instance of Vector3D with value of (1, 1, 1).
     */
    public Vector3D() {
        this.x = this.y = this.z = new BigDecimal(BigInteger.ONE);
    }

    public Vector3D(BigDecimal x, BigDecimal y, BigDecimal z) {
        setX(x);
        setY(y);
        setZ(z);
    }

    public Vector3D vectorProduct(Vector3D vector) {
        return new Vector3D(getY().multiply(vector.getZ()).subtract(getZ().multiply(getY())),
                getZ().multiply(vector.getX()).subtract(getX().multiply(getZ())),
                getX().multiply(vector.getY()).subtract(getY().multiply(getX())));
    }

    public Vector3D add(Vector3D vector) {
        return new Vector3D(getX().add(vector.getX()),
        getY().add(vector.getY()),
        getZ().add(vector.getZ()));
    }

    public Vector3D subtract(Vector3D vector) {
        return new Vector3D(getX().subtract(vector.getX()),
        getY().subtract(vector.getY()),
        getZ().subtract(vector.getZ()));
    }

    public BigDecimal scalarProduct(Vector3D vector) {
        return new BigDecimal(BigInteger.ZERO)
                .add(getX().multiply(vector.getX()))
                .add(getY().multiply(vector.getY()))
                .add(getZ().multiply(vector.getZ()));
    }

    public BigDecimal abs() {
        return sqrt(new BigDecimal(BigInteger.ZERO)
                .add(x.pow(2))
                .add(y.pow(2))
                .add(z.pow(2))
        );
    }

    public Vector3D unify() {
        Vector3D v = null;
        try {
            v = (Vector3D) this.clone();
            v.setX(getX().divide(abs(), RoundingMode.HALF_UP));
            v.setY(getY().divide(abs(), RoundingMode.HALF_UP));
            v.setZ(getZ().divide(abs(), RoundingMode.HALF_UP));
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return v;
    }

    public Vector3D scale(BigDecimal scalar) {
        return new Vector3D(getX().multiply(scalar),
                getY().multiply(scalar),
                getZ().multiply(scalar));
    }

    public void setX(BigDecimal x) {
        this.x = x;
    }

    public BigDecimal getX() {
        return x;
    }

    public void setY(BigDecimal y) {
        this.y = y;
    }

    public BigDecimal getY() {
        return y;
    }

    public void setZ(BigDecimal z) {
        this.z = z;
    }

    public BigDecimal getZ() {
        return z;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new Vector3D(this.x, this.y, this.z);
    }

    /**
     * Calculate the square root of the given {@link BigDecimal}.
     *
     * It uses the babylonian method (https://en.wikipedia.org/wiki/Methods_of_computing_square_roots#Babylonian_method) for calculating the square root.
     * Source: http://stackoverflow.com/a/19743026 (accessed 2016-01-11)
     * @author barwnikk
     * @param A The {@link BigDecimal} of which the square root will be calculated.
     * @return The square root of A.
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