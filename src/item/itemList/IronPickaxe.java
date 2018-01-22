package item.itemList;

import gfx.SpriteSheet;
import item.Pickaxe;
import item.Recipe;
import item.RecipeList;

public class IronPickaxe extends Pickaxe{
	
	public IronPickaxe(){
		super();
		ID = 302;
		name = "IronPickaxe";
		displayName = "Iron Pickaxe";
		col = 0xffA0A0A0;
		strength = 1;
		size = 5;
		range = 30;
		miningTier = 2;
		gfx = new SpriteSheet("/Items/Pickaxe_Iron.png");
	}
	
	public void addRecipes(RecipeList recipeList) {
		recipeList.addRecipe(new Recipe().addP(302, 1).addE(33, 30).addE(6, 10), RecipeList.C_TOOLS);
	}
}
