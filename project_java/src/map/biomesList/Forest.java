package map.biomesList;

import java.util.Random;

import map.Biome;
import map.MapGenerator;

public class Forest extends Biome {

	public Forest() {
		name = "Forest";
		ID = 2;
	}

	public short[][][] generate() {
		double width = 1024, height = 1024;
		short[][][] newMap = new Plains().generate();
		Random r = new Random(System.nanoTime());
		
//		MapGenerator.genCavesArea(0,600,1024,400,newMap); //too much Caves

		for(int y = 0; y < height/2; y ++){
			for(int x = 32; x < width-32; x ++){
				if(newMap[0][x][y+1]==3 && r.nextDouble()*33 <= 1){MapGenerator.newTree(x,y,newMap);}
			}
		}
		
		return newMap;
	}
}
