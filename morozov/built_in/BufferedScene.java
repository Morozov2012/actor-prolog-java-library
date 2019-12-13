// (c) 2018 IRE RAS Alexei A. Morozov

package morozov.built_in;

import morozov.run.*;
import morozov.system.converters.*;
import morozov.system.files.*;
import morozov.system.files.errors.*;
import morozov.system.gui.space3d.*;
import morozov.system.gui.space3d.errors.*;
import morozov.terms.*;
import morozov.worlds.*;

import com.sun.j3d.utils.scenegraph.io.SceneGraphFileReader;
import com.sun.j3d.utils.scenegraph.io.SceneGraphFileWriter;
import com.sun.j3d.loaders.Loader;
import com.sun.j3d.loaders.lw3d.Lw3dLoader;
import com.sun.j3d.loaders.objectfile.ObjectFile;
import com.sun.j3d.loaders.IncorrectFormatException;
import com.sun.j3d.loaders.ParsingErrorException;
import com.sun.j3d.utils.scenegraph.io.UnsupportedUniverseException;
import javax.media.j3d.BranchGroup;

import java.nio.file.Path;
import java.nio.file.Files;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

public abstract class BufferedScene extends DataResourceConsumer {
	//
	protected AtomicReference<BranchGroup> bufferedScene= new AtomicReference<>(null);
	//
	protected static final String format_LWS= "lws";	// Lightwave Scene Format
	protected static final String format_OBJ= "obj";	// Wavefront
	protected static final String format_J3D= "j3d";	// Java3D
	//
	protected static final String temporaryJ3DFilePrefix= "j3d";
	//
	///////////////////////////////////////////////////////////////
	//
	public BufferedScene() {
	}
	public BufferedScene(GlobalWorldIdentifier id) {
		super(id);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void load0s(ChoisePoint iX) {
		bufferedScene.set(readScene(iX));
	}
	public void load1s(ChoisePoint iX, Term a1) {
		bufferedScene.set(readScene(a1,iX));
	}
	//
	public BranchGroup readScene(ChoisePoint iX) {
		ExtendedFileName fileName= retrieveRealGlobalFileName(iX);
		int timeout= getMaximalWaitingTimeInMilliseconds(iX);
		return readScene(fileName,timeout,staticContext);
	}
	public BranchGroup readScene(Term targetName, ChoisePoint iX) {
		ExtendedFileName fileName= retrieveRealGlobalFileName(targetName,iX);
		int timeout= getMaximalWaitingTimeInMilliseconds(iX);
		return readScene(fileName,timeout,staticContext);
	}
	//
	protected BranchGroup readScene(ExtendedFileName fileName, int timeout, StaticContext staticContext) {
		String format= fileName.extractGraphicsFormatName().toLowerCase();
		if (format.equals(format_J3D) && fileName.getIsLocalResource()) {
			Path path= fileName.getPathOfLocalResource();
			return readJ3DFile(path);
		} else {
			byte[] byteArray= fileName.getByteContentOfUniversalResource(CharacterSet.NONE,timeout,staticContext);
			if (format.equals(format_J3D)) {
				try {
					Path path= Files.createTempFile(temporaryJ3DFilePrefix,null);
					Files.write(path,byteArray);
					BranchGroup branchGroup= readJ3DFile(path);
					try {
						Files.deleteIfExists(path);
					} catch (Throwable e2) {
					};
					return branchGroup;
				} catch (IOException e1) {
					throw new SceneConversionError(e1);
				}
			} else {
				InputStream inputStream= new ByteArrayInputStream(byteArray);
				InputStreamReader inputStreamReader= new InputStreamReader(inputStream);
				Loader loader;
				switch (format) {
				case format_LWS: // Lightwave Scene Format
					loader= new Lw3dLoader();
					break;
				case format_OBJ: // Wavefront
					loader= new ObjectFile();
					break;
				default:
					throw new ImageFileHasUnknownFormat(toString());
				};
				try {
					return loader.load(inputStreamReader).getSceneGroup();
				} catch (FileNotFoundException e) {
					throw new FileIsNotFound(toString());
				} catch (IncorrectFormatException e) {
					throw new IncorrectFormat(toString());
				} catch (ParsingErrorException e) {
					throw new ParsingError(toString());
				}
			}
		}
	}
	//
	protected BranchGroup readJ3DFile(Path path) {
		try {
			SceneGraphFileReader reader= new SceneGraphFileReader(path.toFile());
			try {
				BranchGroup[] branchGroup= reader.readBranchGraph(0);
				return branchGroup[0];
			} finally {
				try {
					reader.close();
				} catch (IOException e) {
				}
			}
		} catch (IOException e) {
			throw new SceneConversionError(e);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void save0s(ChoisePoint iX) {
		saveScene(bufferedScene.get(),iX);
	}
	public void save1s(ChoisePoint iX, Term a1) {
		saveScene(bufferedScene.get(),a1,iX);
	}
	//
	public void saveScene(BranchGroup branchGroup, ChoisePoint iX) {
		ExtendedFileName fileName= retrieveRealGlobalFileName(iX);
		int timeout= getMaximalWaitingTimeInMilliseconds(iX);
		saveScene(branchGroup,fileName,timeout,staticContext);
	}
	public void saveScene(BranchGroup branchGroup, Term targetName, ChoisePoint iX) {
		ExtendedFileName fileName= retrieveRealGlobalFileName(targetName,iX);
		int timeout= getMaximalWaitingTimeInMilliseconds(iX);
		saveScene(branchGroup,fileName,timeout,staticContext);
	}
	//
	protected void saveScene(BranchGroup branchGroup, ExtendedFileName fileName, int timeout, StaticContext staticContext) {
		Path path= fileName.getPathOfLocalResource();
		try {
			SceneGraphFileWriter writer= new SceneGraphFileWriter(path.toFile(),null,false,null,null);
			try {
				writer.writeBranchGraph(branchGroup);
			} finally {
				writer.close();
			}
		} catch (IOException e) {
			throw new SceneConversionError(e);
		} catch (UnsupportedUniverseException e) {
			throw new SceneConversionError(e);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setSceneGroup1s(ChoisePoint iX, Term a1) {
		BranchGroup branchGroup= PrincipalNode3D.argumentToBranchGroup(a1,null,null,null,iX);
		branchGroup.compile(); // 2019-04-07
		bufferedScene.set(branchGroup);
	}
	//
	public void setBranchGroup(BranchGroup group) {
		bufferedScene.set(group);
	}
	//
	public BranchGroup getBranchGroup() {
		return bufferedScene.get();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setBinary1s(ChoisePoint iX, Term a1) {
		byte[] byteArray= GeneralConverters.argumentToBinary(a1,iX);
		BranchGroup branchGroup= Space3DWriter.bytesToScene(byteArray);
		bufferedScene.set(branchGroup);
	}
	//
	public void getBinary0ff(ChoisePoint iX, Term result) {
		byte[] byteArray= Space3DWriter.sceneToBytes(bufferedScene.get());
		result.setNonBacktrackableValue(new PrologBinary(byteArray));
	}
	public void getBinary0fs(ChoisePoint iX) {
	}
}
