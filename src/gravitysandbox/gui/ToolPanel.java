package gravitysandbox.gui;

import gravitysandbox.physics.Body;
import gravitysandbox.physics.BodyContainer;

import javax.swing.*;
import java.awt.*;

/**
 * TODO: Documentation
 *
 * @author Christoph Bruckner
 */
public class ToolPanel extends JPanel {

    MainFrame parent;
    private JButton btnSimulate, btnDetails;
    private JComboBox<Body> cmbxBodies;
    private final ImageIcon playIcon;
    private final ImageIcon pauseIcon;

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

    private void btnSimulatePressed() {
        updateStartButton();
        parent.toogleSimulation();
    }

    void updateStartButton() {
        if(parent.isSimualtionRunnig())
            btnSimulate.setIcon(playIcon);
        else
            btnSimulate.setIcon(pauseIcon);

    }
}
