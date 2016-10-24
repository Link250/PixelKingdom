package pixel;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import gfx.SpriteSheet;
import main.Game;
import map.Map;

public abstract class Material<ADType extends AD> {
	
	public int ID = 0;
	/**
	 * 0 -> no burning;
	 * 100 -> max burning
	 */
	public byte burnable = 0;
	public String name = "unnamed";
	public String displayName = "unnamed";
	/**
	 * 0 -> Can't be mined
	 */
	public double usePickaxe = 0;
	public boolean tick = false;
	public boolean solid = true;
	
	/**this AD is HOLY !!! it is used to create new ADs for this Material <b>SO NEVER EVER DELETE IT</b>*/
	private ADType adtype = null;
	
	protected ADType ad = null;
	
	protected int[] texture = null;
	protected int textureWidth;
	protected int textureHeight;
	
	public Material(ADType ad){
		this.adtype = ad;
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
	 * @return {@link pixel.AD}
	 */
	public AD getNewAD() {
		try {
			return (AD) this.adtype.getClass().newInstance();
		}catch(NullPointerException e){
			Game.logError("AD was not Initialized yet !!! ID:"+this.ID+" Name:"+this.name);
		}catch(InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean canHaveAD() {
		return adtype!=null;
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
	public final AD createAD(){
		return this.canHaveAD() ? this.getNewAD() : null;
	}
	
	protected void loadTexture() {
		loadTexture("/Map/"+name+".png");
	}

	protected void loadTexture(String path) {
		BufferedImage image = null;
		
		try {
			image = ImageIO.read(SpriteSheet.class.getResourceAsStream(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(image == null){ return;}
		
		texture = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
	}
	
	public byte tickLight(int x, int y, int l, Map map) {return 0;}
	
	public boolean tick(int x, int y, int l, int numTick, Map map){return false;}
	
	public int render(int x, int y, int l, Map map) {
		return texture[x%textureWidth + (y%textureHeight)*textureWidth];
	}
}
