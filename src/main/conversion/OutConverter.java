package main.conversion;

import java.io.IOException;

public interface OutConverter {

	public void writeByte(byte b) throws IOException ;
	public void writeShort(short s) throws IOException ;
	public void writeInt(int i) throws IOException ;
	public void writeLong(long l) throws IOException ;
	public void writeFloat(float f) throws IOException ;
	public void writeDouble(double d) throws IOException ;
	public void writeBoolean(boolean bool) throws IOException ;
	public void writeChar(char c) throws IOException ;
	
}
