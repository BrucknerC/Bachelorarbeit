package gravitysandbox.physics;

import gravitysandbox.util.Vector3D;

import java.math.BigDecimal;

import static org.nevec.rjm.BigDecimalMath.*;

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
     * The representation of the universal gravitational constant (6.67408 * 10^-11) as {@link BigDecimal}.
     * Source for value: http://physics.nist.gov/cgi-bin/cuu/Value?bg (accessed 2016-01-11)
     */
    public static final BigDecimal G = new BigDecimal("6.67408E-11");

    public static final BigDecimal AU = new BigDecimal(149_597_870_700L);

    /**
     * Calculates the gravitational force between two bodies.
     * <p>
     * Using Newton's law of universal gravitation (F=G*m1*m2/(r^2)) the gravitational force between two bodies is calculated.
     * With r being the distance between the two bodies, m1 and m2 being the masses of the bodies and G being the universal gravitational constant.
     *
     * @param body1 The first body.
     * @param body2 The second body.
     * @return The gravitational force represented as a {@link Vector3D}.
     */
    public static Vector3D calculateGravity(Body body1, Body body2) {
        Vector3D distance = body2.getPosition().subtract(body1.getPosition());
        distance = distance.unify();
        return distance.scale(
                multiplyRound(G,
                        divideRound(
                                multiplyRound(
                                        body1.getMass(),
                                        body2.getMass()),
                                powRound(distance.length(), 2)
                        )
                )
        );
    }
}