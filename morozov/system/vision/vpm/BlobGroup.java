// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm;

import morozov.system.vision.vpm.converters.*;

public class BlobGroup {
	//
	protected BlobType type;
	protected BlobAttributes[] attributeArray;
	//
	///////////////////////////////////////////////////////////////
	//
	public BlobGroup(
			BlobType givenType,
			BlobAttributes[] givenAttributeArray) {
		type= givenType;
		attributeArray= givenAttributeArray;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public BlobType getType() {
		return type;
	}
	public void setType(BlobType t) {
		type= t;
	}
	public BlobAttributes[] getAttributeArray() {
		return attributeArray;
	}
	public void setAttributeArray(BlobAttributes[] array) {
		attributeArray= array;
	}
	public int size() {
		return attributeArray.length;
	}
}
