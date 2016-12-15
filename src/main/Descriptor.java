package main;

import java.util.Vector;

public class Descriptor extends Vector<Double> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	double davg;

	public Descriptor(double[] d, double davg) {
		for (int i=0; i<d.length; i++) {
			this.add(d[i]);
		}
		this.davg = davg;
	}
	
	public Descriptor(double[] d, int[] occupy) {
		for (int i=0; i<d.length; i++) {
			this.add(d[i]);
		}
		this.calculateAvg(occupy);
	}
	
	public Descriptor(Vector<Double> d, double davg) {
		this.addAll(d);
		this.davg = davg;
	}
	
	public Descriptor(Vector<Double> d, int[] occupy) {
		this.addAll(d);
		this.calculateAvg(occupy);
	}
	
	public void calculateAvg(int[] occupy) {
		int count = 0;
		int totcnt = 0;
		davg = 0.0;

		for (int i=0; i<occupy.length; i++ ) {
			count = occupy[i];
			davg += count * this.get(i);
			totcnt += count;
		}
		if (totcnt!=0) {
			davg /= totcnt;
		} else { // set all occupy to 1 and calculate
			for (int i=0; i<occupy.length; i++ ) {
				count = 1;
				davg += count * this.get(i);
				totcnt += count;
			}
			davg /= totcnt;
		}
		
	}

	public double getDavg() {
		return davg;
	}

	public void setDavg(double davg) {
		this.davg = davg;
	}

}
