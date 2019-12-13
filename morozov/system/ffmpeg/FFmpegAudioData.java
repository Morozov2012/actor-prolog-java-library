// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.system.ffmpeg;

import static org.bytedeco.javacpp.avutil.*;

import morozov.system.ffmpeg.interfaces.*;
import morozov.system.sound.frames.data.*;

public class FFmpegAudioData extends FFmpegData implements FFmpegAudioDataInterface {
	//
	protected byte[] byteArray;
	protected AudioFormatBaseAttributes audioFormatAttributes;
	//
	public FFmpegAudioData(
			byte[] array,
			long t1,
			long t2,
			AVRational base,
			AVRational frameRate,
			AudioFormatBaseAttributes attributes,
			long n) {
		super(t1,t2,base,frameRate,n);
		byteArray= array;
		audioFormatAttributes= attributes;
	}
	//
	@Override
	public byte[] getByteArray() {
		return byteArray;
	}
	public AudioFormatBaseAttributes getAudioFormatAttributes() {
		return audioFormatAttributes;
	}
}
