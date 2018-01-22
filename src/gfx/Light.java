package gfx;

public class Light {
	public static final short BRIGHTNESS_GREEN = 150;
	public static final short BRIGHTNESS_RED = 75;
	public static final short BRIGHTNESS_BLUE = 30;
	
	/**
	 * 
	 * @param light
	 * @return a Value from 0 to 255
	 */
	public static short getBrightness(int lightValue) {
		return (short) (
				((lightValue>>16)&0xFF)*BRIGHTNESS_RED/255+
				((lightValue>> 8)&0xFF)*BRIGHTNESS_GREEN/255+
				((lightValue    )&0xFF)*BRIGHTNESS_BLUE/255);
	}

	/**
	 * r,g and b have to be Values from 0 to 255
	 * @return the Color specified by r,g,b a Integer
	 */
	public static int getColorAsInt(short r, short g, short b) {
		return ((r<<16)&0x00ff0000) | ((g<<8)&0x0000ff00) | ((b)&0x000000ff);
	}
	
	/**
	 * r,g and b have to be Values from 0 to 255
	 * @return the Color specified by r,g,b a Integer
	 */
	public static int getColorAsInt(int r, int g, int b) {
		return ((r<<16)&0x00ff0000) | ((g<<8)&0x0000ff00) | ((b)&0x000000ff);
	}
	
	public static int getLighter(int value1, int value2) {
		return    (((value1 & 0x00ff0000) > (value2 & 0x00ff0000)) ? (value1 & 0x00ff0000) : (value2 & 0x00ff0000))
				| (((value1 & 0x0000ff00) > (value2 & 0x0000ff00)) ? (value1 & 0x0000ff00) : (value2 & 0x0000ff00))
				| (((value1 & 0x000000ff) > (value2 & 0x000000ff)) ? (value1 & 0x000000ff) : (value2 & 0x000000ff));
	}
	
	
	public static void getLighter(int[] value1, int[] value2) {
		if(value1[0] < value2[0])value1[0] = value2[0];
		if(value1[1] < value2[1])value1[1] = value2[1];
		if(value1[2] < value2[2])value1[2] = value2[2];
	}
}
