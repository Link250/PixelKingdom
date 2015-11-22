package multiplayer.conversion;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class ConverterOutStream extends ObjectOutputStream implements OutConverter{

	public ConverterOutStream(OutputStream out) throws IOException {
		super(out);
	}
	
	protected void writeStreamHeader(){}
	
	public void writeByte(byte b) throws IOException {
		super.writeByte(b);
	}

	public void writeShort(short s) throws IOException {
		super.writeShort(s);
	}

	public void writeChar(char c) throws IOException {
		super.writeChar(c);
	}

}
