package pixel.pixelList;

import item.Recipe;
import item.RecipeList;
import map.Map;
import pixel.UDS;
import pixel.Material;

public class Torch extends Material<UDS>{
	
	public Torch(){
		super(null);
		ID = 48;
		name = "Torch";
		displayName = "Torch";
		solidity = Map.SOLID_NONE;
		frontLightReduction = new int[] {-1, -1, -1};
		backLightReduction = new int[] {0, 0, 0};
		light = new int[] {255, 150, 100};
		requiredType = MINING_TYPE_PICKAXE;
		requiredTier = 1;
		miningResistance = 1;
		loadTexture();
	}
	
	public void addRecipes(RecipeList recipeList) {
		recipeList.addRecipe(new Recipe().addP(48, 5).addE(16, 5).addE(6, 10), RecipeList.C_PIXEL);
	}
}
