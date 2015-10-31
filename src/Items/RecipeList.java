package Items;

import java.util.ArrayList;

import Items.Recipe.component;
import Main.Game;

public class RecipeList {

	public ArrayList<Recipe> recipes = new ArrayList<Recipe>();
	
	public RecipeList(){
		recipes.add(0, new Recipe());
			recipes.get(0).addE(4, 10).addP(6, 30);
		recipes.add(0, new Recipe());
			recipes.get(0).addE(3, 10).addP(2, 10);
		recipes.add(0, new Recipe());
			recipes.get(0).addE(1, 50).addE(7, 20).addP(64, 20);
		recipes.add(0, new Recipe());
			recipes.get(0).addE(4, 10).addP(16, 1);
		recipes.add(0, new Recipe());
			recipes.get(0).addE(16, 5).addE(6, 10).addP(48, 5);
		recipes.add(0, new Recipe());
			recipes.get(0).addE(1, 30).addE(6, 10).addP(301, 1);
		recipes.add(0, new Recipe());
			recipes.get(0).addE(33, 30).addE(6, 10).addP(302, 1);
		recipes.add(0, new Recipe());
			recipes.get(0).addE(16, 1).addE(6, 3).addP(400, 5);
		for(Recipe r : recipes){
			String s = "Item(s) ";
			for(component c : r.educts){s += "["+ItemList.GetItem(c.ID).name+" x "+c.n+"] ";}
			s += "can be crafted to ";
			for(component c : r.products){s += "["+c.n+" x "+ItemList.GetItem(c.ID).name+"] ";}
			Game.logInfo(s);
		}
	}
	
	public ArrayList<Recipe> getRecipes(Class<?> type){
		ArrayList<Recipe> res = new ArrayList<Recipe>();
		for(Recipe r : recipes){
			for(component c : r.products){
				try{
				if(type.cast(ItemList.GetItem(c.ID)) != null){
					res.add(r);
					break;
				}}catch(ClassCastException e){}
			}
		}
		return res;
	}
}
