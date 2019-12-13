// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.frames.data.converters.errors;

import morozov.system.kinect.frames.tools.*;

public class UnknownRectangleVertex extends RuntimeException {
	//
	protected RectangleVertex vertex;
	//
	public UnknownRectangleVertex(RectangleVertex v) {
		vertex= v;
	}
	//
	@Override
	public String toString() {
		return this.getClass().toString() + "(" + vertex.toString() + ")";
	}
}
