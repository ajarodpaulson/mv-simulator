package com.mvsim.ui.desktop;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.function.Function;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.mvsim.model.observer.SimMgrObserver;
import com.mvsim.model.ventilator.MostRecentTickData;

public abstract class WaveformPanel extends JPanel implements SimMgrObserver {
     private final XYSeries series;
     private final Function<MostRecentTickData, Float> getSystemScalarMetric;

    WaveformPanel(String scalar, String unitNotation, Color color,
    Function<MostRecentTickData, Float> getSystemScalarMetric) {
        setLayout(new BorderLayout());
        this.getSystemScalarMetric = getSystemScalarMetric;
        series = new XYSeries(scalar);
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);
        JFreeChart chart = ChartFactory.createXYLineChart(scalar, null, unitNotation, dataset);

        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.BLACK);
        plot.setDomainGridlinesVisible(false);
        plot.setRangeGridlinesVisible(false);

        NumberAxis domain = (NumberAxis) plot.getDomainAxis();
        domain.setAutoRange(true);
        NumberAxis range = (NumberAxis) plot.getRangeAxis();
        range.setAutoRange(true);

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(true, false);
        renderer.setSeriesPaint(0, color);
        plot.setRenderer(renderer);

        ChartPanel chartPanel = new ChartPanel(chart);
        add(chartPanel, BorderLayout.CENTER);
    }

   @Override
    public void update(MostRecentTickData data) {
        series.add(data.getCurrentSystemTime(), getSystemScalarMetric.apply(data));
    }
}
