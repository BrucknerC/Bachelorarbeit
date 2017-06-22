package gravitysandbox.physics;

import gravitysandbox.util.Vector3D;

import java.math.BigDecimal;

import static java.math.RoundingMode.HALF_UP;

/**
 * Contains constants and functions for calculating physical formulas.
 *
 * @author Christoph Bruckner
 * @version 1.2
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
     * Source for value: http://www.iau.org/static/resolutions/IAU2012_English.pdf, page 3 (accessed 2017-02-28)
     */
    public static final BigDecimal AU = new BigDecimal(149_597_870_700L);

    /**
     * Calculates the gravitational acceleration for one Body.
     * <p>
     * Using Newton's law of universal gravitation (F=G*m1*m2/(r^2)) the gravitational force between two bodies is calculated.
     * With r being the distance between the two bodies, m1 and m2 being the masses of the bodies and G being the universal gravitational constant.
     * <p>
     * The acceleration is calculated with a fourth order Runge-Kutta method to improve the accuracy.
     *
     * @param body1     The body for which the acceleration is calculated.
     * @param body2 The speed of the simulation.
     * @return The gravitational acceleration represented as a {@link Vector3D}.
     */
    public static Vector3D calculateGravitationalForce(Body body1, Body body2) {

        BigDecimal tmp, cubedDistance;
        cubedDistance = ((body2.getPosition().subtract(body1.getPosition())).length()).pow(3);
        tmp = G.multiply(body1.getMass()).multiply(body2.getMass());
        tmp = tmp.divide(cubedDistance, cubedDistance.scale() - tmp.scale(), HALF_UP);
        return(body2.getPosition().subtract(body1.getPosition())).scale(tmp);
    }
}