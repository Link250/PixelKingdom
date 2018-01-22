package item;

import java.util.ArrayList;

import dataUtils.conversion.ConvertData;
import entities.Player;
import gfx.Mouse;
import gfx.Mouse.MouseType;
import gfx.SpriteSheet;
import main.MouseInput;
import map.Map;
import pixel.Material;
import pixel.PixelList;

public abstract class Pickaxe extends Tool implements MiningTool{
	
	protected double miningTier = 0;
	protected double strength = 0;
	private double powerLeft = 0;
	
	public Pickaxe(){
		type = 1;
		name = "Pickaxe";
		displayName = "Pickaxe";
		stack  = 1;
		stackMax  = 1;
		gfxs = new SpriteSheet("/Items/Pickaxeh.png");
	}
	
	public void holdItem(Player plr, Map map) {
	anim = 0;
	
	if((MouseInput.mousel.isPressed() | MouseInput.mouser.isPressed())/* && !plr.iscrouching && !plr.isinair*/){
		powerLeft += strength/60;
		double r = 0;
		boolean couldMine = false;
		while(powerLeft > 0){
			r = size*size;
			int pX = 0, pY = 0;
			Material<?> m = null;
			int l = MouseInput.mousel.isPressed() ? Map.LAYER_FRONT : Map.LAYER_BACK;
			for(int x = -size; x <= size; x++){
				for(int y = -size; y <= size; y++){
					int X = MouseInput.mouse.getMapX()+x, Y = MouseInput.mouse.getMapY()+y;
					if(x*x+y*y < r && ((plr.x-X)*(plr.x-X)+(plr.y-Y)*(plr.y-Y) <= range*range)){
						m = PixelList.GetMat(map.getID(X,Y,l));
						if(m.canBeMinedBy(plr, this)) {
							couldMine = true;
							if(m.getMiningResistance()<=powerLeft){
								r = x*x+y*y;
								pX = X;pY = Y;
							}
						}
					}
				}
			}
			m = PixelList.GetMat(map.getID(pX,pY,l));
			if(m.ID!=0){
				map.breakPixel(pX, pY, l, this);
				powerLeft -= PixelList.GetMat((byte)map.getID(pX, pY, l)).getMiningResistance();
			}else{
				if(!couldMine)powerLeft = 0;
				break;
			}
		}
		
		if((System.nanoTime()/100000000)%10 < 5)anim = 11;
			else anim = 10;
		
		}else{powerLeft = 0;}
	}
	
	public void setMouse(){
		Mouse.mouseType=MouseType.MINING;
		Mouse.mousesize=(byte)size;
	}

	public void save(ArrayList<Byte> file) {
		ConvertData.I2B(file, ID);
		ConvertData.I2B(file, size);
	}

	public void load(ArrayList<Byte> file) {
		size = ConvertData.B2I(file);
	}
	
	public void upgrade(int up){
		size += up;
	}
	
	public boolean canBreak(Material<?> mat) {
		return true;
	}
	
	public int getMiningType() {
		return Material.MINING_TYPE_PICKAXE;
	}
	
	public double getMiningTier() {
		return miningTier;
	}
}
