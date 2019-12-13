// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.system.ffmpeg;

import static org.bytedeco.javacpp.avutil.*;

import morozov.system.ffmpeg.interfaces.*;

public class FFmpegData implements FFmpegDataInterface {
	//
	protected long timeInMilliseconds;
	protected long timeInBaseUnits;
	protected AVRational timeBase;
	protected AVRational averageFrameRate;
	protected long number;
	//
	public FFmpegData(long t1, long t2, AVRational base, AVRational frameRate, long n) {
		timeInMilliseconds= t1;
		timeInBaseUnits= t2;
		timeBase= base;
		averageFrameRate= frameRate;
		number= n;
	}
	//
	@Override
	public long getTimeInMilliseconds() {
		return timeInMilliseconds;
	}
	@Override
	public long getTimeInBaseUnits() {
		return timeInBaseUnits;
	}
	@Override
	public AVRational getTimeBase() {
		return timeBase;
	}
	@Override
	public AVRational getAverageFrameRate() {
		return averageFrameRate;
	}
	@Override
	public long getNumber() {
		return number;
	}
	//
	public double getTimeInSeconds() {
		return FFmpegTools.computeTime(timeInBaseUnits,timeBase);
	}
}
