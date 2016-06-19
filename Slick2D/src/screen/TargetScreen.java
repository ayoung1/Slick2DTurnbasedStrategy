package screen;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

import main.Main;
import state.Board;
import tools.Pathfinding.Node;
import trig.Trig;
import entities.Figure;

public abstract class TargetScreen implements Screen{
	public enum MeasureType{
		MANHATTAN, EUCLIDEAN;
	}
	
	private Figure figure;
	private Board board;
	private MeasureType measure;
	private boolean flicker = true;
	private boolean render = true;
	private int time = 0;
	private Node[][] nodes;
	
	public TargetScreen(Figure figure, Board board, MeasureType measure){
		Main.stackScreen(this);
		this.figure = figure;
		this.board = board;
		this.measure = measure;
	}
	
	protected Figure getFigure(){return this.figure;}
	protected Board getBoard(){return this.board;}
	protected Node[][] getNodes(){return this.nodes;}
	
	protected void setFlicker(boolean flicker){
		this.flicker = flicker;
	}
	
	protected boolean renderingFlicker(){
		return this.render;
	}
	
	protected boolean inRangeEuclidean(int i, int j){
		if(Trig.euclideanDistanceBetweenPoints(this.figure.getWidthPos(), this.figure.getHeightPos(), i, j) <= (double)(this.figure.getMovement()))
			return true;
		return false;
	}
	
	protected boolean inRangeManhattan(int i, int j){
		if(Trig.manhattanDistanceBetweenPoints(this.figure.getWidthPos(), this.figure.getHeightPos(), i, j) <= (double)(this.figure.getMovement()))
			return true;
		return false;
	}
	
	protected boolean isWithinTargetRange(int i, int j){
		if(this.measure == MeasureType.EUCLIDEAN)
			return (inRangeEuclidean(i, j));
		return (inRangeManhattan(i, j));
	}
	
	@Override
	public void render(GameContainer arg0, Graphics g) throws SlickException {
		if(this.nodes != null){
			g.setColor(this.getHighlightColor());
			for(int i = 0; i < nodes.length; ++i){
				for(int j = 0; j < nodes.length; ++j){
					int x = nodes[i][j].getX(), y = nodes[i][j].getY();
					
					if(this.renderNode(nodes[i][j])){
						if(this.isWithinTargetRange(x, y)){
							Shape box = new Rectangle(x*Main.TILE_HEIGHT, y*Main.TILE_HEIGHT, Main.TILE_HEIGHT, Main.TILE_HEIGHT);
							g.fill(box);
						}
					}
				}
			}
			
			g.setColor(this.getSelectorColor());
			g.fill(Main.getSelection());
		}
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		if(this.nodes == null)
			this.nodes = nodeMethod();
		this.time += delta;
		Main.disableSelectorGraphic();
		if(this.flicker && this.time > Main.UPDATE_SECOND){
			this.time -= Main.UPDATE_SECOND;
			this.render = !this.render;
		}
		
		if(gc.getInput().isMousePressed(0)){
			int x = Main.getSelection().getAdjustedX(Main.TILE_HEIGHT), y = Main.getSelection().getAdjustedY(Main.TILE_HEIGHT);
			for(Node[] nodearray : nodes){
				for(Node node : nodearray){
					if(node.getX() == x && node.getY() == y && this.renderNode(node))
						this.onTargetClick();
				}
			}
		}
	}

	@Override
	public void keyPressed(int key, char c) {
		
	}
	
	public abstract Node[][] nodeMethod();
	public abstract Color getHighlightColor();
	public abstract Color getSelectorColor();
	public abstract boolean renderNode(Node node);
	public abstract void onTargetClick();
}
