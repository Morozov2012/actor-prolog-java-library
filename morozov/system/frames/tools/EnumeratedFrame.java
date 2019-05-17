// (c) 2018 Alexei A. Morozov

package morozov.system.frames.tools;

import java.io.Serializable;

public abstract class EnumeratedFrame implements Serializable {
	//
	// private static final long serialVersionUID= 0xFB34B21E58C97F83L; // -345455427992322173L
	private static final long serialVersionUID= 0xFB34B21E58C97F83L; // -345455427992322173L
	//
	// static {
	//	// new SynchronizedFrames(null,0,null,0);
	//	// new EnumeratedFFmpegFrame(null,null,0);
	//	// new EnumeratedCompoundFrame(null,0);
	//	// new EnumeratedDataFrame(null,0);
	//	// new EnumeratedKinectFrame(null,0);
	//	SerialVersionChecker.check(serialVersionUID,"morozov.system.frames.tools","EnumeratedFrame");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	// abstract public long getNumberOfFrame();
	abstract public long getTime();
}
