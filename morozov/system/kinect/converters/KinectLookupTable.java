// (c) 2018 IRE RAS Alexei A. Morozov

package morozov.system.kinect.converters;

import morozov.run.*;
import morozov.syntax.scanner.*;
import morozov.syntax.scanner.errors.*;
import morozov.system.checker.signals.*;
import morozov.system.files.*;
import morozov.system.files.errors.*;
import morozov.system.kinect.converters.errors.*;
import morozov.system.kinect.modes.*;
import morozov.terms.*;

import java.math.BigInteger;

public class KinectLookupTable {
	//
	protected float[][][] lookupTable;
	protected int correctionX;
	protected int correctionY;
	//
	protected transient String recentErrorText;
	protected transient long recentErrorPosition= -1;
	protected transient Throwable recentErrorException;
	//
	protected KinectLookupTableType lookupTableType= KinectLookupTableType.VOLUMETRIC;
	//
	protected static int expectedNumberOfElementsInColumn2D= 2;
	protected static int expectedNumberOfElementsInColumn3D= 2 * 3;
	//
	public void loadLookupTable(ExtendedFileName fileName, int timeout, CharacterSet requestedCharacterSet, int cX, int cY, StaticContext staticContext, ChoisePoint iX) {
		correctionX= cX;
		correctionY= cY;
		recentErrorText= "";
		recentErrorPosition= -1;
		recentErrorException= null;
		try {
			String textBuffer= fileName.getTextData(timeout,requestedCharacterSet,staticContext);
			try {
				loadContent(textBuffer,iX);
			} catch (LexicalScannerError e) {
				recentErrorText= textBuffer.toString();
				recentErrorPosition= e.getPosition();
				recentErrorException= e;
				throw e;
			} catch (RuntimeException e) {
				if (recentErrorException==null) {
					recentErrorException= e;
				};
				throw e;
			}
		} catch (CannotRetrieveContent e) {
			throw new FileInputOutputError(fileName.toString(),e);
		}
	}
	//
	public void loadContent(String text, ChoisePoint iX) throws LexicalScannerError {
		LexicalScanner scanner= new LexicalScanner(false);
		PrologToken[] tokens= scanner.analyse(text,false,true);
		int numberOfTokens= tokens.length;
		int numberOfElements= 0;
		loop: for (int k=0; k < numberOfTokens; k++) {
			PrologToken token= tokens[k];
			PrologTokenType type= token.getType();
			switch (type) {
			case MINUS:
				continue;
			case END_OF_LINE:
				break loop;
			case END_OF_TEXT:
				break loop;
			default:
				numberOfElements++;
			}
		};
		int numberOfRows= 0;
		boolean isBeginningOfLine= true;
		loop: for (int k=0; k < numberOfTokens; k++) {
			PrologToken token= tokens[k];
			PrologTokenType type= token.getType();
			switch (type) {
			case MINUS:
				continue;
			case END_OF_LINE:
				isBeginningOfLine= true;
				continue;
			case END_OF_TEXT:
				break loop;
			default:
				if (isBeginningOfLine) {
					numberOfRows++;
					isBeginningOfLine= false;
				}
			}
		};
		int numberOfElementsInColumn;
		if ((numberOfElements / expectedNumberOfElementsInColumn3D) * expectedNumberOfElementsInColumn3D == numberOfElements) {
			lookupTableType= KinectLookupTableType.VOLUMETRIC;
			numberOfElementsInColumn= expectedNumberOfElementsInColumn3D;
		} else {
			lookupTableType= KinectLookupTableType.FLAT;
			numberOfElementsInColumn= expectedNumberOfElementsInColumn2D;
		};
		int numberOfColumns= numberOfElements / numberOfElementsInColumn;
		lookupTable= new float[numberOfColumns][numberOfRows][numberOfElementsInColumn];
		int row= 0;
		int column= 0;
		boolean afterMinus= false;
		int indexOfElement= 0;
		loop: for (int k=0; k < numberOfTokens; k++) {
			PrologToken token= tokens[k];
			PrologTokenType type= token.getType();
			switch (type) {
			case MINUS:
				afterMinus= true;
				continue;
			case END_OF_LINE:
				if (column*numberOfElementsInColumn != numberOfElements) {
					throw new IllegalNumberOfColumnsInLookupTable(column,numberOfColumns);
				};
				column= 0;
				row++;
				afterMinus= false;
				continue;
			case END_OF_TEXT:
				break loop;
			default:
				if (type==PrologTokenType.INTEGER) {
					TokenInteger10 integer= (TokenInteger10)token;
					BigInteger value= integer.getIntegerValue();
					if (afterMinus) {
						value= value.negate();
					};
					lookupTable[column][row][indexOfElement]= PrologInteger.toInteger(value);
					indexOfElement++;
					if (indexOfElement >= numberOfElementsInColumn) {
						indexOfElement= 0;
						column++;
					}
				} else if (type==PrologTokenType.REAL) {
					TokenReal real= (TokenReal)token;
					double value= real.getRealValue();
					if (afterMinus) {
						value= - value;
					};
					lookupTable[column][row][indexOfElement]= (float)value;
					indexOfElement++;
					if (indexOfElement >= numberOfElementsInColumn) {
						indexOfElement= 0;
						column++;
					}
				};
				afterMinus= false;
			}
		};
		if (row != numberOfRows) {
			throw new IllegalNumberOfRowsInLookupTable(row,numberOfRows);
		}
	}
	//
	public void resetLookupTable() {
		lookupTable= null;
		correctionX= 0;
		correctionY= 0;
	}
	//
	public KinectLookupTableType getLookupTableType() {
		return lookupTableType;
	}
	public boolean isFlatLookupTable() {
		return lookupTableType==KinectLookupTableType.FLAT;
	}
	public boolean isVolumetricLookupTable() {
		return lookupTableType==KinectLookupTableType.VOLUMETRIC;
	}
	public float[][][] getLookupTable() {
		return lookupTable;
	}
	public int getCorrectionX() {
		return correctionX;
	}
	public int getCorrectionY() {
		return correctionY;
	}
}
