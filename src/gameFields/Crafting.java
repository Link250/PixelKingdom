package gameFields;

import java.util.Map;
import java.util.TreeMap;

import dataUtils.PArea;
import entities.Player;
import gfx.SpriteSheet;
import gui.Button;
import gfx.Screen;
import item.*;
import main.MainConfig.GameFields;
import main.MouseInput;
import main.Game;

public class Crafting extends GameField {
	
	private SpriteSheet back = new SpriteSheet("/Crafting/Back.png");
	private Button searchButton;
	private Button backButton;
	private String selectedCategory = null;
	private String mouseoverCategory = null;
	private Recipe mouseoverRecipe = null;
	private PArea fieldsArea;
	private boolean searching = false;
	private Map<String, ItemField> categories = new TreeMap<>();
	private Map<Recipe, ItemField> recipes = new TreeMap<>();
	
	public Player plr;
	
	public Crafting(Player plr){
		super(GameFields.Field_Crafting);
		this.plr = plr;
		this.field.setSize(back.getWidth(), back.getHeight());
		this.fieldTop.setSize(back.getWidth()-2*36, 36);
		this.fieldsArea = new PArea(field.x, field.y+fieldTop.height+2, 226, 226);
		this.plr.recipelist.getCategories().forEach((s) -> this.categories.put(s, new CategoryField(s)));
		this.searchButton = new Button(field.x+field.width-36, field.y, 36, 36, false, false);
		this.searchButton.gfxData("/Crafting/SearchButton.png", false);
		this.backButton = new Button(field.x, field.y, 36, 36, false, false);
		this.backButton.gfxData("/Crafting/BackButton.png", false);
		this.placeFields();
	}
	
	private void placeFields() {
		int x = this.field.x, y = this.field.y+this.fieldTop.height+2, n = 0;
		this.fieldsArea.setPosition(x, y);
		this.searchButton.SetPos(field.x+field.width-36, field.y, false, false);
		this.backButton.SetPos(field.x, field.y, false, false);
		if(selectedCategory == null)for (ItemField field : categories.values()) {
			field.setPosition(x+(n%4)*58, y+(n/4)*58, false, false);
			n++;
		}else for (ItemField field : recipes.values()) {
			field.setPosition(x+(n%6)*38, y+(n/6)*38, false, false);
			n++;
		}
	}
	
	public void tick() {
		if(Drag())placeFields();
		this.fieldTop.setPosition(field.x+36, field.y);
		this.mouseoverCategory = null;
		this.mouseoverRecipe = null;
		if(mouseover()) {
			if(fieldsArea.contains(MouseInput.mouse.x, MouseInput.mouse.y)) {
				if(selectedCategory == null)this.categories.entrySet().forEach((e)->{
					if(e.getValue().field.contains(MouseInput.mouse.x, MouseInput.mouse.y)) {
						if(e.getValue().field.contains(MouseInput.mousel.x, MouseInput.mousel.y) && MouseInput.mousel.click()) {
							selectedCategory = e.getKey();
							recipes = new TreeMap<>();
							plr.recipelist.getRecipes(selectedCategory).forEach((r) -> recipes.put(r, new RecipeField(r)));
							placeFields();
						}else
							mouseoverCategory = e.getKey();
					}
				});
				else this.recipes.entrySet().forEach((e)->{
					if(e.getValue().field.contains(MouseInput.mouse.x, MouseInput.mouse.y)) {
						mouseoverRecipe = e.getKey();
						if(e.getValue().field.contains(MouseInput.mousel.x, MouseInput.mousel.y) && MouseInput.mousel.click())
							plr.CraftItem(e.getKey());
					}
				});
			}
		}
		backButton.tick();
		if(backButton.isclicked)selectedCategory = null;
		searchButton.tick();
	}
	
	public void render() {
		int x = field.x, y = field.y;
		Screen.drawGUISprite(x, y, back);
		Game.sfont.render(x+field.width/2, y+fieldTop.height/2, searching ? "" : "Crafting", 0, 0xff000000);
		backButton.render();
		searchButton.render();
		if(selectedCategory==null) {
			categories.values().forEach((f)->f.render());
			if(mouseoverCategory != null)Game.ccFont.render(x+250, y+42, false, false, mouseoverCategory, 0, 0xff000000);
		}else{
			recipes.values().forEach((f)->f.render());
			if(mouseoverRecipe != null)Game.ccFont.render(x+250, y+42, false, false, ItemList.GetItem(mouseoverRecipe.products.get(0).ID).getDisplayName(), 0, 0xff000000);
		}
	}
	
	private static class CategoryField extends ItemField{
		protected static SpriteSheet categoryBack = new SpriteSheet("/Crafting/CategoryBack.png");
		private SpriteSheet sprite;
		
		public CategoryField(String category) {
			this.field = new PArea(0,0,52,52);
			this.sprite = new SpriteSheet("/Crafting/Category"+category+".png");
		}

		public void render() {
			Screen.drawGUISprite(field.x, field.y, categoryBack);
			Screen.drawGUISprite(field.x+2, field.y+2, sprite);
		}
	}

	private static class RecipeField extends ItemField{
		
		public RecipeField(Recipe recipe) {
			super();
			this.item = ItemList.GetItem(recipe.products.get(0).ID);
		}
	}
}
