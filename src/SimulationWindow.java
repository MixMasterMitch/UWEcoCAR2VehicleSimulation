import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

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
	
	private StepSimulator simulator;
	public static final DecimalFormat DF = new DecimalFormat("0000.0");
	
	/************* GUI STUFF *************/
	private final JTextArea console = new JTextArea();
	private final JGraph graph = new JGraph();
	
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
	private final JButton saveAndResetButton = new JButton("Save & Reset");
	
	
	/**
	 * Constructor
	 * @param simulator
	 */
	public SimulationWindow(StepSimulator s) {
		this.simulator = s;
		
		JFrame mainWindow = new JFrame("Step Simulator");
			mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			mainWindow.setBackground(Color.WHITE);
			
		// create the main content panel and fill it	
		JPanel contentPanel = new JPanel();
			contentPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 10));
			contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
			
			graph.setData(((MalibuStepSimulator)simulator).speed);
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
			
			backButton.setEnabled(false);
			unstepButton.setEnabled(false);
			stepButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
					step();
				}
			});
			forwardButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
					while(!simulator.isDone()) {
						step();
					}
				}
			});
			
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
				
				JPanel saveAndGlobal = new JPanel();
					saveAndGlobal.setLayout(new BoxLayout(saveAndGlobal, BoxLayout.Y_AXIS));
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
				saveAndGlobal.add(globalSettings);
				saveAndGlobal.add(saveAndResetButton);
					
			settings.add(carSettings);
			settings.add(saveAndGlobal);
			
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
        console.append("time [s]\tinput [%]\tspeed [mph]\trpm\ttrq [Nm]\tdistance [m]\tgas\n");
	}
	
	public void step() {
		this.simulator.step();
		this.graph.repaint();
		this.printSimulator();
	}
	
	private void printSimulator() {
		MalibuStepSimulator sim = (MalibuStepSimulator) this.simulator;
		this.console.append(
	        DF.format( sim.getSimulationTime() ) + "\t" +
	        DF.format( sim.getGasPedalPosition() ) + "\t" +
	        DF.format( sim.getSpeed() * 2.23) + "\t" +
	        DF.format( sim.getRpm() ) + "\t" +
	        DF.format( sim.getTorque() ) + "\t" +
	        DF.format( sim.getDistance() ) + "\t" +
	        DF.format( sim.getGasSum() ) + "\n"
		);
	}
}