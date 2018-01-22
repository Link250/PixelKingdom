package pixel.pixelList;

import pixel.UDS;
import item.Recipe;
import item.RecipeList;
import pixel.Material;

public class Earth extends Material<UDS>{

	public Earth(){
		super(null);
		ID = 2;
		name = "Earth";
		displayName = "Earth";
		requiredType = MINING_TYPE_PICKAXE;
		requiredTier = 1;
		miningResistance = 1;
		loadTexture();
	}
	
	public void addRecipes(RecipeList recipeList) {
		recipeList.addRecipe(new Recipe().addP(2, 10).addE(3, 10), RecipeList.C_PIXEL);
	}
}
