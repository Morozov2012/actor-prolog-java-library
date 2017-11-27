// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.built_in;

import morozov.run.*;
import morozov.syntax.scanner.*;
// import morozov.syntax.scanner.signals.*;
import morozov.system.*;
import morozov.terms.*;
import morozov.worlds.*;

import java.util.ArrayList;
import java.util.HashSet;

public abstract class ActorPrologScanner extends Text {
	//
	protected String[] keywords;
	protected Boolean keepTextPositions;
	//
	///////////////////////////////////////////////////////////////
	//
	public ActorPrologScanner() {
	}
	public ActorPrologScanner(GlobalWorldIdentifier id) {
		super(id);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	abstract protected Term getBuiltInSlot_E_keywords();
	abstract protected Term getBuiltInSlot_E_keep_text_positions();
	//
	///////////////////////////////////////////////////////////////
	//
	// get/set keywords
	//
	public void setKeywords1s(ChoisePoint iX, Term a1) {
		setKeywords(Converters.termToStrings(a1,iX));
	}
	public void setKeywords(String[] value) {
		keywords= value;
	}
	public void getKeywords0ff(ChoisePoint iX, PrologVariable result) {
		String[] value= getKeywords(iX);
		result.setNonBacktrackableValue(Converters.stringArrayToList(value));
	}
	public void getKeywords0fs(ChoisePoint iX) {
	}
	public String[] getKeywords(ChoisePoint iX) {
		if (keywords != null) {
			return keywords;
		} else {
			Term value= getBuiltInSlot_E_keywords();
			return Converters.termToStrings(value,iX);
		}
	}
	//
	// get/set keepTextPositions
	//
	public void setKeepTextPositions1s(ChoisePoint iX, Term a1) {
		setKeepTextPositions(YesNo.termYesNo2Boolean(a1,iX));
	}
	public void setKeepTextPositions(boolean value) {
		keepTextPositions= value;
	}
	public void getKeepTextPositions0ff(ChoisePoint iX, PrologVariable result) {
		boolean value= getKeepTextPositions(iX);
		result.setNonBacktrackableValue(YesNo.boolean2TermYesNo(value));
	}
	public void getKeepTextPositions0fs(ChoisePoint iX) {
	}
	public boolean getKeepTextPositions(ChoisePoint iX) {
		if (keepTextPositions != null) {
			return keepTextPositions;
		} else {
			Term value= getBuiltInSlot_E_keep_text_positions();
			return YesNo.termYesNo2Boolean(value,iX);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void convertToTokens1ff(ChoisePoint iX, PrologVariable result, Term a1) {
		String text= Converters.argumentToString(a1,iX);
		String[] keywordArray= getKeywords(iX);
		boolean includeTextPositions= getKeepTextPositions(iX);
		HashSet<String> keywordHash= new HashSet<>();
		HashSet<String> reservedNameHash= new HashSet<>();
		for (int k=0; k < keywordArray.length; k++) {
			String keyword= keywordArray[k];
			keywordHash.add(keyword);
			reservedNameHash.add(keyword.toLowerCase());
		};
		LexicalScanner scanner= new LexicalScanner(false,keywordHash,reservedNameHash);
		PrologToken[] tokens= scanner.analyse(text);
		ArrayList<Term> tokenTermArray= new ArrayList<>();
		int counter= 0;
		while (counter < tokens.length) {
			PrologToken token= tokens[counter];
			Term term;
			if (includeTextPositions) {
				term= token.toTermTP();
			} else {
				term= token.toTerm();
			};
			tokenTermArray.add(term);
			counter++;
		};
		Term tokenListTerm= PrologEmptyList.instance;
		for (int k=tokenTermArray.size()-1; k >= 0; k--) {
			tokenListTerm= new PrologList(tokenTermArray.get(k),tokenListTerm);
		};
		result.setNonBacktrackableValue(tokenListTerm);
	}
	public void convertToTokens1fs(ChoisePoint iX, Term a1) {
	}
}
