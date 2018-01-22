package pixel.pixelList;

import gfx.Screen;
import item.Recipe;
import item.RecipeList;
import map.Map;
import pixel.interfaces.Heatable;
import pixel.Material;

public class Oven extends Material<Heatable.DataStorage> implements Heatable{
	short cooldown = 1;
	int cdFrequency = 10;
	short heatExchange = 50;
	
	public Oven(){
		super(new DataStorage());
		ID = 64;
		name = "Oven";
		displayName = "Oven";
		requiredType = MINING_TYPE_PICKAXE;
		requiredTier = 1;
		miningResistance = 1;
		loadTexture();
	}
	
	public boolean tick(int x, int y, int l, Map map, int numTick) {
		return Heatable.super.tick(x, y, l, map, numTick);
	}
	
	public int getMeltingPoint() {return 1888;}
	
	public int getHeatExchange() {return heatExchange;}
	
	public int getCoolDown() {return cooldown;}
	
	public int getCDFrequency() {return cdFrequency;}
	
	public int[] tickLight(int x, int y, int l, Map map) {
		float strength = map.<DataStorage>getUDS(x, y, l).heat/((float)getMeltingPoint());
		return new int[] {(int) (strength*255), (int) (strength*150), (int) (strength*100)};
	}
	
	public int render(int x, int y, int l, Map map) {
		int r = (int) (map.<DataStorage>getUDS(x, y, l).heat/(((double)getMeltingPoint())/127));if(r>255)r=255;
		return Screen.combineColors(texture[x%textureWidth + (y%textureHeight)*textureWidth], 0x00ff0000 | (r<<24));
	}
	
	public void addRecipes(RecipeList recipeList) {
		recipeList.addRecipe(new Recipe().addP(64, 20).addE(1, 50).addE(7, 20), RecipeList.C_PIXEL);
	}
}
