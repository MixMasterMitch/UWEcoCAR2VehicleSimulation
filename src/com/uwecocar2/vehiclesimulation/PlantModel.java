package com.uwecocar2.vehiclesimulation;

import com.uwecocar2.vehiclesimulation.utils.LookUpTable2D;

import java.io.File;

public class PlantModel implements Vehicle {

    public Parameters params;

    // the acceleration of the car
    private Recorder acceleration;

    // the speed of the car
    private Recorder velocity;

    // the distance the car has traveled
    private Recorder distance;

    private Double gasPedalPosition;

    private Double brakePedalPosition;

    public PlantModel(Parameters params) {
        this.params = params;
        reset();
    }

    @Override
    public void setGasPedalPosition(Double gasPedalPosition) {
        this.gasPedalPosition = gasPedalPosition;
    }

    @Override
    public void setBrakePedalPosition(Double brakePedalPosition) {
        this.brakePedalPosition = brakePedalPosition;
    }

    @Override
    public Double getWheelAngularVelocity() {
        return velocity.get() / (2 * params.getTireRadius() * Math.PI ) * 60;
    }

    @Override
    public Double getVelocity() {
        return velocity.get();
    }

    @Override
    public Double getAcceleration() {
        return acceleration.get();
    }

    @Override
    public Double getDistanceTravelled() {
        return distance.get();
    }

    public Recorder getVelocityRecorder() {
        return velocity;
    }

    public Recorder getAccelerationRecorder() {
        return acceleration;
    }

    public Recorder getDistanceTravelledRecorder() {
        return distance;
    }

    @Override
    public Double getAirResistance() {
        return params.getAirDragCoeff() * params.getAirDensity() * params.getFrontalArea() * Math.pow(getVelocity(), 2) / 2; // F = c 1/2 ρ v2 A
    }

    @Override
    public Double getRollingResistance() {
        // TODO: Handle reverse
        if (Math.round(getVelocity()*100)/100.0 > 0.0) { // We are going faster than .0005 m/s
            return getCarDownForce() * params.getRollingCoeff(); // F = c W
        } else {
            return 0.0;
        }
    }

    @Override
    public Double getEngineTorque() {
        return params.getEngineTorque(gasPedalPosition, getWheelAngularVelocity());
    }

    @Override
    public Double getMotorTorque() {
        return params.getMotorTorque(gasPedalPosition, getWheelAngularVelocity());
    }

    @Override
    public Double getTotalDieselEnergyChange() {
        return 0.0;
    }

    @Override
    public Double getTotalElectircalEnergyChange() {
        return 0.0;
    }

    @Override
    public Double getTotalKineticEnergyChange() {
        return 0.0;
    }

    @Override
    public Double getTotalPotentialEnergyChange() {
        return 0.0;
    }

    @Override
    public Double getInstantaneousDieselEnergyChange() {
        return 0.0;
    }

    @Override
    public Double getInstantaneousElectircalEnergyChange() {
        return 0.0;
    }

    @Override
    public Double getInstantaneousKineticEnergyChange() {
        return 0.0;
    }

    @Override
    public Double getInstantaneousPotentialEnergyChange() {
        return 0.0;
    }

    @Override
    public void cycle(Double length) {
        acceleration.set((getEngineForceAtWheels() + getMotorForceAtWheels() - getRollingResistance() - getAirResistance()) / params.getMass());
        velocity.set(Math.max(velocity.get() + acceleration.get() * length, 0.0));
        distance.set(distance.get() + velocity.get() * length);
    }

    @Override
    public void reset() {
        acceleration = new Recorder(0.0);
        velocity = new Recorder(0.0);
        distance = new Recorder(0.0);
    }

    private Double getEngineForceAtWheels() {
        return getEngineTorque() / params.getTireRadius();
    }

    private Double getMotorForceAtWheels() {
        return getMotorTorque() / params.getTireRadius();
    }

    private Double getCarDownForce() {
        return params.getMass() * params.getGravity();
    }

    public static class Parameters {
        // The effective radius of the tires
        private Double tireRadius = .336; // m

        // The density of air at STP
        private Double airDensity = 1.1; // kg / m^3

        // The coefficient of air resistance of the 2013 Chevy Malibu
        private Double airDragCoeff = .295;

        // The coefficient of rolling resistance of the tires on the road
        private Double rollingCoeff = .01;

        // The frontal area of the 2013 Chevy Malibu (i.e. the cross sectional area)
        private Double frontalArea = 2.295; // m^2

        // The weight of the UW EcoCAR 2013 Chevy Malibu
        private Double mass = 2250.0; // kg

        // Gravitational Acceleration on Earth
        private Double gravity = 9.81; // m/s^2

        // Create a lookup table defining trq at the wheels from the engine
        // for a given gas pedal position and wheel rpm.
        // In the text file, the first row is the pedal positions, the first column is the rpm values.
        private LookUpTable2D gasRpmTrqEngine;
        private LookUpTable2D gasRpmTrqMotor;

        public Parameters() throws Exception {
            gasRpmTrqEngine = new LookUpTable2D(new File("lookUpTables/gasRpmTrqEngine.txt"));
            gasRpmTrqMotor = new LookUpTable2D(new File("lookUpTables/gasRpmTrqMotor.txt"));
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

        public Double getAirDragCoeff() {
            return airDragCoeff;
        }

        public void setAirDragCoeff(Double airDragCoeff) {
            this.airDragCoeff = airDragCoeff;
        }

        public Double getRollingCoeff() {
            return rollingCoeff;
        }

        public void setRollingCoeff(Double rollingCoeff) {
            this.rollingCoeff = rollingCoeff;
        }

        public Double getFrontalArea() {
            return frontalArea;
        }

        public void setFrontalArea(Double frontalArea) {
            this.frontalArea = frontalArea;
        }

        public Double getMass() {
            return mass;
        }

        public void setMass(Double mass) {
            this.mass = mass;
        }

        public Double getGravity() {
            return gravity;
        }

        public void setGravity(Double gravity) {
            this.gravity = gravity;
        }

        public Double getEngineTorque(Double gasPedalPosition, Double rpm) {
            return gasRpmTrqEngine.getValue(gasPedalPosition, rpm);
        }

        public void setEngineTorqueMap(File gasRpmTrq) throws Exception {
            this.gasRpmTrqEngine = new LookUpTable2D(gasRpmTrq);
        }

        public Double getMotorTorque(Double gasPedalPosition, Double rpm) {
            return gasRpmTrqMotor.getValue(gasPedalPosition, rpm);
        }

        public void setMotorTorqueMap(File gasRpmTrq) throws Exception {
            this.gasRpmTrqMotor = new LookUpTable2D(gasRpmTrq);
        }
    }
}
