package main;

import java.util.Iterator;
import java.util.Vector;

public class Descriptor extends Vector<DescriptorCell> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double davg;
	private double lowerBound;
	private double upperBound;
	private int numCells;

	public Descriptor(double lowerBound, double upperBound, int numCells) {
		double inc;
		double lower;
		double upper;
		
//		if (numCells==1) {
//			inc = (upperBound-lowerBound)/numCells;
//		} else {
//			// recalculated to allow cell walls on both sides of lower and upper bounds
//			inc = (upperBound-lowerBound)/(numCells-1);
//			lowerBound = lowerBound - 0.5 * inc;
//			upperBound = upperBound + 0.5 * inc;
//		}
		
		this.numCells = numCells;
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
		
		inc = (upperBound-lowerBound)/numCells;
		
		for (int i=0; i<numCells; i++) {
			lower = lowerBound + i * inc;
			upper = lower + inc;
			if (i==numCells-1) upper = upperBound; // last iteration
			
			DescriptorCell cell = new DescriptorCell(lower, upper);
			this.add(cell);
			this.davg += cell.getAvg();
		}
		this.davg /= this.size();
		
	}
	
	public void addValue(double value) {
		DescriptorCell cell;
		
		if (value<this.lowerBound||this.upperBound<value) throw new Error(" Value out of range");
		
		Iterator<DescriptorCell> iter = this.iterator();
		while (iter.hasNext()) {
			cell = iter.next();
            if (value<=cell.getUpper()) {
            	cell.add(value);
            	break; // jump out of loop
            }
		}
	}

	public double calcDavg() {
		davg = 0;
		int totcnt = 0;
		DescriptorCell cell;
		
		Iterator<DescriptorCell> iter = this.iterator();
		while (iter.hasNext()) {
			cell = iter.next();
			int count = cell.getN();
			davg += count * cell.getAvg();
			totcnt += count;
		}
		davg /= totcnt;
		return davg;
	}
	
	public double getDavg() {
		return davg;
	}
	
	public double getLowerBound() {
		return lowerBound;
	}

	public double getUpperBound() {
		return upperBound;
	}

	public int getNumCells() {
		return numCells;
	}
	
}
