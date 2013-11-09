import java.text.DecimalFormat;
import java.util.Map;
import java.util.TreeMap;

public class EntryPoint {

    // The amount of time between simulation steps. Lower number = more accurate and slower
    public static final Double TIME_STEP = 0.1; // s

    // The amount of time to run the simulation for.
    public static final Double SIMULATION_LENGTH = 10.0; // s

    public static final DecimalFormat DF = new DecimalFormat("0000.0");

    public static void main(String[] args) throws Exception {
    	
    	SimulationWindow window = new SimulationWindow();

        // Create a lookup table defining trq at the wheels from the engine
        // for a given gas pedal position and wheel rpm.
        // In the text file, the first row is the pedal positions, the first column is the rpm values.
        LookUpTable2D gasRpmTrq = new LookUpTable2D("src/gasRpmTrq.txt");

        // Create a drive cycle
        Map<Double, Double> gas = new TreeMap<Double, Double>();
        gas.put(0.0, 0.0);
        gas.put(1.0, 100.0);
        gas.put(3.0, 50.0);
        gas.put(5.0, 50.0);
        gas.put(7.0, 0.0);
        LookUpTable1D lookUpTable1D = new LookUpTable1D(gas);
        
        MalibuStepSimulator simulator = new MalibuStepSimulator(lookUpTable1D, gasRpmTrq, TIME_STEP);
        	
        System.out.println("time [s]\tinput [%]\tspeed [mph]\trpm\ttrq [Nm]\tdistance [m]\tgas");
        
        // Run the simulation
        while (simulator.getSimulationTime() < SIMULATION_LENGTH) {
        	// get gas input before step
        	Double pedalPosition = simulator.getGasPedalPosition();
        	
        	// simulate one TIME_STEP
        	simulator.step();
        	
        	// output 
            System.out.println(
                    DF.format( simulator.getSimulationTime() ) + "\t" +
                    DF.format( pedalPosition ) + "\t" +
                    DF.format( convertMpsToMph( simulator.getSpeed() ) ) + "\t" +
                    DF.format( simulator.getRpm() ) + "\t" +
                    DF.format( simulator.getTorque() ) + "\t" +
                    DF.format( simulator.getDistance() ) + "\t" +
                    DF.format( simulator.getGasSum() )
            	);
        }
    }

    /**
     * Calculates the equivalent speed in miles per hour (mph) of the given speed in meters per second (m/s)
     * @param speed m/s
     */
    public static Double convertMpsToMph(Double speed) {
        return speed * 2.23;
    }
}
