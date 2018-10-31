// (c) 2018 IRE RAS Alexei A. Morozov

package morozov.system.gui.space3d;

import morozov.system.gui.space3d.errors.*;

import com.sun.j3d.utils.scenegraph.io.SceneGraphStreamWriter;
import com.sun.j3d.utils.scenegraph.io.SceneGraphStreamReader;
import com.sun.j3d.utils.scenegraph.io.NamedObjectException;
import javax.media.j3d.BranchGroup;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Space3DWriter {
	//
	public Space3DWriter() {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static byte[] sceneToBytes(BranchGroup branchGroup) {
		try {
			ByteArrayOutputStream outputStream= new ByteArrayOutputStream();
			SceneGraphStreamWriter writer= new SceneGraphStreamWriter(outputStream);
			try {
				writer.writeBranchGraph(branchGroup,null);
				// outputStream.flush();
				return outputStream.toByteArray();
			} catch (NamedObjectException e) {
				throw new SceneConversionError(e);
			} finally {
				try {
					writer.close();
				} catch (IOException e) {
				}
			}
		} catch (IOException e) {
			throw new SceneConversionError(e);
		}
	}
	//
	public static BranchGroup bytesToScene(byte[] bytes) {
		ByteArrayInputStream inputStream= new ByteArrayInputStream(bytes);
		BranchGroup branchGroup;
		try {
			SceneGraphStreamReader reader= new SceneGraphStreamReader(inputStream);
			branchGroup= reader.readBranchGraph(null);
			return branchGroup;
		} catch (IOException e) {
			throw new SceneConversionError(e);
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
			}
		}
	}
}
