// (c) 2018 Alexei A. Morozov
//
// struct TVVidImage {
// public:
//	UInt4 Length;
//	Effi::ShPtr<Byte> ImageData;
// }

package morozov.system.astrohn.frames.data;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.Serializable;

public class TVVidImage implements Serializable {
	//
	protected int length;
	protected byte[] imageData;
	//
	private static final long serialVersionUID= 0x15F74145D6B4994CL;
	//
	// static {
	//	System.out.printf("TVVidImage: serialVersionUID: %x\n",serialVersionUID);
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public TVVidImage(int l, byte[] iD) {
		length= l;
		imageData= iD;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public int getLength() {
		return length;
	}
	public byte[] getImageData() {
		return imageData;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static TVVidImage readTVVidImage(DataInputStream in, int lengthOfRest, boolean reportWarnings) throws IOException {
		int length= Integer.reverseBytes(in.readInt());
		int expectedArrayLength= lengthOfRest - 4;
		/*
		System.out.printf("TVVidImage::length: %s (expected length = %s)\n",length,expectedArrayLength);
		*/
		if (length != expectedArrayLength && reportWarnings) {
			System.out.printf("WARNING: Unexpected image length %s (expected length = %s)\n",length,expectedArrayLength);
		};
		byte[] imageData= new byte[length];
		in.readFully(imageData);
		//for (int k=0; k < length; k++) {
		//	System.out.printf("%03d) %02x\n",k,imageData[k]);
		//};
		return new TVVidImage(length,imageData);
	}
}
