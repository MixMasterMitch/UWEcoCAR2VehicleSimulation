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
	
	private MalibuStepSimulator simulator;
	public static final DecimalFormat DF = new DecimalFormat("0000.0");
	
	/************* GUI STUFF *************/
	private final JTextArea console = new JTextArea();
	private final JGraph graph = new JGraph();
	
	private final String[] graphOptions = {"Speed", "Distance", "Gas"}; 
	private final JComboBox graphComboBox = new JComboBox(graphOptions);
	
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
	public SimulationWindow(MalibuStepSimulator s) {
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
				
				graphComboBox.setSelectedIndex(0);
				graphComboBox.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent event) {
						setGraph();
					}
				});
					
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
					Double[] cValues = {simulator.getCarWeight(), simulator.getCarAirDragCoeff(), simulator.getCarRollingCoeff(), simulator.getCarFrontalArea()};
					
					for(int i = 0; i < cLabels.length; i++) {
						JPanel l = new JPanel();
							l.add(new JLabel(cLabels[i]));
							l.add(cFields[i]);
							cFields[i].setText(cValues[i].toString());
						carSettings.add(l);
					}
				
				JPanel saveAndGlobal = new JPanel();
					saveAndGlobal.setLayout(new BoxLayout(saveAndGlobal, BoxLayout.Y_AXIS));
					JPanel globalSettings = new JPanel();
						globalSettings.setLayout(new BoxLayout(globalSettings, BoxLayout.Y_AXIS));
						globalSettings.setBorder(BorderFactory.createTitledBorder("Global Settings"));
						String[] gLabels = {"Gravity", "Air Density"};
						JTextField[] gFields = {gravity, airDensity};
						Double[] gValues = {simulator.getGravity(), simulator.getAirDensity()};
						
						for(int i = 0; i < gLabels.length; i++) {
							JPanel l = new JPanel();
								l.add(new JLabel(gLabels[i]));
								gFields[i].setText(gValues[i].toString());
								l.add(gFields[i]);
							globalSettings.add(l);
						}
				saveAndGlobal.add(globalSettings);
				saveAndGlobal.add(saveAndResetButton);
				
				saveAndResetButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent event) {
						console.setText("time [s]\tinput [%]\tspeed [mph]\trpm\ttrq [Nm]\tdistance [m]\tgas\n");
						simulator = new MalibuStepSimulator(simulator.gas, simulator.gasRpmTrq, simulator.time_step);
						try {
							simulator.setGravity(Double.parseDouble(gravity.getText()));
							simulator.setAirDensity(Double.parseDouble(airDensity.getText()));
							
							simulator.setCarWeight(Double.parseDouble(carWeight.getText()));
							simulator.setCarAirDragCoeff(Double.parseDouble(carAirDragCoeff.getText()));
							simulator.setCarRollingCoeff(Double.parseDouble(carRollCoeff.getText()));
							simulator.setCarFrontalArea(Double.parseDouble(carFrontalArea.getText()));
						} catch(Exception e) {
							console.setText("Error. Invalid settings.\n");
						} finally {
							setGraph();
						}
						
						checkButtons();
					}
				});
					
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
	
	/**
	 * Runs the simulator for one step, updates the gui as needed
	 */
	public void step() {
		this.simulator.step();
		this.graph.repaint();
		this.checkButtons();
		this.printSimulator();
	}
	
	/**
	 * Writes the current simulator data to the console
	 */
	private void printSimulator() {
		this.console.append(
	        DF.format( simulator.getSimulationTime() ) + "\t" +
	        DF.format( simulator.getGasPedalPosition() ) + "\t" +
	        DF.format( simulator.getSpeed() * 2.23) + "\t" +
	        DF.format( simulator.getRpm() ) + "\t" +
	        DF.format( simulator.getTorque() ) + "\t" +
	        DF.format( simulator.getDistance() ) + "\t" +
	        DF.format( simulator.getGasSum() ) + "\n"
		);
	}
	
	/**
	 * Sets the data of the graph based off the current selection
	 */
	private void setGraph() {
		int index = graphComboBox.getSelectedIndex();
		switch (index) {
			case 0 : graph.setData(simulator.speed);
				break;
			case 1 : graph.setData(simulator.distance);
				break;
			case 2 : graph.setData(simulator.gasSum);
				break;
			default: graph.setData(simulator.speed);
		}
	}
	
	/**
	 * Checks to see if the control buttons should be enabled or disabled
	 */
	private void checkButtons() {
		if (simulator.isDone() && stepButton.isEnabled()) {
			stepButton.setEnabled(false);
			forwardButton.setEnabled(false);
		} else if (!simulator.isDone() && !stepButton.isEnabled()) {
			stepButton.setEnabled(true);
			forwardButton.setEnabled(true);
		}
	}
}