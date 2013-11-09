public class MalibuStepSimulator {
	
	/************ CONTANT VARIABLES ************/
	
	 // The weight of the UW EcoCAR 2013 Chevy Malibu
    private static final Double MALIBU_WEIGHT = 2200.0; // kg

    // Gravitational Acceleration on Earth
    private static final Double GRAVITY = 9.81; // m/s^2

    // The weight of the UW EcoCAR 2013 Chevy Malibu
    private static final Double MALIBU_DOWN_FORCE = MALIBU_WEIGHT * GRAVITY; // N

    // The effective radius of the tires
    private static final Double TIRE_RADIUS = .32; // m

    // The density of air at STP
    private static final Double AIR_DENSITY = 1.2; // kg / m^3

    // The coefficient of air resistance of the 2013 Chevy Malibu
    private static final Double MALIBU_AIR_DRAG_COEFF = .29;

    // The coefficient of rolling resistance of the tires on the road
    private static final Double MALIBU_ROLLING_COEFF = .02;

    // The frontal area of the 2013 Chevy Malibu (i.e. the cross sectional area)
    private static final Double MALIBU_FRONTAL_AREA = 2.15; // m^2
    
    /************ CUSTOM VARIABLES ************/
    
    // The amount of time between simulation steps. Lower number = more accurate and slower
    private final Double time_step;
    
    // A table of mapping time to gas input
    private final LookUpTable1D gas;
    
    // the lookup table
    private final LookUpTable2D gasRpmTrq;
    
    /************ DYNAMIC VARIABLES ************/
    
    // The number of steps the simulation has run for
    private int steps;
    
    // the current speed of the car
    private Double speed;
    
    // the total distance the car has traveled
    private Double distance;
    
    // the total amount of gas used
    private Double gasSum;
        
	public MalibuStepSimulator(LookUpTable1D gas, LookUpTable2D gasRpmTrq, Double time_step) {
		this.gas = gas;
		this.gasRpmTrq = gasRpmTrq;
		this.time_step = time_step;
		
		this.steps = 0;
		this.speed = 0.0;
		this.distance = 0.0;
		this.gasSum = 0.0;
	}
	
	/**
	 * Step forward one time_step in the simulation
	 */
	public void step() {
		Double pedalPosition = this.getGasPedalPosition();
			
		// Calculate speed / rpm
		this.speed += (this.getWheelForce() - this.getRollingResistance() - this.getAirResistance())/ MALIBU_WEIGHT * this.time_step;
		this.speed = Math.max(this.speed, 0);
				
		this.distance += this.speed * this.time_step;
		this.gasSum += pedalPosition * this.time_step;
		
		this.steps++;
	}
	
	/**
	 * Step backwards oen time_step in the simulation
	 * @throws UnsupportedOperationException();
	 */
	public void unstep() {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Calculate the simulation time
	 * @return
	 */
	public Double getSimulationTime() {
		return this.getSteps() * this.time_step;
	}
	
	/**
	 * Calculate the current gas pedal position
	 * @return
	 */
	public Double getGasPedalPosition() {
		return this.gas.getValue(this.getSimulationTime());
	}
	
	/**
	 * Get the current speed of the car
	 * @return
	 */
    public Double getSpeed() {
		return speed;
	}
    
    /**
     * Calculate the current torque exerted by the engine(?)
     * @return
     */
	public Double getTorque() {
		return this.gasRpmTrq.getValue(this.getGasPedalPosition(), this.getRpm());
	}
	
	/**
	 * Get the current total distance traveled
	 * @return
	 */
	public Double getDistance() {
		return distance;
	}
	
	/**
	 * Get the current amount of gas used
	 * @return
	 */
	public Double getGasSum() {
		return gasSum;
	}
	
	 /**
     * Calculates the force being applied to the vehicle by the given axel torque
     * @return the current force exerted by the wheel on the road
     */
    public Double getWheelForce() {
        return this.getTorque() / TIRE_RADIUS; // Nm / m -> N
    }
    
    /**
     * Calculates the rolling resistance of the vehicle at the given speed
     * @return A positive number means a negative force
     */
    public Double getRollingResistance() { // m/s
        // TODO: Handle reverse
        if (Math.round(this.speed*100)/100.0 > 0.0) { // We are going faster than .0005 m/s
            return MALIBU_DOWN_FORCE * MALIBU_ROLLING_COEFF; // F = c W
        } else {
            return 0.0;
        }
    }
    
    /**
     * Calculates the air resistance applied to the vehicle at the given speed
     * @return A positive number means a negative force
     */
    public Double getAirResistance() { // m/s

        return MALIBU_AIR_DRAG_COEFF * AIR_DENSITY * MALIBU_FRONTAL_AREA * Math.pow(this.speed, 2) / 2; // F = c 1/2 ρ v2 A
    }
    
    /**
     * Calculate the RPM of the car wheels
     * @return
     */
    public Double getRpm() {
		return this.speed / (2 * TIRE_RADIUS * Math.PI ) * 60;
	}
    
    /**
     * Get the number of steps that have passed in the simulation
     * @return
     */
	public int getSteps() {
		return steps;
	}
	
}