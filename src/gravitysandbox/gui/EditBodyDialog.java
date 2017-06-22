package gravitysandbox.gui;

import gravitysandbox.physics.Body;
import gravitysandbox.util.Vector3D;

import java.math.BigDecimal;

/**
 * A dialog used for editing existing bodies.
 *
 * @author Christoph Bruckner
 * @version 1.0
 * @since 1.0
 */
public class EditBodyDialog extends BodyDialog {

    /**
     * The {@link Body} which will be edited.
     */
    private Body editingBody;

    /**
     * Creates a new dialog and displays it in the center of the parent window.
     *
     * @param parent The parent window.
     * @param body   The {@link Body} which will be edited.
     */
    public EditBodyDialog(MainFrame parent, Body body) {
        super(parent, "Edit body", "Update", body);
        editingBody = body;

        setVisible(true);
    }

    @Override
    protected void positiveButtonClicked() {
        if (parseInput()) {
            editingBody.setName(txtName.getText());
            editingBody.setMass(new BigDecimal(txtMass.getText()));
            editingBody.setPosition(new Vector3D(txtPositionX.getText(), txtPositionY.getText(), txtPositionZ.getText()));
            editingBody.setVelocity(new Vector3D(txtVelocityX.getText(), txtVelocityY.getText(), txtVelocityZ.getText()));
            editingBody.setStar(chbxIsStar.isSelected());

            ((MainFrame) getParent()).update();
            dispose();
        }
    }
}
