package entities;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2d;

import gfx.Screen;

public class Hitbox {
	public double xmin;
	public double xmax;
	public double ymin;
	public double ymax;
	
	public Hitbox(double xmin, double xmax, double ymin, double ymax){
		this.xmin = xmin;
		this.xmax = xmax;
		this.ymin = ymin;
		this.ymax = ymax;
	}
	
	public void render(double x, double y) {
		List<Vector2d> points = new ArrayList<>();
		for(double i = xmin; i <= xmax; i+=0.5){
			points.add(new Vector2d(x+i,y+ymin));
		}
		Screen.drawLines(points, 0xff00ff00, true);
		points.clear();
		for(double i = xmin; i <= xmax; i+=0.5){
			points.add(new Vector2d(x+i,y+ymax));
		}
		Screen.drawLines(points, 0xff00ff00, true);
		points.clear();
		for(double i = ymin; i <= ymax; i+=0.5){
			points.add(new Vector2d(x+xmin,y+i));
		}
		Screen.drawLines(points, 0xff00ff00, true);
		points.clear();
		for(double i = ymin; i <= ymax; i+=0.5){
			points.add(new Vector2d(x+xmax,y+i));
		}
		Screen.drawLines(points, 0xff00ff00, true);
	}
}
