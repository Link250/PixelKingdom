package map.biomesList;

import map.Biome;

public class Space extends Biome {

	public Space() {
		name = "Space";
		ID = 0;
	}

	public short[][][] generate(int chunkX, int chunkY, long seed) {
		int width = 1024, height = 1024;
		short[][][] newMap = new short[3][width][height];
		return newMap;
	}
}
