package entities;

import main.Main;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

public class Tile implements Entity{

	private String type;
	private boolean blocked;
	
	public Tile(TiledMap map, int tile){
		this.type = map.getTileProperty(tile, "Type", "false");
		this.blocked = "true".equals(map.getTileProperty(tile, "Blocked", "false"));
	}
	
	@Override
	public boolean isPathable(){return !this.blocked;}
	public String getType(){return this.type;}

	@Override
	public void hoverEvent(GameContainer arg0, Graphics g) throws SlickException {
		int x = arg0.getWidth(), y = arg0.getHeight() - Main.TEXT_HEIGHT;
		x -= this.type.length() * 10;
		g.drawString(getType(), x, y);
	}	
}
