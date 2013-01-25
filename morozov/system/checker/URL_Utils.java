// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system.checker;

import target.*;

import morozov.classes.*;
import morozov.terms.*;
import morozov.system.*;
import morozov.system.files.*;

import javax.swing.JApplet;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.nio.file.FileSystems;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.concurrent.locks.ReentrantLock;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URISyntaxException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.CookieManager;
import java.net.CookieHandler;
import java.net.HttpURLConnection;

import java.util.ArrayList;

public class URL_Utils {
	//
	protected static final FileSystem fileSystem= FileSystems.getDefault();
	// protected static final BigDecimal oneMillionBig= BigDecimal.valueOf(1000000);
	protected static final BigDecimal negativeOne= BigDecimal.valueOf(-1);
	//
	public static void installCookieManagerIfNecessary(StaticContext context) {
		if (DefaultOptions.manageCookies) {
			// CookieManager 
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
						// manager= new DumbCookieHandler();
						CookieHandler.setDefault(manager);
						// CookieHandler.setDefault(new ListCookieHandler());
						// CookieHandler.setDefault(new DumbCookieHandler());
						// ((CookieManager)CookieHandler).setCookiePolicy(CookiePolicy.ACCEPT_ALL);
						StaticWebAttributes.setCookieManager(manager,context);
					}
				} finally {
					lock.unlock();
				}
			}
		}
	}
	//
	public static URL_Attributes getResourceAttributes(URI uri, CharacterSet characterSet, int timeout, StaticContext staticContext, boolean backslashIsSeparator) throws URISyntaxException, MalformedURLException, IOException {
		boolean isLocalResource= isLocalResource(uri);
		boolean isDirectoryName= isDirectoryURI(uri);
		if (isDirectoryName) {
			String path= uri.toString();
			path= deleteFilePrefixAndDecodeLocalFileName(path);
			if (path.isEmpty()) {
				path= FileUtils.getUserDirectory();
				uri= create_URI(path,staticContext,backslashIsSeparator);
			}
		};
		URL url= uri.toURL();
		try {
			URLConnection connection= url.openConnection();
			try {
				if (timeout >= 0) {
					connection.setConnectTimeout(timeout);
					connection.setReadTimeout(timeout);
				};
				connection.setUseCaches(false);
				InputStream stream= connection.getInputStream();
				boolean has_UTF_Coding= has_UTF_Coding(stream);
				try {
					return new URL_Attributes(
						uri,
						url,
						characterSet,
						timeout,
						isLocalResource,
						has_UTF_Coding,
						isDirectoryName,
						connection.getLastModified(),
						connection.getContentLength());
				} finally {
					stream.close();
				}
			} catch (Throwable e1) {
				Term exceptionName= getChannelExceptionName(e1,connection);
				return new URL_Attributes(
					uri,
					url,
					e1,
					exceptionName,
					characterSet,
					timeout,
					isLocalResource,
					false,
					isDirectoryName);
			}
		} catch (Throwable e1) {
			return new URL_Attributes(
				uri,
				url,
				e1,
				exceptionToName(e1),
				characterSet,
				timeout,
				isLocalResource,
				false,
				isDirectoryName);
		}
	}
	public static URL_Attributes renewResourceAttributes(URL_Attributes oldAttributes) throws IOException {
		URL url= oldAttributes.url;
		boolean isDirectoryName= isDirectoryURL(url); // url.toURI());
		URLConnection connection= url.openConnection();
		try {
			int timeout= oldAttributes.maxWaitingInterval;
			if (timeout >= 0) {
				connection.setConnectTimeout(timeout);
				connection.setReadTimeout(timeout);
			};
			InputStream stream= connection.getInputStream();
			try {
				return new URL_Attributes(
					oldAttributes,
					isDirectoryName,
					connection.getLastModified(),
					connection.getContentLength());
			} finally {
				stream.close();
			}
		} catch (Throwable e1) {
			Term exceptionName= getChannelExceptionName(e1,connection);
			return new URL_Attributes(
				oldAttributes,
				isDirectoryName,
				e1,
				exceptionName);
		}
	}
	//
	public static Term getChannelExceptionName(Throwable e1, URLConnection connection) {
		if (e1 instanceof SocketTimeoutException) {
			return new PrologSymbol(SymbolCodes.symbolCode_E_lateness);
		} else {
			if (connection != null && connection instanceof HttpURLConnection) {
				HttpURLConnection c2= (HttpURLConnection)connection;
				try {
					Term error= new PrologInteger(c2.getResponseCode());
					return new PrologStructure(SymbolCodes.symbolCode_E_failure,new Term[]{error});
				} catch (Throwable e2) {
					return exceptionToName(e2);
				}
			} else {
				return exceptionToName(e1);
			}
		}
	}
	public static Term exceptionToName(Throwable e) {
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
	public static boolean isLocalResource(String path, boolean backslashIsSeparator) {
		try {
			path= replaceSlashesAndSpaces(path,backslashIsSeparator);
			// path= replaceAmpersands(path);
			URI uri= new URI(path);
			return isLocalResource(uri);
		} catch (URISyntaxException e) {
		// } catch (MalformedURLException e) {
		// } catch (WrongTermIsMalformedURL e) {
		};
		return true;
	}
	public static boolean isLocalResource(URI uri) {
		if (uri.isAbsolute()) {
			String scheme= uri.getScheme();
			if (scheme.equalsIgnoreCase("file")) {
				return true;
			} else if (scheme.length()==1) {
				return true;
			};
			return false;
		};
		return true;
	}
	//
	public static boolean has_UTF_Coding(InputStream stream) {
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
	public static boolean isDirectoryURI(URI uri) {
		// String fileName= uri.toString();
		// fileName= deleteFilePrefixAndDecodeLocalFileName(fileName);
		// if (fileName.isEmpty()) {
		//	return true;
		// } else {
		try {
			Path path= fileSystem.provider().getPath(uri);
			// BasicFileAttributes bfa= Attributes.readBasicFileAttributes(path);
			BasicFileAttributes bfa= Files.readAttributes(path,BasicFileAttributes.class);
			return bfa.isDirectory();
		} catch (Throwable e) {
			return false;
		}
		// }
	}
	public static boolean isDirectoryURL(URL url) {
		// String fileName= uri.toString();
		// fileName= deleteFilePrefixAndDecodeLocalFileName(fileName);
		// if (fileName.isEmpty()) {
		//	return true;
		// } else {
		try {
			URI uri= url.toURI();
			Path path= fileSystem.provider().getPath(uri);
			// BasicFileAttributes bfa= Attributes.readBasicFileAttributes(path);
			BasicFileAttributes bfa= Files.readAttributes(path,BasicFileAttributes.class);
			return bfa.isDirectory();
		} catch (Throwable e) {
			return false;
		}
	}
	public static boolean isDirectoryPath(Path path) {
		// fileName= deleteFilePrefixAndDecodeLocalFileName(fileName);
		// if (fileName.isEmpty()) {
		//	return true;
		// } else {
		try {
			// Path path= fileSystem.getPath(fileName);
			// BasicFileAttributes bfa= Attributes.readBasicFileAttributes(path);
			BasicFileAttributes bfa= Files.readAttributes(path,BasicFileAttributes.class);
			return bfa.isDirectory();
		} catch (Throwable e) {
			return false;
		}
		// }
	}
	//
	public static String addFilePrefixToAbsolutePath(String path) {
		if (path.length() >= 8 && path.substring(0,8).equals("file:///")) {
			return path;
		} else {
			if (path.length() >= 2 && path.substring(1,2).equals(":")) {
				// path= deleteFilePrefix(path);
				return "file:///" + path;
			} else {
				if (path.length() >= 5 && path.substring(0,5).equalsIgnoreCase("file:")) {
					path= deleteFilePrefix(path);
					return "file:///" + path;
				} else {
					return path;
				}
			}
		}
	}
	public static String deleteFilePrefixAndDecodeLocalFileName(String path) {
		path= deleteFilePrefix(path);
		path= decodeLocalFileName(path);
		return path;
	}
	public static String deleteFilePrefix(String path) {
		if (path.length() >= 5 && path.substring(0,5).equalsIgnoreCase("file:")) {
			path= path.substring(5);
			int beginningPosition= StrictMath.min(path.length(),3);
			for (int n=0; n < beginningPosition; n++) {
				if (path.codePointAt(n) == '/') {
					continue;
				} else {
					beginningPosition= n;
					break;
				}
			};
			if (beginningPosition > 0) {
				path= path.substring(beginningPosition);
			}
		};
		return path;
	}
	public static String decodeLocalFileName(String location) {
		try {
			URI uri= new URI(location);
			String scheme= uri.getScheme();
			if (scheme!=null) {
				location= scheme + ":" + uri.getPath();
			} else {
				location= uri.getPath();
			};
			return location;
		} catch (URISyntaxException e) {
			// throw new WrongTermIsMalformedURL(e);
			return location;
		}
	}
	public static URI create_URI(String location, StaticContext staticContext, boolean backslashIsSeparator) {
		try {
			if (location.length() >= 4 && location.substring(0,4).equals("jar:")) {
				location= replaceSlashesAndSpaces(location.substring(4),backslashIsSeparator);
				ClassLoader cl= staticContext.getClass().getClassLoader();
				URL url= cl.getResource(location);
				return url.toURI();
			} else if (isLocalResource(location,backslashIsSeparator)) {
				JApplet applet= StaticContext.retrieveApplet(staticContext);
				if (applet==null) {
					location= replaceSlashesAndSpaces(location,backslashIsSeparator);
					location= deleteFilePrefixAndDecodeLocalFileName(location);
					Path path= FileUtils.resolveFileName(location);
					return path.toUri();
				} else {
					URL codeBase= applet.getCodeBase();
					location= replaceSlashesAndSpaces(location,backslashIsSeparator);
					location= addFilePrefixToAbsolutePath(location);
					return codeBase.toURI().resolve(location);
				}
			} else {
				location= replaceSlashesAndSpaces(location,backslashIsSeparator);
				return new URI(location);
			}
		} catch (URISyntaxException e) {
			throw new WrongTermIsMalformedURL(e);
		// } catch (MalformedURLException e) {
		//	throw new WrongTermIsMalformedURL(e);
		// } catch (UnsupportedEncodingException e) {
		//	throw new WrongTermIsMalformedURL(e);
		}
	}
	//
	public static String replaceSlashesAndSpaces(String path, boolean backslashIsSeparator) {
		path= path.replace(" ","%20");
		if (backslashIsSeparator) {
			return path.replace('\\','/');
		} else {
			return path;
		}
	}
	public static String replaceAmpersands(String path) {
		if (path.toLowerCase().contains("&amp")) {
			int length= path.length();
			StringBuilder buffer= new StringBuilder();
			int beginningOfSegment= 0;
			int p= 0;
			while(p < length) {
				if (path.regionMatches(true,p,"&amp;",0,5)) {
					buffer.append(path.substring(beginningOfSegment,p));
					buffer.append("&");
					beginningOfSegment= p + 5;
					p= p + 5;
				} else if (path.regionMatches(true,p,"&amp",0,4)) {
					buffer.append(path.substring(beginningOfSegment,p));
					buffer.append("&");
					beginningOfSegment= p + 4;
					p= p + 4;
				} else {
					p= p + 1;
				}
			};
			buffer.append(path.substring(beginningOfSegment));
			return buffer.toString();
		} else {
			return path;
		}
	}
	//
	public static String get_URL_string(String location, StaticContext staticContext, boolean backslashIsSeparator) {
		if (isLocalResource(location,backslashIsSeparator)) {
			JApplet applet= StaticContext.retrieveApplet(staticContext);
			if (applet==null) {
				location= replaceSlashesAndSpaces(location,backslashIsSeparator);
				location= deleteFilePrefixAndDecodeLocalFileName(location);
				return FileUtils.resolveAndEncodeLocalFileNameAddFilePrefix(location);
			} else {
				URL codeBase= applet.getCodeBase();
				try {
					location= replaceSlashesAndSpaces(location,backslashIsSeparator);
					location= addFilePrefixToAbsolutePath(location);
					URI uri= codeBase.toURI().resolve(location);
					return uri.toASCIIString();
				} catch (URISyntaxException e) {
					throw new WrongTermIsMalformedURL(e);
				}
			}
		} else {
			try {
				location= replaceSlashesAndSpaces(location,backslashIsSeparator);
				URI uri= new URI(location);
				return uri.toASCIIString();
			} catch (URISyntaxException e) {
				throw new WrongTermIsMalformedURL(e);
			}
		}
	}
	//
	public static int termToWaitingInterval(Term value, ChoisePoint iX) throws TermIsSymbolDefault {
		try {
			BigDecimal n= Converters.termToTimeInterval(value,iX).divide(Converters.oneMillionBig,MathContext.DECIMAL128);
			return PrologInteger.toInteger(n);
		} catch (TermIsNotTimeInterval e1) {
			try {
				long code= value.getSymbolValue(iX);
				if (code==SymbolCodes.symbolCode_E_any) {
					return -1;
				} else if (code==SymbolCodes.symbolCode_E_default) {
					throw new TermIsSymbolDefault();
				} else {
					throw new WrongArgumentIsNotTimeInterval(value);
				}
			} catch (TermIsNotASymbol e2) {
				throw new WrongArgumentIsNotTimeInterval(value);
			}
		}
	}
	//
	public static BigDecimal termToActionPeriod(Term value, ChoisePoint iX) throws TermIsSymbolDefault {
		try {
			return Converters.termToTimeInterval(value,iX);
		} catch (TermIsNotTimeInterval e1) {
			try {
				long code= value.getSymbolValue(iX);
				if (code==SymbolCodes.symbolCode_E_none) {
					return negativeOne;
				} else if (code==SymbolCodes.symbolCode_E_default) {
					throw new TermIsSymbolDefault();
				} else {
					throw new WrongArgumentIsNotTimeInterval(value);
				}
			} catch (TermIsNotASymbol e2) {
				throw new WrongArgumentIsNotTimeInterval(value);
			}
		}
	}
	//
	public static URI getURI(String location, String fileName, StaticContext staticContext, boolean backslashIsSeparator) throws URISyntaxException {
		if (location.isEmpty()) {
			if (isLocalResource(fileName,backslashIsSeparator)) {
				return getFullLocalURI(fileName,staticContext,backslashIsSeparator);
			} else {
				fileName= replaceSlashesAndSpaces(fileName,backslashIsSeparator);
				return new URI(fileName);
			}
		} else if (isLocalResource(location,backslashIsSeparator)) {
			return getFullLocalURI(location,fileName,staticContext,backslashIsSeparator);
		} else {
			location= replaceSlashesAndSpaces(location,backslashIsSeparator);
			URI uri= new URI(location);
			fileName= replaceSlashesAndSpaces(fileName,backslashIsSeparator);
			fileName= addFilePrefixToAbsolutePath(fileName);
			return uri.resolve(fileName);
		}
	}
	//
	public static String getFullName(String location, String fileName, StaticContext staticContext, boolean backslashIsSeparator) {
		if (location.isEmpty()) {
			return getFullName(fileName,staticContext,backslashIsSeparator);
		} else if (isLocalResource(location,backslashIsSeparator)) {
			return getFullLocalName(location,fileName,staticContext,backslashIsSeparator);
		} else {
			String base= replaceSlashesAndSpaces(location,backslashIsSeparator);
			try {
				URI uri= new URI(base);
				fileName= replaceSlashesAndSpaces(fileName,backslashIsSeparator);
				fileName= addFilePrefixToAbsolutePath(fileName);
				uri= uri.resolve(fileName);
				return uri.toString();
			} catch (URISyntaxException e) {
				return location;
			}
		}
	}
	public static String getFullName(String location, StaticContext staticContext, boolean backslashIsSeparator) {
		if (isLocalResource(location,backslashIsSeparator)) {
			return getFullLocalName(location,staticContext,backslashIsSeparator);
		} else {
			String base= replaceSlashesAndSpaces(location,backslashIsSeparator);
			try {
				URI uri= new URI(base);
				String fileName= replaceSlashesAndSpaces(location,backslashIsSeparator);
				fileName= addFilePrefixToAbsolutePath(fileName);
				uri= uri.resolve(fileName);
				return uri.toString();
			} catch (URISyntaxException e) {
				return location;
			}
		}
	}
	//
	public static URI getFullLocalURI(String location, String fileName, StaticContext staticContext, boolean backslashIsSeparator) throws URISyntaxException {
		location= deleteFilePrefixAndDecodeLocalFileName(location);
		fileName= deleteFilePrefixAndDecodeLocalFileName(fileName);
		JApplet applet= StaticContext.retrieveApplet(staticContext);
		if (applet==null) {
			Path base= fileSystem.getPath(location);
			// if (!isSubdirectoryPath(location,backslashIsSeparator)) {
			if (!isDirectoryPath(base)) {
				base= base.getParent();
				if (base != null) {
					Path file= base.resolve(fileName);
					return FileUtils.tryToMakeRealName(file).toUri();
				} else {
					Path file= fileSystem.getPath(fileName);
					return FileUtils.tryToMakeRealName(file).toUri();
				}
			} else {
				Path file= base.resolve(fileName);
				return FileUtils.tryToMakeRealName(file).toUri();
			}
		} else {
			URL codeBase= applet.getCodeBase();
			location= replaceSlashesAndSpaces(location,backslashIsSeparator);
			location= addFilePrefixToAbsolutePath(location);
			URI uri1= codeBase.toURI().resolve(location);
			fileName= replaceSlashesAndSpaces(fileName,backslashIsSeparator);
			fileName= addFilePrefixToAbsolutePath(fileName);
			URI uri2= uri1.resolve(fileName);
			return uri2;
		}
	}
	public static URI getFullLocalURI(String location, StaticContext staticContext, boolean backslashIsSeparator) throws URISyntaxException {
		location= deleteFilePrefixAndDecodeLocalFileName(location);
		JApplet applet= StaticContext.retrieveApplet(staticContext);
		if (applet==null) {
			Path file= fileSystem.getPath(location);
			return FileUtils.tryToMakeRealName(file).toUri();
		} else {
			URL codeBase= applet.getCodeBase();
			location= replaceSlashesAndSpaces(location,backslashIsSeparator);
			location= addFilePrefixToAbsolutePath(location);
			URI uri= codeBase.toURI().resolve(location);
			return uri;
		}
	}
	//
	public static String getFullLocalName(String location, String fileName, StaticContext staticContext, boolean backslashIsSeparator) {
		location= deleteFilePrefixAndDecodeLocalFileName(location);
		fileName= deleteFilePrefixAndDecodeLocalFileName(fileName);
		try {
			JApplet applet= StaticContext.retrieveApplet(staticContext);
			if (applet==null) {
				Path base= fileSystem.getPath(location);
				// if (!isSubdirectoryPath(location,backslashIsSeparator)) {
				if (!isDirectoryPath(base)) {
					base= base.getParent();
					if (base != null) {
						Path file= base.resolve(fileName);
						return FileUtils.tryToMakeRealName(file).toString();
					} else {
						Path file= fileSystem.getPath(fileName);
						return FileUtils.tryToMakeRealName(file).toString();
					}
				} else {
					Path file= base.resolve(fileName);
					return FileUtils.tryToMakeRealName(file).toString();
				}
			} else {
				URL codeBase= applet.getCodeBase();
				location= replaceSlashesAndSpaces(location,backslashIsSeparator);
				location= addFilePrefixToAbsolutePath(location);
				URI uri1= codeBase.toURI().resolve(location);
				fileName= replaceSlashesAndSpaces(fileName,backslashIsSeparator);
				fileName= addFilePrefixToAbsolutePath(fileName);
				URI uri2= uri1.resolve(fileName);
				return uri2.toASCIIString();
			}
		} catch (Throwable e) {
			return fileName;
		}
	}
	public static String getFullLocalName(String location, StaticContext staticContext, boolean backslashIsSeparator) {
		location= deleteFilePrefixAndDecodeLocalFileName(location);
		try {
			JApplet applet= StaticContext.retrieveApplet(staticContext);
			if (applet==null) {
				Path file= fileSystem.getPath(location);
				return FileUtils.tryToMakeRealName(file).toString();
			} else {
				URL codeBase= applet.getCodeBase();
				location= replaceSlashesAndSpaces(location,backslashIsSeparator);
				location= addFilePrefixToAbsolutePath(location);
				URI uri= codeBase.toURI().resolve(location);
				return uri.toASCIIString();
			}
		} catch (Throwable e) {
			return location;
		}
	}
	//
	public static String readTextFile(URL_Attributes attributes) throws Throwable {
		if (attributes.isLocalResource) {
			CharacterSet requestedCharacterSet= attributes.characterSet;
			if (requestedCharacterSet.isDummy() || requestedCharacterSet.isDefault()) {
				if (attributes.has_UTF_Coding) {
					String fileName= deleteFilePrefixAndDecodeLocalFileName(attributes.url.toExternalForm());
					return FileUtils.readLocalTextFile(fileName,new CharacterSet(CharacterSetType.UTF_16));
				} else {
					String fileName= deleteFilePrefixAndDecodeLocalFileName(attributes.url.toExternalForm());
					return FileUtils.readLocalTextFile(fileName);
				}
			} else {
				String fileName= deleteFilePrefixAndDecodeLocalFileName(attributes.url.toExternalForm());
				return FileUtils.readLocalTextFile(fileName,requestedCharacterSet);
			}
		} else {
			URLConnection connection= attributes.url.openConnection();
			CharacterSet characterSet= attributes.characterSet;
			int timeout= attributes.maxWaitingInterval;
			if (timeout >= 0) {
				connection.setConnectTimeout(timeout);
				connection.setReadTimeout(timeout);
			};
			InputStream stream= connection.getInputStream();
			try {
				int length= StrictMath.max(stream.available(),0xFFFF);
				byte[] readingBuffer= new byte[length];
				StringBuilder textBuffer= new StringBuilder();
				while(true) {
					int k= stream.read(readingBuffer,0,length);
					if (k < 0) {
						break;
					};
					textBuffer.append(new String(readingBuffer,0,k));
				};
				return textBuffer.toString();
			} finally {
				stream.close();
			}
		}
	}
	//
	public static byte[] readBytesFromFile(URL_Attributes attributes) throws Throwable {
		if (attributes.isLocalResource) {
			// CharacterSet requestedCharacterSet= attributes.characterSet;
			String fileName= deleteFilePrefixAndDecodeLocalFileName(attributes.url.toExternalForm());
			return FileUtils.readBytesFromLocalFile(fileName);
		} else {
			URLConnection connection= attributes.url.openConnection();
			// CharacterSet characterSet= attributes.characterSet;
			int timeout= attributes.maxWaitingInterval;
			if (timeout >= 0) {
				connection.setConnectTimeout(timeout);
				connection.setReadTimeout(timeout);
			};
			InputStream stream= connection.getInputStream();
			try {
				int length= StrictMath.max(stream.available(),0xFFFF);
				byte[] readingBuffer= new byte[length];
				// StringBuilder totalBuffer= new StringBuilder();
				ArrayList<Byte> totalBuffer= new ArrayList<Byte>();
				while(true) {
					int k= stream.read(readingBuffer,0,length);
					if (k < 0) {
						break;
					};
					// totalBuffer.append(new String(readingBuffer,0,k));
					for (int n=0; n < k; n++) {
						totalBuffer.add(readingBuffer[n]);
					}
				};
				// return totalBuffer.toString();
				// return totalBuffer.toArray(new Byte[0]);
				// return totalBuffer.toArray();
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
	public static byte[] getContentOfResource(URI uri, CharacterSet characterSet, int timeout, StaticContext staticContext, boolean backslashIsSeparator) throws CannotRetrieveContent {
		try {
			installCookieManagerIfNecessary(staticContext);
			URL_Attributes attributes= getResourceAttributes(uri,characterSet,timeout,staticContext,backslashIsSeparator);
			try {
				byte[] array= readBytesFromFile(attributes);
				if (attributes.connectionWasSuccessful()) {
					return array;
				} else {
					throw new CannotRetrieveContent(attributes.getExceptionName());
				}
			} catch (Throwable e2) {
				throw new CannotRetrieveContent(e2);
			// } finally {
			//	attributes.safeCloseConnection();
			}
		} catch (URISyntaxException e) {
			throw new WrongTermIsMalformedURL(e);
		// } catch (MalformedURLException e1) {
		//	throw e1;
		} catch (IOException e1) {
			throw new FileInputOutputError(uri.toString(),e1);
		// } catch (Throwable e1) {
		//	return URL_Utils.exceptionToName(e1);
		}
	}
}
