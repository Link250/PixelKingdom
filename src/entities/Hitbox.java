package entities;

public class Hitbox {
	public int xmin;
	public int xmax;
	public int ymin;
	public int ymax;
	public Hitbox(int xmin, int ymin, int xmax, int ymax){
		this.xmax = xmax;
		this.ymax = ymax;
		this.xmin = xmin;
		this.ymin = ymin;
	}
}
