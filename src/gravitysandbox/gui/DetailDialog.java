package gravitysandbox.gui;

import gravitysandbox.physics.Body;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Observable;
import java.util.Observer;

/**
 * A dialog for monitoring one {@link Body}.
 * <p>
 * The body's current name, mass, position and velocity will be displayed.
 *
 * @author Christoph Bruckner
 * @version 1.1
 * @since 0.3
 */
public class DetailDialog extends JDialog implements Observer {

    private Body observedBody;
    private JLabel lblName, lblMass, lblPosition, lblVelocity;

    /**
     * Creates a new dialog to show the current values for a given {@link Body} and displays it in the center of the parent window.
     *
     * @param parent The parent window.
     * @param body   The {@link Body} which will be observed.
     */
    public DetailDialog(MainFrame parent, Body body) {
        super(parent);
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
        setLocation(getOwner().getLocation().x + (getOwner().getSize().width - getSize().width) / 2,
                getOwner().getLocation().y + (getOwner().getSize().height - getSize().height) / 2);
        setVisible(true);

    }

    /**
     * Updates the all {@link JLabel} objects with the current values of the {@link Body}
     *
     * @param o   The observed {@link Body}. Unused.
     * @param arg Additional information. Unused.
     */
    @Override
    public void update(Observable o, Object arg) {
        if (arg != null && arg instanceof String && arg.equals("DELETE"))
            dispose();

        lblName.setText(observedBody.getName());
        lblMass.setText(observedBody.getMass().toEngineeringString() + " kg");
        lblPosition.setText(observedBody.getPosition().toEngineeringString() + " m");
        lblVelocity.setText(observedBody.getVelocity().toEngineeringString() + " m/s");
    }

}
