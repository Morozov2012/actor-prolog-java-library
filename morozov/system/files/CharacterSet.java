// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.system.files;

import java.nio.charset.Charset;

public class CharacterSet {
	private CharacterSetType type;
	private String name;
	public CharacterSet(String s) {
		name= s;
		type= CharacterSetType.NAMED_CHARSET;
	}
	public CharacterSet(CharacterSetType t) {
		type= t;
	}
	public Charset toCharSet() {
		if (type==CharacterSetType.NAMED_CHARSET) {
			return Charset.forName(name);
		} else {
			return type.toCharSet();
		}
	}
	public boolean isDummy() {
		return type==CharacterSetType.NONE;
	}
	public boolean isDefault() {
		return type==CharacterSetType.DEFAULT;
	}
	public boolean isDummyOrDefault() {
		return type==CharacterSetType.NONE || type==CharacterSetType.DEFAULT;
	}
}
