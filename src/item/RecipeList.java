package item;

import java.util.ArrayList;
import java.util.Set;

import item.Recipe.component;
import main.Game;

public class RecipeList {

	public ArrayList<Recipe> recipes = new ArrayList<Recipe>();
	public Set<Set<Recipe>> categories;
	
	public RecipeList(){
		recipes.add(new Recipe().addE(4, 10).addP(6, 30));
		recipes.add(new Recipe().addE(3, 10).addP(2, 10));
		recipes.add(new Recipe().addE(1, 50).addE(7, 20).addP(64, 20));
		recipes.add(new Recipe().addE(4, 10).addP(16, 1));
		recipes.add(new Recipe().addE(16, 5).addE(6, 10).addP(48, 5));
		recipes.add(new Recipe().addE(1, 30).addE(6, 10).addP(301, 1));
		recipes.add(new Recipe().addE(33, 30).addE(6, 10).addP(302, 1));
		recipes.add(new Recipe().addE(16, 1).addE(6, 3).addP(400, 5));
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
