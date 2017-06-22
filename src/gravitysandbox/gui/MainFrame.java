package gravitysandbox.gui;

import com.jogamp.opengl.util.FPSAnimator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

//TODO: Documentation

/**
 * @version 1.1
 * @since 0.1
 */
public class MainFrame extends JFrame {

    private boolean simualtionRunnig;
    private GravityCanvas gravityCanvas;
    private FPSAnimator animator;
    private ToolPanel toolPanel;
    private MainMenuBar menuBar;

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

    void toogleSimulation() {
        gravityCanvas.setAnimationRunning(!simualtionRunnig);

        toolPanel.updateStartButton();
        menuBar.updateMenuItems();

        simualtionRunnig = !simualtionRunnig;
    }

    public void update() {
        gravityCanvas.repaint();
    }

    void resetView() {
        gravityCanvas.resetView();
    }

    boolean isSimualtionRunnig() {
        return simualtionRunnig;
    }

    void close() {
        animator.stop();
        dispose();
    }
}