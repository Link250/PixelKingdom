package gfx;

public class Screen {

	public static final int MAP_WIDTH = 64;
	public static final int MAP_WIDTH_MASK = MAP_WIDTH-1;
	
	public int[] pixels;
	public int[] shadow;
	public int[] GUI;
	
	public int xOffset = 0;
	public int yOffset = 0;
	
	public int width;
	public int height;
	
	public ColorSheet[] csheets = new ColorSheet[3];
	
	public Screen(int width, int height, ColorSheet f, ColorSheet m, ColorSheet b){
		this.width = width;
		this.height = height;
		csheets[0] = f;
		csheets[1] = m;
		csheets[2] = b;
	
		pixels = new int[width*height];
		shadow = new int[width*height/9];
		GUI = new int[width*height];

	}
	
	public void renderShadow(int xPos, int yPos, int color){
		xPos -= xOffset;
		yPos -= yOffset;
		shadow[xPos + yPos * width/3] = color;
	}
	
	public void resetPixel(int xPos, int yPos){
		pixels[xPos + yPos * width] = 0;
		GUI[xPos + yPos * width] = 0;
	}

	public void renderPixelScaled(int xPos, int yPos, int color){
		xPos -= xOffset;
		yPos -= yOffset;
		xPos*=3;yPos*=3;
		renderPixelArea(xPos,yPos,color,3,3,false);
	}

	public void renderPixel(int xPos, int yPos, int color){
		xPos -= xOffset;
		yPos -= yOffset;
		renderPixelArea(xPos,yPos,color,1,1,false);
	}

	public void renderGUIScaled(int xPos, int yPos, int color){
		xPos -= xOffset;
		yPos -= yOffset;
		xPos*=3;yPos*=3;
		renderPixelArea(xPos,yPos,color,3,3,true);
	}

	public void renderGUI(int xPos, int yPos, int color){
		xPos -= xOffset;
		yPos -= yOffset;
		renderPixelArea(xPos,yPos,color,1,1,true);
	}

	public void renderPixelArea(int xPos, int yPos, int color, int xSize, int ySize, boolean gui){
		double a = color>>>24;
		if(a != 255){
			int r = color>>>16&0x00ff, g = color>>>8&0x0000ff, b = color&0x000000ff;
			int col,ro,go,bo,ao;
			for(int x = 0; x < xSize; x++){
				for(int y = 0; y < ySize; y++){
					if(xPos+x >= 0 && xPos+x < width && yPos+y >= 0 && yPos+y < height){
						if(gui) col = GUI[xPos+x + (yPos+y)*width];
						else col = pixels[xPos+x + (yPos+y)*width];
						ao = col>>>24&0xff;
						if(ao>0){
							ro = (int)(a/255*r) + (int)((255-a)/255*(col>>>16&0x00ff));
							go = (int)(a/255*g) + (int)((255-a)/255*(col>>>8&0x0000ff));
							bo = (int)(a/255*b) + (int)((255-a)/255*(col&0x000000ff));
							if(ao == 255){
								ao = 255;
							}else{
								ao = (int) (a+(255-a)/255*(col>>>24&0xff));
							}
							if(gui) GUI[xPos+x + (yPos+y)*width] = (ao<<24)+(ro<<16)+(go<<8)+bo;
							else pixels[xPos+x + (yPos+y)*width] = (ao<<24)+(ro<<16)+(go<<8)+bo;
						}else{
							if(gui) GUI[xPos+x + (yPos+y)*width] = color;
							else pixels[xPos+x + (yPos+y)*width] = color;
						}
					}
				}
			}
		}else{
			for(int x = 0; x < xSize; x++){
				for(int y = 0; y < ySize; y++){
					if(xPos+x >= 0 && xPos+x < width && yPos+y >= 0 && yPos+y < height){
						if(gui) GUI[xPos+x + (yPos+y)*width] = color;
						else pixels[xPos+x + (yPos+y)*width] = color;
					}
				}
			}
		}
	}
	
	public void renderMaterial(int xPos, int yPos, int tile, int layer){
		int col = csheets[layer-1].pixels[tile];
		renderPixelScaled(xPos, yPos, col);
	}

	public void renderGUITile(int xPos, int yPos, int tile, int mirrorXY, SpriteSheet sheet, int color){
		renderTile(xPos,yPos,tile,mirrorXY,sheet,color,true);
	}
	public void renderTile(int xPos, int yPos, int tile, int mirrorXY, SpriteSheet sheet, int color){
		renderTile(xPos,yPos,tile,mirrorXY,sheet,color,false);
	}

	public void renderTile(int xPos, int yPos, int tile, int mirrorXY, SpriteSheet sheet, int color, boolean GUI){
		xPos -= xOffset;
		yPos -= yOffset;
		xPos*=3;yPos*=3;
		int xTile = tile % (sheet.width/sheet.tileWidth);
		int yTile = tile / (sheet.width/sheet.tileWidth);
		int ysheet,xsheet,sheetc;
		
		for(int y = 0; y < sheet.tileHeight; y++){
			if(y + yPos > 0 & y + yPos < height){
				ysheet = y;
				if((mirrorXY & 0x01) > 0) ysheet = sheet.tileHeight-1-y;
				for(int x = 0; x < sheet.tileWidth; x++){
					if(x + xPos > 0 & x + xPos <= width){
						xsheet = x;
						if((mirrorXY & 0x10) > 0) xsheet = sheet.tileWidth-1-x;
						sheetc = sheet.pixels[(xTile*sheet.tileWidth+xsheet) + (yTile*sheet.tileHeight+ysheet)*sheet.width];
						if(sheetc>>24 != 0){
							if((sheetc & 0xffffff)== 0xff00ff){
								renderPixelArea(xPos+x, yPos+y, color, 1, 1, GUI);
							}else{
								renderPixelArea(xPos+x, yPos+y, sheetc, 1, 1, GUI);
							}
						}
					}
				}	
			}
		}
	}
}
