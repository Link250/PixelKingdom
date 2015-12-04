package gfx;

import main.Game;
import map.Map;

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
	/**== width*height*/
	public int length;
	public int lengthShadow;
	
	public ColorSheet[] csheets = new ColorSheet[3];
	
	public Screen(int width, int height, ColorSheet f, ColorSheet l, ColorSheet b){
		this.width = width;
		this.height = height;
		this.length = width*height;
		this.lengthShadow = this.length/(Game.SCALE*Game.SCALE);
		csheets[Map.LAYER_BACK] = b;
		csheets[Map.LAYER_LIQUID] = l;
		csheets[Map.LAYER_FRONT] = f;
	
		pixels = new int[width*height];
		shadow = new int[width*height/9];
		GUI = new int[width*height];

	}
	
	/**
	 * Draws a Pixel into the Shadow Layer
	 * 
	 * @param xPos 		<u>Map</u> X Position of the Pixel
	 * @param yPos 		<u>Map</u> Y Position of the Pixel
	 * @param color 	Color of the Shadow Pixel.
	 * 		<sub>		normally this is only a black Color with an Alpha Value,
	 * 					but the Shadow <i>could</i> be colored, too
	 */
	public void drawShadow(int xPos, int yPos, int color){
		xPos -= xOffset;
		yPos -= yOffset;
		shadow[xPos + yPos * width/3] = color;
	}
	
	/**
	 * Resets the Pixel on <b>(X,Y)</b> of the Main and GUI Layers
	 */
	public void resetPixel(int xPos, int yPos){
		pixels[xPos + yPos * width] = 0;
		GUI[xPos + yPos * width] = 0;
	}
	
	public void resetPixelArea(int xPos, int yPos, int width, int height){
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				pixels[xPos+x + (yPos+y) * this.width] = 0;
				GUI[xPos+x + (yPos+y) * this.width] = 0;
			}
		}
	}

	public void resetPixelAll(){
		for (int xy = 0; xy < this.length; xy++) {
			pixels[xy] = 0;
			GUI[xy] = 0;
		}
	}

	public void drawPixelScaled(int xPos, int yPos, int color){
		xPos -= xOffset;
		yPos -= yOffset;
		xPos*=3;yPos*=3;
		drawPixelArea(xPos,yPos,color,3,3,false);
	}

	public void drawPixel(int xPos, int yPos, int color){
		xPos -= xOffset;
		yPos -= yOffset;
		drawPixelArea(xPos,yPos,color,1,1,false);
	}

	public void drawGUIScaled(int xPos, int yPos, int color){
		xPos -= xOffset;
		yPos -= yOffset;
		xPos*=3;yPos*=3;
		drawPixelArea(xPos,yPos,color,3,3,true);
	}

	public void drawGUI(int xPos, int yPos, int color){
		xPos -= xOffset;
		yPos -= yOffset;
		drawPixelArea(xPos,yPos,color,1,1,true);
	}

	public void drawPixelArea(int xPos, int yPos, int color, int xSize, int ySize, boolean gui){
		double a = (color>>24)&0xff;
		if(a != 0xff){
			int r = (color>>16)&0xff, g = (color>>8)&0xff, b = color&0xff;
			int col,ro,go,bo,ao;
			for(int x = 0; x < xSize; x++){
				for(int y = 0; y < ySize; y++){
					if(xPos+x >= 0 && xPos+x < width && yPos+y >= 0 && yPos+y < height){
						col = gui ? GUI[xPos+x + (yPos+y)*width] : pixels[xPos+x + (yPos+y)*width];
						ao = (col>>24)&0xff;
						if(ao != 0){
							ro = (int)((a/255*r) + ((255-a)/255*((col>>16)&0xff)));
							go = (int)((a/255*g) + ((255-a)/255*((col>> 8)&0xff)));
							bo = (int)((a/255*b) + ((255-a)/255*((col    )&0xff)));
							if(ao != 255){
								ao = (int) (a+(255-a)/255*((col>>24)&0xff));
							}
							if(gui) GUI[xPos+x + (yPos+y)*width] = (ao<<24)|(ro<<16)|(go<<8)|bo;
							else pixels[xPos+x + (yPos+y)*width] = (ao<<24)|(ro<<16)|(go<<8)|bo;
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
	
	public void drawMaterial(int xPos, int yPos, int tile, int layer){
		int col = csheets[layer].pixels[tile];
		drawPixelScaled(xPos, yPos, col);
	}

	public void drawGUITile(int xPos, int yPos, int tile, int mirrorXY, SpriteSheet sheet, int color){
		drawTile(xPos,yPos,tile,mirrorXY,sheet,color,true);
	}
	public void drawTile(int xPos, int yPos, int tile, int mirrorXY, SpriteSheet sheet, int color){
		drawTile(xPos,yPos,tile,mirrorXY,sheet,color,false);
	}

	public void drawTile(int xPos, int yPos, int tile, int mirrorXY, SpriteSheet sheet, int color, boolean GUI){
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
								drawPixelArea(xPos+x, yPos+y, color, 1, 1, GUI);
							}else{
								drawPixelArea(xPos+x, yPos+y, sheetc, 1, 1, GUI);
							}
						}
					}
				}	
			}
		}
	}
}
