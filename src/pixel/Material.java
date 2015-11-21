package pixel;

import gfx.Screen;
import main.Game;
import map.Map;

public abstract class Material {
	
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
	public int adl = 0;
	private AD ad = null;
	
	public Material(){
		
	}
	
	public final double usePickaxe(){
		return usePickaxe;
	}

	public final String getName(){
		return name;
	}
	
	public AD getNewAD() {
		try {
			return ad.getClass().newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Creates a new AD of the length <b>adl</b> on the Position <b>x y l</b>.<br>
	 * If <b>adl</b> is 0 or negative the current AD will be set to <b><code>null</code></b>.
	 * @param x
	 * @param y
	 * @param l
	 * @param map
	 * @param adl the length of the AD that should be created
	 */
	public final void createAD(int x, int y, int l, Map map, int adl){
		map.setAD(x, y, l, adl>0 ? new AdditionalData(adl) : null);
	}

	/**
	 * Creates a new AD on the Position <b>x y l</b>.<br>
	 * If the AD Length of this Pixel is 0 or negative
	 * the current AD will be set to <b><code>null</code></b>.
	 * @param x
	 * @param y
	 * @param l
	 * @param map
	 */
	public final void createAD(int x, int y, int l, Map map){
		map.setAD(x, y, l, this.adl>0 ? new AdditionalData(adl) : null);
	}

	public final void checkAD(int x, int y, int l, Map map){
		AdditionalData temp = map.getAD(x, y, l);
		if(temp == null){
			if(adl>0){map.setAD(x, y, l, new AdditionalData(adl));Game.logError("Corrupted AD");}
		}else{
			if(adl==0){temp = null;if(map.getAD(x, y, l)!=null)Game.logWarning("Could not delete AD");}
		}
	}
	
	@SuppressWarnings({ "unchecked", "hiding" })
	public final <AD> AD getAD(int x, int y, int l, Map map) {
		return (AD) map.getAD(x, y, l);
	}

	public boolean tick(int x, int y, int l, int numTick, Map map){return false;}
	
	public void render(int x, int y, int l, Map map, Screen screen) {
		screen.drawMaterial(x, y, ID, l);
	}
}
