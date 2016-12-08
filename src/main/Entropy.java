package main;

public class Entropy {
	
	static double[] f = {1.0, 2.0, 3.0, 4.0, 5.0, 6.0};
	static int[] occupy = {0, 0, 0, 0, 0, 10};
	
	static double[] probdist = {0.1666, 0.1666, 0.1666, 0.1666, 0.1666, 0.1667};
	
	static double partition = 0.0;

	public static void main(String[] args) {
		
		System.out.println("maxent = "+maximize(probdist));
		for (int i=0; i<probdist.length; i++) {
			System.out.println("probdist["+i+"] = "+probdist[i]);
		}
		
	}
	
	public static double maximize(double[] probdist) {
		
		double[] prob = new double[probdist.length];

		partition = 0.0;
		double favg = 0.0;
		for (int i=0; i<prob.length; i++) {
			favg += occupy[i]*f[i];
			partition += occupy[i];
		}
		if (partition != 0.0) {
			favg /= partition;
		} else { // partition == 0.0, set all occupy to 1
			partition = 0.0;
			favg = 0.0;
			for (int i=0; i<prob.length; i++) {
				favg += f[i];
				partition += 1;
			}
			favg /= partition; 
		}
//		favg = 4.5;
		System.out.println("favg = "+favg);
		
		double fcalc = 0.0;
		double entropy = 0.0;
		double maxent = 0.0;
		
/*
		double pinc = 0.001;
		double pmax = 1.0;
		double p1 = 0.0;
		double p2 = 0.0;
		double p3 = 0.0;
		double p4 = 0.0;
		while (p1 <= pmax) {
			System.out.println("p1 = "+p1);
			p2 = 0;
			while (p1+p2 <= pmax) {
				p3 = 0;
				while (p1+p2+p3 <= pmax) {
					p4 = pmax - p3 - p2 - p1;
					
					fcalc = p1*f[0] + p2*f[1] + p3*f[2] + p4*f[3];
//					System.out.println("fcalc = "+fcalc);
					if (Math.abs(favg-fcalc)<0.0001*favg) {
						entropy = 0.0;
						if (0.0 < p1) entropy += -p1*Math.log(p1);
						if (0.0 < p2) entropy += -p2*Math.log(p2);
						if (0.0 < p3) entropy += -p3*Math.log(p3);
						if (0.0 < p4) entropy += -p4*Math.log(p4);
//						entropy = - p1*Math.log(p1) - p2*Math.log(p2)- p3*Math.log(p3) - p4*Math.log(p4);
						if (entropy>=maxent) {
							System.out.println("entropy = "+entropy+", fcalc = "+fcalc);
							maxent = entropy;
							probdist[0] = p1;
							probdist[1] = p2;
							probdist[2] = p3;
							probdist[3] = p4;
						}
					}
					
					p3 += pinc;
				}
				p2 += pinc;
			}
			p1 += pinc;
		}
*/
		
		double umin = -40.0/favg;
		double umax = 40.0/favg;
		double uinc = 0.001*(umax-umin);
		
		double u = umin;
		while (u<=umax) {
			
			partition = 0.0;
			for (int i=0; i<prob.length; i++) {
				prob[i] = Math.exp(-u*f[i]);
				partition += prob[i];
			}
			
			fcalc = 0.0;
			for (int i=0; i<prob.length; i++) {
				prob[i] /= partition;
				fcalc += prob[i]*f[i];
			}
//			System.out.println(" u = "+u+", favg = "+favg+", fcalc = "+fcalc);
			
			if (Math.abs(fcalc-favg)<=0.001*favg) {
//				System.out.println(" fcalc = "+fcalc);
				entropy = 0.0;
				for (int i=0; i<prob.length; i++) {
					if (0.0 < prob[i]) entropy += - prob[i] * Math.log(prob[i]);
				}
				if (entropy>maxent) {
					maxent = entropy;
					for (int i=0; i<prob.length; i++) {
						probdist[i] = prob[i];
					}
				}
//				System.out.println("entropy = "+entropy+", u= "+u);
				System.out.println(" favg = "+favg+", fcalc = "+fcalc+", entropy = "+entropy+", u= "+u);
			}
			
			u += uinc;
		}
		
/*		
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
			
			fcalc = 0.0;
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
				fcalc += f[i] * prob[i];
			}
			
			double diff = Math.abs(fcalc-favg);
			System.out.println("x = "+x+", u = "+u+", favg = "+favg+", fcalc = "+fcalc+", diff = "+diff+", mindiff = "+mindiff);
			if (diff < mindiff) {
				mindiff = diff;
				double lnZ = Math.log(partition);
				maxent = lnZ + u * favg;
				for (int i=0; i<prob.length; i++) {
//					prob[i] /= partition;
					probdist[i] =prob[i];
				}
				System.out.println("x = "+x+", u = "+u+", fcalc = "+fcalc+", Z = "+partition+", lnZ = "+lnZ+", entropy = "+maxent);
			}
			
			x += finc;
		}
*/
		return maxent;
	}
	

}
