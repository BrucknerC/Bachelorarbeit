package gravitysandbox.gui;

import com.jogamp.opengl.util.FPSAnimator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainFrame extends JFrame {

    private GravityCanvas gravityCanvas;
    private FPSAnimator animator;
    private JButton btnSimulate;

    public MainFrame() {

        setLayout(new BorderLayout());

        gravityCanvas = new GravityCanvas();
        gravityCanvas.addGLEventListener(gravityCanvas);
        //gravityCanvas.setSize(700,500);
        add(gravityCanvas, BorderLayout.CENTER);

        animator = new FPSAnimator(gravityCanvas, 30);
        animator.setUpdateFPSFrames(60, System.out);

        JPanel toolPanel = new JPanel();

        btnSimulate = new JButton("Start");
        btnSimulate.addActionListener(e -> btnSimulatePressed());

        toolPanel.add(btnSimulate);

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