// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.vision;

class BlobIntersection {
	public int commonAreaSize;
	public int index1;
	public int index2;
	public boolean isStrong;
	public BlobIntersection(int size, int n1, int n2, boolean mode) {
		commonAreaSize= size;
		index1= n1;
		index2= n2;
		isStrong= mode;
	}
	public String toString() {
		return	String.format("%d",commonAreaSize) + ": " +
			String.format("%d",index1) +
			" ^ " +
			String.format("%d",index2);
	}
}
