// (c) 2018 Alexei A. Morozov
//
// struct PacketHeader {
//	UInt4 Magick;
//	UInt4 DataType;
//	UInt4 Length;
// }

package morozov.system.astrohn.frames.data;

import morozov.system.astrohn.frames.data.errors.*;

import java.io.DataInputStream;
import java.io.IOException;

public class PacketHeader {
	//
	protected int magic;
	protected int dataType;
	protected int length;
	//
	protected static int expectedMagicCode= 0x53454144;
	//
	///////////////////////////////////////////////////////////////
	//
	public PacketHeader(int m, int dT, int l) {
		magic= m;
		dataType= dT;
		length= l;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public int getMagicCode() {
		return magic;
	}
	public int getDataType() {
		return dataType;
	}
	public int getLength() {
		return length;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static PacketHeader readPacketHeader(DataInputStream in) throws IOException {
		int magic= Integer.reverseBytes(in.readInt());
		int dataType= Integer.reverseBytes(in.readInt());
		int length= Integer.reverseBytes(in.readInt());
		return new PacketHeader(magic,dataType,length);
	}
	//
	public static int getLength(DataInputStream in) throws IOException {
		PacketHeader packetHeader= readPacketHeader(in);
		if (packetHeader.getMagicCode()==expectedMagicCode) {
			if (packetHeader.getDataType()==1) {
				packetHeader= readPacketHeader(in);
				if (packetHeader.getMagicCode()==expectedMagicCode) {
					if (packetHeader.getDataType()==0) {
						return packetHeader.getLength();
					} else {
						throw new DataType0IsExpected(packetHeader.getDataType());
					}
				} else {
					throw new MagicCodeIsExpected(packetHeader.getMagicCode());
				}
			} else {
				return packetHeader.getLength();
			}
		} else {
			throw new MagicCodeIsExpected(packetHeader.getMagicCode());
		}
	}
}
