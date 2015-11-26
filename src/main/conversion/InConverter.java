package main.conversion;

import java.io.IOException;

public interface InConverter {
	
	public byte readByte() throws IOException ;
	public short readShort() throws IOException ;
	public int readInt() throws IOException ;
	public long readLong() throws IOException ;
	public float readFloat() throws IOException ;
	public double readDouble() throws IOException ;
	public boolean readBoolean() throws IOException ;
	public char readChar() throws IOException ;
	
}
