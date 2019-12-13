// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm.commands.blb;

import morozov.system.vision.vpm.*;
import morozov.system.vision.vpm.commands.*;
import morozov.system.vision.vpm.converters.*;

public class VPMblbFillBlobs extends VPM_FrameCommand {
	//
	public VPMblbFillBlobs() {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void execute(VPM vpm) {
		BlobGroup group= vpm.getRecentBlobGroup();
		if (group==null) {
			return;
		};
		BlobAttributes[] attributeArray= group.getAttributeArray();
		int numberOfBlobs= attributeArray.length;
		if (numberOfBlobs <= 0) {
			return;
		};
		boolean[] foregroundMask= vpm.getForegroundMask();
		int width= vpm.getOperationalImageWidth();
		int height= vpm.getOperationalImageHeight();
		for (int k=0; k < numberOfBlobs; k++) {
			BlobAttributes attributes= attributeArray[k];
			fillBlob(attributes,foregroundMask,width,height);

		};
		boolean[] contourPixels= VisionUtils.contourForeground(foregroundMask,width,height);
		for (int k=0; k < numberOfBlobs; k++) {
			BlobAttributes attributes= attributeArray[k];
			long frameNumber= attributes.getFrameNumber();
			long timeInMilliseconds= attributes.getTimeInMilliseconds();
			BlobType type= attributes.getType();
			int x1= attributes.getX1();
			int x2= attributes.getX2();
			int y1= attributes.getY1();
			int y2= attributes.getY2();
			long numberOfPixels= 0;
			int centroidX= 0;
			int centroidY= 0;
			long contourLength= 0;
			for (int xx=x1; xx <= x2; xx++) {
				for (int yy=y1; yy <= y2; yy++) {
					int index1= width * yy + xx;
					if (foregroundMask[index1]) {
						numberOfPixels++;
						centroidX+= xx;
						centroidY+= yy;
					};
					if (contourPixels[index1]) {
						contourLength++;
					}
				}
			};
			centroidX= (int)(centroidX / numberOfPixels);
			centroidY= (int)(centroidY / numberOfPixels);
			BlobAttributes blobAttributes= new BlobAttributes(
				frameNumber,
				timeInMilliseconds,
				type,
				x1,
				x2,
				y1,
				y2,
				centroidX,
				centroidY,
				numberOfPixels,
				contourLength);
			attributeArray[k]= blobAttributes;
		}
	}
	protected void fillBlob(BlobAttributes attributes, boolean[] foregroundMask, int width, int height) {
		int x1= attributes.getX1();
		int x2= attributes.getX2();
		int y1= attributes.getY1();
		int y2= attributes.getY2();
		TwoPassLoop: for (int n=1; n <= 2; n++) {
			boolean foregroundIsUpdated= false;
			for (int yy= y1; yy <= y2; yy++) {
				int leftBound= width;
				for (int xx=x1; xx <= x2; xx++) {
					int point1= width * yy + xx;
					if (foregroundMask[point1]) {
						leftBound= xx;
						break;
					}
				};
				int rightBound= -1;
				for (int xx=x2; xx >= x1; xx--) {
					int point1= width * yy + xx;
					if (foregroundMask[point1]) {
						rightBound= xx;
						break;
					}
				};
				for (int xx=leftBound; xx <= rightBound; xx++) {
					int point1= width * yy + xx;
					if (!foregroundMask[point1]) {
						foregroundMask[point1]= true;
						foregroundIsUpdated= true;
					}
				}
			};
			if (!foregroundIsUpdated && n==2) {
				break TwoPassLoop;
			};
			for (int xx= x1; xx <= x2; xx++) {
				int lowerBound= height;
				for (int yy=y1; yy <= y2; yy++) {
					int point1= width * yy + xx;
					if (foregroundMask[point1]) {
						lowerBound= yy;
						break;
					}
				};
				int upperBound= -1;
				for (int yy=y2; yy >= y1; yy--) {
					int point1= width * yy + xx;
					if (foregroundMask[point1]) {
						upperBound= yy;
						break;
					}
				};
				for (int yy=lowerBound; yy <= upperBound; yy++) {
					int point1= width * yy + xx;
					foregroundMask[point1]= true;
				}
			}
		}
	}
}
