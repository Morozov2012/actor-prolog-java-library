// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.system.files;

import target.*;

import morozov.run.*;
import morozov.system.checker.*;
import morozov.system.checker.errors.*;
import morozov.system.checker.signals.*;
import morozov.system.datum.*;
import morozov.system.files.errors.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.nio.file.Files;
import java.nio.file.FileSystems;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.FileAlreadyExistsException;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.HttpURLConnection;
import java.net.CookieManager;
import java.net.CookieHandler;
import java.net.URISyntaxException;
import java.net.SocketTimeoutException;
import java.util.concurrent.locks.ReentrantLock;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class SimpleFileName {
	protected boolean isStandardFile= false;
	protected StandardFileName systemName= StandardFileName.STDOUT;
	protected String textName= "";
	protected boolean backslashAlwaysIsSeparator= true;
	protected boolean acceptOnlyUniformResourceIdentifiers= false;
	//
	protected static final int minimalLengthOfValidScheme= 2;
	protected static final FileSystem fileSystem= FileSystems.getDefault();
	protected static final String stringFile= "file:";
	protected static final String stringFile3Slash= "file:///";
	//
	///////////////////////////////////////////////////////////////
	//
	public SimpleFileName(String name, boolean backslashIsSeparator, boolean acceptOnlyURI) {
		textName= name.trim();
		backslashAlwaysIsSeparator= backslashIsSeparator;
		acceptOnlyUniformResourceIdentifiers= acceptOnlyURI;
	}
	public SimpleFileName(StandardFileName name) {
		systemName= name;
		isStandardFile= true;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static SimpleFileName termToSimpleFileName(Term value, boolean backslashIsSeparator, boolean acceptOnlyURI, ChoisePoint iX) {
		try {
			String name= value.getStringValue(iX);
			return new SimpleFileName(name,backslashIsSeparator,acceptOnlyURI);
		} catch (TermIsNotAString e1) {
			if (acceptOnlyURI) {
				throw new WrongArgumentIsNotUniformResourceIdentifier(value);
			};
			try {
				long code= value.getSymbolValue(iX);
				if (code==SymbolCodes.symbolCode_E_stdin) {
					return new SimpleFileName(StandardFileName.STDIN);
				} else if (code==SymbolCodes.symbolCode_E_stdout) {
					return new SimpleFileName(StandardFileName.STDOUT);
				} else if (code==SymbolCodes.symbolCode_E_stderr) {
					return new SimpleFileName(StandardFileName.STDERR);
				} else {
					throw new WrongArgumentIsNotFileName(value);
				}
			} catch (TermIsNotASymbol e2) {
				throw new WrongArgumentIsNotFileName(value);
			}
		}
	}
	public static SimpleFileName termToSimpleFileName(String name, boolean backslashIsSeparator, boolean acceptOnlyURI) {
		return new SimpleFileName(name,backslashIsSeparator,acceptOnlyURI);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public RelativeFileName formRelativeFileName(boolean appendExtension, String extension) {
		if (!isStandardFile) {
			String name= appendExtensionIfNecessary(appendExtension,extension);
			return new RelativeFileName(name,backslashAlwaysIsSeparator,acceptOnlyUniformResourceIdentifiers);
		} else {
			if (acceptOnlyUniformResourceIdentifiers) {
				throw new WrongArgumentIsNotUniformResourceIdentifier(toTerm());
			};
			return new RelativeFileName(systemName);
		}
	}
	//
	public ExtendedFileName formRealFileNameBasedOnPath(boolean createLocalName, boolean appendExtension, String extension, Path currentDirectory, StaticContext staticContext) {
		if (!isStandardFile) {
			String name= appendExtensionIfNecessary(appendExtension,extension);
			return new ExtendedFileName(name,backslashAlwaysIsSeparator,acceptOnlyUniformResourceIdentifiers,createLocalName,currentDirectory,staticContext);
		} else {
			if (acceptOnlyUniformResourceIdentifiers) {
				throw new WrongArgumentIsNotUniformResourceIdentifier(toTerm());
			};
			return new ExtendedFileName(systemName);
		}
	}
	public ExtendedFileName formRealFileNameBasedOnEFN(boolean createLocalName, boolean appendExtension, String extension, ExtendedFileName currentDirectory, StaticContext staticContext) {
		if (!isStandardFile) {
			String name= appendExtensionIfNecessary(appendExtension,extension);
			return new ExtendedFileName(name,backslashAlwaysIsSeparator,acceptOnlyUniformResourceIdentifiers,createLocalName,currentDirectory,staticContext);
		} else {
			if (acceptOnlyUniformResourceIdentifiers) {
				throw new WrongArgumentIsNotUniformResourceIdentifier(toTerm());
			};
			return new ExtendedFileName(systemName);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public boolean isStandardFile() {
		return isStandardFile;
	}
	//
	public StandardFileName getSystemName() {
		return systemName;
	}
	//
	public void isLocalResource() throws Backtracking {
		if (!textNameRefersToTheLocalResource()) {
			throw Backtracking.instance;
		}
	}
	//
	public boolean textNameRefersToTheLocalResource() {
		if (!isStandardFile) {
			String name= textName;
			int p1= name.indexOf(':');
			if (p1 <= 0) {
				return true;
			};
			if (!acceptOnlyUniformResourceIdentifiers) {
				if (p1 <= minimalLengthOfValidScheme-1) {
					return true;
				}
			};
			String scheme= name.substring(0,p1);
			scheme= scheme.toLowerCase();
			if (scheme.equals("file")) {
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static String replaceBackslashesAndSpaces(String name) {
		name= replaceBackslashes(name);
		name= replaceSpaces(name);
		return name;
	}
	public static String replaceBackslashes(String name) {
		return name.replace('\\','/');
	}
	public static String replaceSpaces(String name) {
		return name.replace(" ","%20");
	}
	//
	public boolean isLocalResourceURI(URI uri) {
		if (uri.isAbsolute()) {
			String scheme= uri.getScheme();
			if (scheme==null) {
				return true;
			} else if (scheme.length()==0) {
				return true;
			} else if (scheme.equalsIgnoreCase("file")) {
				return true;
			} else {
				if (!acceptOnlyUniformResourceIdentifiers) {
					if (scheme.length() <= minimalLengthOfValidScheme-1) {
						return true;
					}
				};
				return false;
			}
		} else {
			return true;
		}
	}
	//
	public static boolean isDirectoryURI(URI uri) {
		try {
			Path path= Paths.get(uri);
			BasicFileAttributes bfa= Files.readAttributes(path,BasicFileAttributes.class);
			return bfa.isDirectory();
		} catch (Throwable e) {
			return false;
		}
	}
	//
	public static Term channelExceptionToName(Throwable e) {
		if (e instanceof CannotRetrieveContent) {
			return ((CannotRetrieveContent)e).getExceptionName();
		} else if (e instanceof SocketTimeoutException) {
			return new PrologSymbol(SymbolCodes.symbolCode_E_lateness);
		} else {
			Term error= new PrologString(e.toString());
			return new PrologStructure(SymbolCodes.symbolCode_E_error,new Term[]{error});
		}
	}
	//
	public static Path urlToPath(URL url) {
		try {
			URI uri= url.toURI();
			if (uri.getPath().isEmpty()) {
				String targetDirectory= getUserDirectory();
				Path targetPath= fileSystem.getPath(targetDirectory);
				uri= targetPath.toUri();
			};
			return Paths.get(uri);
		} catch (URISyntaxException e) {
			throw new WrongArgumentIsMalformedURL(url.toString(),e);
		}
	}
	//
	public static Path verifyBaseDirectory(Path path, boolean endsWithSlash) {
		if (path != null) {
			if (endsWithSlash) {
				return path;
			} else {
				String fileName= path.getFileName().toString();
				return path.getParent();
			}
		} else {
			String userDirectory= getUserDirectory();
			return fileSystem.getPath(userDirectory);
		}
	}
	public static Path verifyBaseDirectory(ExtendedFileName extendedFile) {
		Path path= extendedFile.getPathOfLocalResource();
		boolean endsWithSlash= extendedFile.endsWithSlash;
		return verifyBaseDirectory(path,endsWithSlash);
	}
	//
	public static URI pathToBaseDirectoryURI(Path path, boolean endsWithSlash) {
		path= verifyBaseDirectory(path,endsWithSlash);
		return path.resolve(".").toUri();
	}
	//
	public static Path tryToMakeRealName(Path path) {
		try {
			return path.toRealPath();
		} catch (IOException e) {
			return path.toAbsolutePath();
		}
	}
	//
	///////////////////////////////////////////////////////////////
	// Directory inspection / creation procedures                //
	///////////////////////////////////////////////////////////////
	//
	public static void createDirectories(Path path, boolean extractDirectory) {
		if (extractDirectory) {
			path= path.normalize().getParent();
		};
		if (path != null) {
			if (path.getNameCount() > 0) {
				try {
					Files.createDirectories(path);
				} catch (FileAlreadyExistsException e) {
					throw new FileInputOutputError(path.toString(),e);
				} catch (IOException e) {
					throw new FileInputOutputError(path.toString(),e);
				} catch (SecurityException e) {
					throw new FileInputOutputError(path.toString(),e);
				}
			}
		}
	}
	//
	public static String getUserDirectory() {
		Properties properties= System.getProperties();
		return properties.getProperty("user.dir");
	}
	//
	public static List<Path> retrieveDirectoryList(URI uri) {
		Path dir= Paths.get(uri);
		return listSourceFiles(dir);
	}
	//
	protected static List<Path> listSourceFiles(Path dir) {
		return listSourceFiles(dir,null);
	}
	//
	protected static List<Path> listSourceFiles(Path dir, String mask) {
		List<Path> result= new ArrayList<Path>();
		try {
			DirectoryStream<Path> stream;
			if (mask==null) {
				stream= Files.newDirectoryStream(dir);
			} else {
				stream= Files.newDirectoryStream(dir,mask);
			};
			try {
				for (Path entry: stream) {
					result.add(entry);
				}
			} finally {
				try {
					stream.close();
				} catch (Throwable e) {
				}
			};
			return result;
		} catch (DirectoryIteratorException e) {
			// I/O error encounted during the iteration, the cause is an IOException
			// throw e.getCause();
			throw new FileInputOutputError(dir.toString(),e);
		} catch (IOException e) {
			throw new FileInputOutputError(dir.toString(),e);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	// Local file system input-output procedures                 //
	///////////////////////////////////////////////////////////////
	//
	public static byte[] readBytesFromLocalFile(Path path) {
		try {
			return Files.readAllBytes(path);
		} catch (FileNotFoundException e) {
			throw new FileInputOutputError(path.toString(),e);
		} catch (IOException e) {
			throw new FileInputOutputError(path.toString(),e);
		}
	}
	//
	public static String readStringFromLocalFile(Path path, CharacterSet characterSet) {
		try {
			byte[] bytes= Files.readAllBytes(path);
			if (characterSet.isDummy()) {
				return new String(bytes);
			} else {
				return new String(bytes,characterSet.toCharSet());
			}
		} catch (FileNotFoundException e) {
			throw new FileInputOutputError(path.toString(),e);
		} catch (IOException e) {
			throw new FileInputOutputError(path.toString(),e);
		}
	}
	//
	public static String readStringFromStdIn(CharacterSet characterSet) {
		if (characterSet.isDummy()) {
			String encoding= System.getProperty("file.encoding");
			return readStringFromStdIn(Charset.forName(encoding));
		} else {
			return readStringFromStdIn(characterSet.toCharSet());
		}
	}
	//
	protected static String readStringFromStdIn(Charset characterSet) {
		try {
			ArrayList<Byte> byteList= new ArrayList<Byte>();
			BufferedInputStream stream= new BufferedInputStream(System.in);
			int bufferSize= 10000;
			byte[] readingBuffer= new byte[bufferSize];
			while(true) {
				int k= stream.read(readingBuffer);
				if (k <= 0) {
					break;
				};
				for (int n=0; n < k; n++) {
					byteList.add(readingBuffer[n]);
				}
			};
			byte[] bytes= new byte[byteList.size()];
			for (int n=0; n < byteList.size(); n++) {
				bytes[n]= byteList.get(n);
			};
			String result= new String(bytes,characterSet);
			return result;
		} catch (IOException e) {
			throw new FileInputOutputError("stdin",e);
		}
	}
	///////////////////////////////////////////////////////////////
	// Channel input-output procedures                           //
	///////////////////////////////////////////////////////////////
	//
	public static String readStringFromUniversalResource(URL_Attributes attributes) throws Throwable {
		CharacterSet requestedCharacterSet= attributes.characterSet;
		if (attributes.isLocalResource) {
			if (requestedCharacterSet.isDummyOrDefault()) {
				if (attributes.has_UTF_Coding) {
					Path filePath= ExtendedFileName.urlToPath(attributes.url);
					return readStringFromLocalFile(filePath,new CharacterSet(CharacterSetType.UTF_16));
				} else {
					Path filePath= ExtendedFileName.urlToPath(attributes.url);
					return readStringFromLocalFile(filePath,requestedCharacterSet);
				}
			} else {
				Path filePath= ExtendedFileName.urlToPath(attributes.url);
				return readStringFromLocalFile(filePath,requestedCharacterSet);
			}
		} else {
			URLConnection connection= attributes.url.openConnection();
			int timeout= attributes.maxWaitingInterval;
			if (timeout >= 0) {
				connection.setConnectTimeout(timeout);
				connection.setReadTimeout(timeout);
			};
			InputStream stream= connection.getInputStream();
			try {
				int length= StrictMath.max(stream.available(),0xFFFF);
				byte[] readingBuffer= new byte[length];
				ArrayList<Byte> textBuffer= new ArrayList<Byte>();
				while(true) {
					int k= stream.read(readingBuffer,0,length);
					if (k < 0) {
						break;
					};
					for (int n=0; n < k; n++) {
						textBuffer.add(readingBuffer[n]);
					}
				};
				Byte[] objectSequence= textBuffer.toArray(new Byte[0]);
				byte[] byteSequence= new byte[objectSequence.length];
				for (int n=0; n < objectSequence.length; n++) {
					byteSequence[n]= objectSequence[n];
				};
				if (requestedCharacterSet.isDummyOrDefault()) {
					return new String(byteSequence);
				} else {
					return new String(byteSequence,requestedCharacterSet.toCharSet());
				}
			} finally {
				stream.close();
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	// Auxiliary procedures                                      //
	///////////////////////////////////////////////////////////////
	//
	protected static byte[] readBytesFromUniversalResource(URL_Attributes attributes) throws Throwable {
		if (attributes.isLocalResource) {
			Path filePath= ExtendedFileName.urlToPath(attributes.url);
			return SimpleFileName.readBytesFromLocalFile(filePath);
		} else {
			URLConnection connection= attributes.url.openConnection();
			int timeout= attributes.maxWaitingInterval;
			if (timeout >= 0) {
				connection.setConnectTimeout(timeout);
				connection.setReadTimeout(timeout);
			};
			InputStream stream= connection.getInputStream();
			try {
				int length= StrictMath.max(stream.available(),0xFFFF);
				byte[] readingBuffer= new byte[length];
				ArrayList<Byte> totalBuffer= new ArrayList<Byte>();
				while(true) {
					int k= stream.read(readingBuffer,0,length);
					if (k < 0) {
						break;
					};
					for (int n=0; n < k; n++) {
						totalBuffer.add(readingBuffer[n]);
					}
				};
				Byte[] totalArray= totalBuffer.toArray(new Byte[0]);
				byte[] result= new byte[totalArray.length];
				for (int n=0; n < totalArray.length; n++) {
					result[n]= totalArray[n];
				};
				return result;
			} finally {
				stream.close();
			}
		}
	}
	//
	protected static Object readObjectFromUniversalResource(URL_Attributes attributes) throws Throwable {
		if (attributes.isLocalResource) {
			Path path= ExtendedFileName.urlToPath(attributes.url);
			InputStream inputStream= Files.newInputStream(path);
			ObjectInputStream objectStream= new DataStoreInputStream(new BufferedInputStream(inputStream),true);
			try {
				return objectStream.readObject();
			} finally {
				objectStream.close();
			}
		} else {
			URLConnection connection= attributes.url.openConnection();
			int timeout= attributes.maxWaitingInterval;
			if (timeout >= 0) {
				connection.setConnectTimeout(timeout);
				connection.setReadTimeout(timeout);
			};
			InputStream inputStream= connection.getInputStream();
			ObjectInputStream objectStream= new DataStoreInputStream(new BufferedInputStream(inputStream),true);
			try {
				return objectStream.readObject();
			} finally {
				objectStream.close();
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected String appendExtensionIfNecessary(boolean appendExtension, String extension) {
		String name= textName;
		if (appendExtension && extension.length() > 0) {
			if (anExtensionIsToBeAppendedToThisFileName(name)) {
				if (!extension.startsWith(".")) {
					extension= "." + extension;
				};
				name= name + extension;
			}
		};
		return name;
	}
	//
	protected boolean anExtensionIsToBeAppendedToThisFileName(String name) {
		int p1= name.lastIndexOf('/');
		if (p1 >= 0) {
			if (p1 >= name.length()-1) {
				return false;
			} else {
				name= name.substring(p1+1);
			}
		};
		p1= name.lastIndexOf(':');
		if (p1 >= 0) {
			if (p1 >= name.length()-1) {
				return false;
			} else {
				name= name.substring(p1+1);
			}
		};
		if (considerBackslashAsASepator()) {
			p1= name.lastIndexOf('\\');
			if (p1 >= 0) {
				if (p1 >= name.length()-1) {
					return false;
				} else {
					name= name.substring(p1+1);
				}
			}
		};
		name= name.trim();
		if (name.length() <= 0) {
			return false;
		} else if (name.indexOf('.') == -1) {
			return true;
		} else {
			return false;
		}
	}
	//
	protected boolean considerBackslashAsASepator() {
		if (backslashAlwaysIsSeparator) {
			return true;
		} else {
			String separator= fileSystem.getSeparator();
			if (separator.equals("\\")) {
				return true;
			} else {
				return false;
			}
		}
	}
	//
	protected static void installCookieManagerIfNecessary(StaticContext context) {
		if (DefaultOptions.manageCookies) {
			CookieHandler manager= StaticWebAttributes.retrieveCookieManager(context);
			if (manager==null) {
				ReentrantLock lock= StaticWebAttributes.retrieveWebGuard(context);
				lock.lock();
				try {
					manager= StaticWebAttributes.retrieveCookieManager(context);
					if (manager==null) {
						CookieManager defaultManager= new CookieManager();
						defaultManager.setCookiePolicy(DefaultOptions.cookiePolicy);
						manager= defaultManager;
						CookieHandler.setDefault(manager);
						StaticWebAttributes.setCookieManager(manager,context);
					}
				} finally {
					lock.unlock();
				}
			}
		}
	}
	//
	protected static boolean has_UTF_Coding(InputStream stream) {
		try {
			byte[] prefix;
			int k= 0;
			try {
				int bufferSize= 2;
				prefix= new byte[bufferSize];
				k= stream.read(prefix);
			} finally {
				stream.close();
			};
			if (k==2) {
				if	((Byte.compare(prefix[0],(byte)0xFF)==0 && Byte.compare(prefix[1],(byte)0xFE)==0) ||
					 (Byte.compare(prefix[0],(byte)0xFE)==0 && Byte.compare(prefix[1],(byte)0xFF)==0)) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} catch (IOException e) {
			return false;
		}
	}
	//
	protected static Term getChannelExceptionName(Throwable e1, URLConnection connection) {
		if (e1 instanceof SocketTimeoutException) {
			return new PrologSymbol(SymbolCodes.symbolCode_E_lateness);
		} else {
			if (connection != null && connection instanceof HttpURLConnection) {
				HttpURLConnection c2= (HttpURLConnection)connection;
				try {
					Term error= new PrologInteger(c2.getResponseCode());
					return new PrologStructure(SymbolCodes.symbolCode_E_failure,new Term[]{error});
				} catch (Throwable e2) {
					return channelExceptionToName(e2);
				}
			} else {
				return channelExceptionToName(e1);
			}
		}
	}
	//
	protected static String fixTheFilePrefix(String name) {
		if (name.length() >= 5 && name.substring(0,5).equalsIgnoreCase(stringFile)) {
			boolean valuableCharacterWasFound= false;
			int beginningPosition= 5;
			for (int n=5; n < name.length(); n++) {
				if (name.codePointAt(n) == '/') {
					beginningPosition= n + 1;
					continue;
				} else {
					valuableCharacterWasFound= true;
					break;
				}
			};
			if (valuableCharacterWasFound) {
				name= name.substring(beginningPosition);
				if (name.length() > 0) {
					int p1= name.indexOf(':');
					if (p1 >= 0) {
						int p2= name.indexOf('/');
						if (p2 < 0 || p2 > p1) {
							name= stringFile3Slash + name;
						}
					}
				}
			} else {
				name= "";
			}
		};
		return name;
	}
	//
	protected boolean textNameHasAScheme(String name) {
		int p1= name.indexOf(':');
		if (p1 <= 0) {
			return false;
		} else {
			int p2= name.indexOf('/');
			if (p2 >= 0 && p2 < p1) {
				return false;
			};
			p2= name.indexOf('\\');
			if (p2 >= 0 && p2 < p1) {
				return false;
			};
			if (!acceptOnlyUniformResourceIdentifiers) {
				if (p1 <= minimalLengthOfValidScheme-1) {
					return false;
				}
			};
			return true;
		}
	}
	//
	protected boolean textEndsWithSlash(String name) {
		if (name.endsWith("/")) {
			return true;
		} else if (considerBackslashAsASepator() && name.endsWith("\\")) {
			return true;
		} else if (name.length() <= 0) {
			return true;
		} else {
			return false;
		}
	}
	//
	protected static boolean is_JAR_resource(String location) {
		return location.length() >= 4 && location.substring(0,4).equals("jar:");
	}
	//
	protected static URL get_JAR_ResourceURL(String location, StaticContext staticContext) {
		location= location.substring(4);
		Path path= fileSystem.getPath(location);
		location= path.normalize().toString();
		location= replaceBackslashesAndSpaces(location);
		ClassLoader cl= staticContext.getClass().getClassLoader();
		URL url= cl.getResource(location);
		if (url != null) {
			return url;
		} else {
			throw new FileInputOutputError(location,null);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term toTerm() {
		if (!isStandardFile) {
			return new PrologString(textName);
		} else {
			long name;
			if (systemName==StandardFileName.STDIN) {
				name= SymbolCodes.symbolCode_E_stdin;
			} else if (systemName==StandardFileName.STDOUT) {
				name= SymbolCodes.symbolCode_E_stdout;
			} else {
				name= SymbolCodes.symbolCode_E_stderr;
			};
			return new PrologSymbol(name);
		}
	}
	public String toString() {
		if (!isStandardFile) {
			return textName;
		} else {
			long name;
			if (systemName==StandardFileName.STDIN) {
				name= SymbolCodes.symbolCode_E_stdin;
			} else if (systemName==StandardFileName.STDOUT) {
				name= SymbolCodes.symbolCode_E_stdout;
			} else {
				name= SymbolCodes.symbolCode_E_stderr;
			};
			return SymbolNames.retrieveSymbolName(name).toString();
		}
	}
}
