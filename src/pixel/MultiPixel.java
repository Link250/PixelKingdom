package pixel;

import java.awt.Point;

import gfx.Screen;
import gfx.SpriteSheet;
import item.ItemList;
import item.MiningTool;
import map.Map;

public class MultiPixel<UDSType extends MultiPixel.DataStorage> extends Material<UDSType> {
	int width, height;
	int[] bitmap;
	SpriteSheet sprite;
	
	public MultiPixel(UDSType uds, int width, int height) {
		super(uds);
		this.width = width;
		this.height = height;
	}

	protected void loadTexture() {
		Point size = new Point();
		this.texture = loadTexture("/MapTextures/MultiPixel/"+name+".png", size);
		sprite = new SpriteSheet(texture, size.x, size.y, size.x, size.y);
		this.bitmap = loadTexture("/MapTextures/MultiPixel/"+name+"Bitmap.png", new Point());
	}
	
	public boolean breakPixel(Map map, int x, int y, int l, MiningTool item) {
		map.spawnItemEntity(ItemList.NewItem(itemID>0 ? itemID : ID), x, y);
		return true;
	}
	
	public static class DataStorage extends UDS{
		public int xOrigin, yOrigin;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int[] getBitMap() {
		return bitmap;
	}
	
	public int render(int x, int y, int l, Map map) {
		return 0;
	}
	
	public void renderSprite(int x, int y) {
		Screen.drawMapSprite(x, y, sprite);
	}
	
	public void renderOnMouse(int x, int y) {
		Screen.drawMapSprite(x, y, sprite);
	}
}
