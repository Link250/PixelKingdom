package gameFields;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import dataUtils.PArea;
import entities.Player;
import gfx.SpriteSheet;
import gui.Button;
import gui.ScrollBar;
import gui.TextField;
import gfx.Screen;
import item.*;
import main.MainConfig.GameFields;
import main.MouseInput;
import main.Game;

public class Crafting extends GameField {
	
	private SpriteSheet back = new SpriteSheet("/Crafting/Back.png");
	private SpriteSheet selection = new SpriteSheet("/Crafting/Selection.png");
	private Button searchButton;
	private Button editButton;
	private Button backButton;
	private Button craftButton;
	private TextField craftCount;
	private TextField searchText;
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
		this.editButton = new Button(1, 1, 36, 36, false, false);
		this.editButton.gfxData("/Crafting/EditButton.png", false);
		this.backButton = new Button(1, 1, 36, 36, false, false);
		this.backButton.gfxData("/Crafting/BackButton.png", false);
		this.craftButton = new Button(1, 1, 74, 30, false, false);
		this.craftButton.gfxData("/Crafting/CraftButton.png", false);
		this.craftCount = new TextField(1, 1, 74, 30, true, true, Game.ccFont);
		this.craftCount.setCharFilter((c)->{return Character.isDigit(c);});
		this.craftCount.setText("1");
		this.searchText = new TextField(1, 1, 326, 36, true, false, Game.ccFont);
		this.scroll = new ScrollBar(1, 1, 16, 226, false, false);
		this.placeFields();
		this.createScrollSprite();
	}
	
	private void placeFields() {
		int x = this.field.x, y = this.field.y;
		this.searchText.setPos(x+38, y, false, false);
		y+=+this.fieldTop.height+2;
		this.fieldsArea.setPosition(x, y);
		this.ComponentsArea.setPosition(x+246, y);
		this.searchButton.setPos(field.x+field.width-36, field.y, false, false);
		this.editButton.setPos(field.x+field.width-36, field.y, false, false);
		this.backButton.setPos(field.x, field.y, false, false);
		this.craftButton.setPos(x+249, y+193, false, false);
		this.craftCount.setPos(x+325, y+193, false, false);
		this.scroll.setPos(x+228, y, false, false);
		this.placeItemFields();
	}
	
	private void placeItemFields() {
		int x = this.field.x, y = this.field.y+this.fieldTop.height+2;
		if(selectedCategory == null && !searching){
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
		if(hasMoved())placeFields();
		//reposition top field
		this.fieldTop.setPosition(field.x+36, field.y);
		this.mouseoverCategory = null;
		if(mouseover()) {
			//check if the left area contains the mouse
			if(fieldsArea.containsMouse(MouseInput.mouse)) {
				if(selectedCategory == null && !searching) {
					this.categories.entrySet().forEach((e)->{
						if(e.getValue().field.containsMouse(MouseInput.mouse)) {
							if(e.getValue().field.isClicked(MouseInput.mousel)) {selectCategory(e.getKey());}
							else mouseoverCategory = e.getKey();
						}
					});
				}else{
					this.recipes.entrySet().forEach((e)->{
						if(e.getValue().field.isClicked(MouseInput.mousel)) {selectRecipe(e.getKey());}
					});
				}
				int scrollCount;
				if((scrollCount = MouseInput.mouse.getScroll()) != 0) {
					scroll.setValue(scroll.getValue()+scrollCount);
					placeItemFields();
				}
			}else{
				//check if the right area contains the mouse and a recipe is selected
				if(selectedRecipe!=null && ComponentsArea.containsMouse(MouseInput.mouse)){
					educts.values().forEach((f)->{if(f.getField().containsMouse(MouseInput.mouse))f.mouseOver();});
					products.values().forEach((f)->{if(f.getField().containsMouse(MouseInput.mouse))f.mouseOver();});
					craftCount.tick();
					craftButton.tick();
					if(craftButton.isclicked) {
						int n;
						try{n = Integer.parseInt(craftCount.getText());}catch(NumberFormatException e) {n = 0;}
						for (int i = 0; i < n; i++) {
							if(!plr.craftItem(selectedRecipe))break;
						}
					}
				}
			}
			backButton.tick();
			if(backButton.isclicked) {
				if(searching) {
					searchText.setFocus(false);
					searching = false;
				}
				selectedCategory = null;
				selectedRecipe = null;
				placeItemFields();
				createScrollSprite();
			}
			if(searching) {
				editButton.tick();
				if(editButton.isclicked) {
					searchText.setFocus(true);
				}
				searchText.tick();
				if(searchText.hasFinshed()) {
					selectSearch(searchText.getText());
					searchText.setUnfinished();
					selectedRecipe = null;
				}
			}else {
				searchButton.tick();
				if(searchButton.isclicked) {
					searchText.setFocus(true);
					searching = true;
					selectSearch(null);
				}
			}
			int value = scroll.getValue();
			scroll.tick();
			if(value != scroll.getValue())placeItemFields();
		}else {
			backButton.mouseover = false;
			searchButton.mouseover = false;
			editButton.mouseover = false;
			craftButton.mouseover = false;
		}
	}
	
	private void selectCategory(String category) {
		selectedCategory = category;
		recipes = new TreeMap<>();
		plr.recipelist.getRecipes(selectedCategory).forEach((r) -> recipes.put(r, new RecipeField(r)));
		placeFields();
		createScrollSprite();
	}
	
	private void selectRecipe(Recipe recipe) {
		selectedRecipe = recipe;
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
	
	private void selectSearch(String search) {
		recipes = new TreeMap<>();
		if(search==null) {
			plr.recipelist.getAllRecipes().forEach((r) -> recipes.put(r, new RecipeField(r)));
		}else {
			plr.recipelist.getAllRecipes((r)->{
				if(r.educts.stream().filter((c)->{
					return ItemList.GetItem(c.ID).getDisplayName().toLowerCase().contains(search.toLowerCase());
				}).count()>0)return true;
				if(r.products.stream().filter((c)->{
					return ItemList.GetItem(c.ID).getDisplayName().toLowerCase().contains(search.toLowerCase());
				}).count()>0)return true;
				return false;
			}).forEach((r) -> recipes.put(r, new RecipeField(r)));
		}
		placeFields();
		createScrollSprite();
	}
	
	public void render() {
		int x = field.x, y = field.y;
		Screen.drawGUISprite(x, y, back);
		if(searching) {
			searchText.render();
			editButton.render();
		}else {
			Game.sfont.render(x+field.width/2, y+fieldTop.height/2, "Crafting", 0, 0xff000000);
			searchButton.render();
		}
		backButton.render();
		if(selectedCategory==null && !searching) {
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
				educts.forEach((c,f)->f.render(c.n));
				products.forEach((c,f)->f.render(c.n));
				craftButton.render();
				craftCount.render();
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
