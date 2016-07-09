package gameFields;

import entities.Player;
import gfx.Mouse;
import gfx.SpriteSheet;
import item.Bag;
import main.Game;
import main.PArea;

public class BagInv extends GameField {
	
	protected static SpriteSheet Background = new SpriteSheet("/Items/field.png");
	private int width;
	private String title;
	
	public Bag<?> bag;
	
	public BagInv(Bag<?> bag, Player.BAG bagEnum) {
		super(bagEnum.fieldEnum);
		this.width = bag.invWidth();
		this.setSize(width*12, bag.invSize()/width*12+11);
		this.bag = bag;
		this.title = bag.getDisplayName();
	}

	public void tick() {
		Drag();
		if(mouseover(Game.input.mouse.x/Game.SCALE, Game.input.mouse.y/Game.SCALE)){
			Mouse.mousetype=0;
			if(Game.input.mousel.click()){
				int mx = Game.input.mousel.x/Game.SCALE, my = Game.input.mousel.y/Game.SCALE;
				PArea r = new PArea(0,0,10,10);
				for(int x = 0; x < width; x++){
					for(int y = 0; y < bag.invSize()/width; y++){
						r.setBounds(x*12+1+field.x,y*12+12+field.y,10,10);
						if(r.contains(mx, my)){
							if(bag.getItem(x+y*width)!=null){
								if(Mouse.Item==null){
									Mouse.Item = bag.removeItem(x+y*width);
								}
							}else{
								if(bag.setItem(x+y*width, Mouse.Item)) {
									Mouse.Item = null;
								}
							}
						}
					}
				}
			}
		}
	}

	public void render() {
		renderfield();
		Game.sfont.render(Game.screen.xOffset+field.x+2, Game.screen.yOffset+field.y+1, this.title, 0, 0xff000000, Game.screen);
		for(int x = 0; x < width; x++){
			for(int y = 0; y < bag.invSize()/width; y++){
				Game.screen.drawGUITile(Game.screen.xOffset+field.x+x*12, Game.screen.yOffset+field.y+y*12+11, 0, 0, Background, 0xff000000);
				if(bag.getItem(x+y*width)!=null) bag.getItem(x+y*width).render(Game.screen, Game.screen.xOffset+field.x+x*12+1, Game.screen.yOffset+field.y+y*12+12,true);
			}
		}
	}

}
