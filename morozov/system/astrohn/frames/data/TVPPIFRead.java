// (c) 2018 Alexei A. Morozov
//
// struct TVPPIFRead {
//	UInt2 Cookie;
//	UInt2 HdrLength;
//	Byte Line;
//	Byte Position;
//	UInt2 Rows;
//	UInt2 ProductID;
//	Byte Channels;
//	Byte PositionOffset;
//	Byte Lines;
//	Byte Gap;
//	Byte Points;
//	Byte LineOffset;
//	Byte FrameSyncOffset;
//	Byte LineSyncOffset;
//	UInt2 OnTime;
//	UInt2 FMotorSpeed;
//	UInt2 FrameSpeed;
//	UInt2 BoardTemp;
//	Byte MinorVersion;
//	Byte MajorVersion;
//	UInt2 AccOnTime;
//	UInt2 Status;
//	UInt2 HornOffset;
//	UInt2 DropCount;
//	UInt2 QBuf;
//	UInt2 Orientation;
//	UInt2 BoardRevision;
//	UInt2 MultiIOStatus;
//	UInt2 Limit;
//	UInt2 Battery;
//	UInt2 SlowADC1;
//	UInt2 SlowADC2;
//	UInt2 SlowADC3;
//	UInt2 SlowADC4;
//	UInt2 PreampOffset;
//	UInt2 PreampGain;
//	UInt2 Temperature;
//	UInt2 Zero;
// }; // 64 bytes

package morozov.system.astrohn.frames.data;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.Serializable;

public class TVPPIFRead implements Serializable {
	//
	short cookie;
	short hdrLength;
	byte line;
	byte position;
	short rows;
	short productID;
	byte channels;
	byte positionOffset;
	byte lines;
	byte gap;
	byte points;
	byte lineOffset;
	byte frameSyncOffset;
	byte lineSyncOffset;
	short onTime;
	short fMotorSpeed;
	short frameSpeed;
	short boardTemp;
	byte minorVersion;
	byte majorVersion;
	short accOnTime;
	short status;
	short hornOffset;
	short dropCount;
	short qBuf;
	short orientation;
	short boardRevision;
	short multiIOStatus;
	short limit;
	short battery;
	short slowADC1;
	short slowADC2;
	short slowADC3;
	short slowADC4;
	short preampOffset;
	short preampGain;
	short temperature;
	short zero;
	//
	private static final long serialVersionUID= 0xE2E7DAF2965B485DL;
	//
	static {
		int estimatedSize= size();
		/*
		System.out.printf("TVPPIFRead: estimated size: %s; expected size: 64\n",estimatedSize);
		System.out.printf("TVPPIFRead: serialVersionUID: %x\n",serialVersionUID);
		*/
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public TVPPIFRead(
			short givenCookie,
			short givenHdrLength,
			byte givenLine,
			byte givenPosition,
			short givenRows,
			short givenProductID,
			byte givenChannels,
			byte givenPositionOffset,
			byte givenLines,
			byte givenGap,
			byte givenPoints,
			byte givenLineOffset,
			byte givenFrameSyncOffset,
			byte givenLineSyncOffset,
			short givenOnTime,
			short givenFMotorSpeed,
			short givenFrameSpeed,
			short givenBoardTemp,
			byte givenMinorVersion,
			byte givenMajorVersion,
			short givenAccOnTime,
			short givenStatus,
			short givenHornOffset,
			short givenDropCount,
			short givenQBuf,
			short givenOrientation,
			short givenBoardRevision,
			short givenMultiIOStatus,
			short givenLimit,
			short givenBattery,
			short givenSlowADC1,
			short givenSlowADC2,
			short givenSlowADC3,
			short givenSlowADC4,
			short givenPreampOffset,
			short givenPreampGain,
			short givenTemperature,
			short givenZero) {
		cookie= givenCookie;
		hdrLength= givenHdrLength;
		line= givenLine;
		position= givenPosition;
		rows= givenRows;
		productID= givenProductID;
		channels= givenChannels;
		positionOffset= givenPositionOffset;
		lines= givenLines;
		gap= givenGap;
		points= givenPoints;
		lineOffset= givenLineOffset;
		frameSyncOffset= givenFrameSyncOffset;
		lineSyncOffset= givenLineSyncOffset;
		onTime= givenOnTime;
		fMotorSpeed= givenFMotorSpeed;
		frameSpeed= givenFrameSpeed;
		boardTemp= givenBoardTemp;
		minorVersion= givenMinorVersion;
		majorVersion= givenMajorVersion;
		accOnTime= givenAccOnTime;
		status= givenStatus;
		hornOffset= givenHornOffset;
		dropCount= givenDropCount;
		qBuf= givenQBuf;
		orientation= givenOrientation;
		boardRevision= givenBoardRevision;
		multiIOStatus= givenMultiIOStatus;
		limit= givenLimit;
		battery= givenBattery;
		slowADC1= givenSlowADC1;
		slowADC2= givenSlowADC2;
		slowADC3= givenSlowADC3;
		slowADC4= givenSlowADC4;
		preampOffset= givenPreampOffset;
		preampGain= givenPreampGain;
		temperature= givenTemperature;
		zero= givenZero;
	}
	//
	public static int size() {
		return
			2 + 2 +
			1 + 1 +
			2 + 2 +
			1 + 1 + 1 + 1 + 1 + 1 + 1 + 1 +
			2 + 2 + 2 + 2 +
			1 + 1 +
			2 + 2 + 2 + 2 + 2 + 2 + 2 + 2 + 2 + 2 + 2 + 2 + 2 + 2 + 2 + 2 + 2 + 2;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public short getCookie() {
		return cookie;
	}
	public short getHdrLength() {
		return hdrLength;
	}
	public byte getLine() {
		return line;
	}
	public byte getPosition() {
		return position;
	}
	public short getRows() {
		return rows;
	}
	public short getProductID() {
		return productID;
	}
	public byte getChannels() {
		return channels;
	}
	public byte getPositionOffset() {
		return positionOffset;
	}
	public byte getLines() {
		return lines;
	}
	public byte getGap() {
		return gap;
	}
	public byte getPoints() {
		return points;
	}
	public byte getLineOffset() {
		return lineOffset;
	}
	public byte getFrameSyncOffset() {
		return frameSyncOffset;
	}
	public byte getLineSyncOffset() {
		return lineSyncOffset;
	}
	public short getOnTime() {
		return onTime;
	}
	public short getFMotorSpeed() {
		return fMotorSpeed;
	}
	public short getFrameSpeed() {
		return frameSpeed;
	}
	public short getBoardTemp() {
		return boardTemp;
	}
	public byte getMinorVersion() {
		return minorVersion;
	}
	public byte getMajorVersion() {
		return majorVersion;
	}
	public short getAccOnTime() {
		return accOnTime;
	}
	public short getStatus() {
		return status;
	}
	public short getHornOffset() {
		return hornOffset;
	}
	public short getDropCount() {
		return dropCount;
	}
	public short getQBuf() {
		return qBuf;
	}
	public short getOrientation() {
		return orientation;
	}
	public short getBoardRevision() {
		return boardRevision;
	}
	public short getMultiIOStatus() {
		return multiIOStatus;
	}
	public short getLimit() {
		return limit;
	}
	public short getBattery() {
		return battery;
	}
	public short getSlowADC1() {
		return slowADC1;
	}
	public short getSlowADC2() {
		return slowADC2;
	}
	public short getSlowADC3() {
		return slowADC3;
	}
	public short getSlowADC4() {
		return slowADC4;
	}
	public short getPreampOffset() {
		return preampOffset;
	}
	public short getPreampGain() {
		return preampGain;
	}
	public short getTemperature() {
		return temperature;
	}
	public short getZero() {
		return zero;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static TVPPIFRead readTVPPIFRead(DataInputStream in) throws IOException {
		short cookie= Short.reverseBytes(in.readShort());
		short hdrLength= Short.reverseBytes(in.readShort());
		byte line= in.readByte();
		byte position= in.readByte();
		short rows= Short.reverseBytes(in.readShort());
		short productID= Short.reverseBytes(in.readShort());
		byte channels= in.readByte();
		byte positionOffset= in.readByte();
		byte lines= in.readByte();
		byte gap= in.readByte();
		byte points= in.readByte();
		byte lineOffset= in.readByte();
		byte frameSyncOffset= in.readByte();
		byte lineSyncOffset= in.readByte();
		short onTime= Short.reverseBytes(in.readShort());
		short fMotorSpeed= Short.reverseBytes(in.readShort());
		short frameSpeed= Short.reverseBytes(in.readShort());
		short boardTemp= Short.reverseBytes(in.readShort());
		byte minorVersion= in.readByte();
		byte majorVersion= in.readByte();
		short accOnTime= Short.reverseBytes(in.readShort());
		short status= Short.reverseBytes(in.readShort());
		short hornOffset= Short.reverseBytes(in.readShort());
		short dropCount= Short.reverseBytes(in.readShort());
		short qBuf= Short.reverseBytes(in.readShort());
		short orientation= Short.reverseBytes(in.readShort());
		short boardRevision= Short.reverseBytes(in.readShort());
		short multiIOStatus= Short.reverseBytes(in.readShort());
		short limit= Short.reverseBytes(in.readShort());
		short battery= Short.reverseBytes(in.readShort());
		short slowADC1= Short.reverseBytes(in.readShort());
		short slowADC2= Short.reverseBytes(in.readShort());
		short slowADC3= Short.reverseBytes(in.readShort());
		short slowADC4= Short.reverseBytes(in.readShort());
		short preampOffset= Short.reverseBytes(in.readShort());
		short preampGain= Short.reverseBytes(in.readShort());
		short temperature= Short.reverseBytes(in.readShort());
		short zero= Short.reverseBytes(in.readShort());
		return new TVPPIFRead(
			cookie,
			hdrLength,
			line,
			position,
			rows,
			productID,
			channels,
			positionOffset,
			lines,
			gap,
			points,
			lineOffset,
			frameSyncOffset,
			lineSyncOffset,
			onTime,
			fMotorSpeed,
			frameSpeed,
			boardTemp,
			minorVersion,
			majorVersion,
			accOnTime,
			status,
			hornOffset,
			dropCount,
			qBuf,
			orientation,
			boardRevision,
			multiIOStatus,
			limit,
			battery,
			slowADC1,
			slowADC2,
			slowADC3,
			slowADC4,
			preampOffset,
			preampGain,
			temperature,
			zero);
	}
}
