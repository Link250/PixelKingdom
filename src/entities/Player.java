package entities;

import java.awt.Point;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.EnumMap;
import java.util.List;

import org.joml.Vector2d;

import dataUtils.conversion.ConvertData;
import entities.entityList.ItemEntity;
import gfx.Screen;
import gfx.SpriteSheet;
import item.*;
import item.Recipe.Component;
import main.MainConfig.GameFields;
import main.MouseInput;
import main.SinglePlayer;
import main.Game;
import main.Keys;
import main.MainConfig;
import map.Map;
import gameFields.*;

import static dataUtils.PhysicConstants.*;

public class Player extends Mob {

	public static enum BAG {
		TOOL_1(ToolBag.class, new SpriteSheet("/Items/ToolBag.png"), GameFields.Field_ToolBag1), MAT_1(MaterialBag.class, new SpriteSheet("/Items/MaterialBag.png"), GameFields.Field_MatBag1), MAT_2(MaterialBag.class, new SpriteSheet("/Items/MaterialBag.png"), GameFields.Field_MatBag2), BELT_1(BeltBag.class, new SpriteSheet("/Items/BeltBag.png"), GameFields.Field_BeltBag1), ITEM_1(ItemBag.class, new SpriteSheet("/Items/ItemBag.png"), GameFields.Field_ItemBag1), ITEM_2(ItemBag.class, new SpriteSheet("/Items/ItemBag.png"), GameFields.Field_ItemBag2);

		public final Class<? extends Bag<?>> bagClass;
		public final GameFields fieldEnum;
		public final SpriteSheet defaultSprite;

		BAG(Class<? extends Bag<?>> bagClass, SpriteSheet defaultSprite, GameFields fieldEnum) {
			this.bagClass = bagClass;
			this.defaultSprite = defaultSprite;
			this.fieldEnum = fieldEnum;
		}
	}

	// bag Enums
	private EnumMap<BAG, Bag<?>> bags;
	private EnumMap<BAG, BagInv> bagInvs;

	// visual and Hitboxes
	private int anim;
	private int color;
	private Hitbox hitbox_main = new Hitbox(-1, 2, -6, 8); // walking
	private Hitbox hitbox_sneak = new Hitbox(-1, 2, -1, 8);// sneaking
	private Hitbox hitbox_used = null;// currently used Hitbox

	// internal values for physics
	private double jumpspeed = 2;
	private double jumpMaxLength = 25;
	private double walkspeed = 6 * m / s;
	private double accelerationGround = 0.3 * m / s;
	private double slowdownGround = 1.2 * m / s;
	private double accelerationAir = 0.2 * m / s;
	private double slowdownAir = 0.8 * m / s;

	// physical abilities
	private double jumpLength;
	private boolean canJump;
	public boolean iscrouching;
	private int jumpcooldown;

	// Player stuff, equipment, hotbar, etc
	public Equipment equipment;
	public Crafting crafting;
	public RecipeList recipeList = new RecipeList();
	private BitSet itemKnowledge = new BitSet();
	private Point hotBar = new Point(0, 0);
	private boolean openInv = false;
	private boolean openEquip = false;
	private boolean openCraft = false;
	private byte openHotBar = 0;
	private byte selected = 0;
	private SpriteSheet itemBackground = new SpriteSheet("/Items/field.png");
	private SpriteSheet itemBackgrounds = new SpriteSheet("/Items/fields.png");

	public Player(Map map) {
		super(map, "Player", 0, 0, new SpriteSheet("/Mobs/sprite_sheet_player.png", 13 * Screen.MAP_SCALE, 16 * Screen.MAP_SCALE));
		ItemList.addRecipesToList(recipeList);
		this.bags = new EnumMap<>(BAG.class);
		this.bagInvs = new EnumMap<>(BAG.class);
		equipment = new Equipment(this, bags);
		crafting = new Crafting(this);
		color = MainConfig.PlrCol;
		xOffset = 6;
		yOffset = 8;
		// sheet.tileWidth = 13*Screen.MAP_SCALE;
		// sheet.tileHeight = 16*Screen.MAP_SCALE;
		try {
			this.bags.get(BAG.BELT_1).getItem(selected).setMouse();
		} catch (NullPointerException e) {
		}
	}

	public void resetBagInv(BAG bagEnum) {
		if (this.bagInvs.containsKey(bagEnum)) {
			this.bagInvs.put(bagEnum, new BagInv(this.bags.get(bagEnum), bagEnum));
		} else {
			this.bagInvs.remove(bagEnum);
		}
	}

	public int getColor() {
		return this.color;
	}

	public byte getAnim() {
		return (byte) anim;
	}

	public byte getDir() {
		return (byte) movingDir;
	}

	public boolean equipItem(BAG bag_enum, Item item) {
		return equipItem(bag_enum, item, false);
	}
	
	public boolean equipItem(BAG bag_enum, Item item, boolean canOverride) {
		if(item != null) {
			if (bag_enum.bagClass.isInstance(item) && (canOverride || !this.bags.containsKey(bag_enum))) {
				this.bags.put(bag_enum, (Bag<?>) item);
				this.bagInvs.put(bag_enum, new BagInv((Bag<?>) item, bag_enum));
				return true;
			}
			return false;
		}else {
			this.bagInvs.remove(bag_enum);
			return this.bags.remove(bag_enum) != null;
		}
	}
	
	public void delItem(Item item) {
		for (Bag<?> bag : this.bags.values()) {
			if (bag.canContain(item) && bag.removeItem(item))
				break;
		}
	}

	/**
	 * Will add the amout specified in <code>count</code> to an existing
	 * ItemStack with the <code>ID</code> If this Method was sucessfull it will
	 * return 0.
	 * 
	 * @param ID
	 * @param count
	 * @return the leftover that could not be added
	 */
	public int addToStack(int ID, int count) {
		while (count > 0) {
			Bag<?> pBag = null;
			int cIndex, pIndex = 0;
			for (Bag<?> cBag : bags.values()) {
				if (cBag.canContain(ItemList.GetItem(ID)) && (cIndex = cBag.hasSpace(ID)) >= 0) {
					if (pBag == null || cBag.getItemPriority() > pBag.getItemPriority()) {
						pBag = cBag;
						pIndex = cIndex;
					}
				}
			}
			if (pBag != null) {
				if (pBag.getItem(pIndex) == null) {
					pBag.setItem(pIndex, ItemList.NewItem(ID));
				}
				count = pBag.getItem(pIndex).addStack(count);
				if (count > 0) {
					continue;
				}
				break;
			} else {
				break;
			}
		}
		return count;
	}

	/**
	 * Will try to add the Item into the Bags with the highes priority. If this
	 * item is a Stack of Items it will try to fit as many Items into the bags
	 * as Possible, during this prozess the stacksize of <code>item</code> will
	 * be changed. After sucess the stacksize will be at 0.
	 * 
	 * @param item
	 * @return true if there was enough space for the whole Item Stack
	 */
	public boolean pickUp(Item item) {
		addItemKnowledge(item.getID());
		while (item.getStack() > 0) {
			Bag<?> pBag = null;
			int cIndex, pIndex = 0;
			for (Bag<?> cBag : bags.values()) {
				if (cBag.canContain(item) && (cIndex = cBag.hasSpace(item, true)) >= 0) {
					if (pBag == null || cBag.getItemPriority() > pBag.getItemPriority()) {
						pBag = cBag;
						pIndex = cIndex;
					}
				}
			}
			if (pBag != null) {
				if (pBag.insertItem(pIndex, item)) {
					return true;
				}
				continue;
			} else {
				return false;
			}
		}
		return true;
	}

	public boolean craftItem(Recipe r) {
		for (Component c : r.educts) {
			int n = c.n;
			for (Bag<?> bag : this.bags.values()) {
				for (int i = 0; i < bag.invSize(); i++) {
					if (bag.getItem(i) != null) {
						if (bag.getItem(i).getID() == c.ID) {
							n -= bag.getItem(i).getStack();
						}
					}
				}
			}
			if (n > 0)
				return false;
		}
		for (Component c : r.educts) {
			int n = c.n;
			for (Bag<?> bag : this.bags.values()) {
				for (int i = 0; i < bag.invSize(); i++) {
					if (bag.getItem(i) != null) {
						if (bag.getItem(i).getID() == c.ID) {
							n = bag.getItem(i).delStack(n);
							if (bag.getItem(i).getStack() <= 0) {
								this.delItem(bag.getItem(i));
							}
						}
					}
				}
			}
		}
		Item product;
		for (Component c : r.products) {
			if(!pickUp(product = ItemList.NewItem(c.ID, c.n))) {
				map.addItemEntity(new ItemEntity(product, map, (int)x, (int)y));
			}
		}
		// System.out.println("craftable");
		return true;
	}
	
	public void addItemKnowledge(int id) {
		itemKnowledge.set(id);
	}
	
	public boolean hasItemKnowledge(int id) {
		return itemKnowledge.get(id);
	}
	
	public List<Vector2d> temp;

	public void tick(int numTick) {
		// map.setlight((int)x, (int)y, (byte) (64));map.updateLight((int)x,
		// (int)y); // just for testing lightsystem
		/* MOVEMENT */
		if (Keys.JUMP.isPressed() && canJump) {
			speedY = -jumpspeed;
			jumpLength++;
			if(jumpLength>jumpMaxLength) {
				canJump = false;
				jumpcooldown = 10;
			}
		}else if(jumpLength>0){
			canJump = false;
			jumpcooldown = 10;
			jumpLength = 0;
		}
		if (Keys.RIGHT.isPressed() ^ Keys.LEFT.isPressed()) {
			if (Keys.LEFT.isPressed() && (speedX > -walkspeed || (!iscrouching && speedX > -walkspeed / 2))) {
				speedX -= speedX > 0 ? (onGround ? slowdownGround : slowdownAir) : (onGround ? accelerationGround : accelerationAir);
				if (iscrouching) {
					if (speedX < -walkspeed / 2)
						speedX = -walkspeed / 2;
				} else {
					if (speedX < -walkspeed)
						speedX = -walkspeed;
				}
				movingDir = DIRECTION_LEFT;
			}
			if (Keys.RIGHT.isPressed() && (speedX < walkspeed || (!iscrouching && speedX < walkspeed / 2))) {
				speedX += speedX < 0 ? (onGround ? slowdownGround : slowdownAir) : (onGround ? accelerationGround : accelerationAir);
				if (iscrouching) {
					if (speedX > walkspeed / 2)
						speedX = walkspeed / 2;
				} else {
					if (speedX > walkspeed)
						speedX = walkspeed;
				}
				movingDir = DIRECTION_RIGHT;
			}
			isMoving = true;
		} else {
			if (speedX < 0) {
				speedX += onGround ? slowdownGround : slowdownAir;
				if (speedX > 0)
					speedX = 0;
			} else if (speedX > 0) {
				speedX -= onGround ? slowdownGround : slowdownAir;
				if (speedX < 0)
					speedX = 0;
			}
			isMoving = false;
		}
		iscrouching = Keys.CROUCH.isPressed() && onGround;
		if (speedX != 0)
			numSteps++;
		if (jumpcooldown > 0 && onGround)
			jumpcooldown--;
		if (jumpcooldown == 0)
			canJump = true;

		/* COLLISION */
		hitbox_used = iscrouching ? hitbox_sneak : hitbox_main;
		
		Collision c = Collision.canMoveTo(map, hitbox_used, x, y, new Vector2d(speedX, speedY), true);
		
		onGround = (c.collisionFlags & Collision.Y_COLLISION) != 0 && speedY > 0;
		
		if((c.collisionFlags & Collision.X_COLLISION) != 0) {
			speedX = 0;
		}
		if((c.collisionFlags & Collision.Y_COLLISION) != 0) {
			speedY = 0;
		}
		y = c.entityPos.y;
		x = c.entityPos.x;
/*		if (() != null) {
//			for (Vector2d vec : c.collisionPos) {
//				System.out.printf("pX:%f pY:%f\n", vec.x, vec.y);
//			}
//			System.out.printf("cX:%f cY:%f\n", x, y);
			if((c.collisionFlags & Collision.X_COLLISION) != 0) {
				speedX = 0;
				x = c.entityPos.x;
			}else {
				x += speedX;
			}
			if((c.collisionFlags & Collision.Y_COLLISION) != 0) {
				speedY = 0;
				y = c.entityPos.y;
			}else {
				y += speedY;
			}
//			speedX=0;
//			speedY=0;
			
			temp = c.collisionPos;
//			System.out.printf("eX:%f eY:%f sX:%f sY:%f cf:%s\n", x, y, speedX, speedY, Integer.toBinaryString(c.collisionFlags));
		}else {
			x+=speedX;
			y+=speedY;
		}*/
//		if(speedX!=0 || speedY!=0)System.out.printf(" X:%f  Y:%f sX:%f sY:%f\n", x, y, speedX, speedY);
		
		//onGround = Collision.onGround(map, hitbox_used, (int) x, (int) y);
		
		/* COLLECT ITEMS */
		map.getItemEntities(e -> Point.distanceSq(x, y, e.x, e.y) <= 100).forEach(i -> i.collectWith(this::pickUp));

		/* HOTBAR */
		if (MouseInput.mousem.click() || Keys.HOTBAR.click()) {
			if (openHotBar == 0) {
				hotBar.x = MouseInput.mouse.x;
				hotBar.y = MouseInput.mouse.y;
				openHotBar++;
			} else
				openHotBar = 0;
		}
		if (openHotBar != 0) {
			if (this.bags.get(BAG.BELT_1) != null) {
				if (openHotBar < 60 + this.bags.get(BAG.BELT_1).invSize())
					openHotBar += 5;
				if (Math.sqrt(Math.pow((MouseInput.mouse.x - hotBar.x), 2) + Math.pow((MouseInput.mouse.y - hotBar.y), 2)) >= 20 + this.bags.get(BAG.BELT_1).invSize()) {
					selected = (byte) (this.bags.get(BAG.BELT_1).invSize() / 2 - Math.atan2(MouseInput.mouse.x - hotBar.x, MouseInput.mouse.y - hotBar.y) / (Math.PI * 2 / this.bags.get(BAG.BELT_1).invSize()));
				}
			}
		}
		if (this.bags.containsKey(BAG.BELT_1) && this.bags.get(BAG.BELT_1).getItem(selected) != null) {
			this.bags.get(BAG.BELT_1).getItem(selected).setMouse();
		}

		/* EQUIPMENT */
		if (Keys.EQUIP.click()) {
			openEquip = !openEquip;
		}
		if (openEquip)
			equipment.tick();

		/* CRAFTING */
		if (Keys.CRAFT.click()) {
			openCraft = !openCraft;
		}
		if (openCraft)
			crafting.tick();

		/* INVENTORY */
		if (Keys.INV.click()) {
			openInv = !openInv;
		}
		if (openInv) {
			for (BagInv bagInv : bagInvs.values()) {
				if (bagInv != null) {
					bagInv.tick();
				}
			}
		}

		if (this.bags.containsKey(BAG.BELT_1) && this.bags.get(BAG.BELT_1).getItem(selected) != null && MouseInput.mousel.isClickable()) {
			this.bags.get(BAG.BELT_1).getItem(selected).holdItem(this, map);
		} else {
			MouseInput.mousel.click();
		}

		/* ANIMATION */
		if (isMoving) {
			if (numSteps % 24 == 0)
				anim = 3;
			if (numSteps % 24 == 4)
				anim = 4;
			if (numSteps % 24 == 8)
				anim = 5;
			if (numSteps % 24 == 12)
				anim = 6;
			if (numSteps % 24 == 16)
				anim = 5;
			if (numSteps % 24 == 20)
				anim = 4;
			if (iscrouching) {
				if (numSteps % 30 < 30)
					anim = 7;
				if (numSteps % 30 < 15)
					anim = 8;
			}
		} else {
			if (numTick % 30 < 30)
				anim = 0;
			if (numTick % 30 < 15)
				anim = 1;
			if (this.bags.get(BAG.BELT_1) != null) {
				if (this.bags.get(BAG.BELT_1).getItem(selected) != null) {
					if (this.bags.get(BAG.BELT_1).getItem(selected).getAnim() != 0)
						anim = this.bags.get(BAG.BELT_1).getItem(selected).getAnim();
				}
			}
			if (iscrouching)
				anim = 9;
		}
		if (!onGround) {
			anim = 2;
		}
	}

	public void render() {
		Screen.drawMapSprite((x - xOffset), (y - yOffset), sheet, anim, movingDir == 1, false, color);

		if ((anim == 10 || anim == 11) & this.bags.get(BAG.BELT_1) != null) {
			try {
				if (anim == 10)
					this.bags.get(BAG.BELT_1).getItem(selected).renderOnMap((int) (x + 3 - movingDir * 10), (int) (y - 5), movingDir == 1, false);
				if (anim == 11)
					this.bags.get(BAG.BELT_1).getItem(selected).renderOnMap((int) (x + 4 - movingDir * 12), (int) (y - 3), movingDir == 1, false);
			} catch (NullPointerException e) {
			}
		}
		if (anim == 12 & this.bags.get(BAG.BELT_1) != null) {
			try {
				this.bags.get(BAG.BELT_1).getItem(selected).renderOnMap((int) (x + 3 - movingDir * 8), (int) (y - 2), movingDir == 1, false);
			} catch (NullPointerException e) {
			}
		}
	}

	public void renderGUI() {
		/* EQUIPMENT */
		if (openEquip) {
			equipment.render();
		}

		/* CRAFTING */
		if (openCraft) {
			crafting.render();
		}

		/* INVENTORY */
		if (openInv) {
			for (BagInv bagInv : bagInvs.values()) {
				if (bagInv != null) {
					bagInv.render();
				}
			}
		}

		/* HOTBAR */
		if (openHotBar != 0) {
			if (this.bags.get(BAG.BELT_1) != null) {
				for (int i = 0; i < this.bags.get(BAG.BELT_1).invSize(); i++) {
					Screen.drawGUISprite(hotBar.x + (int) (openHotBar * Math.sin((i + 0.5) * Math.PI * 2 / this.bags.get(BAG.BELT_1).invSize())) - 18, hotBar.y + (int) (-openHotBar * Math.cos((i + 0.5) * Math.PI * 2 / this.bags.get(BAG.BELT_1).invSize())) - 18, itemBackground);
					if (i == selected) {
						Screen.drawGUISprite(hotBar.x + (int) (openHotBar * Math.sin((selected + 0.5) * Math.PI * 2 / this.bags.get(BAG.BELT_1).invSize())) - 18, hotBar.y + (int) (-openHotBar * Math.cos((selected + 0.5) * Math.PI * 2 / this.bags.get(BAG.BELT_1).invSize())) - 18, itemBackgrounds);
					}
					try {
						this.bags.get(BAG.BELT_1).getItem(i).render(hotBar.x + (int) (openHotBar * Math.sin((i + 0.5) * Math.PI * 2 / this.bags.get(BAG.BELT_1).invSize())) - 15, hotBar.y + (int) (-openHotBar * Math.cos((i + 0.5) * Math.PI * 2 / this.bags.get(BAG.BELT_1).invSize())) - 15, true);
					} catch (NullPointerException e) {
					}
				}
			}
		}
		if(SinglePlayer.debuginfo) {
			Screen.drawLine(x, y, x+speedX, y+speedY, 0xffff0000, true);
			if(hitbox_used != null)hitbox_used.render(x, y);
			if(temp!=null)Screen.drawLines(temp, 0xffff0000, true);
		}
	}

	public void save(ArrayList<Byte> file) {
		ConvertData.I2B(file, (int) x);
		ConvertData.I2B(file, (int) y);
		for (BAG bagEnum : Player.BAG.values()) {
			if (this.bags.containsKey(bagEnum)) {
				this.bags.get(bagEnum).save(file);
			} else {
				ConvertData.I2B(file, 0);
			}
		}
	}

	public void load(ArrayList<Byte> file) {
		x = ConvertData.B2I(file);
		y = ConvertData.B2I(file);
		for (BAG bagEnum : Player.BAG.values()) {
			Bag<?> bag = (Bag<?>) ItemList.NewItem(ConvertData.B2I(file));
			if (bag != null) {
				bag.load(file);
				this.equipItem(bagEnum, bag);
			}
		}
	}

	public void create() {
		this.x = 524800;
		int h = 524500;// for(h = 524500; PixelList.GetMat(this.x, h,
						// map,1).ID==0 && h < 525000; h++){}
		this.y = h - yOffset;

		// initializing Equipment
		this.equipItem(BAG.BELT_1, ItemList.NewItem("SmallBeltBag"));
		this.equipItem(BAG.ITEM_1, ItemList.NewItem("NormalItemBag"));
		this.equipItem(BAG.MAT_1, ItemList.NewItem("NormalMaterialBag"));
		this.equipItem(BAG.TOOL_1, ItemList.NewItem("SmallToolBag"));

		// initializing Inventory
		Pickaxe newpick = (Pickaxe) ItemList.NewItem("StonePickaxe");
		this.pickUp(newpick);
		if (Game.devmode) {
			Item newitem = ItemList.NewItem(64);
			newitem.addStack(999);
			this.pickUp(newitem);
			newitem = ItemList.NewItem(16);
			newitem.addStack(999);
			this.pickUp(newitem);
			newitem = ItemList.NewItem(17);
			newitem.addStack(999);
			this.pickUp(newitem);
			newitem = ItemList.NewItem(18);
			newitem.addStack(999);
			this.pickUp(newitem);

			this.pickUp(ItemList.NewItem(333));
			this.pickUp(ItemList.NewItem(331));
			newitem = ItemList.NewItem(400);
			newitem.addStack(999);
			this.pickUp(newitem);
			this.pickUp(ItemList.NewItem(332));
			
			this.equipItem(BAG.ITEM_2, ItemList.NewItem("NormalItemBag"));
			this.equipItem(BAG.MAT_2, ItemList.NewItem("NormalMaterialBag"));
		}
	}
}
