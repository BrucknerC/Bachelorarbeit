package gravitysandbox.gui;

import gravitysandbox.physics.Body;
import gravitysandbox.physics.BodyContainer;

import javax.swing.*;
import java.awt.*;

/**
 * A Dialog used for deleting {@link Body} objects from the {@link BodyContainer}.
 *
 * @author Christoph Bruckner
 * @version 1.0
 * @since 1.0
 */
public class RemoveBodyDialog extends JDialog {

    /**
     * The {@link JComboBox} for choosing which {@link Body} to delete.
     */
    JComboBox<Body> cmbxBodies;

    /**
     * Creates a new Dialog and displays it in the center of the parent window.
     *
     * @param parent The parent window.
     */
    public RemoveBodyDialog(MainFrame parent) {
        super(parent, "Remove body", true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        if (parent.isSimualtionRunnig())
            parent.toogleSimulation();

        setLayout(new GridLayout(0, 2));

        BodyContainer bodies = BodyContainer.getInstance();
        cmbxBodies = new JComboBox<>();

        bodies.forEach(b -> cmbxBodies.addItem(b));

        add(new JLabel("Select body to remove: "));
        add(cmbxBodies);

        JButton btnOK, btnCancel;

        btnOK = new JButton("Remove");
        btnOK.addActionListener(e -> {
            bodies.remove((Body) cmbxBodies.getSelectedItem());
            ((MainFrame) getOwner()).update();
            dispose();
        });
        add(btnOK);

        btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(e -> dispose());
        add(btnCancel);

        pack();
        setLocation(getOwner().getLocation().x + (getOwner().getSize().width - getSize().width) / 2,
                getOwner().getLocation().y + (getOwner().getSize().height - getSize().height) / 2);
        setVisible(true);
    }
}
