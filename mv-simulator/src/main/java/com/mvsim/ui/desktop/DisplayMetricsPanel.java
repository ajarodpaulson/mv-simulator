package com.mvsim.ui.desktop;

import java.awt.Color;
import java.awt.Dimension;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.mvsim.model.observer.Observer;
import com.mvsim.model.ventilator.Ventilator;


/**
 * Represents a panel that displays the metrics of the currently selected lung
 * profile in the WorkingLungProfileList, or an informative message if no profile
 * currently selected
 */

public class DisplayMetricsPanel extends JPanel implements Observer {
    private JTextArea metrics;

    /**
     * EFFECTS: constructs a new display metrics panel with non-editable text area for 
     * displaying metrics from currently selected lung profile
     */
    public DisplayMetricsPanel() {
        super();
        this.setBackground(Color.WHITE);
        setPreferredSize(new Dimension(450, 600));
        metrics = new JTextArea();
        metrics.setEditable(false);
    }

    @Override
    public void update(Ventilator vtr) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }
}
