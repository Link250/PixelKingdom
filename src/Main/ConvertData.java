package Main;

import java.util.ArrayList;

public class ConvertData {

	public static int B2I(ArrayList<Byte> file){
		return 	(file.remove(0)<<24)&0xff000000|
				(file.remove(0)<<16)&0x00ff0000|
				(file.remove(0)<< 8)&0x0000ff00|
				(file.remove(0)<< 0)&0x000000ff;
	}
	
	public static int B2I(byte[] b, int i){
		return 	(b[i  ]<<24)&0xff000000|
				(b[i+1]<<16)&0x00ff0000|
				(b[i+2]<< 8)&0x0000ff00|
				(b[i+3]<< 0)&0x000000ff;
	}

	public static int B2I(byte b1, byte b2, byte b3, byte b4){
		return 	(b1<<24)&0xff000000|
				(b2<<16)&0x00ff0000|
				(b3<< 8)&0x0000ff00|
				(b4<< 0)&0x000000ff;
	}

	public static short B2S(ArrayList<Byte> file){
		return (short) ((file.remove(0)<< 8)&0xff00|
						(file.remove(0)<< 0)&0x00ff);
	}
	
	public static short B2S(byte[] b, int i){
		return (short) ((b[i  ]<<8)&0xff00|
						(b[i+1]   )&0x00ff);
	}

	public static short B2S(byte b1, byte b2){
		return (short) ((b1<<8)&0xff00|
						(b2<<0)&0x00ff);
	}

	public static void I2B(byte[] b, int i, int n){
		b[i+0] = (byte) ((n>>24)     );
		b[i+1] = (byte) ((n>>16)&0xff);
		b[i+2] = (byte) ((n>>8 )&0xff);
		b[i+3] = (byte) ((n    )&0xff);
	}

	public static void I2B(ArrayList<Byte> file, int n){
		file.add((byte) ((n>>24)     ));
		file.add((byte) ((n>>16)&0xff));
		file.add((byte) ((n>>8 )&0xff));
		file.add((byte) ((n    )&0xff));
	}
	
	public static byte[] I2B(int n){
		return new byte[] {
		(byte) ((n>>24)     ),
		(byte) ((n>>16)&0xff),
		(byte) ((n>>8 )&0xff),
		(byte) ((n    )&0xff)};
	}

	public static void S2B(byte[] b, int i, short n){
		b[i  ] = (byte) ((n>>8)     );
		b[i+1] = (byte) ((n   )&0xff);
	}

	public static void S2B(ArrayList<Byte> file, short n){
		file.add((byte) ((n>>8)     ));
		file.add((byte) ((n   )&0xff));
	}
	
	public static byte[] S2B(short n){
		return new byte[] {
		(byte) ((n>>8)     ),
		(byte) ((n   )&0xff)};
	}

}
