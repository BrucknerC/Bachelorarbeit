package gravitysandbox.util;

import java.math.BigDecimal;
import java.math.MathContext;

import static java.math.RoundingMode.HALF_EVEN;
import static java.math.RoundingMode.HALF_UP;

//TODO: Documentation

/**
 * @author Christoph Bruckner
 * @version 1.1
 * @since 0.2
 */
public class BigDecimalMath {

    /**
     * Source: http://stackoverflow.com/questions/739532/logarithm-of-a-bigdecimal
     *
     * @param b
     * @param dp
     * @return
     */
    //http://everything2.com/index.pl?node_id=946812
    public static BigDecimal log10(BigDecimal b, int dp) {
        final int NUM_OF_DIGITS = dp + 2; // need to add one to get the right number of dp
        //  and then add one again to get the next number
        //  so I can round it correctly.

        MathContext mc = new MathContext(NUM_OF_DIGITS, HALF_UP);

        //special conditions:
        // log(-x) -> exception
        // log(1) == 0 exactly;
        // log of a number less than one = -log(1/x)
        if (b.signum() <= 0)
            throw new ArithmeticException("log of a negative number! (or zero)");
        else if (b.compareTo(BigDecimal.ONE) == 0)
            return BigDecimal.ZERO;
        else if (b.compareTo(BigDecimal.ONE) < 0)
            return (log10((BigDecimal.ONE).divide(b, mc), dp)).negate();

        StringBuffer sb = new StringBuffer();
        //number of digits on the left of the decimal point
        int leftDigits = b.precision() - b.scale();

        //so, the first digits of the log10 are:
        sb.append(leftDigits - 1).append(".");

        //this is the algorithm outlined in the webpage
        int n = 0;
        while (n < NUM_OF_DIGITS) {
            b = (b.movePointLeft(leftDigits - 1)).pow(10, mc);
            leftDigits = b.precision() - b.scale();
            sb.append(leftDigits - 1);
            n++;
        }

        BigDecimal ans = new BigDecimal(sb.toString());

        //Round the number to the correct number of decimal places.
        ans = ans.round(new MathContext(ans.precision() - ans.scale() + dp, HALF_UP));
        return ans;
    }

    /**
     * Source: http://stackoverflow.com/questions/13649703/square-root-of-bigdecimal-in-java
     *
     * @param A
     * @return
     */
    public static BigDecimal sqrt(BigDecimal A) {
        BigDecimal x0 = new BigDecimal("0");
        BigDecimal x1 = new BigDecimal(Math.sqrt(A.doubleValue()));
        BigDecimal TWO = new BigDecimal(2);
        while (!x0.equals(x1)) {
            x0 = x1;
            x1 = A.divide(x0, A.scale()-x0.scale(), HALF_UP);
            x1 = x1.add(x0);
            x1 = x1.divide(TWO, x1.scale()-TWO.scale(), HALF_UP);

        }
        return x1;
    }

    public static int maxScale(BigDecimal... elems) {
        int tmpmax = elems[0].scale();
        for (int i = 1; i < elems.length; i++) {
            tmpmax = elems[i].scale() > tmpmax ? elems[i].scale() : tmpmax;
        }
        return tmpmax;
    }

    public static BigDecimal max(BigDecimal... elems) {
        BigDecimal tmpmax = elems[0];
        for (int i = 1; i < elems.length; i++) {
            tmpmax = elems[i].compareTo(tmpmax) > 0 ? elems[i] : tmpmax;
        }
        return tmpmax;
    }

    public static int minScale(BigDecimal... elems) {
        int tmpmin = elems[0].scale();
        for (int i = 1; i < elems.length; i++) {
            tmpmin = elems[i].scale() < tmpmin ? elems[i].scale() : tmpmin;
        }
        return tmpmin;
    }

    public static BigDecimal min(BigDecimal... elems) {
        BigDecimal tmpmax = elems[0];
        for (int i = 1; i < elems.length; i++) {
            tmpmax = elems[i].compareTo(tmpmax) < 0 ? elems[i] : tmpmax;
        }
        return tmpmax;
    }

}
