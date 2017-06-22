package gravitysandbox.util;

import java.math.BigDecimal;
import java.math.MathContext;

import static java.math.RoundingMode.HALF_EVEN;
import static java.math.RoundingMode.HALF_UP;

/**
 * This class contains mathematical functions for {@link BigDecimal}.
 *
 * @author Christoph Bruckner
 * @version 1.1
 * @since 0.2
 */
public class BigDecimalMath {

    /**
     * Calculate the approximate square root of a {@link BigDecimal}.
     * @param number The {@link BigDecimal} from which the square root shall be calculated.
     * @return The approximate square root of number.
     */
    public static BigDecimal sqrt(BigDecimal number) {
        return new BigDecimal(Math.sqrt(number.doubleValue()));
    }

    /**
     * Determine the maximum of a given array of {@link BigDecimal}.
     * @param elems The array of {@link BigDecimal}.
     * @return The maximum.
     */
    public static BigDecimal max(BigDecimal... elems) {
        BigDecimal tmpmax = elems[0];
        for (int i = 1; i < elems.length; i++) {
            tmpmax = elems[i].compareTo(tmpmax) > 0 ? elems[i] : tmpmax;
        }
        return tmpmax;
    }

    /**
     * Determine the maximum scale of a given array of {@link BigDecimal}.
     * @param elems The array of {@link BigDecimal}.
     * @return The maximum scale.
     */
    public static int minScale(BigDecimal... elems) {
        int tmpmin = elems[0].scale();
        for (int i = 1; i < elems.length; i++) {
            tmpmin = elems[i].scale() < tmpmin ? elems[i].scale() : tmpmin;
        }
        return tmpmin;
    }

    /**
     * Determine the minimum of a given array of {@link BigDecimal}.
     * @param elems The array of {@link BigDecimal}.
     * @return The minimum.
     */
    public static BigDecimal min(BigDecimal... elems) {
        BigDecimal tmpmax = elems[0];
        for (int i = 1; i < elems.length; i++) {
            tmpmax = elems[i].compareTo(tmpmax) < 0 ? elems[i] : tmpmax;
        }
        return tmpmax;
    }

}
