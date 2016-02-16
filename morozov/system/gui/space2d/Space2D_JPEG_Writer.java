// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.gui.space2d;

import morozov.system.gui.space2d.errors.*;

import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.metadata.IIOInvalidTreeException;
import org.w3c.dom.Node;

public class Space2D_JPEG_Writer extends Space2DWriter {
	public Space2D_JPEG_Writer(ImageTypeSpecifier its) {
		super("jpeg",its);
	}
	public void setCompressionQuality(double compressionQuality) {
		if (iwp.canWriteCompressed()) {
			float quality= StrictMath.max(0,(float)compressionQuality);
			quality= StrictMath.min(1,quality);
			iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			// iwp.setCompressionType(types[0]);
			iwp.setCompressionQuality(quality);
		}
	}
	public void setComment(String comment) {
		String[] names= metadata.getMetadataFormatNames();
		int length= names.length;
		for (int i= 0; i < length; i++) {
			if (names[i].equals("javax_imageio_jpeg_image_1.0")) {
				Node root= metadata.getAsTree(names[i]);
				Node markerSequence= getChildNode(root,"markerSequence");
				if (markerSequence == null) {
					markerSequence= new IIOMetadataNode("markerSequence");
					root.appendChild(markerSequence);
				};
				Node com= getChildNode(markerSequence,"com");
				if (com == null) {
					com= new IIOMetadataNode("com");
					markerSequence.appendChild(com);
				};
				IIOMetadataNode iioCom= (IIOMetadataNode)com;
				iioCom.setAttribute("comment",comment);
				try {
					metadata.mergeTree(names[i],root);
				} catch (IIOInvalidTreeException e) {
					throw new ImageMetadataMergeError();
				}
			}
		}
	}
}
