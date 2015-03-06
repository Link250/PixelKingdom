package Maps;

import java.awt.Point;

public class MapGenerator {
	
	public static short[][][] Generate(byte biome){
		return BiomeList.GetBiome(biome).generate();
	}
	
	public static void newTree(int x, int y, short[][][] Map){
		int height = (int)(Math.random()*30+20);
		y -= height;
		for(int i = 0; i < 60; i++){
			if(Map[2][x+0][y+i]==0)Map[2][x+0][y+i]=4;
			if(Map[2][x+1][y+i]==0)Map[2][x+1][y+i]=4;
			if(Map[2][x+2][y+i]==0)Map[2][x+2][y+i]=4;
			if(Map[2][x+3][y+i]==0)Map[2][x+3][y+i]=4;
		}
		for(double r = 0.9*Math.PI; r <= 1.1*Math.PI*2; r+= Math.PI/20){
			for(int l = 0; l < 3; l+=2){
				int X=x+2,Y=y+1;
				for(int i = 0; i < height/5; i++){
					double d = (Math.random()*0.5-0.25)*Math.PI;
					DrawPixelLine((int)(5*Math.cos(r+d)+X), (int)(4*Math.sin(r+d)+Y), X, Y, Map[l], (byte)5, 2, false);
					DrawPixelLine((int)(5*Math.cos(r+d)+X), (int)(4*Math.sin(r+d)+Y), X, Y, Map[l], (byte)4, 0, true);
					X+=(int)(4*Math.cos(r+d));
					Y+=(int)(4*Math.sin(r+d));
				}
			}
		}
	}
	
	public static Point getNearestPixel(int sx, int sy, int r, short[][] Map, byte ID){
		int fx=0,fy=0;
		double d=r;
		for(int Y = sy-r; Y <= sy+r; Y++){
			for(int X = sx-r; X <= sx+r; X++){
				if(Map[X][Y]==ID && Math.sqrt((X-sx)*(X-sx)+(Y-sy)*(Y-sy))<d){
					fx=X;
					fy=Y;
					d = Math.sqrt((X-sx)*(X-sx)+(Y-sy)*(Y-sy));
				}
			}
		}
		Point coords = new Point(fx,fy);
		return coords;
	}

	public static void DrawPixelLine(int sx, int sy, int fx, int fy, short[][] Map, byte ID, int size, boolean replace){
		double x=sx,y=sy;
		double d = Math.sqrt((fx-sx)*(fx-sx)+(fy-sy)*(fy-sy));
		double dx = (fx-sx)/d,dy= (fy-sy)/d;
		while((int)x != fx | (int)y != fy){
			try{
				if(Map[(int)x][(int)y]==0 | replace)Map[(int)x][(int)y]=ID;
				for(int by = (int)y-size; by <= (int)y+size; by++){
					for(int bx = (int)x-size; bx <= (int)x+size; bx++){
						if((Map[bx][by]==0 | replace) & Math.sqrt((bx-x)*(bx-x)+(by-y)*(by-y))<=size)Map[bx][by]=ID;
					}
				}
			}catch(ArrayIndexOutOfBoundsException e){break;}
			x += dx;
			y += dy;
			double dist = Math.sqrt((fx-x)*(fx-x)+(fy-y)*(fy-y));
			if(dist < d)d = dist;
			else{break;}
		}
	}
	
	public static void genOreCluster(int x, int y, int ID, int replace, int size, short[][] newMap){
		for(int n = size; n > 0; n--){
			for(int X = -10; X < 10; X++){
				for(int Y = -10; Y < 10; Y++){
					try{
						if(Math.sqrt((X)*(X)+(Y)*(Y)) < 10 & Math.random()*Math.sqrt((X)*(X)+(Y)*(Y)) < 2 & newMap[x+X][y+Y]==replace)newMap[x+X][y+Y]=(short) ID;
					}catch(ArrayIndexOutOfBoundsException e){}
				}
			}
			double r = Math.random()*2*Math.PI;
			x += (int) 5*Math.cos(r);
			y += (int) 5*Math.sin(r);
			if(x-10 < 0)x+=10;
			if(x+10 > 1024)x-=10;
			if(y-10 < 0)y+=10;
			if(y+10 > 1024)y-=10;
		}
	}
	
	public static void genCavesArea(int offX, int offY, int width ,int height, short[][][] map){
		for(int i = 0; i < width*height/10000; i++){
			int x = (int) (Math.random()*(width-40)+offX+20),
				y = (int) (Math.random()*(height-40)+offY+20);
			double r = Math.random()*2*Math.PI;
			for(int n = 0; n < 20 & x<offX+width-20 & x>offX+20 & y<offY+height-20 & y>offY+20; n++){
				r+=Math.random()*Math.PI-Math.PI/2;
				MapGenerator.DrawPixelLine(x, y, x+=(int)(Math.sin(r)*Math.random()*10), y+=(int)(Math.cos(r)*Math.random()*10), map[0], (byte) 0, (int)(Math.random()*15+5), true);
			}
		}
	}
}
