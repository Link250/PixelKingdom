package map.biomesList;

import java.util.Random;

import map.Biome;
import map.MapGenerator;

public class Plains extends Biome {

	public Plains() {
		name = "Plains";
		ID = 1;
	}

	public short[][][] generate() {
		double width = 1024, height = 1024;
		short[][][] newMap = new short[3][(int)width][(int)height];
		Random r = new Random(System.nanoTime());
		
		for(int n = 0; n < 2; n++){
			double yg = height/2+height/64;
			double change = (int)(r.nextDouble()*2-1);
			for(int x = 0; x < width; x++){
				if(change < 0){
					yg += change;
					if((int)(r.nextDouble()*3)==1 && change > -1) change-=0.1;
					else if((int)(r.nextDouble()*3)==1);
					else change +=0.1;
				}
				if(change == 0){
					if((int)(r.nextDouble()*20) == 1){
						if(yg <= height/16*7){
							change +=0.2;
						}
						if(yg > height/16*7 && yg < height/16*9){
							if((int)(r.nextDouble()*2) == 1){ change +=0.1;}
							else{ change -=0.2;}
						}
						if(yg >= height/16*9){
							change -=0.2;
						}
					}
				}
				if(change > 0){
					yg += change;
					if((int)(r.nextDouble()*3)==1 && change < 1) change+=0.1;
					else if((int)(r.nextDouble()*3)==1);
					else change -=0.2;
				}
				if(yg<height/4){yg=height/4;change = 0;}
				if(yg>height/4*3){yg=height/4*3;change = 0;}
				if(n==0)newMap[0][x][(int)yg] = 2;
				else newMap[0][((int)width-1)-x][(int)yg] = 2;
			}
		}
		
		for(int x = 0; x < width; x ++){
			boolean fd = false;
			for(int y = (int)height-1; y > 32; y --){
				if(newMap[0][x][y]==2)												fd = true;
				if(!fd)			 												{newMap[0][x][y] = 1;newMap[2][x][y] = 1;}
				for(int i = 1; i < 30; i++){
					if(newMap[0][x][y-i]==2 && !fd && (int)(r.nextDouble()*(i-5))<3)	{newMap[0][x][y] = 2;newMap[2][x][y] = 2;}
				}
				if(fd)															newMap[0][x][y] = 0;
				if(y>height/2 && newMap[0][x][y]==0)								newMap[1][x][y] = 1;
			}
		}
		
		for(int y = 8; y < height-8; y ++){
			for(int x = 8; x < width-8; x ++){
				if(newMap[0][x][y]==2){
					for(int Y = -1; Y < 2 & newMap[0][x][y]==2; Y ++){
						for(int X = -1; X < 2 & newMap[0][x][y]==2; X ++){
							if(newMap[0][x+X][y+Y]==0 & newMap[1][x+X][y+Y]==0){newMap[0][x][y]=3;}
						}
					}
				}
			}
		}

		for(int i = 0; i < 100; i++){
			MapGenerator.genOreCluster((int)(Math.random()*1024), (int)(Math.random()*1024), 2, 1, 50, newMap[0]);
		}
		
		for(int i = 0; i < 2; i++){
			for(int x = 0; x < 1024; x++){
				int y;
				for(y = 512; y < 600 & newMap[0][x][y]!=2; y++){}
				if(newMap[1][x][y-1]==1 & Math.random()*(50-i*40)<1)MapGenerator.genOreCluster(x, y, 7+i, 2, 10, newMap[0]);
			}
		}
		
		MapGenerator.genCavesArea(0,600,1024,400,newMap);

		for(int y = 0; y < height/2; y ++){
			for(int x = 32; x < width-32; x ++){
				if(newMap[0][x][y+1]==3 && r.nextDouble()*333 <= 1){MapGenerator.newTree(x,y,newMap);}
			}
		}
		
		return newMap;
	}
}
