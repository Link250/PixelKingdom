package item;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import item.Recipe.Component;

public class RecipeList {
	/**recipe category**/
	public static final String C_PIXEL = "Pixel", C_TOOLS = "Tools", C_EQUIPMENT = "Equipment", C_MATERIAL = "Material", C_WEAPONS = "Weapons", C_FURNITURE = "Furniture", C_TECHNOLOGY = "Technology", C_STORAGE = "Storage";

	private ArrayList<Recipe> recipes = new ArrayList<Recipe>();
	private Map<String, Set<Recipe>> allRecipes = new TreeMap<>();
	
	public RecipeList(){
		recipes.add(new Recipe().addP(2, 10).addE(3, 10));
		recipes.add(new Recipe().addP(6, 30).addE(4, 10));
		recipes.add(new Recipe().addP(16, 1).addE(4, 10));
		recipes.add(new Recipe().addP(48, 5).addE(16, 5).addE(6, 10));
		recipes.add(new Recipe().addP(64, 20).addE(1, 50).addE(7, 20));
		recipes.add(new Recipe().addP(301, 1).addE(1, 30).addE(6, 10));
		recipes.add(new Recipe().addP(302, 1).addE(33, 30).addE(6, 10));
		recipes.add(new Recipe().addP(400, 5).addE(16, 1).addE(6, 3));
		addRecipe(new Recipe().addP(2, 10).addE(3, 10), C_PIXEL);
		addRecipe(new Recipe().addP(6, 30).addE(4, 10), C_PIXEL);
		addRecipe(new Recipe().addP(16, 1).addE(4, 10), C_PIXEL);
		addRecipe(new Recipe().addP(48, 5).addE(16, 5).addE(6, 10), C_PIXEL);
		addRecipe(new Recipe().addP(64, 20).addE(1, 50).addE(7, 20), C_PIXEL);
		addRecipe(new Recipe().addP(301, 1).addE(1, 30).addE(6, 10), C_TOOLS);
		addRecipe(new Recipe().addP(302, 1).addE(33, 30).addE(6, 10), C_TOOLS);
		addRecipe(new Recipe().addP(400, 5).addE(16, 1).addE(6, 3), C_TOOLS);
		addRecipe(new Recipe().addP(1, 1).addE(1, 1), C_EQUIPMENT);
		addRecipe(new Recipe().addP(1, 1).addE(1, 1), C_MATERIAL);
		addRecipe(new Recipe().addP(1, 1).addE(1, 1), C_WEAPONS);
		addRecipe(new Recipe().addP(1, 1).addE(1, 1), C_FURNITURE);
		addRecipe(new Recipe().addP(1, 1).addE(1, 1), C_TECHNOLOGY);
		for (int i = 0; i < 50; i++) {
			addRecipe(new Recipe().addP((int)(Math.random()*5)+1, i).addE(1, i), C_STORAGE);
		}
	}
	
	public ArrayList<Recipe> getRecipes(Class<?> type){
		ArrayList<Recipe> res = new ArrayList<Recipe>();
		for(Recipe r : recipes){
			for(Component c : r.products){
				try{
				if(type.cast(ItemList.GetItem(c.ID)) != null){
					res.add(r);
					break;
				}}catch(ClassCastException e){}
			}
		}
		return res;
	}
	
	public Set<Recipe> getRecipes(String category){
		return allRecipes.get(category);
	}
	
	public Set<String> getCategories(){
		return allRecipes.keySet();
	}
	
	public void addRecipe(Recipe recipe, String category) {
		if(!allRecipes.containsKey(category))allRecipes.put(category, new TreeSet<>());
		allRecipes.get(category).add(recipe);
	}

	public void removeRecipe(Recipe recipe, String category) {
		allRecipes.get(category).remove(recipe);
	}
}
