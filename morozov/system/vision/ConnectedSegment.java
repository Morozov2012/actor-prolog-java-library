// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.vision;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.geom.Line2D;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.math.BigInteger;
import java.io.PrintStream;

class ConnectedSegment {
	public TrackSegment trackSegment;
	public boolean isFirst;
	public boolean isLast;
	public ConnectedSegment previousSegment;
	public ConnectedSegment nextSegment;
	public HashSet<ConnectedSegment> entries= new HashSet<>();
	public HashSet<ConnectedSegment> origins= new HashSet<>();
	public HashSet<ConnectedSegment> branches= new HashSet<>();
	public static final int nBins= 256;
	public ConnectedSegment(TrackSegment s, boolean first, boolean last) {
		trackSegment= s;
		isFirst= first;
		isLast= last;
	}
	public void resolveLinks(HashMap<BigInteger,StableTrack> tracks, HashSet<BigInteger> refusedTracks, HashMap<BigInteger,HashMap<Integer,ConnectedSegment>> hash1) {
		BigInteger owner= trackSegment.owner;
		if (refusedTracks.contains(owner)) {
			return;
		};
		int number= trackSegment.number;
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
		BigInteger[] entries= trackSegment.entries;
		resolveLinks(entries,tracks,refusedTracks,hash1,inspectedTracks);
	}
	public void resolveLinks(BigInteger[] links, HashMap<BigInteger,StableTrack> tracks, HashSet<BigInteger> refusedTracks, HashMap<BigInteger,HashMap<Integer,ConnectedSegment>> hash1, HashSet<BigInteger> inspectedTracks) {
		for (int n=0; n < links.length; n++) {
			BigInteger neighbour= links[n];
			if (refusedTracks.contains(neighbour)) {
				continue;
			};
			if (inspectedTracks.add(neighbour)) {
				StableTrack track= tracks.get(neighbour);
				if (track != null) {
					long breakPointTime= trackSegment.breakPointTime;
					TrackSegment linkedSegment= track.getLinkedSegment(breakPointTime);
					if (linkedSegment != null) {
						HashMap<Integer,ConnectedSegment> hash2= hash1.get(linkedSegment.owner);
						ConnectedSegment connectedSegment= hash2.get(linkedSegment.number);
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
		if (trackSegment.rectangles.length > 0) {
			if (previousSegment != null) {
				long beginningTime= trackSegment.beginningTime;
				HashSet<ConnectedSegment> emptySegments= new HashSet<ConnectedSegment>();
				previousSegment.registerOriginsAndBranches(origins,this,beginningTime,emptySegments);
			}
		}
	}
	public void registerOriginsAndBranches(HashSet<ConnectedSegment> nextSegmentOrigins, ConnectedSegment virtualNextSegment, long virtualBeginningTime, HashSet<ConnectedSegment> emptySegments) {
		if (trackSegment.breakPointTime <= virtualBeginningTime) {
			if (trackSegment.rectangles.length > 1) {
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
	public BigInteger getOwner() {
		return trackSegment.owner;
	}
	public boolean isInputEntry() {
		if (isFirst && !isBeginningOfBranch()) {
			return true;
		} else {
			return false;
		}
	}
	public boolean isBeginningOfBranch() {
		long breakPointTime= trackSegment.breakPointTime;
		long beginningTime= trackSegment.beginningTime;
		long endTime= trackSegment.endTime;
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
		long time= trackSegment.breakPointTime;
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
	public void dump(PrintStream stream) {
		stream.printf("this:            %s\n",this);
		stream.printf("isFirst:         %s\n",isFirst);
		stream.printf("isLast:          %s\n",isLast);
		stream.printf("previousSegment: %s\n",previousSegment);
		stream.printf("nextSegment:     %s\n",nextSegment);
		// trackSegment.dump(stream);
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
