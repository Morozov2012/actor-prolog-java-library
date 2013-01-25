// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.system.files;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public enum CharacterSetType {
	NAMED_CHARSET {
		public Charset toCharSet() {
			throw new NoCharacterSetIsDefined();
		}
	},
	NONE {
		public Charset toCharSet() {
			throw new NoCharacterSetIsDefined();
		}
	},
	DEFAULT {
		public Charset toCharSet() {
			return Charset.defaultCharset();
		}
	},
	ISO_8859_1 {
		public Charset toCharSet() {
			return StandardCharsets.ISO_8859_1;
		}
	},
	US_ASCII {
		public Charset toCharSet() {
			return StandardCharsets.US_ASCII;
		}
	},
	UTF_16 {
		public Charset toCharSet() {
			return StandardCharsets.UTF_16;
		}
	},
	UTF_16BE {
		public Charset toCharSet() {
			return StandardCharsets.UTF_16BE;
		}
	},
	UTF_16LE {
		public Charset toCharSet() {
			return StandardCharsets.UTF_16LE;
		}
	},
	UTF_8 {
		public Charset toCharSet() {
			return StandardCharsets.UTF_8;
		}
	};
	abstract public Charset toCharSet();
}
