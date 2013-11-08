import java.text.DecimalFormat;
import java.util.Map;
import java.util.TreeMap;

public class EntryPoint {

    // The amount of time between simulation steps. Lower number = more accurate and slower
    public static final Double TIME_STEP = 0.1; // s

    // The amount of time to run the simulation for.
    public static final Double SIMULATION_LENGTH = 10.0; // s

    // The weight of the UW EcoCAR 2013 Chevy Malibu
    public static final Double MALIBU_WEIGHT = 2200.0; // kg

    // Gravitational Acceleration on Earth
    public static final Double GRAVITY = 9.81; // m/s^2

    // The weight of the UW EcoCAR 2013 Chevy Malibu
    public static final Double MALIBU_DOWN_FORCE = MALIBU_WEIGHT * GRAVITY; // N

    // The effective radius of the tires
    public static final Double TIRE_RADIUS = .32; // m

    // The density of air at STP
    public static final Double AIR_DENSITY = 1.2; // kg / m^3

    // The coefficient of air resistance of the 2013 Chevy Malibu
    public static final Double MALIBU_AIR_DRAG_COEFF = .29;

    // The coefficient of rolling resistance of the tires on the road
    public static final Double MALIBU_ROLLING_COEFF = .02;

    // The frontal area of the 2013 Chevy Malibu (i.e. the cross sectional area)
    public static final Double MALIBU_FRONTAL_AREA = 2.15; // m^2

    public static final DecimalFormat DF = new DecimalFormat("0000.0");

    public static void main(String[] args) throws Exception {

        // The forward speed of the vehicle
        Double speed = 0.0; // m/s

        // The trq being applied to the wheels by the engine
        Double engineTrq = 0.0; // Nm

        // The distance traveled (i.e. integral of speed)
        Double distance = 0.0; // m

        // TODO: convert gasSum into actual gas usage
        // The total amount of gas used (i.e. integral of gas)
        Double gasSum = 0.0; // %

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

        // Run the simulation
        for (double simulationTime = 0.0; simulationTime < SIMULATION_LENGTH; simulationTime += TIME_STEP) {
            /*
                STEP 1: Get input values
             */
            Double gasPedalPosition = lookUpTable1D.getValue(simulationTime);

            /*
                STEP 2: Calculate speed / rpm
             */

//            System.out.println("ENGINE FORCE " + getWheelForce(trq));
//            System.out.println("WIND RESISTANCE " + getAirResistance(speed));
//            System.out.println("ROLLING RESISTANCE " + getRollingResistance(speed));

            // TODO: include vehicle rotational inertia calculations
            speed += (getWheelForce(engineTrq) - getRollingResistance(speed) - getAirResistance(speed)) / MALIBU_WEIGHT * TIME_STEP;

            // TODO: negative values cause problems
            speed = Math.max(speed, 0);

            Double rpm = convertSpeedToRpm(speed, TIRE_RADIUS);

            /*
                STEP 3: Calculate Torques
             */
            engineTrq = gasRpmTrq.getValue(gasPedalPosition, rpm);

            /*
                STEP 4: Integrate
             */
            distance += speed * TIME_STEP;
            gasSum += gasPedalPosition * TIME_STEP;

            /*
                STEP 5: Display time step data
             */
            System.out.println(
                    "At time[s]: " + DF.format(simulationTime) +
                            " gas[%]: " + DF.format(gasPedalPosition) +
                            " speed[mph]: " + DF.format(convertMpsToMph(speed)) +
                            " rpm: " + DF.format(rpm) +
                            " trq[Nm]: " + DF.format(engineTrq) +
                            " distance[m]: " + DF.format(distance));
        }

        // Output simulation aggregate results
        System.out.println("Total Gas Used: " + gasSum);

    }

    /**
     * Calculates the air resistance applied to the vehicle at the given speed
     * @param speed m/s
     * @return A positive number means a negative force
     */
    public static Double getAirResistance(Double speed) { // m/s

        return MALIBU_AIR_DRAG_COEFF * AIR_DENSITY * MALIBU_FRONTAL_AREA * speed * speed / 2; // F = c 1/2 ρ v2 A
    }

    /**
     * Calculates the rolling resistance of the vehicle at the given speed
     * @param speed m/s
     * @return A positive number means a negative force
     */
    public static Double getRollingResistance(Double speed) { // m/s
        // TODO: Handle reverse
        if (Math.round(speed*100)/100.0 > 0.0) { // We are going faster than .0005 m/s
            return MALIBU_DOWN_FORCE * MALIBU_ROLLING_COEFF; // F = c W
        } else {
            return 0.0;
        }
    }

    /**
     * Calculates the force being applied to the vehicle by the given axel torque
     * @param trq Nm
     */
    public static Double getWheelForce(Double trq) {
        return trq / TIRE_RADIUS; // Nm / m -> N
    }

    /**
     * Calculates the rpm that a wheel of the given radius would spin with a linear velocity of the given speed.
     * @param speed m/s
     * @param radius m
     */
    public static Double convertSpeedToRpm(Double speed, Double radius) {
        return speed / (2 * radius * Math.PI) * 60;
    }

    /**
     * Calculates the equivalent speed in miles per hour (mph) of the given speed in meters per second (m/s)
     * @param speed m/s
     */
    public static Double convertMpsToMph(Double speed) {
        return speed * 2.23;
    }
}
