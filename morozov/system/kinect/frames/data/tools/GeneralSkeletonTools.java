// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.frames.data.tools;

import edu.ufl.digitalworlds.j4k.Skeleton;
import edu.ufl.digitalworlds.j4k.J4K1;
import edu.ufl.digitalworlds.j4k.J4K2;
import edu.ufl.digitalworlds.j4k.J4KSDK;

import morozov.system.kinect.frames.data.*;
import morozov.system.kinect.frames.data.interfaces.*;
import morozov.system.kinect.frames.tools.*;
import morozov.system.kinect.modes.tools.*;

import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.Line2D;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Locale;

public class GeneralSkeletonTools {
	//
	protected static float thinLineWidth_DepthFrame= 2.0f;
	protected static float heavyLineWidth_DepthFrame= 10.0f;
	protected static float thinLineWidth_ColorFrame= 5.0f;
	protected static float heavyLineWidth_ColorFrame= 25.0f;
	protected static BasicStroke thinLineStroke_DepthFrame= new BasicStroke(thinLineWidth_DepthFrame,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND);
	protected static BasicStroke heavyLineStroke_DepthFrame= new BasicStroke(heavyLineWidth_DepthFrame,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND);
	protected static BasicStroke thinLineStroke_ColorFrame= new BasicStroke(thinLineWidth_ColorFrame,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND);
	protected static BasicStroke heavyLineStroke_ColorFrame= new BasicStroke(heavyLineWidth_ColorFrame,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND);
	//
	///////////////////////////////////////////////////////////////
	//
	public static GeneralSkeleton assembleSkeleton(int id, long frameNumber, long time, boolean[] flags, float[] positions, float[] orientations, byte states[], float[][] u, float[][] v, KinectFrameBaseAttributesInterface attributes) {
		GeneralSkeleton skeleton= new GeneralSkeleton();
		skeleton.setIdentifier(id);
		skeleton.setFrameNumber(frameNumber);
		skeleton.setTime(time);
		skeleton.setBaseAttributes(attributes);
		if (flags != null) {
			skeleton.setIsTracked(flags[id]);
		} else {
			return skeleton;
		};
		byte givenDeviceType= attributes.getDeviceType();
		if (positions != null) {
			float[] data;
			switch(givenDeviceType) {
			case J4KSDK.MICROSOFT_KINECT_1:
				data= new float[J4K1.NUI_SKELETON_POSITION_COUNT*3];
				System.arraycopy(positions,4+id*J4K1.NUI_SKELETON_POSITION_COUNT*3,data,0,J4K1.NUI_SKELETON_POSITION_COUNT*3);
				skeleton.setJointPositions(data);
				break;
			case J4KSDK.MICROSOFT_KINECT_2:
				data= new float[Skeleton.JOINT_COUNT*3];
				System.arraycopy(positions,4+id*J4K2.JointType_Count*3,data,0,J4K2.JointType_Count*3);
				skeleton.setJointPositions(data);
				break;
			}
		} else {
			return skeleton;
		};
		if (orientations != null) {
			float[] data;
			switch(givenDeviceType) {
			case J4KSDK.MICROSOFT_KINECT_1:
				data= new float[J4K1.NUI_SKELETON_POSITION_COUNT*4];
				System.arraycopy(orientations,id*J4K1.NUI_SKELETON_POSITION_COUNT*4,data,0,J4K1.NUI_SKELETON_POSITION_COUNT*4);
				skeleton.setJointOrientations(data);
				break;
			case J4KSDK.MICROSOFT_KINECT_2:
				data= new float[Skeleton.JOINT_COUNT*4];
				System.arraycopy(orientations,id*J4K2.JointType_Count*4,data,0,J4K2.JointType_Count*4);
				skeleton.setJointOrientations(data);
				break;
			}
		} else {
			return skeleton;
		};
		if (states != null) {
			byte[] stateData;
			switch(givenDeviceType) {
			case J4KSDK.MICROSOFT_KINECT_1:
				stateData= new byte[J4K1.NUI_SKELETON_POSITION_COUNT];
				System.arraycopy(states,id*J4K1.NUI_SKELETON_POSITION_COUNT,stateData,0,J4K1.NUI_SKELETON_POSITION_COUNT);
				skeleton.setJointTrackingStates(stateData);
				break;
			case J4KSDK.MICROSOFT_KINECT_2:
				stateData= new byte[Skeleton.JOINT_COUNT];
				System.arraycopy(states,id*J4K2.JointType_Count,stateData,0,J4K2.JointType_Count);
				skeleton.setJointTrackingStates(stateData);
				break;
			}
		} else {
			return skeleton;
		};
		int correctionX= attributes.getCorrectionX();
		int correctionY= attributes.getCorrectionY();
		skeleton.computeDepthFXY(correctionX,correctionY);
		skeleton.setIsInitialized_Depth(true);
		if (u != null && v != null) {
			skeleton.computeColorFXY(u,v,correctionX,correctionY);
			skeleton.setIsInitialized_Color(true);
		};
		return skeleton;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static void draw(GeneralSkeletonInterface skeleton, Graphics2D g2, boolean useColorFramesMode, int x0, int y0, int imageWidth, int imageHeight, int scaledImageWidth, int scaledImageHeight) {
		if(!skeleton.isTracked()) {
			return;
		};
		int[] fX;
		int[] fY;
		BasicStroke thinLineStroke;
		BasicStroke heavyLineStroke;
		if (useColorFramesMode) {
			if (!skeleton.isInitialized_Color()) {
				return;
			};
			fX= skeleton.getColorX();
			fY= skeleton.getColorY();
			thinLineStroke= thinLineStroke_ColorFrame;
			heavyLineStroke= heavyLineStroke_ColorFrame;
		} else {
			if (!skeleton.isInitialized_Depth()) {
				return;
			};
			fX= skeleton.getDepthX();
			fY= skeleton.getDepthY();
			thinLineStroke= thinLineStroke_DepthFrame;
			heavyLineStroke= heavyLineStroke_DepthFrame;
		};
		Color color= KinectColorMapTools.getPersonDefaultColor(skeleton.getIdentifier());
		g2.setColor(color);
		int arrayLength= skeleton.getNumberOfJoints();
		// 1 MAIN BODY: HIP_CENTER, SPINE, SHOULDER_CENTER, HEAD
		int state= minimalState(skeleton,Skeleton.SPINE_BASE,Skeleton.SPINE_MID);
		if (state != Skeleton.NOT_TRACKED) {
			int spineBaseX= fX[Skeleton.SPINE_BASE];
			int spineBaseY= fY[Skeleton.SPINE_BASE];
			int spineMidX= fX[Skeleton.SPINE_MID];
			int spineMidY= fY[Skeleton.SPINE_MID];
			drawLine(g2,state,x0,y0,spineBaseX,spineBaseY,spineMidX,spineMidY,imageWidth,imageHeight,scaledImageWidth,scaledImageHeight,thinLineStroke,heavyLineStroke);
		};
		if (Skeleton.SPINE_SHOULDER < arrayLength) {
			state= minimalState(skeleton,Skeleton.NECK,Skeleton.SPINE_SHOULDER);
			if (state != Skeleton.NOT_TRACKED) {
				int spineShoulderX= fX[Skeleton.SPINE_SHOULDER];
				int spineShoulderY= fY[Skeleton.SPINE_SHOULDER];
				int neckX= fX[Skeleton.NECK];
				int neckY= fY[Skeleton.NECK];
				drawLine(g2,state,x0,y0,spineShoulderX,spineShoulderY,neckX,neckY,imageWidth,imageHeight,scaledImageWidth,scaledImageHeight,thinLineStroke,heavyLineStroke);
			};
			state= minimalState(skeleton,Skeleton.SPINE_SHOULDER,Skeleton.SPINE_MID);
			if (state != Skeleton.NOT_TRACKED) {
				int spineMidX= fX[Skeleton.SPINE_MID];
				int spineMidY= fY[Skeleton.SPINE_MID];
				int spineShoulderX= fX[Skeleton.SPINE_SHOULDER];
				int spineShoulderY= fY[Skeleton.SPINE_SHOULDER];
				drawLine(g2,state,x0,y0,spineMidX,spineMidY,spineShoulderX,spineShoulderY,imageWidth,imageHeight,scaledImageWidth,scaledImageHeight,thinLineStroke,heavyLineStroke);
			}
		} else {
			state= minimalState(skeleton,Skeleton.NECK,Skeleton.SPINE_MID);
			if (state != Skeleton.NOT_TRACKED) {
				int spineMidX= fX[Skeleton.SPINE_MID];
				int spineMidY= fY[Skeleton.SPINE_MID];
				int neckX= fX[Skeleton.NECK];
				int neckY= fY[Skeleton.NECK];
				drawLine(g2,state,x0,y0,spineMidX,spineMidY,neckX,neckY,imageWidth,imageHeight,scaledImageWidth,scaledImageHeight,thinLineStroke,heavyLineStroke);
			}
		};
		state= minimalState(skeleton,Skeleton.NECK,Skeleton.HEAD);
		if (state != Skeleton.NOT_TRACKED) {
			int neckX= fX[Skeleton.NECK];
			int neckY= fY[Skeleton.NECK];
			int headX= fX[Skeleton.HEAD];
			int headY= fY[Skeleton.HEAD];
			drawLine(g2,state,x0,y0,neckX,neckY,headX,headY,imageWidth,imageHeight,scaledImageWidth,scaledImageHeight,thinLineStroke,heavyLineStroke);
		};
		// 2 LEFT ARM: SHOULDER_CENTER, SHOULDER_LEFT, ELBOW_LEFT, WRIST_LEFT, HAND_LEFT
		if (Skeleton.SPINE_SHOULDER < arrayLength) {
			state= minimalState(skeleton,Skeleton.SPINE_SHOULDER,Skeleton.SHOULDER_LEFT);
			if (state != Skeleton.NOT_TRACKED) {
				int spineShoulderX= fX[Skeleton.SPINE_SHOULDER];
				int spineShoulderY= fY[Skeleton.SPINE_SHOULDER];
				int shoulderLeftX= fX[Skeleton.SHOULDER_LEFT];
				int shoulderLeftY= fY[Skeleton.SHOULDER_LEFT];
				drawLine(g2,state,x0,y0,spineShoulderX,spineShoulderY,shoulderLeftX,shoulderLeftY,imageWidth,imageHeight,scaledImageWidth,scaledImageHeight,thinLineStroke,heavyLineStroke);
			}
		} else {
			state= minimalState(skeleton,Skeleton.NECK,Skeleton.SHOULDER_LEFT);
			if (state != Skeleton.NOT_TRACKED) {
				int neckX= fX[Skeleton.NECK];
				int neckY= fY[Skeleton.NECK];
				int shoulderLeftX= fX[Skeleton.SHOULDER_LEFT];
				int shoulderLeftY= fY[Skeleton.SHOULDER_LEFT];
				drawLine(g2,state,x0,y0,neckX,neckY,shoulderLeftX,shoulderLeftY,imageWidth,imageHeight,scaledImageWidth,scaledImageHeight,thinLineStroke,heavyLineStroke);
			}
		};
		state= minimalState(skeleton,Skeleton.SHOULDER_LEFT,Skeleton.ELBOW_LEFT);
		if (state != Skeleton.NOT_TRACKED) {
			int shoulderLeftX= fX[Skeleton.SHOULDER_LEFT];
			int shoulderLeftY= fY[Skeleton.SHOULDER_LEFT];
			int elbowLeftX= fX[Skeleton.ELBOW_LEFT];
			int elbowLeftY= fY[Skeleton.ELBOW_LEFT];
			drawLine(g2,state,x0,y0,shoulderLeftX,shoulderLeftY,elbowLeftX,elbowLeftY,imageWidth,imageHeight,scaledImageWidth,scaledImageHeight,thinLineStroke,heavyLineStroke);
		};
		state= minimalState(skeleton,Skeleton.ELBOW_LEFT,Skeleton.WRIST_LEFT);
		if (state != Skeleton.NOT_TRACKED) {
			int elbowLeftX= fX[Skeleton.ELBOW_LEFT];
			int elbowLeftY= fY[Skeleton.ELBOW_LEFT];
			int wristLeftX= fX[Skeleton.WRIST_LEFT];
			int wristLeftY= fY[Skeleton.WRIST_LEFT];
			drawLine(g2,state,x0,y0,elbowLeftX,elbowLeftY,wristLeftX,wristLeftY,imageWidth,imageHeight,scaledImageWidth,scaledImageHeight,thinLineStroke,heavyLineStroke);
		};
		state= minimalState(skeleton,Skeleton.WRIST_LEFT,Skeleton.HAND_LEFT);
		if (state != Skeleton.NOT_TRACKED) {
			int wristLeftX= fX[Skeleton.WRIST_LEFT];
			int wristLeftY= fY[Skeleton.WRIST_LEFT];
			int handLeftX= fX[Skeleton.HAND_LEFT];
			int handLeftY= fY[Skeleton.HAND_LEFT];
			drawLine(g2,state,x0,y0,wristLeftX,wristLeftY,handLeftX,handLeftY,imageWidth,imageHeight,scaledImageWidth,scaledImageHeight,thinLineStroke,heavyLineStroke);
		};
		if (Skeleton.HAND_TIP_LEFT < arrayLength) {
			state= minimalState(skeleton,Skeleton.HAND_TIP_LEFT,Skeleton.HAND_LEFT);
			if (state != Skeleton.NOT_TRACKED) {
				int handTipLeftX= fX[Skeleton.HAND_TIP_LEFT];
				int handTipLeftY= fY[Skeleton.HAND_TIP_LEFT];
				int handLeftX= fX[Skeleton.HAND_LEFT];
				int handLeftY= fY[Skeleton.HAND_LEFT];
				drawLine(g2,state,x0,y0,handTipLeftX,handTipLeftY,handLeftX,handLeftY,imageWidth,imageHeight,scaledImageWidth,scaledImageHeight,thinLineStroke,heavyLineStroke);
			}
		};
		if (Skeleton.THUMB_LEFT < arrayLength) {
			state= minimalState(skeleton,Skeleton.THUMB_LEFT,Skeleton.HAND_LEFT);
			if (state != Skeleton.NOT_TRACKED) {
				int thumbLeftX= fX[Skeleton.THUMB_LEFT];
				int thumbLeftY= fY[Skeleton.THUMB_LEFT];
				int wristLeftX= fX[Skeleton.WRIST_LEFT];
				int wristLeftY= fY[Skeleton.WRIST_LEFT];
				drawLine(g2,state,x0,y0,thumbLeftX,thumbLeftY,wristLeftX,wristLeftY,imageWidth,imageHeight,scaledImageWidth,scaledImageHeight,thinLineStroke,heavyLineStroke);
			}
		};
		// 3 RIGHT ARM: SHOULDER_CENTER, SHOULDER_RIGHT, ELBOW_RIGHT, WRIST_RIGHT, HAND_RIGHT
		if (Skeleton.SPINE_SHOULDER < arrayLength) {
			state= minimalState(skeleton,Skeleton.SPINE_SHOULDER,Skeleton.SHOULDER_RIGHT);
			if (state != Skeleton.NOT_TRACKED) {
				int spineShoulderX= fX[Skeleton.SPINE_SHOULDER];
				int spineShoulderY= fY[Skeleton.SPINE_SHOULDER];
				int shoulderRightX= fX[Skeleton.SHOULDER_RIGHT];
				int shoulderRightY= fY[Skeleton.SHOULDER_RIGHT];
				drawLine(g2,state,x0,y0,spineShoulderX,spineShoulderY,shoulderRightX,shoulderRightY,imageWidth,imageHeight,scaledImageWidth,scaledImageHeight,thinLineStroke,heavyLineStroke);
			}
		} else {
			state= minimalState(skeleton,Skeleton.NECK,Skeleton.SHOULDER_RIGHT);
			if (state != Skeleton.NOT_TRACKED) {
				int neckX= fX[Skeleton.NECK];
				int neckY= fY[Skeleton.NECK];
				int shoulderRightX= fX[Skeleton.SHOULDER_RIGHT];
				int shoulderRightY= fY[Skeleton.SHOULDER_RIGHT];
				drawLine(g2,state,x0,y0,neckX,neckY,shoulderRightX,shoulderRightY,imageWidth,imageHeight,scaledImageWidth,scaledImageHeight,thinLineStroke,heavyLineStroke);
			}
		};
		state= minimalState(skeleton,Skeleton.SHOULDER_RIGHT,Skeleton.ELBOW_RIGHT);
		if (state != Skeleton.NOT_TRACKED) {
			int shoulderRightX= fX[Skeleton.SHOULDER_RIGHT];
			int shoulderRightY= fY[Skeleton.SHOULDER_RIGHT];
			int elbowRightX= fX[Skeleton.ELBOW_RIGHT];
			int elbowRightY= fY[Skeleton.ELBOW_RIGHT];
			drawLine(g2,state,x0,y0,shoulderRightX,shoulderRightY,elbowRightX,elbowRightY,imageWidth,imageHeight,scaledImageWidth,scaledImageHeight,thinLineStroke,heavyLineStroke);
		};
		state= minimalState(skeleton,Skeleton.ELBOW_RIGHT,Skeleton.WRIST_RIGHT);
		if (state != Skeleton.NOT_TRACKED) {
			int elbowRightX= fX[Skeleton.ELBOW_RIGHT];
			int elbowRightY= fY[Skeleton.ELBOW_RIGHT];
			int wristRightX= fX[Skeleton.WRIST_RIGHT];
			int wristRightY= fY[Skeleton.WRIST_RIGHT];
			drawLine(g2,state,x0,y0,elbowRightX,elbowRightY,wristRightX,wristRightY,imageWidth,imageHeight,scaledImageWidth,scaledImageHeight,thinLineStroke,heavyLineStroke);
		};
		state= minimalState(skeleton,Skeleton.WRIST_RIGHT,Skeleton.HAND_RIGHT);
		if (state != Skeleton.NOT_TRACKED) {
			int wristRightX= fX[Skeleton.WRIST_RIGHT];
			int wristRightY= fY[Skeleton.WRIST_RIGHT];
			int handRightX= fX[Skeleton.HAND_RIGHT];
			int handRightY= fY[Skeleton.HAND_RIGHT];
			drawLine(g2,state,x0,y0,wristRightX,wristRightY,handRightX,handRightY,imageWidth,imageHeight,scaledImageWidth,scaledImageHeight,thinLineStroke,heavyLineStroke);
		};
		if (Skeleton.HAND_TIP_RIGHT < arrayLength) {
			state= minimalState(skeleton,Skeleton.HAND_TIP_RIGHT,Skeleton.HAND_RIGHT);
			if (state != Skeleton.NOT_TRACKED) {
				int handTipRightX= fX[Skeleton.HAND_TIP_RIGHT];
				int handTipRightY= fY[Skeleton.HAND_TIP_RIGHT];
				int handRightX= fX[Skeleton.HAND_RIGHT];
				int handRightY= fY[Skeleton.HAND_RIGHT];
				drawLine(g2,state,x0,y0,handTipRightX,handTipRightY,handRightX,handRightY,imageWidth,imageHeight,scaledImageWidth,scaledImageHeight,thinLineStroke,heavyLineStroke);
			}
		};
		if (Skeleton.THUMB_RIGHT < arrayLength) {
			state= minimalState(skeleton,Skeleton.THUMB_RIGHT,Skeleton.HAND_RIGHT);
			if (state != Skeleton.NOT_TRACKED) {
				int thumbRightX= fX[Skeleton.THUMB_RIGHT];
				int thumbRightY= fY[Skeleton.THUMB_RIGHT];
				int wristRightX= fX[Skeleton.WRIST_RIGHT];
				int wristRightY= fY[Skeleton.WRIST_RIGHT];
				drawLine(g2,state,x0,y0,thumbRightX,thumbRightY,wristRightX,wristRightY,imageWidth,imageHeight,scaledImageWidth,scaledImageHeight,thinLineStroke,heavyLineStroke);
			}
		};
		// 4 LEFT LEG: HIP_CENTER, HIP_LEFT, KNEE_LEFT, ANKLE_LEFT, FOOT_LEFT
		state= minimalState(skeleton,Skeleton.SPINE_BASE,Skeleton.HIP_LEFT);
		if (state != Skeleton.NOT_TRACKED) {
			int spineBaseX= fX[Skeleton.SPINE_BASE];
			int spineBaseY= fY[Skeleton.SPINE_BASE];
			int hipLeftX= fX[Skeleton.HIP_LEFT];
			int hipLeftY= fY[Skeleton.HIP_LEFT];
			drawLine(g2,state,x0,y0,spineBaseX,spineBaseY,hipLeftX,hipLeftY,imageWidth,imageHeight,scaledImageWidth,scaledImageHeight,thinLineStroke,heavyLineStroke);
		};
		state= minimalState(skeleton,Skeleton.HIP_LEFT,Skeleton.KNEE_LEFT);
		if (state != Skeleton.NOT_TRACKED) {
			int hipLeftX= fX[Skeleton.HIP_LEFT];
			int hipLeftY= fY[Skeleton.HIP_LEFT];
			int kneeLeftX= fX[Skeleton.KNEE_LEFT];
			int kneeLeftY= fY[Skeleton.KNEE_LEFT];
			drawLine(g2,state,x0,y0,hipLeftX,hipLeftY,kneeLeftX,kneeLeftY,imageWidth,imageHeight,scaledImageWidth,scaledImageHeight,thinLineStroke,heavyLineStroke);
		};
		state= minimalState(skeleton,Skeleton.KNEE_LEFT,Skeleton.ANKLE_LEFT);
		if (state != Skeleton.NOT_TRACKED) {
			int kneeLeftX= fX[Skeleton.KNEE_LEFT];
			int kneeLeftY= fY[Skeleton.KNEE_LEFT];
			int ankleLeftX= fX[Skeleton.ANKLE_LEFT];
			int ankleLeftY= fY[Skeleton.ANKLE_LEFT];
			drawLine(g2,state,x0,y0,kneeLeftX,kneeLeftY,ankleLeftX,ankleLeftY,imageWidth,imageHeight,scaledImageWidth,scaledImageHeight,thinLineStroke,heavyLineStroke);
		};
		state= minimalState(skeleton,Skeleton.ANKLE_LEFT,Skeleton.FOOT_LEFT);
		if (state != Skeleton.NOT_TRACKED) {
			int ankleLeftX= fX[Skeleton.ANKLE_LEFT];
			int ankleLeftY= fY[Skeleton.ANKLE_LEFT];
			int footLeftX= fX[Skeleton.FOOT_LEFT];
			int footLeftY= fY[Skeleton.FOOT_LEFT];
			drawLine(g2,state,x0,y0,ankleLeftX,ankleLeftY,footLeftX,footLeftY,imageWidth,imageHeight,scaledImageWidth,scaledImageHeight,thinLineStroke,heavyLineStroke);
		};
		// 5 RIGHT LEG: HIP_CENTER, HIP_RIGHT, KNEE_RIGHT, ANKLE_RIGHT, FOOT_RIGHT
		state= minimalState(skeleton,Skeleton.SPINE_BASE,Skeleton.HIP_RIGHT);
		if (state != Skeleton.NOT_TRACKED) {
			int spineBaseX= fX[Skeleton.SPINE_BASE];
			int spineBaseY= fY[Skeleton.SPINE_BASE];
			int hipRightX= fX[Skeleton.HIP_RIGHT];
			int hipRightY= fY[Skeleton.HIP_RIGHT];
			drawLine(g2,state,x0,y0,spineBaseX,spineBaseY,hipRightX,hipRightY,imageWidth,imageHeight,scaledImageWidth,scaledImageHeight,thinLineStroke,heavyLineStroke);
		};
		state= minimalState(skeleton,Skeleton.HIP_RIGHT,Skeleton.KNEE_RIGHT);
		if (state != Skeleton.NOT_TRACKED) {
			int hipRightX= fX[Skeleton.HIP_RIGHT];
			int hipRightY= fY[Skeleton.HIP_RIGHT];
			int kneeRightX= fX[Skeleton.KNEE_RIGHT];
			int kneeRightY= fY[Skeleton.KNEE_RIGHT];
			drawLine(g2,state,x0,y0,hipRightX,hipRightY,kneeRightX,kneeRightY,imageWidth,imageHeight,scaledImageWidth,scaledImageHeight,thinLineStroke,heavyLineStroke);
		};
		state= minimalState(skeleton,Skeleton.KNEE_RIGHT,Skeleton.ANKLE_RIGHT);
		if (state != Skeleton.NOT_TRACKED) {
			int kneeRightX= fX[Skeleton.KNEE_RIGHT];
			int kneeRightY= fY[Skeleton.KNEE_RIGHT];
			int ankleRightX= fX[Skeleton.ANKLE_RIGHT];
			int ankleRightY= fY[Skeleton.ANKLE_RIGHT];
			drawLine(g2,state,x0,y0,kneeRightX,kneeRightY,ankleRightX,ankleRightY,imageWidth,imageHeight,scaledImageWidth,scaledImageHeight,thinLineStroke,heavyLineStroke);
		};
		state= minimalState(skeleton,Skeleton.ANKLE_RIGHT,Skeleton.FOOT_RIGHT);
		if (state != Skeleton.NOT_TRACKED) {
			int ankleRightX= fX[Skeleton.ANKLE_RIGHT];
			int ankleRightY= fY[Skeleton.ANKLE_RIGHT];
			int footRightX= fX[Skeleton.FOOT_RIGHT];
			int footRightY= fY[Skeleton.FOOT_RIGHT];
			drawLine(g2,state,x0,y0,ankleRightX,ankleRightY,footRightX,footRightY,imageWidth,imageHeight,scaledImageWidth,scaledImageHeight,thinLineStroke,heavyLineStroke);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected static int minimalState(GeneralSkeletonInterface skeleton, int jointId1, int jointId2) {
		int state1= skeleton.getJointTrackingState(jointId1);
		int state2= skeleton.getJointTrackingState(jointId2);
		if (state1 < state2) {
			return state1;
		} else {
			return state2;
		}
	}
	//
	protected static void drawLine(Graphics2D g2, int state, int x0, int y0, int x11, int y11, int x21, int y21, int imageWidth, int imageHeight, int scaledImageWidth, int scaledImageHeight, BasicStroke thinLineStroke, BasicStroke heavyLineStroke) {
		if (XY_Tools.isLegal(x11) && XY_Tools.isLegal(x21) && XY_Tools.isLegal(y11) && XY_Tools.isLegal(x21)) {
			double ratioX= (double)scaledImageWidth / imageWidth;
			double ratioY= (double)scaledImageHeight / imageHeight;
			int x12= x0 + (int)(x11 * ratioX);
			int y12= y0 + (int)(y11 * ratioY);
			int x22= x0 + (int)(x21 * ratioX);
			int y22= y0 + (int)(y21 * ratioY);
			if (state==Skeleton.TRACKED) {
				g2.setStroke(heavyLineStroke);
			} else {
				g2.setStroke(thinLineStroke);
			};
			g2.draw(new Line2D.Double(x12,y12,x22,y22));
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static void writeSkeletonTerm(GeneralSkeletonInterface skeleton, BufferedWriter writer, Locale locale) throws IOException {
		writer.write("{");
		writer.write(String.format(locale,"identifier:%d,",skeleton.getIdentifier()));
		writer.write(String.format(locale,"frameNumber:%d,",skeleton.getFrameNumber()));
		writer.write(String.format(locale,"time:%d,",skeleton.getTime()));
		writer.write("head:");
		writeJointTerm(skeleton,writer,locale,Skeleton.HEAD);
		writer.write(",");
		writer.write("spine:");
		writeSpineTerm(skeleton,writer,locale);
		writer.write(",");
		writer.write("right_arm:");
		writeLeftArmTerm(skeleton,writer,locale);
		writer.write(",");
		writer.write("right_arm:");
		writeRightArmTerm(skeleton,writer,locale);
		writer.write(",");
		writer.write("left_leg:");
		writeLeftLegTerm(skeleton,writer,locale);
		writer.write(",");
		writer.write("right_leg:");
		writeRightLegTerm(skeleton,writer,locale);
		writer.write("}");
	}
	//
	public static void writeSpineTerm(GeneralSkeletonInterface skeleton, BufferedWriter writer, Locale locale) throws IOException {
		int arrayLength= skeleton.getNumberOfJoints();
		writer.write("{");
		writer.write("neck:");
		writeJointTerm(skeleton,writer,locale,Skeleton.NECK);
		if (Skeleton.SPINE_SHOULDER < arrayLength) {
			writer.write(",");
			writer.write("shoulder:");
			writeJointTerm(skeleton,writer,locale,Skeleton.SPINE_SHOULDER);
		};
		writer.write(",");
		writer.write("mid:");
		writeJointTerm(skeleton,writer,locale,Skeleton.SPINE_MID);
		writer.write(",");
		writer.write("base:");
		writeJointTerm(skeleton,writer,locale,Skeleton.SPINE_BASE);
		writer.write("}");
	}
	//
	public static void writeLeftArmTerm(GeneralSkeletonInterface skeleton, BufferedWriter writer, Locale locale) throws IOException {
		int arrayLength= skeleton.getNumberOfJoints();
		writer.write("{");
		writer.write("shoulder:");
		writeJointTerm(skeleton,writer,locale,Skeleton.SHOULDER_LEFT);
		writer.write(",");
		writer.write("elbow:");
		writeJointTerm(skeleton,writer,locale,Skeleton.ELBOW_LEFT);
		writer.write(",");
		writer.write("wrist:");
		writeJointTerm(skeleton,writer,locale,Skeleton.WRIST_LEFT);
		writer.write(",");
		writer.write("hand:");
		writeJointTerm(skeleton,writer,locale,Skeleton.HAND_LEFT);
		if (Skeleton.HAND_TIP_LEFT < arrayLength) {
			writer.write(",");
			writer.write("tip:");
			writeJointTerm(skeleton,writer,locale,Skeleton.HAND_TIP_LEFT);
		};
		if (Skeleton.THUMB_LEFT < arrayLength) {
			writer.write(",");
			writer.write("thumb:");
			writeJointTerm(skeleton,writer,locale,Skeleton.THUMB_LEFT);
		};
		writer.write("}");
	}
	//
	public static void writeRightArmTerm(GeneralSkeletonInterface skeleton, BufferedWriter writer, Locale locale) throws IOException {
		int arrayLength= skeleton.getNumberOfJoints();
		writer.write("{");
		writer.write("shoulder:");
		writeJointTerm(skeleton,writer,locale,Skeleton.SHOULDER_RIGHT);
		writer.write(",");
		writer.write("elbow:");
		writeJointTerm(skeleton,writer,locale,Skeleton.ELBOW_RIGHT);
		writer.write(",");
		writer.write("wrist:");
		writeJointTerm(skeleton,writer,locale,Skeleton.WRIST_RIGHT);
		writer.write(",");
		writer.write("hand:");
		writeJointTerm(skeleton,writer,locale,Skeleton.HAND_RIGHT);
		if (Skeleton.HAND_TIP_RIGHT < arrayLength) {
			writer.write(",");
			writer.write("tip:");
			writeJointTerm(skeleton,writer,locale,Skeleton.HAND_TIP_RIGHT);
		};
		if (Skeleton.THUMB_RIGHT < arrayLength) {
			writer.write(",");
			writer.write("thumb:");
			writeJointTerm(skeleton,writer,locale,Skeleton.THUMB_RIGHT);
		};
		writer.write("}");
	}
	//
	public static void writeLeftLegTerm(GeneralSkeletonInterface skeleton, BufferedWriter writer, Locale locale) throws IOException {
		writer.write("{");
		writer.write("hip:");
		writeJointTerm(skeleton,writer,locale,Skeleton.HIP_LEFT);
		writer.write(",");
		writer.write("knee:");
		writeJointTerm(skeleton,writer,locale,Skeleton.KNEE_LEFT);
		writer.write(",");
		writer.write("ankle:");
		writeJointTerm(skeleton,writer,locale,Skeleton.ANKLE_LEFT);
		writer.write(",");
		writer.write("foot:");
		writeJointTerm(skeleton,writer,locale,Skeleton.FOOT_LEFT);
		writer.write("}");
	}
	//
	public static void writeRightLegTerm(GeneralSkeletonInterface skeleton, BufferedWriter writer, Locale locale) throws IOException {
		writer.write("{");
		writer.write("hip:");
		writeJointTerm(skeleton,writer,locale,Skeleton.HIP_RIGHT);
		writer.write(",");
		writer.write("knee:");
		writeJointTerm(skeleton,writer,locale,Skeleton.KNEE_RIGHT);
		writer.write(",");
		writer.write("ankle:");
		writeJointTerm(skeleton,writer,locale,Skeleton.ANKLE_RIGHT);
		writer.write(",");
		writer.write("foot:");
		writeJointTerm(skeleton,writer,locale,Skeleton.FOOT_RIGHT);
		writer.write("}");
	}
	//
	public static void writeJointTerm(GeneralSkeletonInterface skeleton, BufferedWriter writer, Locale locale, int jointCode) throws IOException {
		writer.write("{");
		byte state= skeleton.getJointTrackingState(jointCode);
		writer.write(String.format(locale,"status:'%s',",status2string(state)));
		float positionX= skeleton.get3DJointX(jointCode);
		float positionY= skeleton.get3DJointY(jointCode);
		float positionZ= skeleton.get3DJointZ(jointCode);
		writer.write("position:");
		writer.write(String.format(locale,"p(%f,%f,%f)",positionX,positionY,positionZ));
		writer.write(",");
		float orientationX= skeleton.getJointOrientationX(jointCode);
		float orientationY= skeleton.getJointOrientationY(jointCode);
		float orientationZ= skeleton.getJointOrientationZ(jointCode);
		float orientationW= skeleton.getJointOrientationW(jointCode);
		writer.write("orientation:");
		writer.write(String.format(locale,"q(%f,%f,%f,%f)",orientationX,orientationY,orientationZ,orientationW));
		writer.write(",");
		int[] x_Depth= skeleton.getDepthX();
		int[] y_Depth= skeleton.getDepthY();
		writer.write("mapping1:");
		int mapping1X= x_Depth[jointCode];
		int mapping1Y= y_Depth[jointCode];
		writer.write(String.format(locale,"p(%d,%d)",mapping1X,mapping1Y));
		int[] x_Color= skeleton.getColorX();
		int[] y_Color= skeleton.getColorY();
		if (x_Color != null && y_Color != null) {
			writer.write(",");
			writer.write("mapping2:");
			int mapping2X= x_Color[jointCode];
			int mapping2Y= y_Color[jointCode];
			writer.write(String.format(locale,"p(%d,%d)",mapping2X,mapping2Y));
		};
		writer.write("}");
	}
	//
	public static String status2string(byte status) {
		switch (status) {
		case Skeleton.TRACKED:
			return "TRACKED";
		case Skeleton.INFERRED:
			return "INFERRED";
		default:
			return "UNKNOWN";
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static void writeText(GeneralSkeletonInterface skeleton, BufferedWriter writer, ExportMode exportMode, Locale locale) throws IOException {
		if (exportMode==ExportMode.SKELETONS) {
			writeSkeletonTerm(skeleton,writer,locale);
			return;
		};
		writer.write("; GENERAL SKELETON:\n");
		//
		writer.write(String.format(locale,"; identifier: %s\n",skeleton.getIdentifier()));
		writer.write(String.format(locale,"; frameNumber: %s\n",skeleton.getFrameNumber()));
		writer.write(String.format(locale,"; time: %s\n",skeleton.getTime()));
		writer.write(String.format(locale,"; isTracked= %s\n",skeleton.isTracked()));
		//
		KinectFrameBaseAttributesInterface baseAttributes= skeleton.getBaseAttributes();
		//
		writer.write(String.format(locale,"; deviceType: %s\n",baseAttributes.getDeviceType()));
		writer.write(String.format(locale,"; focalLengthX: %s\n",baseAttributes.getFocalLengthX()));
		writer.write(String.format(locale,"; focalLengthY: %s\n",baseAttributes.getFocalLengthY()));
		writer.write(String.format(locale,"; depthFrameWidth: %s\n",baseAttributes.getDepthFrameWidth()));
		writer.write(String.format(locale,"; depthFrameHeight: %s\n",baseAttributes.getDepthFrameHeight()));
		writer.write(String.format(locale,"; colorFrameWidth: %s\n",baseAttributes.getColorFrameWidth()));
		writer.write(String.format(locale,"; colorFrameHeight: %s\n",baseAttributes.getColorFrameHeight()));
		//
		float[] jointPositions= skeleton.getJointPositions();
		//
		writer.write(String.format(locale,"; jointPositions: %d\n",jointPositions.length));
		for (int n=0; n < jointPositions.length; n++) {
			if (n > 0) {
				writer.write(String.format(locale,"\t%s",jointPositions[n]));
			} else {
				writer.write(String.format(locale,"%s",jointPositions[n]));
			}
		};
		writer.write("\n");
		//
		float[] jointOrientations= skeleton.getJointOrientations();
		//
		writer.write(String.format(locale,"; jointOrientations: %d\n",jointOrientations.length));
		for (int n=0; n < jointOrientations.length; n++) {
			if (n > 0) {
				writer.write(String.format(locale,"\t%s",jointOrientations[n]));
			} else {
				writer.write(String.format(locale,"%s",jointOrientations[n]));
			}
		};
		writer.write("\n");
		//
		byte[] jointTrackingStates= skeleton.getJointTrackingStates();
		//
		writer.write(String.format(locale,"; jointTrackingStates: %d\n",jointTrackingStates.length));
		for (int n=0; n < jointTrackingStates.length; n++) {
			if (n > 0) {
				writer.write(String.format(locale,"\t%s",jointTrackingStates[n]));
			} else {
				writer.write(String.format(locale,"%s",jointTrackingStates[n]));
			}
		};
		writer.write("\n");
		//
		writer.write(String.format(locale,"; isInitialized_Depth= %s\n",skeleton.isInitialized_Depth()));
		//
		int[] x_Depth= skeleton.getDepthX();
		int[] y_Depth= skeleton.getDepthY();
		//
		writer.write(String.format(locale,"; x_Depth: %d\n",x_Depth.length));
		for (int n=0; n < x_Depth.length; n++) {
			if (n > 0) {
				writer.write(String.format(locale,"\t%s",x_Depth[n]));
			} else {
				writer.write(String.format(locale,"%s",x_Depth[n]));
			}
		};
		writer.write("\n");
		//
		writer.write(String.format(locale,"; y_Depth: %d\n",y_Depth.length));
		for (int n=0; n < y_Depth.length; n++) {
			if (n > 0) {
				writer.write(String.format(locale,"\t%s",y_Depth[n]));
			} else {
				writer.write(String.format(locale,"%s",y_Depth[n]));
			}
		};
		writer.write("\n");
		//
		writer.write(String.format(locale,"; isInitialized_Color= %s\n",skeleton.isInitialized_Color()));
		//
		int[] x_Color= skeleton.getColorX();
		int[] y_Color= skeleton.getColorY();
		//
		if (x_Color != null) {
			writer.write(String.format(locale,"; x_Color: %d\n",x_Color.length));
			for (int n=0; n < x_Color.length; n++) {
				if (n > 0) {
					writer.write(String.format(locale,"\t%s",x_Color[n]));
				} else {
					writer.write(String.format(locale,"%s",x_Color[n]));
				}
			};
			writer.write("\n");
		} else {
			writer.write("; No x_Color.\n");
		};
		//
		if (y_Color != null) {
			writer.write(String.format(locale,"; y_Color: %d\n",y_Color.length));
			for (int n=0; n < y_Color.length; n++) {
				if (n > 0) {
					writer.write(String.format(locale,"\t%s",y_Color[n]));
				} else {
					writer.write(String.format(locale,"%s",y_Color[n]));
				}
			};
			writer.write("\n");
		} else {
			writer.write("; No y_Color.\n");
		};
		//
		writer.write("; End of general skeleton.\n");
	}
}
