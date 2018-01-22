package map.trees;

import map.Map;
import map.MapGenerator;

public class TreePine {
	public static void generate(int x, int y, short[][][] map){
		double treeSize = Math.random()*2+8;
		int heightL = y-10, heightR = y-10;
		for (int h = y-10; h < y+10; h++) {
			if(map[0][x-5][heightL] == 0 && map[0][x-5][h] != 0)heightL = h;
			if(map[0][x+5][heightR] == 0 && map[0][x+5][h] != 0)heightR = h;
		}
		double deg = Math.atan2(10, heightL-heightR)+Math.PI;
		drawTwig(x, y, deg, treeSize, map, 0, treeSize);
		for (double d = -treeSize/2; d < treeSize/2; d+=2) {
			drawRoot(x+d, y, (deg-Math.PI)-Math.PI*(d/treeSize), treeSize*0.4, map, 0);
		}
	}
	
	private static void drawTwig(double x, double y, double deg, double size, short[][][] map, int lastSplit, double treeHeight){
		MapGenerator.DrawPixelCircle(x, y, size/2, map[Map.LAYER_FRONT], (byte)4, true);
		if(size<2)MapGenerator.DrawPixelCircle(x, y, 3+Math.random()*(2), map[Map.LAYER_BACK], (byte)5, false);
		double diff = Math.random()*Math.PI/8-Math.PI/16-(deg-Math.PI*1.5)/16;
		deg += diff;
		if(size>1) {
			if(size < treeHeight*0.97 && Math.random()*(20-lastSplit*1.5) < 1) {
				drawTwig(x+Math.cos(deg), y+Math.sin(deg), deg+Math.PI/4, (size)*2/3, map, 0, treeHeight);
				drawTwig(x+Math.cos(deg), y+Math.sin(deg), deg-Math.PI/4, (size)*2/3, map, 0, treeHeight);
			}else {
				drawTwig(x+Math.cos(deg), y+Math.sin(deg), deg, size-0.01, map, lastSplit + 1, treeHeight);
			}
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
