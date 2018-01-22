package map.trees;

import map.Map;
import map.MapGenerator;

public class TreePalm {
	private static final double maxDiff = Math.PI/500;
	
	public static void generate(int x, int y, short[][][] map){
		double treeSize = Math.random()*2+8;
		int heightL = y-10, heightR = y-10;
		for (int h = y-10; h < y+10; h++) {
			if(map[0][x-5][heightL] == 0 && map[0][x-5][h] != 0)heightL = h;
			if(map[0][x+5][heightR] == 0 && map[0][x+5][h] != 0)heightR = h;
		}
		double deg = Math.atan2(10, heightL-heightR)+Math.PI;
		double diff = Math.random()*maxDiff*2-maxDiff;
		double size = treeSize, stemX = x, stemY = y, stemDeg = deg;
		do {
			MapGenerator.DrawPixelCircle(stemX, stemY, size/2, map[Map.LAYER_FRONT], (byte)4, true);
			if(size<2)MapGenerator.DrawPixelCircle(stemX, stemY, 3+Math.random()*(2), map[Map.LAYER_BACK], (byte)5, false);
			stemDeg += diff;
			stemX += Math.cos(stemDeg);
			stemY += Math.sin(stemDeg);
			size -= 0.05;
		}while(size > 4);
		
		for (double d = 0; d < treeSize/2; d++) {
			drawLeaf(stemX, stemY, (stemDeg-Math.PI/4)+Math.PI/2*d/(treeSize/2), 1, map, treeSize/2, false);
		}
		
		for (double d = -treeSize/2; d < treeSize/2; d+=2) {
			drawRoot(x+d, y, (deg-Math.PI)-Math.PI*(d/treeSize), treeSize*0.4, map, 0);
		}
	}
	
	private static void drawLeaf(double x, double y, double deg, double size, short[][][] map, double leafSize, boolean shrinking){
		if(!shrinking || size > 2)MapGenerator.DrawPixelCircle(x, y, 0.6, map[Map.LAYER_FRONT], (byte)4, true);
		MapGenerator.DrawPixelCircle(x, y, size, map[Map.LAYER_BACK], (byte)5, false);
		double diff = (deg - Math.PI/2*3) * 0.05;
		deg += Math.abs(diff) <= 0.05 ? diff : (diff > 0 ? 0.05 : -0.05);
		if(size>=1) {
			if(size >= leafSize)shrinking = true;
			drawLeaf(x+Math.cos(deg), y+Math.sin(deg), deg, size - (shrinking ? 0.15 : -0.15), map, leafSize, shrinking);
		}
	}
	
	private static void drawRoot(double x, double y, double deg, double size, short[][][] map, int lastSplit){
		MapGenerator.DrawPixelCircle(x, y, size/2, map[Map.LAYER_FRONT], (byte)4, true);
		double diff = Math.random()*Math.PI/8-Math.PI/16-(deg-Math.PI*0.5)/8;
		deg += diff;
		if(size>1) {
			if(Math.random()*(15-lastSplit) < 1) {
				drawRoot(x+Math.cos(deg), y+Math.sin(deg), deg+Math.PI/4, (size)*2/3, map, 0);
				drawRoot(x+Math.cos(deg), y+Math.sin(deg), deg-Math.PI/4, (size)*2/3, map, 0);
			}else {
				drawRoot(x+Math.cos(deg), y+Math.sin(deg), deg, size-0.01, map, lastSplit + 1);
			}
		}
	}
}
