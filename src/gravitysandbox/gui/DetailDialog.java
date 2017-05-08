package gravitysandbox.gui;

import gravitysandbox.physics.Body;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Observable;
import java.util.Observer;

/**
 * A Dialog for monitoring one {@link Body}.
 *
 * The body's current name, mass, position and velocity will be displayed.
 * @author Christoph Bruckner
 * @version 1.0
 * @since 0.3
 */
public class DetailDialog extends JDialog implements Observer {

    private Body observedBody;
    private JLabel lblName, lblMass, lblPosition, lblVelocity;

    public DetailDialog(Body body) {
        super();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                observedBody.deleteObserver(DetailDialog.this);
                e.getWindow().dispose();
            }
        });

        body.addObserver(this);
        observedBody = body;

        setLayout(new GridLayout(0, 2));

        lblName = new JLabel(body.getName());
        add(new Label("Name:"));
        add(lblName);

        lblMass = new JLabel(body.getMass().toEngineeringString() + " kg");
        add(new JLabel("Mass:"));
        add(lblMass);

        lblPosition = new JLabel(body.getPosition().toEngineeringString() + " m");
        add(new JLabel("Position:"));
        add(lblPosition);

        lblVelocity = new JLabel(body.getVelocity().toEngineeringString() + " m/s");
        add(new JLabel("Velocity:"));
        add(lblVelocity);

        pack();
        setVisible(true);

    }

    @Override
    public void update(Observable o, Object arg) {
        lblName.setText(observedBody.getName());
        lblMass.setText(observedBody.getMass().toEngineeringString() + " kg");
        lblPosition.setText(observedBody.getPosition().toEngineeringString() + " m");
        lblVelocity.setText(observedBody.getVelocity().toEngineeringString() + " m/s");
    }

}
