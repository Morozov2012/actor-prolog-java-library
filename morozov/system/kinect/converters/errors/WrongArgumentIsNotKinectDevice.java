// (c) 2016 IRE RAS Alexei A. Morozov

package morozov.system.kinect.converters.errors;

import morozov.terms.*;
import morozov.terms.errors.*;

public class WrongArgumentIsNotKinectDevice extends WrongArgument {
	public WrongArgumentIsNotKinectDevice(Term value) {
		super(value);
	}
}
