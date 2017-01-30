package gravitysandbox;

import gravitysandbox.gui.MainFrame;
import gravitysandbox.physics.Body;
import gravitysandbox.physics.BodyConainer;
import gravitysandbox.physics.Physics;
import gravitysandbox.util.Vector3D;
import org.nevec.rjm.BigDecimalMath;

import javax.swing.*;
import java.math.BigDecimal;

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
                new Vector3D(Physics.AU.multiply(new BigDecimal(0.983)), new BigDecimal(0), new BigDecimal(0)),
                new Vector3D(new BigDecimal(0), new BigDecimal(30.3E3), new BigDecimal(0)),
                new BigDecimal("5.972E24")), bodyConainer.size());

        // Sonne in der Mitte
        bodyConainer.add(new Body("Sonne",
                new Vector3D(),
                new Vector3D(),
                new BigDecimal("1.9884E30")), bodyConainer.size());

        new MainFrame();
    }

}