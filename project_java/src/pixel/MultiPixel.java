package pixel;

import java.awt.Point;
import java.io.IOException;

import dataUtils.conversion.InConverter;
import dataUtils.conversion.OutConverter;
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
	
	public static class DataStorage extends UDS{
		public int xOrigin, yOrigin;
		
		public void save(OutConverter out, boolean header, boolean saveAll) {
			if(saveAll)	super.save(out, header);
			else {
				try {
					if(header)out.writeShort((short) 0x7fff);
					out.writeInt(xOrigin);
					out.writeInt(yOrigin);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		public UDS load(InConverter in, boolean loadAll) {
			if(loadAll)return super.load(in);
			else {
				try {
					this.xOrigin = in.readInt();
					this.yOrigin = in.readInt();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return this;
			}
		}
	}
}
