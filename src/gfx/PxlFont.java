package gfx;

public class PxlFont {
	private String chars;
	private int[] size;
	private int[] sizel;
	private SpriteSheet sheet;
	private int pointSize = 0;
	private int letterDistance = 0;
	
	public PxlFont(SpriteSheet sheet, String chars, int sizex, int sizey, int letterDistance){
		this.sheet = sheet;
		this.chars = chars;
		this.letterDistance = letterDistance;
		size = new int[chars.length()];
		sizel = new int[chars.length()];
		for(int i = 0; i < this.chars.length(); i++){
			int xs,xe;
			int pixelfound = 0;
			for(xs = 0; xs < sheet.getWidth() && (pixelfound == 0); xs++){
				pixelfound = 0;
				for(int y = 0; y < sheet.getHeight(); y++){
					if((sheet.getPixels(i)[xs + y*sheet.getWidth()] & 0xffffff)!= 0x000000){
						pixelfound++;
					}
				}
			}
			for(xe = xs; xe < sheet.getWidth() && (pixelfound > 0); xe++){
				pixelfound = 0;
				for(int y = 0; y < sheet.getHeight(); y++){
					if((sheet.getPixels(i)[xe + y*sheet.getWidth()] & 0xffffff)!= 0x000000){
						pixelfound++;
					}
				}
			}
			size[i] = (xe-xs);
			sizel[i] = (xs);
		}
		if(chars.indexOf('.') > -1)pointSize = size[chars.indexOf('.')] + letterDistance;
	}
	
	public void render(int x, int y, String msg, int limit, int color){
		render(x, y, true, true, msg, limit, color);
	}
	
	public int renderLength(String msg, int limit) {
		int xOff = 0;
		int charWidth = 0;
		for(int i = 0; i < msg.length(); i++){
			int charIndex = chars.indexOf(msg.charAt(i));
			if(charIndex > -1){
				charWidth = size[charIndex]+(size[charIndex]>0 ? letterDistance : sheet.getWidth()/2);
				if(limit==0 || xOff < limit-pointSize-charWidth) {
					xOff += charWidth;
				}else {
					xOff+= pointSize;
					break;
				}
			}
		}
		return xOff;
	}
	
	public void render(int x, int y, boolean centeredX, boolean centeredY, String msg, int limit, int color){
		if(msg == null) return;
		int yOff = centeredY ? sheet.getHeight()/2 : 0;
		int xOff = centeredX ? renderLength(msg, limit)/2 : 0;
		int charWidth = 0;
		if(limit!=0)limit+=x;
		for(int i = 0; i < msg.length(); i++){
			int charIndex = chars.indexOf(msg.charAt(i));
			if(charIndex >= 0){
				charWidth = size[charIndex]+(size[charIndex]>0 ? letterDistance : sheet.getWidth()/2);
				if(limit==0 || x < limit-pointSize-charWidth){
					Screen.drawGUISprite(x-sizel[charIndex]-xOff, y-yOff, sheet, charIndex, false, false, color);
					x += charWidth;
				}else{
					if(pointSize > 0)Screen.drawGUISprite(x-sizel[charIndex]-xOff, y-yOff, sheet, chars.indexOf('.'), false, false, color);
					break;
				}
			}
		}
	}
}
