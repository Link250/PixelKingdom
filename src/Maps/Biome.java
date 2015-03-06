package Maps;

public abstract class Biome {
	
	public String name;
	public int col;
	public byte ID;
	
	public Biome(){
		
	}

	public abstract short[][][] generate();
}
