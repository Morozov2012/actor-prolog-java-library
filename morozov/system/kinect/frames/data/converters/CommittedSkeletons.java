// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.frames.data.converters;

import target.*;

import morozov.system.*;
import morozov.system.converters.*;
import morozov.system.kinect.frames.data.interfaces.*;
import morozov.system.kinect.modes.interfaces.*;
import morozov.terms.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Iterator;
import java.util.ListIterator;
import java.io.Serializable;

public class CommittedSkeletons implements Serializable {
	//
	protected LinkedHashMap<Integer,SkeletonTrack> tracks= new LinkedHashMap<>();
	protected ArrayList<TrackTerm> arrayOfCompleteTrackTerms= new ArrayList<>();
	protected ArrayList<KinectFrameNumberAndTime> arrayOfTime= new ArrayList<>();
	// protected long beginningRawSkeletonTime= -1;
	// protected long recentRawSkeletonTime= -1;
	//
	///////////////////////////////////////////////////////////////
	//
	public CommittedSkeletons() {
	}
	//
	public void clear() {
		tracks.clear();
		arrayOfCompleteTrackTerms.clear();
		arrayOfTime.clear();
		// beginningRawSkeletonTime= -1;
		// recentRawSkeletonTime= -1;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term appendFrame(
			long frameNumber,
			long frameTime,
			GeneralSkeletonInterface[] skeletons,
			DimensionsInterface dimensions,
			ArrayList<Term> recentSkeletonTermArray,
			KinectDisplayingModeInterface displayingMode,
			NumericalValue maximalChronicleLength) {
		if (skeletons != null) {
			if (frameTime >= 0) {
				if (!arrayOfTime.isEmpty()) {
					shortenArraysIfNecessary(frameTime,maximalChronicleLength);
				}
			};
			arrayOfTime.add(new KinectFrameNumberAndTime(frameNumber,frameTime));
			for (int k=0; k < skeletons.length; k++) {
				GeneralSkeletonInterface skeleton= skeletons[k];
				if (skeleton != null && skeleton.isTracked()) {
					int identifier= skeleton.getIdentifier();
					SkeletonTrack track= tracks.get(identifier);
					if (track==null) {
						track= new SkeletonTrack(identifier);
						tracks.put(identifier,track);
					};
					SkeletonDimensionsInterface skeletonDimensions;
					if (dimensions != null) {
						skeletonDimensions= dimensions.getSkeletonDimensions(k);
					} else {
						skeletonDimensions= null;
					};
					track.appendFrame(frameNumber,frameTime,skeleton,skeletonDimensions,displayingMode);
				}
			};
			Set<Integer> trackKeySet= tracks.keySet();
			Iterator<Integer> trackKeySetIterator= trackKeySet.iterator();
			while (trackKeySetIterator.hasNext()) {
				int identifier= trackKeySetIterator.next();
				SkeletonTrack track= tracks.get(identifier);
				if (track.getEndTime() < frameTime) {
					arrayOfCompleteTrackTerms.add(track.toTrackTerm(true));
					trackKeySetIterator.remove();
				}
			}
		} else {
			Set<Integer> trackKeySet= tracks.keySet();
			Iterator<Integer> trackKeySetIterator= trackKeySet.iterator();
			while (trackKeySetIterator.hasNext()) {
				int identifier= trackKeySetIterator.next();
				SkeletonTrack track= tracks.get(identifier);
				arrayOfCompleteTrackTerms.add(track.toTrackTerm(true));
				trackKeySetIterator.remove();
			}
		};
		ArrayList<Term> arrayOfIncompleteTrackTerms= new ArrayList<>();
		Set<Integer> trackKeySet= tracks.keySet();
		Iterator<Integer> trackKeySetIterator= trackKeySet.iterator();
		while (trackKeySetIterator.hasNext()) {
			int identifier= trackKeySetIterator.next();
			SkeletonTrack track= tracks.get(identifier);
			arrayOfIncompleteTrackTerms.add(track.toTerm(false));
			SkeletonTerm recentSkeletonTerm= track.getRecentSkeletonTerm();
			if (recentSkeletonTerm != null) {
				recentSkeletonTermArray.add(recentSkeletonTerm.getTerm());
			};
			// trackKeySetIterator.remove();
		};
		Term committedTracksTerm= GeneralConverters.arrayListToTerm(arrayOfIncompleteTrackTerms);
		Term[] arrayOfCommittedTrackTerms= new Term[arrayOfCompleteTrackTerms.size()];
		for (int n=0; n < arrayOfCompleteTrackTerms.size(); n++) {
			arrayOfCommittedTrackTerms[n]= arrayOfCompleteTrackTerms.get(n).getTerm();
		};
		committedTracksTerm= GeneralConverters.arrayToList(arrayOfCommittedTrackTerms,committedTracksTerm);
		return committedTracksTerm;
	}
	//
	protected void shortenArraysIfNecessary(long frameTime, NumericalValue maximalChronicleLength) {
		if (maximalChronicleLength==null) {
			return;
		};
		long longMaximalChronicleLength= maximalChronicleLength.toLong(1000);
		if (longMaximalChronicleLength < 0) {
			return;
		};
		long minimalTime= frameTime - longMaximalChronicleLength;
		ListIterator<KinectFrameNumberAndTime> iteratorOfArrayOfTime= arrayOfTime.listIterator();
		boolean thereWereObsoletePoints= false;
		while (iteratorOfArrayOfTime.hasNext()) {
			KinectFrameNumberAndTime frameNumberAndTime= iteratorOfArrayOfTime.next();
			if (frameNumberAndTime.getTime() < minimalTime) {
				iteratorOfArrayOfTime.remove();
				thereWereObsoletePoints= true;
			} else {
				break;
			}
		};
		if (thereWereObsoletePoints) {
			shortenArrayOfCompleteTrackTerms(minimalTime);
			shortenIncompleteTracks(minimalTime);
		}
	}
	//
	protected void shortenArrayOfCompleteTrackTerms(long minimalTime) {
		ListIterator<TrackTerm> iteratorOfArrayOfCompleteTrackTerms= arrayOfCompleteTrackTerms.listIterator();
		while (iteratorOfArrayOfCompleteTrackTerms.hasNext()) {
			TrackTerm trackTerm= iteratorOfArrayOfCompleteTrackTerms.next();
			long endTime= trackTerm.getEndTime();
			if (endTime < minimalTime) {
				iteratorOfArrayOfCompleteTrackTerms.remove();
			}
		}
	}
	//
	protected void shortenIncompleteTracks(long minimalTime) {
		Set<Integer> trackKeySet= tracks.keySet();
		Iterator<Integer> trackKeySetIterator= trackKeySet.iterator();
		while (trackKeySetIterator.hasNext()) {
			int identifier= trackKeySetIterator.next();
			SkeletonTrack track= tracks.get(identifier);
			track.shorten(minimalTime);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term analyseFrame(
			long givenFrameNumber,
			long givenFrameTime,
			ArrayList<Term> recentSkeletonTermArray) {
		ArrayList<Term> arrayOfIncompleteTrackTerms= new ArrayList<>();
		Set<Integer> trackKeySet= tracks.keySet();
		Iterator<Integer> trackKeySetIterator= trackKeySet.iterator();
		while (trackKeySetIterator.hasNext()) {
			int identifier= trackKeySetIterator.next();
			SkeletonTrack track= tracks.get(identifier);
			long trackFirstFrameNumber= track.getFirstFrameNumber();
			if (trackFirstFrameNumber > givenFrameNumber) {
				continue;
			};
			arrayOfIncompleteTrackTerms.add(track.toTerm(false,givenFrameNumber,givenFrameTime));
			recentSkeletonTermArray.add(track.getSkeletonTerm(givenFrameTime));
		};
		Term committedTracksTerm= GeneralConverters.arrayListToTerm(arrayOfIncompleteTrackTerms);
		// ArrayList<TrackTerm> targetCompleteTrackTerms= new ArrayList<>();
		// for (int n=0; n < arrayOfCompleteTrackTerms.size(); n++) {
		//	TrackTerm trackTerm= arrayOfCompleteTrackTerms.get(n);
		//	if (trackTerm.getLastFrameNumber() > givenFrameNumber) {
		//		break;
		//	};
		//	targetCompleteTrackTerms.add(trackTerm);
		// };
		// Term[] arrayOfCommittedTrackTerms= new Term[targetCompleteTrackTerms.size()];
		// for (int n=0; n < targetCompleteTrackTerms.size(); n++) {
		//	arrayOfCommittedTrackTerms[n]= targetCompleteTrackTerms.get(n).getTerm();
		// };
		// committedTracksTerm= GeneralConverters.arrayToList(arrayOfCommittedTrackTerms,committedTracksTerm);
		return committedTracksTerm;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term chronicleToTerm() {
		Term result= PrologEmptyList.instance;
		ListIterator<KinectFrameNumberAndTime> iteratorOfArrayOfTime= arrayOfTime.listIterator();
		while (iteratorOfArrayOfTime.hasNext()) {
			KinectFrameNumberAndTime frameNumberAndTime= iteratorOfArrayOfTime.next();
			Term skeletonFrameTerm= chronicleFrameToTerm(frameNumberAndTime);
			result= new PrologList(skeletonFrameTerm,result);
		};
		return result;
	}
	public Term chronicleToTerm(long numberOfRepeatedFrame) {
		Term result= PrologEmptyList.instance;
		ListIterator<KinectFrameNumberAndTime> iteratorOfArrayOfTime= arrayOfTime.listIterator();
		while (iteratorOfArrayOfTime.hasNext()) {
			KinectFrameNumberAndTime frameNumberAndTime= iteratorOfArrayOfTime.next();
			if (frameNumberAndTime.getFrameNumber() > numberOfRepeatedFrame) {
				return result;
			};
			Term skeletonFrameTerm= chronicleFrameToTerm(frameNumberAndTime);
			result= new PrologList(skeletonFrameTerm,result);
		};
		return result;
	}
	//
	public Term chronicleFrameToTerm(KinectFrameNumberAndTime frameNumberAndTime) {
		long frameNumber= frameNumberAndTime.getFrameNumber();
		long frameTime= frameNumberAndTime.getTime();
		Term termFrameNumber= new PrologInteger(frameNumber);
		Term termTime= new PrologInteger(frameTime);
		ArrayList<Term> arrayOfSkeletonFrames= new ArrayList<>();
		Set<Integer> trackKeySet= tracks.keySet();
		Iterator<Integer> trackKeySetIterator= trackKeySet.iterator();
		while (trackKeySetIterator.hasNext()) {
			int identifier= trackKeySetIterator.next();
			SkeletonTrack track= tracks.get(identifier);
			Term skeletonTerm= track.getSkeletonTerm(frameTime);
			if (skeletonTerm != null) {
				arrayOfSkeletonFrames.add(skeletonTerm);
			}
		};
		Term termSkeletons= GeneralConverters.arrayListToTerm(arrayOfSkeletonFrames);
		Term result= PrologEmptySet.instance;
		result= new PrologSet(-SymbolCodes.symbolCode_E_skeletons,termSkeletons,result);
		result= new PrologSet(-SymbolCodes.symbolCode_E_time,termTime,result);
		result= new PrologSet(-SymbolCodes.symbolCode_E_frame,termFrameNumber,result);
		return result;
	}
}
