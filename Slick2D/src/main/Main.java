package main;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import screen.Screen;
import state.PlayState;
import state.State;
import tools.Selection;

public class Main extends BasicGame {
	private static final String  TITLE = "Title of Game";
		
	public static final int TEXT_HEIGHT = 20;
	public static final int UPDATE_SECOND = 1000;
	public static final int TILE_HEIGHT = 16;
	public static final float SCALE = 2.0f;
	public static SpriteSheet BASIC_TILES;
	
	private static State state;
	private static List<Screen> screenStack;
	private static Selection selection;
	private static boolean drawSelector = true;
	
	private static SpriteSheet loadSprites(String URL){
		try {
			return new SpriteSheet(URL, 16, 16);
		} catch (SlickException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		return null;
	}
	
	public static void disableSelectorGraphic(){drawSelector = false;}
	public static void enableSelectorGraphic(){drawSelector = true;}
	
	public static void changeState(State newState){
		state = newState;
		selection = state.getSelection();
		screenStack.clear();
		screenStack.add(0, state.getScreen());
	}
	
	public static void stackScreen(Screen newScreen){
		screenStack.add(0, newScreen);
	}
	
	public static void removeScreen(Screen screen){
		screenStack.remove(screen);
	}
	
	public static Selection getSelection(){
		return selection;
	}
	
	public static void main(String[] args) {
		screenStack = new ArrayList<>();
		try{
			AppGameContainer container;
			container = new AppGameContainer(new Main(TITLE));
			container.setDisplayMode(480, 480, false);
			container.start();
		}catch (SlickException ex){
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	public Main(String title) throws SlickException {
		super(title);
	}

	@Override
	public void init(GameContainer arg0) throws SlickException {
		arg0.setShowFPS(false);
		BASIC_TILES = loadSprites("data/basictiles.png");
		changeState(new PlayState());
	}
	
	@Override
	public void keyPressed(int key, char c){
		if(key == Input.KEY_ESCAPE){
			//Replace with opening menu
			screenStack.remove(0);
			if(screenStack.size() == 0)
				System.exit(0);
		}
		
		screenStack.get(0).keyPressed(key, c);
	}
	
	@Override
	public void render(GameContainer arg0, Graphics g) throws SlickException {
		g.scale(SCALE, SCALE);
		state.getScreen().render(arg0, g);
		if(screenStack.size() > 1)
			screenStack.get(0).render(arg0, g);
		screenStack.get(0).hoverEvents(arg0, g);
		g.setColor(Color.white);
		if(drawSelector)
			g.draw(selection);
		g.resetTransform();
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		enableSelectorGraphic();
		selection.update(gc);
		if(gc.getInput().isMousePressed(1)){
			if(screenStack.size() > 1)
				screenStack.remove(0);
		}else
			screenStack.get(0).update(gc, delta);
	}

}
