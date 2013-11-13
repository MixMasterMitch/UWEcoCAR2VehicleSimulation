import java.util.*;

public class LookUpTable1D {
    private NavigableMap<Double, Double> data;

    public LookUpTable1D() {
        this.data = new TreeMap<Double, Double>();
    }

    public LookUpTable1D(Map<Double, Double> data) {
        this.data = new TreeMap<Double, Double>(data);
    }

    /**
     * Determines if the exact key given is in the lookup table (i.e. no interpolation required)
     */
    public boolean inTable(Double x) {
        return data.get(x) != null;
    }

    /**
     * Adds the given key and value to the lookup table
     */
    public void addEntry(Double x, Double y) {
        data.put(x, y);
    }

    /**
     * Interpolates (if needed) to find the value that best fits the given key.
     */
    public Double getValue(Double x) {
        if (inTable(x)) { // We have the exact value
            return data.get(x);
        }

        Map.Entry<Double, Double> beforeEntry = data.lowerEntry(x);
        Map.Entry<Double, Double> afterEntry = data.higherEntry(x);

        if (beforeEntry == null) {
            // TODO: make this work with negative values
            beforeEntry = new AbstractMap.SimpleEntry<Double, Double>(0.0, 0.0);
        }
        if (afterEntry == null) {
            return beforeEntry.getValue();
        }

        // (y2 - y1 / x2 - x1)(x - x1) + y1
        return (afterEntry.getValue() - beforeEntry.getValue()) / (afterEntry.getKey() - beforeEntry.getKey()) * (x - beforeEntry.getKey()) + beforeEntry.getValue();
    }
    
    /**
     * Determines if the key is contained within the upper and lower bounds of the table
     */
    public boolean isIn(Double x) {
    	return inTable(x) || (data.lowerEntry(x) != null && data.higherEntry(x) != null);
    }

}
