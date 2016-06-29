package state;

import java.util.List;

import characters.Figure;
import main.Main;
import screen.MapScreen;
import tools.Selection;

public class PlayState extends State {

	private Selection selection;
	
	public PlayState(List<Figure> party) throws Exception {
		super(new MapScreen(party));
		selection = new Selection(0,0, Main.TILE_HEIGHT, Main.TILE_HEIGHT, 15, 15);
		Main.stackScreen(getScreen());
	}

	@Override
	public Selection getSelection() {
		return this.selection;
	}
}
