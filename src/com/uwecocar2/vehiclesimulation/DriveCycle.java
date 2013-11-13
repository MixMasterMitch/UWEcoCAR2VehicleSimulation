package com.uwecocar2.vehiclesimulation;

import com.uwecocar2.vehiclesimulation.utils.LookUpTable1D;

import java.io.File;

public class DriveCycle implements Driver {
    private LookUpTable1D gasPedalPositions;
    private LookUpTable1D brakePedalPosition;

    public DriveCycle(File file) throws Exception {
        gasPedalPositions = new LookUpTable1D(file, 0, 1);
        brakePedalPosition = new LookUpTable1D(file, 0, 2);
    }

    public Double getGasPedalPosition(Double time) {
        if (!gasPedalPositions.isIn(time)) {
            return null;
        }
        return gasPedalPositions.getValue(time);
    }

    public Double getBrakePedalPosition(Double time) {
        if (!brakePedalPosition.isIn(time)) {
            return null;
        }
        return brakePedalPosition.getValue(time);
    }
}
