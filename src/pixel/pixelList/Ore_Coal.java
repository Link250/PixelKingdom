package pixel.pixelList;

import item.Recipe;
import item.RecipeList;
import pixel.Material;
import pixel.UDS;
import pixel.interfaces.Burnable;

public class Ore_Coal extends Material<UDS> implements Burnable{

	public Ore_Coal(){
		super(null);
		ID = 16;
		name = "CoalOre";
		displayName = "Coal Ore";
		requiredType = MINING_TYPE_PICKAXE;
		requiredTier = 1;
		miningResistance = 1.8;
		loadTexture();
	}
	
	public byte getBurnStrength() {return 50;}
	
	public void addRecipes(RecipeList recipeList) {
		recipeList.addRecipe(new Recipe().addP(16, 1).addE(4, 10), RecipeList.C_PIXEL);
	}
}
