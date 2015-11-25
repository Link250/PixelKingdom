package multiplayer;

public class Request {
	public static final byte END_OF_STREAM = (byte)	0xff;
	public static final byte NONE = 				0x00;
	public static final byte CLOSE_CONNECTION = 	0x01;
	
	public static final byte PLAYER_DATA = 			0x02;
	public static final byte PLAYER_COLOR = 		0x12;
	
	public static final byte MAP_DATA = 			0x03;
	public static final byte MAP_CHUNK_DATA = 		0x13;
	public static final byte MAP_UPDATE_PXL = 		0x23;
	public static final byte MAP_UPDATE_AD = 		0x33;
	
	public class PLAYER{
		public static final byte NEW = 1;
		public static final byte DELETE = 2;
		public static final byte COLOR = 3;
		public static final byte REFRESH = 4;
	}
}
