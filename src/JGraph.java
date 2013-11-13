import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.text.DecimalFormat;

import javax.swing.JPanel;

public class JGraph extends JPanel {

	// the data to graph
	private Recorder data;
	
	// the graph padding
	private static final int OFFSET_X = 75;
	private static final int OFFSET_Y = 75;
	private static final int SIZE = 2;
	private static final int FONT_SIZE = 20;
	
	private static final DecimalFormat DF = new DecimalFormat("###.##");
	private static final Font FONT = new Font("UNICODE", Font.PLAIN, FONT_SIZE);
	private final FontMetrics METRICS;
	
	public JGraph() {
		super();
		this.setFont(FONT);
		METRICS = this.getFontMetrics(FONT);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		int width = this.getWidth() - OFFSET_X;
		int height = this.getHeight() - OFFSET_Y;
		
		g.clearRect(0, 0, this.getWidth(), this.getHeight());
		
		g.setColor(Color.BLACK);
		g.drawLine(OFFSET_X, OFFSET_Y, OFFSET_X, height);
		g.drawLine(OFFSET_X, height, width, height);
		
		double xMult = ((double)width - OFFSET_X)/data.size();
		double yMult = ((double)height - OFFSET_Y)/data.max();
		
		int index = 0;
		for(Double value : data) {
			int x = (int)(index*xMult) + OFFSET_X;
			int y = (int)(value*yMult) - OFFSET_Y;
			g.fillOval(x - SIZE/2, height - y - OFFSET_Y - SIZE/2, SIZE, SIZE);
			index++;
		}		
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
	public void setData(Recorder data) {
		this.data = data;
		this.repaint();
	}

}