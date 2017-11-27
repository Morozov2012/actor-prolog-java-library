// (c) 2013-2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm;

import target.*;

import morozov.system.vision.vpm.converters.*;
import morozov.terms.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ArrayList;
import java.math.BigInteger;
import java.io.PrintStream;

public class ConnectedSegment {
	//
	protected TrackSegment trackSegment;
	protected boolean isFirst;
	protected boolean isLast;
	//
	protected ConnectedSegment previousSegment;
	protected ConnectedSegment nextSegment;
	protected HashSet<ConnectedSegment> entries= new HashSet<>();
	protected HashSet<ConnectedSegment> origins= new HashSet<>();
	protected HashSet<ConnectedSegment> branches= new HashSet<>();
	//
	protected Term prologConnectedSegment;
	//
	protected static final int minimalSegmentLength= 2;
	//
	///////////////////////////////////////////////////////////////
	//
	public ConnectedSegment(TrackSegment s, boolean first, boolean last) {
		trackSegment= s;
		isFirst= first;
		isLast= last;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public TrackSegment getTrackSegment() {
		return trackSegment;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public BigInteger getOwner() {
		return trackSegment.getOwner();
	}
	public HashSet<ConnectedSegment> getEntries() {
		return entries;
	}
	public HashSet<ConnectedSegment> getOrigins() {
		return origins;
	}
	public HashSet<ConnectedSegment> getBranches() {
		return branches;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public boolean isEmpty() {
		return trackSegment.getLength() < minimalSegmentLength;
	}
	public boolean isInputEntry() {
		if (isFirst && !isBeginningOfBranch()) {
			return true;
		} else {
			return false;
		}
	}
	public boolean isBeginningOfBranch() {
		long breakPointTime= trackSegment.getSegmentBreakPoint();
		long beginningTime= trackSegment.getSegmentBeginning();
		long endTime= trackSegment.getSegmentEnd();
		if (breakPointTime==beginningTime && breakPointTime==endTime) {
			return true;
		} else {
			return false;
		}
	}
	public boolean isOutputEntry() {
		if (!isLast) {
			return false;
		};
		boolean hasSuccessor= false;
		Iterator<ConnectedSegment> iterator= entries.iterator();
		while (iterator.hasNext()) {
			ConnectedSegment entry= iterator.next();
			if (entry.hasSuccessor(this)) {
				hasSuccessor= true;
				break;
			}
		};
		return !hasSuccessor;
	}
	public boolean hasSuccessor(ConnectedSegment self) {
		if (this==self) {
			return false;
		};
		return !isLast;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void resolveLinks(HashMap<BigInteger,StableTrack> tracks, HashSet<BigInteger> refusedTracks, HashMap<BigInteger,HashMap<Integer,ConnectedSegment>> hash1) {
		BigInteger owner= trackSegment.getOwner();
		// if (refusedTracks.contains(owner)) {
		//	return;
		// };
		int number= trackSegment.getNumber();
		if (!isFirst) {
			HashMap<Integer,ConnectedSegment> hash2= hash1.get(owner);
			ConnectedSegment connectedSegment= hash2.get(number-1);
			previousSegment= connectedSegment;
		};
		if (!isLast) {
			HashMap<Integer,ConnectedSegment> hash2= hash1.get(owner);
			ConnectedSegment connectedSegment= hash2.get(number+1);
			nextSegment= connectedSegment;
		};
		HashSet<BigInteger> inspectedTracks= new HashSet<>();
		BigInteger[] entries= trackSegment.getEntries();
		resolveLinks(entries,tracks,refusedTracks,hash1,inspectedTracks);
	}
	public void resolveLinks(BigInteger[] links, HashMap<BigInteger,StableTrack> tracks, HashSet<BigInteger> refusedTracks, HashMap<BigInteger,HashMap<Integer,ConnectedSegment>> hash1, HashSet<BigInteger> inspectedTracks) {
		for (int n=0; n < links.length; n++) {
			BigInteger neighbour= links[n];
			// if (refusedTracks.contains(neighbour)) {
			//	continue;
			// };
			if (inspectedTracks.add(neighbour)) {
				StableTrack track= tracks.get(neighbour);
				if (track != null) {
					long breakPointTime= trackSegment.getSegmentBreakPoint();
					TrackSegment linkedSegment= track.getLinkedSegment(breakPointTime);
					if (linkedSegment != null) {
						HashMap<Integer,ConnectedSegment> hash2= hash1.get(linkedSegment.getOwner());
						ConnectedSegment connectedSegment= hash2.get(linkedSegment.getNumber());
						entries.add(connectedSegment);
						connectedSegment.addEntry(this);
					}
				}
			}
		}
	}
	public void addEntry(ConnectedSegment segment) {
		entries.add(segment);
	}
	public void collectOriginsAndBranches() {
		if (trackSegment.getLength() >= minimalSegmentLength) {
			if (previousSegment != null) {
				long beginningTime= trackSegment.getSegmentBeginning();
				HashSet<ConnectedSegment> emptySegments= new HashSet<ConnectedSegment>();
				previousSegment.registerOriginsAndBranches(origins,this,beginningTime,emptySegments);
			}
		}
	}
	public void registerOriginsAndBranches(HashSet<ConnectedSegment> nextSegmentOrigins, ConnectedSegment virtualNextSegment, long virtualBeginningTime, HashSet<ConnectedSegment> emptySegments) {
		if (trackSegment.getSegmentBreakPoint() <= virtualBeginningTime) {
			if (trackSegment.getLength() >= minimalSegmentLength) {
				nextSegmentOrigins.add(this);
				branches.add(virtualNextSegment);
			} else {
				emptySegments.add(this);
				if (previousSegment != null && !nextSegmentOrigins.contains(previousSegment)) {
					previousSegment.registerOriginsAndBranches(nextSegmentOrigins,virtualNextSegment,virtualBeginningTime,emptySegments);
				}
			};
			Iterator<ConnectedSegment> iterator= entries.iterator();
			while (iterator.hasNext()) {
				ConnectedSegment entry= iterator.next();
				if (!nextSegmentOrigins.contains(entry) && !emptySegments.contains(entry)) {
					entry.registerOriginsAndBranches(nextSegmentOrigins,virtualNextSegment,virtualBeginningTime,emptySegments);
				}
			}
		}
	}
	public void inspectTree(SegmentChain previousUnit, HashSet<SegmentChain> hypotheticPathes, ConnectedSegment origin) {
		if (previousUnit != null && previousUnit.contains(this)) {
			return;
		};
		SegmentChain currentUnit= new SegmentChain(this,previousUnit,origin);
		boolean isOutputEntry= true;
		if (!isLast) {
			nextSegment.inspectTree(currentUnit,hypotheticPathes,origin);
			isOutputEntry= false;
		};
		long time= trackSegment.getSegmentBreakPoint();
		Iterator<ConnectedSegment> iterator= entries.iterator();
		while (iterator.hasNext()) {
			ConnectedSegment entry= iterator.next();
			if (!entry.isLast) {
				ConnectedSegment successor= entry.nextSegment;
				successor.inspectTree(currentUnit,hypotheticPathes,origin);
				isOutputEntry= false;
			}
		};
		if (isOutputEntry) {
			hypotheticPathes.add(currentUnit);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void excludeSegment() {
		// if (!isFirst) {
			// HashSet<ConnectedSegment> entries= getEntries();
			// Iterator<ConnectedSegment> entriesIterator= entries.iterator();
			// while (entriesIterator.hasNext()) {
			//	ConnectedSegment entry= entriesIterator.next();
			//	entry.deleteBranch(this);
			//	entry.addBranches(branches);
			// };
			HashSet<ConnectedSegment> origins= getOrigins();
			Iterator<ConnectedSegment> originsIterator= origins.iterator();
			while (originsIterator.hasNext()) {
				ConnectedSegment origin= originsIterator.next();
				origin.deleteBranch(this);
				origin.addBranches(branches);
			};
		// };
		// if (!isLast) {
			HashSet<ConnectedSegment> branches= getBranches();
			Iterator<ConnectedSegment> branchesIterator= branches.iterator();
			while (branchesIterator.hasNext()) {
				ConnectedSegment branch= branchesIterator.next();
				branch.deleteOrigin(this);
				branch.addOrigins(origins);
			}
		// }
	}
	public void deleteOrigin(ConnectedSegment segment) {
		origins.remove(segment);
	}
	public void deleteBranch(ConnectedSegment segment) {
		branches.remove(segment);
	}
	public void addOrigins(HashSet<ConnectedSegment> segments) {
		Iterator<ConnectedSegment> segmentsIterator= segments.iterator();
		while (segmentsIterator.hasNext()) {
			ConnectedSegment segment= segmentsIterator.next();
			origins.add(segment);
		}
	}
	public void addBranches(HashSet<ConnectedSegment> segments) {
		Iterator<ConnectedSegment> segmentsIterator= segments.iterator();
		while (segmentsIterator.hasNext()) {
			ConnectedSegment segment= segmentsIterator.next();
			branches.add(segment);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term toTerm(ArrayList<ConnectedSegment> connectedSegments) {
		if (prologConnectedSegment != null) {
			return prologConnectedSegment;
		};
		prologConnectedSegment= trackSegment.toTerm();
		BlobType blobType= trackSegment.getBlobType();
		if (blobType != null) {
			prologConnectedSegment= new PrologSet(
				- SymbolCodes.symbolCode_E_type,
				blobType.toTerm(),
				prologConnectedSegment);
		};
		int connectedSegmentNumber= connectedSegments.indexOf(this);
		Term prologOriginEdges= collectOriginEdges(connectedSegmentNumber,connectedSegments);
		Term prologBrachEdges= collectBranchEdges(connectedSegmentNumber,connectedSegments);
		Term prologIdentifier= new PrologInteger(trackSegment.getOwner());
		prologConnectedSegment= new PrologSet(
			- SymbolCodes.symbolCode_E_inputs,
			prologOriginEdges,
			new PrologSet(
			- SymbolCodes.symbolCode_E_outputs,
			prologBrachEdges,
			new PrologSet(
			- SymbolCodes.symbolCode_E_identifier,
			prologIdentifier,
			prologConnectedSegment)));
		return prologConnectedSegment;
	}
	//
	protected Term collectOriginEdges(int connectedSegmentNumber, ArrayList<ConnectedSegment> connectedSegments) {
		HashSet<ConnectedSegment> origins= getOrigins();
		Term result= PrologEmptyList.instance;
		Iterator<ConnectedSegment> iterator= origins.iterator();
		while (iterator.hasNext()) {
			ConnectedSegment entry= iterator.next();
			BigInteger owner= entry.getOwner();
			int entryNumber= connectedSegments.indexOf(entry);
			if (entryNumber < 0) {
				continue;
			} else if (entryNumber==connectedSegmentNumber) {
				continue;
			};
			Term prologEdgeNumber= new PrologInteger(entryNumber+1);
			result= new PrologList(prologEdgeNumber,result);
		};
		return result;
	}
	//
	protected Term collectBranchEdges(int connectedSegmentNumber, ArrayList<ConnectedSegment> connectedSegments) {
		HashSet<ConnectedSegment> branches= getBranches();
		Term result= PrologEmptyList.instance;
		Iterator<ConnectedSegment> iterator= branches.iterator();
		while (iterator.hasNext()) {
			ConnectedSegment entry= iterator.next();
			BigInteger owner= entry.getOwner();
			int entryNumber= connectedSegments.indexOf(entry);
			if (entryNumber < 0) {
				continue;
			} else if (entryNumber==connectedSegmentNumber) {
				continue;
			};
			Term prologEdgeNumber= new PrologInteger(entryNumber+1);
			result= new PrologList(prologEdgeNumber,result);
		};
		return result;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void dump(PrintStream stream) {
		stream.printf("this:            %s\n",this);
		stream.printf("isFirst:         %s\n",isFirst);
		stream.printf("isLast:          %s\n",isLast);
		stream.printf("previousSegment: %s\n",previousSegment);
		stream.printf("nextSegment:     %s\n",nextSegment);
		stream.printf("ENTRIES:\n");
		Iterator<ConnectedSegment> iterator= entries.iterator();
		int index= 0;
		while (iterator.hasNext()) {
			ConnectedSegment segment= iterator.next();
			stream.printf("%s: %s\n",index,segment);
			index++;
		};
		stream.printf("ORIGINS:\n");
		iterator= origins.iterator();
		index= 0;
		while (iterator.hasNext()) {
			ConnectedSegment segment= iterator.next();
			stream.printf("%s: %s\n",index,segment);
			index++;
		}
	}
	public void createMatlab(PrintStream stream, BigInteger identifier, int n) {
		stream.printf("%% this:            %s\n",this);
		stream.printf("%% isFirst:         %s\n",isFirst);
		stream.printf("%% isLast:          %s\n",isLast);
		stream.printf("%% previousSegment: %s\n",previousSegment);
		stream.printf("%% nextSegment:     %s\n",nextSegment);
		trackSegment.createMatlab(stream,identifier,n);
		stream.printf("%% ENTRIES:\n");
		Iterator<ConnectedSegment> iterator= entries.iterator();
		int index= 0;
		while (iterator.hasNext()) {
			ConnectedSegment segment= iterator.next();
			stream.printf("%% %s: %s\n",index,segment);
			index++;
		}
	}
}
