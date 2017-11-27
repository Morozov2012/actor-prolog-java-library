// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.built_in;

//import target.*;

import morozov.run.*;
import morozov.system.*;
import morozov.system.errors.*;
//import morozov.system.files.*;
//import morozov.system.gui.*;
//import morozov.system.gui.signals.*;
//import morozov.system.gui.space2d.*;
import morozov.terms.*;
import morozov.worlds.*;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;

//import java.awt.Color;
//import java.awt.Rectangle;
//import java.awt.GraphicsConfiguration;
//import java.awt.Graphics2D;
//import java.util.concurrent.atomic.AtomicReference;

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
		int x1= Converters.argumentToSmallInteger(a2,iX);
		int y1= Converters.argumentToSmallInteger(a3,iX);
		int width= Converters.argumentToSmallInteger(a4,iX);
		int height= Converters.argumentToSmallInteger(a5,iX);
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
