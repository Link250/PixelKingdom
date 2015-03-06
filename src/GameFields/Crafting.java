package GameFields;

import java.util.ArrayList;

import entities.Player;
import gfx.Mouse;
import gfx.SpriteSheet;
import ItemList.MatStack;
import Items.*;
import Main.Game;
import Main.PArea;

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
	
	public Crafting(Game game, Player plr){
		super(5*12+4,10+ 5*12, game, 7);
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
		if(mouseover(game.input.mouse.x/Game.SCALE, game.input.mouse.y/Game.SCALE)){
			Mouse.mousetype=0;
			if(game.input.mousel.click()){
				int mx = game.input.mousel.x/Game.SCALE, my = game.input.mousel.y/Game.SCALE;
				int x = field.x, y = field.y;
				PArea r = new PArea(x,y,12,12);
				switch(select_type){
				case 0:
					y+=12; r.setBounds(x,y,12,12);
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
						}x+=13; r.setBounds(x,y,12,12);
					}
					break;
				default:
					y+=11; r.setBounds(x,y,12,12);
					if(r.contains(mx, my))select_type = 0;
					if(recipelist.size() != 0){
						y+=13;
						for(int i = -2; i <= 2; i ++){
							r.setBounds(x,y,12,12);
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
							}x+=13;
						}x-=13*4-1;y+=13;
						int n = 0;
						if(recipelist.get(select_recipe).educts.size()>3){for(int i = -1; i <= 1; i ++){
							r.setBounds(x,y,10,10);
							if(r.contains(mx, my)){
								select_educt += i;
								if(select_educt < 0)select_educt = 0;
								if(select_educt >= recipelist.get(select_recipe).educts.size()-2)select_educt = recipelist.get(select_recipe).educts.size()-3;
							}y+=11;n++;
						}}x+=26;y-=n*11;n=0;
						if(recipelist.get(select_recipe).products.size()>3){for(int i = -1; i <= 1; i ++){
							r.setBounds(x,y,10,10);
							if(r.contains(mx, my)){
								select_product += i;
								if(select_product < 0)select_product = 0;
								if(select_product >= recipelist.get(select_recipe).products.size()-2)select_product = recipelist.get(select_recipe).products.size()-3;
							}y+=11;n++;
						}}
						y+=12-n*11;x-=14;
						r.setBounds(x,y,12,8);
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
		int x = game.screen.xOffset+field.x, y = game.screen.yOffset+field.y;
		Game.sfont.render(x+2, y+1, "Crafting", 0, 0xff000000, game.screen);
		if(select_type == 0){
			y += 11;
			for(SpriteSheet gfx : gfxs){
				game.screen.renderGUITile(x, y, 0, 0, Background, 0);
				game.screen.renderGUITile(x+1, y+1, 0, 0, gfx, 0);
				x += 13;
			}	
		}else{
			for(int X = 0; X < field.width; X++){for(int Y = 0; Y < 12; Y++){game.screen.renderPixelScaled(x+X, y+Y+24, 0x30808080);}}
			y += 11;
			game.screen.renderGUITile(x, y, 0, 0, Background, 0);
			game.screen.renderGUITile(x+1, y+1, 0, 0, BackButton, 0); x+= 26;
			game.screen.renderGUITile(x, y, 0, 0, Background, 0);
			game.screen.renderGUITile(x+1, y+1, 0, 0, gfxs.get(select_type-1), 0); x+= 26;
			game.screen.renderGUITile(x, y, 0, 0, Background, 0);
			game.screen.renderGUITile(x+1, y+1, 0, 0, Search, 0);
			if(recipelist.size()!=0){
				game.screen.renderGUITile(x-26, y+38, 0, 0, Craft, 0);
				x -= 26; y += 13;
				for(int i = -2; i <= 2; i ++){
					if(i == 0)game.screen.renderGUITile(x+i*13, y, 0, 0, Background, 0);
					try{
						ItemList.GetItem(recipelist.get(i+select_recipe).products.get(0).ID).render(game.screen, x+i*13+1, y+1);
					}catch(IndexOutOfBoundsException e){}
				}x-=12;y+=13;
				int n = 0;
				n = recipelist.get(select_recipe).educts.size();
				if(n<3)if(n>1)y+=5;else y+=11;
				
				if(select_educt>0)game.screen.renderGUITile(x+11, y, 0, 0x01, scroll, 0);
				if(select_educt<recipelist.get(select_recipe).educts.size()-3)game.screen.renderGUITile(x+11, y+2*11+5, 0, 0x00, scroll, 0);
				
				for(int i = 0; i < 3; i ++){
					try{
						Game.mfont.render( x-13, y+i*11+3, Integer.toString(recipelist.get(select_recipe).educts.get(i+select_educt).n), 0, 0xff000000, game.screen);
						ItemList.GetItem(recipelist.get(select_recipe).educts.get(i+select_educt).ID).render(game.screen, x, y+i*11);
					}catch(IndexOutOfBoundsException e){}
				}x+=26;
				if(n<3)if(n>1)y-=5;else y-=11;
				n = recipelist.get(select_recipe).products.size();
				if(n<3)if(n>1)y+=5;else y+=11;
				
				if(select_product>0)game.screen.renderTile(x-4, y, 0, 0x01, scroll, 0);
				if(select_product<recipelist.get(select_recipe).products.size()-3)game.screen.renderTile(x-4, y+2*11+5, 0, 0x00, scroll, 0);
				
				for(int i = 0; i < 3; i ++){
					try{
						Game.mfont.render( x+12, y+i*11+3, Integer.toString(recipelist.get(select_recipe).products.get(i+select_product).n), 0, 0xff000000, game.screen);
						ItemList.GetItem(recipelist.get(select_recipe).products.get(i+select_product).ID).render(game.screen, x, y+i*11);
					}catch(IndexOutOfBoundsException e){}
				}
			}
		}
	}
}
