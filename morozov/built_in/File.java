// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.built_in;

import morozov.syntax.scanner.*;
import morozov.syntax.*;
import morozov.system.*;
import morozov.system.checker.*;
import morozov.system.files.*;
import morozov.terms.*;

import java.io.BufferedReader;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.BufferedWriter;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.ByteBuffer;
import java.nio.file.FileSystems;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.DosFileAttributes;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.StandardCopyOption;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.AccessDeniedException;
import java.nio.file.StandardOpenOption;

import java.util.List;

public abstract class File extends Text {
	//
	protected static final FileSystem fileSystem= FileSystems.getDefault();
	//
	protected Path currentDirectory= null;
	//
	protected FileAccessMode currentMode;
	protected boolean randomAccess;
	protected CharacterSet currentCharacterSet;
	//
	protected ExtendedFileName currentFileName;
	protected BufferedReader bufferedReader;
	protected DataInputStream inputStream;
	// protected BufferedOutputStream outputStream;
	protected BufferedWriter bufferedWriter;
	protected DataOutputStream outputStream;
	protected RandomAccessFile randomAccessFile;
	// protected PrintStream standardPrintStream;
	//
	// protected static BufferedReader stdin= new BufferedReader(new InputStreamReader(System.in));
	// protected static DataInputStream stdin= new DataInputStream(new BufferedInputStream(System.in));
	// protected static PrintStream stdout= System.out;
	// protected static PrintStream stderr= System.err;
	//
	protected String recentErrorText;
	protected long recentErrorPosition= -1;
	protected Throwable recentErrorException;
	//
	abstract protected Term getBuiltInSlot_E_name();
	abstract protected Term getBuiltInSlot_E_extension();
	// abstract protected Term getBuiltInSlot_E_type();
	abstract protected Term getBuiltInSlot_E_random_access();
	abstract protected Term getBuiltInSlot_E_character_set();
	abstract protected Term getBuiltInSlot_E_backslash_is_separator_always();
	//
	public void clear0s(ChoisePoint iX) {
		ExtendedFileName fileName= retrieveRealFileName(iX);
		if (!fileName.isSystemFile) {
			java.io.File file= new java.io.File(fileName.textName);
			try {
				if (file.exists()) {
					boolean isReadOnly= false;
					if (!file.canWrite()) {
						isReadOnly= true;
						file.setWritable(true);
					};
					FileChannel channel= new FileOutputStream(fileName.textName).getChannel();
					channel.write(ByteBuffer.wrap(new byte[0]));
					channel.close();
					if (isReadOnly) {
						file.setWritable(false);
					}
				} else {
					FileUtils.createDirectories(fileName.textName);
					file.createNewFile();
				}
			} catch (java.io.IOException e) {
				throw new FileInputOutputError(fileName.toString(),e);
			}
		} else {
			flushFile(false);
		}
	}
	//
	public void setString1s(ChoisePoint iX, Term inputText) {
		ExtendedFileName fileName= retrieveRealFileName(iX);
		setString(iX,fileName,inputText);
	}
	public void setString2s(ChoisePoint iX, Term name, Term inputText) {
		ExtendedFileName fileName= FileUtils.termToExtendedFileName(name,iX);
		setString(iX,fileName,inputText);
	}
	protected void setString(ChoisePoint iX, ExtendedFileName fileName, Term inputText) {
		try {
			String text= inputText.getStringValue(iX);
			CharacterSet requiredCharacterSet= FileUtils.term2CharacterSet(getBuiltInSlot_E_character_set(),iX);
			if (!fileName.isSystemFile) {
				FileUtils.writeTextFile(text,fileName.textName,requiredCharacterSet);
			} else {
				if (fileName.systemName==SystemFileName.STDOUT) {
					System.out.print(text);
				} else if (fileName.systemName==SystemFileName.STDERR) {
					System.err.print(text);
				} else {
					throw new StandardInputStreamDoesNotSupportThisOperation();
				}
			}
		} catch (TermIsNotAString e) {
			throw new WrongArgumentIsNotAString(inputText);
		} catch (IOException e) {
			throw new FileInputOutputError(fileName.toString(),e);
		}
	}
	//
	public void getString1ff(ChoisePoint iX, PrologVariable outputText, Term name) {
		ExtendedFileName fileName= FileUtils.termToExtendedFileName(name,iX);
		getString(iX,outputText,fileName);
	}
	public void getString1fs(ChoisePoint iX, PrologVariable outputText, Term name) {
	}
	public void getString0ff(ChoisePoint iX, PrologVariable outputText) {
		ExtendedFileName fileName= retrieveRealFileName(iX);
		getString(iX,outputText,fileName);
	}
	public void getString0fs(ChoisePoint iX) {
	}
	protected void getString(ChoisePoint iX, PrologVariable outputText, ExtendedFileName fileName) {
		// ExtendedFileName fileName= retrieveRealFileName(iX);
		// try {
		if (!fileName.isSystemFile) {
			CharacterSet requiredCharacterSet= FileUtils.term2CharacterSet(getBuiltInSlot_E_character_set(),iX);
			outputText.value= new PrologString(FileUtils.readLocalTextFile(fileName.textName,requiredCharacterSet));
		} else {
			if (fileName.systemName==SystemFileName.STDIN) {
				// try {
				CharacterSet requiredCharacterSet= FileUtils.term2CharacterSet(getBuiltInSlot_E_character_set(),iX);
				String buffer= FileUtils.readStdIn(requiredCharacterSet);
				// String buffer;
				// if (requiredCharacterSet.isDummy()) {
				//	DataInputStream stdin= new DataInputStream(new BufferedInputStream(System.in));
				//	try {
				//		buffer= stdin.readLine();
				//	} finally {
				//		stdin.close();
				//	}
				// } else {
				//	buffer= new BufferedReader(new InputStreamReader(System.in,requiredCharacterSet.toCharSet())).readLine();
				// };
				if (buffer != null) {
					outputText.value= new PrologString(buffer);
				} else {
					outputText.value= new PrologString("");
				}
				// } catch (IOException e) {
				//	throw new FileInputOutputError(fileName.toString(),e);
				// }
			} else if (fileName.systemName==SystemFileName.STDOUT) {
				throw new StandardOutputStreamDoesNotSupportThisOperation();
			} else {
				throw new StandardErrorOutputStreamDoesNotSupportThisOperation();
			}
		}
	}
	//
	public void open2s(ChoisePoint iX, Term name, Term mode) {
		// String fileName= name.getStringValue(iX);
		ExtendedFileName fileName= FileUtils.termToExtendedFileName(name,iX);
		FileAccessMode requiredMode= FileUtils.termToFileAccessMode(mode,iX);
		boolean requiredRandomAccessMode= Converters.term2OnOff(getBuiltInSlot_E_random_access(),iX);
		CharacterSet requiredCharacterSet= FileUtils.term2CharacterSet(getBuiltInSlot_E_character_set(),iX);
		openFile(fileName,requiredMode,requiredRandomAccessMode,requiredCharacterSet);
	}
	public void open1s(ChoisePoint iX, Term mode) {
		ExtendedFileName fileName= retrieveRealFileName(iX);
		FileAccessMode requiredMode= FileUtils.termToFileAccessMode(mode,iX);
		boolean requiredRandomAccessMode= Converters.term2OnOff(getBuiltInSlot_E_random_access(),iX);
		CharacterSet requiredCharacterSet= FileUtils.term2CharacterSet(getBuiltInSlot_E_character_set(),iX);
		openFile(fileName,requiredMode,requiredRandomAccessMode,requiredCharacterSet);
	}
	public void open0s(ChoisePoint iX) {
		ExtendedFileName fileName= retrieveRealFileName(iX);
		boolean requiredRandomAccessMode= Converters.term2OnOff(getBuiltInSlot_E_random_access(),iX);
		CharacterSet requiredCharacterSet= FileUtils.term2CharacterSet(getBuiltInSlot_E_character_set(),iX);
		openFile(fileName,FileAccessMode.MODIFYING,requiredRandomAccessMode,requiredCharacterSet);
	}
	protected void openFile(ExtendedFileName fileName, FileAccessMode requiredMode, boolean requiredRandomAccessMode, CharacterSet requiredCharacterSet) {
		if (!(currentMode==null)) {
			closeFile();
		};
		currentMode= requiredMode;
		randomAccess= requiredRandomAccessMode;
		currentCharacterSet= requiredCharacterSet;
		try {
			if (!fileName.isSystemFile) {
				String textName= fileName.textName;
				if (currentMode==FileAccessMode.READING) {
					if (!randomAccess) {
						if (requiredCharacterSet.isDummy()) {
							inputStream= new DataInputStream(new BufferedInputStream(new FileInputStream(textName)));
						} else {
							// bufferedReader= new BufferedReader(new InputStreamReader(new FileInputStream(textName),requiredCharacterSet.toCharSet()));
							Path path= fileSystem.getPath(textName);
							bufferedReader= Files.newBufferedReader(path,requiredCharacterSet.toCharSet());
						}
					} else {
						if (requiredCharacterSet.isDummy()) {
							randomAccessFile= new RandomAccessFile(textName,"r");
							// Path path= fileSystem.getPath(textName);
							// SeekableByteChannel channel= Files.newByteChannel(path,EnumSet.of(StandardOpenOption.READ,StandardOpenOption.WRITE));
						} else {
							throw new RandomAccessRequiresTheNoneCharacterSet();
						}
					}
				} else if (currentMode==FileAccessMode.WRITING) {
					if (!randomAccess) {
						FileUtils.createDirectories(textName);
						if (requiredCharacterSet.isDummy()) {
							outputStream= new DataOutputStream(new BufferedOutputStream(new FileOutputStream(textName)));
						} else {
							// bufferedWriter= new BufferedWriter(new OutputStreamWriter(new FileOutputStream(textName),requiredCharacterSet.toCharSet()));
							Path path= fileSystem.getPath(textName);
							bufferedWriter= Files.newBufferedWriter(path,requiredCharacterSet.toCharSet());
						}
					} else {
						if (requiredCharacterSet.isDummy()) {
							FileUtils.createDirectories(textName);
							randomAccessFile= new RandomAccessFile(textName,"rw");
							randomAccessFile.setLength(0);
						} else {
							throw new RandomAccessRequiresTheNoneCharacterSet();
						}
					}
				} else if (currentMode==FileAccessMode.APPENDING) {
					if (!randomAccess) {
						FileUtils.createDirectories(textName);
						if (requiredCharacterSet.isDummy()) {
							outputStream= new DataOutputStream(new BufferedOutputStream(new FileOutputStream(textName,true)));
						} else {
							// bufferedWriter= new BufferedWriter(new OutputStreamWriter(new FileOutputStream(textName,true),requiredCharacterSet.toCharSet()));
							Path path= fileSystem.getPath(textName);
							bufferedWriter= Files.newBufferedWriter(
								path,
								requiredCharacterSet.toCharSet(),
								StandardOpenOption.CREATE,
								StandardOpenOption.WRITE,
								StandardOpenOption.APPEND);
						}
					} else {
						if (requiredCharacterSet.isDummy()) {
							FileUtils.createDirectories(textName);
							randomAccessFile= new RandomAccessFile(textName,"rw");
							randomAccessFile.seek(randomAccessFile.length());
						} else {
							throw new RandomAccessRequiresTheNoneCharacterSet();
						}
					}
				} else {
					if (requiredCharacterSet.isDummy()) {
						FileUtils.createDirectories(textName);
						randomAccessFile= new RandomAccessFile(textName,"rw");
					} else {
						throw new RandomAccessRequiresTheNoneCharacterSet();
					}
				}
			} else {
				if (!randomAccess) {
					if (currentMode==FileAccessMode.READING) {
						if (fileName.systemName==SystemFileName.STDOUT) {
							throw new StandardOutputStreamDoesNotSupportThisOperation();
						} else if (fileName.systemName==SystemFileName.STDERR) {
							throw new StandardErrorOutputStreamDoesNotSupportThisOperation();
						} else {
							if (requiredCharacterSet.isDummy()) {
								inputStream= new DataInputStream(new BufferedInputStream(System.in));
							} else {
								bufferedReader= new BufferedReader(new InputStreamReader(System.in,requiredCharacterSet.toCharSet()));
								// Path path= fileSystem.getPath(textName);
								// bufferedReader= Files.newBufferedReader(path,requiredCharacterSet.toCharSet());
							}
						}
					} else if (currentMode==FileAccessMode.WRITING) {
						if (fileName.systemName==SystemFileName.STDIN) {
							throw new StandardInputStreamDoesNotSupportThisOperation();
						} else if (fileName.systemName==SystemFileName.STDOUT) {
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
					} else if (currentMode==FileAccessMode.APPENDING) {
						if (fileName.systemName==SystemFileName.STDIN) {
							throw new StandardInputStreamDoesNotSupportThisOperation();
						} else if (fileName.systemName==SystemFileName.STDOUT) {
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
					} else {
						throw new StandardStreamsDoNotSupportReadWriteAccess();
					}
				} else {
					throw new StandardStreamsDoNotSupportRandomAccess();
				}
			}
		} catch (FileNotFoundException e) {
			throw new FileIsNotFound();
		} catch (java.io.IOException e) {
			throw new FileInputOutputError(fileName.toString(),e);
		};
		currentFileName= fileName;
	}
	//
	public void close0s(ChoisePoint iX) {
		closeFile();
	}
	protected void closeFile() {
		if (currentMode != null) {
			try {
				// if (!currentFileName.isSystemFile) {
					if (!randomAccess) {
						if (currentMode==FileAccessMode.READING) {
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
						} else if (currentMode==FileAccessMode.MODIFYING) {
							if (randomAccessFile != null) {
								randomAccessFile.close();
								randomAccessFile= null;
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
					}
				// };
				currentMode= null;
				randomAccess= false;
			} catch (java.io.IOException e) {
				throw new InputOutputError(e);
			}
		// } else {
		//	throw new FileIsNotOpen();
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
		// for (int n=0; n < textBuffer.length(); n++) {
		//	int c= textBuffer.charAt(n);
		//	System.out.printf("%d) %d\n",n,c);
		// };
		// String s= textBuffer.toString();
		// for (int n=0; n < s.length(); n++) {
		//	int c= s.charAt(n);
		//	System.out.printf("STRING: %d) %d\n",n,c);
		// };
		// byte[] b= s.getBytes(StandardCharsets.ISO_8859_1);
		// for (int n=0; n < b.length; n++) {
		//	int c= b[n];
		//	System.out.printf("BYTES: %d) %d\n",n,c);
		// };
		// char[] ca= s.toCharArray();
		// for (int n=0; n < ca.length; n++) {
		//	int c= ca[n];
		//	System.out.printf("CHAR: %d) %d\n",n,c);
		// };
		write_string_buffer(textBuffer);
	}
	//
	public void newLine0s(ChoisePoint iX) {
		write_string_buffer(new StringBuilder("\n"));
	}
	//
	public void write_string_buffer(StringBuilder textBuffer) {
		if (currentMode != null) {
			try {
				if (!currentFileName.isSystemFile) {
					if (!randomAccess) {
						if (currentMode==FileAccessMode.READING) {
							throw new FileIsNotInWritingMode();
						} else if (currentMode==FileAccessMode.MODIFYING) {
							randomAccessFile.writeBytes(textBuffer.toString());
						} else {
							if (currentCharacterSet.isDummy()) {
								outputStream.writeBytes(textBuffer.toString());
							} else {
								bufferedWriter.write(textBuffer.toString(),0,textBuffer.length());
							}
						}
					} else {
						randomAccessFile.writeBytes(textBuffer.toString());
					}
				} else {
					if (currentFileName.systemName==SystemFileName.STDIN) {
						throw new StandardInputStreamDoesNotSupportThisOperation();
					} else {
						// if (currentFileName.systemName==SystemFileName.STDOUT) {
						// stdout.print(textBuffer.toString());
						if (currentCharacterSet.isDummy()) {
							outputStream.writeBytes(textBuffer.toString());
						} else {
							bufferedWriter.write(textBuffer.toString(),0,textBuffer.length());
						}
					// } else if (currentFileName.systemName==SystemFileName.STDERR) {
					//	stderr.print(textBuffer.toString());
					}
				}
			} catch (java.io.IOException e) {
				throw new InputOutputError(e);
			}
		} else {
			throw new FileIsNotOpen();
		}
	}
	//
	public void readByte0ff(ChoisePoint iX, PrologVariable c) throws Backtracking {
		c.value= new PrologInteger(readByte());
	}
	public void readByte0fs(ChoisePoint iX) throws Backtracking {
		readByte();
	}
	protected int readByte() throws Backtracking {
		int c= readCharacter();
		return (c & 0xFF);
	}
	//
	public void readCharacter0ff(ChoisePoint iX, PrologVariable c) throws Backtracking {
		c.value= new PrologInteger(readCharacter());
	}
	public void readCharacter0fs(ChoisePoint iX) throws Backtracking {
		readCharacter();
	}
	protected int readCharacter() throws Backtracking {
		if (currentMode != null) {
			try {
				if (!currentFileName.isSystemFile) {
					if (!randomAccess) {
						if (currentMode==FileAccessMode.READING) {
							int value;
							if (currentCharacterSet.isDummy()) {
								value= inputStream.read();
							} else {
								value= bufferedReader.read();
							};
							if (value >= 0) {
								return value;
							} else {
								throw new Backtracking();
							}
						} else if (currentMode==FileAccessMode.MODIFYING) {
							int value= randomAccessFile.read();
							if (value >= 0) {
								return value;
							} else {
								throw new Backtracking();
							}
						} else {
							throw new FileIsNotInReadingMode();
						}
					} else {
						int value= randomAccessFile.read();
						if (value >= 0) {
							return value;
						} else {
							throw new Backtracking();
						}
					}
				} else {
					if (currentFileName.systemName==SystemFileName.STDIN) {
						// int value= stdin.read();
						int value;
						if (currentCharacterSet.isDummy()) {
							value= inputStream.read();
						} else {
							value= bufferedReader.read();
						};
						if (value >= 0) {
							return value;
						} else {
							throw new Backtracking();
						}
					} else if (currentFileName.systemName==SystemFileName.STDOUT) {
						throw new StandardOutputStreamDoesNotSupportThisOperation();
					} else {
						throw new StandardErrorOutputStreamDoesNotSupportThisOperation();
					}
				}
			} catch (java.io.IOException e) {
				throw new InputOutputError(e);
			}
		} else {
			throw new FileIsNotOpen();
		}
	}
	//
	public void readLine0ff(ChoisePoint iX, PrologVariable s) throws Backtracking {
		s.value= new PrologString(readLine());
	}
	public void readLine0fs(ChoisePoint iX) throws Backtracking {
		readLine();
	}
	@SuppressWarnings("deprecation")
	protected String readLine() throws Backtracking {
		if (currentMode != null) {
			try {
				if (!currentFileName.isSystemFile) {
					if (!randomAccess) {
						if (currentMode==FileAccessMode.READING) {
							String value;
							if (currentCharacterSet.isDummy()) {
								value= inputStream.readLine();
							} else {
								value= bufferedReader.readLine();
							};
							if (value != null) {
								return value;
							} else {
								throw new Backtracking();
							}
						} else if (currentMode==FileAccessMode.MODIFYING) {
							String value= randomAccessFile.readLine();
							if (value != null) {
								return value;
							} else {
								throw new Backtracking();
							}
						} else {
							throw new FileIsNotInReadingMode();
						}
					} else {
						String value= randomAccessFile.readLine();
						if (value != null) {
							return value;
						} else {
							throw new Backtracking();
						}
					}
				} else {
					if (currentFileName.systemName==SystemFileName.STDIN) {
						// String value= stdin.readLine();
						String value;
						if (currentCharacterSet.isDummy()) {
							value= inputStream.readLine();
						} else {
							value= bufferedReader.readLine();
						};
						if (value != null) {
							return value;
						} else {
							throw new Backtracking();
						}
					} else if (currentFileName.systemName==SystemFileName.STDOUT) {
						throw new StandardOutputStreamDoesNotSupportThisOperation();
					} else {
						throw new StandardErrorOutputStreamDoesNotSupportThisOperation();
					}
				}
			} catch (java.io.IOException e) {
				throw new InputOutputError(e);
			}
		} else {
			throw new FileIsNotOpen();
		}
	}
	//
	public void readTerm0ff(ChoisePoint iX, PrologVariable t) throws Backtracking {
		t.value= readTerm();
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
				throw new Backtracking();
			};
			Parser parser= new Parser(true);
			try {
				Term[] terms= parser.stringToTerms(text);
				if (terms.length==1) {
					return terms[0];
				} else {
					throw new Backtracking();
				}
			} catch (LexicalScannerError e) {
				long errorPosition= e.getPosition();
				recentErrorText= text;
				recentErrorPosition= errorPosition;
				recentErrorException= e;
				throw new Backtracking();
			} catch (ParserError e) {
				long errorPosition= e.getPosition();
				recentErrorText= text;
				recentErrorPosition= errorPosition;
				recentErrorException= e;
				throw new Backtracking();
			}
		} catch (RuntimeException e) {
			if (recentErrorException==null) {
				recentErrorException= e;
			};
			throw e;
		}
	}
	//
	public void readTerms0ff(ChoisePoint iX, PrologVariable t) throws Backtracking {
		t.value= readTerms();
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
				throw new Backtracking();
			};
			Parser parser= new Parser(true);
			try {
				Term[] terms= parser.stringToTerms(text);
				return Converters.arrayToList(terms);
			} catch (LexicalScannerError e) {
				long errorPosition= e.getPosition();
				recentErrorText= text;
				recentErrorPosition= errorPosition;
				recentErrorException= e;
				throw new Backtracking();
			} catch (ParserError e) {
				long errorPosition= e.getPosition();
				recentErrorText= text;
				recentErrorPosition= errorPosition;
				recentErrorException= e;
				throw new Backtracking();
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
			a1.value= new PrologString(recentErrorText);
			a2.value= new PrologInteger(recentErrorPosition);
			a3.value= new PrologString(recentErrorException.toString());
			a4.value= new PrologString(recentErrorException.toString());
			iX.pushTrail(a1);
			iX.pushTrail(a2);
			iX.pushTrail(a3);
			iX.pushTrail(a4);
		} else {
			throw new Backtracking();
		}
	}
	public void recentReadingError3s(ChoisePoint iX, PrologVariable a1, PrologVariable a2, PrologVariable a3) throws Backtracking {
		if (recentErrorException != null && recentErrorText != null) {
			a1.value= new PrologString(recentErrorText);
			a2.value= new PrologInteger(recentErrorPosition);
			a3.value= new PrologString(recentErrorException.toString());
			iX.pushTrail(a1);
			iX.pushTrail(a2);
			iX.pushTrail(a3);
		} else {
			throw new Backtracking();
		}
	}
	//
	public void getPosition0ff(ChoisePoint iX, PrologVariable p) {
		if (currentMode != null) {
			try {
				if (!currentFileName.isSystemFile) {
					if (randomAccess) {
						p.value= new PrologInteger(randomAccessFile.getFilePointer());
						// iX.pushTrail(p);
					} else {
						throw new FileIsNotInRandomAccessMode();
					}
				} else {
					throw new StandardStreamsDoNotSupportRandomAccess();
				}
			} catch (java.io.IOException e) {
				throw new InputOutputError(e);
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
			PositioningMode pM= FileUtils.termToPositioningMode(positioningMode,iX);
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
		if (currentMode != null) {
			try {
				if (!currentFileName.isSystemFile) {
					if (randomAccess) {
						if (pM==PositioningMode.RELATIVE) {
							long currentPosition= randomAccessFile.getFilePointer();
							randomAccessFile.seek(currentPosition+position);
							// randomAccessFile.seek(position);
						} else if (pM==PositioningMode.END) {
							long length= randomAccessFile.length();
							// randomAccessFile.seek(length-position-1);
							randomAccessFile.seek(length+position);
						} else {
							randomAccessFile.seek(position);
						}
					} else {
						throw new FileIsNotInRandomAccessMode();
					}
				} else {
					throw new StandardStreamsDoNotSupportRandomAccess();
				}
			} catch (java.io.IOException e) {
				throw new InputOutputError(e);
			}
		} else {
			throw new FileIsNotOpen();
		}
	}

	//
	public void eof0s(ChoisePoint iX) throws Backtracking {
		if (currentMode != null) {
			try {
				if (!currentFileName.isSystemFile) {
					if (!randomAccess) {
						if (currentMode==FileAccessMode.READING) {
							if (currentCharacterSet.isDummy()) {
								endOfInputStream(inputStream);
							} else {
								endOfBufferedReader(bufferedReader);
							}
							// throw new Backtracking();
						} else if (currentMode==FileAccessMode.WRITING) {
							// throw new Backtracking();
						} else if (currentMode==FileAccessMode.APPENDING) {
							// throw new Backtracking();
						} else if (currentMode==FileAccessMode.MODIFYING) {
							endOfRandomAccessFile();
						}
					} else {
						endOfRandomAccessFile();
					}
				} else {
					if (currentFileName.systemName==SystemFileName.STDIN) {
						// endOfInputStream(stdin);
						if (currentCharacterSet.isDummy()) {
							endOfInputStream(inputStream);
						} else {
							endOfBufferedReader(bufferedReader);
						}
					// } else {
					//	throw new Backtracking();
					}
					// throw new Backtracking();
				}
			} catch (java.io.IOException e) {
				throw new InputOutputError(e);
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
					throw new Backtracking();
				}
			} finally {
				stream.reset();
			}
		} else {
			throw new Backtracking();
		}
	}
	protected void endOfBufferedReader(BufferedReader reader) throws Backtracking, IOException {
		if (reader.markSupported()) {
			reader.mark(10);
			try {
				int c= reader.read();
				if (c != -1) {
					throw new Backtracking();
				}
			} finally {
				reader.reset();
			}
		} else {
			throw new Backtracking();
		}
	}
	protected void endOfRandomAccessFile() throws Backtracking, IOException {
		long position= randomAccessFile.getFilePointer();
		long length= randomAccessFile.length();
		if (length >= position + 1) {
			throw new Backtracking();
		}
	}
	//
	public void flush0s(ChoisePoint iX) {
		flushFile(true);
	}
	protected void flushFile(boolean reportIfFileIsNotOpen) {
		if (currentMode != null) {
			try {
				if (!currentFileName.isSystemFile) {
					if (!randomAccess) {
						if (currentMode==FileAccessMode.WRITING) {
							if (currentCharacterSet.isDummy()) {
								outputStream.flush();
							} else {
								bufferedWriter.flush();
							}
						} else if (currentMode==FileAccessMode.APPENDING) {
							if (currentCharacterSet.isDummy()) {
								outputStream.flush();
							} else {
								bufferedWriter.flush();
							}
						}
					}
				} else {
					if (currentFileName.systemName==SystemFileName.STDOUT) {
						// stdout.flush();
						if (currentCharacterSet.isDummy()) {
							outputStream.flush();
						} else {
							bufferedWriter.flush();
						}
					} else if (currentFileName.systemName==SystemFileName.STDERR) {
						// stderr.flush();
						if (currentCharacterSet.isDummy()) {
							outputStream.flush();
						} else {
							bufferedWriter.flush();
						}
					}
				}
			} catch (java.io.IOException e) {
				throw new InputOutputError(e);
			}
		} else if (reportIfFileIsNotOpen) {
			throw new FileIsNotOpen();
		}
	}
	//
	public void truncate0s(ChoisePoint iX) {
		if (currentMode != null) {
			try {
				if (!currentFileName.isSystemFile) {
					if (currentMode==FileAccessMode.READING) {
						throw new FileIsNotInWritingMode();
					} else {
						if (!randomAccess) {
							if (currentMode==FileAccessMode.MODIFYING) {
								randomAccessFile.setLength(randomAccessFile.getFilePointer());
							}
						} else {
							randomAccessFile.setLength(randomAccessFile.getFilePointer());
						}
					}
				} else {
					if (currentFileName.systemName==SystemFileName.STDIN) {
						throw new FileIsNotInWritingMode();
					}
				}
			} catch (java.io.IOException e) {
				throw new InputOutputError(e);
			}
		} else {
			throw new FileIsNotOpen();
		}
	}
	//
	public void doesExist1s(ChoisePoint iX, Term name) throws Backtracking {
		try {
			ExtendedFileName fileName= retrieveRealFileName(name,false,iX);
			doesExist(fileName);
		} catch (Throwable e) {
			throw new Backtracking();
		}
	}
	public void doesExist0s(ChoisePoint iX) throws Backtracking {
		try {
			ExtendedFileName fileName= retrieveRealFileName(iX);
			doesExist(fileName);
		} catch (Throwable e) {
			throw new Backtracking();
		}
	}
	protected void doesExist(ExtendedFileName fileName) throws Backtracking {
		if (!fileName.isSystemFile) {
			Path path= fileSystem.getPath(fileName.textName);
			// if (!path.exists()) {
			if (!Files.exists(path)) {
				throw new Backtracking();
			}
			// java.io.File file= new java.io.File(fileName.textName);
			// if (!file.exists()) {
			//	throw new Backtracking();
			// }
		}
	}
	//
	public void isDirectory1s(ChoisePoint iX, Term name) throws Backtracking {
		ExtendedFileName fileName= retrieveRealFileName(name,false,iX);
		isDirectory(fileName);
	}
	public void isDirectory0s(ChoisePoint iX) throws Backtracking {
		ExtendedFileName fileName= retrieveRealFileName(false,iX);
		isDirectory(fileName);
	}
	protected void isDirectory(ExtendedFileName fileName) throws Backtracking {
		if (!fileName.isSystemFile) {
			Path path= fileSystem.getPath(fileName.textName);
			try {
				// BasicFileAttributes bfa= Attributes.readBasicFileAttributes(path);
				BasicFileAttributes bfa= Files.readAttributes(path,BasicFileAttributes.class);
				if (!bfa.isDirectory()) {
					throw new Backtracking();
				}
			} catch (IOException e) {
				throw new Backtracking();
			}
		} else {
			throw new Backtracking();
		}
	}
	//
	public void isNormal1s(ChoisePoint iX, Term name) throws Backtracking {
		ExtendedFileName fileName= retrieveRealFileName(name,false,iX);
		isNormal(fileName);
	}
	public void isNormal0s(ChoisePoint iX) throws Backtracking {
		ExtendedFileName fileName= retrieveRealFileName(iX);
		isNormal(fileName);
	}
	protected void isNormal(ExtendedFileName fileName) throws Backtracking {
		if (!fileName.isSystemFile) {
			// Path path= fileSystem.getPath(fileName.textName);
			java.io.File file= new java.io.File(fileName.textName);
			try {
				// if (	!path.exists() ||
				//	path.isHidden()) {
				if (	!file.exists() ||
					file.isHidden() ||
					!file.canRead() ||
					!file.canWrite() ||
					!file.canExecute() ||
					file.isDirectory() ) {
					throw new Backtracking();
				} else {
					// path.checkAccess(AccessMode.READ);
					// path.checkAccess(AccessMode.WRITE);
					// path.checkAccess(AccessMode.EXECUTE);
					// BasicFileAttributes bfa= Attributes.readBasicFileAttributes(path);
					// if (	bfa.isDirectory() ||
					BasicFileAttributes bfa= Files.readAttributes(file.toPath(),BasicFileAttributes.class);
					if (	!bfa.isRegularFile()) {
						throw new Backtracking();
					}
				}
			} catch (UnsupportedOperationException e) {
				throw new Backtracking();
			} catch (NoSuchFileException e) {
				throw new Backtracking();
			} catch (AccessDeniedException e) {
				throw new Backtracking();
			} catch (IOException e) {
				throw new Backtracking();
			} catch (SecurityException e) {
				throw new Backtracking();
			}
		}
	}
	//
	public void isArchive1s(ChoisePoint iX, Term name) throws Backtracking {
		ExtendedFileName fileName= retrieveRealFileName(name,false,iX);
		isArchive(fileName);
	}
	public void isArchive0s(ChoisePoint iX) throws Backtracking {
		ExtendedFileName fileName= retrieveRealFileName(iX);
		isArchive(fileName);
	}
	protected void isArchive(ExtendedFileName fileName) throws Backtracking {
		if (!fileName.isSystemFile) {
			Path path= fileSystem.getPath(fileName.textName);
			try {
				// DosFileAttributes da= Attributes.readDosFileAttributes(path);
				DosFileAttributes da= Files.readAttributes(path,DosFileAttributes.class);
				if (!da.isArchive()) {
					throw new Backtracking();
				}
			} catch (UnsupportedOperationException e) {
				throw new Backtracking();
			} catch (IOException e) {
				throw new Backtracking();
			}
		} else {
			throw new Backtracking();
		}
	}
	//
	public void isHidden1s(ChoisePoint iX, Term name) throws Backtracking {
		ExtendedFileName fileName= retrieveRealFileName(name,false,iX);
		isHidden(fileName);
	}
	public void isHidden0s(ChoisePoint iX) throws Backtracking {
		ExtendedFileName fileName= retrieveRealFileName(iX);
		isHidden(fileName);
	}
	protected void isHidden(ExtendedFileName fileName) throws Backtracking {
		if (!fileName.isSystemFile) {
			// Path path= fileSystem.getPath(fileName.textName);
			java.io.File file= new java.io.File(fileName.textName);
			// try {
				if (!file.isHidden()) {
					throw new Backtracking();
				}
			// } catch (IOException e) {
			//	throw new Backtracking();
			// }
		} else {
			throw new Backtracking();
		}
	}
	//
	public void isReadOnly1s(ChoisePoint iX, Term name) throws Backtracking {
		ExtendedFileName fileName= retrieveRealFileName(name,false,iX);
		isReadOnly(fileName);
	}
	public void isReadOnly0s(ChoisePoint iX) throws Backtracking {
		ExtendedFileName fileName= retrieveRealFileName(iX);
		isReadOnly(fileName);
	}
	protected void isReadOnly(ExtendedFileName fileName) throws Backtracking {
		if (!fileName.isSystemFile) {
			java.io.File file= new java.io.File(fileName.textName);
			if (!file.exists() || file.canWrite()) {
				throw new Backtracking();
			}
		//	Path path= fileSystem.getPath(fileName.textName);
		//	try {
		//		if (path.notExists()) {
		//			throw new Backtracking();
		//		} else {
		//			path.checkAccess(AccessMode.WRITE);
		//		}
		//	} catch (UnsupportedOperationException e) {
		//		throw new Backtracking();
		//	} catch (NoSuchFileException e) {
		//		throw new Backtracking();
		//	} catch (AccessDeniedException e) {
		//		throw new Backtracking();
		//	} catch (IOException e) {
		//		throw new Backtracking();
		//	}
		} else {
			if (fileName.systemName != SystemFileName.STDIN) {
				throw new Backtracking();
			}
		}
	}
	//
	public void isSystem1s(ChoisePoint iX, Term name) throws Backtracking {
		ExtendedFileName fileName= retrieveRealFileName(name,false,iX);
		isSystem(fileName);
	}
	public void isSystem0s(ChoisePoint iX) throws Backtracking {
		ExtendedFileName fileName= retrieveRealFileName(iX);
		isSystem(fileName);
	}
	protected void isSystem(ExtendedFileName fileName) throws Backtracking {
		if (!fileName.isSystemFile) {
			Path path= fileSystem.getPath(fileName.textName);
			try {
				// DosFileAttributes da= Attributes.readDosFileAttributes(path);
				DosFileAttributes da= Files.readAttributes(path,DosFileAttributes.class);
				if (!da.isSystem()) {
					throw new Backtracking();
				}
			} catch (UnsupportedOperationException e) {
				throw new Backtracking();
			} catch (IOException e) {
				throw new Backtracking();
			}
		} else {
			throw new Backtracking();
		}
	}
	//
	public void makeDirectory1s(ChoisePoint iX, Term name) {
		ExtendedFileName fileName= retrieveRealFileName(name,false,iX);
		makeDirectory(fileName);
	}
	public void makeDirectory0s(ChoisePoint iX) {
		ExtendedFileName fileName= retrieveRealFileName(false,iX);
		makeDirectory(fileName);
	}
	protected void makeDirectory(ExtendedFileName fileName) {
		String text= fileName.textName;
		if (!fileName.isSystemFile) {
			new java.io.File(text).mkdirs();
		} else {
			throw new StandardStreamIsNotAllowedInThisOperation();
		}
	}
	//
	public void setArchive2s(ChoisePoint iX, Term name, Term mode) {
		ExtendedFileName fileName= retrieveRealFileName(name,false,iX);
		boolean flag= Converters.term2OnOff(mode,iX);
		setArchive(fileName,flag);
	}
	public void setArchive1s(ChoisePoint iX, Term mode) {
		ExtendedFileName fileName= retrieveRealFileName(iX);
		boolean flag= Converters.term2OnOff(mode,iX);
		setArchive(fileName,flag);
	}
	protected void setArchive(ExtendedFileName fileName, boolean flag) {
		if (!fileName.isSystemFile) {
			Path path= fileSystem.getPath(fileName.textName);
			try {
				// if (path.exists()) {
				if (Files.exists(path)) {
					// path.setAttribute("dos:archive",flag);
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
	public void setHidden2s(ChoisePoint iX, Term name, Term mode) {
		ExtendedFileName fileName= retrieveRealFileName(name,false,iX);
		boolean flag= Converters.term2OnOff(mode,iX);
		setHidden(fileName,flag);
	}
	public void setHidden1s(ChoisePoint iX, Term mode) {
		ExtendedFileName fileName= retrieveRealFileName(iX);
		boolean flag= Converters.term2OnOff(mode,iX);
		setHidden(fileName,flag);
	}
	protected void setHidden(ExtendedFileName fileName, boolean flag) {
		if (!fileName.isSystemFile) {
			Path path= fileSystem.getPath(fileName.textName);
			try {
				// if (path.exists()) {
				if (Files.exists(path)) {
					// path.setAttribute("dos:hidden",flag);
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
	public void setReadOnly2s(ChoisePoint iX, Term name, Term mode) {
		ExtendedFileName fileName= retrieveRealFileName(name,false,iX);
		boolean flag= Converters.term2OnOff(mode,iX);
		setReadOnly(fileName,flag);
	}
	public void setReadOnly1s(ChoisePoint iX, Term mode) {
		ExtendedFileName fileName= retrieveRealFileName(iX);
		boolean flag= Converters.term2OnOff(mode,iX);
		setReadOnly(fileName,flag);
	}
	protected void setReadOnly(ExtendedFileName fileName, boolean flag) {
		if (!fileName.isSystemFile) {
			java.io.File file= new java.io.File(fileName.textName);
			if (file.exists()) {
				if (flag) {
					file.setReadOnly();
				} else {
					file.setWritable(true);
				}
			}
			// Path path= fileSystem.getPath(fileName.textName);
			// try {
			//	if (path.exists()) {
			//		path.setAttribute("dos:readonly",flag);
			//	}
			// } catch (UnsupportedOperationException e) {
			// // } catch (IllegalArgumentException e) {
			// // } catch (ClassCastException e) {
			// } catch (IOException e) {
			// }
		} else {
			if (fileName.systemName==SystemFileName.STDIN) {
				if (!flag) {
					throw new StandardInputStreamDoesNotSupportThisOperation();
				}
			} else if (fileName.systemName==SystemFileName.STDOUT) {
				if (flag) {
					throw new StandardOutputStreamDoesNotSupportThisOperation();
				}
			} else if (fileName.systemName==SystemFileName.STDERR) {
				if (flag) {
					throw new StandardErrorOutputStreamDoesNotSupportThisOperation();
				}
			} else {
				throw new StandardStreamsDoNotSupportReadWriteAccess();
			}
		}
	}
	//
	public void setSystem2s(ChoisePoint iX, Term name, Term mode) {
		ExtendedFileName fileName= retrieveRealFileName(name,false,iX);
		boolean flag= Converters.term2OnOff(mode,iX);
		setSystem(fileName,flag);
	}
	public void setSystem1s(ChoisePoint iX, Term mode) {
		ExtendedFileName fileName= retrieveRealFileName(iX);
		boolean flag= Converters.term2OnOff(mode,iX);
		setSystem(fileName,flag);
	}
	protected void setSystem(ExtendedFileName fileName, boolean flag) {
		if (!fileName.isSystemFile) {
			Path path= fileSystem.getPath(fileName.textName);
			try {
				// if (path.exists()) {
				if (Files.exists(path)) {
					// path.setAttribute("dos:system",flag);
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
	public void rename2s(ChoisePoint iX, Term source, Term destination) {
		ExtendedFileName fileName1= retrieveRealFileName(source,false,iX);
		ExtendedFileName fileName2= retrieveRealFileName(destination,false,iX);
		renameFile(fileName1,fileName2);
	}
	public void rename1s(ChoisePoint iX, Term destination) {
		ExtendedFileName fileName2= retrieveRealFileName(destination,false,iX);
		ExtendedFileName fileName1= retrieveRealFileName(iX);
		renameFile(fileName1,fileName2);
	}
	protected void renameFile(ExtendedFileName fileName1, ExtendedFileName fileName2) {
		if (!fileName1.isSystemFile && !fileName2.isSystemFile) {
			Path path= fileSystem.getPath(fileName1.textName);
			try {
				// path.moveTo(fileSystem.getPath(fileName2.textName),StandardCopyOption.ATOMIC_MOVE);
				Files.move(path,fileSystem.getPath(fileName2.textName),StandardCopyOption.ATOMIC_MOVE);
			// } catch (UnsupportedOperationException e) {
			// } catch (FileAlreadyExistsException e) {
			// } catch (AtomicMoveNotSupportedException e) {
			} catch (IOException e) {
				throw new FileInputOutputError(fileName1.toString(),fileName2.toString(),e);
			// } catch (SecurityException e) {
			}
		} else {
			throw new StandardStreamIsNotAllowedInThisOperation();
		}
	}
	//
	public void copy2s(ChoisePoint iX, Term source, Term destination) {
		ExtendedFileName fileName1= retrieveRealFileName(source,false,iX);
		ExtendedFileName fileName2= retrieveRealFileName(destination,false,iX);
		copyFile(fileName1,fileName2);
	}
	public void copy1s(ChoisePoint iX, Term destination) {
		ExtendedFileName fileName2= retrieveRealFileName(destination,false,iX);
		ExtendedFileName fileName1= retrieveRealFileName(iX);
		copyFile(fileName1,fileName2);
	}
	protected void copyFile(ExtendedFileName fileName1, ExtendedFileName fileName2) {
		if (!fileName1.isSystemFile && !fileName2.isSystemFile) {
			Path path= fileSystem.getPath(fileName1.textName);
			try {
				// path.copyTo(fileSystem.getPath(fileName2.textName),StandardCopyOption.COPY_ATTRIBUTES);
				Files.copy(path,fileSystem.getPath(fileName2.textName),StandardCopyOption.COPY_ATTRIBUTES);
			// } catch (UnsupportedOperationException e) {
			// } catch (FileAlreadyExistsException e) {
			} catch (IOException e) {
				throw new FileInputOutputError(fileName1.toString(),fileName2.toString(),e);
			// } catch (SecurityException e) {
			}
		} else {
			throw new StandardStreamIsNotAllowedInThisOperation();
		}
	}
	//
	public void delete1s(ChoisePoint iX, Term name) {
		ExtendedFileName fileName= retrieveRealFileName(name,false,iX);
		deleteFile(fileName);
	}
	public void delete0s(ChoisePoint iX) {
		ExtendedFileName fileName= retrieveRealFileName(iX);
		deleteFile(fileName);
	}
	protected void deleteFile(ExtendedFileName fileName) {
		if (!fileName.isSystemFile) {
			Path path= fileSystem.getPath(fileName.textName);
			try {
				// path.deleteIfExists();
				Files.deleteIfExists(path);
			} catch (DirectoryNotEmptyException e) {
			} catch (IOException e) {
			}
		} else {
			throw new StandardStreamIsNotAllowedInThisOperation();
		}
	}
	//
	public void setCurrentDirectory1s(ChoisePoint iX, Term name) {
		ExtendedFileName fileName= retrieveRealFileName(name,false,iX);
		if (!fileName.isSystemFile) {
			Path path= fileSystem.getPath(fileName.textName);
			// if (path.exists()) {
			if (Files.exists(path)) {
				currentDirectory= path;
			} else {
				throw new DirectoryDoesNotExist();
			}
		} else {
			throw new StandardStreamIsNotAllowedInThisOperation();
		}
	}
	//
	public void getCurrentDirectory0ff(ChoisePoint iX, PrologVariable a1) {
		String name;
		if (currentDirectory==null) {
			name= FileUtils.getUserDirectory();
		} else {
			name= currentDirectory.toString();
		};
		a1.value= new PrologString(name);
	}
	public void getCurrentDirectory0fs(ChoisePoint iX) {
	}
	//
	public void listDirectory1ff(ChoisePoint iX, PrologVariable a1, Term a2) {
		try {
			String mask= a2.getStringValue(iX);
			a1.value= listDirectory(mask);
		} catch (TermIsNotAString e) {
			throw new WrongArgumentIsNotAString(a2);
		}
	}
	public void listDirectory1fs(ChoisePoint iX, Term a1) {
	}
	public void listDirectory0ff(ChoisePoint iX, PrologVariable a1) {
		a1.value= listDirectory(null);
	}
	public void listDirectory0fs(ChoisePoint iX) {
	}
	protected Term listDirectory(String mask) {
		List<Path> list= FileUtils.listSourceFiles(currentDirectory,mask);
		Term result= new PrologEmptyList();
		for (int n=list.size()-1; n >= 0; n--) {
			result= new PrologList(new PrologString(list.get(n).toString()),result);
		};
		return result;
	}
	//
	public void getFullName1ff(ChoisePoint iX, PrologVariable a1, Term name) {
		ExtendedFileName fileName= retrieveRealFileName(name,false,iX);
		a1.value= getFullName(fileName);
	}
	public void getFullName1fs(ChoisePoint iX, Term name) {
	}
	public void getFullName0ff(ChoisePoint iX, PrologVariable a1) {
		ExtendedFileName fileName= retrieveRealFileName(iX);
		a1.value= getFullName(fileName);
	}
	public void getFullName0fs(ChoisePoint iX) {
	}
	protected Term getFullName(ExtendedFileName fileName) {
		return fileName.toTerm();
	}
	//
	public void getURL1ff(ChoisePoint iX, PrologVariable a1, Term name) {
		ExtendedFileName fileName= retrieveRealFileName(name,false,iX);
		boolean backslashIsSeparator= FileUtils.checkIfBackslashIsSeparator(getBuiltInSlot_E_backslash_is_separator_always(),iX);
		a1.value= getURL(fileName,backslashIsSeparator);
	}
	public void getURL1fs(ChoisePoint iX, Term name) {
	}
	public void getURL0ff(ChoisePoint iX, PrologVariable a1) {
		ExtendedFileName fileName= retrieveRealFileName(iX);
		boolean backslashIsSeparator= FileUtils.checkIfBackslashIsSeparator(getBuiltInSlot_E_backslash_is_separator_always(),iX);
		a1.value= getURL(fileName,backslashIsSeparator);
	}
	public void getURL0fs(ChoisePoint iX) {
	}
	protected Term getURL(ExtendedFileName fileName, boolean backslashIsSeparator) {
		if (!fileName.isSystemFile) {
			return new PrologString(URL_Utils.get_URL_string(fileName.textName,staticContext,backslashIsSeparator));
		} else {
			throw new StandardStreamIsNotAllowedInThisOperation();
		}
	}
	//
	public void extractPath3s(ChoisePoint iX, Term a1, PrologVariable a2, PrologVariable a3) {
		ExtendedFileName fileName= retrieveRelativeFileName(a1,iX);
		extractFileName(iX,fileName,a2,a3);
	}
	public void extractPath2s(ChoisePoint iX, PrologVariable a1, PrologVariable a2) {
		ExtendedFileName fileName= retrieveRelativeFileName(iX);
		extractFileName(iX,fileName,a1,a2);
	}
	//
	protected void extractFileName(ChoisePoint iX, ExtendedFileName fileName, PrologVariable a1, PrologVariable a2) {
		if (!fileName.isSystemFile) {
			boolean backslashIsSeparatorAlways= Converters.term2YesNo(getBuiltInSlot_E_backslash_is_separator_always(),iX);
			String fullName= fileName.textName;
			int p1= fullName.lastIndexOf(':');
			int p2= fullName.lastIndexOf('/');
			p1= StrictMath.max(p1,p2);
			if (backslashIsSeparatorAlways || java.io.File.separatorChar=='\\') {
				p2= fullName.lastIndexOf('\\');
				p1= StrictMath.max(p1,p2);
			};
			if (p1 >= 0) {
				String path= fullName.substring(0,p1+1);
				String name= fullName.substring(p1+1);
				a1.value= new PrologString(path);
				a2.value= new PrologString(name);
				iX.pushTrail(a1);
				iX.pushTrail(a2);
				return;
			} else {
				a1.value= new PrologString("");
				a2.value= fileName.toTerm();
				iX.pushTrail(a1);
				iX.pushTrail(a2);
				return;
			}
		} else {
			a1.value= new PrologString("");
			a2.value= fileName.toTerm();
			iX.pushTrail(a1);
			iX.pushTrail(a2);
		}
		// if (!fileName.isSystemFile) {
		//	Path totalPath= fileSystem.getPath(fileName.textName);
		//	Path parent= totalPath.getParent();
		//	// Path name= totalPath.getName();
		//	Path name= totalPath.getName(totalPath.getNameCount()-1);
		//	String shortName= name.toString();
		//	String parentName;
		//	if (parent != null) {
		//		parentName= parent.toString();
		//	} else {
		//		parentName= "";
		//	};
		//	a1.value= new PrologString(parentName);
		//	a2.value= new PrologString(shortName);
		//	iX.pushTrail(a1);
		//	iX.pushTrail(a2);
		// } else {
		//	a1.value= new PrologString("");
		//	a2.value= fileName.toTerm();
		//	iX.pushTrail(a1);
		//	iX.pushTrail(a2);
		// }
	}
	//
	public void extractExtension3s(ChoisePoint iX, Term a1, PrologVariable a2, PrologVariable a3) {
		ExtendedFileName fileName= retrieveRelativeFileName(a1,iX);
		extractFileExtension(iX,fileName,a2,a3);
	}
	public void extractExtension2s(ChoisePoint iX, PrologVariable a1, PrologVariable a2) {
		ExtendedFileName fileName= retrieveRelativeFileName(iX);
		extractFileExtension(iX,fileName,a1,a2);
	}
	protected void extractFileExtension(ChoisePoint iX, ExtendedFileName fileName, PrologVariable a1, PrologVariable a2) {
		if (!fileName.isSystemFile) {
			boolean backslashIsSeparatorAlways= Converters.term2YesNo(getBuiltInSlot_E_backslash_is_separator_always(),iX);
			String fullName= fileName.textName;
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
					} else if (backslashIsSeparatorAlways || java.io.File.separatorChar=='\\') {
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
			a1.value= fileName.toTerm();
			a2.value= new PrologString("");
			iX.pushTrail(a1);
			iX.pushTrail(a2);
		}
	}
	//
	public void replaceExtension2ff(ChoisePoint iX, PrologVariable result, Term a1, Term a2) {
		ExtendedFileName fileName= retrieveRelativeFileName(a1,iX);
		try {
			String newExtension= a2.getStringValue(iX);
			boolean backslashIsSeparator= FileUtils.checkIfBackslashIsSeparator(getBuiltInSlot_E_backslash_is_separator_always(),iX);
			result.value= replaceFileExtension(fileName,newExtension,backslashIsSeparator);
		} catch (TermIsNotAString e) {
			throw new WrongArgumentIsNotAString(a2);
		}
	}
	public void replaceExtension2fs(ChoisePoint iX, Term a1, Term a2) {
	}
	public void replaceExtension1ff(ChoisePoint iX, PrologVariable result, Term a1) {
		ExtendedFileName fileName= retrieveRelativeFileName(iX);
		try {
			String newExtension= a1.getStringValue(iX);
			boolean backslashIsSeparator= FileUtils.checkIfBackslashIsSeparator(getBuiltInSlot_E_backslash_is_separator_always(),iX);
			result.value= replaceFileExtension(fileName,newExtension,backslashIsSeparator);
		} catch (TermIsNotAString e) {
			throw new WrongArgumentIsNotAString(a1);
		}
	}
	public void replaceExtension1fs(ChoisePoint iX, Term a1) {
	}
	protected Term replaceFileExtension(ExtendedFileName fileName, String newExtension, boolean backslashIsSeparator) {
		if (!fileName.isSystemFile) {
			String fullName= fileName.textName;
			return new PrologString(FileUtils.modifyFileExtension(fullName,newExtension,backslashIsSeparator));
		} else {
			return fileName.toTerm();
		}
	}
	//
	protected ExtendedFileName retrieveRelativeFileName(Term name, ChoisePoint iX) {
		try {
			String textName= name.getStringValue(iX);
			// textName= appendExtensionIfNecessary(textName,iX);
			return new ExtendedFileName(textName);
		} catch (TermIsNotAString e1) {
			return FileUtils.termToExtendedFileName(name,iX);
		}
	}
	protected ExtendedFileName retrieveRelativeFileName(ChoisePoint iX) {
		Term name= getBuiltInSlot_E_name();
		try {
			String textName= name.getStringValue(iX);
			textName= appendExtensionIfNecessary(textName,iX);
			return new ExtendedFileName(textName);
		} catch (TermIsNotAString e1) {
			return FileUtils.termToExtendedFileName(name,iX);
		}
	}
	protected ExtendedFileName retrieveRealFileName(Term name, ChoisePoint iX) {
		return retrieveRealFileName(name,true,iX);
	}
	protected ExtendedFileName retrieveRealFileName(Term name, boolean appendExtension, ChoisePoint iX) {
		try {
			String textName= name.getStringValue(iX);
			if (appendExtension) {
				textName= appendExtensionIfNecessary(textName,iX);
			};
			if (currentDirectory==null) {
				return new ExtendedFileName(FileUtils.makeRealName(textName));
			} else {
				return new ExtendedFileName(FileUtils.makeRealName(currentDirectory,textName));
			}
		} catch (TermIsNotAString e1) {
			return FileUtils.termToExtendedFileName(name,iX);
		}
	}
	protected ExtendedFileName retrieveRealFileName(ChoisePoint iX) {
		return retrieveRealFileName(true,iX);
	}
	protected ExtendedFileName retrieveRealFileName(boolean appendExtension, ChoisePoint iX) {
		Term name= getBuiltInSlot_E_name();
		try {
			String textName= name.getStringValue(iX);
			if (appendExtension) {
				textName= appendExtensionIfNecessary(textName,iX);
			};
			if (currentDirectory==null) {
				return new ExtendedFileName(FileUtils.makeRealName(textName));
			} else {
				return new ExtendedFileName(FileUtils.makeRealName(currentDirectory,textName));
			}
		} catch (TermIsNotAString e1) {
			return FileUtils.termToExtendedFileName(name,iX);
		}
	}
	protected String appendExtensionIfNecessary(String textName, ChoisePoint iX) {
		if (textName.indexOf('.') == -1) {
			Term extension= getBuiltInSlot_E_extension();
			String textExtension= null;
			try {
				textExtension= extension.getStringValue(iX);
			} catch (TermIsNotAString e2) {
				throw new WrongArgumentIsNotAString(extension);
			};
			textName= textName + textExtension;
		};
		return textName;
	}
}
