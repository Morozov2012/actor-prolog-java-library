// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.system.files;

import morozov.run.*;
import morozov.system.files.errors.*;
import morozov.terms.*;

public class RelativeFileName extends SimpleFileName {
	public RelativeFileName(String name, boolean backslashIsSeparator, boolean acceptOnlyURI) {
		super(name,backslashIsSeparator,acceptOnlyURI);
	}
	public RelativeFileName(StandardFileName name) {
		super(name);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void extractFileName(ChoisePoint iX, PrologVariable a1, PrologVariable a2) {
		String pathOfFile;
		Term nameOfFile;
		if (!isStandardFile) {
			String fullName= textName;
			int p1= fullName.lastIndexOf(':');
			int p2= fullName.lastIndexOf('/');
			p1= StrictMath.max(p1,p2);
			if (considerBackslashAsASepator()) {
				p2= fullName.lastIndexOf('\\');
				p1= StrictMath.max(p1,p2);
			};
			if (p1 >= 0) {
				String path= fullName.substring(0,p1+1);
				String name= fullName.substring(p1+1);
				pathOfFile= path;
				nameOfFile= new PrologString(name);
			} else {
				pathOfFile= "";
				nameOfFile= toTerm();
			}
		} else {
			// pathOfFile= "";
			// nameOfFile= toTerm();
			throw new StandardStreamIsNotAllowedInThisOperation();
		};
		a1.value= new PrologString(pathOfFile);
		a2.value= nameOfFile;
		iX.pushTrail(a1);
		iX.pushTrail(a2);
	}
	//
	public void extractFileExtension(ChoisePoint iX, PrologVariable a1, PrologVariable a2) {
		if (!isStandardFile) {
			String fullName= textName;
			int p1= fullName.lastIndexOf('.');
			if (p1 >= 0) {
				String name= fullName.substring(0,p1);
				String extension= fullName.substring(p1);
				int p2= extension.indexOf(':');
				if (p2 >= 0) {
					name= fullName;
					extension= "";
				} else {
					p2= extension.indexOf('/');
					if (p2 >= 0) {
						name= fullName;
						extension= "";
					} else if (considerBackslashAsASepator()) {
						p2= extension.indexOf('\\');
						if (p2 >= 0) {
							name= fullName;
							extension= "";
						}
					}
				};
				a1.value= new PrologString(name);
				a2.value= new PrologString(extension);
				iX.pushTrail(a1);
				iX.pushTrail(a2);
				return;
			} else {
				a1.value= new PrologString(fullName);
				a2.value= new PrologString("");
				iX.pushTrail(a1);
				iX.pushTrail(a2);
				return;
			}
		} else {
			// a1.value= toTerm();
			// a2.value= new PrologString("");
			// iX.pushTrail(a1);
			// iX.pushTrail(a2);
			throw new StandardStreamIsNotAllowedInThisOperation();
		}
	}
	//
	public String discardFileExtension() {
		if (!isStandardFile) {
			String fullName= textName;
			int p1= fullName.lastIndexOf('.');
			if (p1 >= 0) {
				String name= fullName.substring(0,p1);
				String extension= fullName.substring(p1);
				int p2= extension.indexOf(':');
				if (p2 >= 0) {
					name= fullName;
				} else {
					p2= extension.indexOf('/');
					if (p2 >= 0) {
						name= fullName;
					} else if (considerBackslashAsASepator()) {
						p2= extension.indexOf('\\');
						if (p2 >= 0) {
							name= fullName;
						}
					}
				};
				return name;
			} else {
				return fullName;
			}
		} else {
			// return toString();
			throw new StandardStreamIsNotAllowedInThisOperation();
		}
	}
	//
	public String extractFileNameExtension() {
		if (!isStandardFile) {
			String fullName= textName;
			int p1= fullName.lastIndexOf('.');
			if (p1 >= 0) {
				String extension= fullName.substring(p1);
				int p2= extension.indexOf(':');
				if (p2 >= 0) {
					extension= "";
				} else {
					p2= extension.indexOf('/');
					if (p2 >= 0) {
						extension= "";
					} else if (considerBackslashAsASepator()) {
						p2= extension.indexOf('\\');
						if (p2 >= 0) {
							extension= "";
						}
					}
				};
				return extension;
			} else {
				return "";
			}
		} else {
			// return "";
			throw new StandardStreamIsNotAllowedInThisOperation();
		}
	}
	//
	public Term replaceFileExtension(String newExtension) {
		if (!isStandardFile) {
			return new PrologString(modifyFileExtension(newExtension));
		} else {
			// return toTerm();
			throw new StandardStreamIsNotAllowedInThisOperation();
		}
	}
	//
	///////////////////////////////////////////////////////////////
	// Auxiliary procedures                                      //
	///////////////////////////////////////////////////////////////
	//
	protected String modifyFileExtension(String newExtension) {
		if (!isStandardFile) {
			if (!newExtension.startsWith(".")) {
				newExtension= "." + newExtension;
			};
			int p1= textName.lastIndexOf('.');
			if (p1 >= 0) {
				String name= textName.substring(0,p1);
				String extension= textName.substring(p1);
				int p2= extension.indexOf(':');
				if (p2 >= 0) {
					name= textName;
				} else {
					p2= extension.indexOf('/');
					if (p2 >= 0) {
						name= textName;
					} else if (considerBackslashAsASepator()) {
						p2= extension.indexOf('\\');
						if (p2 >= 0) {
							name= textName;
						}
					}
				};
				return name + newExtension;
			} else {
				return textName + newExtension;
			}
		} else {
			throw new StandardStreamIsNotAllowedInThisOperation();
		}
	}
}
