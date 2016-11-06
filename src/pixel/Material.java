package pixel;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import item.Tool;
import main.Game;
import main.MouseInput.Mouse;
import map.Map;

public abstract class Material<UDSType extends UDS> {
	
	public int ID = 0;
	protected String name = "unnamed";
	protected String displayName = "unnamed";
	/**
	 * 0 -> Can't be mined
	 */
	public double usePickaxe = 0;
	public boolean solid = true;
	
	/**this UDS is HOLY !!! it is used to create new UDS Objects for this Material <b>SO NEVER EVER DELETE IT</b>*/
	private UDSType udsType = null;
	
	protected UDSType uds = null;
	
	protected int[] texture = null;
	protected int textureWidth;
	protected int textureHeight;
	
	protected short frontLightReduction = 8;
	protected short backLightReduction = 255;
	
	public Material(UDSType uds){
		this.udsType = uds;
	}
	
	public boolean breakPixel(Tool item) {
		return false;
	}
	
	public void mouseClick(int x, int y, Mouse mouse) {
	}
	
	public void mouseOver(int x, int y) {
	}
	
	public final double usePickaxe(){
		return usePickaxe;
	}

	public final String getName(){
		return name;
	}
	
	public final String getDisplayName(){
		return displayName;
	}
	
	/**
	 * creates a new Intance of the Generic AD Type from the Material this method is called on
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
	 * Creates a new AD on the Position <b>x y l</b>.<br>
	 * If the AD Length of this Pixel is 0 or negative
	 * the current AD will be set to <b><code>null</code></b>.
	 * <pre>The AD will have the Generic Type specific to the Material this Method is called from</pre>
	 * @param x
	 * @param y
	 * @param l
	 * @param map
	 */
	public final UDS createUDS(){
		return this.canHaveUDS() ? this.getNewUDS() : null;
	}
	
	protected void loadTexture() {
		loadTexture("/MapTextures/"+name+".png");
	}

	protected void loadTexture(String path) {
		BufferedImage image = null;
		
		try {
			image = ImageIO.read(Material.class.getResourceAsStream(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(image == null){ return;}
		
		textureWidth = image.getWidth();
		textureHeight = image.getHeight();

		texture = image.getRGB(0, 0, textureWidth, textureHeight, null, 0, textureWidth);
	}
	
	public short tickLight(int x, int y, int l, Map map) {return 0;}
	
	public short frontLightReduction(){return this.frontLightReduction;}
	
	public short backLightReduction(){return this.backLightReduction;}
	
	public boolean tick(int x, int y, int l, int numTick, Map map){return false;}
	
	public int render(int x, int y, int l, Map map) {
		return texture[x%textureWidth + (y%textureHeight)*textureWidth];
	}
	
	public int getColor() {
		return texture!=null ? texture[0] : 0;
	}
}
