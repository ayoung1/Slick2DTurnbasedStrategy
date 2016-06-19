package state;

import main.Main;

import org.newdawn.slick.SlickException;

import screen.MapScreen;
import tools.Selection;

public class PlayState extends State {

	private Selection selection;
	
	public PlayState() throws SlickException {
		super(new MapScreen());
		selection = new Selection(0,0, Main.TILE_HEIGHT, Main.TILE_HEIGHT, 15, 15);
		Main.stackScreen(getScreen());
	}

	@Override
	public Selection getSelection() {
		return this.selection;
	}
}
