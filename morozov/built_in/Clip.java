// (c) 2012 IRE RAS Alexei A. Morozov

package morozov.built_in;

import target.*;

//import morozov.domains.*;
//import morozov.run.*;
import morozov.system.*;
import morozov.system.checker.*;
import morozov.system.files.*;
//import morozov.system.records.*;
//import morozov.syntax.*;
//import morozov.syntax.scanner.*;
import morozov.terms.*;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioInputStream;
// import javax.sound.sampled.Clip;
// import java.io.File;
import java.io.InputStream;
// import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.lang.Thread;
import java.lang.InterruptedException;
import java.net.URI;

import java.math.BigInteger;

public abstract class Clip extends Alpha {
	//
	protected javax.sound.sampled.Clip commonClip;
	//
	protected static final int defaultWaitingInterval= -1;
	//
	abstract protected Term getBuiltInSlot_E_name();
	abstract protected Term getBuiltInSlot_E_extension();
	abstract protected Term getBuiltInSlot_E_max_waiting_time();
	abstract protected Term getBuiltInSlot_E_backslash_is_separator_always();
	//
	public void open1s(ChoisePoint iX, Term name) {
		URI uri= retrieveLocationURI(name,iX);
		openContent(uri,iX,true);
	}
	public void open0s(ChoisePoint iX) {
		URI uri= retrieveLocationURI(iX);
		openContent(uri,iX,true);
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
	public void start0s(ChoisePoint iX) {
		if (commonClip != null) {
			commonClip.start();
		} else {
			throw new ClipIsNotOpen();
		}
	}
	//
	public void stop0s(ChoisePoint iX) {
		if (commonClip != null) {
			commonClip.stop();
		} else {
			throw new ClipIsNotOpen();
		}
	}
	//
	public void loop1s(ChoisePoint iX, Term value) {
		try {
			BigInteger number= Converters.termToRoundInteger(value,iX,false);
			int count= PrologInteger.toInteger(number);
			if (count < 0) {
				count= javax.sound.sampled.Clip.LOOP_CONTINUOUSLY;
			};
			if (commonClip != null) {
				commonClip.loop(count);
			} else {
				throw new ClipIsNotOpen();
			}
		} catch (TermIsNotAnInteger e) {
			throw new WrongArgumentIsNotAnInteger(value);
		}
	}
	//
	public void play1s(ChoisePoint iX, Term name) {
		URI uri= retrieveLocationURI(name,iX);
		play(uri,iX,false);
	}
	public void play0s(ChoisePoint iX) {
		URI uri= retrieveLocationURI(iX);
		play(uri,iX,true);
	}
	//
	protected void play(URI uri, ChoisePoint iX, boolean useCommonClip) {
		javax.sound.sampled.Clip clip;
		if (useCommonClip && commonClip != null && commonClip.isOpen()) {
			clip= commonClip;
			clip.setFramePosition(0);
		} else {
			clip= openContent(uri,iX,useCommonClip);
		};
		startWaitAndStop(clip);
		if (!useCommonClip) {
			clip.close();
		}
	}
	protected javax.sound.sampled.Clip openContent(URI uri, ChoisePoint iX, boolean useCommonClip) {
		int timeout= retrieveMaxWaitingTime(iX);
		boolean backslashIsSeparator= FileUtils.checkIfBackslashIsSeparator(getBuiltInSlot_E_backslash_is_separator_always(),iX);
		InputStream stream;
		try {
			byte[] array= URL_Utils.getContentOfResource(uri,null,timeout,staticContext,backslashIsSeparator);
			stream= new ByteArrayInputStream(array);
		} catch (CannotRetrieveContent e1) {
			throw new FileInputOutputError(uri.toString(),e1);
		};
		AudioInputStream audioStream;
		try {
			audioStream= AudioSystem.getAudioInputStream(stream);
		} catch (UnsupportedAudioFileException e2) {
			throw new WrongArgumentIsUnsupportedAudioFile(uri.toString(),e2);
		} catch(IOException e3) {
			throw new FileInputOutputError(uri.toString(),e3);
		};
		javax.sound.sampled.Clip clip;
		if (useCommonClip) {
			if (commonClip == null) {
				try {
					clip= AudioSystem.getClip();
				} catch (LineUnavailableException e4) {
					throw new WrongArgumentIsUnavailableLine(uri.toString(),e4);
				};
				commonClip= clip;
			} else {
				clip= commonClip;
			}
		} else {
			try {
				clip= AudioSystem.getClip();
			} catch (LineUnavailableException e4) {
				throw new WrongArgumentIsUnavailableLine(uri.toString(),e4);
			}
		};
		try {
			clip.open(audioStream);
		} catch(IOException e5) {
			throw new FileInputOutputError(uri.toString(),e5);
		} catch (LineUnavailableException e6) {
			throw new WrongArgumentIsUnavailableLine(uri.toString(),e6);
		};
		return clip;
	}
	protected void startWaitAndStop(javax.sound.sampled.Clip clip) {
		long length= clip.getMicrosecondLength() / 1000;
		clip.start();
		try {
			Thread.sleep(length);
		} catch (InterruptedException e4) {
		};
		clip.stop();
	}
	//
	public void getFrameLength0ff(ChoisePoint iX, PrologVariable a1) {
		if (commonClip != null) {
			long length= commonClip.getFrameLength();
			a1.value= new PrologInteger(length);
		} else {
			a1.value= new PrologInteger(-1);
		}
	}
	public void getFrameLength0fs(ChoisePoint iX) {
	}
	//
	public void getMicrosecondLength0ff(ChoisePoint iX, PrologVariable a1) {
		if (commonClip != null) {
			long length= commonClip.getMicrosecondLength();
			a1.value= new PrologInteger(length);
		} else {
			a1.value= new PrologInteger(-1);
		}
	}
	public void getMicrosecondLength0fs(ChoisePoint iX) {
	}
	//
	public void getFramePosition0ff(ChoisePoint iX, PrologVariable a1) {
		if (commonClip != null) {
			long length= commonClip.getLongFramePosition();
			a1.value= new PrologInteger(length);
		} else {
			a1.value= new PrologInteger(0);
		}
	}
	public void getFramePosition0fs(ChoisePoint iX) {
	}
	//
	public void getMicrosecondPosition0ff(ChoisePoint iX, PrologVariable a1) {
		if (commonClip != null) {
			long length= commonClip.getMicrosecondPosition();
			a1.value= new PrologInteger(length);
		} else {
			a1.value= new PrologInteger(0);
		}
	}
	public void getMicrosecondPosition0fs(ChoisePoint iX) {
	}
	//
	public void setFramePosition1s(ChoisePoint iX, Term value) {
		try {
			BigInteger number= Converters.termToRoundInteger(value,iX,false);
			int frames= PrologInteger.toInteger(number);
			if (commonClip != null) {
				commonClip.setFramePosition(frames);
			} else {
				throw new ClipIsNotOpen();
			}
		} catch (TermIsNotAnInteger e) {
			throw new WrongArgumentIsNotAnInteger(value);
		}
	}
	//
	public void setMicrosecondPosition1s(ChoisePoint iX, Term value) {
		try {
			BigInteger number= Converters.termToRoundInteger(value,iX,false);
			long microseconds= PrologInteger.toLong(number);
			if (commonClip != null) {
				commonClip.setMicrosecondPosition(microseconds);
			} else {
				throw new ClipIsNotOpen();
			}
		} catch (TermIsNotAnInteger e) {
			throw new WrongArgumentIsNotAnInteger(value);
		}
	}
	//
	public void setLoopPoints2s(ChoisePoint iX, Term a1, Term a2) {
		try {
			BigInteger number1= Converters.termToRoundInteger(a1,iX,false);
			try {
				BigInteger number2= Converters.termToRoundInteger(a2,iX,false);
				int start= PrologInteger.toInteger(number1);
				int end= PrologInteger.toInteger(number2);
				if (commonClip != null) {
					commonClip.setLoopPoints(start,end);
				} else {
					throw new ClipIsNotOpen();
				}
			} catch (TermIsNotAnInteger e) {
				throw new WrongArgumentIsNotAnInteger(a2);
			}
		} catch (TermIsNotAnInteger e) {
			throw new WrongArgumentIsNotAnInteger(a1);
		}
	}
	//
	public void isOpen0s(ChoisePoint iX) throws Backtracking {
		if (commonClip != null) {
			if (!commonClip.isOpen()) {
				throw new Backtracking();
			}
		} else {
			throw new Backtracking();
		}
	}
	//
	public void isActive0s(ChoisePoint iX) throws Backtracking {
		if (commonClip != null) {
			if (!commonClip.isActive()) {
				throw new Backtracking();
			}
		} else {
			throw new Backtracking();
		}
	}
	//
	public void isRunning0s(ChoisePoint iX) throws Backtracking {
		if (commonClip != null) {
			if (!commonClip.isRunning()) {
				throw new Backtracking();
			}
		} else {
			throw new Backtracking();
		}
	}
	//
	protected URI retrieveLocationURI(ChoisePoint iX) {
		Term name= getBuiltInSlot_E_name();
		return retrieveLocationURI(name,iX);
	}
	protected URI retrieveLocationURI(Term name, ChoisePoint iX) {
		try {
			String textName= name.getStringValue(iX);
			textName= appendExtensionIfNecessary(textName,iX);
			boolean backslashIsSeparator= FileUtils.checkIfBackslashIsSeparator(getBuiltInSlot_E_backslash_is_separator_always(),iX);
			// System.out.printf("textName=>>>%s<<<\n",textName);
			URI uri= URL_Utils.create_URI(textName,staticContext,backslashIsSeparator);
			return uri;
		} catch (TermIsNotAString e) {
			throw new WrongArgumentIsNotAString(name);
		}
	}
	protected String appendExtensionIfNecessary(String textName, ChoisePoint iX) {
		if (textName.indexOf('.') == -1) {
			Term extension= getBuiltInSlot_E_extension();
			String textExtension= null;
			try {
				textExtension= extension.getStringValue(iX);
			} catch (TermIsNotAString e2) {
				throw new WrongArgumentIsNotAString(extension);
			};
			textName= textName + textExtension;
		};
		return textName;
	}
	protected int retrieveMaxWaitingTime(ChoisePoint iX) {
		Term interval= getBuiltInSlot_E_max_waiting_time();
		try {
			return URL_Utils.termToWaitingInterval(interval,iX);
		} catch (TermIsSymbolDefault e1) {
			try {
				return URL_Utils.termToWaitingInterval(DefaultOptions.waitingInterval,iX);
			} catch (TermIsSymbolDefault e2) {
				return defaultWaitingInterval;
			}
		}
	}
}
