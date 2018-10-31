// (c) 2018 Alexei A. Morozov
//
// struct TVImageHeader {
//	UInt4 Indicator;
//	UInt8 RelTime;
//	UInt2 ApparentPPL;
//	Byte Obsolete;
//	Byte Zero;
//	UInt8 FileTime;
//	UInt2 RectLeft;
//	UInt2 RectTop;
//	UInt2 RectWidth;
//	UInt2 RectHeight;
//	Byte ForExpansion[10];
//	Byte AlertLevel;
//	Byte PaneSet;
//	Byte PaneSets;
//	Byte Version;
//	TVPPIFRead HWIF;
// }; // 110 bytes

package morozov.system.astrohn.frames.data;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.Serializable;

public class TVImageHeader implements Serializable {
	//
	protected int indicator;
	protected long relTime;
	protected short apparentPPL;
	protected byte obsolete;
	protected byte zero;
	protected long fileTime;
	protected short rectLeft;
	protected short rectTop;
	protected short rectWidth;
	protected short rectHeight;
	protected byte[] forExpansion; // [10];
	protected byte alertLevel;
	protected byte paneSet;
	protected byte paneSets;
	protected byte version;
	protected TVPPIFRead hWIF;
	//
	protected static short expectedCookie= (short)0xFAAC;
	//
	private static final long serialVersionUID= 0xCF69CDEC6BC4E815L;
	// private static final long serialVersionUID= 0x73E85DA4CF373B53L;
	//
	static {
		int estimatedSize= size();
		/*
		System.out.printf("TVImageHeader: estimated size: %s; expected size: 110\n",estimatedSize);
		System.out.printf("TVImageHeader: serialVersionUID: %x\n",serialVersionUID);
		*/
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public TVImageHeader(
			int givenIndicator,
			long givenRelTime,
			short givenApparentPPL,
			byte givenObsolete,
			byte givenZero,
			long givenFileTime,
			short givenRectLeft,
			short givenRectTop,
			short givenRectWidth,
			short givenHectHeight,
			byte[] givenForExpansion,
			byte givenAlertLevel,
			byte givenPaneSet,
			byte givenPaneSets,
			byte givenVersion,
			TVPPIFRead givenHWIF) {
		indicator= givenIndicator;
		relTime= givenRelTime;
		apparentPPL= givenApparentPPL;
		obsolete= givenObsolete;
		zero= givenZero;
		fileTime= givenFileTime;
		rectLeft= givenRectLeft;
		rectTop= givenRectTop;
		rectWidth= givenRectWidth;
		rectHeight= givenHectHeight;
		forExpansion= givenForExpansion;
		alertLevel= givenAlertLevel;
		paneSet= givenPaneSet;
		paneSets= givenPaneSets;
		version= givenVersion;
		hWIF= givenHWIF;
	}
	//
	public static int size() {
		return
			4 + 8 + 2 +
			1 + 1 +
			8 +
			2 + 2 + 2 + 2 +
			10 +
			1 + 1 + 1 + 1 +
			TVPPIFRead.size();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public int getIndicator() {
		return indicator;
	}
	public long getRelTime() {
		return relTime;
	}
	public short getApparentPPL() {
		return apparentPPL;
	}
	public byte getObsolete() {
		return obsolete;
	}
	public byte getZero() {
		return zero;
	}
	public long getFileTime() {
		return fileTime;
	}
	public short getRectLeft() {
		return rectLeft;
	}
	public short getRectTop() {
		return rectTop;
	}
	public short getRectWidth() {
		return rectWidth;
	}
	public short getRectHeight() {
		return rectHeight;
	}
	public byte[] getForExpansion() {
		return forExpansion;
	}
	public byte getAlertLevel() {
		return alertLevel;
	}
	public byte getPaneSet() {
		return paneSet;
	}
	public byte getPaneSets() {
		return paneSets;
	}
	public byte getVersion() {
		return version;
	}
	public TVPPIFRead getHWIF() {
		return hWIF;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static TVImageHeader readTVImageHeader(DataInputStream in) throws IOException {
		int indicator= Integer.reverseBytes(in.readInt());
		long relTime= Long.reverseBytes(in.readLong());
		short apparentPPL= Short.reverseBytes(in.readShort());
		byte obsolete= in.readByte();
		byte zero= in.readByte();
		long fileTime= Long.reverseBytes(in.readLong());
		short rectLeft= Short.reverseBytes(in.readShort());
		short rectTop= Short.reverseBytes(in.readShort());
		short rectWidth= Short.reverseBytes(in.readShort());
		short rectHeight= Short.reverseBytes(in.readShort());
		byte[] forExpansion= new byte[10];
		in.readFully(forExpansion);
		byte alertLevel= in.readByte();
		byte paneSet= in.readByte();
		byte paneSets= in.readByte();
		byte version= in.readByte();
		if (zero != 0) {
			System.out.printf("WARNING: TVFilterImageHeader.TVImageHeader.Zero != 0\n");
		};
		/*
		System.out.printf("TVImageHeader::Indicator (0 or 110): %s\n",indicator);
		System.out.printf("TVImageHeader::RelTime: %s\n",relTime);
		System.out.printf("TVImageHeader::ApparentPPL: %s\n",apparentPPL);
		System.out.printf("TVImageHeader::obsolete: %s\n",obsolete);
		System.out.printf("TVImageHeader::zero: %s\n",zero);
		System.out.printf("TVImageHeader::FileTime: %s\n",fileTime);
		System.out.printf("TVImageHeader::RECT: %s x %s + %s + %s\n",rectWidth,rectHeight,rectLeft,rectTop);
		for (int k=0; k < 10; k++) {
			System.out.printf("(TVImageHeader::forExpansion[%02d]: %02x\n",k,forExpansion[k]);
		};
		System.out.printf("TVImageHeader::AlertLevel: %x\n",alertLevel);
		System.out.printf("TVImageHeader::PaneSet: %x\n",paneSet);
		System.out.printf("TVImageHeader::PaneSets: %x\n",paneSets);
		System.out.printf("TVImageHeader::Version: %x\n",version);
		*/
		TVPPIFRead hWIF= TVPPIFRead.readTVPPIFRead(in);
		if (hWIF.getCookie() != expectedCookie) {
			System.out.printf("WARNING: Incorrect Cookie %x. Expected %x\n",hWIF.getCookie(),expectedCookie);
		};
		return new TVImageHeader(
			indicator,
			relTime,
			apparentPPL,
			obsolete,
			zero,
			fileTime,
			rectLeft,
			rectTop,
			rectWidth,
			rectHeight,
			forExpansion,
			alertLevel,
			paneSet,
			paneSets,
			version,
			hWIF);
	}
}
