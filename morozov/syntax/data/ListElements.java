// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data;

import morozov.run.*;
import morozov.syntax.*;
import morozov.syntax.errors.*;
import morozov.terms.*;

import java.util.ArrayList;

public class ListElements {
	//
	protected ArrayList<InternalTerm> argumentSequence;
	protected boolean hasTail;
	protected Term tail;
	protected int barPosition;
	//
	protected static Term[] emptyTermArray= new Term[0];
	//
	public ListElements(ArrayList<InternalTerm> arguments, boolean hT, Term t, int bP) {
		argumentSequence= arguments;
		hasTail= hT;
		tail= t;
		barPosition= bP;
	}
	//
	public ArrayList<InternalTerm> getArgumentSequence() {
		return argumentSequence;
	}
	public boolean hasTail() {
		return hasTail;
	}
	public Term getTail() {
		return tail;
	}
	public int getBarPosition() {
		return barPosition;
	}
	//
	public Term toMetaTerm(ElementaryParser parser) {
		if (argumentSequence != null) {
			Term result= tail;
			for (int k=argumentSequence.size()-1; k >= 0; k--) {
				InternalTerm internalTerm= argumentSequence.get(k);
				result= parser.createTermListElement(internalTerm.getValue(),result);
				result= parser.attachTermPositionIfNecessary(result,internalTerm.getPosition());
			};
			return result;
		} else {
			return tail;
		}
	}
	//
	public Term[] toTerms(ElementaryParser parser, ChoisePoint iX) throws ParserError {
		if (hasTail) {
			parser.handleError(new CommaIsExpected(barPosition),iX);
		};
		if (argumentSequence != null) {
			Term[] termArray= new Term[argumentSequence.size()];
			for (int k=0; k < argumentSequence.size(); k++) {
				InternalTerm internalTerm= argumentSequence.get(k);
				Term element= internalTerm.getValue();
				termArray[k]= element;
			};
			return termArray;
		} else {
			return emptyTermArray;
		}
	}
}
