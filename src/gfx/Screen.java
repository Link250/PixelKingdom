package gfx;

import map.Map;

public class Screen {

	public static final int SHADOW_SCALE = 2;
	public static final int MAP_SCALE = 2;
	public static int MAP_ZOOM = 2;

	public int[] pixels;
	public int[] shadow;
	public int[] GUI;
	
	public int xOffset = 0;
	public int yOffset = 0;
	
	public int width;
	public int height;
	/**== width*height*/
	public int length;
	public int lengthMap;
	public int lengthShadow;
	
	public ColorSheet[] csheets = new ColorSheet[3];
	
	public Screen(int width, int height, ColorSheet f, ColorSheet l, ColorSheet b){
		this.width = width;
		this.height = height;
		this.length = width*height;
		this.lengthMap = width/MAP_ZOOM*height/MAP_ZOOM;
		this.lengthShadow = this.lengthMap/(SHADOW_SCALE*SHADOW_SCALE);
		csheets[Map.LAYER_BACK] = b;
		csheets[Map.LAYER_LIQUID] = l;
		csheets[Map.LAYER_FRONT] = f;
	
		GUI = new int[this.length];
		pixels = new int[this.lengthMap];
		shadow = new int[this.lengthShadow];

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
		shadow[xPos + yPos * width/SHADOW_SCALE/MAP_ZOOM] = color;
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
		for (int xy = 0; xy < this.lengthMap; xy++) {
			pixels[xy] = 0;
		}
		for (int xy = 0; xy < this.length; xy++) {
			GUI[xy] = 0;
		}
	}

	public void drawMapPixelScaled(int xPos, int yPos, int color){
		xPos -= xOffset;
		yPos -= yOffset;
		xPos*=MAP_SCALE;yPos*=MAP_SCALE;
		drawPixelArea(xPos,yPos,MAP_SCALE,MAP_SCALE,color,false);
	}

	public void drawMapPixel(int xPos, int yPos, int color){
		xPos -= xOffset;
		yPos -= yOffset;
		drawPixel(xPos,yPos,color,false);
	}

	/**
	 * This Mehtod should be used if you want to draw a Pixel relative to the Map and with the zoomlevel of the Map
	 * @param xPos
	 * @param yPos
	 * @param color
	 */
	public void drawGUIPixelScaled(int xPos, int yPos, int color){
		xPos -= xOffset;
		yPos -= yOffset;
		xPos*=MAP_SCALE*MAP_ZOOM;yPos*=MAP_SCALE*MAP_ZOOM;
		drawPixelArea(xPos,yPos,MAP_SCALE*MAP_ZOOM,MAP_SCALE*MAP_ZOOM,color,true);
	}

	public void drawGUIPixel(int xPos, int yPos, int color){
		xPos -= xOffset;
		yPos -= yOffset;
		drawPixel(xPos,yPos,color,true);
	}

	public void drawGUIPixelArea(int xPos, int yPos, int width, int height, int color){
		xPos -= xOffset;
		yPos -= yOffset;
		drawPixelArea(xPos,yPos,width,height,color,true);
	}

	public void drawGUIPixelBorder(int xPos, int yPos, int width, int height, int thickness, int color){
		xPos -= xOffset;
		yPos -= yOffset;
		drawPixelArea(xPos					,yPos					,width		,thickness			,color,true);
		drawPixelArea(xPos					,yPos+height-thickness	,width		,thickness			,color,true);
		drawPixelArea(xPos					,yPos+thickness			,thickness	,height-thickness*2	,color,true);
		drawPixelArea(xPos+width-thickness	,yPos+thickness			,thickness	,height-thickness*2	,color,true);
	}

	private void drawPixelArea(int xPos, int yPos, int xSize, int ySize, int color, boolean gui){
		int width = this.width;
		int height = this.height;
		if(!gui) { width /= MAP_ZOOM; height /= MAP_ZOOM;}
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
	
	private void drawPixel(int x, int y, int color, boolean gui){
		int width = this.width;
		int height = this.height;
		if(!gui) { width /= MAP_ZOOM; height /= MAP_ZOOM;}
		double a = (color>>24)&0xff;
		if(a != 0xff){
			int r = (color>>16)&0xff, g = (color>>8)&0xff, b = color&0xff;
			int col,ro,go,bo,ao;
			if(x >= 0 && x < width && y >= 0 && y < height){
				col = gui ? GUI[x + (y)*width] : pixels[x + (y)*width];
				ao = (col>>24)&0xff;
				if(ao != 0){
					ro = (int)((a/255*r) + ((255-a)/255*((col>>16)&0xff)));
					go = (int)((a/255*g) + ((255-a)/255*((col>> 8)&0xff)));
					bo = (int)((a/255*b) + ((255-a)/255*((col    )&0xff)));
					if(ao != 255){
						ao = (int) (a+(255-a)/255*((col>>24)&0xff));
					}
					if(gui) GUI[x + (y)*width] = (ao<<24)|(ro<<16)|(go<<8)|bo;
					else pixels[x + (y)*width] = (ao<<24)|(ro<<16)|(go<<8)|bo;
				}else{
					if(gui) GUI[x + (y)*width] = color;
					else pixels[x + (y)*width] = color;
				}
			}
		}else{
			if(x >= 0 && x < width && y >= 0 && y < height){
				if(gui) GUI[x + (y)*width] = color;
				else pixels[x + (y)*width] = color;
			}
		}
	}
	
	public void drawMaterial(int xPos, int yPos, int tile, int layer){
		int col = csheets[layer].pixels[tile];
		drawMapPixelScaled(xPos, yPos, col);
	}

	/**
	 * 
	 * @param xPos on the Screen
	 * @param yPos on the Screen
	 * @param tile
	 * @param mirrorXY values : 0x00 => no mirror ; 0x10 => X-mirror ; 0x01 => Y-mirror ; 0x11 => X- and Y-mirror ; 
	 * @param sheet
	 * @param color
	 */
	public void drawGUITile(int xPos, int yPos, int tile, int mirrorXY, SpriteSheet sheet, int color){
		drawTile(xPos,yPos,tile,mirrorXY,sheet,color,true);
	}
	
	/**
	 * 
	 * @param xPos on the Map
	 * @param yPos on the Map
	 * @param tile
	 * @param mirrorXY values : 0x00 => no mirror ; 0x10 => X-mirror ; 0x01 => Y-mirror ; 0x11 => X- and Y-mirror ; 
	 * @param sheet
	 * @param color
	 */
	public void drawMapTile(int xPos, int yPos, int tile, int mirrorXY, SpriteSheet sheet, int color){
		drawTile(xPos-xOffset,yPos-yOffset,tile,mirrorXY,sheet,color,false);
	}

	private void drawTile(int xPos, int yPos, int tile, int mirrorXY, SpriteSheet sheet, int color, boolean GUI){
		if(!GUI) {xPos*=MAP_SCALE;yPos*=MAP_SCALE;}
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
							drawPixel(xPos+x, yPos+y, ((sheetc & 0xffffff)== 0xff00ff) ? color : sheetc, GUI);
						}
					}
				}	
			}
		}
	}
}
