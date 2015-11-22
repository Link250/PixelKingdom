package pixel;

import gfx.Screen;
import main.Game;
import map.Map;

public abstract class Material<ADType extends AD> {
	
	public int ID = 0;
	/**
	 * 0 -> no burning;
	 * 100 -> max burning
	 */
	public byte burnable = 0;
	public String name = "Unnamed";
	/**
	 * 0 -> Can't be mined
	 */
	public double usePickaxe = 0;
	public boolean tick = false;
	public boolean solid = true;
	
	/**this AD is HOLY !!! it is used to create new ADs for this Material <b>SO NEVER EVER DELETE IT</b>*/
	private ADType adtype = null;
	
	protected ADType ad = null;
	
	public Material(ADType ad){
		this.adtype = ad;
	}
	
	public final double usePickaxe(){
		return usePickaxe;
	}

	public final String getName(){
		return name;
	}
	
	/**
	 * creates a new Intance of the Generic AD Type from the Material this method is called on
	 * @return {@link pixel.AD}
	 */
	public AD getNewAD() {
		try {
			return (AD) this.adtype.getClass().newInstance();
		}catch(NullPointerException e){
			System.out.println(this.adtype);
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
	public final void createAD(int x, int y, int l, Map map){
		map.setAD(x, y, l, this.canHaveAD() ? getNewAD() : null);
	}

	public final void checkAD(int x, int y, int l, Map map){
		AD temp = map.getAD(x, y, l);
		if(temp == null){
			if(this.canHaveAD()){map.setAD(x, y, l, getNewAD());Game.logError("Corrupted AD");}
		}else{
			if(!this.canHaveAD()){temp = null;if(map.getAD(x, y, l)!=null)Game.logWarning("Could not delete AD");}
		}
	}
	
	public boolean tick(int x, int y, int l, int numTick, Map map){return false;}
	
	public void render(int x, int y, int l, Map map, Screen screen) {
		screen.drawMaterial(x, y, ID, l);
	}
}
