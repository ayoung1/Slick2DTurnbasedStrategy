package screen;

import java.util.ArrayList;
import java.util.List;

import main.Main;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

import screen.PlaceFigureScreen.FigurePlacementListener;
import state.Board;
import characters.Figure;
import entities.Combatant;

public class PlacePartyScreen implements Screen, FigurePlacementListener {

	public interface PlacementFinishListener{
		public void onPlacementFinish(List<Combatant> placed);
	}
	
	private static final int MAX_UNITS = 3;
	
	private Board board;
	private List<Figure> party;
	private List<Combatant> placed;
	private PlacementFinishListener listener;
	private boolean display = true;
	private int timer = 0;
	
	private boolean[][] placeable;
	
	public PlacePartyScreen(List<Figure> party, Board board, PlacementFinishListener listener){
		Main.stackScreen(this);
		this.board = board;
		this.placeable = new boolean[board.getWidth()][board.getHeight()];
		this.party = party;
		this.listener = listener;
		this.placed = new ArrayList<>();
		this.pullData();
	}
	
	public Combatant[] getPlacedUnits(){
		Combatant[] temp = new Combatant[0];
		return this.placed.toArray(temp);
	}
	
	private void pullData(){
		int layer = this.board.getLayerIndex("PlacementLayer");
		boolean placement;
		for(int i = 0; i < this.board.getHeight(); ++i){
			for(int j = 0; j < this.board.getWidth(); ++j){
				placement = "true".equals(this.board.getTileProperty(this.board.getTileId(i, j, layer), "PlacementTile", "false"));
				this.placeable[i][j] = placement;
			}
		}
	}
	
	private void drawText(Graphics g){
		g.resetTransform();
		g.setColor(Color.white);
		
		g.drawString("Select a tile to place a unit: " + (MAX_UNITS - this.placed.size()) + " units left", 10, Main.TEXT_HEIGHT);
		
		g.scale(Main.SCALE, Main.SCALE);
	}
	
	private void highlightPlaceableTiles(Graphics g){
		float alpha = 0.5f;
		
		if(!this.display)
			alpha = alpha * alpha;		
		
		g.setColor(new Color(192, 192, 192, alpha));
		
		for(int i = 0; i < this.placeable.length; ++i){
			for(int j = 0; j < this.placeable.length; ++j){
				if(this.placeable[i][j])
					g.fill(new Rectangle(Main.TILE_HEIGHT * i, Main.TILE_HEIGHT * j, Main.TILE_HEIGHT, Main.TILE_HEIGHT));
			}
		}
	}
	
	@Override
	public void render(GameContainer arg0, Graphics g) throws SlickException {
		this.highlightPlaceableTiles(g);
		this.drawText(g);
	}

	private void calculateToggle(int delta){
		this.timer += delta;
		if(this.timer >= Main.UPDATE_SECOND){
			this.timer -= Main.UPDATE_SECOND;
			this.display = !this.display;
		}
	}
	
	@Override
	public void updateBackground(GameContainer gc, int delta) throws SlickException{
		this.calculateToggle(delta);
	}
	
	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		int mx = Main.getSelection().getAdjustedX(Main.TILE_HEIGHT), my = Main.getSelection().getAdjustedY(Main.TILE_HEIGHT);
		
		for(Combatant combatant : this.placed)
			combatant.update(gc, delta);
		
		if(gc.getInput().isMousePressed(0)){
			if(this.placeable[mx][my] && (MAX_UNITS - this.placed.size()) > 0){
				placed.remove(board.figureAt(mx, my));
				new PlaceFigureScreen(this, this.party, board, mx, my, this);
			}
		}
	}

	@Override
	public void hoverEvents(GameContainer arg0, Graphics g) throws SlickException{
		this.board.hoverEvents(Main.getSelection(), arg0, g);
	}
	
	@Override
	public void keyPressed(int key, char c) {
		if(key == Input.KEY_ENTER){
			if(!this.placed.isEmpty()){
				this.listener.onPlacementFinish(this.placed);
				Main.removeScreen(this);
			}
		}
	}
	
	@Override
	public void onRightClick(){

	}

	@Override
	public void onFigurePlacement(Combatant c) {
		this.placed.add(c);
	}

}
