package entities;


import java.awt.Point;
import java.util.ArrayList;

import gfx.Mouse;
import gfx.SpriteSheet;
import GameFields.*;
import ItemList.MatStack;
import Items.*;
import Items.Recipe.component;
import Main.ConvertData;
import Main.Game;
import Maps.Map;

public class Player extends Mob{
	
	private Game game;
	private int anim;
	private int Color;
	private Hitbox col = new Hitbox(-1,-7,1,6); //walking
	private Hitbox cols = new Hitbox(-1,-1,1,6);//sneaking
	
	private int gravity = 1;
	private int jumpspeed = 4;
	private int walkspeed = 1;
	
	public boolean isinair;
	private boolean canJump;
	public boolean iscrouching;
	private int jumpcooldown;
	
	public Equipment equipment;
	public Crafting crafting;
	public ToolBagInv toolbaginv1;
	public MaterialBagInv materialbaginv1, materialbaginv2;
	public BeltBagInv beltbaginv1;
	public ItemBagInv itembaginv1, itembaginv2;
	public RecipeList recipelist = new RecipeList();
	private Point hotBar = new Point(0,0);
	private boolean openInv = false;
	private boolean openEquip = false;
	private boolean openCraft = false;
	private byte openHotBar = 0;
	private byte selected = 0;
	private SpriteSheet itemBackground = new SpriteSheet("/Items/field.png");
	private SpriteSheet itemBackgrounds = new SpriteSheet("/Items/fields.png");

	public Player(Map map, Game game) {
		super(map ,"Player", 0, 0, new SpriteSheet("/sprite_sheet_player.png"));
		this.game = game;
		this.map = map;
		equipment = new Equipment(game, this);
		crafting = new Crafting(game, this);
		Color = Game.configs.PlrCol;
		xOffset=6;
		yOffset=9;
		sheet.tileWidth = 13*3;
		sheet.tileHeight = 16*3;
		try{equipment.beltbag1.inventory[selected].setMouse();}catch(NullPointerException e){}
	}
	
	public void Gravity(){
		if(gravity != 0 && isinair){
			if(speedX < -1) speedX = -1;
			if(speedX > 1) speedX = 1;
			speedY += gravity;
		}
	}
	
	public void delItem(Item item){
		if(equipment.toolbag1!=null)delItemfrom(item, (Item[])equipment.toolbag1.inventory);
		if(equipment.materialbag1!=null)delItemfrom(item, equipment.materialbag1.inventory);
		if(equipment.materialbag2!=null)delItemfrom(item, equipment.materialbag2.inventory);
		if(equipment.beltbag1!=null)delItemfrom(item, equipment.beltbag1.inventory);
		if(equipment.itembag1!=null)delItemfrom(item, equipment.itembag1.inventory);
		if(equipment.itembag2!=null)delItemfrom(item, equipment.itembag2.inventory);
	}
	public void delItemfrom(Item item, Item[] inventory){
		if(inventory != null){
			for(int i = 0; i < inventory.length; i++){
				if(inventory[i]==item){
					inventory[i]=null;
				}
			}
		}
	}
	
	public boolean PickUp(Item item, int ID){
		if(equipment.beltbag1!=null){if(AddtoStack(ID, equipment.beltbag1.inventory))return true;}
		
		try{item = (MatStack)item;
			if(equipment.materialbag1!=null){if(AddtoStack(ID, equipment.materialbag1.inventory))return true;}
			if(equipment.materialbag2!=null){if(AddtoStack(ID, equipment.materialbag2.inventory))return true;}
			if(equipment.materialbag1!=null){if(CreateStack(ID, equipment.materialbag1.inventory))return true;}
			if(equipment.materialbag2!=null){if(CreateStack(ID, equipment.materialbag2.inventory))return true;}
		}catch(ClassCastException e){}
		try{item = (Tool)item;
			if(equipment.toolbag1!=null){if(AddtoStack(ID, equipment.toolbag1.inventory))return true;}
			if(equipment.toolbag1!=null){if(CreateStack(ID, equipment.toolbag1.inventory))return true;}
		}catch(ClassCastException e){}
		if(equipment.itembag1!=null){if(AddtoStack(ID, equipment.itembag1.inventory))return true;}
		if(equipment.itembag2!=null){if(AddtoStack(ID, equipment.itembag2.inventory))return true;}
		if(equipment.itembag1!=null){if(CreateStack(ID, equipment.itembag1.inventory))return true;}
		if(equipment.itembag2!=null){if(CreateStack(ID, equipment.itembag2.inventory))return true;}
		
		return false;
	}
	public boolean AddtoStack(int ID, Item[] inv){
		for(int i = 0; i < inv.length; i++){
			try{
				if(inv[i].getID()==ID && inv[i].getStack() < inv[i].getStackMax()){
					inv[i].addStack(1);
					return true;
				}
			}catch(NullPointerException e){}
		}
		return false;
	}
	public boolean CreateStack(int ID, Item[] inv){
		for(int i = 0; i < inv.length; i++){
			if(inv[i]==null){
				inv[i] = ItemList.NewItem(ID);
				if(inv[i] != null)return true;
				else return false;
			}
		}return false;
	}

	public boolean CraftItem(Recipe r){
		Bag b;
		for(component c : r.educts){
			int n = c.n;
			if(equipment.materialbag1!=null){b=equipment.materialbag1;for(int i = 0; i < b.inventory.length; i++){	if(b.inventory[i]!=null){if(b.inventory[i].getID()==c.ID)n-=b.inventory[i].getStack();}}}
			if(equipment.materialbag2!=null){b=equipment.materialbag2;for(int i = 0; i < b.inventory.length; i++){	if(b.inventory[i]!=null){if(b.inventory[i].getID()==c.ID)n-=b.inventory[i].getStack();}}}
			if(equipment.toolbag1!=null){b=equipment.toolbag1;for(int i = 0; i < b.inventory.length; i++){			if(b.inventory[i]!=null){if(b.inventory[i].getID()==c.ID)n-=b.inventory[i].getStack();}}}
			if(equipment.itembag1!=null){b=equipment.itembag1;for(int i = 0; i < b.inventory.length; i++){			if(b.inventory[i]!=null){if(b.inventory[i].getID()==c.ID)n-=b.inventory[i].getStack();}}}
			if(equipment.itembag2!=null){b=equipment.itembag2;for(int i = 0; i < b.inventory.length; i++){			if(b.inventory[i]!=null){if(b.inventory[i].getID()==c.ID)n-=b.inventory[i].getStack();}}}
			if(n > 0)return false;
		}
		for(component c : r.educts){
			int n = c.n;
			if(equipment.materialbag1!=null){b=equipment.materialbag1;for(int i = 0; i < b.inventory.length; i++){	if(b.inventory[i]!=null){if(b.inventory[i].getID()==c.ID)n=b.inventory[i].delStack(n);}}}
			if(equipment.materialbag2!=null){b=equipment.materialbag2;for(int i = 0; i < b.inventory.length; i++){	if(b.inventory[i]!=null){if(b.inventory[i].getID()==c.ID)n=b.inventory[i].delStack(n);}}}
			if(equipment.toolbag1!=null){b=equipment.toolbag1;for(int i = 0; i < b.inventory.length; i++){			if(b.inventory[i]!=null){if(b.inventory[i].getID()==c.ID)n=b.inventory[i].delStack(n);}}}
			if(equipment.itembag1!=null){b=equipment.itembag1;for(int i = 0; i < b.inventory.length; i++){			if(b.inventory[i]!=null){if(b.inventory[i].getID()==c.ID)n=b.inventory[i].delStack(n);}}}
			if(equipment.itembag2!=null){b=equipment.itembag2;for(int i = 0; i < b.inventory.length; i++){			if(b.inventory[i]!=null){if(b.inventory[i].getID()==c.ID)n=b.inventory[i].delStack(n);}}}
		}
		for(component c : r.products){for(int i=0; i < c.n; i++){PickUp(ItemList.GetItem(c.ID), c.ID);}}
//		System.out.println("craftable");
		return true;
	}
	
	public void tick(int numTick) {
		
//		map.setlight(x, y, (byte) (64));map.addBlockUpdate(x, y,0); // just for testing lightsystem
		if(game.input.space.isPressed() && canJump){
			speedY-= jumpspeed;
			canJump = false;
			jumpcooldown = 10;
		}
		if(game.input.left.isPressed() && speedX > -walkspeed){
			speedX-=1;
			movingDir = 1;
		}
		if(game.input.right.isPressed()  && speedX < walkspeed){
			speedX+=1;
			movingDir = 0;
		}
		if(game.input.shift.isPressed() && !isinair){
			iscrouching=true;
		}else{
			iscrouching=false;
		}
		if((!game.input.right.isPressed() && !game.input.left.isPressed()) | (game.input.right.isPressed() && game.input.left.isPressed())){
			speedX=0;
			isMoving = false;
		}else{
			isMoving = true;
		}
		if(speedX!=0) numSteps++;
		if(jumpcooldown > 0) jumpcooldown--;
		
		Hitbox temp = col;
		if(iscrouching) temp =cols;
		if(!iscrouching || numTick%2==0){
			if(Colision.onGround(map, temp, x, y)){
				isinair = false;
				if(jumpcooldown == 0)canJump = true;
			}else{isinair = true;}
			if(Colision.canMove(map, temp, x, y, speedX, 0)){ x += speedX;
			}else{
				if(speedX != 0 & Colision.canWalkOver(map, temp, x, y, speedX, 0)){y --; x += speedX;}
				else speedX=0;
			}
			int n;
			if(speedY < 0){
				for(n = 0; n >= speedY; n--){
					if(!Colision.canMove(map, temp, x, y, 0, n)) speedY = n+1;
				}
			}else{
				for(n = 0; n <= speedY; n++){
					if(!Colision.canMove(map, temp, x, y, 0, n)) speedY = n-1;
				}
			}
			y += speedY;
		}
		
		/*		HOTBAR		*/
		if(game.input.mousem.click() || game.input.H.click()){
			if(openHotBar == 0) {
				hotBar.x = game.input.mouse.x;
				hotBar.y = game.input.mouse.y;
				openHotBar++;
			}else
				openHotBar=0;
		}
		if(openHotBar != 0){
			if(equipment.beltbag1 != null){
				if(openHotBar < 20+equipment.beltbag1.inventory.length)openHotBar +=2;
				if(Math.sqrt(Math.pow((game.input.mouse.x-hotBar.x)/Game.SCALE, 2)+Math.pow((game.input.mouse.y-hotBar.y)/Game.SCALE, 2)) >= 20+equipment.beltbag1.inventory.length){
					selected = (byte)(equipment.beltbag1.inventory.length/2-Math.atan2(game.input.mouse.x-hotBar.x, game.input.mouse.y-hotBar.y)/(Math.PI*2/equipment.beltbag1.inventory.length));
				}
			}
		}
		try{
			equipment.beltbag1.inventory[selected].setMouse();
		}catch(NullPointerException e){Mouse.mousetype=0;}

		/*		EQUIPMENT		*/
		if(game.input.equip.click()){
			if(openEquip)openEquip = false;
			else openEquip = true;
		}if(openEquip)equipment.tick();
		
		/*		CRAFTING		*/
		if(game.input.craft.click()){
			if(openCraft)openCraft = false;
			else openCraft = true;
		}if(openCraft)crafting.tick();
		
		/*		INVENTORY		*/
		if(game.input.inv.click()){
			if(openInv)openInv = false;
			else openInv = true;
		}if(openInv){
			try{toolbaginv1.tick();}catch(NullPointerException e){}
			try{materialbaginv1.tick();}catch(NullPointerException e){}
			try{materialbaginv2.tick();}catch(NullPointerException e){}
			try{beltbaginv1.tick();}catch(NullPointerException e){}
			try{itembaginv1.tick();}catch(NullPointerException e){}
			try{itembaginv2.tick();}catch(NullPointerException e){}
		}
		Bag b;
		if(equipment.materialbag1!=null){b=equipment.materialbag1;for(int i = 0; i < b.inventory.length; i++){	if(b.inventory[i]!=null){if(b.inventory[i].getStack()<=0)b.inventory[i] = null;}}}
		if(equipment.materialbag2!=null){b=equipment.materialbag2;for(int i = 0; i < b.inventory.length; i++){	if(b.inventory[i]!=null){if(b.inventory[i].getStack()<=0)b.inventory[i] = null;}}}
		if(equipment.toolbag1!=null){b=equipment.toolbag1;for(int i = 0; i < b.inventory.length; i++){			if(b.inventory[i]!=null){if(b.inventory[i].getStack()<=0)b.inventory[i] = null;}}}
		if(equipment.itembag1!=null){b=equipment.itembag1;for(int i = 0; i < b.inventory.length; i++){			if(b.inventory[i]!=null){if(b.inventory[i].getStack()<=0)b.inventory[i] = null;}}}
		if(equipment.itembag2!=null){b=equipment.itembag2;for(int i = 0; i < b.inventory.length; i++){			if(b.inventory[i]!=null){if(b.inventory[i].getStack()<=0)b.inventory[i] = null;}}}

		try{
			if(game.input.mousel.isClickable())equipment.beltbag1.inventory[selected].useItem(game.input, this, map, game.screen);
			else game.input.mousel.click();
		}catch(NullPointerException e){}

		
		/*		ANIMATION		*/
		if(isMoving){
			if(numSteps%24==0)anim=3;
			if(numSteps%24==4)anim=4;
			if(numSteps%24==8)anim=5;
			if(numSteps%24==12)anim=6;
			if(numSteps%24==16)anim=5;
			if(numSteps%24==20)anim=4;
			if(iscrouching){
				if(numSteps%30<30)anim=7;
				if(numSteps%30<15)anim=8;
			}
		}else{
			if(numTick%30<30)anim = 0;
			if(numTick%30<15)anim = 1;
			if(equipment.beltbag1 != null){if(equipment.beltbag1.inventory[selected] != null){
				if(equipment.beltbag1.inventory[selected].getAnim()!=0) anim = equipment.beltbag1.inventory[selected].getAnim();
			}}
			if(iscrouching)anim = 9;
		}
		if(isinair){anim = 2;}
	}
	
	public void render() {
		game.screen.drawTile(x-xOffset, y-yOffset, anim, movingDir*16, sheet, Color);
		
		if((anim == 10 || anim == 11) & equipment.beltbag1 != null){
			try{
				if(anim == 10)	equipment.beltbag1.inventory[selected].render(game.screen, x+3-movingDir*10, y-5, movingDir*16);
				if(anim == 11)	equipment.beltbag1.inventory[selected].render(game.screen, x+4-movingDir*12, y-3, movingDir*16);
			}catch(NullPointerException e){}
		}
		if(anim == 12 & equipment.beltbag1 != null){
			try{
				equipment.beltbag1.inventory[selected].render(game.screen, x+3-movingDir*8, y-2, movingDir*16);
			}catch(NullPointerException e){}
		}
		/*		EQUIPMENT		*/
		if(openEquip){equipment.render();}
		
		/*		EQUIPMENT		*/
		if(openCraft){crafting.render();}
		
		/*		INVENTORY		*/
		if(openInv){
			try{toolbaginv1.render();}catch(NullPointerException e){}
			try{materialbaginv1.render();}catch(NullPointerException e){}
			try{materialbaginv2.render();}catch(NullPointerException e){}
			try{beltbaginv1.render();}catch(NullPointerException e){}
			try{itembaginv1.render();}catch(NullPointerException e){}
			try{itembaginv2.render();}catch(NullPointerException e){}
			}
		
		/*		HOTBAR		*/
		if(openHotBar != 0){
			if(equipment.beltbag1 != null){
				for(int i = 0; i < equipment.beltbag1.inventory.length; i ++){
					game.screen.drawGUITile(
							hotBar.x/Game.SCALE+game.screen.xOffset+(int)(openHotBar*Math.sin((i+0.5)*Math.PI*2/equipment.beltbag1.inventory.length))-6
							,hotBar.y/Game.SCALE+game.screen.yOffset+(int)(-openHotBar*Math.cos((i+0.5)*Math.PI*2/equipment.beltbag1.inventory.length))-6
							, 0, 0, itemBackground, 0);
					if(i == selected){
						game.screen.drawGUITile(
								hotBar.x/Game.SCALE+game.screen.xOffset+(int)(openHotBar*Math.sin((selected+0.5)*Math.PI*2/equipment.beltbag1.inventory.length))-6
								,hotBar.y/Game.SCALE+game.screen.yOffset+(int)(-openHotBar*Math.cos((selected+0.5)*Math.PI*2/equipment.beltbag1.inventory.length))-6
								, 0, 0, itemBackgrounds, 0);
					}
					try{
						equipment.beltbag1.inventory[i].render(game.screen
								,hotBar.x/Game.SCALE+game.screen.xOffset+(int)(openHotBar*Math.sin((i+0.5)*Math.PI*2/equipment.beltbag1.inventory.length))-5
								,hotBar.y/Game.SCALE+game.screen.yOffset+(int)(-openHotBar*Math.cos((i+0.5)*Math.PI*2/equipment.beltbag1.inventory.length))-5
								,true);
					}catch(NullPointerException e){}
				}
				try{Game.font.render(game.input.mouse.x/Game.SCALE+game.screen.xOffset, game.input.mouse.y/Game.SCALE+game.screen.yOffset, (equipment.beltbag1.inventory[selected].getName()), 0, 0xff000000, game.screen);}
			catch(NullPointerException e){}
			}
		}
	}

	public void save(ArrayList<Byte> file){
		ConvertData.I2B(file, x);
		ConvertData.I2B(file, y);
		equipment.save(file);
	}

	public void load(ArrayList<Byte> file){
		x = ConvertData.B2I(file);
		y = ConvertData.B2I(file);
		equipment.load(file);
	}

	public void create(){
		this.x = 524800;
		map.loadChunk(x,524500);
		int h=524500;//for(h = 524500; PixelList.GetMat(this.x, h, map,1).ID==0 && h < 525000; h++){}
		this.y = h-yOffset;
		equipment.create();
		Pickaxe newpick = (Pickaxe) ItemList.NewItem(301);
		equipment.beltbag1.inventory[0] = newpick;
		if(Game.devmode){
			Item newitem = ItemList.NewItem(64); newitem.addStack(999);
			equipment.materialbag1.inventory[0] = newitem;
			newitem = ItemList.NewItem(16); newitem.addStack(999);
			equipment.materialbag1.inventory[1] = newitem;
			newitem = ItemList.NewItem(17); newitem.addStack(999);
			equipment.materialbag1.inventory[2] = newitem;
			newitem = ItemList.NewItem(18); newitem.addStack(999);
			equipment.materialbag1.inventory[3] = newitem;
			
			newpick = (Pickaxe) ItemList.NewItem(333);
			equipment.beltbag1.inventory[1] = newpick;
			equipment.beltbag1.inventory[2] = ItemList.NewItem(400);
			equipment.beltbag1.inventory[3] = ItemList.NewItem(332);
			equipment.itembag2 = (ItemBag) ItemList.NewItem(403);
		}
	}
}
