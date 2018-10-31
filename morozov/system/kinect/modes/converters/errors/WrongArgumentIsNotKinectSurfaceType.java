// (c) 2018 IRE RAS Alexei A. Morozov

package morozov.system.kinect.modes.converters.errors;

import morozov.terms.*;
import morozov.terms.errors.*;

public class WrongArgumentIsNotKinectSurfaceType extends WrongArgument {
	public WrongArgumentIsNotKinectSurfaceType(Term value) {
		super(value);
	}
}
