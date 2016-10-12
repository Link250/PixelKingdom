package gameFields;

import java.util.ArrayList;

import entities.Player;
import gfx.Mouse;
import gfx.SpriteSheet;
import gfx.Mouse.MouseType;
import item.*;
import item.itemList.MatStack;
import main.MainConfig.GameFields;
import main.Game;
import main.PArea;

public class Crafting extends GameField {
	
	private ArrayList<Class<?>> types = new ArrayList<Class<?>>();
	private ArrayList<SpriteSheet> gfxs = new ArrayList<SpriteSheet>();
	private ArrayList<Recipe> recipelist = new ArrayList<Recipe>();
	private SpriteSheet Background = new SpriteSheet("/Items/field.png");
	private SpriteSheet Search = new SpriteSheet("/Buttons/back_small.png");
	private SpriteSheet BackButton = new SpriteSheet("/Buttons/back_small.png");
	private SpriteSheet Craft = new SpriteSheet("/Buttons/Craft.png");
	private SpriteSheet scroll = new SpriteSheet("/Buttons/scroll.png");
	private int select_type,select_recipe,select_educt,select_product;
	
	public Player plr;
	
	public Crafting(Player plr){
		super(5*36+12,10+ 5*36, GameFields.Field_Crafting);
		this.plr = plr;
		types.add(Tool.class);
		types.add(Bag.class);
		types.add(MatStack.class);
		for(Class<?> c : types){
			gfxs.add(new SpriteSheet("/Items/Category"+c.getSimpleName()+".png"));
		}
	}
	
	public void tick() {
		Drag();
		if(mouseover(Game.input.mouse.x, Game.input.mouse.y)){
			Mouse.mouseType=MouseType.DEFAULT;
			if(Game.input.mousel.click()){
				int mx = Game.input.mousel.x, my = Game.input.mousel.y;
				int x = field.x, y = field.y;
				PArea r = new PArea(x,y,36,36);
				switch(select_type){
				case 0:
					y+=36; r.setBounds(x,y,36,36);
					for(int i = 0; i < types.size(); i ++){
						if(r.contains(mx, my)){
							select_type = i+1;
							recipelist = plr.recipelist.getRecipes(types.get(i));
							if(recipelist.size()!=0){
								if(select_recipe < 0)select_recipe = 0;
								if(select_recipe >= recipelist.size())select_recipe = recipelist.size()-1;
							}else{
								select_recipe = 0;
							}
						}x+=39; r.setBounds(x,y,36,36);
					}
					break;
				default:
					y+=33; r.setBounds(x,y,36,36);
					if(r.contains(mx, my))select_type = 0;
					if(recipelist.size() != 0){
						y+=39;
						for(int i = -2; i <= 2; i ++){
							r.setBounds(x,y,36,36);
							if(r.contains(mx, my)){
								select_recipe += i;
								if(select_recipe < 0)select_recipe = 0;
								if(select_recipe >= recipelist.size())select_recipe = recipelist.size()-1;
								if(recipelist.get(select_recipe).educts.size()>3){
									if(select_educt < 0)select_educt = 0;
									if(select_educt >= recipelist.get(select_recipe).educts.size()-2)select_educt = recipelist.get(select_recipe).educts.size()-3;
								}else select_educt = 0;
								if(recipelist.get(select_recipe).products.size()>3){
									if(select_product < 0)select_product = 0;
									if(select_product >= recipelist.get(select_recipe).products.size()-2)select_product = recipelist.get(select_recipe).products.size()-3;
								}else select_product = 0;
							}x+=39;
						}x-=39*4-3;y+=39;
						int n = 0;
						if(recipelist.get(select_recipe).educts.size()>3){for(int i = -1; i <= 1; i ++){
							r.setBounds(x,y,30,30);
							if(r.contains(mx, my)){
								select_educt += i;
								if(select_educt < 0)select_educt = 0;
								if(select_educt >= recipelist.get(select_recipe).educts.size()-2)select_educt = recipelist.get(select_recipe).educts.size()-3;
							}y+=33;n++;
						}}x+=78;y-=n*33;n=0;
						if(recipelist.get(select_recipe).products.size()>3){for(int i = -1; i <= 1; i ++){
							r.setBounds(x,y,30,30);
							if(r.contains(mx, my)){
								select_product += i;
								if(select_product < 0)select_product = 0;
								if(select_product >= recipelist.get(select_recipe).products.size()-2)select_product = recipelist.get(select_recipe).products.size()-3;
							}y+=33;n++;
						}}
						y+=36-n*33;x-=42;
						r.setBounds(x,y,36,24);
						if(r.contains(mx, my)){
							plr.CraftItem(recipelist.get(select_recipe));
						}
					}
					break;
				}
			}
		}
	}
	
	public void render() {
		renderfield();
		int x = field.x, y = field.y;
		Game.sfont.render(x+field.width/2, y+fieldTop.height/2, "Crafting", 0, 0xff000000, Game.screen);
		if(select_type == 0){
			y += 33;
			for(SpriteSheet gfx : gfxs){
				Game.screen.drawGUITile(x, y, 0, 0, Background, 0);
				Game.screen.drawGUITile(x+1, y+1, 0, 0, gfx, 0);
				x += 39;
			}	
		}else{
			for(int X = 0; X < field.width; X++){for(int Y = 0; Y < 36; Y++){Game.screen.drawGUIPixel(x+X, y+Y+72, 0x30808080);}}
			y += 33;
			Game.screen.drawGUITile(x, y, 0, 0, Background, 0);
			Game.screen.drawGUITile(x+1, y+1, 0, 0, BackButton, 0); x+= 78;
			Game.screen.drawGUITile(x, y, 0, 0, Background, 0);
			Game.screen.drawGUITile(x+1, y+1, 0, 0, gfxs.get(select_type-1), 0); x+= 78;
			Game.screen.drawGUITile(x, y, 0, 0, Background, 0);
			Game.screen.drawGUITile(x+1, y+1, 0, 0, Search, 0);
			if(recipelist.size()!=0){
				Game.screen.drawGUITile(x-78, y+114, 0, 0, Craft, 0);
				x -= 78; y += 39;
				for(int i = -2; i <= 2; i ++){
					if(i == 0)Game.screen.drawGUITile(x+i*39, y, 0, 0, Background, 0);
					try{
						ItemList.GetItem(recipelist.get(i+select_recipe).products.get(0).ID).render(Game.screen, x+i*39+1, y+1);
					}catch(IndexOutOfBoundsException e){}
				}x-=36;y+=39;
				int n = 0;
				n = recipelist.get(select_recipe).educts.size();
				if(n<3)if(n>1)y+=15;else y+=33;
				
				if(select_educt>0)Game.screen.drawGUITile(x+33, y, 0, 0x01, scroll, 0);
				if(select_educt<recipelist.get(select_recipe).educts.size()-3)Game.screen.drawGUITile(x+33, y+2*33+15, 0, 0x00, scroll, 0);
				
				for(int i = 0; i < 3; i ++){
					try{
						Game.mfont.render( x-39, y+i*33+9, false, false, Integer.toString(recipelist.get(select_recipe).educts.get(i+select_educt).n), 0, 0xff000000, Game.screen);
						ItemList.GetItem(recipelist.get(select_recipe).educts.get(i+select_educt).ID).render(Game.screen, x, y+i*33);
					}catch(IndexOutOfBoundsException e){}
				}x+=78;
				if(n<3)if(n>1)y-=15;else y-=33;
				n = recipelist.get(select_recipe).products.size();
				if(n<3)if(n>1)y+=15;else y+=33;
				
				if(select_product>0)Game.screen.drawGUITile(x-12, y, 0, 0x01, scroll, 0);
				if(select_product<recipelist.get(select_recipe).products.size()-3)Game.screen.drawGUITile(x-12, y+2*33+15, 0, 0x00, scroll, 0);
				
				for(int i = 0; i < 3; i ++){
					try{
						Game.mfont.render( x+36, y+i*33+9, false, false, Integer.toString(recipelist.get(select_recipe).products.get(i+select_product).n), 0, 0xff000000, Game.screen);
						ItemList.GetItem(recipelist.get(select_recipe).products.get(i+select_product).ID).render(Game.screen, x, y+i*33);
					}catch(IndexOutOfBoundsException e){}
				}
			}
		}
	}
}
