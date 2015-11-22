package multiplayer.conversion;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

public class ConverterList implements OutConverter, InConverter{
	
	private Queue<Byte> data;
	
	public ConverterList() {
		data = new LinkedList<>();
	}
	
	public int length() {
		return data.size();
	}

	public void writeByte(byte b) throws IOException {
		data.add(b);
	}

	public void writeShort(short s) throws IOException {
		data.add((byte) ((s>>8 )     ));
		data.add((byte) ((s    )&0xff));
	}

	public void writeInt(int i) throws IOException {
		data.add((byte) ((i>>24)     ));
		data.add((byte) ((i>>16)&0xff));
		data.add((byte) ((i>>8 )&0xff));
		data.add((byte) ((i    )&0xff));
	}

	public void writeLong(long l) throws IOException {
		data.add((byte) ((l>>56)     ));
		data.add((byte) ((l>>48)&0xff));
		data.add((byte) ((l>>40)&0xff));
		data.add((byte) ((l>>32)&0xff));
		data.add((byte) ((l>>24)&0xff));
		data.add((byte) ((l>>16)&0xff));
		data.add((byte) ((l>>8 )&0xff));
		data.add((byte) ((l    )&0xff));
	}

	public void writeFloat(float f) throws IOException {
		int i = Float.floatToIntBits(f);
		data.add((byte) ((i>>24)     ));
		data.add((byte) ((i>>16)&0xff));
		data.add((byte) ((i>>8 )&0xff));
		data.add((byte) ((i    )&0xff));
	}

	public void writeDouble(double d) throws IOException {
		long l = Double.doubleToLongBits(d);
		data.add((byte) ((l>>56)     ));
		data.add((byte) ((l>>48)&0xff));
		data.add((byte) ((l>>40)&0xff));
		data.add((byte) ((l>>32)&0xff));
		data.add((byte) ((l>>24)&0xff));
		data.add((byte) ((l>>16)&0xff));
		data.add((byte) ((l>>8 )&0xff));
		data.add((byte) ((l    )&0xff));
	}

	public void writeBoolean(boolean bool) throws IOException {
		data.add((byte)(bool ? 1 : 0));
	}

	public void writeChar(char c) throws IOException {
		data.add((byte) ((c>>8 )     ));
		data.add((byte) ((c    )&0xff));
	}

	public byte readByte() throws IOException {
		return data.poll();
	}

	public short readShort() throws IOException {
		return (short) ((data.poll()<< 8)&0xff00|
						(data.poll()    )&0x00ff);
	}

	public int readInt() throws IOException {
		return 	(data.poll()<<24)&0xff000000|
				(data.poll()<<16)&0x00ff0000|
				(data.poll()<< 8)&0x0000ff00|
				(data.poll()    )&0x000000ff;
	}

	public long readLong() throws IOException {
		return 	(data.poll()<<56)&0xff00000000000000l|
				(data.poll()<<48)&0x00ff000000000000l|
				(data.poll()<<40)&0x0000ff0000000000l|
				(data.poll()<<32)&0x000000ff00000000l|
				(data.poll()<<24)&0x00000000ff000000l|
				(data.poll()<<16)&0x0000000000ff0000l|
				(data.poll()<< 8)&0x000000000000ff00l|
				(data.poll()    )&0x00000000000000ffl;
	}

	public float readFloat() throws IOException {
		return Float.intBitsToFloat((data.poll()<<24)&0xff000000|
									(data.poll()<<16)&0x00ff0000|
									(data.poll()<< 8)&0x0000ff00|
									(data.poll()    )&0x000000ff);
	}

	public double readDouble() throws IOException {
		return Double.longBitsToDouble(	(data.poll()<<56)&0xff00000000000000l|
										(data.poll()<<48)&0x00ff000000000000l|
										(data.poll()<<40)&0x0000ff0000000000l|
										(data.poll()<<32)&0x000000ff00000000l|
										(data.poll()<<24)&0x00000000ff000000l|
										(data.poll()<<16)&0x0000000000ff0000l|
										(data.poll()<< 8)&0x000000000000ff00l|
										(data.poll()    )&0x00000000000000ffl);
	}

	public boolean readBoolean() throws IOException {
		return data.poll()==1;
	}

	public char readChar() throws IOException {
		return (char) ((data.poll()<< 8)&0xff00|
					   (data.poll()    )&0x00ff);
	}
	
}
