import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class JGraph extends JPanel {

	// the data to graph
	private Recorder data;
	
	// the graph padding
	private static final int OFFSET_X = 50;
	private static final int OFFSET_Y = 50;
	
	@Override
	public void paintComponent(Graphics g) {
		int width = this.getWidth() - OFFSET_X;
		int height = this.getHeight() - OFFSET_Y;
		
		g.clearRect(0, 0, this.getWidth(), this.getHeight());
		
		g.setColor(Color.BLACK);
		g.drawLine(OFFSET_X, OFFSET_Y, OFFSET_X, height);
		g.drawLine(OFFSET_X, height, width, height);
		
		double xMult = width/data.size();
		double yMult = (height - OFFSET_Y)/data.max();
		int index = 0;
		for(Double value : data) {
			int x = (int)(index*xMult) + OFFSET_X;
			int y = (int)(value*yMult) - OFFSET_Y;
			g.fillOval(x, height - y - OFFSET_Y, 3, 3);
			index++;
		}
	}
	
	/**
	 * Set the data to graph
	 * @param data
	 */
	public void setData(Recorder data) {
		this.data = data;
	}

}