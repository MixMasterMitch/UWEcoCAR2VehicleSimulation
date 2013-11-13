package com.uwecocar2.vehiclesimulation;

public interface Vehicle {
    /*
        INPUTS
     */
    public void setGasPedalPosition(Double gasPedalPosition);
    public void setBrakePedalPosition(Double brakePedalPosition);

    /*
        OUTPUTS
     */

    /**
     * In Rotations Per Minute (RPM)
     */
    public Double getWheelAngularVelocity();

    /**
     * The current velocity of the vehicle
     * In m/s
     */
    public Double getVelocity();

    /**
     * The current acceleration of the vehicle
     * In m/s^2
     */
    public Double getAcceleration();

    /**
     * The total distance that the vehicle has travelled
     * In m
     */
    public Double getDistanceTravelled();

    /**
     * The force of air resistance on the vehicle
     * In N
     */
    public Double getAirResistance();

    public Double getRollingResistance();

    public Double getEngineTorque();

    public Double getMotorTorque();

    public Double getTotalDieselEnergyChange();

    public Double getTotalElectircalEnergyChange();

    public Double getTotalKineticEnergyChange();

    public Double getTotalPotentialEnergyChange();

    public Double getInstantaneousDieselEnergyChange();

    public Double getInstantaneousElectircalEnergyChange();

    public Double getInstantaneousKineticEnergyChange();

    public Double getInstantaneousPotentialEnergyChange();

    /*
        CYLCE TRIGGER
     */
    public void cycle(Double length);

    /*
        OTHER
     */
    public void reset();

}
