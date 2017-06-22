package gravitysandbox.gui;

import gravitysandbox.physics.Body;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

/**
 * The base class for {@link AddBodyDialog} and {@link EditBodyDialog}.
 *
 * @author Christoph Bruckner
 * @version 1.0
 * @since 1.0
 */
public abstract class BodyDialog extends JDialog {

    /**
     * The {@link JTextField} for inserting the name.
     */
    JTextField txtName;

    /**
     * The {@link JTextField} for inserting the mass.
     */
    JTextField txtMass;

    /**
     * The {@link JTextField} for inserting the x coordinate of the position.
     */
    JTextField txtPositionX;

    /**
     * The {@link JTextField} for inserting the y coordinate of the position.
     */
    JTextField txtPositionY;

    /**
     * The {@link JTextField} for inserting the z coordinate of the position.
     */
    JTextField txtPositionZ;

    /**
     * The {@link JTextField} for inserting the x coordinate of the velocity.
     */
    JTextField txtVelocityX;

    /**
     * The {@link JTextField} for inserting the y coordinate of the velocity.
     */
    JTextField txtVelocityY;

    /**
     * The {@link JTextField} for inserting the z coordinate of the velocity.
     */
    JTextField txtVelocityZ;

    /**
     * The {@link JCheckBox} for indicating whether a {@link Body} emits light on its own.
     */
    JCheckBox chbxIsStar;

    /**
     * Creates a new dialog and displays it with the given title and text for the positive answer button
     * in the center of the parent window.
     *
     * @param parent The parent window.
     * @param title The title of the dialog.
     * @param positiveButtonText The text of the positive answer button.
     */
    BodyDialog(MainFrame parent, String title, String positiveButtonText) {
        super(parent, title, true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        if (parent.isSimualtionRunnig())
            parent.toogleSimulation();

        setLayout(new GridLayout(0, 2));

        txtName = new JTextField();
        add(new Label("Name:"));
        add(txtName);

        txtMass = new JTextField();
        add(new JLabel("Mass (in kg):"));
        add(txtMass);

        add(new JLabel("Position (in m):"));
        add(new JLabel());

        txtPositionX = new JTextField();
        add(new JLabel("     - X:"));
        add(txtPositionX);

        txtPositionY = new JTextField();
        add(new JLabel("     - Y:"));
        add(txtPositionY);

        txtPositionZ = new JTextField();
        add(new JLabel("     - Z:"));
        add(txtPositionZ);

        add(new JLabel("Velocity (in m/s):"));
        add(new JLabel());

        txtVelocityX = new JTextField();
        add(new JLabel("     - X:"));
        add(txtVelocityX);

        txtVelocityY = new JTextField();
        add(new JLabel("     - Y:"));
        add(txtVelocityY);

        txtVelocityZ = new JTextField();
        add(new JLabel("     - Z:"));
        add(txtVelocityZ);

        chbxIsStar = new JCheckBox();
        add(new JLabel("Is star:"));
        add(chbxIsStar);

        JButton positiveButton, btnCancel;

        positiveButton = new JButton(positiveButtonText);
        positiveButton.addActionListener(e -> positiveButtonClicked());
        add(positiveButton);

        btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(e -> dispose());
        add(btnCancel);

        pack();

        setLocation(getOwner().getLocation().x + (getOwner().getSize().width - getSize().width) / 2,
                getOwner().getLocation().y + (getOwner().getSize().height - getSize().height) / 2);
    }

    /**
     * Creates a new dialog and displays it with the given title and text for the positive answer button
     * in the center of the parent window and fills the text fields with initial values.
     *
     * @param parent The parent window.
     * @param title The title of the dialog.
     * @param positiveButtonText The text of the positive answer button.
     * @param initialValues The {@link Body} which values will fill the text fields.
     */
    BodyDialog(MainFrame parent, String title, String positiveButtonText, Body initialValues) {
        this(parent, title, positiveButtonText);
        txtName.setText(initialValues.getName());
        txtMass.setText((initialValues.getMass().toEngineeringString()));
        txtPositionX.setText(initialValues.getPosition().getX().toEngineeringString());
        txtPositionY.setText(initialValues.getPosition().getY().toEngineeringString());
        txtPositionZ.setText(initialValues.getPosition().getZ().toEngineeringString());
        txtVelocityX.setText(initialValues.getVelocity().getX().toEngineeringString());
        txtVelocityY.setText(initialValues.getVelocity().getY().toEngineeringString());
        txtVelocityZ.setText(initialValues.getVelocity().getZ().toEngineeringString());
        chbxIsStar.setSelected(initialValues.isStar());
    }

    /**
     * The action which will be performed, when the positive answer button will be pressed.
     */
    protected abstract void positiveButtonClicked();

    /**
     * Parses the values in the text fields and displays an error message if one or more value can't be parsed correctly.
     * @return true, if all text fields are in a valid format. false otherwise.
     */
    boolean parseInput() {
        BigDecimal temp;
        boolean[] errorFlags = new boolean[7];

        for (int i = 1; i < 7; i++) {
            try {
                switch (i % 3) {
                    case 1:
                        temp = new BigDecimal(i < 4 ? txtPositionX.getText() : txtVelocityX.getText());
                        break;
                    case 2:
                        temp = new BigDecimal(i < 4 ? txtPositionY.getText() : txtVelocityY.getText());
                        break;
                    case 0:
                        temp = new BigDecimal(i < 4 ? txtPositionZ.getText() : txtVelocityZ.getText());
                        break;
                }
            } catch (NumberFormatException e) {
                errorFlags[0] = errorFlags[i] = true;
            }
        }
        if (errorFlags[0]) {
            StringBuilder message = new StringBuilder("There was an error parsing your inputs for:");
            for (int i = 1; i < 7; i++) {
                if (errorFlags[i]) {
                    if (i < 4) {
                        message.append("\r\nPosition ");
                    } else {
                        message.append("\r\nVelocity ");
                    }
                    switch (i % 3) {
                        case 1:
                            message.append("X");
                            break;
                        case 2:
                            message.append("Y");
                            break;
                        case 0:
                            message.append("Z");
                            break;
                    }
                    message.append(" value");
                }
            }

            JOptionPane.showMessageDialog(this,
                    message.toString(),
                    "Input error",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }
}
