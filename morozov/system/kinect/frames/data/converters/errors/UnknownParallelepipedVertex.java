// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.frames.data.converters.errors;

import morozov.system.kinect.frames.tools.*;

public class UnknownParallelepipedVertex extends RuntimeException {
	protected ParallelepipedVertex vertex;
	public UnknownParallelepipedVertex(ParallelepipedVertex v) {
		vertex= v;
	}
	public String toString() {
		return this.getClass().toString() + "(" + vertex.toString() + ")";
	}
}
