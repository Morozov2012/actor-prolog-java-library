// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.gui;

import target.*;

import morozov.terms.*;
import java.awt.event.MouseEvent;

public class MenuUtils {
	public static Term mouseEvent2Term(MouseEvent ev, int windowWidth, int windowHeight, int sizeFactor, long mouseTypeCode) {
		//
		Term value= PrologEmptySet.instance;
		long flag= SymbolCodes.symbolCode_E_no;
		//
		if (ev.isShiftDown()) {
			flag= SymbolCodes.symbolCode_E_yes;
		} else {
			flag= SymbolCodes.symbolCode_E_no;
		};
		value= new PrologSet(-SymbolCodes.symbolCode_E_isShiftDown,new PrologSymbol(flag),value);
		//
		if (ev.isMetaDown()) {
			flag= SymbolCodes.symbolCode_E_yes;
		} else {
			flag= SymbolCodes.symbolCode_E_no;
		};
		value= new PrologSet(-SymbolCodes.symbolCode_E_isMetaDown,new PrologSymbol(flag),value);
		//
		if (ev.isControlDown()) {
			flag= SymbolCodes.symbolCode_E_yes;
		} else {
			flag= SymbolCodes.symbolCode_E_no;
		};
		value= new PrologSet(-SymbolCodes.symbolCode_E_isControlDown,new PrologSymbol(flag),value);
		//
		if (ev.isAltGraphDown()) {
			flag= SymbolCodes.symbolCode_E_yes;
		} else {
			flag= SymbolCodes.symbolCode_E_no;
		};
		value= new PrologSet(-SymbolCodes.symbolCode_E_isAltGraphDown,new PrologSymbol(flag),value);
		//
		if (ev.isAltDown()) {
			flag= SymbolCodes.symbolCode_E_yes;
		} else {
			flag= SymbolCodes.symbolCode_E_no;
		};
		value= new PrologSet(-SymbolCodes.symbolCode_E_isAltDown,new PrologSymbol(flag),value);
		//
		long when= ev.getWhen();
		value= new PrologSet(-SymbolCodes.symbolCode_E_when,new PrologInteger(when),value);
		//
		double x= ev.getX();
		if (sizeFactor < 0) {
			x= x / windowWidth;
		} else {
			x= x / sizeFactor;
		};
		value= new PrologSet(-SymbolCodes.symbolCode_E_x,new PrologReal(x),value);
		//
		double y= ev.getY();
		if (sizeFactor < 0) {
			y= y / windowHeight;
		} else {
			y= y / sizeFactor;
		};
		value= new PrologSet(-SymbolCodes.symbolCode_E_y,new PrologReal(y),value);
		//
		int count= ev.getClickCount();
		value= new PrologSet(-SymbolCodes.symbolCode_E_clickCount,new PrologInteger(count),value);
		//
		int button= ev.getButton();
		value= new PrologSet(-SymbolCodes.symbolCode_E_button,new PrologInteger(button),value);
		//
		value= new PrologSet(-UnderdeterminedSet.keyNameCode,new PrologSymbol(mouseTypeCode),value);
		//
		return value;
	}
}
