// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.frames.data.converters;

import edu.ufl.digitalworlds.j4k.Skeleton;
import edu.ufl.digitalworlds.j4k.J4K1;
import edu.ufl.digitalworlds.j4k.J4K2;
import edu.ufl.digitalworlds.j4k.J4KSDK;

import target.*;

import morozov.system.kinect.frames.data.interfaces.*;
import morozov.terms.*;

import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.Line2D;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.Locale;

public class GeneralSkeletonConverters {
	//
	protected static Term termTRACKED= new PrologSymbol(SymbolCodes.symbolCode_E_TRACKED);
	protected static Term termINFERRED= new PrologSymbol(SymbolCodes.symbolCode_E_INFERRED);
	protected static Term termUNKNOWN= new PrologSymbol(SymbolCodes.symbolCode_E_UNKNOWN);
	//
	///////////////////////////////////////////////////////////////
	//
	public static Term toTerm(GeneralSkeletonInterface skeleton) {
		return toTerm(skeleton,null,PrologEmptySet.instance);
	}
	public static Term toTerm(GeneralSkeletonInterface skeleton, SkeletonTrack track, Term result) {
		Term termIdentifier= new PrologInteger(skeleton.getIdentifier());
		Term termFrameNumber= new PrologInteger(skeleton.getFrameNumber());
		Term termTime= new PrologInteger(skeleton.getTime());
		Term termHead= jointToTerm(skeleton,track,Skeleton.HEAD);
		Term termSpine= spineToTerm(skeleton,track);
		Term termLeftArm= leftArmToTerm(skeleton,track);
		Term termRightArm= rightArmToTerm(skeleton,track);
		Term termLeftLeg= leftLegToTerm(skeleton,track);
		Term termRightLeg= rightLegToTerm(skeleton,track);
		result= new PrologSet(-SymbolCodes.symbolCode_E_right_leg,termRightLeg,result);
		result= new PrologSet(-SymbolCodes.symbolCode_E_left_leg,termLeftLeg,result);
		result= new PrologSet(-SymbolCodes.symbolCode_E_right_arm,termRightArm,result);
		result= new PrologSet(-SymbolCodes.symbolCode_E_left_arm,termLeftArm,result);
		result= new PrologSet(-SymbolCodes.symbolCode_E_spine,termSpine,result);
		result= new PrologSet(-SymbolCodes.symbolCode_E_head,termHead,result);
		result= new PrologSet(-SymbolCodes.symbolCode_E_time,termTime,result);
		result= new PrologSet(-SymbolCodes.symbolCode_E_frame,termFrameNumber,result);
		result= new PrologSet(-SymbolCodes.symbolCode_E_identifier,termIdentifier,result);
		return result;
	}
	//
	protected static Term spineToTerm(GeneralSkeletonInterface skeleton, SkeletonTrack track) {
		int arrayLength= skeleton.getNumberOfJoints();
		Term termNeck= jointToTerm(skeleton,track,Skeleton.NECK);
		Term termSpineMid= jointToTerm(skeleton,track,Skeleton.SPINE_MID);
		Term termSpineBase= jointToTerm(skeleton,track,Skeleton.SPINE_BASE);
		Term result= PrologEmptySet.instance;
		result= new PrologSet(-SymbolCodes.symbolCode_E_base,termSpineBase,result);
		result= new PrologSet(-SymbolCodes.symbolCode_E_mid,termSpineMid,result);
		if (Skeleton.SPINE_SHOULDER < arrayLength) {
			Term termSpineShoulder= jointToTerm(skeleton,track,Skeleton.SPINE_SHOULDER);
			result= new PrologSet(-SymbolCodes.symbolCode_E_shoulder,termSpineShoulder,result);
		};
		result= new PrologSet(-SymbolCodes.symbolCode_E_neck,termNeck,result);
		return result;
	}
	//
	protected static Term leftArmToTerm(GeneralSkeletonInterface skeleton, SkeletonTrack track) {
		int arrayLength= skeleton.getNumberOfJoints();
		Term termShoulderLeft= jointToTerm(skeleton,track,Skeleton.SHOULDER_LEFT);
		Term termElbowLeft= jointToTerm(skeleton,track,Skeleton.ELBOW_LEFT);
		Term termWristLeft= jointToTerm(skeleton,track,Skeleton.WRIST_LEFT);
		Term termHandLeft= jointToTerm(skeleton,track,Skeleton.HAND_LEFT);
		Term result= PrologEmptySet.instance;
		if (Skeleton.THUMB_LEFT < arrayLength) {
			Term termThumbLeft= jointToTerm(skeleton,track,Skeleton.THUMB_LEFT);
			result= new PrologSet(-SymbolCodes.symbolCode_E_thumb,termThumbLeft,result);
		};
		if (Skeleton.HAND_TIP_LEFT < arrayLength) {
			Term termHandTipLeft= jointToTerm(skeleton,track,Skeleton.HAND_TIP_LEFT);
			result= new PrologSet(-SymbolCodes.symbolCode_E_tip,termHandTipLeft,result);
		};
		result= new PrologSet(-SymbolCodes.symbolCode_E_hand,termHandLeft,result);
		result= new PrologSet(-SymbolCodes.symbolCode_E_wrist,termWristLeft,result);
		result= new PrologSet(-SymbolCodes.symbolCode_E_elbow,termElbowLeft,result);
		result= new PrologSet(-SymbolCodes.symbolCode_E_shoulder,termShoulderLeft,result);
		return result;
	}
	//
	protected static Term rightArmToTerm(GeneralSkeletonInterface skeleton, SkeletonTrack track) {
		int arrayLength= skeleton.getNumberOfJoints();
		Term termShoulderRight= jointToTerm(skeleton,track,Skeleton.SHOULDER_RIGHT);
		Term termElbowRight= jointToTerm(skeleton,track,Skeleton.ELBOW_RIGHT);
		Term termWristRight= jointToTerm(skeleton,track,Skeleton.WRIST_RIGHT);
		Term termHandRight= jointToTerm(skeleton,track,Skeleton.HAND_RIGHT);
		Term result= PrologEmptySet.instance;
		if (Skeleton.THUMB_RIGHT < arrayLength) {
			Term termThumbRight= jointToTerm(skeleton,track,Skeleton.THUMB_RIGHT);
			result= new PrologSet(-SymbolCodes.symbolCode_E_thumb,termThumbRight,result);
		};
		if (Skeleton.HAND_TIP_RIGHT < arrayLength) {
			Term termHandTipRight= jointToTerm(skeleton,track,Skeleton.HAND_TIP_RIGHT);
			result= new PrologSet(-SymbolCodes.symbolCode_E_tip,termHandTipRight,result);
		};
		result= new PrologSet(-SymbolCodes.symbolCode_E_hand,termHandRight,result);
		result= new PrologSet(-SymbolCodes.symbolCode_E_wrist,termWristRight,result);
		result= new PrologSet(-SymbolCodes.symbolCode_E_elbow,termElbowRight,result);
		result= new PrologSet(-SymbolCodes.symbolCode_E_shoulder,termShoulderRight,result);
		return result;
	}
	//
	protected static Term leftLegToTerm(GeneralSkeletonInterface skeleton, SkeletonTrack track) {
		Term termHipLeft= jointToTerm(skeleton,track,Skeleton.HIP_LEFT);
		Term termKneeLeft= jointToTerm(skeleton,track,Skeleton.KNEE_LEFT);
		Term termAnkleLeft= jointToTerm(skeleton,track,Skeleton.ANKLE_LEFT);
		Term termFootLeft= jointToTerm(skeleton,track,Skeleton.FOOT_LEFT);
		Term result= PrologEmptySet.instance;
		result= new PrologSet(-SymbolCodes.symbolCode_E_foot,termFootLeft,result);
		result= new PrologSet(-SymbolCodes.symbolCode_E_ankle,termAnkleLeft,result);
		result= new PrologSet(-SymbolCodes.symbolCode_E_knee,termKneeLeft,result);
		result= new PrologSet(-SymbolCodes.symbolCode_E_hip,termHipLeft,result);
		return result;
	}
	//
	protected static Term rightLegToTerm(GeneralSkeletonInterface skeleton, SkeletonTrack track) {
		Term termHipRight= jointToTerm(skeleton,track,Skeleton.HIP_RIGHT);
		Term termKneeRight= jointToTerm(skeleton,track,Skeleton.KNEE_RIGHT);
		Term termAnkleRight= jointToTerm(skeleton,track,Skeleton.ANKLE_RIGHT);
		Term termFootRight= jointToTerm(skeleton,track,Skeleton.FOOT_RIGHT);
		Term result= PrologEmptySet.instance;
		result= new PrologSet(-SymbolCodes.symbolCode_E_foot,termFootRight,result);
		result= new PrologSet(-SymbolCodes.symbolCode_E_ankle,termAnkleRight,result);
		result= new PrologSet(-SymbolCodes.symbolCode_E_knee,termKneeRight,result);
		result= new PrologSet(-SymbolCodes.symbolCode_E_hip,termHipRight,result);
		return result;
	}
	//
	protected static Term jointToTerm(GeneralSkeletonInterface skeleton, SkeletonTrack track, int jointCode) {
		byte state= skeleton.getJointTrackingState(jointCode);
		Term termStatus= status2term(state);
		Term result= PrologEmptySet.instance;
		if (state != Skeleton.NOT_TRACKED) {
			Term termPosition= positionToTerm(skeleton,track,jointCode);
			float orientationX= skeleton.getJointOrientationX(jointCode);
			float orientationY= skeleton.getJointOrientationY(jointCode);
			float orientationZ= skeleton.getJointOrientationZ(jointCode);
			float orientationW= skeleton.getJointOrientationW(jointCode);
			Term termOrientationX= new PrologReal(orientationX);
			Term termOrientationY= new PrologReal(orientationY);
			Term termOrientationZ= new PrologReal(orientationZ);
			Term termOrientationW= new PrologReal(orientationW);
			Term termOrientation= new PrologStructure(SymbolCodes.symbolCode_E_q,new Term[]{termOrientationX,termOrientationY,termOrientationZ,termOrientationW});
			result= new PrologSet(-SymbolCodes.symbolCode_E_orientation,termOrientation,result);
			result= new PrologSet(-SymbolCodes.symbolCode_E_position,termPosition,result);
			result= new PrologSet(-SymbolCodes.symbolCode_E_status,termStatus,result);
		} else {
			result= new PrologSet(-SymbolCodes.symbolCode_E_status,termStatus,result);
		};
		return result;
	}
	//
	protected static Term positionToTerm(GeneralSkeletonInterface skeleton, SkeletonTrack track, int jointCode) {
		byte state= skeleton.getJointTrackingState(jointCode);
		float pointX= skeleton.get3DJointX(jointCode);
		float pointY= skeleton.get3DJointY(jointCode);
		float pointZ= skeleton.get3DJointZ(jointCode);
		Term termPointX= new PrologReal(pointX);
		Term termPointY= new PrologReal(pointY);
		Term termPointZ= new PrologReal(pointZ);
		Term termPoint= new PrologStructure(SymbolCodes.symbolCode_E_p,new Term[]{termPointX,termPointY,termPointZ});
		int[] x_Depth= skeleton.getDepthX();
		int[] y_Depth= skeleton.getDepthY();
		int mapping1X= x_Depth[jointCode];
		int mapping1Y= y_Depth[jointCode];
		Term termMapping1X= new PrologInteger(mapping1X);
		Term termMapping1Y= new PrologInteger(mapping1Y);
		Term termMapping1= new PrologStructure(SymbolCodes.symbolCode_E_p,new Term[]{termMapping1X,termMapping1Y});
		Term result= PrologEmptySet.instance;
		int[] x_Color= skeleton.getColorX();
		int[] y_Color= skeleton.getColorY();
		if (x_Color != null && y_Color != null) {
			int mapping2X= x_Color[jointCode];
			int mapping2Y= y_Color[jointCode];
			Term termMapping2X= new PrologInteger(mapping2X);
			Term termMapping2Y= new PrologInteger(mapping2Y);
			Term termMapping2= new PrologStructure(SymbolCodes.symbolCode_E_p,new Term[]{termMapping2X,termMapping2Y});
			result= new PrologSet(-SymbolCodes.symbolCode_E_mapping2,termMapping2,result);
		};
		result= new PrologSet(-SymbolCodes.symbolCode_E_mapping1,termMapping1,result);
		if (track != null) {
			if (track.containsCurrentJointAccelerations()) {
				if (track.getCurrentJointAccelerationsState(jointCode)==Skeleton.TRACKED) {
					double accelerationX= track.getCurrentJointAccelerationX(jointCode);
					double accelerationY= track.getCurrentJointAccelerationY(jointCode);
					double accelerationZ= track.getCurrentJointAccelerationZ(jointCode);
					Term termAccelerationX= new PrologReal(accelerationX);
					Term termAccelerationY= new PrologReal(accelerationY);
					Term termAccelerationZ= new PrologReal(accelerationZ);
					Term termAcceleration= new PrologStructure(SymbolCodes.symbolCode_E_p,new Term[]{termAccelerationX,termAccelerationY,termAccelerationZ});
					result= new PrologSet(-SymbolCodes.symbolCode_E_acceleration,termAcceleration,result);
				}
			};
			if (track.containsCurrentJointVelocities()) {
				if (track.getCurrentJointVelocitiesState(jointCode)==Skeleton.TRACKED) {
					double velocityX= track.getCurrentJointVelocityX(jointCode);
					double velocityY= track.getCurrentJointVelocityY(jointCode);
					double velocityZ= track.getCurrentJointVelocityZ(jointCode);
					Term termVelocityX= new PrologReal(velocityX);
					Term termVelocityY= new PrologReal(velocityY);
					Term termVelocityZ= new PrologReal(velocityZ);
					Term termVelocity= new PrologStructure(SymbolCodes.symbolCode_E_p,new Term[]{termVelocityX,termVelocityY,termVelocityZ});
					result= new PrologSet(-SymbolCodes.symbolCode_E_velocity,termVelocity,result);
				}
			}
		};
		result= new PrologSet(-SymbolCodes.symbolCode_E_point,termPoint,result);
		return result;
	}
	//
	public static Term status2term(byte status) {
		switch (status) {
		case Skeleton.TRACKED:
			return termTRACKED;
		case Skeleton.INFERRED:
			return termINFERRED;
		default:
			return termUNKNOWN;
		}
	}
}
