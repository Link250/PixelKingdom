package gui;

import java.util.function.Predicate;

import dataUtils.PArea;
import gfx.PxlFont;
import main.KeyInput;
import main.MouseInput;
import main.KeyInput.Focusable;

public class TextField implements Focusable{
	private boolean hasFocus = false;
	private boolean finished = false;
	private String text = "";
	private boolean textLimit;
	private boolean background = false;
	private PArea field;
	private PxlFont font;
	private Predicate<Character> charFilter;
	
	public TextField(int x, int y, int width, int height, boolean textLimit, boolean background, PxlFont font) {
		this.field = new PArea(x, y, width, height);
		this.textLimit = textLimit;
		this.background = background;
		this.font = font;
		this.charFilter = (c)->{return true;};
	}
		
	public void setPos(int x, int y){
		setPos(x, y, true, true);
	}
	
	public void setPos(int x, int y, boolean centeredX, boolean centeredY){
		field.x = x - (centeredX ? field.width/2 : 0);
		field.y = y - (centeredY ? field.height/2 : 0);
	}
	
	public void tick(){
		if(field.containsMouse(MouseInput.mouse) && MouseInput.mousel.click()) setFocus(true);
	}

	public void render(){
		if(background){
			field.showArea();
		}
		font.render(field.x+field.width/2, field.y+field.height/2, text+(hasFocus && System.currentTimeMillis()%1000<500 ? '|' : ""), textLimit ? field.width-10 : 0, 0xff000000);
	}
	
	public void useInput(char c) {
		switch (c) {
		case '\n':
			finished = true;
			hasFocus = false;
			break;
		case (char) -1:
			if(text.length()>0)text = text.subSequence(0, text.length()-1).toString();
			finished = false;
			break;
		default:
			if(charFilter.test(c))text+=c;
			finished = false;
		}
	}

	public boolean hasFocus() {
		return hasFocus;
	}
	
	public void setFocus(boolean hasFocus) {
		this.hasFocus = hasFocus;
		if(hasFocus)KeyInput.setFocus(this);
	}
	
	public boolean hasFinshed() {
		return finished;
	}
	
	public void setUnfinished() {
		this.finished = false;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public void setCharFilter(Predicate<Character> charFilter) {
		this.charFilter = charFilter;
	}
}
