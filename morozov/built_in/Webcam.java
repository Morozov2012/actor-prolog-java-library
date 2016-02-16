// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.built_in;

import morozov.run.*;
import morozov.system.*;
import morozov.system.gui.*;
import morozov.system.gui.signals.*;
import morozov.system.signals.*;
import morozov.system.webcam.*;
import morozov.system.webcam.errors.*;
import morozov.terms.*;
import morozov.worlds.*;

import com.github.sarxos.webcam.WebcamDiscoveryListener;
import com.github.sarxos.webcam.WebcamListener;
import com.github.sarxos.webcam.WebcamDiscoveryEvent;
import com.github.sarxos.webcam.WebcamEvent;

import java.awt.Dimension;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

public abstract class Webcam
		extends BufferedImageController
		implements WebcamDiscoveryListener, WebcamListener {
	//
	public WebcamName defaultName= null;
	// public WaitingInterval maxWaitingTime= null;
	//
	protected com.github.sarxos.webcam.Webcam currentWebcam= null;
	protected boolean isOpen= false;
	protected boolean isWebcamArrayListener= false;
	protected boolean isWebcamStateListener= false;
	//
	protected static long longDefaultInterval= 1000 * 60;	// 1 minute
	protected static long longAnyInterval= Long.MAX_VALUE;
	//
	///////////////////////////////////////////////////////////////
	//
	abstract protected Term getBuiltInSlot_E_default_name();
	abstract protected Term getBuiltInSlot_E_max_waiting_time();
	//
	public abstract long entry_s_WebcamAdded_1_i();
	public abstract long entry_s_WebcamRemoved_1_i();
	public abstract long entry_s_WebcamOpen_1_i();
	public abstract long entry_s_WebcamImageObtained_1_i();
	public abstract long entry_s_WebcamClosed_1_i();
	public abstract long entry_s_WebcamDisposed_1_i();
	//
	///////////////////////////////////////////////////////////////
	//
	public Webcam() {
	}
	public Webcam(GlobalWorldIdentifier id) {
		super(id);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	// get/set defaultName
	//
	public void setDefaultName1s(ChoisePoint iX, Term a1) {
		setDefaultName(WebcamName.termToWebcamName(a1,iX));
	}
	public void setDefaultName(WebcamName value) {
		defaultName= value;
	}
	public void getDefaultName0ff(ChoisePoint iX, PrologVariable a1) {
		a1.value= getDefaultName(iX).toTerm();
	}
	public void getDefaultName0fs(ChoisePoint iX) {
	}
	public WebcamName getDefaultName(ChoisePoint iX) {
		if (defaultName != null) {
			return defaultName;
		} else {
			Term value= getBuiltInSlot_E_default_name();
			return WebcamName.termToWebcamName(value,iX);
		}
	}
	//
	// get/set maxWaitingTime
	//
	// public void setMaxWaitingTime1s(ChoisePoint iX, Term a1) {
	//	setMaxWaitingTime(WaitingInterval.termToWaitingInterval(a1,iX));
	// }
	// public void setMaxWaitingTime(WaitingInterval value) {
	//	maxWaitingTime= value;
	// }
	// public void getMaxWaitingTime0ff(ChoisePoint iX, PrologVariable a1) {
	//	a1.value= getMaxWaitingTime(iX).toTerm();
	// }
	// public void getMaxWaitingTime0fs(ChoisePoint iX) {
	// }
	// public WaitingInterval getMaxWaitingTime(ChoisePoint iX) {
	//	if (maxWaitingTime != null) {
	//		return maxWaitingTime;
	//	} else {
	//		Term value= getBuiltInSlot_E_max_waiting_time();
	//		return WaitingInterval.termToWaitingInterval(value,iX);
	//	}
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	// get/set Default Resolution
	//
	public void setDefaultResolution1s(ChoisePoint iX, Term a1) {
		WebcamResolution resolution= WebcamResolution.termToWebcamResolution(a1,iX);
		setWidth(resolution.getExtendedWidth());
		setHeight(resolution.getExtendedHeight());
	}
	public void setDefaultResolution2s(ChoisePoint iX, Term a1, Term a2) {
		setWidth(ExtendedSize.termToExtendedSize(a1,iX));
		setHeight(ExtendedSize.termToExtendedSize(a2,iX));
	}
	public void getDefaultResolution2s(ChoisePoint iX, PrologVariable a1, PrologVariable a2) {
		a1.value= getWidth(iX).toTerm();
		a2.value= getHeight(iX).toTerm();
		iX.pushTrail(a1);
		iX.pushTrail(a2);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void open0ff(ChoisePoint iX, PrologVariable a1) {
		WebcamName webcamName= getDefaultName(iX);
		WaitingInterval interval= getMaxWaitingTime(iX);
		boolean result= open(webcamName,interval,iX);
		a1.value= YesNo.boolean2TermYesNo(result);
	}
	public void open0fs(ChoisePoint iX) {
		WebcamName webcamName= getDefaultName(iX);
		WaitingInterval interval= getMaxWaitingTime(iX);
		open(webcamName,interval,iX);
	}
	public void open1ff(ChoisePoint iX, PrologVariable a1, Term a2) {
		WebcamName webcamName= WebcamName.termToWebcamName(a2,iX);
		WaitingInterval interval= getMaxWaitingTime(iX);
		boolean result= open(webcamName,interval,iX);
		a1.value= YesNo.boolean2TermYesNo(result);
	}
	public void open1fs(ChoisePoint iX, Term a2) {
		WebcamName webcamName= WebcamName.termToWebcamName(a2,iX);
		WaitingInterval interval= getMaxWaitingTime(iX);
		open(webcamName,interval,iX);
	}
	public void open2ff(ChoisePoint iX, PrologVariable a1, Term a2, Term a3) {
		WebcamName webcamName= WebcamName.termToWebcamName(a2,iX);
		WaitingInterval interval= WaitingInterval.termToWaitingInterval(a3,iX);
		boolean result= open(webcamName,interval,iX);
		a1.value= YesNo.boolean2TermYesNo(result);
	}
	public void open2fs(ChoisePoint iX, Term a2, Term a3) {
		WebcamName webcamName= WebcamName.termToWebcamName(a2,iX);
		WaitingInterval interval= WaitingInterval.termToWaitingInterval(a3,iX);
		open(webcamName,interval,iX);
	}
	//
	protected boolean open(WebcamName webcamName, WaitingInterval interval, ChoisePoint iX) {
		ExtendedSize eW= getWidth(iX);
		ExtendedSize eH= getHeight(iX);
		if (isOpen) {
			return true;
		};
		try {
			try {
				String textName= webcamName.getTextName();
				List<com.github.sarxos.webcam.Webcam> webcams;
				try {
					long timeout= interval.toMillisecondsLongOrDefault();
					webcams= com.github.sarxos.webcam.Webcam.getWebcams(timeout);
				} catch (UseDefaultInterval e) {
					webcams= com.github.sarxos.webcam.Webcam.getWebcams(longDefaultInterval);
				} catch (UseAnyInterval e) {
					webcams= com.github.sarxos.webcam.Webcam.getWebcams(longAnyInterval);
				};
				boolean nameIsFound= false;
				for (com.github.sarxos.webcam.Webcam webcam : webcams) {
					if (textName.equals(webcam.getName())) {
						currentWebcam= webcam;
						nameIsFound= true;
						break;
					}
				};
				if (!nameIsFound) {
					throw new WebcamIsNotFound(textName);
				}
			} catch (UseDefaultName e1) {
				try {
					long timeout= interval.toMillisecondsLongOrDefault();
					currentWebcam= com.github.sarxos.webcam.Webcam.getDefault(timeout);
				} catch (UseDefaultInterval e) {
					currentWebcam= com.github.sarxos.webcam.Webcam.getDefault(longDefaultInterval);
				} catch (UseAnyInterval e) {
					currentWebcam= com.github.sarxos.webcam.Webcam.getDefault(longAnyInterval);
				};
				if (currentWebcam==null) {
					throw new DefaultWebcamIsNotFound();
				}
			};
			if (currentWebcam==null) {
				throw new WebcamIsNotFound(webcamName.toString());
			};
			if (isWebcamStateListener) {
				currentWebcam.addWebcamListener(this);
			} else {
				currentWebcam.removeWebcamListener(this);
			};
			try {
				int viewWidth= eW.getIntegerValue();
				int viewHeight= eH.getIntegerValue();
				Dimension viewSize= new Dimension(viewWidth,viewHeight);
				currentWebcam.setCustomViewSizes(new Dimension[]{viewSize});
				currentWebcam.setViewSize(viewSize);
			} catch(UseDefaultSize e) {
			};
			boolean result= currentWebcam.open(true);
			isOpen= result;
			if (!result) {
				currentWebcam= null;
			};
			return result;
		} catch (TimeoutException e) {
			currentWebcam= null;
			throw new WebcamTimeoutException();
		}
	}
	//
	public void close0s(ChoisePoint iX) {
		if (isOpen) {
			if (currentWebcam != null) {
				currentWebcam.close();
				currentWebcam= null;
			};
			isOpen= false;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void isOpen0s(ChoisePoint iX) throws Backtracking {
		if (isOpen) {
			if (currentWebcam != null) {
				if (!currentWebcam.isOpen()) {
					throw Backtracking.instance;
				}
			} else {
				throw Backtracking.instance;
			}
		} else {
			throw Backtracking.instance;
		}
	}
	//
	public void containsNewImage0s(ChoisePoint iX) throws Backtracking {
		if (isOpen) {
			if (currentWebcam != null) {
				if (!currentWebcam.isImageNew()) {
					throw Backtracking.instance;
				}
			} else {
				throw Backtracking.instance;
			}
		} else {
			throw Backtracking.instance;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void getActualName0ff(ChoisePoint iX, PrologVariable a1) {
		if (isOpen) {
			if (currentWebcam != null) {
				a1.value= new PrologString(currentWebcam.getName());
			} else {
				throw new WebcamIsNotOpen();
			}
		} else {
			throw new WebcamIsNotOpen();
		}
	}
	public void getActualName0fs(ChoisePoint iX) {
		if (isOpen) {
			if (currentWebcam == null) {
				throw new WebcamIsNotOpen();
			}
		} else {
			throw new WebcamIsNotOpen();
		}
	}
	//
	public void getFPS0ff(ChoisePoint iX, PrologVariable a1) {
		if (isOpen) {
			if (currentWebcam != null) {
				a1.value= new PrologReal(currentWebcam.getFPS());
			} else {
				throw new WebcamIsNotOpen();
			}
		} else {
			throw new WebcamIsNotOpen();
		}
	}
	public void getFPS0fs(ChoisePoint iX) {
		if (isOpen) {
			if (currentWebcam == null) {
				throw new WebcamIsNotOpen();
			}
		} else {
			throw new WebcamIsNotOpen();
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void getImage1s(ChoisePoint iX, Term value) {
		if (isOpen) {
			if (currentWebcam != null) {
				java.awt.image.BufferedImage nativeImage= currentWebcam.getImage();
				modifyImage(value,nativeImage,iX);
			} else {
				throw new WebcamIsNotOpen();
			}
		} else {
			throw new WebcamIsNotOpen();
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setActualResolution1s(ChoisePoint iX, Term a1) {
		WebcamResolution resolution= WebcamResolution.termToWebcamResolution(a1,iX);
		setActualResolution(resolution.getWidth(),resolution.getHeight());
	}
	public void setActualResolution2s(ChoisePoint iX, Term a1, Term a2) {
		int viewWidth= Converters.argumentToSmallInteger(a1,iX);
		int viewHeight= Converters.argumentToSmallInteger(a2,iX);
		setActualResolution(viewWidth,viewHeight);
	}
	//
	protected void setActualResolution(int viewWidth, int viewHeight) {
		if (isOpen) {
			if (currentWebcam != null) {
				Dimension viewSize= new Dimension(viewWidth,viewHeight);
				isOpen= false;
				currentWebcam.close();
				currentWebcam.setCustomViewSizes(new Dimension[]{viewSize});
				currentWebcam.setViewSize(viewSize);
				boolean result= currentWebcam.open(true);
				if (result) {
					isOpen= true;
				} else {
					currentWebcam= null;
				}
			} else {
				throw new WebcamIsNotOpen();
			}
		} else {
			throw new WebcamIsNotOpen();
		}
	}
	//
	public void getActualResolution2s(ChoisePoint iX, PrologVariable a1, PrologVariable a2) {
		if (isOpen) {
			if (currentWebcam != null) {
				Dimension size= currentWebcam.getViewSize();
				a1.value= new PrologInteger(size.width);
				a2.value= new PrologInteger(size.height);
			} else {
				throw new WebcamIsNotOpen();
			}
		} else {
			throw new WebcamIsNotOpen();
		}
	}
	//
	public void getViewSizes0ff(ChoisePoint iX, PrologVariable a1) {
		if (isOpen) {
			if (currentWebcam != null) {
				Dimension[] sizes= currentWebcam.getViewSizes();
				a1.value= Converters.dimensionArrayToList(sizes);
			} else {
				throw new WebcamIsNotOpen();
			}
		} else {
			throw new WebcamIsNotOpen();
		}
	}
	public void getViewSizes0fs(ChoisePoint iX) {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void getWebcamList0ff(ChoisePoint iX, PrologVariable a1) {
		WaitingInterval interval= getMaxWaitingTime(iX);
		a1.value= getWebcamList(interval);
	}
	public void getWebcamList0fs(ChoisePoint iX) {
	}
	public void getWebcamList1ff(ChoisePoint iX, PrologVariable a1, Term a2) {
		WaitingInterval interval= WaitingInterval.termToWaitingInterval(a2,iX);
		a1.value= getWebcamList(interval);
	}
	public void getWebcamList1fs(ChoisePoint iX, Term a2) {
	}
	//
	protected Term getWebcamList(WaitingInterval interval) {
		try {
			List<com.github.sarxos.webcam.Webcam> webcams;
			try {
				long timeout= interval.toMillisecondsLongOrDefault();
				webcams= com.github.sarxos.webcam.Webcam.getWebcams(timeout);
			} catch (UseDefaultInterval e) {
				webcams= com.github.sarxos.webcam.Webcam.getWebcams(longDefaultInterval);
			} catch (UseAnyInterval e) {
				webcams= com.github.sarxos.webcam.Webcam.getWebcams(longAnyInterval);
			};
			ArrayList<String> names= new ArrayList<>();
			for (com.github.sarxos.webcam.Webcam webcam : webcams) {
				names.add(webcam.getName());
			};
			Term result= Converters.stringArrayToList(names);
			return result;
		} catch (TimeoutException e) {
			throw new WebcamTimeoutException();
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void watchWebcamList1s(ChoisePoint iX, PrologVariable a1) {
		if (isWebcamArrayListener) {
			a1.value= termYes;
		} else {
			a1.value= termNo;
		}
	}
	public void watchWebcamList1s(ChoisePoint iX, Term a1) {
		YesNo mode= YesNo.term2YesNo(a1,iX);
		if (mode==YesNo.YES) {
			if (!isWebcamArrayListener) {
				com.github.sarxos.webcam.Webcam.getWebcams();
				com.github.sarxos.webcam.Webcam.addDiscoveryListener(this);
				isWebcamArrayListener= true;
			}
		} else {
			if (isWebcamArrayListener) {
				com.github.sarxos.webcam.Webcam.removeDiscoveryListener(this);
				isWebcamArrayListener= false;
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void webcamFound(WebcamDiscoveryEvent event) {
		long domainSignature= entry_s_WebcamAdded_1_i();
		processWebcamDiscoveryEvent(domainSignature,event);
	}
	public void webcamGone(WebcamDiscoveryEvent event) {
		long domainSignature= entry_s_WebcamRemoved_1_i();
		processWebcamDiscoveryEvent(domainSignature,event);
	}
	//
	protected void processWebcamDiscoveryEvent(long domainSignature, WebcamDiscoveryEvent event) {
		Term predicateArgument= new PrologString(event.getWebcam().getName());
		Term[] arguments= new Term[]{predicateArgument};
		AsyncCall call= new AsyncCall(domainSignature,this,true,false,arguments,true);
		transmitAsyncCall(call,null);
	}
	//
	public void webcamAdded1s(ChoisePoint iX, Term a1) {
	}
	//
	public void webcamRemoved1s(ChoisePoint iX, Term a1) {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void watchWebcamState1s(ChoisePoint iX, PrologVariable a1) {
		if (isWebcamStateListener) {
			a1.value= termYes;
		} else {
			a1.value= termNo;
		}
	}
	public void watchWebcamState1s(ChoisePoint iX, Term a1) {
		YesNo mode= YesNo.term2YesNo(a1,iX);
		if (isOpen) {
			if (currentWebcam != null) {
				if (mode==YesNo.YES) {
					if (!isWebcamStateListener) {
						currentWebcam.addWebcamListener(this);
						isWebcamStateListener= true;
					}
				} else {
					if (isWebcamStateListener) {
						currentWebcam.removeWebcamListener(this);
						isWebcamStateListener= false;
					}
				}
			} else {
				// throw new WebcamIsNotOpen();
				if (mode==YesNo.YES) {
					isWebcamStateListener= true;
				} else {
					isWebcamStateListener= false;
				}
			}
		} else {
			// throw new WebcamIsNotOpen();
			if (mode==YesNo.YES) {
				isWebcamStateListener= true;
			} else {
				isWebcamStateListener= false;
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void webcamOpen(WebcamEvent event) {
		long domainSignature= entry_s_WebcamOpen_1_i();
		processWebcamEvent(domainSignature,event);
	}
	public void webcamImageObtained(WebcamEvent event) {
		long domainSignature= entry_s_WebcamImageObtained_1_i();
		processWebcamEvent(domainSignature,event);
	}
	public void webcamClosed(WebcamEvent event) {
		long domainSignature= entry_s_WebcamClosed_1_i();
		processWebcamEvent(domainSignature,event);
	}
	public void webcamDisposed(WebcamEvent event) {
		long domainSignature= entry_s_WebcamDisposed_1_i();
		processWebcamEvent(domainSignature,event);
	}
	//
	protected void processWebcamEvent(long domainSignature, WebcamEvent event) {
		Term predicateArgument= new PrologString(event.getSource().getName());
		Term[] arguments= new Term[]{predicateArgument};
		AsyncCall call= new AsyncCall(domainSignature,this,true,false,arguments,true);
		transmitAsyncCall(call,null);
	}
	//
	public void webcamOpen1s(ChoisePoint iX, Term a1) {
	}
	public void webcamImageObtained1s(ChoisePoint iX, Term a1) {
	}
	public void webcamClosed1s(ChoisePoint iX, Term a1) {
	}
	public void webcamDisposed1s(ChoisePoint iX, Term a1) {
	}
}
