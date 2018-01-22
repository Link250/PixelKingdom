package map.biomesList;

import java.awt.Point;
import dataUtils.OpenSimplexNoise;
import map.Biome;
import map.Chunk;
import map.MapGenerator;
import map.trees.TreeOak;

public class Hills extends Biome {

	public Hills() {
		name = "Hills";
		ID = 3;
	}

	public short[][][] generate(int chunkX, int chunkY, long seed) {
		OpenSimplexNoise noise = new OpenSimplexNoise(seed);
		Point nOff = new Point(chunkX*Chunk.width, chunkY*Chunk.height);
		int width = 1024, height = 1024;
		short[][][] newMap = new short[3][width][height];
		int[] baseLine = new int[width], mainLine = new int[width], dirtLine = new int[width], sandLine = new int[width];
		for(int x = 0; x < width; x++){
			baseLine[x] = (int) Math.round(noise.eval((x+nOff.x)/500.0, 0)*256+512-64);
			mainLine[x] = (int) Math.round(baseLine[x] + noise.eval((x+nOff.x)/100.0, 1)*64);
			sandLine[x] = (int) Math.round((mainLine[x] + noise.eval((x+nOff.x)/30.0, 2)*16+32) * (mainLine[x] < 512 ? mainLine[x]/512.0 : 1));
			dirtLine[x] = (int) Math.round(mainLine[x] + noise.eval((x+nOff.x)/40.0, 3)*32+64);
		}
		MapGenerator.smoothOutBy(baseLine, 2);
		MapGenerator.smoothOutBy(mainLine, 2);
		MapGenerator.smoothOutBy(sandLine, 2);
		MapGenerator.smoothOutBy(dirtLine, 2);
		for(int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
				if(y < mainLine[x]) {
					newMap[0][x][y] = 0;
				}else if(y < dirtLine[x]){
					if(y < sandLine[x]) {
						newMap[0][x][y] = 8;
					}else if(y == mainLine[x]){
						newMap[0][x][y] = 3;
					}else {
						newMap[0][x][y] = 2;
					}
				}else {
					newMap[0][x][y] = 1;
				}
				newMap[1][x][y]= (short) (y > 512 && newMap[0][x][y] == 0 ? 1 : 0);
			}
		}
		
		double phaseY = 768;
		for(int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
				if(noise.eval((x+nOff.x)/80.0, (y+nOff.y)/80.0, 7) > 0.5+(y>phaseY ? (y-phaseY)/(height-phaseY) : 0)) {
					if(newMap[0][x][y]!=0)newMap[0][x][y]= 7;
				}
			}
		}
		for(int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
				if(Math.abs(noise.eval((x+nOff.x)/80.0, (y+nOff.y)/80.0, 2)) < 0.5-(y>phaseY ? (y-phaseY)/(height-phaseY) : 0)) {
					if(newMap[0][x][y]==1)newMap[0][x][y]= 2;
				}
			}
		}
		for(int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
				newMap[2][x][y] = newMap[0][x][y];
			}
		}
		
		for (int x = 100; x < width-100; x++) {
			if(newMap[0][x][mainLine[x]]==3 && Math.random()*60 < 1 && mainLine[x] > sandLine[x]) {
				x+=30;
				TreeOak.generate(x, mainLine[x], newMap);
			}
		}
		return newMap;
	}
}
