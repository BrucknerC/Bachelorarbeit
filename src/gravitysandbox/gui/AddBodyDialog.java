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
}
