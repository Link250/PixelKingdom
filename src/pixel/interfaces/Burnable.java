package pixel.interfaces;

public interface Burnable {
	
	public static byte MAX_BURNTIME = 100;
	/**
	 * 0 -> no burning;
	 * 100 -> max burning
	 */
	public byte getBurnStrength();
}
