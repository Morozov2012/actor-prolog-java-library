/*
 * @(#)SortedStrings.java 1.0 2015/04/25
 *
 * Copyright 2015 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.scalable.common;

import java.util.Arrays;

public class SortedStrings {
	public String[] initialArray;
	public String[] sortedArray;
	public int[] reverseIndex;
	public SortedStrings(String[] inputList) {
		initialArray= inputList;
		StringWithNumber[] pairs= new StringWithNumber[initialArray.length];
		for (int n=0; n < initialArray.length; n++) {
			pairs[n]= new StringWithNumber(n,initialArray[n]);
		};
		Arrays.sort(pairs,new AlphabeticStringComparator());
		sortedArray= new String[initialArray.length];
		reverseIndex= new int[initialArray.length];
		for (int n=0; n < initialArray.length; n++) {
			sortedArray[n]= pairs[n].text;
			reverseIndex[pairs[n].number]= n;
		};
	}
	public int resolveIndex(int index) {
		return reverseIndex[index];
	}
	public int[] resolveIndices(int[] initialIndices) {
		int[] resolvedIndices= new int[initialIndices.length];
		for (int n=0; n < initialIndices.length; n++) {
			resolvedIndices[n]= reverseIndex[initialIndices[n]];
		};
		return resolvedIndices;
	}
}
