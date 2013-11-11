public class MalibuStepSimulator implements StepSimulator {
	
	/************ CONTANT VARIABLES ************/
	
	 // The weight of the UW EcoCAR 2013 Chevy Malibu
    private Double carWeight = 2200.0; // kg

	// Gravitational Acceleration on Earth
    private Double gravity = 9.81; // m/s^2
    
    private Double timeStep = .1;

    // The effective radius of the tires
    private Double tireRadius = .32; // m

    // The density of air at STP
    private Double airDensity = 1.2; // kg / m^3

    // The coefficient of air resistance of the 2013 Chevy Malibu
    private Double carAirDragCoeff = .29;

    // The coefficient of rolling resistance of the tires on the road
    private Double carRollingCoeff = .02;

    // The frontal area of the 2013 Chevy Malibu (i.e. the cross sectional area)
    private Double carFrontalArea = 2.15; // m^2
    
    /************ CUSTOM VARIABLES ************/
    
    // A table of mapping time to gas input
    public final LookUpTable1D gas;
    
    // the lookup table
    public final LookUpTable2D gasRpmTrq;
    
    /************ DYNAMIC VARIABLES ************/
    
    // The number of steps the simulation has run for
    private int steps;
    
    // the current speed of the car
    public Recorder speed;
       
    // the total distance the car has traveled
    public Recorder distance;
    
    // the total amount of gas used
    public Recorder gasSum;
        
	public MalibuStepSimulator(LookUpTable1D gas, LookUpTable2D gasRpmTrq) {
		this.gas = gas;
		this.gasRpmTrq = gasRpmTrq;
		
		this.steps = 0;
		this.speed = new Recorder(0.0);
		this.distance = new Recorder(0.0);
		this.gasSum = new Recorder(0.0);
	}
	
	/**
	 * Step forward one time_step in the simulation
	 */
	public void step() {
		Double pedalPosition = getGasPedalPosition();

		speed.set( Math.max(speed.get() + (getWheelForce() - getRollingResistance() - getAirResistance())/ getCarWeight() * timeStep, 0.0));
		distance.set( distance.get() + speed.get() * timeStep );
		gasSum.set( gasSum.get() + pedalPosition * timeStep );
		
		steps++;
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
		return getSteps() * getTimeStep();
	}
	
	/**
	 * Calculate the current gas pedal position
	 * @return
	 */
	public Double getGasPedalPosition() {
		return gas.getValue(getSimulationTime());
	}
	
	/**
	 * Get the current speed of the car
	 * @return
	 */
    public Double getSpeed() {
		return speed.get();
	}
    
    /**
     * Calculate the current torque exerted by the engine(?)
     * @return
     */
	public Double getTorque() {
		return gasRpmTrq.getValue(getGasPedalPosition(), getRpm());
	}
	
	/**
	 * Get the current total distance traveled
	 * @return
	 */
	public Double getDistance() {
		return distance.get();
	}
	
	/**
	 * Get the current amount of gas used
	 * @return
	 */
	public Double getGasSum() {
		return gasSum.get();
	}
	
	 /**
     * Calculates the force being applied to the vehicle by the given axel torque
     * @return the current force exerted by the wheel on the road
     */
    public Double getWheelForce() {
        return getTorque() / getTireRadius(); // Nm / m -> N
    }
    
    /**
     * Calculates the rolling resistance of the vehicle at the given speed
     * @return A positive number means a negative force
     */
    public Double getRollingResistance() { // m/s
        // TODO: Handle reverse
        if (Math.round(getSpeed()*100)/100.0 > 0.0) { // We are going faster than .0005 m/s
            return getCarDownForce() * getCarRollingCoeff(); // F = c W
        } else {
            return 0.0;
        }
    }
    
    /**
     * Calculates the air resistance applied to the vehicle at the given speed
     * @return A positive number means a negative force
     */
    public Double getAirResistance() { // m/s
        return getCarAirDragCoeff() * getAirDensity() * getCarFrontalArea() * Math.pow(getSpeed(), 2) / 2; // F = c 1/2 ρ v2 A
    }
    
    /**
     * Calculate the RPM of the car wheels
     * @return
     */
    public Double getRpm() {
		return getSpeed() / (2 * getTireRadius() * Math.PI ) * 60;
	}
    
    /**
     * Get the number of steps that have passed in the simulation
     * @return
     */
	public int getSteps() {
		return steps;
	}
	
	/**
	 * Returns true if the time is out of the defined range
	 */
	public boolean isDone() {
		return !gas.isIn(getSimulationTime());
	}
	
	public Double getCarWeight() {
		return carWeight;
	}
	public void setCarWeight(Double carWeight) {
		this.carWeight = carWeight;
	}

	public Double getGravity() {
		return gravity;
	}
	public void setGravity(Double gravity) {
		this.gravity = gravity;
	}
	
	public Double getTimeStep() {
		return this.timeStep;
	}
	public void setTimeStep(Double timeStep) {
		this.timeStep = timeStep;
	}

	public Double getCarDownForce() {
		return getCarWeight() * getGravity();
	}

	public Double getTireRadius() {
		return tireRadius;
	}
	public void setTireRadius(Double tireRadius) {
		this.tireRadius = tireRadius;
	}

	public Double getAirDensity() {
		return airDensity;
	}
	public void setAirDensity(Double airDensity) {
		this.airDensity = airDensity;
	}

	public Double getCarAirDragCoeff() {
		return carAirDragCoeff;
	}
	public void setCarAirDragCoeff(Double carAirDragCoeff) {
		this.carAirDragCoeff = carAirDragCoeff;
	}

	public Double getCarRollingCoeff() {
		return carRollingCoeff;
	}
	public void setCarRollingCoeff(Double carRollingCoeff) {
		this.carRollingCoeff = carRollingCoeff;
	}

	public Double getCarFrontalArea() {
		return carFrontalArea;
	}
	public void setCarFrontalArea(Double carFrontalArea) {
		this.carFrontalArea = carFrontalArea;
	}

}