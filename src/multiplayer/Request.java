package multiplayer;

public class Request {
	public static final byte END_OF_STREAM = (byte)	0xff;
	public static final byte NONE = 				0x00;
	public static final byte CLOSE_CONNECTION = 	0x01;
	
	public static final byte PLAYER_DATA = 			0x10;
	public static final byte PLAYER_NEW = 			0x11;
	public static final byte PLAYER_DELETE = 		0x12;
	public static final byte PLAYER_COLOR = 		0x13;
	public static final byte PLAYER_REFRESH = 		0x14;
	
	public static final byte MAP_DATA = 			0x20;
	public static final byte CHUNK_DATA = 			0x21;
	public static final byte MAP_UPDATE_PXL = 		0x22;
	public static final byte MAP_UPDATE_UDS = 		0x23;
}
