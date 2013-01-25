// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.run;

import target.*;

import morozov.syntax.scanner.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Collections;

public abstract class SymbolTable {
	public static SymbolName[] initialContent;
	public static Map<Integer,SymbolName> symbolNameHash= Collections.synchronizedMap(new HashMap<Integer,SymbolName>());
	public static Map<String,Integer> symbolCodeHash= Collections.synchronizedMap(new HashMap<String,Integer>());
	public static int staticTableSize;
	public static int totalTableSize;
	protected final static int firstSymbolCode = 100;
	// public SymbolTable() {}
	public static void initializeSymbolTable() {
		synchronized(symbolCodeHash) {
			// SymbolName[] initialContent= getStaticSymbolNames();
			for (int n=0; n<initialContent.length; n++) {
				symbolNameHash.put(n,initialContent[n]);
				symbolCodeHash.put(initialContent[n].identifier,n);
				// System.out.printf("%d) %s\n",n,initialContent[n].identifier);
			};
			staticTableSize= initialContent.length;
			totalTableSize= initialContent.length;
		}
	}
	// public abstract SymbolName[] getStaticSymbolNames();
	public static SymbolName retrieveSymbolName(long value) {
		// System.out.printf("code: %s\n",value);
		if (value - firstSymbolCode > Integer.MAX_VALUE) {
			throw new IncorrecSymbolCode();
		};
		int absoluteCode= (int)(value-firstSymbolCode);
		// System.out.printf("absoluteCode: %s\n",absoluteCode);
		if (SymbolNames.staticTableSize > absoluteCode) {
			// System.out.printf("staticTableSize:%s > absoluteCode:%s\n",staticTableSize,absoluteCode);
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
		synchronized(symbolCodeHash) {
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
}
