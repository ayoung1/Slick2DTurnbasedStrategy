package screen;

import java.util.LinkedList;
import java.util.Queue;

import main.Main;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;

import entities.Figure;
import state.Board;

public class MapScreen implements Screen {

	private Board board;
	private Queue<Figure> turnOrder;
	
	public MapScreen() throws SlickException{
		this.board = new Board("data/MapCompressed64.tmx");
		this.turnOrder = new LinkedList<>();
		
		this.turnOrder.add(new Figure(10, 5, this.board, 16, new SpriteSheet("data/dead.png", Main.TILE_HEIGHT, Main.TILE_HEIGHT).getSprite(2, 0), "data/female.png"));
		this.turnOrder.add(new Figure(5, 6, this.board, 16, new SpriteSheet("data/dead.png", Main.TILE_HEIGHT, Main.TILE_HEIGHT).getSprite(2, 0), "data/female.png"));
	}
	
	@Override
	public void hoverEvents(GameContainer arg0, Graphics g) throws SlickException{
		board.hoverEvents(Main.getSelection(), arg0, g);
	}
	
	@Override
	public void render(GameContainer arg0, Graphics g) throws SlickException {
		int x = this.turnOrder.peek().getWidthPos() * Main.TILE_HEIGHT, y = this.turnOrder.peek().getHeightPos() * Main.TILE_HEIGHT;
		this.board.render(0, 0, 0, 0, 15, 15, arg0, g);
		g.setColor(Color.cyan);
		g.draw(new Rectangle(x, y, Main.TILE_HEIGHT, Main.TILE_HEIGHT));
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		Figure figure;
		if(gc.getInput().isMousePressed(0) && this.board.figureAt(Main.getSelection()) instanceof Figure){
			figure = (Figure) this.board.figureAt(Main.getSelection());
			if(figure == this.turnOrder.peek())
				figure.clickEvent(this.board);
		}
		if(this.turnOrder.peek().isTurnOver()){
			figure = this.turnOrder.poll();
			this.turnOrder.remove(0);
			this.turnOrder.add(figure);
			this.turnOrder.peek().startTurn();
		}
	}

	@Override
	public void keyPressed(int key, char c) {
		System.out.println("Key Press");
	}

}
