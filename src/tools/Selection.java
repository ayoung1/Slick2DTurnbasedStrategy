package tools;

import main.Main;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Rectangle;

public class Selection extends Rectangle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int maxX;
	private int maxY;
	
	public Selection(float x, float y, float width, float height, int maxX, int maxY) {
		super(x, y, width, height);
		this.maxX = maxX;
		this.maxY = maxY;
	}
	
	public int getAdjustedX(int tileSize){
		int ax = (int)(super.getX() / tileSize);
		
		ax = ax < 0 ? 0 : ax;
		ax = ax < maxX ? ax : maxX-1;
		
		return ax;
	}
	
	public int getAdjustedY(int tileSize){
		int ay = (int)(super.getY() / tileSize);
		
		ay = ay < 0 ? 0 : ay;
		ay = ay < maxY ? ay : maxY-1;
		
		return ay;
	}
	
	public void update(GameContainer gc){
		int mx, my, fx, fy;
		int height = (int) (Main.TILE_HEIGHT * Main.SCALE);
		
		mx = gc.getInput().getMouseX();
		my = gc.getInput().getMouseY();
		
		fx = ((int) mx / height) * Main.TILE_HEIGHT;
		fy = ((int) my / height) * Main.TILE_HEIGHT;
		
		
		super.setX(fx);
		super.setY(fy);
	}

}
