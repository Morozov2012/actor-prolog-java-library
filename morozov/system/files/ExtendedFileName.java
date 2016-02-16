// (c) 2010-2015 IRE RAS Alexei A. Morozov

package morozov.system.files;

import morozov.run.*;
import morozov.system.checker.*;
import morozov.system.checker.errors.*;
import morozov.system.checker.signals.*;
import morozov.system.datum.*;
import morozov.system.files.errors.*;
import morozov.terms.*;

import javax.swing.JApplet;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.DosFileAttributes;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.NoSuchFileException;
import java.nio.file.AccessDeniedException;
import java.nio.charset.Charset;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.ByteArrayInputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URISyntaxException;
import java.net.MalformedURLException;
import java.util.List;

public class ExtendedFileName extends RelativeFileName {
	protected boolean hasScheme= false;
	protected boolean endsWithSlash= false;
	protected boolean isLocalResource= false;
	protected Path filePath;
	protected URI fileURI;
	protected static String defaultGraphicsFormatName= "png";
	protected static final Term termEmptyPrologString= new PrologString("");
	protected static final Charset completeCharset= Charset.forName("ISO-8859-1");
	//
	static {
		try {
			ImageIO.setUseCache(false);
		} catch (SecurityException e) {
		}
	};
	//
	///////////////////////////////////////////////////////////////
	//
	public ExtendedFileName(String initialName, boolean backslashIsSeparator, boolean acceptOnlyURI, boolean createLocalName, Path currentDirectory, StaticContext staticContext) {
		super(initialName,backslashIsSeparator,acceptOnlyURI);
		hasScheme= textNameHasAScheme(textName);
		endsWithSlash= textEndsWithSlash(textName);
		if (hasScheme || acceptOnlyUniformResourceIdentifiers) {
			String location= replaceBackslashesAndSpaces(textName);
			try {
				if (is_JAR_resource(location)) {
					if (createLocalName) {
						throw new ThisIsNotALocalResource(location);
					};
					isLocalResource= false;
					fileURI= get_JAR_ResourceURL(location,staticContext).toURI();
				} else {
					location= fixTheFilePrefix(location);
					URI localURI= new URI(location);
					if (isLocalResourceURI(localURI)) {
						JApplet applet= StaticContext.retrieveApplet(staticContext);
						if (applet==null) {
							storeFilePath(localURI,currentDirectory);
						} else {
							URL codeBase= applet.getCodeBase();
							if (codeBase != null) {
								URI baseURI= codeBase.toURI();
								fileURI= baseURI.resolve(localURI);
								isLocalResource= isLocalResourceURI(fileURI);
								if (isLocalResource) {
									filePath= Paths.get(fileURI);
								} else {
									if (createLocalName) {
										throw new ThisIsNotALocalResource(fileURI.toString());
									}
								}
							} else { // A probable bug in JDK 1.7.0_40
								storeFilePath(localURI,currentDirectory);
							}
						}
					} else {
						if (createLocalName) {
							throw new ThisIsNotALocalResource(localURI.toString());
						};
						isLocalResource= false;
						URI baseURI= pathToBaseDirectoryURI(currentDirectory,true);
						fileURI= baseURI.resolve(location);
					}
				}
			} catch (URISyntaxException e) {
				throw new WrongArgumentIsMalformedURL(location,e);
			}
		} else {
			storeFilePath(textName,currentDirectory);
		}
	}
	public ExtendedFileName(String initialName, boolean backslashIsSeparator, boolean acceptOnlyURI, boolean createLocalName, ExtendedFileName currentDirectory, StaticContext staticContext) {
		super(initialName,backslashIsSeparator,acceptOnlyURI);
		hasScheme= textNameHasAScheme(textName);
		endsWithSlash= textEndsWithSlash(textName);
		if (hasScheme || acceptOnlyUniformResourceIdentifiers) {
			String location= replaceBackslashesAndSpaces(textName);
			try {
				if (is_JAR_resource(location)) {
					if (createLocalName) {
						throw new ThisIsNotALocalResource(location);
					};
					isLocalResource= false;
					fileURI= get_JAR_ResourceURL(location,staticContext).toURI();
				} else {
					location= fixTheFilePrefix(location);
					URI localURI= new URI(location);
					if (isLocalResourceURI(localURI)) {
						JApplet applet= StaticContext.retrieveApplet(staticContext);
						storeFilePathOrURI(localURI,currentDirectory,createLocalName);
					} else {
						if (createLocalName) {
							throw new ThisIsNotALocalResource(localURI.toString());
						};
						isLocalResource= false;
						URI baseURI= currentDirectory.formBaseDirectoryURI();
						fileURI= baseURI.resolve(location);
					}
				}
			} catch (URISyntaxException e) {
				throw new WrongArgumentIsMalformedURL(location,e);
			}
		} else {
			storeFilePathOrURI(textName,currentDirectory,createLocalName);
		}
	}
	public ExtendedFileName(StandardFileName name) {
		super(name);
		isLocalResource= true;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void storeFilePath(String name, Path currentDirectory) {
		isLocalResource= true;
		if (considerBackslashAsASepator()) {
			name= replaceBackslashes(name);
		};
		Path localPath= fileSystem.getPath(name);
		localPath= verifyBaseDirectory(currentDirectory,true).resolve(localPath).normalize();
		filePath= tryToMakeRealName(localPath);
	}
	//
	protected void storeFilePath(URI localURI, Path currentDirectory) {
		isLocalResource= true;
		URI baseURI= pathToBaseDirectoryURI(currentDirectory,true);
		fileURI= baseURI.resolve(localURI.normalize());
		filePath= Paths.get(fileURI);
	}
	//
	protected void storeFilePathOrURI(String name, ExtendedFileName currentDirectory, boolean createLocalName) {
		if (currentDirectory.getIsLocalResource()) {
			isLocalResource= true;
			if (considerBackslashAsASepator()) {
				name= replaceBackslashes(name);
			};
			Path localPath= fileSystem.getPath(name);
			localPath= verifyBaseDirectory(currentDirectory).resolve(localPath).normalize();
			filePath= tryToMakeRealName(localPath);
		} else {
			if (createLocalName) {
				throw new ThisIsNotALocalResource(name);
			};
			isLocalResource= false;
			URI baseURI= currentDirectory.formBaseDirectoryURI();
			fileURI= baseURI.resolve(name);
		}
	}
	//
	protected void storeFilePathOrURI(URI localURI, ExtendedFileName currentDirectory, boolean createLocalName) {
		if (currentDirectory.getIsLocalResource()) {
			isLocalResource= true;
			URI baseURI= currentDirectory.formBaseDirectoryURI();
			fileURI= baseURI.resolve(localURI);
			filePath= Paths.get(fileURI);
		} else {
			if (createLocalName) {
				throw new ThisIsNotALocalResource(localURI.toString());
			};
			isLocalResource= false;
			URI baseURI= currentDirectory.formBaseDirectoryURI();
			fileURI= baseURI.resolve(localURI);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term getUniversalResourceParameters(int timeout, CharacterSet characterSet, StaticContext staticContext, ChoisePoint iX) throws URISyntaxException, MalformedURLException, IOException {
		URL_Attributes attributes= getUniversalResourceAttributes(timeout,characterSet,staticContext);
		return attributes.toTerm();
	}
	//
	public URL_Attributes getUniversalResourceAttributes(int timeout, CharacterSet characterSet, StaticContext staticContext) throws URISyntaxException, MalformedURLException, IOException {
		URI uri= get_URI_OfResource();
		installCookieManagerIfNecessary(staticContext);
		boolean isLocalResource= isLocalResourceURI(uri);
		boolean isDirectoryName= isDirectoryURI(uri);
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
					long lastModificationTime;
					try {
						lastModificationTime= connection.getLastModified();
					} catch (Throwable e1) {
						lastModificationTime= 0;
					};
					long contentLength;
					try {
						contentLength= connection.getContentLengthLong();
					} catch (Throwable e1) {
						contentLength= -1;
					};
					return new URL_Attributes(
						uri,
						url,
						characterSet,
						timeout,
						isLocalResource,
						has_UTF_Coding,
						isDirectoryName,
						lastModificationTime,
						contentLength);
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
				channelExceptionToName(e1),
				characterSet,
				timeout,
				isLocalResource,
				false,
				isDirectoryName);
		}
	}
	//
	public static URL_Attributes renewUniversalResourceAttributes(URL_Attributes oldAttributes) throws IOException {
		URL url= oldAttributes.url;
		boolean isDirectoryName= isDirectoryURI(oldAttributes.uri);
		URLConnection connection= url.openConnection();
		try {
			int timeout= oldAttributes.maxWaitingInterval;
			if (timeout >= 0) {
				connection.setConnectTimeout(timeout);
				connection.setReadTimeout(timeout);
			};
			InputStream stream= connection.getInputStream();
			long lastModificationTime;
			try {
				lastModificationTime= connection.getLastModified();
			} catch (Throwable e1) {
				lastModificationTime= 0;
			};
			long contentLength;
			try {
				contentLength= connection.getContentLengthLong();
			} catch (Throwable e1) {
				contentLength= -1;
			};
			try {
				return new URL_Attributes(
					oldAttributes,
					isDirectoryName,
					lastModificationTime,
					contentLength);
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
	public Term getUniversalResourceContent(int timeout, CharacterSet characterSet, StaticContext staticContext, ChoisePoint iX) throws URISyntaxException, MalformedURLException, IOException {
		Term result;
		try {
			URL_Attributes attributes= getUniversalResourceAttributes(timeout,characterSet,staticContext);
			result= getTermContentOfUniversalResource(attributes);
		} catch (CannotRetrieveContent e) {
			result= e.getExceptionName();
		} catch (Throwable e) {
			throw e;
		};
		return result;
	}
	//
	public Term getTermContentOfUniversalResource(URL_Attributes attributes) throws CannotRetrieveContent, URISyntaxException, MalformedURLException, IOException {
		try {
			Term result;
			if (attributes.isDirectory) {
				List<Path> list= retrieveDirectoryList(attributes.uri);
				StringBuilder buffer= new StringBuilder();
				for (int n=0; n < list.size(); n++) {
					buffer.append(tryToMakeRealName(list.get(n)).toString());
					buffer.append("\n");
				};
				result= new PrologString(buffer.toString());
			} else {
				String text= readStringFromUniversalResource(attributes);
				result= new PrologString(text);
			};
			if (attributes.connectionWasSuccessful()) {
				return result;
			} else {
				throw new CannotRetrieveContent(attributes.getExceptionName());
			}
		} catch (CannotRetrieveContent e2) {
			throw e2;
		} catch (Throwable e2) {
			throw new CannotRetrieveContent(e2);
		}
	}
	//
	public String getTextContentOfUniversalResource(int timeout, CharacterSet characterSet, StaticContext staticContext) throws CannotRetrieveContent {
		URI uri= get_URI_OfResource();
		try {
			// installCookieManagerIfNecessary(staticContext);
			URL_Attributes attributes= getUniversalResourceAttributes(timeout,characterSet,staticContext);
			try {
				String text= readStringFromUniversalResource(attributes);
				if (attributes.connectionWasSuccessful()) {
					return text;
				} else {
					throw new CannotRetrieveContent(attributes.getExceptionName());
				}
			} catch (Throwable e2) {
				throw new CannotRetrieveContent(e2);
			}
		} catch (URISyntaxException e1) {
			throw new WrongArgumentIsMalformedURL(uri.toString(),e1);
		// } catch (MalformedURLException e1) {
		//	throw e1;
		} catch (IOException e1) {
			throw new FileInputOutputError(uri.toString(),e1);
		// } catch (Throwable e1) {
		//	return channelExceptionToName(e1);
		}
	}
	//
	public byte[] getByteContentOfUniversalResource(CharacterSet characterSet, int timeout, StaticContext staticContext) {
		URI uri= get_URI_OfResource();
		try {
			URL_Attributes attributes= getUniversalResourceAttributes(timeout,characterSet,staticContext);
			try {
				byte[] array= readBytesFromUniversalResource(attributes);
				if (attributes.connectionWasSuccessful()) {
					if (characterSet.isDummyOrDefault()) {
						return array;
					} else {
						String buffer= new String(array,characterSet.toCharSet());
						array= buffer.getBytes(completeCharset);
						return array;
					}
				} else {
					throw new CannotRetrieveContent(attributes.getExceptionName());
				}
			} catch (Throwable e2) {
				throw new CannotRetrieveContent(e2);
			// } finally {
			//	attributes.safeCloseConnection();
			}
		} catch (CannotRetrieveContent e) {
			throw new FileInputOutputError(uri.toString(),e);
		} catch (URISyntaxException e1) {
			throw new WrongArgumentIsMalformedURL(uri.toString(),e1);
		// } catch (MalformedURLException e1) {
		//	throw e1;
		} catch (IOException e1) {
			throw new FileInputOutputError(uri.toString(),e1);
		// } catch (Throwable e1) {
		//	return channelExceptionToName(e1);
		}
	}
	//
	public Object getObjectContentOfUniversalResource(int timeout, StaticContext staticContext) throws CannotRetrieveContent {
		URI uri= get_URI_OfResource();
		try {
			URL_Attributes attributes= getUniversalResourceAttributes(timeout,CharacterSet.NONE,staticContext);
			try {
				Object object= readObjectFromUniversalResource(attributes);
				if (!attributes.connectionWasSuccessful()) {
					return object;
				} else {
					throw new CannotRetrieveContent(attributes.getExceptionName());
				}
			} catch (Throwable e2) {
				throw new CannotRetrieveContent(e2);
			// } finally {
			//	attributes.safeCloseConnection();
			}
		} catch (URISyntaxException e1) {
			throw new WrongArgumentIsMalformedURL(uri.toString(),e1);
		// } catch (MalformedURLException e1) {
		//	throw e1;
		} catch (IOException e1) {
			throw new FileInputOutputError(uri.toString(),e1);
		// } catch (Throwable e1) {
		//	return channelExceptionToName(e1);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void isExistentLocalResource(int timeout, CharacterSet characterSet, StaticContext staticContext) throws Backtracking {
		try {
			if (!doesExist(true,timeout,characterSet,staticContext)) {
				throw Backtracking.instance;
			}
		} catch (Throwable e) {
			throw Backtracking.instance;
		}
	}
	public void doesExist(int timeout, CharacterSet characterSet, StaticContext staticContext) throws Backtracking {
		try {
			if (!doesExist(false,timeout,characterSet,staticContext)) {
				throw Backtracking.instance;
			}
		} catch (Throwable e) {
			throw Backtracking.instance;
		}
	}
	//
	protected boolean doesExist(boolean considerLocalResourcesOnly, int timeout, CharacterSet characterSet, StaticContext staticContext) {
		if (!isStandardFile) {
			if (isLocalResource) {
				return Files.exists(filePath);
			} else {
				if (considerLocalResourcesOnly && !isLocalResource) {
					return false;
				};
				try {
					URL_Attributes attributes= getUniversalResourceAttributes(timeout,characterSet,staticContext);
					if (attributes.connectionWasSuccessful()) {
						return true;
					} else {
						return false;
					}
				} catch (MalformedURLException e) {
					// throw new WrongArgumentIsMalformedURL(e);
					return false;
				} catch (URISyntaxException e) {
					// throw new WrongArgumentIsMalformedURL(e);
					return false;
				} catch (IOException e) {
					// throw new WrongArgumentIsMalformedURL(e);
					return false;
				}
			}
		} else {
			return true;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void isLocalResource() throws Backtracking {
		if (!isLocalResource) {
			throw Backtracking.instance;
		}
	}
	//
	public boolean getIsLocalResource() {
		return isLocalResource;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Path setCurrentDirectory() {
		if (!isStandardFile) {
			Path directoryPath= getPathOfLocalResource();
			if (Files.exists(directoryPath)) {
				if (Files.isDirectory(directoryPath)) {
					return directoryPath;
				} else {
					throw new FileIsNotADirectory(directoryPath.toString());
				}
			} else {
				throw new DirectoryDoesNotExist(directoryPath.toString());
			}
		} else {
			throw new StandardStreamIsNotAllowedInThisOperation();
		}
	}
	//
	public static String getCurrentDirectory(Path currentDirectory) {
		if (currentDirectory==null) {
			return getUserDirectory();
		} else {
			return currentDirectory.toString();
		}
	}
	//
	public void makeDirectory() {
		if (!isStandardFile) {
			Path targetPath= getPathOfLocalResource();
			createDirectories(targetPath,false);
		} else {
			throw new StandardStreamIsNotAllowedInThisOperation();
		}
	}
	//
	public static Term listDirectory(Path currentDirectory, String mask) {
		List<Path> list;
		if (currentDirectory==null) {
			Path path= fileSystem.getPath(getUserDirectory());
			list= listSourceFiles(path,mask);
		} else {
			list= listSourceFiles(currentDirectory,mask);
		};
		Term result= PrologEmptyList.instance;
		for (int n=list.size()-1; n >= 0; n--) {
			result= new PrologList(new PrologString(list.get(n).toString()),result);
		};
		return result;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void clear() {
		if (!isStandardFile) {
			Path path= getPathOfLocalResource();
			try {
				createDirectories(path,true);
				Files.write(path,new byte[0]);
			} catch (java.io.IOException e) {
				throw new FileInputOutputError(toString(),e);
			}
		} else {
			if (systemName==StandardFileName.STDIN) {
				// throw new StandardInputStreamDoesNotSupportThisOperation();
				try {
					int n= System.in.available();
					System.in.read(new byte[n]);
				} catch (IOException e) {
				}
			} else if (systemName==StandardFileName.STDOUT) {
				System.out.flush();
			} else {
				System.err.flush();
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void delete() {
		if (!isStandardFile) {
			Path targetPath= getPathOfLocalResource();
			try {
				Files.deleteIfExists(targetPath);
			} catch (DirectoryNotEmptyException e) {
			} catch (IOException e) {
			}
		} else {
			throw new StandardStreamIsNotAllowedInThisOperation();
		}
	}
	//
	public void renameFile(ExtendedFileName fileName2) {
		if (!isStandardFile && !fileName2.isStandardFile) {
			Path path1= getPathOfLocalResource();
			Path path2= fileName2.getPathOfLocalResource();
			try {
				Files.move(path1,path2,StandardCopyOption.ATOMIC_MOVE);
			// } catch (UnsupportedOperationException e) {
			// } catch (FileAlreadyExistsException e) {
			// } catch (AtomicMoveNotSupportedException e) {
			} catch (IOException e) {
				throw new FileInputOutputError(toString(),fileName2.toString(),e);
			// } catch (SecurityException e) {
			}
		} else {
			throw new StandardStreamIsNotAllowedInThisOperation();
		}
	}
	//
	public void copyFile(ExtendedFileName fileName2) {
		if (!isStandardFile && !fileName2.isStandardFile) {
			Path path1= getPathOfLocalResource();
			Path path2= fileName2.getPathOfLocalResource();
			try {
				Files.copy(path1,path2,StandardCopyOption.COPY_ATTRIBUTES);
			// } catch (UnsupportedOperationException e) {
			// } catch (FileAlreadyExistsException e) {
			} catch (IOException e) {
				throw new FileInputOutputError(toString(),fileName2.toString(),e);
			// } catch (SecurityException e) {
			}
		} else {
			throw new StandardStreamIsNotAllowedInThisOperation();
		}
	}
	//
	public void create_BAK_File() {
		if (!isStandardFile) {
			Path path1= getPathOfLocalResource();
			String fileName2= modifyFileExtension(".bak");
			Path path2= fileSystem.getPath(fileName2);
			try {
				Files.deleteIfExists(path2);
				Files.move(path1,path2,StandardCopyOption.ATOMIC_MOVE);
			} catch (IOException e) {
			}
		}
	}
	//
	public ExtendedFileName modifyFileExtension(String newExtension, boolean createLocalName, Path currentDirectory, StaticContext staticContext) {
		if (!isStandardFile) {
			Path path1= getPathOfLocalResource();
			String fileName2= modifyFileExtension(newExtension);
			SimpleFileName sFN2= termToSimpleFileName(fileName2,backslashAlwaysIsSeparator,acceptOnlyUniformResourceIdentifiers);
			ExtendedFileName eFN2= sFN2.formRealFileNameBasedOnPath(createLocalName,true,newExtension,currentDirectory,staticContext);
			return eFN2;
		} else {
			throw new StandardStreamIsNotAllowedInThisOperation();
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void isDirectory() throws Backtracking {
		if (!isStandardFile) {
			Path path= getPathOfLocalResource();
			try {
				BasicFileAttributes bfa= Files.readAttributes(path,BasicFileAttributes.class);
				if (!bfa.isDirectory()) {
					throw Backtracking.instance;
				}
			} catch (IOException e) {
				throw Backtracking.instance;
			}
		} else {
			throw Backtracking.instance;
		}
	}
	//
	public void isNormal() throws Backtracking {
		if (!isStandardFile) {
			Path path= getPathOfLocalResource();
			try {
				if (	!Files.exists(path) ||
					Files.isHidden(path) ||
					!Files.isReadable(path) ||
					!Files.isWritable(path) ) {
					throw Backtracking.instance;
				} else {
					BasicFileAttributes bfa= Files.readAttributes(path,BasicFileAttributes.class);
					if (	!bfa.isRegularFile() ||
						bfa.isDirectory() ) {
						throw Backtracking.instance;
					}
				}
			} catch (UnsupportedOperationException e) {
				throw Backtracking.instance;
			} catch (NoSuchFileException e) {
				throw Backtracking.instance;
			} catch (AccessDeniedException e) {
				throw Backtracking.instance;
			} catch (IOException e) {
				throw Backtracking.instance;
			} catch (SecurityException e) {
				throw Backtracking.instance;
			}
		}
	}
	//
	public void isArchive() throws Backtracking {
		if (!isStandardFile) {
			Path path= getPathOfLocalResource();
			try {
				DosFileAttributes da= Files.readAttributes(path,DosFileAttributes.class);
				if (!da.isArchive()) {
					throw Backtracking.instance;
				}
			} catch (UnsupportedOperationException e) {
				throw Backtracking.instance;
			} catch (IOException e) {
				throw Backtracking.instance;
			}
		} else {
			throw Backtracking.instance;
		}
	}
	//
	public void isHidden() throws Backtracking {
		if (!isStandardFile) {
			Path path= getPathOfLocalResource();
			try {
				if (!Files.isHidden(path)) {
					throw Backtracking.instance;
				}
			} catch (IOException e) {
				throw Backtracking.instance;
			}
		} else {
			throw Backtracking.instance;
		}
	}
	//
	public void isReadOnly() throws Backtracking {
		if (!isStandardFile) {
			Path path= getPathOfLocalResource();
			if (!Files.exists(path) || Files.isWritable(path)) {
				throw Backtracking.instance;
			}
		} else {
			if (systemName != StandardFileName.STDIN) {
				throw Backtracking.instance;
			}
		}
	}
	//
	public void isSystem() throws Backtracking {
		if (!isStandardFile) {
			Path path= getPathOfLocalResource();
			try {
				DosFileAttributes da= Files.readAttributes(path,DosFileAttributes.class);
				if (!da.isSystem()) {
					throw Backtracking.instance;
				}
			} catch (UnsupportedOperationException e) {
				throw Backtracking.instance;
			} catch (IOException e) {
				throw Backtracking.instance;
			}
		} else {
			throw Backtracking.instance;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setArchive(boolean flag) {
		if (!isStandardFile) {
			Path path= getPathOfLocalResource();
			try {
				if (Files.exists(path)) {
					Files.setAttribute(path,"dos:archive",flag);
				}
			} catch (UnsupportedOperationException e) {
			// } catch (IllegalArgumentException e) {
			// } catch (ClassCastException e) {
			} catch (IOException e) {
			}
		} else {
			throw new StandardStreamIsNotAllowedInThisOperation();
		}
	}
	//
	public void setHidden(boolean flag) {
		if (!isStandardFile) {
			Path path= getPathOfLocalResource();
			try {
				if (Files.exists(path)) {
					Files.setAttribute(path,"dos:hidden",flag);
				}
			} catch (UnsupportedOperationException e) {
			// } catch (IllegalArgumentException e) {
			// } catch (ClassCastException e) {
			} catch (IOException e) {
			}
		} else {
			throw new StandardStreamIsNotAllowedInThisOperation();
		}
	}
	//
	public void setReadOnly(boolean flag) {
		if (!isStandardFile) {
			Path path= getPathOfLocalResource();
			try {
				if (Files.exists(path)) {
					Files.setAttribute(path,"dos:readonly",flag);
				}
			} catch (UnsupportedOperationException e) {
			// } catch (IllegalArgumentException e) {
			// } catch (ClassCastException e) {
			} catch (IOException e) {
			}
		} else {
			if (systemName==StandardFileName.STDIN) {
				if (!flag) {
					throw new StandardInputStreamDoesNotSupportThisOperation();
				}
			} else if (systemName==StandardFileName.STDOUT) {
				if (flag) {
					throw new StandardOutputStreamDoesNotSupportThisOperation();
				}
			} else if (systemName==StandardFileName.STDERR) {
				if (flag) {
					throw new StandardErrorOutputStreamDoesNotSupportThisOperation();
				}
			} else {
				throw new StandardStreamsDoNotSupportReadWriteAccess();
			}
		}
	}
	//
	public void setSystem(boolean flag) {
		if (!isStandardFile) {
			Path path= getPathOfLocalResource();
			try {
				if (Files.exists(path)) {
					Files.setAttribute(path,"dos:system",flag);
				}
			} catch (UnsupportedOperationException e) {
			// } catch (IllegalArgumentException e) {
			// } catch (ClassCastException e) {
			} catch (IOException e) {
			}
		} else {
			throw new StandardStreamIsNotAllowedInThisOperation();
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term getString(CharacterSet requiredCharacterSet) {
		if (!isStandardFile) {
			Path path= getPathOfLocalResource();
			return new PrologString(readStringFromLocalFile(path,requiredCharacterSet));
		} else {
			if (systemName==StandardFileName.STDIN) {
				String buffer= readStringFromStdIn(requiredCharacterSet);
				if (buffer != null) {
					return new PrologString(buffer);
				} else {
					return termEmptyPrologString;
				}
			} else if (systemName==StandardFileName.STDOUT) {
				throw new StandardOutputStreamDoesNotSupportThisOperation();
			} else {
				throw new StandardErrorOutputStreamDoesNotSupportThisOperation();
			}
		}
	}
	//
	public String getTextData(int timeout, CharacterSet requestedCharacterSet, StaticContext staticContext) throws CannotRetrieveContent {
		if (!isStandardFile) {
			// URI uri= get_URI_OfResource();
			return getTextContentOfUniversalResource(timeout,requestedCharacterSet,staticContext);
		} else {
			if (systemName==StandardFileName.STDIN) {
				String buffer= readStringFromStdIn(requestedCharacterSet);
				if (buffer != null) {
					return buffer;
				} else {
					return "";
				}
			} else if (systemName==StandardFileName.STDOUT) {
				throw new StandardOutputStreamDoesNotSupportThisOperation();
			} else {
				throw new StandardErrorOutputStreamDoesNotSupportThisOperation();
			}
		}
	}
	//
	public java.awt.image.BufferedImage readImage(int timeout, StaticContext staticContext) {
		byte[] array= getByteContentOfUniversalResource(CharacterSet.NONE,timeout,staticContext);
		InputStream stream= new ByteArrayInputStream(array);
		try {
			java.awt.image.BufferedImage image= ImageIO.read(stream);
			return image;
		} catch(IOException e) {
			throw new FileInputOutputError(toString(),e);
		}
	}
	//
	public Object readObject(int timeout, StaticContext staticContext) {
		try {
			if (!isStandardFile) {
				if (isLocalResource) {
					Path path= getPathOfLocalResource();
					InputStream inputStream= Files.newInputStream(path);
					ObjectInputStream objectStream= new DataStoreInputStream(new BufferedInputStream(inputStream),true);
					try {
						return objectStream.readObject();
					} finally {
						objectStream.close();
					}
				} else {
					try {
						return getObjectContentOfUniversalResource(timeout,staticContext);
					} catch (CannotRetrieveContent e) {
						throw new FileInputOutputError(toString(),e);
					}
				}
			} else {
				if (systemName==StandardFileName.STDIN) {
					ObjectInputStream objectStream= new DataStoreInputStream(new BufferedInputStream(System.in),true);
					return objectStream.readObject();
				} else {
					throw new StandardOutputStreamDoesNotSupportThisOperation();
				}
			}
		} catch (ClassNotFoundException e) {
			throw new FileInputOutputError(toString(),e);
		} catch (IOException e) {
			throw new FileInputOutputError(toString(),e);
		}
	}
	//
	public void writeTextFile(String text, CharacterSet characterSet) throws IOException {
		if (!isStandardFile) {
			Path path= getPathOfLocalResource();
			createDirectories(path,true);
			byte[] dst;
			if (characterSet.isDummy()) {
				dst= new byte[text.length()];
				dst= text.getBytes(completeCharset);
			} else {
				dst= text.getBytes(characterSet.toCharSet());
			};
			Files.write(path,dst);
		} else {
			if (systemName==StandardFileName.STDOUT) {
				System.out.print(text);
			} else if (systemName==StandardFileName.STDERR) {
				System.err.print(text);
			} else {
				throw new StandardInputStreamDoesNotSupportThisOperation();
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public String extractGraphicsFormatName() {
		if (!isStandardFile) {
			String formatName= extractFileNameExtension();
			if (formatName.length() > 0 && formatName.codePointAt(0)=='.') {
				formatName= formatName.substring(1);
			};
			return formatName.toLowerCase();
		} else {
			return defaultGraphicsFormatName;
		}
	}
	//
	public void writeObject(Object tableHash) {
		try {
			if (!isStandardFile) {
				Path path= getPathOfLocalResource();
				OutputStream outputStream= Files.newOutputStream(path);
				ObjectOutputStream objectStream= new DataStoreOutputStream(new BufferedOutputStream(outputStream));
				try {
					objectStream.writeObject(tableHash);
				} finally {
					objectStream.close();
				}
			} else {
				if (systemName==StandardFileName.STDIN) {
					throw new StandardInputStreamDoesNotSupportThisOperation();
				} else if (systemName==StandardFileName.STDOUT) {
					ObjectOutputStream objectStream= new DataStoreOutputStream(new BufferedOutputStream(System.out));
					try {
						objectStream.writeObject(tableHash);
					} finally {
						objectStream.flush();
					}
				} else {
					ObjectOutputStream objectStream= new DataStoreOutputStream(new BufferedOutputStream(System.err));
					try {
						objectStream.writeObject(tableHash);
					} finally {
						objectStream.flush();
					}
				}
			}
		} catch (IOException e) {
			throw new FileInputOutputError(toString(),e);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term toFullName() {
		if (!isStandardFile) {
			if (isLocalResource) {
				return new PrologString(filePath.toString());
			} else {
				return new PrologString(fileURI.toASCIIString());
			}
		} else {
			return new PrologString(toString());
		}
	}
	//
	public Term toURL() {
		if (!isStandardFile) {
			URI uri= get_URI_OfResource();
			return new PrologString(uri.toASCIIString());
		} else {
			return new PrologString(toString());
		}
	}
	//
	public Path getPathOfLocalResource() {
		if (!isStandardFile) {
			if (isLocalResource) {
				return filePath;
			} else {
				throw new ThisIsNotALocalResource(fileURI.toString());
			}
		} else {
			throw new StandardStreamIsNotAllowedInThisOperation();
		}
	}
	//
	public URI get_URI_OfResource() {
		if (!isStandardFile) {
			if (isLocalResource) {
				if (endsWithSlash) {
					return filePath.resolve(".").toUri();
				} else {
					// In the case of the default provider, and
					// the file exists, and it can be determined
					// that the file is a directory, then
					// the resulting URI will end with a slash.
					return filePath.toUri();
				}
			} else {
				return fileURI;
			}
		} else {
			throw new StandardStreamIsNotAllowedInThisOperation();
		}
	}
	//
	public URI formBaseDirectoryURI() {
		if (!isStandardFile) {
			if (isLocalResource) {
				return pathToBaseDirectoryURI(filePath,endsWithSlash);
			} else {
				return fileURI;
			}
		} else {
			throw new StandardStreamIsNotAllowedInThisOperation();
		}
	}
	//
	public Path createSubdirectoryPath() {
		Path dataStorePath= getPathOfLocalResource();
		String subdirectoryName= discardFileExtension();
		Path subdirectoryPath= fileSystem.getPath(subdirectoryName);
		subdirectoryPath= subdirectoryPath.toAbsolutePath();
		return subdirectoryPath;
	}
}
