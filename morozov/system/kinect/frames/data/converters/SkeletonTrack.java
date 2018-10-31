// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.frames.data.converters;

import target.*;

import morozov.system.*;
import morozov.system.converters.*;
import morozov.system.kinect.frames.data.*;
import morozov.system.kinect.frames.data.interfaces.*;
import morozov.system.kinect.frames.data.tools.*;
import morozov.system.kinect.modes.interfaces.*;
import morozov.terms.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;
import java.io.Serializable;

public class SkeletonTrack implements Serializable {
	//
	protected int identifier;
	protected long firstFrameNumber= -1;
	protected long recentFrameNumber= -1;
	protected long beginningTime= -1;
	protected long previousTime= -1;
	protected long endTime= -1;
	//
	///////////////////////////////////////////////////////////////
	//
	protected ArrayList<SkeletonTerm> arrayOfSkeletonTerms= new ArrayList<>();
	protected HashMap<Long,Term> mapOfSkeletonTerms= new HashMap<>();
	//
	///////////////////////////////////////////////////////////////
	//
	protected byte[] currentJointTrackingStates;
	protected float[] currentJointPositions;
	//
	protected byte[] currentJointVelocitiesStates;
	protected double[] currentJointVelocities;
	//
	protected byte[] currentJointAccelerationsStates;
	protected double[] currentJointAccelerations;
	//
	protected byte[] previousJointTrackingStates;
	protected float[] previousJointPositions;
	//
	protected byte[] previousJointVelocitiesStates;
	protected double[] previousJointVelocities;
	//
	///////////////////////////////////////////////////////////////
	//
	protected SkeletonDimensionsInterface currentSkeletonDimensions;
	protected SkeletonDimensionsChangeInterface currentSkeletonDimensionsVelocities;
	protected SkeletonDimensionsChangeInterface currentSkeletonDimensionsAccelerations;
	//
	protected SkeletonDimensionsInterface previousSkeletonDimensions;
	protected SkeletonDimensionsChangeInterface previousSkeletonDimensionsVelocities;
	//
	protected static int number12= 12;
	//
	///////////////////////////////////////////////////////////////
	//
	public SkeletonTrack(int id) {
		identifier= id;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public int getIdentifier() {
		return identifier;
	}
	public long getFirstFrameNumber() {
		return firstFrameNumber;
	}
	public long getRecentFrameNumber() {
		return recentFrameNumber;
	}
	public long getBeginningTime() {
		return beginningTime;
	}
	public long getPreviousTime() {
		return previousTime;
	}
	public long getEndTime() {
		return endTime;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public ArrayList<SkeletonTerm> getArrayOfSkeletonTerms() {
		return arrayOfSkeletonTerms;
	}
	//
	public HashMap<Long,Term> getMapOfSkeletonTerms() {
		return mapOfSkeletonTerms;
	}
	//
	public Term getSkeletonTerm(long time) {
		return mapOfSkeletonTerms.get(time);
	}
	//
	public SkeletonTerm getRecentSkeletonTerm() {
		if (arrayOfSkeletonTerms.size() > 0) {
			return arrayOfSkeletonTerms.get(arrayOfSkeletonTerms.size()-1);
		} else {
			return null;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public byte[] getCurrentJointTrackingStates() {
		return currentJointTrackingStates;
	}
	public float[] getCurrentJointPositions() {
		return currentJointPositions;
	}
	public boolean containsCurrentJointPositions() {
		return	(currentJointPositions != null) &&
			(currentJointTrackingStates != null);
	}
	public float getCurrentJointX(int jointId) {
		return currentJointPositions[jointId*3+0];
	}
	public float getCurrentJointY(int jointId) {
		return currentJointPositions[jointId*3+1];
	}
	public float getCurrentJointZ(int jointId) {
		return currentJointPositions[jointId*3+2];
	}
	public byte getCurrentJointTrackingState(int jointId) {
		return currentJointTrackingStates[jointId];
	}
	public int getCurrentNumberOfJoints() {
		return currentJointTrackingStates.length;
	}
	//
	public byte[] getCurrentJointVelocitiesStates() {
		return currentJointVelocitiesStates;
	}
	public double[] getCurrentJointVelocities() {
		return currentJointVelocities;
	}
	public boolean containsCurrentJointVelocities() {
		return	(currentJointVelocities != null) &&
			(currentJointVelocitiesStates != null);
	}
	public double getCurrentJointVelocityX(int jointId) {
		return currentJointVelocities[jointId*3+0];
	}
	public double getCurrentJointVelocityY(int jointId) {
		return currentJointVelocities[jointId*3+1];
	}
	public double getCurrentJointVelocityZ(int jointId) {
		return currentJointVelocities[jointId*3+2];
	}
	public byte getCurrentJointVelocitiesState(int jointId) {
		return currentJointVelocitiesStates[jointId];
	}
	public int getCurrentNumberOfJointVelocities() {
		return currentJointVelocitiesStates.length;
	}
	//
	public byte[] getCurrentJointAccelerationsStates() {
		return currentJointAccelerationsStates;
	}
	public double[] getCurrentJointAccelerations() {
		return currentJointAccelerations;
	}
	public boolean containsCurrentJointAccelerations() {
		return	(currentJointAccelerations != null) &&
			(currentJointAccelerationsStates != null);
	}
	public double getCurrentJointAccelerationX(int jointId) {
		return currentJointAccelerations[jointId*3+0];
	}
	public double getCurrentJointAccelerationY(int jointId) {
		return currentJointAccelerations[jointId*3+1];
	}
	public double getCurrentJointAccelerationZ(int jointId) {
		return currentJointAccelerations[jointId*3+2];
	}
	public byte getCurrentJointAccelerationsState(int jointId) {
		return currentJointAccelerationsStates[jointId];
	}
	public int getCurrentNumberOfJointAccelerations() {
		return currentJointAccelerationsStates.length;
	}
	//
	public byte[] getPreviousJointTrackingStates() {
		return previousJointTrackingStates;
	}
	public float[] getPreviousJointPositions() {
		return previousJointPositions;
	}
	public byte[] getPreviousJointVelocitiesStates() {
		return previousJointVelocitiesStates;
	}
	public double[] getPreviousJointVelocities() {
		return previousJointVelocities;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public SkeletonDimensionsInterface getCurrentSkeletonDimensions() {
		return currentSkeletonDimensions;
	}
	//
	public PlayerDimensionsInterface getCurrentTotalDepthDimensions() {
		if (currentSkeletonDimensions != null) {
			return currentSkeletonDimensions.getTotalDepthDimensions();
		} else {
			return null;
		}
	}
	public PlayerDimensionsInterface getCurrentSkeletonDepthDimensions() {
		if (currentSkeletonDimensions != null) {
			return currentSkeletonDimensions.getSkeletonDepthDimensions();
		} else {
			return null;
		}
	}
	public PlayerDimensionsInterface getCurrentTotalColorDimensions() {
		if (currentSkeletonDimensions != null) {
			return currentSkeletonDimensions.getTotalColorDimensions();
		} else {
			return null;
		}
	}
	public PlayerDimensionsInterface getCurrentSkeletonColorDimensions() {
		if (currentSkeletonDimensions != null) {
			return currentSkeletonDimensions.getSkeletonColorDimensions();
		} else {
			return null;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public SkeletonDimensionsChangeInterface getCurrentSkeletonDimensionsVelocities() {
		return currentSkeletonDimensionsVelocities;
	}
	//
	public PlayerDimensionsChangeInterface getCurrentTotalDepthDimensionsVelocities() {
		if (currentSkeletonDimensionsVelocities != null) {
			return currentSkeletonDimensionsVelocities.getChangeOfTotalDepthDimensions();
		} else {
			return null;
		}
	}
	public PlayerDimensionsChangeInterface getCurrentSkeletonDepthDimensionsVelocities() {
		if (currentSkeletonDimensionsVelocities != null) {
			return currentSkeletonDimensionsVelocities.getChangeOfSkeletonDepthDimensions();
		} else {
			return null;
		}
	}
	public PlayerDimensionsChangeInterface getCurrentTotalColorDimensionsVelocities() {
		if (currentSkeletonDimensionsVelocities != null) {
			return currentSkeletonDimensionsVelocities.getChangeOfTotalColorDimensions();
		} else {
			return null;
		}
	}
	public PlayerDimensionsChangeInterface getCurrentSkeletonColorDimensionsVelocities() {
		if (currentSkeletonDimensionsVelocities != null) {
			return currentSkeletonDimensionsVelocities.getChangeOfSkeletonColorDimensions();
		} else {
			return null;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public SkeletonDimensionsChangeInterface getCurrentSkeletonDimensionsAccelerations() {
		return currentSkeletonDimensionsAccelerations;
	}
	//
	public PlayerDimensionsChangeInterface getCurrentTotalDepthDimensionsAccelerations() {
		if (currentSkeletonDimensionsAccelerations != null) {
			return currentSkeletonDimensionsAccelerations.getChangeOfTotalDepthDimensions();
		} else {
			return null;
		}
	}
	public PlayerDimensionsChangeInterface getCurrentSkeletonDepthDimensionsAccelerations() {
		if (currentSkeletonDimensionsAccelerations != null) {
			return currentSkeletonDimensionsAccelerations.getChangeOfSkeletonDepthDimensions();
		} else {
			return null;
		}
	}
	public PlayerDimensionsChangeInterface getCurrentTotalColorDimensionsAccelerations() {
		if (currentSkeletonDimensionsAccelerations != null) {
			return currentSkeletonDimensionsAccelerations.getChangeOfTotalColorDimensions();
		} else {
			return null;
		}
	}
	public PlayerDimensionsChangeInterface getCurrentSkeletonColorDimensionsAccelerations() {
		if (currentSkeletonDimensionsAccelerations != null) {
			return currentSkeletonDimensionsAccelerations.getChangeOfSkeletonColorDimensions();
		} else {
			return null;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public SkeletonDimensionsInterface getPreviousSkeletonDimensions() {
		return previousSkeletonDimensions;
	}
	//
	public PlayerDimensionsInterface getPreviousTotalDepthDimensions() {
		if (previousSkeletonDimensions != null) {
			return previousSkeletonDimensions.getTotalDepthDimensions();
		} else {
			return null;
		}
	}
	public PlayerDimensionsInterface getPreviousSkeletonDepthDimensions() {
		if (previousSkeletonDimensions != null) {
			return previousSkeletonDimensions.getSkeletonDepthDimensions();
		} else {
			return null;
		}
	}
	public PlayerDimensionsInterface getPreviousTotalColorDimensions() {
		if (previousSkeletonDimensions != null) {
			return previousSkeletonDimensions.getTotalColorDimensions();
		} else {
			return null;
		}
	}
	public PlayerDimensionsInterface getPreviousSkeletonColorDimensions() {
		if (previousSkeletonDimensions != null) {
			return previousSkeletonDimensions.getSkeletonColorDimensions();
		} else {
			return null;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public SkeletonDimensionsChangeInterface getPreviousSkeletonDimensionsVelocities() {
		return previousSkeletonDimensionsVelocities;
	}
	//
	public PlayerDimensionsChangeInterface getPreviousTotalDepthDimensionsVelocities() {
		if (previousSkeletonDimensionsVelocities != null) {
			return previousSkeletonDimensionsVelocities.getChangeOfTotalDepthDimensions();
		} else {
			return null;
		}
	}
	public PlayerDimensionsChangeInterface getPreviousSkeletonDepthDimensionsVelocities() {
		if (previousSkeletonDimensionsVelocities != null) {
			return previousSkeletonDimensionsVelocities.getChangeOfSkeletonDepthDimensions();
		} else {
			return null;
		}
	}
	public PlayerDimensionsChangeInterface getPreviousTotalColorDimensionsVelocities() {
		if (previousSkeletonDimensionsVelocities != null) {
			return previousSkeletonDimensionsVelocities.getChangeOfTotalColorDimensions();
		} else {
			return null;
		}
	}
	public PlayerDimensionsChangeInterface getPreviousSkeletonColorDimensionsVelocities() {
		if (previousSkeletonDimensionsVelocities != null) {
			return previousSkeletonDimensionsVelocities.getChangeOfSkeletonColorDimensions();
		} else {
			return null;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void appendFrame(long frameNumber, long time, GeneralSkeletonInterface skeleton, SkeletonDimensionsInterface skeletonDimensions, KinectDisplayingModeInterface displayingMode) {
		if (firstFrameNumber < 0) {
			firstFrameNumber= frameNumber;
			beginningTime= time;
		};
		if (time <= endTime) {
			return;
		};
		previousTime= endTime;
		recentFrameNumber= frameNumber;
		endTime= time;
		//
		///////////////////////////////////////////////////////
		//
		currentJointTrackingStates= skeleton.getJointTrackingStates();
		currentJointPositions= skeleton.getJointPositions();
		//
		if (previousJointTrackingStates==null) {
			currentJointVelocitiesStates= new byte[currentJointTrackingStates.length];
			currentJointVelocities= new double[currentJointPositions.length];
		} else {
			long deltaTimeInMilliseconds= endTime - previousTime;
			double deltaTimeInSeconds= deltaTimeInMilliseconds / 1000.0;
			if (deltaTimeInMilliseconds > 0) {
				for (int n=0; n < previousJointPositions.length; n++) {
					currentJointVelocities[n]= (currentJointPositions[n] - previousJointPositions[n]) / deltaTimeInSeconds;
				}
			} else {
				for (int n=0; n < previousJointPositions.length; n++) {
					currentJointVelocities[n]= 0;
				}
			};
			for (int n=0; n < previousJointTrackingStates.length; n++) {
				byte minimalState= currentJointTrackingStates[n];
				if (minimalState > previousJointTrackingStates[n]) {
					minimalState= previousJointTrackingStates[n];
				};
				currentJointVelocitiesStates[n]= minimalState;
			};
			if (previousJointVelocitiesStates==null) {
				previousJointVelocitiesStates= new byte[currentJointTrackingStates.length];
				previousJointVelocities= new double[currentJointPositions.length];
				currentJointAccelerationsStates= new byte[currentJointTrackingStates.length];
				currentJointAccelerations= new double[currentJointPositions.length];
			} else {
				if (deltaTimeInMilliseconds > 0) {
					for (int n=0; n < previousJointVelocities.length; n++) {
						currentJointAccelerations[n]= (currentJointVelocities[n] - previousJointVelocities[n]) / deltaTimeInSeconds;
					}
				} else {
					for (int n=0; n < previousJointVelocities.length; n++) {
						currentJointAccelerations[n]= 0;
					}
				};
				for (int n=0; n < previousJointVelocitiesStates.length; n++) {
					byte minimalState= currentJointVelocitiesStates[n];
					if (minimalState > previousJointVelocitiesStates[n]) {
						minimalState= previousJointVelocitiesStates[n];
					};
					currentJointAccelerationsStates[n]= minimalState;
				}
			};
			for (int n=0; n < currentJointVelocities.length; n++) {
				previousJointVelocities[n]= currentJointVelocities[n];
			};
			for (int n=0; n < currentJointVelocitiesStates.length; n++) {
				previousJointVelocitiesStates[n]= currentJointVelocitiesStates[n];
			}
		};
		//
		previousJointPositions= currentJointPositions;
		previousJointTrackingStates= currentJointTrackingStates;
		//
		///////////////////////////////////////////////////////
		//
		if (skeletonDimensions != null) {
			currentSkeletonDimensions= skeletonDimensions;
			//
			if (currentSkeletonDimensionsVelocities==null) {
				currentSkeletonDimensionsVelocities= new SkeletonDimensionsChange();
			} else {
				SkeletonDimensionsTools.subtractDimensions(currentSkeletonDimensions,previousSkeletonDimensions,currentSkeletonDimensionsVelocities);
				if (currentSkeletonDimensionsAccelerations==null) {
					currentSkeletonDimensionsAccelerations= new SkeletonDimensionsChange();
				} else {
					SkeletonDimensionsChangeTools.subtractVelocities(currentSkeletonDimensionsVelocities,previousSkeletonDimensionsVelocities,currentSkeletonDimensionsAccelerations);
				};
				previousSkeletonDimensionsVelocities= currentSkeletonDimensionsVelocities;
			};
			previousSkeletonDimensions= currentSkeletonDimensions;
		};
		//
		///////////////////////////////////////////////////////
		//
		Term term= DimensionsConverters.skeletonAndDimensionsToTerm(skeleton,skeletonDimensions,this,displayingMode);
		if (term != null) {
			arrayOfSkeletonTerms.add(new SkeletonTerm(identifier,frameNumber,time,term));
			mapOfSkeletonTerms.put(time,term);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void shorten(long minimalTime) {
		ListIterator<SkeletonTerm> iteratorOfArrayOfSkeletonTerms= arrayOfSkeletonTerms.listIterator();
		while (iteratorOfArrayOfSkeletonTerms.hasNext()) {
			SkeletonTerm skeletonTerm= iteratorOfArrayOfSkeletonTerms.next();
			long currentTime= skeletonTerm.getTime();
			if (currentTime < minimalTime) {
				iteratorOfArrayOfSkeletonTerms.remove();
				mapOfSkeletonTerms.remove(currentTime);
			} else {
				break;
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public TrackTerm toTrackTerm(boolean isComplete) {
		return new TrackTerm(
			identifier,
			firstFrameNumber,
			recentFrameNumber,
			beginningTime,
			endTime,
			toTerm(isComplete));
	}
	//
	public Term toTerm(boolean isComplete) {
		Term termIdentifier= new PrologInteger(identifier);
		Term termFirstFrameNumber= new PrologInteger(firstFrameNumber);
		Term termRecentFrameNumber= new PrologInteger(recentFrameNumber);
		Term termBeginningTime= new PrologInteger(beginningTime);
		Term termEndTime= new PrologInteger(endTime);
		Term[] skeletonTerms= new Term[arrayOfSkeletonTerms.size()];
		for (int n=0; n < arrayOfSkeletonTerms.size(); n++) {
			skeletonTerms[n]= arrayOfSkeletonTerms.get(n).getTerm();
		};
		Term termListOfSkeletons= GeneralConverters.arrayToList(skeletonTerms);
		Term result= PrologEmptySet.instance;
		result= new PrologSet(-SymbolCodes.symbolCode_E_skeletons,termListOfSkeletons,result);
		result= new PrologSet(-SymbolCodes.symbolCode_E_time2,termEndTime,result);
		result= new PrologSet(-SymbolCodes.symbolCode_E_frame2,termRecentFrameNumber,result);
		result= new PrologSet(-SymbolCodes.symbolCode_E_time1,termBeginningTime,result);
		result= new PrologSet(-SymbolCodes.symbolCode_E_frame1,termFirstFrameNumber,result);
		result= new PrologSet(-SymbolCodes.symbolCode_E_is_complete,YesNo.boolean2TermYesNo(isComplete),result);
		result= new PrologSet(-SymbolCodes.symbolCode_E_identifier,termIdentifier,result);
		return result;
	}
	public Term toTerm(boolean isComplete, long givenFrameNumber, long givenFrameTime) {
		Term termIdentifier= new PrologInteger(identifier);
		Term termFirstFrameNumber= new PrologInteger(firstFrameNumber);
		Term termRecentFrameNumber= new PrologInteger(givenFrameNumber);
		Term termBeginningTime= new PrologInteger(beginningTime);
		Term termEndTime= new PrologInteger(givenFrameTime);
		ArrayList<SkeletonTerm> targetSkeletonTerms= new ArrayList<>();
		for (int n=0; n < arrayOfSkeletonTerms.size(); n++) {
			SkeletonTerm skeletonTerm= arrayOfSkeletonTerms.get(n);
			if (skeletonTerm.getFrameNumber() > givenFrameNumber) {
				break;
			};
			targetSkeletonTerms.add(skeletonTerm);
		};
		Term[] skeletonTerms= new Term[targetSkeletonTerms.size()];
		for (int n=0; n < targetSkeletonTerms.size(); n++) {
			skeletonTerms[n]= targetSkeletonTerms.get(n).getTerm();
		};
		Term termListOfSkeletons= GeneralConverters.arrayToList(skeletonTerms);
		Term result= PrologEmptySet.instance;
		result= new PrologSet(-SymbolCodes.symbolCode_E_skeletons,termListOfSkeletons,result);
		result= new PrologSet(-SymbolCodes.symbolCode_E_time2,termEndTime,result);
		result= new PrologSet(-SymbolCodes.symbolCode_E_frame2,termRecentFrameNumber,result);
		result= new PrologSet(-SymbolCodes.symbolCode_E_time1,termBeginningTime,result);
		result= new PrologSet(-SymbolCodes.symbolCode_E_frame1,termFirstFrameNumber,result);
		result= new PrologSet(-SymbolCodes.symbolCode_E_is_complete,YesNo.boolean2TermYesNo(isComplete),result);
		result= new PrologSet(-SymbolCodes.symbolCode_E_identifier,termIdentifier,result);
		return result;
	}
}
