package com.uwecocar2.vehiclesimulation.ui;
import java.awt.BorderLayout;

import java.awt.Color;

import javax.swing.JFrame;


import org.jfree.chart.*;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.uwecocar2.vehiclesimulation.Recorder;

/**
 * 
 */
public class DynamicGraph extends JFrame {
    private XYSeries added = new XYSeries("Data");

    /**
     * Instantiates a new DynamicGraph
     * @param s the name of the graph
     */
    public DynamicGraph(String s) {
        super(s);
        final ChartPanel chartPanel = createPanel();
        this.add(chartPanel, BorderLayout.CENTER);
    }
    
    /**
     * Sets the data stored and displayed in the graph
     * @param records the Recorder to set as data
     * @param name 
     */
    public void setData(Recorder records) {
    	added.clear();
    	int index = 0;
    	for (double item : records) {
    		added.add(index, item);
    		index++;
    	}
    }

    private ChartPanel createPanel() {
    	XYSeriesCollection xySeriesCollection = new XYSeriesCollection();
        xySeriesCollection.addSeries(added);
        JFreeChart jfreechart = ChartFactory.createScatterPlot(
            "Drive Cycle", "Time", "", xySeriesCollection,
            PlotOrientation.VERTICAL, true, true, false);
        XYPlot xyPlot = (XYPlot) jfreechart.getPlot();
        xyPlot.setDomainCrosshairVisible(true);
        xyPlot.setRangeCrosshairVisible(true);
        XYItemRenderer renderer = xyPlot.getRenderer();
        renderer.setSeriesPaint(0, Color.blue);
        NumberAxis domain = (NumberAxis) xyPlot.getDomainAxis();
        domain.setVerticalTickLabels(true);
        return new ChartPanel(jfreechart);
    }
}