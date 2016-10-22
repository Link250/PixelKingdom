package dataUtils;

import java.awt.Point;
import java.lang.reflect.Array;
import java.util.function.Function;

public class ChunkContainer <T>{
	
	private T[][] data;
	private int width, height;
	private int modXOff, modYOff;
	private int absXOff, absYOff;
	@SuppressWarnings("unused")
	private Function<T,Boolean> unload;
	@SuppressWarnings("unused")
	private Function<Point,T> load;
	
	@SuppressWarnings("unchecked")
	public ChunkContainer(Class<T> clazz, int width, int height) {
		this.width = width;
		this.height = height;
		this.data = (T[][]) Array.newInstance(clazz, width, height);
		this.modXOff = 0;
		this.modYOff = 0;
	}
	
	public void setUnloadOperation(Function<T,Boolean> function) {
		this.unload = function;
	}
	
	public void setLoadOperation(Function<Point,T> function) {
		this.load = function;
	}
	
	public void changeOffset(int changeX, int changeY) {
		this.absXOff += changeX;
		this.absYOff += changeY;
		for (int i = changeX; i > 0; i--) {
//			data[1][2] = load.apply(new Point(1,2));
		}
	}
	
	public T get(int x, int y) {
		return data[(x+modXOff)%width][(y+modYOff)%height];
	}
}
