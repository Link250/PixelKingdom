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
	
	private SpriteSheet back = new SpriteSheet("/Crafting/BackV2.png");
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
	
	private static final int categoriesPerRow = 4, categoryIconDistance = 2, categoryIconOffset = 8;
	private static final int recipesPerRow = 5, recipeIconDistance = 8, recipeIconOffset = 9;
	
	public Player plr;
	
	public Crafting(Player plr){
		super(GameFields.Field_Crafting);
		this.plr = plr;
		this.field.setSize(back.getWidth(), back.getHeight());
		this.fieldTop.setSize(back.getWidth()-2*36, 36);
		this.fieldsArea = new PArea(1, 1, 230, 230);
		this.ComponentsArea = new PArea(1, 1, 162, 230);
		this.plr.recipeList.getCategories().forEach((s) -> this.categories.put(s, new CategoryField(s)));
		this.searchButton = new Button(1, 1, 36, 36, false, false);
		this.searchButton.gfxData("/Crafting/SearchButton.png", false);
		this.editButton = new Button(1, 1, 36, 36, false, false);
		this.editButton.gfxData("/Crafting/EditButton.png", false);
		this.backButton = new Button(1, 1, 36, 36, false, false);
		this.backButton.gfxData("/Crafting/BackButton.png", false);
		this.craftButton = new Button(1, 1, 74, 26, false, false);
		this.craftButton.gfxData("/Crafting/CraftButton.png", false);
		this.craftCount = new TextField(1, 1, 74, 26, true, true, Game.ccFont);
		this.craftCount.setCharFilter(Character::isDigit);
		this.craftCount.setText("1");
		this.searchText = new TextField(1, 1, 336, 36, true, false, Game.ccFont);
		this.scroll = new ScrollBar(1, 1, 16, fieldsArea.height, false, false);
		this.placeFields();
		this.createScrollSprite();
	}
	
	private void placeFields() {
		int x = this.field.x, y = this.field.y;
		this.searchText.setPos(x+38, y, false, false);
		y+=+this.fieldTop.height+2;
		this.fieldsArea.setPosition(x, y);
		this.ComponentsArea.setPosition(x+fieldsArea.width+2+scroll.getWidth()+2, y);
		this.searchButton.setPos(field.x+field.width-36, field.y, false, false);
		this.editButton.setPos(field.x+field.width-36, field.y, false, false);
		this.backButton.setPos(field.x, field.y, false, false);
		this.craftButton.setPos(x+256, y+198, false, false);
		this.craftCount.setPos(x+332, y+198, false, false);
		this.scroll.setPos(x+fieldsArea.width+2, y, false, false);
		this.placeItemFields();
	}
	
	private void placeItemFields() {
		int x = this.field.x, y = this.field.y+this.fieldTop.height+2;
		if(selectedCategory == null && !searching){
			this.scroll.setValues(categories.size()/categoriesPerRow, categoriesPerRow);
			int n = -scroll.getValue()*categoriesPerRow;
			for (ItemField field : categories.values()) {
				if(n>=0 && n<categoriesPerRow*categoriesPerRow)
					field.setPosition(x+(n%categoriesPerRow)*(52+categoryIconDistance)+categoryIconOffset, y+(n/categoriesPerRow)*(52+categoryIconDistance)+categoryIconOffset, false, false);
				n++;
			}
		}else{
			this.scroll.setValues(recipes.size()/recipesPerRow, recipesPerRow);
			int n = -scroll.getValue()*recipesPerRow;
			for (ItemField field : recipes.values()) {
				if(n>=0 && n<recipesPerRow*recipesPerRow)
					field.setPosition(x+(n%recipesPerRow)*(36+recipeIconDistance)+recipeIconOffset, y+(n/recipesPerRow)*(36+recipeIconDistance)+recipeIconOffset, false, false);
				n++;
			}
			if(selectedRecipe != null) {
				x = this.ComponentsArea.x + 6;
				y = this.ComponentsArea.y + 26;
				n = 0;
				for (ItemField field : educts.values()) {
					field.setPosition(x+(n%4)*38, y+(n/4)*38, false, false);
					n++;
				}
				y = this.ComponentsArea.y + 122;
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
						if(e.getValue().getField().containsMouse(MouseInput.mouse))e.getValue().mouseOver();
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
					if(craftButton.isclicked && selectedRecipe.researched) {
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
		plr.recipeList.getRecipes(selectedCategory).forEach((r) -> recipes.put(r, getSuitableItemField(r.mainProduct.ID, r.researched)));
		placeFields();
		createScrollSprite();
	}
	
	private void selectRecipe(Recipe recipe) {
		selectedRecipe = recipe;
		educts = new TreeMap<>();
		selectedRecipe.educts.forEach((educt) -> educts.put(educt, getSuitableItemField(educt.ID, recipe.researched)));
		products = new TreeMap<>();
		selectedRecipe.products.forEach((product) -> products.put(product, getSuitableItemField(product.ID, recipe.researched)));
		placeFields();
	}
	
	private void selectSearch(String search) {
		recipes = new TreeMap<>();
		if(search==null) {
			plr.recipeList.getAllRecipes().forEach((r) -> recipes.put(r, getSuitableItemField(r.mainProduct.ID, r.researched)));
		}else {
			plr.recipeList.getAllRecipes((r)->{
				if(r.educts.stream().filter((c)->{
					return ItemList.GetItem(c.ID).getDisplayName().toLowerCase().contains(search.toLowerCase());
				}).count()>0)return true;
				if(r.products.stream().filter((c)->{
					return ItemList.GetItem(c.ID).getDisplayName().toLowerCase().contains(search.toLowerCase());
				}).count()>0)return true;
				return false;
			}).forEach((r) -> recipes.put(r, getSuitableItemField(r.mainProduct.ID, r.researched)));
		}
		placeFields();
		createScrollSprite();
	}
	
	private ItemField getSuitableItemField(int id, boolean knownRecipe) {
		return (knownRecipe || plr.hasItemKnowledge(id)) ? new ItemField(ItemList.GetItem(id)) : new UnknownItemField();
//		return new ItemField(ItemList.GetItem(id));
	}
	
	public void render() {
		int x = field.x, y = field.y;
		Screen.drawGUISprite(x, y, back);
		if(searching) {
			searchText.render();
			editButton.render();
		}else {
			if(selectedCategory == null) {
				Game.sfont.render(x+field.width/2, y+fieldTop.height/2, "Crafting", 0, 0xff000000);
			}else {
				Game.sfont.render(x+field.width/2, y+fieldTop.height/2, selectedCategory, 0, 0xff000000);
			}
			searchButton.render();
		}
		backButton.render();
		if(selectedCategory==null && !searching) {
			int n = -scroll.getValue()*4;
			for (ItemField f : categories.values()) {
				if(n>=0 && n<16)f.render();
				n++;
			}
			if(mouseoverCategory != null)Game.ccFont.render(this.ComponentsArea.x + 10, this.ComponentsArea.y + 10, false, false, mouseoverCategory, 0, 0xff000000);
		}else{
			int n = -scroll.getValue()*5;
			for (Entry<Recipe, ItemField> entry : recipes.entrySet()) {
				if(n>=0 && n<25) {
					entry.getValue().render();
					if(entry.getKey().equals(selectedRecipe))Screen.drawGUISprite(entry.getValue().getField().x, entry.getValue().getField().y, selection);;
				}
				n++;
			}
			if(selectedRecipe != null) {
				Game.ccFont.render(this.ComponentsArea.x + 10, y+53, false, true, "Ingredients:", 0, 0xff000000);
				Game.ccFont.render(this.ComponentsArea.x + 10, y+149, false, true, "Products:", 0, 0xff000000);
				educts.forEach((c,f)->f.render(c.n));
				products.forEach((c,f)->f.render(c.n));
				craftButton.render();
				craftCount.render();
			}
		}
		Screen.drawGUISprite(scroll.getX(), scroll.getY()+scroll.getSlider().y, scrollSprite);
	}
	
	private static class CategoryField extends ItemField{
		protected static final SpriteSheet categoryBack = new SpriteSheet("/Crafting/CategoryBack.png");
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
}
