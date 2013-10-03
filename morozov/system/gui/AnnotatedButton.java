// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.gui;

public class AnnotatedButton {
	String name;
	int mnemonicCode;
	int mnemonicIndex;
	public AnnotatedButton(String text, int code, int index) {
		name= text;
		mnemonicCode= code;
		mnemonicIndex= index;
	}
	public String toString() {
		return "(" +
			name + "," +
			String.format("%d",mnemonicCode) + "," +
			String.format("%d",mnemonicIndex) +
			")";
	}
}
