package gravitysandbox.gui;

import gravitysandbox.physics.Body;
import gravitysandbox.physics.BodyContainer;

import javax.swing.*;
import java.awt.*;

/**
 * The tool panel with components for easy access to basic functions
 *
 * @author Christoph Bruckner
 * @version 1.0
 * @since 1.0
 */
public class ToolPanel extends JPanel {

    /**
     * The parent window in which the panel is located.
     */
    MainFrame parent;

    /**
     * The button for toggling the simulation.
     */
    private JButton btnSimulate;

    /**
     * The button for showing the {@link DetailDialog}.
     */
    private JButton btnDetails;

    /**
     * The {@link JComboBox} for selecting the {@link Body} to display details.
     */
    private JComboBox<Body> cmbxBodies;

    /**
     * The icon of btnSimulate when the simulation is stopped.
     */
    private final ImageIcon playIcon;

    /**
     * The icon of btnSimulate when the simulation is running.
     */
    private final ImageIcon pauseIcon;

    /**
     * Creates a new ToolPanel.
     * @param parent The parent window.
     */
    public ToolPanel(MainFrame parent) {
        this.parent = parent;
        setLayout(new BoxLayout(this,BoxLayout.X_AXIS));

        add(Box.createHorizontalGlue());

        playIcon = new ImageIcon("res/play.png");
        pauseIcon = new ImageIcon("res/pause.png");

        btnSimulate = new JButton();
        btnSimulate.setIcon(playIcon);
        btnSimulate.addActionListener(e -> btnSimulatePressed());

        add(btnSimulate);

        BodyContainer bodies = BodyContainer.getInstance();
        cmbxBodies = new JComboBox<>();

        bodies.forEach(b->cmbxBodies.addItem(b));

        add(cmbxBodies);

        btnDetails = new JButton("Details");
        btnDetails.addActionListener(e -> new DetailDialog(parent, (Body)cmbxBodies.getSelectedItem()));

        add(btnDetails);

        add(Box.createHorizontalGlue());

        setSize(cmbxBodies.getWidth(), getHeight());
    }

    /**
     * The action which will be performed, when btnSimulate is pressed.
     */
    private void btnSimulatePressed() {
        updateStartButton();
        parent.toogleSimulation();
    }

    /**
     * Updates the icon of btnSimulate.
     */
    void updateStartButton() {
        if(parent.isSimualtionRunnig())
            btnSimulate.setIcon(playIcon);
        else
            btnSimulate.setIcon(pauseIcon);

    }

    /**
     * Updates cmbxBodies with the current bodies in {@link BodyContainer}.
     */
    void updateComboBox() {
        Object selectedBody = cmbxBodies.getSelectedItem();

        cmbxBodies.removeAllItems();
        BodyContainer.getInstance().forEach(b->cmbxBodies.addItem(b));

        cmbxBodies.setSelectedItem(selectedBody);
    }

    /**
     * Retrieves the currently selected {@link Body} of cmbxBodies.
     * @return The currently selected {@link Body}.
     */
    Body getSelectedBody() {
        return (Body)cmbxBodies.getSelectedItem();
    }
}
