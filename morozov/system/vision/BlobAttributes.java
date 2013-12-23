// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.vision;

class BlobAttributes {
	public long time;
	public int x1;
	public int x2;
	public int y1;
	public int y2;
	public double[][] spectrum;
	public BlobAttributes(long t, int x11, int x12, int y11, int y12, double[][] histograms) {
		time= t;
		x1= x11;
		x2= x12;
		y1= y11;
		y2= y12;
		spectrum= histograms;
	}
}
