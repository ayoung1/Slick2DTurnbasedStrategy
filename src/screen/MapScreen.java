package screen;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import main.Main;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

import characters.Figure;
import entities.Combatant;
import screen.PlacePartyScreen.PlacementFinishListener;
import state.Board;

public class MapScreen implements Screen, PlacementFinishListener {

	private Board board;
	private Queue<Combatant> turnOrder;
	private List<Figure> party;
	
	public MapScreen(List<Figure> party) throws Exception{
		this.board = new Board("data/MapCompressed64.tmx");
		this.turnOrder = new LinkedList<>();
		this.party = party;
	}
	
	@Override
	public void hoverEvents(GameContainer arg0, Graphics g) throws SlickException{
		board.hoverEvents(Main.getSelection(), arg0, g);
	}
	
	@Override
	public void render(GameContainer arg0, Graphics g) throws SlickException {
		this.board.render(0, 0, 0, 0, 15, 15, arg0, g);
		if(this.turnOrder.isEmpty())
			return;
		int force = this.turnOrder.peek().getForce();
		int x = this.turnOrder.peek().getWidthPos() * Main.TILE_HEIGHT, y = this.turnOrder.peek().getHeightPos() * Main.TILE_HEIGHT;
		
		if(force == 0)
			g.setColor(Color.cyan);
		else
			g.setColor(Color.green);

		g.draw(new Rectangle(x, y, Main.TILE_HEIGHT, Main.TILE_HEIGHT));
	}

	@Override
	public void updateBackground(GameContainer gc, int delta) throws SlickException{
		for(Combatant f : this.turnOrder){
			f.update(gc, delta);
		}
	}
	
	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		if(this.turnOrder.isEmpty()){
			new PlacePartyScreen(party, board, this);
			return;
		}
		Combatant figure;
		if(gc.getInput().isMousePressed(0) && this.board.figureAt(Main.getSelection()) instanceof Combatant){
			figure = (Combatant) this.board.figureAt(Main.getSelection());
			if(figure == this.turnOrder.peek())
				figure.clickEvent(this.board);
		}
		if(this.turnOrder.peek().isTurnOver()){
			figure = this.turnOrder.poll();
			this.turnOrder.remove(0);
			this.turnOrder.add(figure);
			this.turnOrder.peek().startTurn();
			this.board.repairEnviromentalMana();
		}
	}
	
	@Override
	public void onRightClick(){
		
	}

	@Override
	public void keyPressed(int key, char c) {
		
	}

	@Override
	public void onPlacementFinish(List<Combatant> placed) {
		for(Combatant combatant : placed){
			this.turnOrder.add(combatant);
			this.turnOrder.peek().setForce(0);
		}
	}
}
