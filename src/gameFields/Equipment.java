package gameFields;

import java.util.ArrayList;

import entities.Player;
import gfx.Mouse;
import gfx.SpriteSheet;
import item.*;
import main.Game;
import main.PArea;
import main.conversion.ConvertData;

public class Equipment extends GameField {
	
	private SpriteSheet Background = new SpriteSheet("/Equipment.png");
	
	public ToolBag		toolbag1;
	public MaterialBag	materialbag1;
	public MaterialBag	materialbag2;
	public BeltBag		beltbag1;
	public ItemBag		itembag1;
	public ItemBag		itembag2;
	public Player plr;
	
	public Equipment(Game game, Player plr){
		super(70,59, game, 0);
		this.plr = plr;
	}
	
	public void tick() {
		Drag();
		if(mouseover(game.input.mouse.x/Game.SCALE, game.input.mouse.y/Game.SCALE)){
			Mouse.mousetype=0;
			if(game.input.mousel.click()){
				int mx = game.input.mousel.x/Game.SCALE, my = game.input.mousel.y/Game.SCALE;
				int x = field.x, y = field.y;
				PArea r = new PArea(0,0,10,10);
				x+=30;y+=12;	//r.setBounds(x,y,10,10);if(r.contains(mx, my)){if(item!=null)		{if(Mouse.Item==null){Mouse.Item = item; item = null;}}					else{try{item=(Item)Mouse.Item;Mouse.Item = null;}					catch(ClassCastException e){}}}
				y+=12;			//r.setBounds(x,y,10,10);if(r.contains(mx, my)){if(item!=null)		{if(Mouse.Item==null){Mouse.Item = item; item = null;}}					else{try{item=(Item)Mouse.Item;Mouse.Item = null;}					catch(ClassCastException e){}}}
				y+=12;			//r.setBounds(x,y,10,10);if(r.contains(mx, my)){if(item!=null)		{if(Mouse.Item==null){Mouse.Item = item; item = null;}}					else{try{item=(Item)Mouse.Item;Mouse.Item = null;}					catch(ClassCastException e){}}}
				y+=12;			//r.setBounds(x,y,10,10);if(r.contains(mx, my)){if(item!=null)		{if(Mouse.Item==null){Mouse.Item = item; item = null;}}					else{try{item=(Item)Mouse.Item;Mouse.Item = null;}					catch(ClassCastException e){}}}
				x-=15;y-=30;	r.setBounds(x,y,10,10);if(r.contains(mx, my)){if(toolbag1!=null)	{if(Mouse.Item==null){Mouse.Item = toolbag1; toolbag1 = null;}}			else{try{toolbag1=(ToolBag)Mouse.Item;Mouse.Item = null;}			catch(ClassCastException e){}}}
				y+=12;			r.setBounds(x,y,10,10);if(r.contains(mx, my)){if(materialbag1!=null){if(Mouse.Item==null){Mouse.Item = materialbag1; materialbag1 = null;}}	else{try{materialbag1=(MaterialBag)Mouse.Item;Mouse.Item = null;}	catch(ClassCastException e){}}}
				y+=12;			r.setBounds(x,y,10,10);if(r.contains(mx, my)){if(materialbag2!=null){if(Mouse.Item==null){Mouse.Item = materialbag2; materialbag2 = null;}}	else{try{materialbag2=(MaterialBag)Mouse.Item;Mouse.Item = null;}	catch(ClassCastException e){}}}
				y-=24;x+=30;	r.setBounds(x,y,10,10);if(r.contains(mx, my)){if(beltbag1!=null)	{if(Mouse.Item==null){Mouse.Item = beltbag1; beltbag1 = null;}}			else{try{beltbag1=(BeltBag)Mouse.Item;Mouse.Item = null;}			catch(ClassCastException e){}}}
				y+=12;			r.setBounds(x,y,10,10);if(r.contains(mx, my)){if(itembag1!=null)	{if(Mouse.Item==null){Mouse.Item = itembag1; itembag1 = null;}}			else{try{itembag1=(ItemBag)Mouse.Item;Mouse.Item = null;}			catch(ClassCastException e){}}}
				y+=12;			r.setBounds(x,y,10,10);if(r.contains(mx, my)){if(itembag2!=null)	{if(Mouse.Item==null){Mouse.Item = itembag2; itembag2 = null;}}			else{try{itembag2=(ItemBag)Mouse.Item;Mouse.Item = null;}			catch(ClassCastException e){}}}
				if(toolbag1!=null)plr.toolbaginv1 = new ToolBagInv(toolbag1, game, 1);					else plr.toolbaginv1 = null;
				if(materialbag1!=null)plr.materialbaginv1 = new MaterialBagInv(materialbag1, game, 2);	else plr.materialbaginv1 = null;
				if(materialbag2!=null)plr.materialbaginv2 = new MaterialBagInv(materialbag2, game, 3);	else plr.materialbaginv2 = null;
				if(beltbag1!=null)plr.beltbaginv1 = new BeltBagInv(beltbag1, game, 4);					else plr.beltbaginv1 = null;
				if(itembag1!=null)plr.itembaginv1 = new ItemBagInv(itembag1, game, 5);					else plr.itembaginv1 = null;
				if(itembag2!=null)plr.itembaginv2 = new ItemBagInv(itembag2, game, 6);					else plr.itembaginv2 = null;
			}
		}
	}
	
	public void render() {
		renderfield();
		game.screen.drawGUITile(game.screen.xOffset+field.x, game.screen.yOffset+field.y, 0, 0, Background, 0xff000000);
		Game.sfont.render(game.screen.xOffset+field.x+2, game.screen.yOffset+field.y+1, "Equipment", 0, 0xff000000, game.screen);
		
		int x = game.screen.xOffset+field.x, y = game.screen.yOffset+field.y;
		x+=30;y+=12;	//if(item != null){item.render(game.screen, x, y);}
		y+=12;			//if(item != null){item.render(game.screen, x, y);}
		y+=12;			//if(item != null){item.render(game.screen, x, y);}
		y+=12;			//if(item != null){item.render(game.screen, x, y);}
		x-=15;y-=30;	if(toolbag1 != null)	{toolbag1.render(game.screen, x, y);}		else{ToolBag.show(game.screen, x, y);}
		y+=12;			if(materialbag1 != null){materialbag1.render(game.screen, x, y);}	else{MaterialBag.show(game.screen, x, y);}
		y+=12;			if(materialbag2 != null){materialbag2.render(game.screen, x, y);}	else{MaterialBag.show(game.screen, x, y);}
		y-=24;x+=30;	if(beltbag1 != null)	{beltbag1.render(game.screen, x, y);}		else{BeltBag.show(game.screen, x, y);}
		y+=12;			if(itembag1 != null)	{itembag1.render(game.screen, x, y);}		else{ItemBag.show(game.screen, x, y);}
		y+=12;			if(itembag2 != null)	{itembag2.render(game.screen, x, y);}		else{ItemBag.show(game.screen, x, y);}
	}
	
	public void save(ArrayList<Byte> file){
		try{toolbag1.save(file);}catch(NullPointerException e){ConvertData.I2B(file, 0);}
		try{materialbag1.save(file);}catch(NullPointerException e){ConvertData.I2B(file, 0);}
		try{materialbag2.save(file);}catch(NullPointerException e){ConvertData.I2B(file, 0);}
		try{beltbag1.save(file);}catch(NullPointerException e){ConvertData.I2B(file, 0);}
		try{itembag1.save(file);}catch(NullPointerException e){ConvertData.I2B(file, 0);}
		try{itembag2.save(file);}catch(NullPointerException e){ConvertData.I2B(file, 0);}
	}

	public void load(ArrayList<Byte> file) {
		toolbag1 = (ToolBag) ItemList.NewItem(ConvertData.B2I(file));
		if(toolbag1 != null){toolbag1.load(file);plr.toolbaginv1 = new ToolBagInv(toolbag1, game, 1);}
		materialbag1 = (MaterialBag) ItemList.NewItem(ConvertData.B2I(file));
		if(materialbag1 != null){materialbag1.load(file);plr.materialbaginv1 = new MaterialBagInv(materialbag1, game, 2);}
		materialbag2 = (MaterialBag) ItemList.NewItem(ConvertData.B2I(file));
		if(materialbag2 != null){materialbag2.load(file);plr.materialbaginv2 = new MaterialBagInv(materialbag2, game, 3);}
		beltbag1 = (BeltBag) ItemList.NewItem(ConvertData.B2I(file));
		if(beltbag1 != null){beltbag1.load(file);plr.beltbaginv1 = new BeltBagInv(beltbag1, game, 4);}
		itembag1 = (ItemBag) ItemList.NewItem(ConvertData.B2I(file));
		if(itembag1 != null){itembag1.load(file);plr.itembaginv1 = new ItemBagInv(itembag1, game, 5);}
		itembag2 = (ItemBag) ItemList.NewItem(ConvertData.B2I(file));
		if(itembag2 != null){itembag2.load(file);plr.itembaginv2 = new ItemBagInv(itembag2, game, 6);}
	}

	public void create(){
		itembag1 = 		(ItemBag) ItemList.NewItem(401);
		toolbag1 = 		(ToolBag) ItemList.NewItem(426);
		materialbag1 = 	(MaterialBag) ItemList.NewItem(451);
		beltbag1 = 		(BeltBag) ItemList.NewItem(476);
		plr.toolbaginv1 = new ToolBagInv(toolbag1, game, 1);
		plr.materialbaginv1 = new MaterialBagInv(materialbag1, game, 2);
		plr.beltbaginv1 = new BeltBagInv(beltbag1, game, 4);
		plr.itembaginv1 = new ItemBagInv(itembag1, game, 5);
	}
}
