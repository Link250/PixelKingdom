package map.biomesList;

import java.awt.Point;

import com.sun.javafx.geom.Rectangle;

import dataUtils.OpenSimplexNoise;
import map.Biome;
import map.Chunk;
import map.MapGenerator;

public class Underground extends Biome {

	public Underground() {
		name = "Underground";
		ID = 51;
	}

	public short[][][] generate(int chunkX, int chunkY, long seed) {
		int width = 1024, height = 1024;
		short[][][] newMap = new short[3][width][height];
		for(int x = 0; x < width; x++){
			for(int y = 0; y < width; y++){
				newMap[0][x][y]=1;
				newMap[2][x][y]=1;
			}
		}

		MapGenerator.genCavesArea(new Rectangle(0,0,1024,1024),newMap, new OpenSimplexNoise(seed), new Point(chunkX*Chunk.width, chunkY*Chunk.height));

		for(int i = 0; i < 50; i++){
			MapGenerator.genOreCluster((int)(Math.random()*1024), (int)(Math.random()*1024), 2, 1, 50, newMap[0]);
		}

		for(int n = 0; n < 33; n++){
			int x = (int)(Math.random()*1000+10);
			int y = (int)(Math.random()*1000+10);
			MapGenerator.genOreCluster(x, y, 16, 1, 10, newMap[0]);
		}
		for(int n = 0; n < 22; n++){
			int x = (int)(Math.random()*1000+10);
			int y = (int)(Math.random()*1000+10);
			MapGenerator.genOreCluster(x, y, 17, 1, 7, newMap[0]);
		}
		
		return newMap;
	}
}
