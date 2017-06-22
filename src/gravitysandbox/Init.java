package gravitysandbox;

import gravitysandbox.gui.MainFrame;
import gravitysandbox.physics.BodyContainer;
import gravitysandbox.util.XMLEngine;

import javax.swing.*;
import java.io.IOException;

/**
 * @version 0.2
 * @since 0.1
 */
public class Init {

    public static void main(String[] args) {
        try {
            // Set System L&F
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        }
        catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            // handle exception
        }

        try {
            XMLEngine.load("sol.xml");
        } catch (IOException e) {
            BodyContainer.getInstance().clear();
            JOptionPane.showMessageDialog(null,
                    "Could not load system \"Sol\" from file \"sol.xml\"!\r\nError message: " + e.getMessage(),
                    "Error loading system",
                    JOptionPane.ERROR_MESSAGE);
        }

        new MainFrame();
    }

}