package gravitysandbox;

import gravitysandbox.gui.MainFrame;
import gravitysandbox.physics.Body;
import gravitysandbox.physics.BodyConainer;
import gravitysandbox.physics.Physics;
import gravitysandbox.util.Vector3D;

import javax.swing.*;
import java.math.BigDecimal;

/**
 * @version 0.2
 * @since 0.1
 */
public class Init {

    public static void main(String[] args) {
        try {
            // Set System L&F
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        }
        catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            // handle exception
        }

        BodyConainer bodyConainer = BodyConainer.getInstance();
        // Erde startet am Perihel
        bodyConainer.add(new Body("Erde",
                new Vector3D(Physics.AU.multiply(new BigDecimal("0.983")), new BigDecimal(0), new BigDecimal(0)),
                new Vector3D(new BigDecimal(0), new BigDecimal("30.3E3"), new BigDecimal(0)),
                new BigDecimal("5.972E25")), bodyConainer.size());

        // Sonne in der Mitte
        bodyConainer.add(new Body("Sonne",
                new Vector3D(),
                new Vector3D(),
                new BigDecimal("1.9884E30")), bodyConainer.size());

        // Jupiter am Aphel
        bodyConainer.add(new Body("Jupiter",
                new Vector3D(Physics.AU.multiply(new BigDecimal("-5.37")), new BigDecimal(0), new BigDecimal(0)),
                new Vector3D(new BigDecimal("6.07E3"), new BigDecimal("-13.07E3"), new BigDecimal(0)),
                new BigDecimal("1.899E27")), bodyConainer.size());

        new MainFrame();
    }

}