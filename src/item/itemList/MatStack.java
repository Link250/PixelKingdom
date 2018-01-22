package item.itemList;

import java.util.ArrayList;

import dataUtils.conversion.ConvertData;
import entities.Player;
import gfx.Mouse;
import gfx.SpriteSheet;
import gfx.Mouse.MouseType;
import item.Item;
import item.RecipeList;
import main.MouseInput;
import map.Map;
import pixel.PixelList;

public class MatStack extends Item{
	private byte buildsize = 2;
	
	public MatStack(){
		ID = 1;
		name = PixelList.GetMat((byte)ID).getName();
		displayName = PixelList.GetMat((byte)ID).getDisplayName();
		stack  = 1;
		stackMax  = 1024;
		gfx = new SpriteSheet("/Items/Material.png");
		gfxs = new SpriteSheet("/Items/Materialh.png");
		col = PixelList.GetMat((byte)ID).getColor();
	}

	public void holdItem(Player plr, Map map) {
		MouseInput.mouse.refresh();
		anim = 12;
		int l = Map.LAYER_FRONT;if(!MouseInput.mousel.isPressed())l = Map.LAYER_BACK;
		if((MouseInput.mousel.isPressed() | MouseInput.mouser.isPressed()) && !plr.iscrouching
		&& Math.sqrt(Math.pow(plr.x-(MouseInput.mouse.getMapX()),2)+Math.pow(plr.y-(MouseInput.mouse.getMapY()), 2)) <= 25){
			for(int y = -buildsize+1; y < buildsize; y++){
				for(int x = -buildsize+1; x < buildsize; x++){
					if(stack>0 && map.getID(MouseInput.mouse.getMapX()+x, MouseInput.mouse.getMapY()+y,l)==0){
						map.setID(MouseInput.mouse.getMapX()+x,MouseInput.mouse.getMapY()+y,l,ID);
						stack--;
					}
					if(stack==0){
						plr.delItem(this);
						Mouse.mouseType=MouseType.DEFAULT;
					}
				}
			}
		}
		if(MouseInput.mouse.scrolled!=0){
			buildsize -= MouseInput.mouse.getScroll();
			if(buildsize < 1)buildsize = 1;
			if(buildsize > 10)buildsize = 10;
			Mouse.mousesize=buildsize;
		}
	}
	
	public void setMat(int mat){
		ID = mat;
		name = PixelList.GetMat((byte)ID).getName();
		displayName = PixelList.GetMat((byte)ID).getDisplayName();
		col = PixelList.GetMat((byte)ID).getColor();
	}

	public void setMouse() {
		Mouse.mouseType=MouseType.BUILDING;
		Mouse.mousesize=buildsize;
	}

	public void save(ArrayList<Byte> file) {
		ConvertData.I2B(file, ID);
		ConvertData.I2B(file, stack);
	}

	public void load(ArrayList<Byte> file) {
		stack = ConvertData.B2I(file);
	}
	
/*	private SpriteSheet loadTexture() {
		SpriteSheet sprite = new SpriteSheet("/Items/Material.png");
		int[] pixels = sprite.getPixels(0);
		int w = sprite.getWidth(), h = sprite.getHeight();
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				if((pixels[x + y * w] & 0xffffff) == 0xff00ff) {
					pixels[x + y * w] = PixelList.GetMat(ID).getColor(x, y);
				}
			}
		}
		sprite.setPixels(pixels, w, h, w, h);
		return sprite;
	}*/
	
	public void addRecipes(RecipeList recipeList) {
		PixelList.GetMat(ID).addRecipes(recipeList);
	}
}
