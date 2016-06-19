package state;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

import tools.Selection;
import entities.Entity;
import entities.Figure;
import entities.NullEntity;
import entities.Tile;

public class Board extends TiledMap {

	private Entity[][] tiles;
	private Entity[][] figures;
	
	public Board(String ref) throws SlickException {
		super(ref);
		this.tiles = new Entity[this.height][this.width];
		this.figures = new Entity[this.height][this.width];
		
		for(int x = 0; x < getHeight(); x++){
			for(int y = 0; y < getWidth(); y++){
				int tile = getTileId(x, y, 0);
				int mountian = getTileId(x, y, 2);
				
				if(mountian == 0)
					this.tiles[x][y] = new Tile(this, tile);
				else
					this.tiles[x][y] = new Tile(this, mountian);
				this.figures[x][y] = new NullEntity();
			}
		}
	}

	public Entity figureAt(Selection selection){
		return this.figureAt(selection.getAdjustedX((int)(this.tileHeight)), selection.getAdjustedY((int)(this.tileWidth)));
	}
	
	public Entity figureAt(int x, int y){
		return this.figures[x][y];
	}
	
	public void removeFigure(Figure figure){
		this.figures[figure.getCoords()[0]][figure.getCoords()[1]] = new NullEntity();
	}
	
	public void addFigure(Figure figure){
		this.figures[figure.getCoords()[0]][figure.getCoords()[1]] = figure;
	}
	
	public boolean isInBounds(int x, int y){
		if(x < 0 || x >= getWidth() || y < 0 || y >= getHeight())
			return false;
		return true;
	}
	
	public boolean isPathable(int x, int y){
		if(isInBounds(x, y)){
			if(this.tiles[x][y].isPathable())
				return this.figures[x][y].isPathable();
		}
		return false;
	}
	
	public void render(int x, int y, int cx, int cy, int height, int width, GameContainer arg0, Graphics g) throws SlickException{
		super.render(x, y, cx, cy, height, width);
		for(int i = 0; i < getHeight(); ++i){
			for(int j = 0; j < getWidth(); ++j){
				this.figures[i][j].render(arg0, g);
			}
		}
		for(int i = 0; i < getHeight(); ++i){
			for(int j = 0; j < getWidth(); ++j){
				this.figures[i][j].renderResourceBars(arg0, g);
			}
		}
	}
	
	public void clickEvents(Selection selection) throws SlickException{
		figures[selection.getAdjustedX((int)(this.tileHeight))][selection.getAdjustedY((int)(this.tileWidth))].clickEvent(this);
	}
	
	public void hoverEvents(Selection selection, GameContainer arg0, Graphics g) throws SlickException{
		tiles[selection.getAdjustedX((int)(this.tileHeight))][selection.getAdjustedY((int)(this.tileWidth))].hoverEvent(arg0, g);
		figures[selection.getAdjustedX((int)(this.tileHeight))][selection.getAdjustedY((int)(this.tileWidth))].hoverEvent(arg0, g);
	}
}
