// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system.files;

import target.*;

import morozov.system.*;
import morozov.terms.*;

import java.nio.file.FileSystems;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.DirectoryStream;
import java.nio.file.DirectoryIteratorException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;

import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class FileUtils {
	protected static final FileSystem fileSystem= FileSystems.getDefault();
	//
	public static ExtendedFileName termToExtendedFileName(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			// if (code==SymbolCodes.symbolCode_E_console) {
			//	return new ExtendedFileName(SystemFileName.CONSOLE);
			if (code==SymbolCodes.symbolCode_E_stdin) {
				return new ExtendedFileName(SystemFileName.STDIN);
			} else if (code==SymbolCodes.symbolCode_E_stdout) {
				return new ExtendedFileName(SystemFileName.STDOUT);
			} else if (code==SymbolCodes.symbolCode_E_stderr) {
				return new ExtendedFileName(SystemFileName.STDERR);
			} else {
				throw new WrongTermIsNotFileName(value);
			}
		} catch (TermIsNotASymbol e1) {
			try {
				return new ExtendedFileName(value.getStringValue(iX));
			} catch (TermIsNotAString e2) {
				throw new WrongTermIsNotFileName(value);
			}
		}
	}
	//
	public static String makeRealName(String textName) {
		Path path= resolveFileName(textName);
		return tryToMakeRealName(path).toString();
	}
	public static String makeRealName(Path base, String textName) {
		Path path= base.resolve(textName);
		return tryToMakeRealName(path).toString();
	}
	public static Path tryToMakeRealName(Path path) {
		try {
			return path.toRealPath();
			// return path.toRealPath(LinkOption.NOFOLLOW_LINKS);
		} catch (IOException e1) {
			return path.toAbsolutePath();
		}
	}
	public static String resolveAndEncodeLocalFileNameAddFilePrefix(String textName) {
		try {
			return resolveFileName(textName).toUri().toASCIIString();
		} catch (Throwable e) {
			return textName;
		}
	}
	//
	public static Path resolveFileName(String textName) {
		return fileSystem.getPath(textName).toAbsolutePath();
	}
	//
	public static FileNameMask[] termToFileNameMasks(Term types, ChoisePoint iX) {
		ArrayList<FileNameMask> list= new ArrayList<FileNameMask>();
		Term nextHead= null;
		Term currentTail= types;
		try {
			while (true) {
				nextHead= currentTail.getNextListHead(iX);
				String description= nextHead.getStringValue(iX);
				currentTail= currentTail.getNextListTail(iX);
				nextHead= currentTail.getNextListHead(iX);
				String wildcardText= nextHead.getStringValue(iX);
				String[] wildcards= stringToWildcards(wildcardText);
				currentTail= currentTail.getNextListTail(iX);
				list.add(new FileNameMask(description,wildcards));
			}
		} catch (EndOfList e) {
		} catch (TermIsNotAList e) {
			throw new WrongArgumentIsNotWildcardList(currentTail);
		} catch (TermIsNotAString e) {
			throw new WrongArgumentIsNotAString(nextHead);
		};
		return list.toArray(new FileNameMask[0]);
	}
	protected static String[] stringToWildcards(String wildcardText) {
		ArrayList<String> list= new ArrayList<String>();
		int length= wildcardText.length();
		int currentPosition= 0;
		while (true) {
			if (currentPosition < length) {
				int p1= wildcardText.indexOf(';',currentPosition);
				if (p1 >= 0) {
					String wildcard= wildcardText.substring(currentPosition,p1).trim();
					if (!wildcard.isEmpty()) {
						list.add(wildcard);
					};
					currentPosition= p1 + 1;
					continue;
				} else {
					String wildcard= wildcardText.substring(currentPosition,wildcardText.length()).trim();
					if (!wildcard.isEmpty()) {
						list.add(wildcard);
					};
					break;
				}
			} else {
				break;
			}
		};
		return list.toArray(new String[0]);
	}
	//
	public static PositioningMode termToPositioningMode(Term mode, ChoisePoint iX) {
		try {
			long code= mode.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_start) {
				return PositioningMode.START;
			} else if (code==SymbolCodes.symbolCode_E_relative) {
				return PositioningMode.RELATIVE;
			} else if (code==SymbolCodes.symbolCode_E_end) {
				return PositioningMode.END;
			} else {
				throw new WrongArgumentIsNotPositioningMode(mode);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongArgumentIsNotPositioningMode(mode);
		}
	}
	//
	public static FileAccessMode termToFileAccessMode(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_reading) {
				return FileAccessMode.READING;
			} else if (code==SymbolCodes.symbolCode_E_writing) {
				return FileAccessMode.WRITING;
			} else if (code==SymbolCodes.symbolCode_E_appending) {
				return FileAccessMode.APPENDING;
			} else if (code==SymbolCodes.symbolCode_E_modifying) {
				return FileAccessMode.MODIFYING;
			} else {
				throw new WrongTermIsNotFileAccessMode(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongTermIsNotFileAccessMode(value);
		}
	}
	//
	public static CharacterSet term2CharacterSet(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_none) {
				return new CharacterSet(CharacterSetType.NONE);
			} else if (code==SymbolCodes.symbolCode_E_default) {
				return new CharacterSet(CharacterSetType.DEFAULT);
			} else if (code==SymbolCodes.symbolCode_E_ISO_8859_1) {
				return new CharacterSet(CharacterSetType.ISO_8859_1);
			} else if (code==SymbolCodes.symbolCode_E_US_ASCII) {
				return new CharacterSet(CharacterSetType.US_ASCII);
			} else if (code==SymbolCodes.symbolCode_E_UTF_16) {
				return new CharacterSet(CharacterSetType.UTF_16);
			} else if (code==SymbolCodes.symbolCode_E_UTF_16BE) {
				return new CharacterSet(CharacterSetType.UTF_16BE);
			} else if (code==SymbolCodes.symbolCode_E_UTF_16LE) {
				return new CharacterSet(CharacterSetType.UTF_16LE);
			} else if (code==SymbolCodes.symbolCode_E_UTF_8) {
				return new CharacterSet(CharacterSetType.UTF_8);
			} else {
				throw new WrongTermIsNotCharacterSet(value);
			}
		} catch (TermIsNotASymbol e1) {
			try {
				return new CharacterSet(value.getStringValue(iX));
			} catch (TermIsNotAString e2) {
				throw new WrongTermIsNotCharacterSet(value);
			}
		}
	}
	//
	public static boolean checkIfBackslashIsSeparator(Term value, ChoisePoint iX) {
		boolean backslashIsSeparator= Converters.term2YesNo(value,iX);
		backslashIsSeparator= backslashIsSeparator || java.io.File.separatorChar=='\\';
		return backslashIsSeparator;
	}
	//
	public static List<Path> listSourceFiles(Path dir) {
		return listSourceFiles(dir,null);
	}
	public static List<Path> listSourceFiles(Path dir, String mask) {
		List<Path> result= new ArrayList<Path>();
		try {
			DirectoryStream<Path> stream;
			if (mask==null) {
				// stream= dir.newDirectoryStream();
				stream= Files.newDirectoryStream(dir);
			} else {
				// stream= dir.newDirectoryStream(mask);
				stream= Files.newDirectoryStream(dir,mask);
			};
			for (Path entry: stream) {
				result.add(entry);
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
	public static String getUserDirectory() {
		Properties properties= System.getProperties();
		return properties.getProperty("user.dir");
	}
	//
	public static void createDirectories(String fileName) {
		String path= new java.io.File(fileName).getParent();
		if (path != null) {
			new java.io.File(path).mkdirs();
		}
	}
	//
	public static String deleteQuotationMarks(String name) {
		name= name.trim();
		int length= name.length();
		int beginning= name.codePointAt(0);
		int end= name.codePointAt(length-1);
		if (beginning=='\"' && end=='\"' && length-1 >= 1) {
			name= name.substring(1,length-1);
			name= name.trim();
			return name;
		} else {
			return name;
		}
	}
	//
	public static String read_UTF_or_ASCII_File(String fileName) {
		Path path= fileSystem.getPath(fileName);
		try {
			InputStream stream= Files.newInputStream(path,StandardOpenOption.READ);
			byte[] prefix;
			int k;
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
					byte[] bytes= Files.readAllBytes(path);
					return new String(bytes,StandardCharsets.UTF_16);
				} else {
					return readLocalTextFile(fileName,new CharacterSet(CharacterSetType.DEFAULT));
				}
			} else {
				return readLocalTextFile(fileName,new CharacterSet(CharacterSetType.DEFAULT));
			}
		} catch (FileNotFoundException e) {
			throw new FileInputOutputError(fileName,e);
		} catch (IOException e) {
			throw new FileInputOutputError(fileName,e);
		}
	}
	//
	public static String readLocalTextFile(String fileName) {
		return readLocalTextFile(fileName,new CharacterSet(CharacterSetType.DEFAULT));
	}
	public static String readLocalTextFile(String fileName, CharacterSet characterSet) {
		try {
			Path path= fileSystem.getPath(fileName);
			byte[] bytes= Files.readAllBytes(path);
			if (characterSet.isDummy()) {
				return new String(bytes);
			} else {
				return new String(bytes,characterSet.toCharSet());
			}
		} catch (FileNotFoundException e) {
			throw new FileInputOutputError(fileName,e);
		} catch (IOException e) {
			throw new FileInputOutputError(fileName,e);
		}
	}
	public static String readStdIn(CharacterSet characterSet) {
		if (characterSet.isDummy()) {
			String encoding= System.getProperty("file.encoding");
			return readStdIn(Charset.forName(encoding));
		} else {
			return readStdIn(characterSet.toCharSet());
		}
	}
	public static String readStdIn(Charset characterSet) {
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
			// return "";
			throw new FileInputOutputError("stdin",e);
		}
	}
	//
	public static byte[] readBytesFromLocalFile(String fileName) {
		try {
			Path path= fileSystem.getPath(fileName);
			// byte[] bytes= Files.readAllBytes(path);
			return Files.readAllBytes(path);
		} catch (FileNotFoundException e) {
			throw new FileInputOutputError(fileName,e);
		} catch (IOException e) {
			throw new FileInputOutputError(fileName,e);
		}
	}
	//
	@SuppressWarnings("deprecation")
	public static void writeTextFile(String text, String fileName, CharacterSet characterSet) throws IOException {
		createDirectories(fileName);
		if (characterSet.isDummy()) {
			byte[] dst= new byte[text.length()];
			text.getBytes(0,text.length(),dst,0);
			Path path= fileSystem.getPath(fileName);
			Files.write(path,dst);
		} else {
			byte[] dst= text.getBytes(characterSet.toCharSet());
			FileChannel channel= new FileOutputStream(fileName).getChannel();
			try {
				channel.write(ByteBuffer.wrap(dst));
			} finally {
				channel.close();
			}
		}
	}
	//
	public static void create_BAK_File(String fileName1, boolean backslashIsSeparator) {
		String fileName2= modifyFileExtension(fileName1,".bak",backslashIsSeparator);
		Path path1= fileSystem.getPath(fileName1);
		Path path2= fileSystem.getPath(fileName2);
		try {
			Files.deleteIfExists(path2);
			Files.move(path1,path2,StandardCopyOption.ATOMIC_MOVE);
		} catch (IOException e) {
		};
	}
	//
	public static String modifyFileExtension(String fullName, String newExtension, boolean backslashIsSeparator) {
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
				} else if (backslashIsSeparator) {
					p2= extension.indexOf('\\');
					if (p2 >= 0) {
						name= fullName;
					}
				}
			};
			return name + newExtension;
		} else {
			return fullName + newExtension;
		}
	}
}
