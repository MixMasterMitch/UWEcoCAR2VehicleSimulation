import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class LookUpTable2D {

    private NavigableMap<Double, LookUpTable1D> data;

    public LookUpTable2D(Map<Double, Map<Double, Double>> data) {
        this.data = new TreeMap<Double, LookUpTable1D>();
        for(Double key : data.keySet()) {
            this.data.put(key, new LookUpTable1D(data.get(key)));
        }
    }

    // In the text file, the first row is the pedal positions, the first column is the rpm values.
    public LookUpTable2D(String file) throws Exception {
        Map<Double, LookUpTable1D> tempData = new LinkedHashMap<Double, LookUpTable1D>();

        BufferedReader br = new BufferedReader(new FileReader(file));

        // Find all of the column keys
        String[] columns = br.readLine().split("\\s+");
        for (String column : columns) {
            if (!column.equals("")) {
                tempData.put(Double.parseDouble(column), new LookUpTable1D());
            }
        }

        // Find all of the row keys and values
        String row;
        while ((row = br.readLine()) != null) {
            int rowPartsIndex = 0;
            String[] rowParts = row.split("\\s+");

            // Get to the row key
            while (rowParts[rowPartsIndex].equals("")) {
                rowPartsIndex++;
            }

            Double rowKey = Double.parseDouble(rowParts[rowPartsIndex]);
            rowPartsIndex++;

            // For each value in the row, add it to the column
            Iterator<Double> colIterator = tempData.keySet().iterator();
            while (rowPartsIndex < rowParts.length) {
                if (!rowParts[rowPartsIndex].equals("") && colIterator.hasNext()) {

                    // Add value to column
                    tempData.get(colIterator.next()).addEntry(rowKey, Double.parseDouble(rowParts[rowPartsIndex]));
                }
                rowPartsIndex++;
            }

        }

        this.data = new TreeMap<Double, LookUpTable1D>(tempData);

        br.close();
    }

    /**
     * Determines if the exact key given is in the lookup table (i.e. no interpolation required)
     */
    public boolean inTable(Double x, Double y) {
        return data.get(x) != null && data.get(x).inTable(y);
    }

    /**
     * Interpolates (if needed) to find the value that best fits the given keys.
     */
    public Double getValue(Double x, Double y) {
        if (inTable(x, y)) { // We have the exact value
            return data.get(x).getValue(y);
        }

        Map.Entry<Double, LookUpTable1D> beforeEntry = data.lowerEntry(x);
        Map.Entry<Double, LookUpTable1D> afterEntry = data.higherEntry(x);

        if (beforeEntry == null) {
            // TODO: make this work with negative values
            beforeEntry = new AbstractMap.SimpleEntry<Double, LookUpTable1D>(0.0, new LookUpTable1D());
        }
        if (afterEntry == null) {
            return beforeEntry.getValue().getValue(y);
        }

        // Get the best z value for the column before the given x and the column after the given x
        Double beforeInterp = beforeEntry.getValue().getValue(y);
        Double afterInterp = afterEntry.getValue().getValue(y);

        // (z2 - z1 / x2 - x1)(x - x1) + z1
        Double returnValue = (afterInterp - beforeInterp) / (afterEntry.getKey() - beforeEntry.getKey()) * (x - beforeEntry.getKey()) + beforeInterp;
        return returnValue;
    }
}
