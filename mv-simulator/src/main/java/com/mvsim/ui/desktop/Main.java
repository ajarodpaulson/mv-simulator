package com.mvsim.ui.desktop;

/**
 * Represents the launch point for the application
 */

public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MvSimulatorAppGUI();
            }
        });
    }
}