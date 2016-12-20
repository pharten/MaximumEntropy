package main;

/**
 * @author pharten
 *
 */
public class DescriptorCell {

	/**
	 * 
	 */
	private double lower;
	private double upper;
	private double avg;
	private int n;
	
	public DescriptorCell(double lower, double upper) {
		this.lower = lower;
		this.upper = upper;
		this.avg = 0.5*(upper+lower);
		this.n = 1;
	}
	
	public int add(double value) {
		int n = this.n;
		int np1 = n + 1;
		this.avg = (n * avg + value)/np1;
		this.n = np1;
		return np1;
	}

	public double getAvg() {
		return avg;
	}

	public int getN() {
		return n;
	}

	public double getLower() {
		return lower;
	}

	public double getUpper() {
		return upper;
	}

}
