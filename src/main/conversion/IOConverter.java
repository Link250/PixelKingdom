package main.conversion;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class IOConverter {
	public static void sendList(ArrayList<Byte> list, OutputStream out) throws IOException {
		for(Byte b : list) {
			out.write(b);
		}out.flush();
	}
	
	public static int receiveInt(InputStream in) throws IOException {
		return (in.read()<<24)&0xff000000|(in.read()<<16)&0x00ff0000|(in.read()<< 8)&0x0000ff00|(in.read()<< 0)&0x000000ff;
	}
	
	public static int receive3bInt(InputStream in) throws IOException {
		return (in.read()<<16)&0x00ff0000|(in.read()<< 8)&0x0000ff00|(in.read()<< 0)&0x000000ff;
	}

	public static Short receiveShort(InputStream in) throws IOException {
		return (short) ((in.read()<< 8)&0xff00|(in.read()<< 0)&0x00ff);
	}
}
