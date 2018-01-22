package item;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Predicate;

import static main.Game.logInfo;

public class RecipeList {
	/**recipe category**/
	public static final String C_PIXEL = "Pixel", C_TOOLS = "Tools", C_EQUIPMENT = "Equipment", C_MATERIAL = "Material", C_WEAPONS = "Weapons", C_FURNITURE = "Furniture", C_TECHNOLOGY = "Technology", C_STORAGE = "Storage";

	private Map<String, Set<Recipe>> allRecipes = new TreeMap<>();
	
	public Set<Recipe> getRecipes(String category){
		return allRecipes.get(category);
	}
	
	public Set<String> getCategories(){
		return allRecipes.keySet();
	}
	
	public Set<Recipe> getAllRecipes(){
		Set<Recipe> recipes = new TreeSet<>();
		allRecipes.values().forEach(rSet->recipes.addAll(rSet));
		return recipes;
	}
	
	public Set<Recipe> getAllRecipes(Predicate<Recipe> predicate){
		Set<Recipe> recipes = new TreeSet<>();
		allRecipes.values().forEach(rSet->rSet.stream().filter(predicate).forEach(r->recipes.add(r)));
		return recipes;
	}
	
	public void addRecipe(Recipe recipe, String category) {
		if(!allRecipes.containsKey(category)) {
			allRecipes.put(category, new TreeSet<>());
			logInfo("Added Categroy (" + category + ")");
		}
		allRecipes.get(category).add(recipe);
		logInfo("Added Recipe (" + recipe.toString() + ")");
	}

	public void removeRecipe(Recipe recipe, String category) {
		allRecipes.get(category).remove(recipe);
	}
}
