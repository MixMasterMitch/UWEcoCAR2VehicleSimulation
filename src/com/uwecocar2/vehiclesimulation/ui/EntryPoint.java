package com.uwecocar2.vehiclesimulation.ui;

import com.uwecocar2.vehiclesimulation.*;
import com.uwecocar2.vehiclesimulation.utils.LookUpTable1D;
import com.uwecocar2.vehiclesimulation.utils.LookUpTable2D;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;

public class EntryPoint {

    public static void main(String[] args) throws Exception {
        DiscreteStepSimulator simulator = new DiscreteStepSimulator(
                new DriveCycle(new File("driveCycles/simpleDrive.txt")),
                new PlantModel(new PlantModel.Parameters()),
                DiscreteStepSimulator.DEFUALT_TIME_STEP);
    	SimulationWindow window = new SimulationWindow(simulator);
    }
}
