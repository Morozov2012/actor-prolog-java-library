// (c) 2016 IRE RAS Alexei A. Morozov

package morozov.system.kinect.modes.converters.errors;

import morozov.terms.*;
import morozov.terms.errors.*;

public class WrongArgumentIsNotKinectFrameType extends WrongArgument {
	public WrongArgumentIsNotKinectFrameType(Term value) {
		super(value);
	}
}
