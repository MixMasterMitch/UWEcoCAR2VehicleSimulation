package com.uwecocar2.vehiclesimulation;

import com.uwecocar2.vehiclesimulation.utils.LookUpTable2D;

public class DiscreteStepSimulator implements StepSimulator {

    public static final Double DEFUALT_TIME_STEP = .1;

    // The simulation cycle speed
    private Double timeStep; // s
    
    // Supplies gas and pedal positions
    public final Driver driver;

    // The object responding the driver inputs
    public final Vehicle vehicle;
    
    // The number of steps the simulation has run for
    private int steps;

	public DiscreteStepSimulator(Driver driver, Vehicle vehicle, Double timeStep) {
        this.vehicle = vehicle;
		this.driver = driver;
		this.timeStep = timeStep;
		this.steps = 0;
	}
	
	/**
	 * Step forward one time_step in the simulation
	 */
	public void step() {
        if (getGasPedalPosition() == null || getBrakePedalPosition() == null) {
            throw new IllegalStateException("The brake and gas pedal positions must be defined to step");
        }
        vehicle.setGasPedalPosition(getGasPedalPosition());
        vehicle.setBrakePedalPosition(getBrakePedalPosition());

        vehicle.cycle(timeStep);
		
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
		return driver.getGasPedalPosition(getSimulationTime());
	}

    /**
     * Calculate the current brake pedal position
     * @return
     */
    public Double getBrakePedalPosition() {
        return driver.getBrakePedalPosition(getSimulationTime());
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
		return driver.getGasPedalPosition(getSimulationTime()) == null || driver.getBrakePedalPosition(getSimulationTime()) == null;
	}

    public void reset() {
        steps = 0;
        vehicle.reset();
    }
	
	public Double getTimeStep() {
		return this.timeStep;
	}
	public void setTimeStep(Double timeStep) {
		this.timeStep = timeStep;
	}

}