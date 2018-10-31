// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.built_in;

import morozov.run.*;
import morozov.system.converters.*;
import morozov.system.errors.*;
import morozov.terms.*;
import morozov.worlds.*;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;

public abstract class ScreenCapture extends BufferedImageController {
	//
	protected static Robot robot;
	protected static Toolkit toolkit;
	//
	///////////////////////////////////////////////////////////////
	//
	public ScreenCapture() {
	}
	public ScreenCapture(GlobalWorldIdentifier id) {
		super(id);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void initializeRobotIfNecessary() {
		if (robot==null) {
			try {
				robot= new Robot();
				toolkit= Toolkit.getDefaultToolkit();
			} catch (AWTException e) {
				throw new PlatformConfigurationDoesNotAllowLowLevelInputControl();
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void getImage1s(ChoisePoint iX, Term a1) {
		initializeRobotIfNecessary();
		java.awt.image.BufferedImage bufferedImage= getBufferedImage();
		modifyImage(a1,bufferedImage,iX);
	}
	public void getImage5s(ChoisePoint iX, Term a1, Term a2, Term a3, Term a4, Term a5) {
		int x1= GeneralConverters.argumentToSmallInteger(a2,iX);
		int y1= GeneralConverters.argumentToSmallInteger(a3,iX);
		int width= GeneralConverters.argumentToSmallInteger(a4,iX);
		int height= GeneralConverters.argumentToSmallInteger(a5,iX);
		initializeRobotIfNecessary();
		Rectangle screenbounds= new Rectangle(x1,y1,width,height);
		java.awt.image.BufferedImage bufferedImage= getBufferedImage(screenbounds);
		modifyImage(a1,bufferedImage,iX);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected java.awt.image.BufferedImage getBufferedImage() {
		Rectangle screenbounds= new Rectangle(toolkit.getScreenSize());
		return getBufferedImage(screenbounds);
	}
	protected java.awt.image.BufferedImage getBufferedImage(Rectangle screenbounds) {
		return robot.createScreenCapture(screenbounds);
	}
}
