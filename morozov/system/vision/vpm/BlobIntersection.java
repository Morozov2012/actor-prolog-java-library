// (c) 2013-2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm;

public class BlobIntersection {
	//
	protected int commonAreaSize;
	protected int index1;
	protected int index2;
	protected boolean isStrong;
	//
	///////////////////////////////////////////////////////////////
	//
	public BlobIntersection(int size, int n1, int n2, boolean mode) {
		commonAreaSize= size;
		index1= n1;
		index2= n2;
		isStrong= mode;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public int getCommonAreaSize() {
		return commonAreaSize;
	}
	public int getIndex1() {
		return index1;
	}
	public int getIndex2() {
		return index2;
	}
	public boolean isStrong() {
		return isStrong;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public String toString() {
		return	String.format("%d",commonAreaSize) + ": " +
			String.format("%d",index1) +
			" ^ " +
			String.format("%d",index2);
	}
}
