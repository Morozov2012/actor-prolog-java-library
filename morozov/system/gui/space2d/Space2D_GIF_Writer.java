// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.gui.space2d;

import morozov.system.gui.space2d.errors.*;

import javax.imageio.ImageTypeSpecifier;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.metadata.IIOInvalidTreeException;
import org.w3c.dom.Node;

public class Space2D_GIF_Writer extends Space2DWriter {
	public Space2D_GIF_Writer(ImageTypeSpecifier its) {
		super("gif",its);
	}
	public void setInterlacing(boolean interlacing) {
		String[] names= metadata.getMetadataFormatNames();
		int length= names.length;
		for (int i= 0; i < length; i++) {
			if (names[i].equals("javax_imageio_gif_image_1.0")) {
				Node root= metadata.getAsTree(names[i]);
				Node imageDescriptor= getChildNode(root,"ImageDescriptor");
				if (imageDescriptor == null) {
					imageDescriptor= new IIOMetadataNode("ImageDescriptor");
					root.appendChild(imageDescriptor);
				};
				IIOMetadataNode iioImageDescriptor= (IIOMetadataNode)imageDescriptor;
				if (interlacing) {
					iioImageDescriptor.setAttribute("interlaceFlag","TRUE");
				} else {
					iioImageDescriptor.setAttribute("interlaceFlag","FALSE");
				};
				try {
					metadata.mergeTree(names[i],root);
				} catch (IIOInvalidTreeException e) {
					throw new ImageMetadataMergeError();
				}
			}
		}
	}
	public void setComment(String comment) {
		String[] names= metadata.getMetadataFormatNames();
		int length= names.length;
		for (int i= 0; i < length; i++) {
			if (names[i].equals("javax_imageio_gif_image_1.0")) {
				Node root= metadata.getAsTree(names[i]);
				Node commentExtensions= getChildNode(root,"CommentExtensions");
				if (commentExtensions == null) {
					commentExtensions= new IIOMetadataNode("CommentExtensions");
					root.appendChild(commentExtensions);
				};
				Node commentExtension= getChildNode(commentExtensions,"CommentExtension");
				if (commentExtension == null) {
					commentExtension= new IIOMetadataNode("CommentExtension");
					commentExtensions.appendChild(commentExtension);
				};
				IIOMetadataNode iioCommentExtension= (IIOMetadataNode)commentExtension;
				iioCommentExtension.setAttribute("value",comment);
				try {
					metadata.mergeTree(names[i],root);
				} catch (IIOInvalidTreeException e) {
					throw new ImageMetadataMergeError();
				}
			}
		}
	}
}
