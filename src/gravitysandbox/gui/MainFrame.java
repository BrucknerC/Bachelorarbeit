package gravitysandbox.gui;

import com.jogamp.opengl.util.FPSAnimator;
import gravitysandbox.physics.BodyConainer;

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

    private GravityCanvas gravityCanvas;
    private FPSAnimator animator;
    private JButton btnSimulate, btnDetails;

    public MainFrame() {

        setLayout(new BorderLayout());

        gravityCanvas = new GravityCanvas();
        gravityCanvas.addGLEventListener(gravityCanvas);
        add(gravityCanvas, BorderLayout.CENTER);

        animator = new FPSAnimator(gravityCanvas, 30);
        animator.setUpdateFPSFrames(60, System.out);

        JPanel toolPanel = new JPanel();
        toolPanel.setLayout(new BoxLayout(toolPanel,BoxLayout.Y_AXIS));

        btnSimulate = new JButton("Start");
        btnSimulate.addActionListener(e -> btnSimulatePressed());

        toolPanel.add(btnSimulate);

        btnDetails = new JButton("Details");
        btnDetails.addActionListener(e -> new DetailDialog(BodyConainer.getInstance().get(0)));

        toolPanel.add(btnDetails);

        add(toolPanel, BorderLayout.EAST);

        setSize(700, 500);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                animator.stop();
                dispose();
            }
        });
        setVisible(true);

        btnSimulatePressed();

        gravityCanvas.requestFocus();

    }

    private void btnSimulatePressed() {
        if (btnSimulate.getText().equals("Start")) {
            btnSimulate.setText("Stopp");
            animator.start();
        } else {
            btnSimulate.setText("Start");
            animator.stop();
        }
    }

}