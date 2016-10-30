package item;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class RecipeList {
	/**recipe category**/
	public static final String C_PIXEL = "Pixel", C_TOOLS = "Tools", C_EQUIPMENT = "Equipment", C_MATERIAL = "Material", C_WEAPONS = "Weapons", C_FURNITURE = "Furniture", C_TECHNOLOGY = "Technology", C_STORAGE = "Storage";

	private Map<String, Set<Recipe>> allRecipes = new TreeMap<>();
	
	public RecipeList(){
		addRecipe(new Recipe().addP(2, 10).addE(3, 10), C_PIXEL);
		addRecipe(new Recipe().addP(6, 30).addE(4, 10), C_PIXEL);
		addRecipe(new Recipe().addP(16, 1).addE(4, 10), C_PIXEL);
		addRecipe(new Recipe().addP(48, 5).addE(16, 5).addE(6, 10), C_PIXEL);
		addRecipe(new Recipe().addP(64, 20).addE(1, 50).addE(7, 20), C_PIXEL);
		addRecipe(new Recipe().addP(301, 1).addE(1, 30).addE(6, 10), C_TOOLS);
		addRecipe(new Recipe().addP(302, 1).addE(33, 30).addE(6, 10), C_TOOLS);
		addRecipe(new Recipe().addP(400, 5).addE(16, 1).addE(6, 3), C_TOOLS);
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
