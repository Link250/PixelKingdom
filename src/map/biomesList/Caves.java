package map.biomesList;

import dataUtils.OpenSimplexNoise;
import map.Biome;

public class Caves extends Biome {

	public Caves() {
		name = "Caves";
		ID = 52;
	}

	public short[][][] generate(int chunkX, int chunkY, long seed) {
		OpenSimplexNoise noise = new OpenSimplexNoise((long)(Math.random()*Long.MAX_VALUE));
		int width = 1024, height = 1024;
		short[][][] newMap = new short[3][width][height];
		for(int x = 0; x < width; x++){
			for(int y = 0; y < width; y++){
				newMap[0][x][y]= (short) ((noise.eval(x/50.0, y/50.0, 1)+1)*noise.eval(x/100.0, y/100.0) > 0.2 ? 0 : 1);
				newMap[2][x][y]=1;
			}
		}
/*		for(int x = 0; x < width; x++){
			for(int y = 0; y < width; y++){
				if(newMap[0][x][y] == 1 && eval(noise.eval(x/30.0, y/30.0, 16), 0.5, 0.7)) {
					newMap[0][x][y] = 16;
				}
			}
		}
		for(int x = 0; x < width; x++){
			for(int y = 0; y < width; y++){
				if(newMap[0][x][y] == 1 && eval(noise.eval(x/50.0, y/50.0, 2), 0.2, 0.6)) {
					newMap[0][x][y] = 2;
				}
			}
		}*/
		return newMap;
	}
	
	public boolean eval(double in, double min, double max) {
		if(in > min) {
			return (in + Math.random()*(max-min)) > max;
		}return false;
	}
}
