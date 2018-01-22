package pixel.pixelList;

import pixel.UDS;
import pixel.interfaces.Burnable;
import java.awt.Point;

import pixel.Material;

public class Twig extends Material<Twig.DataStorage> implements Burnable{

	public Twig(){
		super(new DataStorage());
		ID = 4;
		name = "Twig";
		displayName = "Twig";
		requiredType = MINING_TYPE_PICKAXE;
		requiredTier = 1;
		miningResistance = 1;
		loadTexture();
	}
	
	protected void loadTexture() {
		Point size = new Point();
		texture = loadTexture("/MapTextures/"+"Wood"+".png", size);
		this.textureWidth = size.x;
		this.textureHeight = size.y;
	}

	public byte getBurnStrength() {return 20;}
	
	public static class DataStorage extends UDS{
		public byte growth;
	}
}
