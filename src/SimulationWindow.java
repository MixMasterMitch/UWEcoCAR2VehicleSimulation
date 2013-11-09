import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class SimulationWindow {
	
	private final JTextArea console = new JTextArea();
	
	private final JButton backButton = new JButton("◀︎◀︎");
	private final JButton unstepButton = new JButton("◀︎");
	private final JButton stepButton = new JButton("►");
	private final JButton forwardButton = new JButton("►►");
	
	private final JTextField gravity = new JTextField(10);
	private final JTextField airDensity = new JTextField(10);
	
	private final JTextField carWeight = new JTextField(10);
	private final JTextField carAirDragCoeff = new JTextField(10);
	private final JTextField carRollCoeff = new JTextField(10);
	private final JTextField carFrontalArea = new JTextField(10);
	
	public SimulationWindow() {
		JFrame mainWindow = new JFrame("Step Simulator");
			mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			mainWindow.setBackground(Color.WHITE);

			
			
		// create the main content panel and fill it	
		JPanel contentPanel = new JPanel();
			contentPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 10));
			contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
			
			JPanel graph = new JPanel();
				graph.setBorder(BorderFactory.createTitledBorder("Graph"));
				graph.setPreferredSize(new Dimension(500, 350));
			
			JPanel simControls = new JPanel();
				simControls.setBorder(BorderFactory.createTitledBorder("Controls"));
				simControls.setLayout(new BoxLayout(simControls, BoxLayout.X_AXIS));
			
				String[] graphOptions = {"Speed", "Torque", "Distance", "Gas"}; 
				JComboBox graphComboBox = new JComboBox(graphOptions);
					graphComboBox.setSelectedIndex(0);
					
			simControls.add(graphComboBox);
			simControls.add(backButton);
			simControls.add(unstepButton);
			simControls.add(stepButton);
			simControls.add(forwardButton);
			
			JPanel settings = new JPanel();
				settings.setLayout(new BoxLayout(settings, BoxLayout.X_AXIS));
				JPanel carSettings = new JPanel();
					carSettings.setLayout(new BoxLayout(carSettings, BoxLayout.Y_AXIS));
					carSettings.setBorder(BorderFactory.createTitledBorder("Car Settings"));
					String[] cLabels = {"Weight", "Air Drag Coeff", "Rolling Coeff", "Frontal Area"};
					JTextField[] cFields = {carWeight, carAirDragCoeff, carRollCoeff, carFrontalArea};
					
					for(int i = 0; i < cLabels.length; i++) {
						JPanel l = new JPanel();
							l.add(new JLabel(cLabels[i]));
							l.add(cFields[i]);
						carSettings.add(l);
					}
						
				JPanel globalSettings = new JPanel();
					globalSettings.setLayout(new BoxLayout(globalSettings, BoxLayout.Y_AXIS));
					globalSettings.setBorder(BorderFactory.createTitledBorder("Global Settings"));
					String[] gLabels = {"Gravity", "Air Density"};
					JTextField[] gFields = {gravity, airDensity};
					
					for(int i = 0; i < gLabels.length; i++) {
						JPanel l = new JPanel();
							l.add(new JLabel(gLabels[i]));
							l.add(gFields[i]);
						globalSettings.add(l);
					}
					
			settings.add(carSettings);
			settings.add(globalSettings);
			
		contentPanel.add(graph);
		contentPanel.add(simControls);
		contentPanel.add(settings);
		
		// setup console
		console.setEditable(false);
		JScrollPane consoleScroller = new JScrollPane(console);
		consoleScroller.setPreferredSize(new Dimension(400,0));
		
		// create main panel and fill it
		JPanel mainPanel = new JPanel(new BorderLayout());
			mainPanel.add(contentPanel, BorderLayout.LINE_START);
			mainPanel.add(consoleScroller, BorderLayout.LINE_END);
			
		// finally get add main panel to the frame and display window
		mainWindow.getContentPane().add(mainPanel, BorderLayout.CENTER);
		mainWindow.pack();
        mainWindow.setVisible(true);
	}
}