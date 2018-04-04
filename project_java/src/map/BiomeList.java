package map;

import java.util.ArrayList;

import map.biomesList.Caves;
import map.biomesList.Forest;
import map.biomesList.Plains;
import map.biomesList.Space;
import map.biomesList.Underground;

public class BiomeList {

	public static ArrayList<Biome> biomelist = new ArrayList<Biome>();
	
	public BiomeList(){
		biomelist.add(new Plains());
		biomelist.add(new Forest());
		biomelist.add(new Space());
		biomelist.add(new Underground());
		biomelist.add(new Caves());
	}
	
	public static Biome GetBiome(int ID){
		for(Biome b : biomelist){if(b.ID==ID)return b;}
		return null;
	}
}
