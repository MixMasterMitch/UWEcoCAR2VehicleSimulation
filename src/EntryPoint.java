import java.util.Map;
import java.util.TreeMap;

public class EntryPoint {

    // The amount of time between simulation steps. Lower number = more accurate and slower
    public static final Double TIME_STEP = 0.1; // s

    public static void main(String[] args) throws Exception {

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
        gas.put(10.0, 0.5);
        LookUpTable1D lookUpTable1D = new LookUpTable1D(gas);
        
        
        
        MalibuStepSimulator simulator = new MalibuStepSimulator(lookUpTable1D, gasRpmTrq, TIME_STEP);      
    	SimulationWindow window = new SimulationWindow(simulator);
    }
}
