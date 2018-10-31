// (c) 2010-2015 IRE RAS Alexei A. Morozov

package morozov.built_in;

import morozov.run.*;
import morozov.syntax.errors.*;
import morozov.syntax.scanner.errors.*;
import morozov.syntax.*;
import morozov.system.*;
import morozov.system.converters.*;
import morozov.system.errors.*;
import morozov.system.files.*;
import morozov.system.files.errors.*;
import morozov.terms.*;
import morozov.terms.signals.*;
import morozov.worlds.*;

import java.io.BufferedReader;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.BufferedWriter;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.DataInput;
import java.nio.channels.SeekableByteChannel;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.nio.channels.NonReadableChannelException;
import java.nio.charset.Charset;

public abstract class File extends Text {
	//
	protected static final Charset completeCharset= Charset.forName("ISO-8859-1");
	//
	public Boolean randomAccess= null;
	//
	protected ExtendedFileName currentFileName;
	protected FileAccessMode currentFileAccessMode;
	protected boolean currentRandomAccessMode;
	protected CharacterSet currentCharacterSet;
	//
	protected BufferedReader bufferedReader;
	protected DataInputStream inputStream;
	protected BufferedWriter bufferedWriter;
	protected DataOutputStream outputStream;
	protected SeekableByteChannel randomAccessFile;
	//
	protected String recentErrorText;
	protected long recentErrorPosition= -1;
	protected Throwable recentErrorException;
	//
	public File() {
	}
	public File(GlobalWorldIdentifier id) {
		super(id);
	}
	//
	// abstract public Term getBuiltInSlot_E_name();
	// abstract public Term getBuiltInSlot_E_extension();
	// abstract public Term getBuiltInSlot_E_type();
	abstract public Term getBuiltInSlot_E_random_access();
	// abstract public Term getBuiltInSlot_E_character_set();
	// abstract public Term getBuiltInSlot_E_backslash_always_is_separator();
	//
	///////////////////////////////////////////////////////////////
	//
	// get/set random_access
	//
	public void setRandomAccess1s(ChoisePoint iX, Term a1) {
		setRandomAccess(OnOff.termOnOff2Boolean(a1,iX));
	}
	public void setRandomAccess(boolean value) {
		randomAccess= value;
	}
	public void getRandomAccess0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(OnOff.boolean2TermOnOff(getRandomAccess(iX)));
	}
	public void getRandomAccess0fs(ChoisePoint iX) {
	}
	public boolean getRandomAccess(ChoisePoint iX) {
		if (randomAccess != null) {
			return randomAccess;
		} else {
			Term value= getBuiltInSlot_E_random_access();
			return OnOff.termOnOff2Boolean(value,iX);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void releaseSystemResources() {
		closeFile();
		super.releaseSystemResources();
	}
	//
	public void clear0s(ChoisePoint iX) {
		ExtendedFileName fileName= retrieveRealLocalFileName(iX);
		fileName.clear();
	}
	//
	public void setString1s(ChoisePoint iX, Term inputText) {
		ExtendedFileName fileName= retrieveRealLocalFileName(iX);
		String text= GeneralConverters.argumentToString(inputText,iX);
		CharacterSet requiredCharacterSet= getCharacterSet(iX);
		try {
			fileName.create_BAK_File();
			fileName.writeTextFile(text,requiredCharacterSet);
		} catch (IOException e) {
			throw new FileInputOutputError(fileName.toString(),e);
		}
	}
	public void setString2s(ChoisePoint iX, Term a1, Term inputText) {
		ExtendedFileName fileName= retrieveRealLocalFileName(a1,iX);
		String text= GeneralConverters.argumentToString(inputText,iX);
		CharacterSet requiredCharacterSet= getCharacterSet(iX);
		try {
			fileName.create_BAK_File();
			fileName.writeTextFile(text,requiredCharacterSet);
		} catch (IOException e) {
			throw new FileInputOutputError(fileName.toString(),e);
		}
	}
	//
	public void getString0ff(ChoisePoint iX, PrologVariable result) {
		ExtendedFileName fileName= retrieveRealLocalFileName(iX);
		CharacterSet requiredCharacterSet= getCharacterSet(iX);
		result.setNonBacktrackableValue(fileName.getString(requiredCharacterSet));
	}
	public void getString0fs(ChoisePoint iX) {
	}
	public void getString1ff(ChoisePoint iX, PrologVariable result, Term a1) {
		ExtendedFileName fileName= retrieveRealLocalFileName(a1,iX);
		CharacterSet requiredCharacterSet= getCharacterSet(iX);
		result.setNonBacktrackableValue(fileName.getString(requiredCharacterSet));
	}
	public void getString1fs(ChoisePoint iX, Term a1) {
	}
	//
	public void open0s(ChoisePoint iX) {
		ExtendedFileName fileName= retrieveRealLocalFileName(iX);
		boolean requiredRandomAccessMode= getRandomAccess(iX);
		CharacterSet requiredCharacterSet= getCharacterSet(iX);
		openFile(fileName,FileAccessMode.MODIFYING,requiredRandomAccessMode,requiredCharacterSet);
	}
	public void open1s(ChoisePoint iX, Term mode) {
		ExtendedFileName fileName= retrieveRealLocalFileName(iX);
		FileAccessMode requiredFileAccessMode= FileAccessMode.argumentToFileAccessMode(mode,iX);
		boolean requiredRandomAccessMode= getRandomAccess(iX);
		CharacterSet requiredCharacterSet= getCharacterSet(iX);
		openFile(fileName,requiredFileAccessMode,requiredRandomAccessMode,requiredCharacterSet);
	}
	public void open2s(ChoisePoint iX, Term a1, Term mode) {
		ExtendedFileName fileName= retrieveRealLocalFileName(a1,iX);
		FileAccessMode requiredMode= FileAccessMode.argumentToFileAccessMode(mode,iX);
		boolean requiredRandomAccessMode= getRandomAccess(iX);
		CharacterSet requiredCharacterSet= getCharacterSet(iX);
		openFile(fileName,requiredMode,requiredRandomAccessMode,requiredCharacterSet);
	}
	//
	protected void openFile(ExtendedFileName fileName, FileAccessMode requiredFileAccessMode, boolean requiredRandomAccessMode, CharacterSet requiredCharacterSet) {
		if (currentFileAccessMode != null) {
			closeFile();
		};
		// prologFile.setCurrentFileAccessMode(requiredFileAccessMode);
		currentFileAccessMode= requiredFileAccessMode;
		// prologFile.setCurrentRandomAccessMode(requiredRandomAccessMode);
		currentRandomAccessMode= requiredRandomAccessMode;
		// prologFile.setCurrentCharacterSet(requiredCharacterSet);
		currentCharacterSet= requiredCharacterSet;
		try {
			if (!fileName.isStandardFile()) {
				Path path= fileName.getPathOfLocalResource();
				if (requiredFileAccessMode==FileAccessMode.READING) {
					if (!requiredRandomAccessMode) {
						if (requiredCharacterSet.isDummy()) {
							InputStream newStream= Files.newInputStream(path);
							inputStream= new DataInputStream(new BufferedInputStream(newStream));
						} else {
							bufferedReader= Files.newBufferedReader(path,requiredCharacterSet.toCharSet());
						}
					} else { // requiredRandomAccessMode
						if (requiredCharacterSet.isDummyOrDefault()) {
							// prologFile.randomAccessFile= new RandomAccessFile(textName,"r");
							randomAccessFile= Files.newByteChannel(
								path,
								StandardOpenOption.READ);
						} else {
							throw new RandomAccessRequiresTheNoneCharacterSet();
						}
					}
				} else if (requiredFileAccessMode==FileAccessMode.WRITING) {
					if (!requiredRandomAccessMode) {
						SimpleFileName.createDirectories(path,true);
						if (requiredCharacterSet.isDummy()) {
							OutputStream newStream= Files.newOutputStream(
								path,
								StandardOpenOption.CREATE,
								StandardOpenOption.TRUNCATE_EXISTING,
								StandardOpenOption.WRITE);
							outputStream= new DataOutputStream(new BufferedOutputStream(newStream));
						} else {
							bufferedWriter= Files.newBufferedWriter(
								path,
								requiredCharacterSet.toCharSet(),
								StandardOpenOption.CREATE,
								StandardOpenOption.TRUNCATE_EXISTING,
								StandardOpenOption.WRITE);
						}
					} else { // requiredRandomAccessMode
						if (requiredCharacterSet.isDummyOrDefault()) {
							SimpleFileName.createDirectories(path,true);
							// randomAccessFile= new RandomAccessFile(textName,"rw");
							randomAccessFile= Files.newByteChannel(
								path,
								StandardOpenOption.CREATE,
								StandardOpenOption.TRUNCATE_EXISTING,
								StandardOpenOption.WRITE);
							// randomAccessFile.setLength(0);
							// prologFile.randomAccessFile.truncate(0);
						} else {
							throw new RandomAccessRequiresTheNoneCharacterSet();
						}
					}
				} else if (requiredFileAccessMode==FileAccessMode.APPENDING) {
					if (!requiredRandomAccessMode) {
						SimpleFileName.createDirectories(path,true);
						if (requiredCharacterSet.isDummy()) {
							OutputStream newStream= Files.newOutputStream(
								path,
								StandardOpenOption.CREATE,
								StandardOpenOption.APPEND,
								StandardOpenOption.WRITE);
							// outputStream= new DataOutputStream(new BufferedOutputStream(new FileOutputStream(textName,true)));
							outputStream= new DataOutputStream(new BufferedOutputStream(newStream));
						} else {
							bufferedWriter= Files.newBufferedWriter(
								path,
								requiredCharacterSet.toCharSet(),
								StandardOpenOption.CREATE,
								StandardOpenOption.APPEND,
								StandardOpenOption.WRITE);
						}
					} else { // requiredRandomAccessMode
						if (requiredCharacterSet.isDummyOrDefault()) {
							SimpleFileName.createDirectories(path,true);
							// randomAccessFile= new RandomAccessFile(textName,"rw");
							randomAccessFile= Files.newByteChannel(
								path,
								StandardOpenOption.CREATE,
								StandardOpenOption.APPEND,
								StandardOpenOption.WRITE);
							// randomAccessFile.seek(randomAccessFile.length());
							// prologFile.randomAccessFile.position(prologFile.randomAccessFile.size());
						} else {
							throw new RandomAccessRequiresTheNoneCharacterSet();
						}
					}
				} else { // requiredFileAccessMode==FileAccessMode.MODIFYING
					if (!requiredRandomAccessMode) {
						SimpleFileName.createDirectories(path,true);
						if (requiredCharacterSet.isDummy()) {
							OutputStream newStream= Files.newOutputStream(
								path,
								StandardOpenOption.CREATE,
								// StandardOpenOption.APPEND,
								StandardOpenOption.WRITE);
							outputStream= new DataOutputStream(new BufferedOutputStream(newStream));
						} else {
							bufferedWriter= Files.newBufferedWriter(
								path,
								requiredCharacterSet.toCharSet(),
								StandardOpenOption.CREATE,
								// StandardOpenOption.APPEND,
								StandardOpenOption.WRITE);
						}
					} else { // requiredRandomAccessMode
						if (requiredCharacterSet.isDummyOrDefault()) {
							SimpleFileName.createDirectories(path,true);
							// randomAccessFile= new RandomAccessFile(textName,"rw");
							randomAccessFile= Files.newByteChannel(
								path,
								StandardOpenOption.CREATE,
								// StandardOpenOption.APPEND,
								StandardOpenOption.WRITE,
								StandardOpenOption.READ);
							// randomAccessFile.seek(randomAccessFile.length());
							// prologFile.randomAccessFile.position(prologFile.randomAccessFile.size());
						} else {
							throw new RandomAccessRequiresTheNoneCharacterSet();
						}
					}
				}
			} else {
				StandardFileName systemName= fileName.getSystemName();
				if (!requiredRandomAccessMode) {
					if (requiredFileAccessMode==FileAccessMode.READING) {
						if (systemName==StandardFileName.STDOUT) {
							throw new StandardOutputStreamDoesNotSupportThisOperation();
						} else if (systemName==StandardFileName.STDERR) {
							throw new StandardErrorOutputStreamDoesNotSupportThisOperation();
						} else {
							if (requiredCharacterSet.isDummy()) {
								inputStream= new DataInputStream(new BufferedInputStream(System.in));
							} else {
								bufferedReader= new BufferedReader(new InputStreamReader(System.in,requiredCharacterSet.toCharSet()));
							}
						}
					} else {
						if (systemName==StandardFileName.STDIN) {
							throw new StandardInputStreamDoesNotSupportThisOperation();
						} else if (systemName==StandardFileName.STDOUT) {
							if (requiredCharacterSet.isDummy()) {
								outputStream= new DataOutputStream(new BufferedOutputStream(System.out));
							} else {
								bufferedWriter= new BufferedWriter(new OutputStreamWriter(System.out,requiredCharacterSet.toCharSet()));
							}
						} else {
							if (requiredCharacterSet.isDummy()) {
								outputStream= new DataOutputStream(new BufferedOutputStream(System.err));
							} else {
								bufferedWriter= new BufferedWriter(new OutputStreamWriter(System.err,requiredCharacterSet.toCharSet()));
							}
						}
					}
				} else { // requiredRandomAccessMode
					throw new StandardStreamsDoNotSupportRandomAccess();
				}
			}
		} catch (FileNotFoundException e) {
			throw new FileIsNotFound(toString());
		} catch (java.io.IOException e) {
			throw new FileInputOutputError(toString(),e);
		// } catch (Throwable e) {
		//	e.printStackTrace();
		//	throw e;
		};
		currentFileName= fileName;
	}
	//
	public void close0s(ChoisePoint iX) {
		closeFile();
	}
	//
	protected void closeFile() {
		if (currentFileAccessMode != null) {
			try {
				if (!currentFileName.isStandardFile()) {
					if (!currentRandomAccessMode) {
						if (currentFileAccessMode==FileAccessMode.READING) {
							if (currentCharacterSet.isDummy()) {
								if (inputStream != null) {
									inputStream.close();
									inputStream= null;
								}
							} else {
								if (bufferedReader != null) {
									bufferedReader.close();
									bufferedReader= null;
								}
							}
						} else {
							if (currentCharacterSet.isDummy()) {
								if (outputStream != null) {
									outputStream.close();
									outputStream= null;
								}
							} else {
								if (bufferedWriter != null) {
									bufferedWriter.close();
									bufferedWriter= null;
								}
							}
						}
					} else {
						if (randomAccessFile != null) {
							randomAccessFile.close();
							randomAccessFile= null;
						}
					};
					currentFileAccessMode= null;
					currentRandomAccessMode= false;
				} else {
					flush(false);
				}
			} catch (IOException e) {
// e.printStackTrace();
				throw new FileInputOutputError(currentFileName.toString(),e);
			}
		}
	}
	//
	public void write1ms(ChoisePoint iX, Term... args) {
		StringBuilder textBuffer= FormatOutput.termsToString(iX,(Term[])args);
		write_string_buffer(textBuffer);
	}
	//
	public void writeLn1ms(ChoisePoint iX, Term... args) {
		StringBuilder textBuffer= FormatOutput.termsToString(iX,(Term[])args);
		textBuffer.append("\n");
		write_string_buffer(textBuffer);
	}
	//
	public void writeF2ms(ChoisePoint iX, Term... args) {
		StringBuilder textBuffer= FormatOutput.termsToFormattedString(iX,(Term[])args);
		write_string_buffer(textBuffer);
	}
	//
	public void newLine0s(ChoisePoint iX) {
		write_string_buffer(new StringBuilder("\n"));
	}
	//
	public void write_string_buffer(StringBuilder textBuffer) {
		if (currentFileAccessMode != null) {
			try {
				if (!currentFileName.isStandardFile()) {
					if (!currentRandomAccessMode) {
						if (currentFileAccessMode==FileAccessMode.READING) {
							throw new FileIsNotInWritingMode();
						} else {
							if (currentCharacterSet.isDummy()) {
								outputStream.writeBytes(textBuffer.toString());
							} else {
								bufferedWriter.write(textBuffer.toString(),0,textBuffer.length());
							}
						}
					} else {
						// randomAccessFile.writeBytes(textBuffer.toString());
						randomAccessFile.write(ByteBuffer.wrap(textBuffer.toString().getBytes(completeCharset)));
					}
				} else {
					StandardFileName systemName= currentFileName.getSystemName();
					if (systemName==StandardFileName.STDIN) {
						throw new StandardInputStreamDoesNotSupportThisOperation();
					} else {
						if (currentCharacterSet.isDummy()) {
							outputStream.writeBytes(textBuffer.toString());
						} else {
							bufferedWriter.write(textBuffer.toString(),0,textBuffer.length());
						}
					}
				}
			} catch (IOException e) {
				throw new FileInputOutputError(currentFileName.toString(),e);
			}
		} else {
			throw new FileIsNotOpen();
		}
	}
	//
	public void readByte0ff(ChoisePoint iX, PrologVariable result) throws Backtracking {
		result.setNonBacktrackableValue(new PrologInteger(readByte()));
	}
	public void readByte0fs(ChoisePoint iX) throws Backtracking {
		readByte();
	}
	protected int readByte() throws Backtracking {
		if (currentFileAccessMode != null) {
			try {
				if (!currentFileName.isStandardFile()) {
					if (!currentRandomAccessMode) {
						if (currentFileAccessMode==FileAccessMode.READING) {
							int value;
							if (currentCharacterSet.isDummy()) {
								value= inputStream.read();
							} else {
								value= bufferedReader.read();
							};
							if (value >= 0) {
								return (value & 0xFF);
							} else {
								throw Backtracking.instance;
							}
						} else {
							throw new FileIsNotInReadingMode();
						}
					} else {
						int value= readByteFromSeekableByteChannel(randomAccessFile);
						if (value >= 0) {
							return value;
						} else {
							throw Backtracking.instance;
						}
					}
				} else {
					StandardFileName systemName= currentFileName.getSystemName();
					if (systemName==StandardFileName.STDIN) {
						int value;
						if (currentCharacterSet.isDummy()) {
							value= inputStream.read();
						} else {
							value= bufferedReader.read();
						};
						if (value >= 0) {
							return (value & 0xFF);
						} else {
							throw Backtracking.instance;
						}
					} else if (systemName==StandardFileName.STDOUT) {
						throw new StandardOutputStreamDoesNotSupportThisOperation();
					} else {
						throw new StandardErrorOutputStreamDoesNotSupportThisOperation();
					}
				}
			} catch (IOException e) {
				throw new FileInputOutputError(currentFileName.toString(),e);
			}
		} else {
			throw new FileIsNotOpen();
		}
	}
	//
	public void readCharacter0ff(ChoisePoint iX, PrologVariable result) throws Backtracking {
		result.setNonBacktrackableValue(new PrologInteger(readCharacter()));
	}
	public void readCharacter0fs(ChoisePoint iX) throws Backtracking {
		readCharacter();
	}
	protected int readCharacter() throws Backtracking {
		if (currentFileAccessMode != null) {
			try {
				if (!currentFileName.isStandardFile()) {
					if (!currentRandomAccessMode) {
						if (currentFileAccessMode==FileAccessMode.READING) {
							int value;
							if (currentCharacterSet.isDummy()) {
								value= inputStream.read();
							} else {
								value= bufferedReader.read();
							};
							if (value >= 0) {
								return value;
							} else {
								throw Backtracking.instance;
							}
						} else {
							throw new FileIsNotInReadingMode();
						}
					} else {
						ByteBuffer buffer= ByteBuffer.allocate(2);
						try {
							randomAccessFile.read(buffer);
							buffer.rewind();
							return buffer.getChar();
						} catch (Throwable e) {
							// e.printStackTrace();
							throw Backtracking.instance;
						}
					}
				} else {
					StandardFileName systemName= currentFileName.getSystemName();
					if (systemName==StandardFileName.STDIN) {
						int value;
						if (currentCharacterSet.isDummy()) {
							value= inputStream.read();
						} else {
							value= bufferedReader.read();
						};
						if (value >= 0) {
							return value;
						} else {
							throw Backtracking.instance;
						}
					} else if (systemName==StandardFileName.STDOUT) {
						throw new StandardOutputStreamDoesNotSupportThisOperation();
					} else {
						throw new StandardErrorOutputStreamDoesNotSupportThisOperation();
					}
				}
			} catch (IOException e) {
				throw new FileInputOutputError(currentFileName.toString(),e);
			}
		} else {
			throw new FileIsNotOpen();
		}
	}
	//
	public void readLine0ff(ChoisePoint iX, PrologVariable result) throws Backtracking {
		result.setNonBacktrackableValue(new PrologString(readLine()));
	}
	public void readLine0fs(ChoisePoint iX) throws Backtracking {
		readLine();
	}
	protected String readLine() throws Backtracking {
		if (currentFileAccessMode != null) {
			try {
				if (!currentFileName.isStandardFile()) {
					if (!currentRandomAccessMode) {
						if (currentFileAccessMode==FileAccessMode.READING) {
							String value;
							if (currentCharacterSet.isDummy()) {
								// Deprecated. This method does not properly convert bytes
								// to characters. As of JDK 1.1, the preferred way to read
								// lines of text is via the BufferedReader.readLine() method.
								// Programs that use the DataInputStream class to read lines
								// can be converted to use the BufferedReader class.
								value= ((DataInput)inputStream).readLine();
							} else {
								value= bufferedReader.readLine();
							};
							if (value != null) {
								return value;
							} else {
								throw Backtracking.instance;
							}
						} else {
							throw new FileIsNotInReadingMode();
						}
					} else {
						String value= readLineFromSeekableByteChannel(randomAccessFile);
						if (value != null) {
							return value;
						} else {
							throw Backtracking.instance;
						}
					}
				} else {
					StandardFileName systemName= currentFileName.getSystemName();
					if (systemName==StandardFileName.STDIN) {
						// String value= stdin.readLine();
						String value;
						if (currentCharacterSet.isDummy()) {
							// Deprecated. This method does not properly convert bytes
							// to characters. As of JDK 1.1, the preferred way to read
							// lines of text is via the BufferedReader.readLine() method.
							// Programs that use the DataInputStream class to read lines
							// can be converted to use the BufferedReader class.
							value= ((DataInput)inputStream).readLine();
						} else {
							value= bufferedReader.readLine();
						};
						if (value != null) {
							return value;
						} else {
							throw Backtracking.instance;
						}
					} else if (systemName==StandardFileName.STDOUT) {
						throw new StandardOutputStreamDoesNotSupportThisOperation();
					} else {
						throw new StandardErrorOutputStreamDoesNotSupportThisOperation();
					}
				}
			} catch (IOException e) {
				throw new FileInputOutputError(currentFileName.toString(),e);
			}
		} else {
			throw new FileIsNotOpen();
		}
	}
	//
	protected int readByteFromSeekableByteChannel(SeekableByteChannel channel) throws IOException {
		ByteBuffer buffer= ByteBuffer.allocate(1);
		try {
			if (channel.read(buffer) > 0) {
				buffer.rewind();
				int result= buffer.get() & 0x00FF;
				return result;
			} else {
				return -1;
			}
		} catch (NonReadableChannelException e) {
			throw e;
		} catch (Throwable e) {
//e.printStackTrace();
			return -1;
		}
	}
	protected String readLineFromSeekableByteChannel(SeekableByteChannel channel) throws IOException {
		StringBuffer input= new StringBuffer();
		int c= -1;
		boolean eol= false;
		while (!eol) {
			c= readByteFromSeekableByteChannel(channel);
			switch (c) {
			case -1:
			case '\n':
				eol= true;
				break;
			case '\r':
				eol= true;
				long cp= channel.position();
				if ((readByteFromSeekableByteChannel(channel)) != '\n') {
					channel.position(cp);
				};
				break;
			default:
				input.append((char)c);
				break;
			}
		};
		if ((c == -1) && (input.length() == 0)) {
			return null;
		};
		return input.toString();
        }
	//
	public void readTerm0ff(ChoisePoint iX, PrologVariable result) throws Backtracking {
		result.setNonBacktrackableValue(readTerm());
	}
	public void readTerm0fs(ChoisePoint iX) throws Backtracking {
		readTerm();
	}
	protected Term readTerm() throws Backtracking {
		recentErrorText= "";
		recentErrorPosition= -1;
		recentErrorException= null;
		try {
			String text= readLine();
			if (text==null) {
				throw Backtracking.instance;
			};
			Parser parser= new Parser(true);
			try {
				Term[] terms= parser.stringToTerms(text);
				if (terms.length==1) {
					return terms[0];
				} else {
					throw Backtracking.instance;
				}
			} catch (LexicalScannerError e) {
				long errorPosition= e.getPosition();
				recentErrorText= text;
				recentErrorPosition= errorPosition;
				recentErrorException= e;
				throw Backtracking.instance;
			} catch (ParserError e) {
				long errorPosition= e.getPosition();
				recentErrorText= text;
				recentErrorPosition= errorPosition;
				recentErrorException= e;
				throw Backtracking.instance;
			}
		} catch (RuntimeException e) {
			if (recentErrorException==null) {
				recentErrorException= e;
			};
			throw e;
		}
	}
	//
	public void readTerms0ff(ChoisePoint iX, PrologVariable result) throws Backtracking {
		result.setNonBacktrackableValue(readTerms());
	}
	public void readTerms0fs(ChoisePoint iX) throws Backtracking {
		readTerms();
	}
	protected Term readTerms() throws Backtracking {
		recentErrorText= "";
		recentErrorPosition= -1;
		recentErrorException= null;
		try {
			String text= readLine();
			if (text==null) {
				throw Backtracking.instance;
			};
			Parser parser= new Parser(true);
			try {
				Term[] terms= parser.stringToTerms(text);
				return GeneralConverters.arrayToList(terms);
			} catch (LexicalScannerError e) {
				long errorPosition= e.getPosition();
				recentErrorText= text;
				recentErrorPosition= errorPosition;
				recentErrorException= e;
				throw Backtracking.instance;
			} catch (ParserError e) {
				long errorPosition= e.getPosition();
				recentErrorText= text;
				recentErrorPosition= errorPosition;
				recentErrorException= e;
				throw Backtracking.instance;
			}
		} catch (RuntimeException e) {
			if (recentErrorException==null) {
				recentErrorException= e;
			};
			throw e;
		}
	}
	//
	public void recentReadingError4s(ChoisePoint iX, PrologVariable a1, PrologVariable a2, PrologVariable a3, PrologVariable a4) throws Backtracking {
		if (recentErrorException != null && recentErrorText != null) {
			a1.setBacktrackableValue(new PrologString(recentErrorText),iX);
			a2.setBacktrackableValue(new PrologInteger(recentErrorPosition),iX);
			a3.setBacktrackableValue(new PrologString(recentErrorException.toString()),iX);
			a4.setBacktrackableValue(new PrologString(recentErrorException.toString()),iX);
		} else {
			throw Backtracking.instance;
		}
	}
	public void recentReadingError3s(ChoisePoint iX, PrologVariable a1, PrologVariable a2, PrologVariable a3) throws Backtracking {
		if (recentErrorException != null && recentErrorText != null) {
			a1.setBacktrackableValue(new PrologString(recentErrorText),iX);
			a2.setBacktrackableValue(new PrologInteger(recentErrorPosition),iX);
			a3.setBacktrackableValue(new PrologString(recentErrorException.toString()),iX);
		} else {
			throw Backtracking.instance;
		}
	}
	//
	public void getPosition0ff(ChoisePoint iX, PrologVariable result) {
		if (currentFileAccessMode != null) {
			try {
				if (!currentFileName.isStandardFile()) {
					if (currentRandomAccessMode) {
						result.setNonBacktrackableValue(new PrologInteger(randomAccessFile.position()));
					} else {
						throw new FileIsNotInRandomAccessMode();
					}
				} else {
					throw new StandardStreamsDoNotSupportRandomAccess();
				}
			} catch (IOException e) {
				throw new FileInputOutputError(currentFileName.toString(),e);
			}
		} else {
			throw new FileIsNotOpen();
		}
	}
	public void getPosition0fs(ChoisePoint iX) {
	}
	//
	public void setPosition2s(ChoisePoint iX, Term p, Term positioningMode) {
		try {
			long position= p.getLongIntegerValue(iX);
			PositioningMode pM= PositioningMode.argumentToPositioningMode(positioningMode,iX);
			setPosition(position,pM);
		} catch (TermIsNotAnInteger e) {
			throw new WrongArgumentIsNotAnInteger(p);
		}
	}
	public void setPosition1s(ChoisePoint iX, Term p) {
		try {
			long position= p.getLongIntegerValue(iX);
			setPosition(position,PositioningMode.START);
		} catch (TermIsNotAnInteger e) {
			throw new WrongArgumentIsNotAnInteger(p);
		}
	}
	protected void setPosition(long position, PositioningMode pM) {
		if (currentFileAccessMode != null) {
			try {
				if (!currentFileName.isStandardFile()) {
					if (currentRandomAccessMode) {
						if (pM==PositioningMode.RELATIVE) {
							// long currentPosition= randomAccessFile.getFilePointer();
							long currentPosition= randomAccessFile.position();
							// randomAccessFile.seek(currentPosition+position);
							randomAccessFile.position(currentPosition+position);
							// randomAccessFile.seek(position);
						} else if (pM==PositioningMode.END) {
							// long length= randomAccessFile.length();
							long length= randomAccessFile.size();
							// randomAccessFile.seek(length-position-1);
							// randomAccessFile.seek(length+position);
							randomAccessFile.position(length+position);
						} else {
							// randomAccessFile.seek(position);
							randomAccessFile.position(position);
						}
					} else {
						throw new FileIsNotInRandomAccessMode();
					}
				} else {
					throw new StandardStreamsDoNotSupportRandomAccess();
				}
			} catch (IOException e) {
				throw new FileInputOutputError(currentFileName.toString(),e);
			}
		} else {
			throw new FileIsNotOpen();
		}
	}
	//
	public void eof0s(ChoisePoint iX) throws Backtracking {
		if (currentFileAccessMode != null) {
			try {
				if (!currentFileName.isStandardFile()) {
					if (!currentRandomAccessMode) {
						if (currentFileAccessMode==FileAccessMode.READING) {
							if (currentCharacterSet.isDummy()) {
								endOfInputStream(inputStream);
							} else {
								endOfBufferedReader(bufferedReader);
							}
						} else {
							throw new SequentialAccessFilesSupportThisOperationOnlyInReadingMode();
						}
					} else { // currentRandomAccessMode
						endOfRandomAccessFile();
					}
				} else {
					if (currentFileAccessMode==FileAccessMode.READING) {
						StandardFileName systemName= currentFileName.getSystemName();
						if (systemName==StandardFileName.STDIN) {
							throw Backtracking.instance;
						}
					} else {
						throw new SequentialAccessFilesSupportThisOperationOnlyInReadingMode();
					}
				}
			} catch (IOException e) {
				throw new FileInputOutputError(currentFileName.toString(),e);
			}
		} else {
			throw new FileIsNotOpen();
		}
	}
	protected void endOfInputStream(DataInputStream stream) throws Backtracking, IOException {
		if (stream.markSupported()) {
			stream.mark(10);
			try {
				int c= stream.read();
				if (c != -1) {
					throw Backtracking.instance;
				}
			} finally {
				stream.reset();
			}
		} else {
			throw Backtracking.instance;
		}
	}
	protected void endOfBufferedReader(BufferedReader reader) throws Backtracking, IOException {
		if (reader.markSupported()) {
			reader.mark(10);
			try {
				int c= reader.read();
				if (c != -1) {
					throw Backtracking.instance;
				}
			} finally {
				reader.reset();
			}
		} else {
			throw Backtracking.instance;
		}
	}
	protected void endOfRandomAccessFile() throws Backtracking, IOException {
		// long position= randomAccessFile.getFilePointer();
		long position= randomAccessFile.position();
		// long length= randomAccessFile.length();
		long length= randomAccessFile.size();
		if (length >= position + 1) {
			throw Backtracking.instance;
		}
	}
	//
	public void flush0s(ChoisePoint iX) {
		flush(true);
	}
	//
	protected void flush(boolean reportIfFileIsNotOpen) {
		if (currentFileAccessMode != null) {
			try {
				if (!currentFileName.isStandardFile()) {
					if (!currentRandomAccessMode) {
						if (currentFileAccessMode==FileAccessMode.WRITING) {
							if (currentCharacterSet.isDummy()) {
								outputStream.flush();
							} else {
								bufferedWriter.flush();
							}
						} else if (currentFileAccessMode==FileAccessMode.APPENDING) {
							if (currentCharacterSet.isDummy()) {
								outputStream.flush();
							} else {
								bufferedWriter.flush();
							}
						}
					}
				} else {
					StandardFileName systemName= currentFileName.getSystemName();
					if (systemName==StandardFileName.STDOUT) {
						// stdout.flush();
						if (currentCharacterSet.isDummy()) {
							outputStream.flush();
						} else {
							bufferedWriter.flush();
						}
					} else if (systemName==StandardFileName.STDERR) {
						// stderr.flush();
						if (currentCharacterSet.isDummy()) {
							outputStream.flush();
						} else {
							bufferedWriter.flush();
						}
					}
				}
			} catch (java.io.IOException e) {
				throw new FileInputOutputError(currentFileName.toString(),e);
			}
		} else if (reportIfFileIsNotOpen) {
			throw new FileIsNotOpen();
		}
	}
	//
	public void truncate0s(ChoisePoint iX) {
		if (currentFileAccessMode != null) {
			try {
				if (currentRandomAccessMode) {
					if (!currentFileName.isStandardFile()) {
						if (currentFileAccessMode==FileAccessMode.READING) {
							throw new FileIsNotInWritingMode();
						} else {
							randomAccessFile.truncate(randomAccessFile.position());
						}
					} else {
						StandardFileName systemName= currentFileName.getSystemName();
						if (systemName==StandardFileName.STDIN) {
							throw new FileIsNotInWritingMode();
						}
					}
				} else {
					throw new SequentialAccessFilesDoNotSupportThisOperation();
				}
			} catch (IOException e) {
				throw new FileInputOutputError(currentFileName.toString(),e);
			}
		} else {
			throw new FileIsNotOpen();
		}
	}
	//
	public void doesExist0s(ChoisePoint iX) throws Backtracking {
		try {
			ExtendedFileName fileName= retrieveRealLocalFileName(iX);
			int timeout= getMaximalWaitingTimeInMilliseconds(iX);
			CharacterSet characterSet= getCharacterSet(iX);
			fileName.isExistentLocalResource(timeout,characterSet,staticContext);
		} catch (Throwable e) {
			throw Backtracking.instance;
		}
	}
	public void doesExist1s(ChoisePoint iX, Term a1) throws Backtracking {
		try {
			ExtendedFileName fileName= retrieveRealLocalFileName(a1,iX);
			int timeout= getMaximalWaitingTimeInMilliseconds(iX);
			CharacterSet characterSet= getCharacterSet(iX);
			fileName.isExistentLocalResource(timeout,characterSet,staticContext);
		} catch (Throwable e) {
			throw Backtracking.instance;
		}
	}
}
