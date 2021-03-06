// (c) 2012 IRE RAS Alexei A. Morozov

package morozov.built_in;

import morozov.run.*;
import morozov.system.*;
import morozov.system.converters.*;
import morozov.system.files.*;
import morozov.system.files.errors.*;
import morozov.terms.*;
import morozov.worlds.*;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioInputStream;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;

import java.math.BigInteger;

public abstract class Clip extends DataAbstraction {
	//
	protected javax.sound.sampled.Clip commonClip;
	//
	public Clip() {
	}
	public Clip(GlobalWorldIdentifier id) {
		super(id);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void releaseSystemResources() {
		if (commonClip != null) {
			commonClip.close();
		};
		super.releaseSystemResources();
	}
	//
	public void open0s(ChoisePoint iX) {
		ExtendedFileName fileName= retrieveRealGlobalFileName(iX);
		openContent(fileName,iX,true);
	}
	public void open1s(ChoisePoint iX, Term a1) {
		ExtendedFileName fileName= retrieveRealGlobalFileName(a1,iX);
		openContent(fileName,iX,true);
	}
	//
	public void close0s(ChoisePoint iX) {
		if (commonClip != null) {
			commonClip.close();
		} else {
			throw new ClipIsNotOpen();
		}
	}
	//
	@Override
	public void start0s(ChoisePoint iX) {
		if (commonClip != null) {
			commonClip.start();
		} else {
			throw new ClipIsNotOpen();
		}
	}
	//
	@Override
	public void stop0s(ChoisePoint iX) {
		if (commonClip != null) {
			commonClip.stop();
		} else {
			throw new ClipIsNotOpen();
		}
	}
	//
	public void loop1s(ChoisePoint iX, Term value) {
		BigInteger number= GeneralConverters.argumentToRoundInteger(value,iX);
		int count= Arithmetic.toInteger(number);
		if (count < 0) {
			count= javax.sound.sampled.Clip.LOOP_CONTINUOUSLY;
		};
		if (commonClip != null) {
			commonClip.loop(count);
		} else {
			throw new ClipIsNotOpen();
		}
	}
	//
	public void play0s(ChoisePoint iX) {
		ExtendedFileName fileName= retrieveRealGlobalFileName(iX);
		play(fileName,iX,true);
	}
	public void play1s(ChoisePoint iX, Term a1) {
		ExtendedFileName fileName= retrieveRealGlobalFileName(a1,iX);
		play(fileName,iX,false);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void play(ExtendedFileName fileName, ChoisePoint iX, boolean useCommonClip) {
		javax.sound.sampled.Clip clip;
		if (useCommonClip && commonClip != null && commonClip.isOpen()) {
			clip= commonClip;
			clip.setFramePosition(0);
		} else {
			clip= openContent(fileName,iX,useCommonClip);
		};
		startWaitAndStop(clip);
		if (!useCommonClip) {
			clip.close();
		}
	}
	//
	protected javax.sound.sampled.Clip openContent(ExtendedFileName fileName, ChoisePoint iX, boolean useCommonClip) {
		int timeout= getMaximalWaitingTimeInMilliseconds(iX);
		byte[] array= fileName.getByteContentOfUniversalResource(CharacterSet.NONE,timeout,staticContext);
		InputStream stream= new ByteArrayInputStream(array);
		AudioInputStream audioStream;
		try {
			audioStream= AudioSystem.getAudioInputStream(stream);
		} catch (UnsupportedAudioFileException e) {
			throw new WrongArgumentIsUnsupportedAudioFile(fileName.toString(),e);
		} catch(IOException e) {
			throw new FileInputOutputError(fileName.toString(),e);
		};
		javax.sound.sampled.Clip clip;
		if (useCommonClip) {
			if (commonClip == null) {
				try {
					clip= AudioSystem.getClip();
				} catch (LineUnavailableException e) {
					throw new WrongArgumentIsUnavailableLine(fileName.toString(),e);
				};
				commonClip= clip;
			} else {
				clip= commonClip;
			}
		} else {
			try {
				clip= AudioSystem.getClip();
			} catch (LineUnavailableException e) {
				throw new WrongArgumentIsUnavailableLine(fileName.toString(),e);
			}
		};
		try {
			clip.open(audioStream);
		} catch(IOException e) {
			throw new FileInputOutputError(fileName.toString(),e);
		} catch (LineUnavailableException e) {
			throw new WrongArgumentIsUnavailableLine(fileName.toString(),e);
		};
		return clip;
	}
	protected void startWaitAndStop(javax.sound.sampled.Clip clip) {
		long length= clip.getMicrosecondLength() / 1000;
		clip.start();
		try {
			Thread.sleep(length);
		} catch (InterruptedException e) {
		};
		clip.stop();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void getFrameLength0ff(ChoisePoint iX, PrologVariable result) {
		if (commonClip != null) {
			long length= commonClip.getFrameLength();
			result.setNonBacktrackableValue(new PrologInteger(length));
		} else {
			result.setNonBacktrackableValue(new PrologInteger(-1));
		}
	}
	public void getFrameLength0fs(ChoisePoint iX) {
	}
	//
	public void getMicrosecondLength0ff(ChoisePoint iX, PrologVariable result) {
		if (commonClip != null) {
			long length= commonClip.getMicrosecondLength();
			result.setNonBacktrackableValue(new PrologInteger(length));
		} else {
			result.setNonBacktrackableValue(new PrologInteger(-1));
		}
	}
	public void getMicrosecondLength0fs(ChoisePoint iX) {
	}
	//
	public void getFramePosition0ff(ChoisePoint iX, PrologVariable result) {
		if (commonClip != null) {
			long length= commonClip.getLongFramePosition();
			result.setNonBacktrackableValue(new PrologInteger(length));
		} else {
			result.setNonBacktrackableValue(new PrologInteger(0));
		}
	}
	public void getFramePosition0fs(ChoisePoint iX) {
	}
	//
	public void getMicrosecondPosition0ff(ChoisePoint iX, PrologVariable result) {
		if (commonClip != null) {
			long length= commonClip.getMicrosecondPosition();
			result.setNonBacktrackableValue(new PrologInteger(length));
		} else {
			result.setNonBacktrackableValue(new PrologInteger(0));
		}
	}
	public void getMicrosecondPosition0fs(ChoisePoint iX) {
	}
	//
	public void setFramePosition1s(ChoisePoint iX, Term value) {
		BigInteger number= GeneralConverters.argumentToRoundInteger(value,iX);
		int frames= Arithmetic.toInteger(number);
		if (commonClip != null) {
			commonClip.setFramePosition(frames);
		} else {
			throw new ClipIsNotOpen();
		}
	}
	//
	public void setMicrosecondPosition1s(ChoisePoint iX, Term value) {
		BigInteger number= GeneralConverters.argumentToRoundInteger(value,iX);
		long microseconds= Arithmetic.toLong(number);
		if (commonClip != null) {
			commonClip.setMicrosecondPosition(microseconds);
		} else {
			throw new ClipIsNotOpen();
		}
	}
	//
	public void setLoopPoints2s(ChoisePoint iX, Term a1, Term a2) {
		BigInteger number1= GeneralConverters.argumentToRoundInteger(a1,iX);
		BigInteger number2= GeneralConverters.argumentToRoundInteger(a2,iX);
		int start= Arithmetic.toInteger(number1);
		int end= Arithmetic.toInteger(number2);
		if (commonClip != null) {
			commonClip.setLoopPoints(start,end);
		} else {
			throw new ClipIsNotOpen();
		}
	}
	//
	public void isOpen0s(ChoisePoint iX) throws Backtracking {
		if (commonClip != null) {
			if (!commonClip.isOpen()) {
				throw Backtracking.instance;
			}
		} else {
			throw Backtracking.instance;
		}
	}
	//
	public void isActive0s(ChoisePoint iX) throws Backtracking {
		if (commonClip != null) {
			if (!commonClip.isActive()) {
				throw Backtracking.instance;
			}
		} else {
			throw Backtracking.instance;
		}
	}
	//
	public void isRunning0s(ChoisePoint iX) throws Backtracking {
		if (commonClip != null) {
			if (!commonClip.isRunning()) {
				throw Backtracking.instance;
			}
		} else {
			throw Backtracking.instance;
		}
	}
}
