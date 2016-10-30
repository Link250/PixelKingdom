package gui;

import dataUtils.PArea;
import main.MouseInput;

public class ScrollBar {
	private PArea main;
	private PArea slider;
	private int value = 0, maxValues = 1, sliderSize = 1;
	private boolean render = false;
	
	public ScrollBar(int x, int y, int width, int height){
		main = new PArea(x-width/2, y-height/2, width, height);
		slider = new PArea(0, 0, width, height);
	}

	public ScrollBar(int x, int y, int width, int height, boolean centeredX, boolean centeredY){
		main = new PArea(x - (centeredX ? width/2 : 0), y - (centeredY ? height/2 : 0), width, height);
		slider = new PArea(0, 0, width, height);
	}

	public void setPos(int x, int y){
		setPos(x, y, true, true);
	}
	
	public void setPos(int x, int y, boolean centeredX, boolean centeredY){
		main.setPosition(x - (centeredX ? main.width/2 : 0), y - (centeredY ? main.height/2 : 0));
		slider.setPosition(0, 0);
	}
	
	public void setValue(int value) {
		this.value = value;
		checkValueBounds();
	}
	
	public void checkValueBounds() {
		if(value>=(maxValues-sliderSize))value = maxValues-sliderSize;
		if(value<0)value = 0;
		repositionSlider();
	}
	
	public void setValues(int maxValues, int sliderSize) {
		this.sliderSize = sliderSize;
		if(maxValues>0) {
			this.maxValues = maxValues;
		}else {
			this.maxValues = 1;
		}
		resizeSlider();
		checkValueBounds();
	}
	
	private void repositionSlider() {
		slider.y = slider.height*value/sliderSize;
		if(slider.y+slider.height > main.height || value==maxValues-sliderSize) slider.y = main.height-slider.height;
	}
	
	private void resizeSlider() {
		slider.height = main.height/maxValues * sliderSize;
		if(slider.height > main.height) slider.height = main.height;
	}
	
	public void tick(){
		if(main.contains(MouseInput.mouse.x, MouseInput.mouse.y) && MouseInput.mousel.isPressed() && main.contains(MouseInput.mousel.x, MouseInput.mousel.y)){
			setValue((MouseInput.mouse.y-main.y)/(this.main.height/(this.maxValues-this.sliderSize+1)));
		}
	}

	public void render(){
		if(render){
			
		}
	}
	
	public int getWidth() {
		return main.width;
	}

	public int getHeight() {
		return main.height;
	}
	
	public PArea getSlider() {
		return slider;
	}
	
	public int getValue() {
		return value;
	}
}
