package gravitysandbox.gui;

import gravitysandbox.physics.Body;
import gravitysandbox.physics.BodyContainer;
import gravitysandbox.util.Vector3D;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

/**
 * A dialog used for adding new bodies to the system
 *
 * @author Christoph Bruckner
 * @version 1.0
 * @since 1.0
 */
class AddBodyDialog extends BodyDialog {

    /**
     * Creates a new dialog and displays it in the center of the parent window.
     *
     * @param parent The parent window.
     */
    AddBodyDialog(MainFrame parent) {
        super(parent, "Add new body", "Add");
        setVisible(true);
    }

    /**
     * Adds a new {@link Body} object to the {@link BodyContainer}.
     *
     * @throws IllegalArgumentException if there was an error adding the {@link Body}.
     */
    @Override
    protected void positiveButtonClicked() throws IllegalArgumentException {
        if (parseInput()) {
            boolean positionFlag = false;
            Vector3D position = new Vector3D(
                    new BigDecimal(txtPositionX.getText()),
                    new BigDecimal(txtPositionY.getText()),
                    new BigDecimal(txtPositionZ.getText()));
            for (Body body : BodyContainer.getInstance()) {
                positionFlag = positionFlag || (position.getX().compareTo(body.getPosition().getX()) == 0
                        && position.getY().compareTo(body.getPosition().getY()) == 0
                        && position.getZ().compareTo(body.getPosition().getZ()) == 0);


            }
            if (positionFlag) {
                JOptionPane.showMessageDialog(this,
                        "Two bodies can't be in the same position!",
                        "Error adding body",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                BodyContainer.getInstance().add(new Body(txtName.getText(),
                                position,
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
    }
}
