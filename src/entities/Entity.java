package entities;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import state.Board;

public interface Entity {
	public int[] getCoords();
	public default void update(GameContainer arg0, int delta){}
	public default void render(GameContainer arg0, Graphics g) throws SlickException{}
	public default void hoverEvent(GameContainer arg0, Graphics g) throws SlickException{}
	public default void clickEvent(Board board) throws SlickException{}
	public default boolean isPathable(){return false;}
}
