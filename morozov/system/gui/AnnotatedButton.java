// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.gui;

import morozov.run.*;
import morozov.terms.*;

import javax.swing.AbstractButton;
import javax.swing.SwingUtilities;
import java.awt.event.KeyEvent;
import java.util.concurrent.atomic.AtomicReference;
import java.lang.reflect.InvocationTargetException;

public class AnnotatedButton {
	//
	String name;
	int mnemonicCode;
	int mnemonicIndex;
	//
	///////////////////////////////////////////////////////////////
	//
	public AnnotatedButton(String text, int code, int index) {
		name= text;
		mnemonicCode= code;
		mnemonicIndex= index;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public String getName() {
		return name;
	}
	public int getMnemonicCode() {
		return mnemonicCode;
	}
	public int getMnemonicIndex() {
		return mnemonicIndex;
	}
	public boolean hasMnemonicIndex() {
		return mnemonicIndex >= 0;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static void safelyUpdateAbstractButton(final AbstractButton jButton, Term value, ChoisePoint iX) {
		String text= value.toString(iX);
		final AnnotatedButton aButton= AnnotatedButton.interpretAmpersands(text);
		if (SwingUtilities.isEventDispatchThread()) {
			quicklyUpdateAbstractButton(jButton,aButton);
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						quicklyUpdateAbstractButton(jButton,aButton);
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	protected static void quicklyUpdateAbstractButton(AbstractButton jButton, AnnotatedButton aButton) {
		jButton.setText(aButton.getName());
		if (aButton.hasMnemonicIndex()) {
			jButton.setMnemonic(aButton.getMnemonicCode());
			try {
				jButton.setDisplayedMnemonicIndex(aButton.getMnemonicIndex());
			} catch (IllegalArgumentException e) {
			}
		} else {
			jButton.setMnemonic(KeyEvent.KEY_LOCATION_UNKNOWN);
			try {
				jButton.setDisplayedMnemonicIndex(-1);
			} catch (IllegalArgumentException e) {
			}
		}
	}
	//
	public static AnnotatedButton interpretAmpersands(String text) {
		int mnemonicCode= -1;
		int mnemonicIndex= -1;
		char[] characters= text.toCharArray();
		int textLength= characters.length;
		StringBuilder buffer= new StringBuilder();
		boolean processSupplementaryCharacter= false;
		int position= 0;
		char c1;
		char c2;
		int code;
		while (true) {
			if (position >= textLength) {
				break;
			} else {
				c1= characters[position];
				if (	(position + 1 <= textLength - 1) &&
					Character.isSurrogatePair(c1,characters[position+1])) {
					c2= characters[position+1];
					code= Character.toCodePoint(c1,c2);
					processSupplementaryCharacter= true;
				} else {
					c2= 0;
					code= c1;
					processSupplementaryCharacter= false;
				}
			};
			if (code == '&') {
				if (position + 1 <= textLength - 1) {
					if (characters[position+1]=='&') {
						buffer.append(c1);
						position= position + 2;
						continue;
					} else {
						if (mnemonicIndex==-1) {
							mnemonicCode= characters[position+1];
							mnemonicIndex= position;
						};
						position++;
						continue;
					}
				} else {
					position++;
					continue;
				}
			} else {
				if (processSupplementaryCharacter) {
					buffer.append(c1);
					buffer.append(c2);
					position= position + 2;
				} else {
					buffer.append(c1);
					position++;
				}
			}
		};
		return new AnnotatedButton(buffer.toString(),mnemonicCode,mnemonicIndex);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static String safelyRestoreText(final AbstractButton jButton) {
		if (SwingUtilities.isEventDispatchThread()) {
			return quicklyRestoreText(jButton);
		} else {
			try {
				final AtomicReference<String> result= new AtomicReference<>();
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						result.set(quicklyRestoreText(jButton));
					}
				});
				return result.get();
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		};
		return "";
	}
	protected static String quicklyRestoreText(AbstractButton jButton) {
		String text= jButton.getText();
		int index= jButton.getDisplayedMnemonicIndex();
		if (index >= 0) {
			text= text.substring(0,index) + "&" + text.substring(index);
		};
		return text;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static String reduceAmpersands(String text) {
		int p1= text.indexOf('&');
		if (p1 < 0) {
			return text;
		};
		char[] characters= text.toCharArray();
		int textLength= characters.length;
		StringBuilder buffer= new StringBuilder();
		boolean processSupplementaryCharacter= false;
		int position= 0;
		char c1;
		char c2;
		int code;
		while (true) {
			if (position >= textLength) {
				break;
			} else {
				c1= characters[position];
				if (	(position + 1 <= textLength - 1) &&
					Character.isSurrogatePair(c1,characters[position+1])) {
					c2= characters[position+1];
					code= Character.toCodePoint(c1,c2);
					processSupplementaryCharacter= true;
				} else {
					c2= 0;
					code= c1;
					processSupplementaryCharacter= false;
				}
			};
			if (code == '&') {
				if (position + 1 <= textLength - 1) {
					if (characters[position+1]=='&') {
						buffer.append(c1);
						position= position + 2;
						continue;
					} else {
						position++;
						continue;
					}
				} else {
					position++;
					continue;
				}
			} else {
				if (processSupplementaryCharacter) {
					buffer.append(c1);
					buffer.append(c2);
					position= position + 2;
				} else {
					buffer.append(c1);
					position++;
				}
			}
		};
		return buffer.toString();
	}
	//
	public static String restoreAmpersands(String text) {
		int p1= text.indexOf('&');
		if (p1 < 0) {
			return text;
		};
		char[] characters= text.toCharArray();
		int textLength= characters.length;
		StringBuilder buffer= new StringBuilder();
		boolean processSupplementaryCharacter= false;
		int position= 0;
		char c1;
		char c2;
		int code;
		while (true) {
			if (position >= textLength) {
				break;
			} else {
				c1= characters[position];
				if (	(position + 1 <= textLength - 1) &&
					Character.isSurrogatePair(c1,characters[position+1])) {
					c2= characters[position+1];
					code= Character.toCodePoint(c1,c2);
					processSupplementaryCharacter= true;
				} else {
					c2= 0;
					code= c1;
					processSupplementaryCharacter= false;
				}
			};
			if (code == '&') {
				buffer.append(code);
				buffer.append(code);
				position++;
				continue;
			} else {
				if (processSupplementaryCharacter) {
					buffer.append(c1);
					buffer.append(c2);
					position= position + 2;
				} else {
					buffer.append(c1);
					position++;
				}
			}
		};
		return buffer.toString();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public String toString() {
		return "(" +
			name + "," +
			String.format("%d",mnemonicCode) + "," +
			String.format("%d",mnemonicIndex) +
			")";
	}
}
