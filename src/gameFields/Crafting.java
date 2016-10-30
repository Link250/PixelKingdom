package gameFields;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import dataUtils.PArea;
import entities.Player;
import gfx.SpriteSheet;
import gui.Button;
import gui.ScrollBar;
import gfx.Screen;
import item.*;
import main.MainConfig.GameFields;
import main.MouseInput;
import main.Game;

public class Crafting extends GameField {
	
	private SpriteSheet back = new SpriteSheet("/Crafting/Back.png");
	private SpriteSheet selection = new SpriteSheet("/Crafting/Selection.png");
	private Button searchButton;
	private Button backButton;
	private Button craftButton;
	private ScrollBar scroll;
	private SpriteSheet scrollSprite;
	private String mouseoverCategory = null;
	private String selectedCategory = null;
	private Recipe selectedRecipe = null;
	private PArea fieldsArea;
	private PArea ComponentsArea;
	private boolean searching = false;
	private Map<String, ItemField> categories = new TreeMap<>();
	private Map<Recipe, ItemField> recipes = new TreeMap<>();
	private Map<Recipe.Component, ItemField> educts = new TreeMap<>(), products = new TreeMap<>();
	
	public Player plr;
	
	public Crafting(Player plr){
		super(GameFields.Field_Crafting);
		this.plr = plr;
		this.field.setSize(back.getWidth(), back.getHeight());
		this.fieldTop.setSize(back.getWidth()-2*36, 36);
		this.fieldsArea = new PArea(1, 1, 226, 226);
		this.ComponentsArea = new PArea(1, 1, 156, 226);
		this.plr.recipelist.getCategories().forEach((s) -> this.categories.put(s, new CategoryField(s)));
		this.searchButton = new Button(1, 1, 36, 36, false, false);
		this.searchButton.gfxData("/Crafting/SearchButton.png", false);
		this.backButton = new Button(1, 1, 36, 36, false, false);
		this.backButton.gfxData("/Crafting/BackButton.png", false);
		this.craftButton = new Button(1, 1, 150, 30, false, false);
		this.craftButton.gfxData("/Crafting/CraftButton.png", false);
		this.scroll = new ScrollBar(1, 1, 16, 226, false, false);
		this.placeFields();
		this.createScrollSprite();
	}
	
	private void placeFields() {
		int x = this.field.x, y = this.field.y+this.fieldTop.height+2;
		this.fieldsArea.setPosition(x, y);
		this.ComponentsArea.setPosition(x+246, y);
		this.searchButton.setPos(field.x+field.width-36, field.y, false, false);
		this.backButton.setPos(field.x, field.y, false, false);
		this.craftButton.setPos(x+249, y+193, false, false);
		this.scroll.setPos(x+228, y, false, false);
		this.placeItemFields();
	}
	
	private void placeItemFields() {
		int x = this.field.x, y = this.field.y+this.fieldTop.height+2;
		if(selectedCategory == null){
			this.scroll.setValues(categories.size()/4, 4);
			int n = -scroll.getValue()*4;
			for (ItemField field : categories.values()) {
				if(n>=0 && n<16)field.setPosition(x+(n%4)*58, y+(n/4)*58, false, false);
				n++;
			}
		}else{
			this.scroll.setValues(recipes.size()/6, 6);
			int n = -scroll.getValue()*6;
			for (ItemField field : recipes.values()) {
				if(n>=0 && n<36)field.setPosition(x+(n%6)*38, y+(n/6)*38, false, false);
				n++;
			}
			if(selectedRecipe != null) {
				x = this.field.x+249;
				y = this.field.y+this.fieldTop.height+24;
				n = 0;
				for (ItemField field : educts.values()) {
					field.setPosition(x+(n%4)*38, y+(n/4)*38, false, false);
					n++;
				}
				y = this.field.y+this.fieldTop.height+118;
				n = 0;
				for (ItemField field : products.values()) {
					field.setPosition(x+(n%4)*38, y+(n/4)*38, false, false);
					n++;
				}
			}
		}
	}
	
	private void createScrollSprite() {
		PArea sliderArea = this.scroll.getSlider();
		int[] pixels = new int[sliderArea.width*sliderArea.height];
		for (int i = 0; i < pixels.length; i++) {pixels[i] = 0xff404040;}
		if(this.scrollSprite == null)this.scrollSprite = new SpriteSheet(pixels, sliderArea.width, sliderArea.height, sliderArea.width, sliderArea.height);
		else this.scrollSprite.setPixels(pixels, sliderArea.width, sliderArea.height, sliderArea.width, sliderArea.height);
	}
	
	public void tick() {
		if(Drag())placeFields();
		this.fieldTop.setPosition(field.x+36, field.y);
		this.mouseoverCategory = null;
		if(mouseover()) {
			if(fieldsArea.contains(MouseInput.mouse.x, MouseInput.mouse.y)) {
				if(selectedCategory == null)this.categories.entrySet().forEach((e)->{
					if(e.getValue().field.contains(MouseInput.mouse.x, MouseInput.mouse.y)) {
						if(e.getValue().field.contains(MouseInput.mousel.x, MouseInput.mousel.y) && MouseInput.mousel.click()) {
							selectedCategory = e.getKey();
							recipes = new TreeMap<>();
							plr.recipelist.getRecipes(selectedCategory).forEach((r) -> recipes.put(r, new RecipeField(r)));
							placeFields();
							createScrollSprite();
						}else
							mouseoverCategory = e.getKey();
					}
				});
				else this.recipes.entrySet().forEach((e)->{
					if(e.getValue().field.contains(MouseInput.mouse.x, MouseInput.mouse.y)) {
						if(e.getValue().field.contains(MouseInput.mousel.x, MouseInput.mousel.y) && MouseInput.mousel.click()) {
							selectedRecipe = e.getKey();
							educts = new TreeMap<>();
							selectedRecipe.educts.forEach((educt) -> {
								ItemField f = new ItemField();
								f.item = ItemList.GetItem(educt.ID);
								educts.put(educt, f);
							});
							products = new TreeMap<>();
							selectedRecipe.products.forEach((product) -> {
								ItemField f = new ItemField();
								f.item = ItemList.GetItem(product.ID);
								products.put(product, f);
							});
							placeFields();
						}
					}
				});
				int scrollCount;
				if((scrollCount = MouseInput.mouse.getScroll()) != 0) {
					scroll.setValue(scroll.getValue()+scrollCount);
					placeItemFields();
				}
			}else if(selectedRecipe!=null && ComponentsArea.contains(MouseInput.mouse.x, MouseInput.mouse.y)){
				educts.values().forEach((f)->{if(f.getField().contains(MouseInput.mouse.x, MouseInput.mouse.y))f.mouseOver();});
				products.values().forEach((f)->{if(f.getField().contains(MouseInput.mouse.x, MouseInput.mouse.y))f.mouseOver();});
				craftButton.tick();
				if(craftButton.isclicked) {
					plr.CraftItem(selectedRecipe);
				}
			}
		}
		backButton.tick();
		if(backButton.isclicked) {
			selectedCategory = null;
			selectedRecipe = null;
			placeItemFields();
			createScrollSprite();
		}
		searchButton.tick();
		int value = scroll.getValue();
		scroll.tick();
		if(value != scroll.getValue())placeItemFields();
	}
	
	public void render() {
		int x = field.x, y = field.y;
		Screen.drawGUISprite(x, y, back);
		Game.sfont.render(x+field.width/2, y+fieldTop.height/2, searching ? "" : "Crafting", 0, 0xff000000);
		backButton.render();
		searchButton.render();
		if(selectedCategory==null) {
			int n = -scroll.getValue()*4;
			for (ItemField f : categories.values()) {
				if(n>=0 && n<16)f.render();
				n++;
			}
			if(mouseoverCategory != null)Game.ccFont.render(x+250, y+42, false, false, mouseoverCategory, 0, 0xff000000);
		}else{
			int n = -scroll.getValue()*6;
			for (Entry<Recipe, ItemField> entry : recipes.entrySet()) {
				if(n>=0 && n<36) {
					entry.getValue().render();
					if(entry.getKey().equals(selectedRecipe))Screen.drawGUISprite(entry.getValue().getField().x, entry.getValue().getField().y, selection);;
				}
				n++;
			}
			if(selectedRecipe != null) {
				Game.ccFont.render(x+323, y+50, true, true, "Ingredients:", 0, 0xff000000);
				Game.ccFont.render(x+323, y+144, true, true, "Products:", 0, 0xff000000);
				Game.ccFont.render(x+323, y+250, true, true, "Craft", 0, 0xff000000);
				educts.forEach((c,f)->f.render(c.n));
				products.forEach((c,f)->f.render(c.n));
				craftButton.render();
//				Game.ccFont.render(x+250, y+42, false, false, ItemList.GetItem(selectedRecipe.products.get(0).ID).getDisplayName(), 0, 0xff000000);
			}
		}
		Screen.drawGUISprite(x+228, y+38+scroll.getSlider().y, scrollSprite);
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
