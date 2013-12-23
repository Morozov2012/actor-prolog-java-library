// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.built_in;

import target.*;

import morozov.run.*;
import morozov.system.checker.*;
import morozov.system.checker.errors.*;
import morozov.system.checker.signals.*;
import morozov.system.errors.*;
import morozov.system.files.*;
import morozov.system.signals.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.MalformedURLException;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.DirectoryNotEmptyException;

import java.util.List;

public abstract class WebResource extends Text {
	//
	protected static final FileSystem fileSystem= FileSystems.getDefault();
	//
	protected static final int defaultWaitingInterval= -1;
	//
	abstract protected Term getBuiltInSlot_E_location();
	abstract protected Term getBuiltInSlot_E_max_waiting_time();
	abstract protected Term getBuiltInSlot_E_character_set();
	abstract protected Term getBuiltInSlot_E_backslash_always_is_separator();
	// abstract protected Term getBuiltInSlot_E_mask();
	// abstract protected Term getBuiltInSlot_E_send_full_path();
	// abstract protected Term getBuiltInSlot_E_query();
	// abstract protected Term getBuiltInSlot_E_content_type();
	//
	public void getParameters0ff(ChoisePoint iX, PrologVariable a1) {
		URI uri= retrieveLocationURI(iX);
		CharacterSet characterSet= retrieveCharacterSet(iX);
		int timeout= retrieveMaxWaitingTime(iX);
		a1.value= getResourceParameters(uri,characterSet,timeout,iX);
		// iX.pushTrail(a1);
	}
	public void getParameters0fs(ChoisePoint iX) {
	}
	public void getParameters1ff(ChoisePoint iX, PrologVariable a1, Term a2) {
		try {
			String path= a2.getStringValue(iX);
			CharacterSet characterSet= retrieveCharacterSet(iX);
			int timeout= retrieveMaxWaitingTime(iX);
			a1.value= getResourceParameters(path,characterSet,timeout,iX);
		} catch (TermIsNotAString e) {
			throw new WrongArgumentIsNotAString(a2);
		}
	}
	public void getParameters1fs(ChoisePoint iX, Term a1) {
	}
	protected Term getResourceParameters(URI uri, CharacterSet characterSet, int timeout, ChoisePoint iX) {
		boolean backslashIsSeparator= FileUtils.checkIfBackslashIsSeparator(getBuiltInSlot_E_backslash_always_is_separator(),iX);
		try {
			URL_Utils.installCookieManagerIfNecessary(staticContext);
			URL_Attributes attributes= URL_Utils.getResourceAttributes(uri,characterSet,timeout,staticContext,backslashIsSeparator);
			return attributes.toTerm();
		} catch (Throwable e) {
			return URL_Utils.exceptionToName(e);
		}
	}
	protected Term getResourceParameters(String path, CharacterSet characterSet, int timeout, ChoisePoint iX) {
		boolean backslashIsSeparator= FileUtils.checkIfBackslashIsSeparator(getBuiltInSlot_E_backslash_always_is_separator(),iX);
		try {
			URL_Utils.installCookieManagerIfNecessary(staticContext);
			URI uri= getURI(iX,path);
			URL_Attributes attributes= URL_Utils.getResourceAttributes(uri,characterSet,timeout,staticContext,backslashIsSeparator);
			return attributes.toTerm();
		} catch (Throwable e) {
			return URL_Utils.exceptionToName(e);
		}
	}
	//
	public void getContent0ff(ChoisePoint iX, PrologVariable a1) {
		URI uri= retrieveLocationURI(iX);
		a1.value= getResourceContent(uri,iX);
		// iX.pushTrail(a1);
	}
	public void getContent0fs(ChoisePoint iX) {
	}
	public void getContent1ff(ChoisePoint iX, PrologVariable a1, Term a2) {
		try {
			String path= a2.getStringValue(iX);
			a1.value= getResourceContent(path,iX);
			// iX.pushTrail(a1);
		} catch (TermIsNotAString e) {
			throw new WrongArgumentIsNotAString(a2);
		}
	}
	public void getContent1fs(ChoisePoint iX, Term a1) {
	}
	protected Term getResourceContent(String path, ChoisePoint iX) {
		try {
			URI uri= getURI(iX,path);
			return getResourceContent(uri,iX);
		} catch (Throwable e) {
			// Term error= new PrologString(e.toString());
			// return new PrologStructure(SymbolCodes.symbolCode_E_error,new Term[]{error});
			return URL_Utils.exceptionToName(e);
		}
	}
	protected Term getResourceContent(URI uri, ChoisePoint iX) {
		try {
			URL_Utils.installCookieManagerIfNecessary(staticContext);
			CharacterSet characterSet= retrieveCharacterSet(iX);
			int timeout= retrieveMaxWaitingTime(iX);
			boolean backslashIsSeparator= FileUtils.checkIfBackslashIsSeparator(getBuiltInSlot_E_backslash_always_is_separator(),iX);
			Term result;
			try {
				result= getContentOfResource(uri,characterSet,timeout,backslashIsSeparator);
			} catch (CannotRetrieveContent e2) {
				result= e2.getExceptionName();
			} catch (Throwable e2) {
				throw e2;
			};
			return result;
		// } catch (MalformedURLException e1) {
		} catch (Throwable e1) {
			return URL_Utils.exceptionToName(e1);
		}
	}
	protected Term getContentOfResource(URI uri, CharacterSet characterSet, int timeout, boolean backslashIsSeparator) throws CannotRetrieveContent, URISyntaxException, MalformedURLException {
		Term result;
		try {
			// URL_Utils.installCookieManagerIfNecessary(staticContext);
			URL_Attributes attributes= URL_Utils.getResourceAttributes(uri,characterSet,timeout,staticContext,backslashIsSeparator);
			try {
				if (attributes.isDirectory) {
					List<Path> list= retrieveDirectoryList(uri);
					StringBuilder buffer= new StringBuilder();
					for (int n=0; n < list.size(); n++) {
						buffer.append(FileUtils.tryToMakeRealName(list.get(n)).toString());
						buffer.append("\n");
					};
					result= new PrologString(buffer.toString());
				} else {
					String text= URL_Utils.readTextFile(attributes);
					result= new PrologString(text);
				};
				if (attributes.connectionWasSuccessful()) {
					return result;
				} else {
					throw new CannotRetrieveContent(attributes.getExceptionName());
				}
			} catch (Throwable e2) {
				throw new CannotRetrieveContent(e2);
			// } finally {
			//	attributes.safeCloseConnection();
			}
		} catch (URISyntaxException e1) {
			// Term error= new PrologString(e1.toString());
			// return new PrologStructure(SymbolCodes.symbolCode_E_error,new Term[]{error});
			throw e1;
		} catch (MalformedURLException e1) {
			// Term error= new PrologString(e1.toString());
			// return new PrologStructure(SymbolCodes.symbolCode_E_error,new Term[]{error});
			throw e1;
		} catch (IOException e1) {
			return URL_Utils.exceptionToName(e1);
		} catch (Throwable e1) {
			return URL_Utils.exceptionToName(e1);
		}
	}
	//
	public void doesExist0s(ChoisePoint iX) throws Backtracking {
		try {
			URI uri= retrieveLocationURI(iX);
			doesExist(uri,iX);
		} catch (Throwable e) {
			throw Backtracking.instance;
		}
	}
	public void doesExist1s(ChoisePoint iX, Term name) throws Backtracking {
		try {
			URI uri= retrieveLocationURI(name,iX);
			doesExist(uri,iX);
		} catch (Throwable e) {
			throw Backtracking.instance;
		}
	}
	protected void doesExist(URI uri, ChoisePoint iX) throws Backtracking {
		CharacterSet characterSet= FileUtils.term2CharacterSet(getBuiltInSlot_E_character_set(),iX);
		int timeout= retrieveMaxWaitingTime(iX);
		boolean backslashIsSeparator= FileUtils.checkIfBackslashIsSeparator(getBuiltInSlot_E_backslash_always_is_separator(),iX);
		try {
			URL_Utils.installCookieManagerIfNecessary(staticContext);
			URL_Attributes attributes= URL_Utils.getResourceAttributes(uri,characterSet,timeout,staticContext,backslashIsSeparator);
			if (!attributes.connectionWasSuccessful()) {
				throw Backtracking.instance;
			}
		} catch (Throwable e1) {
			throw Backtracking.instance;
		}
	}
	//
	public void isLocalResource0s(ChoisePoint iX) throws Backtracking {
		URI uri= retrieveLocationURI(iX);
		isLocalResource(uri,iX);
	}
	public void isLocalResource1s(ChoisePoint iX, Term name) throws Backtracking {
		URI uri= retrieveLocationURI(name,iX);
		isLocalResource(uri,iX);
	}
	protected void isLocalResource(URI fileName, ChoisePoint iX) throws Backtracking {
		if (!URL_Utils.isLocalResource(fileName)) {
			throw Backtracking.instance;
		}
	}
	//
	public void getFullName0ff(ChoisePoint iX, PrologVariable a1) {
		String resolvedName= getFullName(iX);
		a1.value= new PrologString(resolvedName);
	}
	public void getFullName0fs(ChoisePoint iX) {
	}
	public void getFullName1ff(ChoisePoint iX, PrologVariable a1, Term a2) {
		String resolvedName= getFullName(iX,a2);
		a1.value= new PrologString(resolvedName);
	}
	public void getFullName1fs(ChoisePoint iX, Term a1) {
	}
	protected String getFullName(ChoisePoint iX, Term a1) {
		String fileName= null;
		try {
			fileName= a1.getStringValue(iX);
		} catch (TermIsNotAString e1) {
			throw new WrongArgumentIsNotAString(a1);
		};
		return getFullName(iX,fileName);
	}
	protected String getFullName(ChoisePoint iX, String fileName) {
		String location= retrieveLocationString(iX);
		boolean backslashIsSeparator= FileUtils.checkIfBackslashIsSeparator(getBuiltInSlot_E_backslash_always_is_separator(),iX);
		return URL_Utils.getFullName(location,fileName,staticContext,backslashIsSeparator);
	}
	protected String getFullName(ChoisePoint iX) {
		String location= retrieveLocationString(iX);
		boolean backslashIsSeparator= FileUtils.checkIfBackslashIsSeparator(getBuiltInSlot_E_backslash_always_is_separator(),iX);
		return URL_Utils.getFullName(location,staticContext,backslashIsSeparator);
	}
	//
	public void getURL0ff(ChoisePoint iX, PrologVariable a1) {
		String resolvedName= getFullName(iX);
		boolean backslashIsSeparator= FileUtils.checkIfBackslashIsSeparator(getBuiltInSlot_E_backslash_always_is_separator(),iX);
		a1.value= new PrologString(URL_Utils.get_URL_string(resolvedName,staticContext,backslashIsSeparator));
	}
	public void getURL0fs(ChoisePoint iX) {
	}
	public void getURL1ff(ChoisePoint iX, PrologVariable a1, Term a2) {
		String resolvedName= getFullName(iX,a2);
		boolean backslashIsSeparator= FileUtils.checkIfBackslashIsSeparator(getBuiltInSlot_E_backslash_always_is_separator(),iX);
		a1.value= new PrologString(URL_Utils.get_URL_string(resolvedName,staticContext,backslashIsSeparator));
	}
	public void getURL1fs(ChoisePoint iX, Term a1) {
	}
	//
	public void delete0s(ChoisePoint iX) {
		String fileName= getFullName(iX);
		deleteFile(fileName);
	}
	public void delete1s(ChoisePoint iX, Term name) {
		String fileName= getFullName(iX,name);
		deleteFile(fileName);
	}
	protected void deleteFile(String fileName) {
		Path path= fileSystem.getPath(fileName);
		try {
			Files.deleteIfExists(path);
		} catch (DirectoryNotEmptyException e) {
		} catch (IOException e) {
		}
	}
	//
	protected URI getURI(ChoisePoint iX, String fileName) throws URISyntaxException {
		String location= retrieveLocationString(iX);
		boolean backslashIsSeparator= FileUtils.checkIfBackslashIsSeparator(getBuiltInSlot_E_backslash_always_is_separator(),iX);
		return URL_Utils.getURI(location,fileName,staticContext,backslashIsSeparator);
	}
	//
	protected URI retrieveLocationURI(ChoisePoint iX) {
		Term location= getBuiltInSlot_E_location();
		return retrieveLocationURI(location,iX);
	}
	protected URI retrieveLocationURI(Term location, ChoisePoint iX) {
		try {
			String path= location.getStringValue(iX);
			boolean backslashIsSeparator= FileUtils.checkIfBackslashIsSeparator(getBuiltInSlot_E_backslash_always_is_separator(),iX);
			URI uri= URL_Utils.create_URI(path,staticContext,backslashIsSeparator);
			return uri;
		} catch (TermIsNotAString e) {
			throw new WrongArgumentIsNotAString(location);
		}
	}
	protected String retrieveLocationString(ChoisePoint iX) {
		Term location= getBuiltInSlot_E_location();
		try {
			return location.getStringValue(iX);
		} catch (TermIsNotAString e) {
			throw new WrongArgumentIsNotAString(location);
		}
	}
	//
	protected CharacterSet retrieveCharacterSet(ChoisePoint iX) {
		Term value= getBuiltInSlot_E_character_set();
		return FileUtils.term2CharacterSet(value,iX);
	}
	//
	protected int retrieveMaxWaitingTime(ChoisePoint iX) {
		Term interval= getBuiltInSlot_E_max_waiting_time();
		try {
			return URL_Utils.termToWaitingInterval(interval,iX);
		} catch (TermIsSymbolDefault e1) {
			try {
				return URL_Utils.termToWaitingInterval(DefaultOptions.waitingInterval,iX);
			} catch (TermIsSymbolDefault e2) {
				return defaultWaitingInterval;
			}
		}
	}
	//
	protected List<Path> retrieveDirectoryList(URI uri) {
		Path dir= fileSystem.provider().getPath(uri);
		return FileUtils.listSourceFiles(dir);
	}
	//
	public void getString0ff(ChoisePoint iX, PrologVariable a1) {
		getContent0ff(iX,a1);
	}
	public void setString1s(ChoisePoint iX, Term inputText) {
		throw new DisabledOperation();
	}
	public void clear0s(ChoisePoint iX) {
		throw new DisabledOperation();
	}
	public void write1ms(ChoisePoint iX, Term... args) {
		throw new DisabledOperation();
	}
	public void writeLn1ms(ChoisePoint iX, Term... args) {
		throw new DisabledOperation();
	}
	public void writeF2ms(ChoisePoint iX, Term... args) {
		throw new DisabledOperation();
	}
	public void newLine0s(ChoisePoint iX) {
		throw new DisabledOperation();
	}
}
