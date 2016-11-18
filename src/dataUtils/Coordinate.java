package dataUtils;

public class Coordinate {
	public int x, y, l;
	
	public Coordinate() {
		setAll(0, 0, 0);
	}
	
	public Coordinate(int x, int y, int l) {
		setAll(x, y, l);
	}
	
	public Coordinate(Coordinate copy) {
		setAll(copy.x, copy.y, copy.l);
	}
	
	public void setAll(int x, int y, int l) {
		this.x = x;
		this.y = y;
		this.l = l;
	}
	
	public void setXY(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void moveAll(int x, int y, int l) {
		this.x += x;
		this.y += y;
		this.l += l;
	}
	
	public void moveXY(int x, int y) {
		this.x += x;
		this.y += y;
	}
	
	public double distanceXY(Coordinate other) {
		return Math.sqrt((this.x-other.x)*(this.x-other.x)+(this.y-other.y)*(this.y-other.y));
	}
	
	public double distanceXYsquared(Coordinate other) {
		return (this.x-other.x)*(this.x-other.x)+(this.y-other.y)*(this.y-other.y);
	}
}
