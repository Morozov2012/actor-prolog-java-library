// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.run;

import morozov.built_in.*;
import morozov.syntax.data.*;
import morozov.terms.*;
import morozov.worlds.errors.*;

import java.util.HashMap;
import java.util.ArrayList;

public class ActorPrologBacktrackableParserStackState extends Term {
	//
	private ActorPrologBacktrackableParser parser;
	private int currentPosition;
	private int recentVariableNumber;
	private int recentFunctionCallDefinitionArraySize;
	//
	private static final long serialVersionUID= 0x3FA943F5CF50F83AL; // 458727241847779
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.run","ActorPrologBacktrackableParserStackState");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public ActorPrologBacktrackableParserStackState(ActorPrologBacktrackableParser p) {
		parser= p;
		currentPosition= parser.getStackPosition();
		recentVariableNumber= parser.getRecentVariableNumber();
		recentFunctionCallDefinitionArraySize= parser.getFunctionCallDefinitionArraySize();
	}
	//
	public void clear() {
		parser.setStackPosition(currentPosition);
		int newVariableNumber= parser.getRecentVariableNumber();
		if (newVariableNumber > recentVariableNumber) {
			HashMap<String,Integer> variableNameRegister= parser.getVariableNameRegister();
			HashMap<Integer,String> invertedVariableNameRegister= parser.getInvertedVariableNameRegister();
			HashMap<Integer,VariableRole> variableRoleRegister= parser.getVariableRoleRegister();
			parser.setRecentVariableNumber(recentVariableNumber);
			for (int k=newVariableNumber; k > recentVariableNumber; k--) {
				String name= invertedVariableNameRegister.remove(k);
				if (name != null) {
					variableNameRegister.remove(name);
				};
				variableRoleRegister.remove(k);
			}
		};
		int newFunctionCallDefinitionArraySize= parser.getFunctionCallDefinitionArraySize();
		if (newFunctionCallDefinitionArraySize > recentFunctionCallDefinitionArraySize) {
			ArrayList<FunctionCallDefinition> functionCallDefinitions= parser.getFunctionCallDefinitions();
			int beginning= recentFunctionCallDefinitionArraySize + 1;
			int end= newFunctionCallDefinitionArraySize + 1;
			int currentArraySize= functionCallDefinitions.size();
			if (beginning >= currentArraySize-1) {
				beginning= currentArraySize - 1;
			};
			if (end > currentArraySize) {
				end= currentArraySize;
			};
			functionCallDefinitions.subList(beginning,end).clear();
		}
	}
	// General "Unify With" function
	public void unifyWith(Term t, ChoisePoint cp) throws Backtracking {
		throw new SpecialTermCannotBeUnified();
	}
	public String toString() {
		return "ActorPrologBacktrackableParserStackState";
	}
}
