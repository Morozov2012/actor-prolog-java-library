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
	protected ActorPrologBacktrackableParser parser;
	protected int currentPosition;
	protected boolean useSecondVariableNameRegister;
	protected int firstRecentVariableNumber;
	protected int secondRecentVariableNumber;
	protected int firstRecentAnonymousVariableRegisterSize;
	protected int secondRecentAnonymousVariableRegisterSize;
	protected int firstRecentFunctionCallDefinitionArraySize;
	protected int secondRecentFunctionCallDefinitionArraySize;
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
		useSecondVariableNameRegister= parser.useSecondVariableNameRegister();
		firstRecentVariableNumber= parser.getFirstRecentVariableNumber();
		secondRecentVariableNumber= parser.getSecondRecentVariableNumber();
		firstRecentAnonymousVariableRegisterSize= parser.getFirstAnonymousVariableRegisterSize();
		secondRecentAnonymousVariableRegisterSize= parser.getSecondRecentVariableNumber();
		firstRecentFunctionCallDefinitionArraySize= parser.getFirstFunctionCallDefinitionArraySize();
		secondRecentFunctionCallDefinitionArraySize= parser.getSecondFunctionCallDefinitionArraySize();
	}
	//
	@Override
	public void clear() {
		int parserPosition= parser.getStackPosition();
		if (parserPosition <= currentPosition) {
			return;
		};
		parser.setStackPosition(currentPosition);
		parser.setUseSecondVariableNameRegister(useSecondVariableNameRegister);
		//
		int newFirstVariableNumber= parser.getFirstRecentVariableNumber();
		if (newFirstVariableNumber > firstRecentVariableNumber) {
			HashMap<String,Integer> firstVariableNameRegister= parser.getFirstVariableNameRegister();
			HashMap<Integer,String> firstInvertedVariableNameRegister= parser.getFirstInvertedVariableNameRegister();
			HashMap<Integer,VariableRole> firstVariableRoleRegister= parser.getFirstVariableRoleRegister();
			parser.setFirstRecentVariableNumber(firstRecentVariableNumber);
			for (int k=newFirstVariableNumber; k > firstRecentVariableNumber; k--) {
				String name= firstInvertedVariableNameRegister.remove(k);
				if (name != null) {
					firstVariableNameRegister.remove(name);
				};
				firstVariableRoleRegister.remove(k);
			}
		};
		//
		int newSecondVariableNumber= parser.getSecondRecentVariableNumber();
		if (newSecondVariableNumber > secondRecentVariableNumber) {
			HashMap<String,Integer> secondVariableNameRegister= parser.getSecondVariableNameRegister();
			HashMap<Integer,String> secondInvertedVariableNameRegister= parser.getSecondInvertedVariableNameRegister();
			HashMap<Integer,VariableRole> secondVariableRoleRegister= parser.getSecondVariableRoleRegister();
			parser.setSecondRecentVariableNumber(secondRecentVariableNumber);
			for (int k=newSecondVariableNumber; k > secondRecentVariableNumber; k--) {
				String name= secondInvertedVariableNameRegister.remove(k);
				if (name != null) {
					secondVariableNameRegister.remove(name);
				};
				secondVariableRoleRegister.remove(k);
			}
		};
		//
		int newFirstAnonymousVariableRegisterSize= parser.getFirstAnonymousVariableRegisterSize();
		if (newFirstAnonymousVariableRegisterSize > firstRecentAnonymousVariableRegisterSize) {
			ArrayList<Integer> anonymousVariableRegister= parser.getFirstAnonymousVariableRegister();
			int beginning= firstRecentAnonymousVariableRegisterSize + 1;
			int end= newFirstAnonymousVariableRegisterSize + 1;
			int currentArraySize= anonymousVariableRegister.size();
			if (beginning >= currentArraySize-1) {
				beginning= currentArraySize - 1;
			};
			if (end > currentArraySize) {
				end= currentArraySize;
			};
			anonymousVariableRegister.subList(beginning,end).clear();
		}
		//
		int newSecondAnonymousVariableRegisterSize= parser.getSecondAnonymousVariableRegisterSize();
		if (newSecondAnonymousVariableRegisterSize > secondRecentAnonymousVariableRegisterSize) {
			ArrayList<Integer> anonymousVariableRegister= parser.getSecondAnonymousVariableRegister();
			int beginning= secondRecentAnonymousVariableRegisterSize + 1;
			int end= newSecondAnonymousVariableRegisterSize + 1;
			int currentArraySize= anonymousVariableRegister.size();
			if (beginning >= currentArraySize-1) {
				beginning= currentArraySize - 1;
			};
			if (end > currentArraySize) {
				end= currentArraySize;
			};
			anonymousVariableRegister.subList(beginning,end).clear();
		}
		//
		int newFirstFunctionCallDefinitionArraySize= parser.getFirstFunctionCallDefinitionArraySize();
		if (newFirstFunctionCallDefinitionArraySize > firstRecentFunctionCallDefinitionArraySize) {
			ArrayList<FunctionCallDefinition> functionCallDefinitions= parser.getFirstFunctionCallDefinitions();
			int beginning= firstRecentFunctionCallDefinitionArraySize + 1;
			int end= newFirstFunctionCallDefinitionArraySize + 1;
			int currentArraySize= functionCallDefinitions.size();
			if (beginning >= currentArraySize-1) {
				beginning= currentArraySize - 1;
			};
			if (end > currentArraySize) {
				end= currentArraySize;
			};
			functionCallDefinitions.subList(beginning,end).clear();
		}
		//
		int newSecondFunctionCallDefinitionArraySize= parser.getSecondFunctionCallDefinitionArraySize();
		if (newSecondFunctionCallDefinitionArraySize > secondRecentFunctionCallDefinitionArraySize) {
			ArrayList<FunctionCallDefinition> functionCallDefinitions= parser.getSecondFunctionCallDefinitions();
			int beginning= secondRecentFunctionCallDefinitionArraySize + 1;
			int end= newSecondFunctionCallDefinitionArraySize + 1;
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
	// General "Unify With" function:
	@Override
	public void unifyWith(Term t, ChoisePoint cp) throws Backtracking {
		throw new SpecialTermCannotBeUnified();
	}
	@Override
	public String toString() {
		return "ActorPrologBacktrackableParserStackState";
	}
}
