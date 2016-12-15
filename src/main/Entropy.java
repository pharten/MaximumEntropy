package main;

public class Entropy {
	
	static double[] f = {1.0, 2.0, 3.0, 4.0, 5.0, 6.0};
	static double[] g = {1.1, 1.2, 1.3, 1.4, 1.5, 1.6};
	static int[] occupyf = {0, 0, 0, 0, 0, 1};
	static int[] occupyg = {0, 1, 0, 0, 0, 0};
	
	static double[] probdist = {0.1666, 0.1666, 0.1666, 0.1666, 0.1666, 0.1667};

	public static void main(String[] args) {
		
		Descriptor d1 = new Descriptor(f, occupyf);
		
		System.out.println("d1 maxent = "+maximizeByU(d1, probdist));
		for (int i=0; i<probdist.length; i++) {
			System.out.println("probdist["+i+"] = "+probdist[i]);
		}
		
		Descriptor d2 = new Descriptor(g, occupyg);
		
		System.out.println("d2 maxent = "+maximizeByU(d2, probdist));
		for (int i=0; i<probdist.length; i++) {
			System.out.println("probdist["+i+"] = "+probdist[i]);
		}
		
	}
	
	public static double maximizeByU(Descriptor d, double[] probdist) {
		
		double[] prob = new double[probdist.length];
		double calcf = 0.0;
		double entropy = 0.0;
		double maxent = 0.0;
		
//		d.setDavg(4.5);
		System.out.println("descriptor average = "+d.getDavg());
		
		double umin = -40.0/d.getDavg();
		double umax = 40.0/d.getDavg();
		double uinc = 0.0001*(umax-umin);
		double partition = 0.0;
		
		double u = umin;
		while (u<=umax) {
			
			partition = 0.0;
			for (int i=0; i<prob.length; i++) {
				prob[i] = Math.exp(-u*d.get(i)) ;
				partition += prob[i];
			}
			
			calcf = 0.0;
			for (int i=0; i<prob.length; i++) {
				prob[i] /= partition;
				calcf += prob[i]*d.get(i);
			}
//			System.out.println(" u = "+u+", avgf = "+avgf+", calcf = "+calcf);
			
			double davg = d.getDavg();
			if (Math.abs(calcf-davg)<=0.001*davg) {
//				System.out.println(" calcf = "+calcf);
				entropy = 0.0;
				for (int i=0; i<prob.length; i++) {
					if (prob[i]>0.0) entropy += - prob[i] * Math.log(prob[i]);
				}
				if (entropy>maxent) {
					maxent = entropy;
					for (int i=0; i<prob.length; i++) {
						probdist[i] = prob[i];
					}
				}
//				System.out.println("entropy = "+entropy+", u= "+u);
				System.out.println(" avg = "+davg+", calcf = "+calcf+", entropy = "+entropy+", u= "+u);
			}
			
			u += uinc;
		}
		
		return maxent;
	}
	
	public static double maximizeByF(Descriptor d, double[] probdist) {
		
		double[] prob = new double[probdist.length];
		double calcf = 0.0;
		double maxent = 0.0;
		double avgf = d.getDavg();
		double partition = 0.0;
		
//		d.setDavg(4.5);
		System.out.println("descriptor average = "+d.getDavg());
				
		double minf = f[0];
		double maxf = f[f.length-1];
		for (int i=0; i<prob.length; i++) {
			if (minf > f[i]) minf = f[i];
			if (maxf < f[i]) maxf = f[i];
		}
		
		double mindiff = 0.0001*(maxf-minf);

		double finc = 0.0001*(maxf-minf);
		double x = minf;
	  	while (x<maxf) {
			
			calcf = 0.0;
			partition = 0.0;
//			double u = - Math.log(x);
//			double u = 20.0*(x-minf) /(maxf-minf) - 10.0;  
//			double u = (maxf-x)/(maxf-minf);
			double u = ((maxf-x)/(maxf-minf))*(1.0/(maxf-minf));
			for (int i=0; i<prob.length; i++) { 
				prob[i] = Math.exp(-u*f[i]);
				partition += prob[i]; 
			}
			for (int i=0; i<prob.length; i++) {  
				prob[i] /= partition;  
				calcf += f[i] * prob[i];
			}
			
			double diff = Math.abs(calcf-avgf);
			System.out.println("x = "+x+", u = "+u+", favg = "+avgf+", fcalc = "+calcf+", diff = "+diff+", mindiff = "+mindiff);
			if (diff < mindiff) {
				mindiff = diff;
				double lnZ = Math.log(partition);
				maxent = lnZ + u * avgf;
				for (int i=0; i<prob.length; i++) {
//					prob[i] /= partition;
					probdist[i] =prob[i];
				}
				System.out.println("x = "+x+", u = "+u+", fcalc = "+calcf+", Z = "+partition+", lnZ = "+lnZ+", entropy = "+maxent);
			}
			
			x += finc;
		}

		return maxent;
	}
	

}
