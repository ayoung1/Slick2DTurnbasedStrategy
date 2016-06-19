package screen;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public interface Screen {
	public abstract void render(GameContainer arg0, Graphics g) throws SlickException;
	public abstract void update(GameContainer gc, int delta) throws SlickException;
	public abstract void keyPressed(int key, char c);
	public default void hoverEvents(GameContainer arg0, Graphics g) throws SlickException{}
}
