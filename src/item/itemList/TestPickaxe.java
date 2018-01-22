package item.itemList;

import gfx.SpriteSheet;
import item.Pickaxe;
import item.Recipe;
import item.RecipeList;

public class TestPickaxe extends Pickaxe{
	
	public TestPickaxe(){
		super();
		ID = 303;
		name = "TingruPickaxe";
		displayName = "Tingaxe";
		col = 0xff808080;
		strength = 123;
		size = 127;
		range = 300;
		miningTier = 99;
		gfx = new SpriteSheet("/Items/Pickaxe_Stone.png");
	}
	
	@Override
	public void addRecipes(RecipeList recipeList) {
		recipeList.addRecipe(new Recipe().addP(303, 1).addE(1, 10).addE(6, 10), RecipeList.C_TOOLS);
	}
}
