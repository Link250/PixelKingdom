package pixel;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import entities.Entity;
import entities.Player;
import item.Item;
import item.ItemList;
import item.MiningTool;
import item.RecipeList;
import main.Game;
import main.MouseInput.Mouse;
import map.Map;

public abstract class Material<UDSType extends UDS> {
	public static final int MINING_TYPE_NONE = 0;
	public static final int MINING_TYPE_PICKAXE = 0b1;
	public static final int MINING_TYPE_AXE = 0b10;
	public static final int MINING_TYPE_SHOVEL = 0b100;
	public static final int MINING_TYPE_HAMMER = 0b1000;
	
	public int ID = 0;
	protected int itemID = 0;
	
	/**the internally used name for this Material*/
	protected String name = "unnamed";
	/**the internally used name for this Material*/
	protected String displayName = "unnamed";
	protected int requiredType = MINING_TYPE_NONE;
	protected double requiredTier = 1;
	protected double miningResistance = 1/25;
	protected int solidity = Map.SOLID_ALL;
	protected int friction = 1;
	
	/**this UDS is HOLY !!! it is used to create new UDS Objects for this Material <b>SO NEVER EVER DELETE IT</b>*/
	private final UDSType udsType;
	
	protected UDSType uds = null;
	
	protected int[] texture = null;
	protected int textureWidth = 1;
	protected int textureHeight = 1;
	
	/** an array of length 3 which contains values from 0 to -255 for the front light reduction of Red, Green and Blue */
	protected int[] frontLightReduction = new int[] {-8, -8, -8};
	/** an array of length 3 which contains values from 0 to -255 for the back light reduction of Red, Green and Blue */
	protected int[] backLightReduction = new int[] {-255, -255, -255};
	/** an array of length 3 which contains values from 0 to 255 for the front light reduction of Red, Green and Blue */
	protected int[] light = new int[] {0, 0, 0};
	
	public Material(UDSType uds){
		this.udsType = uds;
	}
	
	public boolean breakPixel(Map map, int x, int y, int l, MiningTool item) {
		map.spawnItemEntity(ItemList.NewItem(itemID>0 ? itemID : ID), x, y);
		map.setID(x, y, l, 0);
		return true;
	}
	
	public void mouseClick(Map map, int x, int y, Mouse mouse, Item item) {}
	
	public void mouseOver(Map map, int x, int y, Item item) {}
	
	public final String getName(){
		return name;
	}
	
	public final String getDisplayName(){
		return displayName;
	}
	
	/**
	 * creates a new Intance of the Generic UDS Type from the Material this method is called on
	 * @return {@link pixel.UDS}
	 */
	public UDS getNewUDS() {
		try {
			return (UDS) this.udsType.getClass().newInstance();
		}catch(NullPointerException e){
			Game.logError("UDS was not Initialized yet !!! ID:"+this.ID+" Name:"+this.name);
		}catch(InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean canHaveUDS() {
		return udsType!=null;
	}
	
	/**
	 * Creates a new UDS on the Position <b>x y l</b>.<br>
	 * If the UDS Type is null
	 * the current UDS will be set to <b><code>null</code></b>.
	 * <pre>The UDS will have the Generic Type specific to the Material this Method is called from</pre>
	 * @param x
	 * @param y
	 * @param l
	 * @param map
	 */
	public final UDS createUDS(){
		return this.canHaveUDS() ? this.getNewUDS() : null;
	}
	
	protected void loadTexture() {
		Point size = new Point();
		texture = loadTexture("/MapTextures/"+name+".png", size);
		this.textureWidth = size.x;
		this.textureHeight = size.y;
	}

	protected int[] loadTexture(String path, Point size) {
		BufferedImage image = null;
		
		try {
			image = ImageIO.read(Material.class.getResourceAsStream(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(image == null){ return null;}
		
		size.x = image.getWidth();
		size.y = image.getHeight();

		return image.getRGB(0, 0, size.x, size.y, null, 0, size.x);
	}
	
	public int[] tickLight(int x, int y, int l, Map map) {return this.light;}
	
	public int[] frontLightReduction(){return this.frontLightReduction;}
	
	public int[] backLightReduction(){return this.backLightReduction;}
	
	public boolean tick(int x, int y, int l, Map map, int numTick){return false;}
	
	public int render(int x, int y, int l, Map map) {
		return getColor(x, y);
	}
	
	public int getColor(int x, int y) {
		return texture[x%textureWidth + (y%textureHeight)*textureWidth];
	}
	
	public int getColor() {
		return texture!=null ? texture[0] : 0;
	}
	
	public int getSolidity() {
		return solidity;
	}
	
	public double getMiningResistance() {
		return miningResistance;
	}
	
	public double getRequiredTier() {
		return requiredTier;
	}
	
	public int getRequiredType() {
		return requiredType;
	}
	
	public boolean canBeMinedBy(Player plr, MiningTool tool) {
		return (this.requiredTier<=tool.getMiningTier()) && ((tool.getMiningType() & this.requiredType) > 0);
	}
	
	public boolean mine(Player plr, MiningTool tool) {
		return false;
	}
	
	public int getFriction() {
		return this.friction;
	}
	
	public void collideWith(Entity entity) {
		return;
	}
	
	public void passThroughWith(Entity entity) {
		return;
	}
	
	public void addRecipes(RecipeList recipeList) {
		return;
	}
}
