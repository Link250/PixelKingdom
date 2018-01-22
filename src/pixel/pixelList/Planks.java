package pixel.pixelList;

import pixel.UDS;
import pixel.interfaces.Burnable;
import item.Recipe;
import item.RecipeList;
import pixel.Material;

public class Planks extends Material<UDS> implements Burnable{

	public Planks(){
		super(null);
		ID = 6;
		name = "Planks";
		displayName = "Planks";
		requiredType = MINING_TYPE_PICKAXE;
		requiredTier = 1;
		miningResistance = 1;
		loadTexture();
	}
	
	public byte getBurnStrength() {return 15;}
	
	public void addRecipes(RecipeList recipeList) {
		recipeList.addRecipe(new Recipe().addP(6, 30).addE(4, 10), RecipeList.C_PIXEL);
	}
}
