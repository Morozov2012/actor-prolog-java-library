// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.system;

public class SystemUtils {
	public static int indexOf(String text, String target, int currentPosition, boolean caseSensitivity) {
		// System.out.printf("currentPosition: %d\n",currentPosition);
		if (caseSensitivity) {
			return text.indexOf(target,currentPosition);
		} else {
			int targetLength= target.length();
			if (targetLength==0) {
				if (currentPosition <= text.length()) {
					return currentPosition;
				} else {
					return -1;
				}
			} else {
				for (int n=currentPosition; n < text.length()-targetLength+1; n++) {
					// System.out.printf("n=%d >>>%s<<<\n",n,text.substring(n,n+targetLength));
					if (text.substring(n,n+targetLength).compareToIgnoreCase(target) == 0) {
						return n;
					}
				};
				return -1;
			}
		}
	}
}
