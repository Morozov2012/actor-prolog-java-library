// (c) 2010-2015 IRE RAS Alexei A. Morozov

package morozov.system.files;

import morozov.run.*;
import morozov.system.converters.errors.*;
import morozov.system.files.errors.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.util.ArrayList;

public class FileUtils {
	//
	public static FileNameMask[] argumentToFileNameMasks(Term types, ChoisePoint iX) {
		ArrayList<FileNameMask> list= new ArrayList<>();
		Term nextHead= null;
		Term currentTail= types;
		try {
			while (true) {
				nextHead= currentTail.getNextListHead(iX);
				String wildcardText= nextHead.getStringValue(iX);
				String[] wildcards= stringToWildcards(wildcardText);
				currentTail= currentTail.getNextListTail(iX);
				nextHead= currentTail.getNextListHead(iX);
				String description= nextHead.getStringValue(iX);
				currentTail= currentTail.getNextListTail(iX);
				list.add(new FileNameMask(description,wildcards));
			}
		} catch (EndOfList e) {
		} catch (TermIsNotAList e) {
			throw new WrongArgumentIsNotWildcardList(currentTail);
		} catch (TermIsNotAString e) {
			throw new WrongArgumentIsNotAString(nextHead);
		};
		return list.toArray(new FileNameMask[list.size()]);
	}
	//
	protected static String[] stringToWildcards(String wildcardText) {
		ArrayList<String> list= new ArrayList<>();
		int length= wildcardText.length();
		int currentPosition= 0;
		while (true) {
			if (currentPosition < length) {
				int p1= wildcardText.indexOf(';',currentPosition);
				if (p1 >= 0) {
					String wildcard= wildcardText.substring(currentPosition,p1).trim();
					if (!wildcard.isEmpty()) {
						list.add(wildcard);
					};
					currentPosition= p1 + 1;
					continue;
				} else {
					String wildcard= wildcardText.substring(currentPosition,wildcardText.length()).trim();
					if (!wildcard.isEmpty()) {
						list.add(wildcard);
					};
					break;
				}
			} else {
				break;
			}
		};
		return list.toArray(new String[list.size()]);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static String deleteQuotationMarks(String name) {
		name= name.trim();
		int length= name.length();
		int beginning= name.codePointAt(0);
		int end= name.codePointAt(length-1);
		if (beginning=='\"' && end=='\"' && length-1 >= 1) {
			name= name.substring(1,length-1);
			name= name.trim();
			return name;
		} else {
			return name;
		}
	}
}
