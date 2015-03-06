package Main;

import java.util.ArrayList;

public class ConvertData {

	public static int B2I(byte b1, byte b2, byte b3, byte b4){
		return (b1<<24)&0xff000000|(b2<<16)&0x00ff0000|(b3<< 8)&0x0000ff00|(b4<< 0)&0x000000ff;
	}

	public static int B2I(ArrayList<Byte> file){
		return (file.remove(0)<<24)&0xff000000|(file.remove(0)<<16)&0x00ff0000|(file.remove(0)<< 8)&0x0000ff00|(file.remove(0)<< 0)&0x000000ff;
	}

	public static short B2S(byte b1, byte b2){
		return (short) ((b1<<8)&0xff00|(b2<<0)&0x00ff);
	}

	public static short B2S(ArrayList<Byte> file){
		return (short) ((file.remove(0)<< 8)&0xff00|(file.remove(0)<< 0)&0x00ff);
	}

	public static byte[] I2B(int n){
		byte[] b = new byte[4];
		b[0] = (byte) ((n&0xff000000)>>24);
		b[1] = (byte) ((n&0x00ff0000)>>16);
		b[2] = (byte) ((n&0x0000ff00)>>8);
		b[3] = (byte) ((n&0x000000ff)>>0);
		return b;
	}

	public static void I2B(ArrayList<Byte> file, int n){
		file.add((byte) ((n&0xff000000)>>24));
		file.add((byte) ((n&0x00ff0000)>>16));
		file.add((byte) ((n&0x0000ff00)>>8));
		file.add((byte) ((n&0x000000ff)>>0));
	}

	public static byte[] S2B(short n){
		byte[] b = new byte[2];
		b[0] = (byte) ((n&0xff00)>>8);
		b[1] = (byte) ((n&0x00ff)>>0);
		return b;
	}

	public static void S2B(ArrayList<Byte> file, short n){
		file.add((byte) ((n&0xff00)>>8));
		file.add((byte) ((n&0x00ff)>>0));
	}

}
