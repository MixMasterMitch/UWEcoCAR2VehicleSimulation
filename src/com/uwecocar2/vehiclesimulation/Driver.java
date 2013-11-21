package com.uwecocar2.vehiclesimulation;

public interface Driver {
    public Double getGasPedalPosition(Double time);

    public Double getBrakePedalPosition(Double time);
    
    public String getName();
}
