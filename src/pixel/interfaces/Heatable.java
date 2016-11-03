package pixel.interfaces;

/**
 * This interface can be used for anything that should should be able to transfer or store heat, for example an Oven and smeltable Ores
 * @author QuantumHero
 *
 */
public interface Heatable{

	/**
	 * 
	 * @param heat the amount of heat that should be added to this Pixel
	 * @return the amout of heat that was actually used
	 */
	public int heatUp(int heat);
	
	/**
	 * 
	 * @return the heat of this Pixel as an Integer
	 */
	public int getHeat();
}
