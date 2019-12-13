// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.run;

import target.*;

import morozov.run.errors.*;
import morozov.syntax.scanner.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Collections;

public abstract class SymbolTable {
	//
	protected static SymbolName[] initialContent;
	protected static Map<Integer,SymbolName> symbolNameHash= Collections.synchronizedMap(new HashMap<Integer,SymbolName>());
	protected static Map<String,Integer> symbolCodeHash= Collections.synchronizedMap(new HashMap<String,Integer>());
	protected static volatile int staticTableSize;
	protected static volatile int totalTableSize;
	//
	protected static int[][] predefinedClassNames;
	protected static Map<Integer,Integer> predefinedClassNameMap= Collections.synchronizedMap(new HashMap<Integer,Integer>());
	//
	protected final static int firstSymbolCode= 100;
	//
	public static void initializeSymbolTable() {
		synchronized (symbolCodeHash) {
			for (int k=0; k < initialContent.length; k++) {
				symbolNameHash.put(k,initialContent[k]);
				symbolCodeHash.put(initialContent[k].identifier,k);
			};
			staticTableSize= initialContent.length;
			totalTableSize= initialContent.length;
			int numberOfPredefinedClasses= predefinedClassNames.length;
			for (int k=0; k < numberOfPredefinedClasses; k++) {
				int[] pair= predefinedClassNames[k];
				predefinedClassNameMap.put(pair[0],pair[1]);
			}
		}
	}
	public static SymbolName retrieveSymbolName(long value) {
		if (value - firstSymbolCode > Integer.MAX_VALUE) {
			throw new IncorrecSymbolCode();
		};
		int absoluteCode= (int)(value-firstSymbolCode);
		if (SymbolNames.staticTableSize > absoluteCode) {
			return initialContent[absoluteCode];
		} else {
			SymbolName name= symbolNameHash.get(absoluteCode);
			if (name==null) {
				throw new UnknownSymbolCode();
			} else {
				return name;
			}
		}
	}
	public static int retrieveSymbolCode(String name) {
		Integer code= symbolCodeHash.get(name);
		if (code==null) {
			throw new UnknownSymbolName();
		} else {
			if (code > Integer.MAX_VALUE - firstSymbolCode) {
				throw new IncorrecSymbolCode();
			} else {
				return code + firstSymbolCode;
			}
		}
	}
	public static int insertSymbolName(String name) {
		synchronized (symbolCodeHash) {
			Integer code= symbolCodeHash.get(name);
			if (code==null) {
				totalTableSize++;
				if (totalTableSize > Integer.MAX_VALUE - firstSymbolCode) {
					throw new SymbolTableOverflow();
				} else {
					boolean isSafe= LexicalScanner.isSafeIdentifier(name);
					symbolNameHash.put(totalTableSize,new SymbolName(name,isSafe));
					symbolCodeHash.put(name,totalTableSize);
					code= totalTableSize + firstSymbolCode;
					return code;
				}
			} else {
				if (code > Integer.MAX_VALUE - firstSymbolCode) {
					throw new UnknownSymbolCode();
				} else {
					return code + firstSymbolCode;
				}
			}
		}
	}
	//
	public static boolean isPredefinedClassName(long code) {
		return predefinedClassNameMap.containsKey((int)code);
	}
	public static int getPredefinedClassAncestorName(long code) {
		return predefinedClassNameMap.get((int)code);
	}
}
