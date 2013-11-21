package com.uwecocar2.vehiclesimulation.ui;

import com.uwecocar2.vehiclesimulation.Recorder;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class JGraph extends JPanel {

	// the data to graph
	private Recorder data;
	private String yAxis;
	XYSeriesCollection dataCollection;
	
	// the graph padding
	private static final int OFFSET_X = 75;
	private static final int OFFSET_Y = 75;
	private static final int SIZE = 4;
	private static final int FONT_SIZE = 20;
	
	private static final DecimalFormat DF = new DecimalFormat("###.##");
	private static final Font FONT = new Font("UNICODE", Font.PLAIN, FONT_SIZE);
	private final FontMetrics METRICS;
	
	public JGraph() {
		super();
		this.setFont(FONT);
		METRICS = this.getFontMetrics(FONT);
		dataCollection = new XYSeriesCollection();
	}
	
	public JPanel getChart() {
		XYSeries series = new XYSeries("Efficiency");
		int index = 0;
		
		int width = this.getWidth() - OFFSET_X;
		int height = this.getHeight() - OFFSET_Y;
		
		double xMult;
        double yMult;
        try {
            xMult = ((double)width - OFFSET_X)/this.data.size();
            yMult = ((double)height - OFFSET_Y)/this.data.max();
        } catch (Exception e) {
            xMult = 0;
            yMult = 0;
        }
		
		
		for(Double value : this.data) {
			int x = (int)(index*xMult) + OFFSET_X;
			int y = (int)(value*yMult) - OFFSET_Y;
			series.add(x, y);
			index++;
		}
		dataCollection.addSeries(series);
		JFreeChart lineChartObject=ChartFactory.createXYLineChart("Efficiency", "Time", yAxis, dataCollection);
		//JFreeChart lineChartObject=ChartFactory.createLineChart("title","x","y", data,PlotOrientation.VERTICAL,true,true,false);
		ChartPanel chPanel = new ChartPanel(lineChartObject); //creating the chart panel, which extends JPanel
		
		chPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 10));
		chPanel.setLayout(new BoxLayout(chPanel, BoxLayout.Y_AXIS));
		
		//chPanel.setPreferredSize(new Dimension(400, 400)); //size according to my window
		chPanel.setMouseWheelEnabled(true);
		chPanel.setVisible(true);
		JPanel containingPanel = new JPanel();
		containingPanel.add(chPanel);
		return containingPanel;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		int width = this.getWidth() - OFFSET_X;
		int height = this.getHeight() - OFFSET_Y;
		
		g.clearRect(0, 0, this.getWidth(), this.getHeight());
		
		g.setColor(Color.BLACK);
		// draw the x and y axis
		g.drawLine(OFFSET_X, OFFSET_Y, OFFSET_X, height);
		g.drawLine(OFFSET_X, height, width, height);
		
		double xMult;
        double yMult;
        try {
            xMult = ((double)width - OFFSET_X)/data.size();
            yMult = ((double)height - OFFSET_Y)/data.max();
        } catch (Exception e) {
            xMult = 0;
            yMult = 0;
        }
		
		int index = 0;
		double prev = 0.0;
		for(Double value : data) {
			int x = (int)(index*xMult) + OFFSET_X;
			int y = (int)(value*yMult) - OFFSET_Y;
			if (value < prev) {
				g.setColor(Color.RED);
			} else {
				g.setColor(Color.BLACK);
			}
			g.fillOval(x - SIZE/2, height - y - OFFSET_Y - SIZE/2, SIZE, SIZE);
			index++;
			
			prev = value;
		}
		g.setColor(Color.BLACK);

		g.drawString("0", OFFSET_X - FONT_SIZE, height + FONT_SIZE);
		if (data.size() > 1) {
			g.drawString(Integer.toString(data.size()),(int)(xMult * (data.size() - 1)) + OFFSET_X, height + FONT_SIZE);
			g.drawString(DF.format(data.max()), OFFSET_X - (int)METRICS.getStringBounds(DF.format(data.max()), g).getWidth(), OFFSET_Y);
		}
	}
	
	/**
	 * Set the data to graph
	 * @param data
	 */
	public JPanel setData(Recorder data, String yAxis) {
		this.data = data;
		this.yAxis = yAxis;
		this.dataCollection.removeAllSeries();
		return this.getChart();
		//this.repaint();
	}

}