// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.frames.data;

import morozov.system.kinect.frames.data.interfaces.*;

import java.io.Serializable;

public class PlayerDimensionsChange implements PlayerDimensionsChangeInterface, Serializable {
	//
	protected boolean areInitialized= false;
	//
	protected float changeOfMinimalX= 0;
	protected float changeOfMaximalX= 0;
	protected float changeOfMinimalY= 0;
	protected float changeOfMaximalY= 0;
	protected float changeOfMinimalZ= 0;
	protected float changeOfMaximalZ= 0;
	//
	///////////////////////////////////////////////////////////////
	//
	public PlayerDimensionsChange() {
	}
	//
	/////////////////////////////////////////////////////////////
	//
	public void subtract(PlayerDimensionsInterface left, PlayerDimensionsInterface right) {
		if (left.areInitialized_Depth3D_Bounds() && right.areInitialized_Depth3D_Bounds()) {
			areInitialized= true;
		} else {
			areInitialized= false;
		};
		changeOfMinimalX= left.getMinimalX_3D() - right.getMinimalX_3D();
		changeOfMaximalX= left.getMaximalX_3D() - right.getMaximalX_3D();
		changeOfMinimalY= left.getMinimalY_3D() - right.getMinimalY_3D();
		changeOfMaximalY= left.getMaximalY_3D() - right.getMaximalY_3D();
		changeOfMinimalZ= left.getMinimalZ_3D() - right.getMinimalZ_3D();
		changeOfMaximalZ= left.getMaximalZ_3D() - right.getMaximalZ_3D();
	}
	//
	public void subtract(PlayerDimensionsChangeInterface left, PlayerDimensionsChangeInterface right) {
		if (left.areInitialized() && right.areInitialized()) {
			areInitialized= true;
		} else {
			areInitialized= false;
		};
		changeOfMinimalX= left.getChangeOfMinimalX() - right.getChangeOfMinimalX();
		changeOfMaximalX= left.getChangeOfMaximalX() - right.getChangeOfMaximalX();
		changeOfMinimalY= left.getChangeOfMinimalY() - right.getChangeOfMinimalY();
		changeOfMaximalY= left.getChangeOfMaximalY() - right.getChangeOfMaximalY();
		changeOfMinimalZ= left.getChangeOfMinimalZ() - right.getChangeOfMinimalZ();
		changeOfMaximalZ= left.getChangeOfMaximalZ() - right.getChangeOfMaximalZ();
	}
	//
	/////////////////////////////////////////////////////////////
	//
	public boolean areInitialized() {
		return areInitialized;
	}
	public float getChangeOfMinimalX() {
		return changeOfMinimalX;
	}
	public float getChangeOfMaximalX() {
		return changeOfMaximalX;
	}
	public float getChangeOfMinimalY() {
		return changeOfMinimalY;
	}
	public float getChangeOfMaximalY() {
		return changeOfMaximalY;
	}
	public float getChangeOfMinimalZ() {
		return changeOfMinimalZ;
	}
	public float getChangeOfMaximalZ() {
		return changeOfMaximalZ;
	}
}
