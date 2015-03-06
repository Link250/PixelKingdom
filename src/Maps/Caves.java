package Maps;

public class Caves extends Biome {

	public Caves() {
		name = "Caves";
		ID = 52;
	}

	public short[][][] generate() {
		int width = 1024, height = 1024;
		short[][][] newMap = new short[3][width][height];
		for(int x = 0; x < width; x++){
			for(int y = 0; y < width; y++){
				newMap[0][x][y]=1;
				newMap[2][x][y]=1;
			}
		}
		for(int i = 0; i < 100; i++){
			int x = (int) (Math.random()*990+30),
				y = (int) (Math.random()*990+30);
			double r = Math.random()*2*Math.PI;
			for(int n = 0; n < 20 & x<990 & x>30 & y<990 & y>30; n++){
				MapGenerator.DrawPixelLine(x, y, (int)(x+Math.sin(r)*Math.random()*10), y+(int)(Math.cos(r)*Math.random()*10), newMap[0], (byte) 0, (int)(Math.random()*10+50), true);
				r+=Math.random()*Math.PI-Math.PI/2;
				x+=(int)(Math.sin(r)*10);
				y+=(int)(Math.cos(r)*10);
			}
		}
		return newMap;
	}
}
