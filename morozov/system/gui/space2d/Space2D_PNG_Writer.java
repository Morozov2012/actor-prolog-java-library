// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.gui.space2d;

import morozov.system.gui.space2d.errors.*;

import javax.imageio.ImageTypeSpecifier;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.metadata.IIOInvalidTreeException;
import org.w3c.dom.Node;

public class Space2D_PNG_Writer extends Space2DWriter {
	public Space2D_PNG_Writer(ImageTypeSpecifier its) {
		super("png",its);
	}
	// public void setInterlacing(boolean interlacing) {
	//	String[] names= metadata.getMetadataFormatNames();
	//	int length= names.length;
	//	for (int i= 0; i < length; i++) {
	//		if (names[i].equals("javax_imageio_png_1.0")) {
	//			Node root= metadata.getAsTree(names[i]);
	//			Node ihdr= getChildNode(root,"IHDR");
	//			if (ihdr == null) {
	//				ihdr= new IIOMetadataNode("IHDR");
	//				root.appendChild(ihdr);
	//			};
	//			IIOMetadataNode iioIHDR= (IIOMetadataNode)ihdr;
	//			if (interlacing) {
	//				iioIHDR.setAttribute("interlaceMethod","adam7");
	//			} else {
	//				iioIHDR.setAttribute("interlaceMethod","none");
	//			};
	//			try {
	//				metadata.mergeTree(names[i],root);
	//			} catch (IIOInvalidTreeException e) {
	//				throw new ImageMetadataMergeError();
	//			}
	//		}
	//	}
	// }
	public void setComment(String comment) {
		String[] names= metadata.getMetadataFormatNames();
		int length= names.length;
		for (int i= 0; i < length; i++) {
			if (names[i].equals("javax_imageio_png_1.0")) {
				Node root= metadata.getAsTree(names[i]);
				Node text= getChildNode(root,"tEXt");
				if (text == null) {
					text= new IIOMetadataNode("tEXt");
					root.appendChild(text);
				};
				Node textEntry= getChildNode(text,"tEXtEntry");
				if (textEntry == null) {
					textEntry= new IIOMetadataNode("tEXtEntry");
					text.appendChild(textEntry);
				};
				IIOMetadataNode iioTextEntry= (IIOMetadataNode)textEntry;
				iioTextEntry.setAttribute("keyword","comment");
				iioTextEntry.setAttribute("value",comment);
				try {
					metadata.mergeTree(names[i],root);
				} catch (IIOInvalidTreeException e) {
					throw new ImageMetadataMergeError();
				}
			}
		}
	}
}
