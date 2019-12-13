// (c) 2018 Alexei A. Morozov
//
// struct TVFilterImageHeader {
//	UInt4 Length;
//	// 2: 16 bit greyscale, 3: 8 bit RGB, 8: 64 bit greyscale float
//	UInt4 Format;
//	UInt4 Columns;
//	UInt4 Rows;
//	Bool LinesAreRows;
//	TVImageHeader Hdr;
//	TVVidImage Image;
// }

package morozov.system.astrohn.frames.data;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.Serializable;

public class TVFilterImageHeader implements Serializable {
	//
	protected int length;
	protected int format;
	protected int columns;
	protected int rows;
	protected boolean linesAreRows;
	protected TVImageHeader hdr;
	protected TVVidImage image;
	//
	private static final long serialVersionUID= 0xF67865DF2ED17411L;
	//
	static {
		int estimatedSize= constantHeaderSize();
		/*
		System.out.printf("TVFilterImageHeader: estimated constant header size: %s\n",estimatedSize);
		System.out.printf("TVFilterImageHeader: serialVersionUID: %x\n",serialVersionUID);
		*/
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public TVFilterImageHeader(int l, int f, int c, int r, boolean lAR, TVImageHeader h, TVVidImage i) {
		length= l;
		format= f;
		columns= c;
		rows= r;
		linesAreRows= lAR;
		hdr= h;
		image= i;
	}
	//
	public static int constantHeaderSize() {
		return 4 + 4 + 4 + 4 + 1 + TVImageHeader.size();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public int getLength() {
		return length;
	}
	public int getFormat () {
		return format;
	}
	public int getColumns() {
		return columns;
	}
	public int getRows() {
		return rows;
	}
	public boolean linesAreRows() {
		return linesAreRows;
	}
	public TVImageHeader getHDR() {
		return hdr;
	}
	public TVVidImage getImage() {
		return image;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static TVFilterImageHeader readTVFilterImageHeader(DataInputStream in, int lengthP, boolean reportWarnings) throws IOException {
		int length= Integer.reverseBytes(in.readInt());
		int format= Integer.reverseBytes(in.readInt());
		int columns= Integer.reverseBytes(in.readInt());
		int rows= Integer.reverseBytes(in.readInt());
		boolean linesAreRows= in.readBoolean();
		TVImageHeader hdr= TVImageHeader.readTVImageHeader(in,reportWarnings);
		int lengthOfRest= lengthP - constantHeaderSize();
		TVVidImage image= TVVidImage.readTVVidImage(in,lengthOfRest,reportWarnings);
		return new TVFilterImageHeader(
			length,
			format,
			columns,
			rows,
			linesAreRows,
			hdr,
			image);
	}
}
