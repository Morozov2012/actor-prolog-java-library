// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.gui.space2d;

import morozov.system.gui.space2d.errors.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.IIOImage;
import org.w3c.dom.Node;

import java.util.Iterator;

public class Space2DWriter {
	//
	protected ImageWriter writer;
	protected ImageWriteParam iwp;
	protected IIOMetadata metadata;
	//
	protected static String defaultImageTransferFormat= "png";
	// protected static String defaultImageTransferFormat= "jpg";
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
	public Space2DWriter(String defaultFormatName, ImageTypeSpecifier its) {
		Iterator<ImageWriter> iterator= ImageIO.getImageWritersByFormatName(defaultFormatName);
		writer= iterator.next();
		iwp= writer.getDefaultWriteParam();
		metadata= writer.getDefaultImageMetadata(its,iwp);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static Space2DWriter createSpace2DWriter(java.awt.image.BufferedImage image, GenericImageEncodingAttributes attributes) {
		return createSpace2DWriter(defaultImageTransferFormat,image,attributes);
	}
	public static Space2DWriter createSpace2DWriter(String defaultFormatName, java.awt.image.BufferedImage image, GenericImageEncodingAttributes attributes) {
		ImageFileFormat format= attributes.getFormat();
		ImageTypeSpecifier its= new ImageTypeSpecifier(image);
		Space2DWriter writer= format.createWriter(defaultFormatName,its);
		attributes.setWriterAttributes(writer);
		return writer;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setCompressionQuality(double compressionQuality) {
	}
	public void setProgressiveMode(boolean progressiveMode) {
		if (iwp.canWriteProgressive()) {
			if (progressiveMode) {
				iwp.setProgressiveMode(ImageWriteParam.MODE_DEFAULT);
			} else {
				iwp.setProgressiveMode(ImageWriteParam.MODE_DISABLED);
			}
		}
	}
	public void setInterlacing(boolean interlacing) {
	}
	public void setComment(String comment) {
		if (metadata.isReadOnly()) {
			return;
		};
		String[] names= metadata.getMetadataFormatNames();
		int length= names.length;
		for (int i= 0; i < length; i++) {
			if (names[i].equals("javax_imageio_1.0")) {
				Node root= metadata.getAsTree(names[i]);
				Node text= getChildNode(root,"Text");
				if (text == null) {
					text= new IIOMetadataNode("Text");
					root.appendChild(text);
				};
				Node textEntry= getChildNode(text,"TextEntry");
				if (textEntry == null) {
					textEntry= new IIOMetadataNode("TextEntry");
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
	//
	///////////////////////////////////////////////////////////////
	//
	public void setOutput(ImageOutputStream output) {
		writer.setOutput(output);
	}
	//
	public void write(java.awt.image.BufferedImage image) throws IOException {
		IIOImage iioImage= new IIOImage(image,null,metadata);
		writer.write(null,iioImage,iwp);
	}
	//
	public void dispose() {
		writer.reset(); // 2017-03-04
		writer.dispose();
	}
	//
	static Node getChildNode(Node node, String givenName) {
		Node child= node.getFirstChild();
		if (child == null) {
			return null;
		};
		while (child != null) {
			String childName= child.getNodeName();
			if (childName.equals(givenName)) {
				return child;
			} else {
				child= child.getNextSibling();
			}
		};
		return null;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public byte[] imageToBytes(java.awt.image.BufferedImage nativeImage) {
		ByteArrayOutputStream outputStream= new ByteArrayOutputStream();
		MemoryCacheImageOutputStream imageStream= new MemoryCacheImageOutputStream(outputStream);
		try {
			setOutput(imageStream);
			write(nativeImage);
			imageStream.flush();
			return outputStream.toByteArray();
		} catch (IOException e) {
			throw new ImageConversionError(e);
		} finally {
			try {
				imageStream.close();
				// outputStream.close();
			} catch (IOException e) {
			}
			// writer.dispose();
		}
	}
	//
	public static java.awt.image.BufferedImage bytesToImage(byte[] bytes) {
		ByteArrayInputStream inputStream= new ByteArrayInputStream(bytes);
		java.awt.image.BufferedImage nativeImage;
		try {
			nativeImage= ImageIO.read(inputStream);
			return nativeImage;
		} catch (IOException e) {
			throw new ImageConversionError(e);
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
			}
		}
	}
}
