// (c) 2018 Alexei A. Morozov

package morozov.system.frames.tools;

import java.io.Serializable;

public abstract class EnumeratedFrame implements Serializable {
	//
	// private static final long serialVersionUID= 0xFB34B21E58C97F83L; // -345455427992322173L
	private static final long serialVersionUID= 0xFB34B21E58C97F83L; // -345455427992322173L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.system.frames.tools","EnumeratedFrame");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	abstract public long getTime();
}
