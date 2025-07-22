package com.mvsim.ui.desktop;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;

/**
 * Represents a GUI application for the Mechanical Ventilator simulator app;
 * sets up the GUI's JFrame and adds the UI components to it
 */
public class MvSimulatorAppGUI extends JFrame implements WindowListener {
    private static final String IMG_PATH = "mv-simulator/src/main/java/com/mvsim/data/splash.jpeg";

    /**
     * Create the GUI and show it. For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    public MvSimulatorAppGUI() {
        setTitle("Mechanical Ventilation Simulator");
        addWindowListener(this);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        // Use GridBagLayout for proportional sizing
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Create panels
        JPanel menuPanel = new MenuPanel();
        JPanel infoPanel = new InfoPanel();
        JPanel waveformsPanel = new WaveformPanels();
        JPanel metricsPanel = new MetricsPanel();
        JPanel settingsPanel = new SettingsPanel();
        
        // Common constraints
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(2, 2, 2, 2); // Add a small gap between components
        
        // InfoPanel: 90% width, 10% height at the top
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Spans menu + metrics columns
        gbc.gridheight = 1;
        gbc.weightx = 0.9;
        gbc.weighty = 0.1;
        add(infoPanel, gbc);
        
        // MenuPanel: 10% width, 80% height on the left
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 2; // spans info and waveforms rows
        gbc.weightx = 0.1; // This ensures it stays at 10% width
        gbc.weighty = 0.9;
        add(menuPanel, gbc);
        
        // WaveformArea: 60% width, 80% height in the center
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.6;
        gbc.weighty = 0.8;
        add(waveformsPanel, gbc);
        
        // MetricsPanel: 30% width, 80% height on the right
        gbc.gridx = 2;
        gbc.gridy = 1; // Start at the top
        gbc.gridwidth = 1;
        gbc.gridheight = 1; // Spans both info and waveform rows
        gbc.weightx = 0.3;
        gbc.weighty = 0.8; // Takes up info + waveform height (0.1 + 0.8)
        add(metricsPanel, gbc);
        
        // SettingsPanel: 100% width, 10% height at the bottom
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 3; // Spans all columns
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.1;
        add(settingsPanel, gbc);
        
        // Maximize the window to fill the screen
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        // Get screen size for splash image
        Dimension screenSize = getToolkit().getScreenSize();
        // displaySplashImage(screenSize); XXX
        
        // Make sure the window is centered
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Displays a splash image for 3s
     */
    private void displaySplashImage(Dimension size) {
        JWindow splashScreen = new JWindow();
        JLabel label = new JLabel(new ImageIcon(IMG_PATH));
        splashScreen.setSize(size);
        splashScreen.add(label);
        splashScreen.setLocationRelativeTo(null);
        splashScreen.setVisible(true);
        splashScreen.paintAll(splashScreen.getGraphics());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        splashScreen.setVisible(false);
        splashScreen.dispose();
    }

    @Override
    public void windowOpened(WindowEvent e) {
        return;
    }

    @Override
    public void windowClosing(WindowEvent e) {
        dispose();
    }

    @Override
    public void windowClosed(WindowEvent e) {
        System.exit(0);
    }

    @Override
    public void windowIconified(WindowEvent e) {
        return;
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        return;
    }

    @Override
    public void windowActivated(WindowEvent e) {
        return;
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        return;
    }
}
