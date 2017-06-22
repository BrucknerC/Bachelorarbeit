package gravitysandbox.gui;

import com.jogamp.opengl.util.FPSAnimator;
import gravitysandbox.physics.Body;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * The main window for the simulation.
 *
 * @version 1.1
 * @since 0.1
 */
public class MainFrame extends JFrame {

    /**
     * A flag showing whether the simulation is running or not.
     */
    private boolean simualtionRunnig;

    /**
     * The {@link GravityCanvas} in which the simulation will be rendered.
     */
    private GravityCanvas gravityCanvas;

    /**
     * The {@link FPSAnimator} used for continuously redrawing the {@link GravityCanvas}.
     */
    private FPSAnimator animator;

    /**
     * The {@link ToolPanel} where operations on the simulation can be performed.
     */
    private ToolPanel toolPanel;

    /**
     * The {@link MainMenuBar} displaying the menu bar.
     */
    private MainMenuBar menuBar;

    /**
     * Creates a new MainFrame and displays it.
     */
    public MainFrame() {

        menuBar = new MainMenuBar(this);

        setJMenuBar(menuBar);

        setLayout(new BorderLayout());

        gravityCanvas = new GravityCanvas();
        gravityCanvas.addGLEventListener(gravityCanvas);
        add(gravityCanvas, BorderLayout.CENTER);

        animator = new FPSAnimator(gravityCanvas, 30);
        animator.setUpdateFPSFrames(60, System.out);
        animator.start();

        toolPanel = new ToolPanel(this);

        add(toolPanel, BorderLayout.NORTH);

        setSize(700, 500);
        setTitle("Gravity sandbox");
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                close();
            }
        });
        setVisible(true);

        gravityCanvas.requestFocus();

    }

    /**
     * Stops the simulation if running. Otherwise it will be.
     */
    void toogleSimulation() {
        gravityCanvas.setAnimationRunning(!simualtionRunnig);

        toolPanel.updateStartButton();
        menuBar.updateMenuItems();

        simualtionRunnig = !simualtionRunnig;
    }

    /**
     * Update the MainFrame controls.
     */
    public void update() {
        gravityCanvas.repaint();
        toolPanel.updateComboBox();
    }

    /**
     * Reset the camera of the {@link GravityCanvas} to default values.
     */
    void resetView() {
        gravityCanvas.resetView();
    }

    /**
     * Getter for simulationRunning flag.
     *
     * @return true if the simulation is running. false otherwise
     */
    boolean isSimualtionRunnig() {
        return simualtionRunnig;
    }

    /**
     * Closes this MainFrame.
     */
    void close() {
        animator.stop();
        dispose();
    }

    /**
     * Returns the currently selected {@link Body} from the {@link ToolPanel}.
     * @return the currently selected {@link Body}.
     */
    Body getSelectedBody() {
        return toolPanel.getSelectedBody();
    }
}