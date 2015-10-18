package Multiplayer;

public class Request {
	public static final byte CLOSE_CONNECTION = 1;
	public static final byte CHUNK_DATA = 2;
	public static final byte PLAYER_DATA = 3;
	public static final byte MAP_DATA = 4;
	
	public class PLAYER{
		public static final byte NEW = 1;
		public static final byte DELETE = 2;
		public static final byte REFRESH = 3;
	}
}
