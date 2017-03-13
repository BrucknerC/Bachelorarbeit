package gravitysandbox.physics;

import gravitysandbox.util.Vector3D;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

//TODO:Documentation

/**
 * Contains constants and functions for calculating physical formulas.
 *
 * @author Christoph Bruckner
 * @version 1.0
 * @since 0.1
 */
public class Physics {

    /**
     * The universal gravitational constant.
     * <p>
     * The representation of the universal gravitational constant (6.67408 * 10^-11	m^3 kg^-1 s^-2) as {@link BigDecimal}.<br/>
     * Source for value: http://physics.nist.gov/cgi-bin/cuu/Value?bg (accessed 2017-01-11)
     */
    public static final BigDecimal G = new BigDecimal("6.67408E-11");

    /**
     * One astronomical unit
     * <p>
     * The representation of the astronomical unit, which is roughly the distance from the Earth to the Sun.<br/>
     * One AU is defined as 149,597,870,700.0 m.<br/>
     * Source for value: http://www.iau.org/static/resolutions/IAU2012_English.pdf, page 1 (accessed 2017-02-28)
     */
    public static final BigDecimal AU = new BigDecimal(149_597_870_700L);

    /**
     * One parsec
     * <p>
     * The representation of the parsec, which is is the distance at which one astronomical unit subtends an angle of one arcsecond.<br/>
     * One pc is defined as 648000/PI * 1 AU.<br/>
     * Source for value: https://www.iau.org/public/themes/measuring/ (accessed 2017-02-28)
     */
    public static final BigDecimal pc = new BigDecimal("3.08567758149137E16");

    private static Vector3D gravAcceleration = new Vector3D();
    private static final BigDecimal BD_0_5 = new BigDecimal("0.5");
    private static final BigDecimal BD_2 = new BigDecimal(2);
    private static final BigDecimal BD_6 = new BigDecimal(6);

    /**
     * Calculates the gravitational acceleration for one Body.
     * <p>
     * Using Newton's law of universal gravitation (F=G*m1*m2/(r^2)) the gravitational force between two bodies is calculated.
     * With r being the distance between the two bodies, m1 and m2 being the masses of the bodies and G being the universal gravitational constant.
     * <p>
     * The acceleration is calculated with a fourth order Runge-Kutta method to improve the accuracy.
     *
     * @param body     The body for which the acceleration is calculated.
     * @param simSpeed The speed of the simulation.
     * @return The gravitational acceleration represented as a {@link Vector3D}.
     */
    public static Vector3D calculateGravitationalAcceleration(Body body, BigDecimal simSpeed) {
        gravAcceleration = gravAcceleration.scale(BigDecimal.ZERO);

        BigDecimal tmp, cubedDistance;
        int tmpScales[] = new int[12];
        Vector3D tmpLoc, tmpVel, tmpAccel;
        Vector3D rk1, rk2, rk3, rk4;

        for (Body externalBody : BodyConainer.getInstance()) {
            if (!body.equals(externalBody)) {
                cubedDistance = (externalBody.getPosition().subtract(body.getPosition()).length()).pow(3);
                tmp = G.multiply(externalBody.getMass());
                tmp = tmp.divide(cubedDistance, cubedDistance.scale() - tmp.scale(), RoundingMode.HALF_EVEN);

                rk1 = externalBody.getPosition().subtract(body.getPosition()).scale(tmp);
                tmpScales[0] = rk1.getX().scale();
                tmpScales[1] = rk1.getY().scale();
                tmpScales[2] = rk1.getZ().scale();

                tmpVel = partialCalculationStep(body.getVelocity(), rk1, BD_0_5);
                tmpLoc = partialCalculationStep(body.getPosition(), tmpVel, simSpeed.multiply(BD_0_5));
                rk2 = externalBody.getPosition().subtract(tmpLoc).scale(tmp);
                tmpScales[3] = rk2.getX().scale();
                tmpScales[4] = rk2.getY().scale();
                tmpScales[5] = rk2.getZ().scale();

                tmpVel = partialCalculationStep(body.getVelocity(), rk2, BD_0_5);
                tmpLoc = partialCalculationStep(body.getPosition(), tmpVel, simSpeed.multiply(BD_0_5));
                rk3 = externalBody.getPosition().subtract(tmpLoc).scale(tmp);
                tmpScales[6] = rk3.getX().scale();
                tmpScales[7] = rk3.getY().scale();
                tmpScales[8] = rk3.getZ().scale();

                tmpVel = partialCalculationStep(body.getVelocity(), rk3, BigDecimal.ONE);
                tmpLoc = partialCalculationStep(body.getPosition(), tmpVel, simSpeed);
                rk4 = externalBody.getPosition().subtract(tmpLoc).scale(tmp);
                tmpScales[9] = rk4.getX().scale();
                tmpScales[10] = rk4.getY().scale();
                tmpScales[11] = rk4.getZ().scale();

                tmpAccel = rk1
                        .add(rk2.scale(BD_2))
                        .add(rk3.scale(BD_2))
                        .add(rk4);

                Arrays.sort(tmpScales);

                tmpAccel = tmpAccel.scale(BigDecimal.ONE.divide(BD_6, tmpScales[tmpScales.length - 1], RoundingMode.HALF_EVEN)).stripTrailingZeros();
                gravAcceleration = gravAcceleration.add(tmpAccel);
            }
        }
        return gravAcceleration;
    }

    private static Vector3D partialCalculationStep(Vector3D point1, Vector3D point2, BigDecimal timeStep) {
        return new Vector3D(point1.getX().add(point2.getX().multiply(timeStep)),
                point1.getY().add(point2.getZ().multiply(timeStep)),
                point1.getZ().add(point2.getZ().multiply(timeStep))
        );
    }

}