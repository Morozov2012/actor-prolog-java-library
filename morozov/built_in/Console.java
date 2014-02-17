// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.built_in;

import morozov.run.*;
import morozov.system.*;
import morozov.system.errors.*;
import morozov.system.files.*;
import morozov.system.gui.*;
import morozov.system.gui.dialogs.special.*;
import morozov.system.signals.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import javax.swing.JOptionPane;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.nio.file.Path;

import java.math.BigInteger;

public abstract class Console extends Report {
	//
	protected java.io.File selectedFile= null;
	//
	abstract protected Term getBuiltInSlot_E_backslash_always_is_separator();
	//
	public void ask2s(ChoisePoint iX, Term title, Term question) throws Backtracking {
		askUser(question.toString(iX),title.toString(iX));
	}
	public void ask1s(ChoisePoint iX, Term question) throws Backtracking {
		askUser(question.toString(iX),"");
	}
	public void ask3ff(ChoisePoint iX, PrologVariable a1, Term title, Term question, Term list) throws Backtracking {
		String[] buttons= Converters.termToStrings(list,iX);
		a1.value= askUser(question.toString(iX),title.toString(iX),buttons);
	}
	public void ask3fs(ChoisePoint iX, Term title, Term question, Term list) throws Backtracking {
		String[] buttons= Converters.termToStrings(list,iX);
		askUser(question.toString(iX),title.toString(iX),buttons);
	}
	public void ask2ff(ChoisePoint iX, PrologVariable a1, Term question, Term list) throws Backtracking {
		String[] buttons= Converters.termToStrings(list,iX);
		a1.value= askUser(question.toString(iX),"",buttons);
	}
	public void ask2fs(ChoisePoint iX, Term question, Term list) throws Backtracking {
		String[] buttons= Converters.termToStrings(list,iX);
		askUser(question.toString(iX),"",buttons);
	}
	protected Term askUser(String question, String title, String[] buttons) throws Backtracking {
		// DesktopUtils.createPaneIfNecessary(staticContext);
		int answer= JOptionPane.showOptionDialog(
			StaticDesktopAttributes.retrieveDesktopPane(staticContext),
			question,
			title,
			JOptionPane.YES_NO_CANCEL_OPTION,
			JOptionPane.QUESTION_MESSAGE,
			null,
			buttons,
			0);
		if (answer < 0) {
			throw Backtracking.instance;
		} else {
			return new PrologString(buttons[answer]);
		}
	}
	protected void askUser(String question, String title) throws Backtracking {
		// DesktopUtils.createPaneIfNecessary(staticContext);
		int answer= JOptionPane.showConfirmDialog(
			StaticDesktopAttributes.retrieveDesktopPane(staticContext),
			question,
			title,
			JOptionPane.YES_NO_OPTION,
			JOptionPane.QUESTION_MESSAGE);
		if (answer==JOptionPane.YES_OPTION) {
			return;
		} else {
			throw Backtracking.instance;
		}
	}
	//
	public void note2s(ChoisePoint iX, Term title, Term message) {
		try {
			String dialogTitle= title.getStringValue(iX);
			note(iX,dialogTitle,message);
		} catch (TermIsNotAString e) {
			throw new WrongArgumentIsNotAString(title);
		}
	}
	public void note1s(ChoisePoint iX, Term message) {
		note(iX,"",message);
	}
	protected void note(ChoisePoint iX, String title, Term message) {
		// DesktopUtils.createPaneIfNecessary(staticContext);
		JOptionPane.showMessageDialog(
			StaticDesktopAttributes.retrieveDesktopPane(staticContext),
			message.toString(iX),
			title, // .toString(iX),
			JOptionPane.INFORMATION_MESSAGE);
	}
	//
	public void warning2s(ChoisePoint iX, Term title, Term message) {
		try {
			String dialogTitle= title.getStringValue(iX);
			warning(iX,dialogTitle,message);
		} catch (TermIsNotAString e) {
			throw new WrongArgumentIsNotAString(title);
		}
	}
	public void warning1s(ChoisePoint iX, Term message) {
		warning(iX,"",message);
	}
	protected void warning(ChoisePoint iX, String title, Term message) {
		// DesktopUtils.createPaneIfNecessary(staticContext);
		JOptionPane.showMessageDialog(
			StaticDesktopAttributes.retrieveDesktopPane(staticContext),
			message.toString(iX),
			title, // .toString(iX),
			JOptionPane.WARNING_MESSAGE);
	}
	//
	public void error2s(ChoisePoint iX, Term title, Term message) {
		try {
			String dialogTitle= title.getStringValue(iX);
			error(iX,dialogTitle,message);
		} catch (TermIsNotAString e) {
			throw new WrongArgumentIsNotAString(title);
		}
	}
	public void error1s(ChoisePoint iX, Term message) {
		error(iX,"",message);
	}
	protected void error(ChoisePoint iX, String title, Term message) {
		// DesktopUtils.createPaneIfNecessary(staticContext);
		JOptionPane.showMessageDialog(
			StaticDesktopAttributes.retrieveDesktopPane(staticContext),
			message.toString(iX),
			title, // .toString(iX),
			JOptionPane.ERROR_MESSAGE);
	}
	//
	public void inputString3ff(ChoisePoint iX, PrologVariable functionOutput, Term title, Term prompt, Term initialValue) throws Backtracking {
		try {
			String dialogTitle= title.getStringValue(iX);
			inputString(iX,functionOutput,dialogTitle,prompt,initialValue);
		} catch (TermIsNotAString e) {
			throw new WrongArgumentIsNotAString(title);
		}
	}
	public void inputString3fs(ChoisePoint iX, Term title, Term prompt, Term initialValue) throws Backtracking {
		inputString3ff(iX,null,title,prompt,initialValue);
	}
	public void inputString2ff(ChoisePoint iX, PrologVariable functionOutput, Term prompt, Term initialValue) throws Backtracking {
		inputString(iX,functionOutput,"",prompt,initialValue);
	}
	public void inputString2fs(ChoisePoint iX, Term prompt, Term initialValue) throws Backtracking {
		inputString2ff(iX,null,prompt,initialValue);
	}
	protected void inputString(ChoisePoint iX, PrologVariable functionOutput, String title, Term prompt, Term initialValue) throws Backtracking {
		// DesktopUtils.createPaneIfNecessary(staticContext);
		String text= (String)JOptionPane.showInputDialog(
			StaticDesktopAttributes.retrieveDesktopPane(staticContext),
			prompt.toString(iX),
			title,
			JOptionPane.PLAIN_MESSAGE,
			null,
			null,
			initialValue.toString(iX));
		if (text==null) {
			throw Backtracking.instance;
		} else {
			if (functionOutput != null) {
				functionOutput.value= new PrologString(text);
				// iX.pushTrail(functionOutput);
			}
		}
	}
	//
	public void inputInteger4ff(ChoisePoint iX, PrologVariable functionOutput, Term title, Term prompt, Term initialValue, Term errorMessage) throws Backtracking {
		try {
			String dialogTitle= title.getStringValue(iX);
			inputInteger(iX,functionOutput,dialogTitle,prompt,initialValue,errorMessage);
		} catch (TermIsNotAString e) {
			throw new WrongArgumentIsNotAString(title);
		}
	}
	public void inputInteger4fs(ChoisePoint iX, Term title, Term prompt, Term initialValue, Term errorMessage) throws Backtracking {
		inputInteger4ff(iX,null,title,prompt,initialValue,errorMessage);
	}
	public void inputInteger3ff(ChoisePoint iX, PrologVariable functionOutput, Term title, Term prompt, Term initialValue) throws Backtracking {
		inputInteger4ff(iX,functionOutput,title,prompt,initialValue,null);
	}
	public void inputInteger3fs(ChoisePoint iX, Term title, Term prompt, Term initialValue) throws Backtracking {
		inputInteger3ff(iX,null,title,prompt,initialValue);
	}
	public void inputInteger2ff(ChoisePoint iX, PrologVariable functionOutput, Term prompt, Term initialValue) throws Backtracking {
		inputInteger(iX,functionOutput,"",prompt,initialValue,null);
	}
	public void inputInteger2fs(ChoisePoint iX, Term prompt, Term initialValue) throws Backtracking {
		inputInteger2ff(iX,null,prompt,initialValue);
	}
	protected void inputInteger(ChoisePoint iX, PrologVariable functionOutput, String title, Term prompt, Term initialValue, Term errorMessage) throws Backtracking {
		// DesktopUtils.createPaneIfNecessary(staticContext);
		String warning;
		if (errorMessage==null) {
			warning= "It isn't a valid integer!";
		} else {
			warning= errorMessage.toString(iX);
		};
		InputDialog dialog= new InputDialog(
			true,
			title,
			prompt.toString(iX),
			initialValue.toString(iX),
			warning);
		String text= dialog.getValidatedText();
		if (text==null) {
			throw Backtracking.instance;
		} else {
			if (functionOutput != null) {
				try {
					BigInteger result= Converters.stringToStrictInteger(text);
					functionOutput.value= new PrologInteger(result);
					// iX.pushTrail(functionOutput);
				} catch (TermIsNotAnInteger error) {
					throw Backtracking.instance;
				}
			}
		}
	}
	//
	public void inputReal4ff(ChoisePoint iX, PrologVariable functionOutput, Term title, Term prompt, Term initialValue, Term errorMessage) throws Backtracking {
		try {
			String dialogTitle= title.getStringValue(iX);
			inputReal(iX,functionOutput,dialogTitle,prompt,initialValue,errorMessage);
		} catch (TermIsNotAString e) {
			throw new WrongArgumentIsNotAString(title);
		}
	}
	public void inputReal4fs(ChoisePoint iX, Term title, Term prompt, Term initialValue, Term errorMessage) throws Backtracking {
		inputReal4ff(iX,null,title,prompt,initialValue,errorMessage);
	}
	public void inputReal3ff(ChoisePoint iX, PrologVariable functionOutput, Term title, Term prompt, Term initialValue) throws Backtracking {
		inputReal4ff(iX,functionOutput,title,prompt,initialValue,null);
	}
	public void inputReal3fs(ChoisePoint iX, Term title, Term prompt, Term initialValue) throws Backtracking {
		inputReal3ff(iX,null,title,prompt,initialValue);
	}
	public void inputReal2ff(ChoisePoint iX, PrologVariable functionOutput, Term prompt, Term initialValue) throws Backtracking {
		inputReal(iX,functionOutput,"",prompt,initialValue,null);
	}
	public void inputReal2fs(ChoisePoint iX, Term prompt, Term initialValue) throws Backtracking {
		inputReal2ff(iX,null,prompt,initialValue);
	}
	protected void inputReal(ChoisePoint iX, PrologVariable functionOutput, String title, Term prompt, Term initialValue, Term errorMessage) throws Backtracking {
		// DesktopUtils.createPaneIfNecessary(staticContext);
		String warning;
		if (errorMessage==null) {
			warning= "It isn't a valid real!";
		} else {
			warning= errorMessage.toString(iX);
		};
		InputDialog dialog= new InputDialog(
			false,
			title,
			prompt.toString(iX),
			initialValue.toString(iX),
			warning);
		String text= dialog.getValidatedText();
		if (text==null) {
			throw Backtracking.instance;
		} else {
			if (functionOutput != null) {
				try {
					double result= Converters.stringToReal(text);
					functionOutput.value= new PrologReal(result);
					// iX.pushTrail(functionOutput);
				} catch (TermIsNotAReal error) {
					throw Backtracking.instance;
				}
			}
		}
	}
	//
	public void inputFileName7ff(ChoisePoint iX, PrologVariable fileName, Term title, Term mask, Term types, Term startPath, Term multiSel, PrologVariable selList, PrologVariable selType) throws Backtracking {
		try {
			String dialogTitle= title.getStringValue(iX);
			String dialogStartPath= startPath.getStringValue(iX);
			inputFileName(iX,fileName,dialogTitle,mask,types,dialogStartPath,false,multiSel,selList,selType);
		} catch (TermIsNotAString e) {
			throw new WrongArgumentIsNotAString(title);
		}
	}
	public void inputFileName7fs(ChoisePoint iX, Term title, Term mask, Term types, Term startPath, Term multiSel, PrologVariable selList, PrologVariable selType) throws Backtracking {
		inputFileName7ff(iX,null,title,mask,types,startPath,multiSel,selList,selType);
	}
	public void inputFileName4ff(ChoisePoint iX, PrologVariable fileName, Term title, Term mask, Term types, Term startPath) throws Backtracking {
		try {
			String dialogTitle= title.getStringValue(iX);
			String dialogStartPath= startPath.getStringValue(iX);
			inputFileName(iX,fileName,dialogTitle,mask,types,dialogStartPath,false,null,null,null);
		} catch (TermIsNotAString e) {
			throw new WrongArgumentIsNotAString(title);
		}
	}
	public void inputFileName4fs(ChoisePoint iX, Term title, Term mask, Term types, Term startPath) throws Backtracking {
		inputFileName4ff(iX,null,title,mask,types,startPath);
	}
	public void inputFileName3ff(ChoisePoint iX, PrologVariable fileName, Term title, Term mask, Term types) throws Backtracking {
		try {
			String dialogTitle= title.getStringValue(iX);
			inputFileName(iX,fileName,dialogTitle,mask,types,null,false,null,null,null);
		} catch (TermIsNotAString e) {
			throw new WrongArgumentIsNotAString(title);
		}
	}
	public void inputFileName3fs(ChoisePoint iX, Term title, Term mask, Term types) throws Backtracking {
		inputFileName3ff(iX,null,title,mask,types);
	}
	public void inputFileName2ff(ChoisePoint iX, PrologVariable fileName, Term mask, Term types) throws Backtracking {
		inputFileName(iX,fileName,"",mask,types,null,false,null,null,null);
	}
	public void inputFileName2fs(ChoisePoint iX, Term mask, Term types) throws Backtracking {
		inputFileName2ff(iX,null,mask,types);
	}
	//
	public void inputNewFileName7ff(ChoisePoint iX, PrologVariable fileName, Term title, Term mask, Term types, Term startPath, Term multiSel, PrologVariable selList, PrologVariable selType) throws Backtracking {
		try {
			String dialogTitle= title.getStringValue(iX);
			String dialogStartPath= startPath.getStringValue(iX);
			inputFileName(iX,fileName,dialogTitle,mask,types,dialogStartPath,true,multiSel,selList,selType);
		} catch (TermIsNotAString e) {
			throw new WrongArgumentIsNotAString(title);
		}
	}
	public void inputNewFileName7fs(ChoisePoint iX, Term title, Term mask, Term types, Term startPath, Term multiSel, PrologVariable selList, PrologVariable selType) throws Backtracking {
		inputNewFileName7ff(iX,null,title,mask,types,startPath,multiSel,selList,selType);
	}
	public void inputNewFileName4ff(ChoisePoint iX, PrologVariable fileName, Term title, Term mask, Term types, Term startPath) throws Backtracking {
		try {
			String dialogTitle= title.getStringValue(iX);
			String dialogStartPath= startPath.getStringValue(iX);
			inputFileName(iX,fileName,dialogTitle,mask,types,dialogStartPath,true,null,null,null);
		} catch (TermIsNotAString e) {
			throw new WrongArgumentIsNotAString(title);
		}
	}
	public void inputNewFileName4fs(ChoisePoint iX, Term title, Term mask, Term types, Term startPath) throws Backtracking {
		inputNewFileName4ff(iX,null,title,mask,types,startPath);
	}
	public void inputNewFileName3ff(ChoisePoint iX, PrologVariable fileName, Term title, Term mask, Term types) throws Backtracking {
		try {
			String dialogTitle= title.getStringValue(iX);
			inputFileName(iX,fileName,dialogTitle,mask,types,null,true,null,null,null);
		} catch (TermIsNotAString e) {
			throw new WrongArgumentIsNotAString(title);
		}
	}
	public void inputNewFileName3fs(ChoisePoint iX, Term title, Term mask, Term types) throws Backtracking {
		inputNewFileName3ff(iX,null,title,mask,types);
	}
	public void inputNewFileName2ff(ChoisePoint iX, PrologVariable fileName, Term mask, Term types) throws Backtracking {
		inputFileName(iX,fileName,"",mask,types,null,true,null,null,null);
	}
	public void inputNewFileName2fs(ChoisePoint iX, Term mask, Term types) throws Backtracking {
		inputNewFileName2ff(iX,null,mask,types);
	}
	//
	protected void inputFileName(ChoisePoint iX, PrologVariable fileName, String dialogTitle, Term mask, Term types, String startPath, boolean showSaveDialog, Term multiSel, PrologVariable selList, PrologVariable selType) throws Backtracking {
		try {
			String initialMask= mask.getStringValue(iX);
			FileNameMask[] masks= FileUtils.termToFileNameMasks(types,iX);
			//
			// DesktopUtils.createPaneIfNecessary(staticContext);
			//
			JFileChooser chooser= new ExtendedFileChooser();
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			chooser.setAcceptAllFileFilterUsed(false);
			chooser.setDialogTitle(dialogTitle);
			if (startPath == null) {
				if (selectedFile != null) {
					// chooser.setCurrentDirectory(selectedFile);
					chooser.setSelectedFile(selectedFile);
				}
			} else {
				boolean backslashIsSeparator= FileUtils.checkIfBackslashIsSeparator(getBuiltInSlot_E_backslash_always_is_separator(),iX);
				startPath= FileUtils.replaceBackslashes(startPath,backslashIsSeparator);
				java.io.File file= new java.io.File(startPath);
				// chooser.setCurrentDirectory(file);
				chooser.setSelectedFile(file);
			};
			FileFilter initialFilter= null;
			for (int n=0; n < masks.length; n++) {
				if (masks[n].containsWildcard(initialMask)) {
					initialFilter= masks[n];
				};
				chooser.addChoosableFileFilter(masks[n]);
			};
			if (initialFilter != null) {
				chooser.setFileFilter(initialFilter);
			};
			if (multiSel != null) {
				boolean enableMultipleSelection= Converters.term2OnOff(multiSel,iX);
				chooser.setMultiSelectionEnabled(enableMultipleSelection);
			};
			int returnVal;
			if (showSaveDialog) {
				returnVal= chooser.showSaveDialog(null);
			} else {
				returnVal= chooser.showOpenDialog(null);
			};
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				selectedFile= chooser.getSelectedFile();
				if (selectedFile==null) {
					throw Backtracking.instance;
				};
				FileFilter currentFilter= chooser.getFileFilter();
				FileNameMask currentMask= null;
				if (currentFilter instanceof FileNameMask) {
					currentMask= (FileNameMask)currentFilter;
				};
				// String selectedName= FileUtils.tryToMakeRealName(selectedFile.toPath()).toString();
				Path selectedPath= selectedFile.toPath();
				if (selectedPath==null) {
					throw Backtracking.instance;
				};
				String selectedName= selectedPath.toString();
				if (currentMask != null) {
					selectedName= currentMask.appendExtensionIfNecessary(selectedName);
				};
				if (fileName != null) {
					fileName.value= new PrologString(selectedName);
					// iX.pushTrail(fileName);
				};
				if (selList != null) {
					if (chooser.isMultiSelectionEnabled()) {
						java.io.File[] selectedFiles= chooser.getSelectedFiles();
						Term list= PrologEmptyList.instance;
						for (int n= selectedFiles.length-1; n >= 0; n--) {
							// String currentName= FileUtils.tryToMakeRealName(selectedFiles[n].toPath()).toString();
							String currentName= selectedFiles[n].toPath().toString();
							if (currentMask != null) {
								currentName= currentMask.appendExtensionIfNecessary(currentName);
							};
							list= new PrologList(new PrologString(currentName),list);
						};
						selList.value= list;
						iX.pushTrail(selList);
					} else {
						selList.value= new PrologList(new PrologString(selectedName),PrologEmptyList.instance);
						iX.pushTrail(selList);
					}
				};
				if (selType != null) {
					if (currentMask != null) {
						// System.out.printf("currentFilter= %s\n",currentFilter);
						boolean maskIsFound= false;
						for (int n=0; n < masks.length; n++) {
							if (masks[n]==currentMask) {
								selType.value= new PrologInteger(n+1);
								iX.pushTrail(selType);
								maskIsFound= true;
								break;
							}
						};
						if (!maskIsFound) {
							selType.value= new PrologInteger(1); // Actor Prolog default value
							iX.pushTrail(selType);
						}
					} else {
						selType.value= new PrologInteger(1); // Actor Prolog default value
						iX.pushTrail(selType);
					}
				}
			} else {
				throw Backtracking.instance;
			}
		} catch (TermIsNotAString e) {
			throw new WrongArgumentIsNotAString(mask);
		}
	}
	//
	public void inputDirectoryName2ff(ChoisePoint iX, PrologVariable directoryName, Term title, Term oldPath) throws Backtracking {
		String dialogTitle;
		String initialPath;
		try {
			dialogTitle= title.getStringValue(iX);
		} catch (TermIsNotAString e) {
			throw new WrongArgumentIsNotAString(title);
		};
		try {
			initialPath= oldPath.getStringValue(iX);
		} catch (TermIsNotAString e) {
			throw new WrongArgumentIsNotAString(oldPath);
		};
		inputDirectoryName(iX,directoryName,dialogTitle,initialPath);
	}
	public void inputDirectoryName2fs(ChoisePoint iX, Term title, Term oldPath) throws Backtracking {
		inputDirectoryName2ff(iX,null,title,oldPath);
	}
	public void inputDirectoryName1ff(ChoisePoint iX, PrologVariable directoryName, Term oldPath) throws Backtracking {
		try {
			String initialPath= oldPath.getStringValue(iX);
			inputDirectoryName(iX,directoryName,"",initialPath);
		} catch (TermIsNotAString e) {
			throw new WrongArgumentIsNotAString(oldPath);
		}
	}
	public void inputDirectoryName1fs(ChoisePoint iX, Term oldPath) throws Backtracking {
		inputDirectoryName1ff(iX,null,oldPath);
	}
	//
	protected void inputDirectoryName(ChoisePoint iX, PrologVariable directoryName, String dialogTitle, String initialPath) throws Backtracking {
		// DesktopUtils.createPaneIfNecessary(staticContext);
		JFileChooser chooser= new ExtendedFileChooser();
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setDialogTitle(dialogTitle);
		if (initialPath.isEmpty()) {
			initialPath= FileUtils.getUserDirectory();
		};
		try {
			java.io.File initialDirectory= new java.io.File(initialPath);
			chooser.setCurrentDirectory(initialDirectory);
		} finally {
		};
		int returnVal= chooser.showOpenDialog(null);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			selectedFile= chooser.getSelectedFile();
			String name= FileUtils.tryToMakeRealName(selectedFile.toPath()).toString();
			if (directoryName != null) {
				directoryName.value= new PrologString(name);
				// iX.pushTrail(directoryName);
			}
		} else {
			throw Backtracking.instance;
		}
	}
	//
	public void inputColor1ff(ChoisePoint iX, PrologVariable functionOutput, Term initialValue) throws Backtracking {
		Color initialColor= null;
		try {
			initialColor= GUI_Utils.termToColor(initialValue,iX);
		} catch (TermIsSymbolDefault e) {
			initialColor= Color.WHITE;
		};
		// DesktopUtils.createPaneIfNecessary(staticContext);
		Color newColor= JColorChooser.showDialog(
			StaticDesktopAttributes.retrieveDesktopPane(staticContext),
			"", // title.toString(iX),
			initialColor);
		if (newColor==null) {
			throw Backtracking.instance;
		} else {
			if (functionOutput != null) {
				functionOutput.value= new PrologInteger(newColor.getRGB());
				// iX.pushTrail(functionOutput);
			}
		}
	}
	public void inputColor1fs(ChoisePoint iX, Term initialValue) throws Backtracking {
		inputColor1ff(iX,null,initialValue);
	}
	//
	public void inputFont3s(ChoisePoint iX, PrologVariable outputVariable1, PrologVariable outputVariable2, PrologVariable outputVariable3) throws Backtracking {
		FontDialog m_fontDialog= showFontDialog();
		int option= m_fontDialog.getOption();
		if (option==JOptionPane.OK_OPTION) {
			AttributeSet a2= m_fontDialog.getAttributes();
			if (a2==null) {
				throw Backtracking.instance;
			} else {
				String fontName= StyleConstants.getFontFamily(a2);
				int fontSize= StyleConstants.getFontSize(a2);
				
				outputVariable1.value= new PrologString(fontName);
				outputVariable2.value= new PrologInteger(fontSize);
				iX.pushTrail(outputVariable1);
				iX.pushTrail(outputVariable2);
				if (outputVariable3 != null) {
					boolean isBold= StyleConstants.isBold(a2);
					boolean isItalic= StyleConstants.isItalic(a2);
					boolean isUnderline= StyleConstants.isUnderline(a2);
					outputVariable3.value= GUI_Utils.fontStyleToTerm(isBold,isItalic,isUnderline);
					iX.pushTrail(outputVariable3);
				}
			}
		} else {
			throw Backtracking.instance;
		}
	}
	public void inputFont2s(ChoisePoint iX, PrologVariable outputVariable1, PrologVariable outputVariable2) throws Backtracking {
		inputFont3s(iX,outputVariable1,outputVariable2,null);
	}
	//
	protected FontDialog showFontDialog() {
		// DesktopUtils.createPaneIfNecessary(staticContext);
		GraphicsEnvironment ge= GraphicsEnvironment.getLocalGraphicsEnvironment();
		String[] m_fontNames= ge.getAvailableFontFamilyNames();
		String[] m_fontSizes= new String[] {
			"8", "9", "10", "11", "12", "14",
			"16", "18", "20", "22", "24", "26",
			"28", "36", "48", "72"};
		FontDialog m_fontDialog= new FontDialog(JOptionPane.getRootFrame(),m_fontNames,m_fontSizes);
		SimpleAttributeSet a1= new SimpleAttributeSet();
		StyleConstants.setFontFamily(a1,"Monospaced");
		StyleConstants.setFontSize(a1,12);
		m_fontDialog.setAttributes(a1);
		m_fontDialog.setVisible(true);
		return m_fontDialog;
	}
	//
	public void selectItem6s(ChoisePoint iX, Term title, Term message, Term list, Term initialPosition, PrologVariable selectedName, PrologVariable selectedPosition) throws Backtracking {
		String dialogTitle;
		String dialogMessage;
		try {
			dialogTitle= title.getStringValue(iX);
		} catch (TermIsNotAString e) {
			throw new WrongArgumentIsNotAString(title);
		};
		try {
			dialogMessage= message.getStringValue(iX);
		} catch (TermIsNotAString e) {
			throw new WrongArgumentIsNotAString(message);
		};
		selectItem(iX,dialogTitle,dialogMessage,list,initialPosition,selectedName,selectedPosition);
	}
	public void selectItem5s(ChoisePoint iX, Term title, Term list, Term initialPosition, PrologVariable selectedName, PrologVariable selectedPosition) throws Backtracking {
		try {
			String dialogTitle= title.getStringValue(iX);
			selectItem(iX,dialogTitle,null,list,initialPosition,selectedName,selectedPosition);
		} catch (TermIsNotAString e) {
			throw new WrongArgumentIsNotAString(title);
		}
	}
	public void selectItem4s(ChoisePoint iX, Term list, Term initialPosition, PrologVariable selectedName, PrologVariable selectedPosition) throws Backtracking {
		selectItem(iX,"",null,list,initialPosition,selectedName,selectedPosition);
	}
	protected void selectItem(ChoisePoint iX, String dialogTitle, String dialogMessage, Term list, Term initialPosition, PrologVariable selectedName, PrologVariable selectedPosition) throws Backtracking {
		String[] possibilities= Converters.termToStrings(list,iX);
		try {
			int iP= initialPosition.getSmallIntegerValue(iX);
			String initialValue= null;
			if (iP >= 1 && iP <= possibilities.length) {
				initialValue= possibilities[iP-1];
			};
			String result= (String)JOptionPane.showInputDialog(
				StaticDesktopAttributes.retrieveDesktopPane(staticContext),
				dialogMessage,
				dialogTitle,
				JOptionPane.PLAIN_MESSAGE,
				null, // icon
				possibilities,
				initialValue);
			if (result != null) {
				int selectedIndex= -1;
				for (int n=0; n < possibilities.length; n++) {
					if (result.equals(possibilities[n])) {
						selectedIndex= n + 1;
						break;
					}
				};
				if (selectedIndex > 0) {
					selectedName.value= new PrologString(result);
					selectedPosition.value= new PrologInteger(selectedIndex);
					iX.pushTrail(selectedName);
					iX.pushTrail(selectedPosition);
				} else {
					throw Backtracking.instance;
				}
			} else {
				throw Backtracking.instance;
			};
		} catch (TermIsNotAnInteger e) {
			throw new WrongArgumentIsNotAnInteger(initialPosition);
		}
	}
}
