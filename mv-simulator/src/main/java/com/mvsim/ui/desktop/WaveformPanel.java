package com.mvsim.ui.desktop;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.util.function.Function;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.mvsim.model.SimulationManager;
import com.mvsim.model.observer.SimMgrObserver;
import com.mvsim.model.ventilator.metrics.Metrics;

public abstract class WaveformPanel extends JPanel implements SimMgrObserver {
    private final XYSeries series;
    private final Function<Metrics, Float> getSystemScalarMetric;
    private static final double TIME_WINDOW_SECONDS = 10.0; // Display 10 seconds of data

    private double lastSystemTime = -1.0;
    private double displayTime = 0.0;

    WaveformPanel(String scalar, String unitNotation, Color color,
            Function<Metrics, Float> getSystemScalarMetric) {
        setLayout(new BorderLayout());
        this.getSystemScalarMetric = getSystemScalarMetric;

        // Create a series that does not auto-sort, which is best for time-series
        series = new XYSeries(scalar, false, true);
        XYSeriesCollection dataset = new XYSeriesCollection(series);

        JFreeChart chart = ChartFactory.createXYLineChart(
                scalar, null, unitNotation, dataset,
                PlotOrientation.VERTICAL, false, false, false);

        // --- Chart Styling ---
        chart.setBackgroundPaint(Color.BLACK);
        chart.getTitle().setPaint(Color.LIGHT_GRAY);

        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.BLACK);
        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(new Color(40, 40, 40));
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(new Color(40, 40, 40));
        plot.setOutlinePaint(null);

        // --- Axis Styling ---
        NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
        domainAxis.setFixedAutoRange(TIME_WINDOW_SECONDS); // This makes the chart scroll
        domainAxis.setTickLabelsVisible(false);
        domainAxis.setLabel(null);

        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setAutoRange(true);
        rangeAxis.setTickLabelPaint(Color.LIGHT_GRAY);
        rangeAxis.setLabelPaint(Color.LIGHT_GRAY);

        // --- Renderer Styling ---
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, color);
        renderer.setSeriesStroke(0, new BasicStroke(2.0f));
        renderer.setSeriesShapesVisible(0, false); // Hide markers for a clean line

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBorder(null);
        add(chartPanel, BorderLayout.CENTER);
    }

    @Override
    public void update(Metrics metrics) {
        double currentSystemTime = metrics.getCurrentSystemTime();

        // Detect if simulation was restarted by checking for a large time gap
        if (lastSystemTime > 0 && (currentSystemTime - lastSystemTime) > 1.0) {
            clearSeries(); // Reset the chart to start fresh
        }
        lastSystemTime = currentSystemTime;

        // Use a fixed time increment for a smooth scroll, based on the simulation tick rate
        double timeIncrement = SimulationManager.getInstance().getVtrController().getTickPeriodInMs() / 1000.0;
        displayTime += timeIncrement;

        float value = getSystemScalarMetric.apply(metrics);
        series.add(displayTime, value);

        // Remove old data points from the series to prevent memory leaks over time.
        // The visual scrolling is handled by the axis's fixed auto-range.
        while (series.getItemCount() > 0 && series.getX(0).doubleValue() < (displayTime - TIME_WINDOW_SECONDS)) {
            series.remove(0);
        }
    }

    /**
     * Clears the waveform data and resets the internal clock.
     */
    public void clearSeries() {
        series.clear();
        displayTime = 0.0;
    }
}
