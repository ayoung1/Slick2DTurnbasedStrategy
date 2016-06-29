package screen;

import java.util.List;

import main.Main;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

import state.Board;
import characters.Figure;
import entities.Combatant;

public class PlaceFigureScreen implements Screen{

	public interface FigurePlacementListener{
		public void onFigurePlacement(Combatant c);
	}
	
	private int selection = 0, x, y;
	private List<Figure> party;
	private FigurePlacementListener listener;
	private Board board;
	private PlacePartyScreen below;
	
	public PlaceFigureScreen(PlacePartyScreen below, List<Figure> party, Board board, int x, int y, FigurePlacementListener listener){
		this.party = party;
		this.board = board;
		this.below = below;
		this.x = x;
		this.y = y;
		this.listener = listener;
		board.removeEntity(board.figureAt(x, y));
		Main.stackScreen(this);
	}
	
	@Override
	public void render(GameContainer arg0, Graphics g) throws SlickException {
		this.below.render(arg0, g);
		Animation idle = this.party.get(this.selection).getAnimations().get("idle");
		idle.draw(this.x * Main.TILE_HEIGHT, this.y * Main.TILE_HEIGHT);
		
		if(this.isSelectionPlaced()){
			g.setColor(new Color(192, 192, 192, 0.45f));
			g.fill(new Rectangle(this.x * Main.TILE_HEIGHT, this.y * Main.TILE_HEIGHT, Main.TILE_HEIGHT, Main.TILE_HEIGHT));
		}
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		
	}

	@Override
	public void keyPressed(int key, char c) {
		if(key == (Input.KEY_LEFT))
			this.decrimentSelection();
		else if(key == (Input.KEY_RIGHT))
			this.incrementSelection();
		else if(key == Input.KEY_ENTER){
			
			
			if(!this.isSelectionPlaced()){
				try {
					this.listener.onFigurePlacement(Combatant.constructCombatant(this.party.get(this.selection), this.board, this.x, this.y));
				} catch (Exception e) {
					e.printStackTrace();
					System.exit(-1);
				}
				Main.removeScreen(this);
			}
		}
	}
	
	private boolean isSelectionPlaced(){
		for(Combatant combatant : this.below.getPlacedUnits()){
			if(combatant.getFigure() == this.party.get(this.selection))
				return true;
		}
		return false;
	}
	
	private void decrimentSelection(){
		--this.selection;
		this.selection = this.selection < 0 ? this.party.size() - 1 : this.selection;
	}
	
	private void incrementSelection(){
		++this.selection;
		this.selection = this.selection >= this.party.size() ? 0 : this.selection;
	}

}
