package map;

public abstract class Biome {
	
	public String name;
	public int col;
	public byte ID;
	private double gravity = 1.0;
	
	public Biome(){
		
	}

	public abstract short[][][] generate(int chunkX, int chunkY, long seed);
	
	public double getGravity() {
		return gravity;
	}
}
