package gravitysandbox.gui;

import gravitysandbox.physics.Body;
import gravitysandbox.physics.BodyContainer;
import gravitysandbox.util.Vector3D;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

//TODO:Documentation

/**
 * A Dialog used for adding new bodies to the system
 *
 * @author Christoph Bruckner
 * @version 1.0
 * @since 1.0
 */
class AddBodyDialog extends JDialog {

    private JTextField txtName;
    private JTextField txtMass;
    private JTextField txtPositionX;
    private JTextField txtPositionY;
    private JTextField txtPositionZ;
    private JTextField txtVelocityX;
    private JTextField txtVelocityY;
    private JTextField txtVelocityZ;
    private JCheckBox chbxIsStar;

    /**
     * Constructor for
     * @param parent
     */
    AddBodyDialog(MainFrame parent) {
        super(parent, "Add new body", true);
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

        JButton btnAdd, btnCancel;

        btnAdd = new JButton("Add");
        btnAdd.addActionListener(e -> addBody());
        add(btnAdd);

        btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(e -> dispose());
        add(btnCancel);

        pack();

        setLocation(getOwner().getLocation().x + (getOwner().getSize().width - getSize().width) / 2,
                getOwner().getLocation().y + (getOwner().getSize().height - getSize().height) / 2);
        setVisible(true);
    }

    private void addBody() throws IllegalArgumentException {
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
            return;
        }

        BodyContainer.getInstance().add(new Body(txtName.getText(),
                        new Vector3D(new BigDecimal(txtPositionX.getText()),
                                new BigDecimal(txtPositionY.getText()),
                                new BigDecimal(txtPositionZ.getText())),
                        new Vector3D(new BigDecimal(txtVelocityX.getText()),
                                new BigDecimal(txtVelocityY.getText()),
                                new BigDecimal(txtVelocityZ.getText())),
                        new BigDecimal(txtMass.getText()),
                        chbxIsStar.isSelected()
                )
        );

        ((MainFrame) getOwner()).update();
        dispose();
    }
}
