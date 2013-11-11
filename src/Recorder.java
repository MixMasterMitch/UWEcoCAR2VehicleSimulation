import java.util.ArrayList;
import java.util.Iterator;

public class Recorder implements Iterable<Double>{
	private ArrayList<Double> history;
	private double max;
	private double min;
	
	public Recorder() {
		this.history = new ArrayList<Double>();
		this.max = Double.MIN_VALUE;
		this.min = Double.MAX_VALUE;
	}
	
	public Recorder(double value) {
		this();
		this.set(value);
	}
	
	/**
	 * Get the most recent value
	 */
	public double get() {
		return this.history.get(this.history.size() - 1);
	}
	
	/**
	 * Store the current value
	 */
	public void set(double value) {
		if (value > this.max) {
			this.max = value;
		}
		if (value < this.min) {
			this.min = value;
		}
		history.add(value);
	}
	
	/**
	 * Get the size of the record
	 */
	public int size() {
		return history.size();
	}
	
	/**
	 * Return the max value recorded
	 */
	public double max() {
		if (history.isEmpty()) {
			throw new IndexOutOfBoundsException();
		}
		return this.max;
	}
	
	/**
	 * Return the minimum value recorded
	 */
	public double min() {
		if (history.isEmpty()) {
			throw new IndexOutOfBoundsException();
		}
		return this.min;
	}

	@Override
	public Iterator<Double> iterator() {
		return history.iterator();
	}
}