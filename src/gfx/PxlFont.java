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
		System.out.println(chars.length());
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
		if(chars.indexOf('.') > -1)pointSize = size[chars.indexOf('.')];
	}
	
	public void render(int x, int y, String msg, int limit, int color, Screen screen){
		render(x, y, true, true, msg, limit, color, screen);
	}
	
	public int renderLength(String msg, int limit) {
		int xOff = 0;
		for(int i = 0; i < msg.length(); i++){
			int charIndex = chars.indexOf(msg.charAt(i));
			if(charIndex > -1){
				if(limit==0 || xOff < limit-pointSize) {
					xOff += size[charIndex]+(size[charIndex]>0 ? letterDistance : sheet.getWidth()/2);
				}else {
					break;
				}
			}
		}
		return xOff;
	}
	
	public void render(int x, int y, boolean centeredX, boolean centeredY, String msg, int limit, int color, Screen screen){
		if(msg == null) return;
		int yOff = centeredY ? sheet.getHeight()/2 : 0;
		int xOff = 0;
		if(centeredX) xOff = renderLength(msg, limit)/2;
		if(limit!=0)limit+=x;
		for(int i = 0; i < msg.length(); i++){
			int charIndex = chars.indexOf(msg.charAt(i));
			if(charIndex >= 0){
				if(limit==0 || x < limit-pointSize){
					screen.drawTileOGL(x-sizel[charIndex]-xOff, y-yOff, charIndex, sheet);
//					screen.drawGUITile(x-sizel[charIndex]-xOff, y-yOff, charIndex, 0x00, sheet, color);
					x += size[charIndex]+(size[charIndex]>0 ? letterDistance : sheet.getWidth()/2);
				}else{
					if(pointSize > 0)screen.drawTileOGL(x-sizel[charIndex]-xOff, y-yOff, chars.indexOf('.'), sheet);
//					if(pointSize > 0)screen.drawGUITile(x-sizel[charIndex]-xOff, y-yOff, chars.indexOf('.'), 0x00, sheet, color);
					break;
				}
			}
		}
	}
}
