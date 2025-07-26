package com.mvsim.ui.desktop;

import javax.swing.*;
import com.mvsim.model.SimulationManager;
import com.mvsim.model.observer.SimMgrObserver;
import com.mvsim.model.ventilator.metrics.Metric;
import com.mvsim.model.ventilator.metrics.MetricName;
import com.mvsim.model.ventilator.metrics.Metrics;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class MetricsPanel extends JPanel implements SimMgrObserver {
    private Map<MetricName, JLabel> metricLabelMap;
    private DecimalFormat formatter = new DecimalFormat("0.0"); // Format with one decimal place

    public MetricsPanel() {
        setLayout(new GridLayout(0, 2, 6, 6));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(Color.DARK_GRAY);

        metricLabelMap = new HashMap<>();

        // Create initial labels
        for (Entry<MetricName, Metric> metric : SimulationManager.getInstance().getVtrController().getMetrics()
                .getAllMetrics()) {
            // Format to one decimal place
            String initialValue = formatter.format(metric.getValue().getValue()) + " "
                    + metric.getValue().getUnits().getNotation();
            JLabel valueLabel = addMetric(metric.getKey().getDisplayName(), initialValue);
            metricLabelMap.put(metric.getKey(), valueLabel);
        }

        // Register as observer to receive updates
        SimulationManager.getInstance().addObserver(this);
    }

    private JLabel addMetric(String name, String value) {
        JLabel lblName = new JLabel(name + ":");
        lblName.setForeground(Color.WHITE);
        lblName.setFont(lblName.getFont().deriveFont(14f));

        JLabel lblVal = new JLabel(value);
        lblVal.setForeground(Color.GREEN.brighter());
        lblVal.setFont(new Font("Monospaced", Font.BOLD, 16)); // Use monospaced font
        
        // Set fixed size for value label to prevent panel resizing
        Dimension labelSize = new Dimension(100, 20);
        lblVal.setPreferredSize(labelSize);
        lblVal.setMinimumSize(labelSize);
        lblVal.setMaximumSize(labelSize);
        
        // Right-align text for consistent appearance
        lblVal.setHorizontalAlignment(SwingConstants.RIGHT);

        add(lblName);
        add(lblVal);

        return lblVal;
    }

    @Override
    public void update(Metrics metrics) {
        // Update all metric values when new data arrives
        for (Entry<MetricName, Metric> metric : metrics.getAllMetrics()) {
            JLabel valueLabel = metricLabelMap.get(metric.getKey());
            
            if (valueLabel != null) {
                // Format to one decimal place
                String newValue = formatter.format(metric.getValue().getValue()) + " "
                        + metric.getValue().getUnits().getNotation();
                valueLabel.setText(newValue);
            }
        }
    }
}