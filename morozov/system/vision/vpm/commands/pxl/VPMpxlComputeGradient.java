// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm.commands.pxl;

import morozov.system.vision.vpm.*;
import morozov.system.vision.vpm.commands.*;
import morozov.system.vision.vpm.converters.*;

import java.util.Arrays;

public class VPMpxlComputeGradient extends VPM_FrameCommand {
	//
	protected GradientComputationMode mode;
	//
	public VPMpxlComputeGradient(GradientComputationMode m) {
		mode= m;
	}
	//
	public void execute(VPM vpm) {
		int imageWidth= vpm.getOperationalImageWidth();
		int imageHeight= vpm.getOperationalImageHeight();
		int[] operationalMatrix= vpm.getOperationalMatrix();
		int[] updatedMatrix= Arrays.copyOf(operationalMatrix,operationalMatrix.length);
		boolean isModulusMode= mode.isModulusMode();
		for (int y=0; y < imageHeight; y++) {
			for (int x=0; x < imageWidth; x++) {
//---------------------------------------------------------------------
int index0= imageWidth * y + x;
int v0= operationalMatrix[index0];
int maximumValue= 0;
int maximumDirection= 0;
boolean isFirstElement= true;
for (int direction=0; direction <= 7; direction++) {
	int h= 0;
	int w= 0;
	switch (direction) {
	case 0:
		h= 1;
		w= 0;
		break;
	case 1:
		h= 1;
		w= 1;
		break;
	case 2:
		h= 0;
		w= 1;
		break;
	case 3:
		h= -1;
		w= 1;
		break;
	case 4:
		h= -1;
		w= 0;
		break;
	case 5:
		h= -1;
		w= -1;
		break;
	case 6:
		h= 0;
		w= -1;
		break;
	case 7:
		h= 1;
		w= -1;
		break;
	};
	int yy= y + h;
	if (yy < 0 || yy >= imageHeight) {
		continue;
	};
	int xx= x + w;
	if (xx < 0 || xx >= imageWidth) {
		continue;
	};
	int index1= imageWidth * yy + xx;
	int v1= operationalMatrix[index1];
	int delta= v1 - v0;
	if (isModulusMode) {
		if (delta < 0) {
			delta= -delta;
		}
	};
	if (isFirstElement) {
		maximumValue= delta;
		maximumDirection= direction;
		isFirstElement= false;
	} else {
		if (maximumValue < delta) {
			maximumValue= delta;
			maximumDirection= direction;
		}
	}
};
if (isModulusMode) {
	updatedMatrix[index0]= maximumValue;
} else {
	updatedMatrix[index0]= maximumDirection * 32;
}
//---------------------------------------------------------------------
			}
		};
		vpm.setOperationalMatrix(updatedMatrix);
	}
}
