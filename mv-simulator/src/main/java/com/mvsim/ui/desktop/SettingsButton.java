package com.mvsim.ui.desktop;

import java.awt.BorderLayout;
import java.awt.Container;
import java.util.function.Consumer;

import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SettingsButton {
    SettingsButton(Container c, Consumer<JSpinner> consumer, String label, float value, float min, float max, float stepSize) {
        
        // Create the label and center it
        JLabel l = new JLabel(label, SwingConstants.CENTER);
        
        // Create the spinner
        SpinnerNumberModel model = new SpinnerNumberModel(value, min, max, stepSize);
        JSpinner spinner = new JSpinner(model);
        spinner.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                consumer.accept(spinner);
            }
            
        });
        l.setLabelFor(spinner);
        
        c.add(l, BorderLayout.NORTH);      // Label on top
        c.add(spinner, BorderLayout.CENTER); // Spinner below
    }
}
