package state;
import main.Main;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.tiled.TiledMap;

import tools.Selection;
import entities.Entity;
import entities.NullEntity;
import entities.Tile;

public class Board extends TiledMap {

	private final int maxMana = 100;
	private final int transferRate = 10;
	private final int repairRate = 10;
	private final float damageRate = 2.0f;
	
	private Entity[][] tiles;
	private Entity[][] entities;
	private int[][] enviromentalMana;
	
	public Board(String ref) throws SlickException {
		super(ref);
		int ground = this.getLayerIndex("Ground");
		int mountian = this.getLayerIndex("MountianLayer");
		this.tiles = new Entity[this.height][this.width];
		this.entities = new Entity[this.height][this.width];
		this.enviromentalMana = new int[this.height][this.width];
		
		for(int x = 0; x < getHeight(); x++){
			for(int y = 0; y < getWidth(); y++){
				int tile = getTileId(x, y, ground);
				int mountianTile = getTileId(x, y, mountian);
				
				if(mountianTile == 0)
					this.tiles[x][y] = new Tile(this, tile);
				else
					this.tiles[x][y] = new Tile(this, mountianTile);
				this.entities[x][y] = new NullEntity(x, y);
				this.enviromentalMana[x][y] = this.maxMana;
			}
		}
	}

	public Entity figureAt(Selection selection){
		return this.figureAt(selection.getAdjustedX((int)(this.tileHeight)), selection.getAdjustedY((int)(this.tileWidth)));
	}
	
	public Entity figureAt(int x, int y){
		if(!this.isInBounds(x, y))
			return new NullEntity(-1, -1);
		return this.entities[x][y];
	}
	
	public void removeEntity(Entity entity){
		if(this.isInBounds(entity.getCoords()[0], entity.getCoords()[1]))
			this.entities[entity.getCoords()[0]][entity.getCoords()[1]] = new NullEntity(entity.getCoords()[0], entity.getCoords()[1]);
	}
	
	public boolean addEntity(Entity entity){
		int[] coords = entity.getCoords();
		if(this.entities[coords[0]][coords[1]].isPathable()){
			this.entities[coords[0]][coords[1]] = entity;
			return true;
		}
		return false;
	}
	
	public boolean isInBounds(int x, int y){
		if(x < 0 || x >= getWidth() || y < 0 || y >= getHeight())
			return false;
		return true;
	}
	
	public boolean isPathable(int x, int y){
		if(isInBounds(x, y)){
			if(this.tiles[x][y].isPathable())
				return this.entities[x][y].isPathable();
		}
		return false;
	}
	
	public int drainMana(int x, int y){
		float percent = this.enviromentalMana[x][y] / 100.0f;
		int amount = (int) (this.transferRate * percent);
		this.enviromentalMana[x][y] -= amount*this.damageRate;
		
		return amount;
	}
	
	public void repairEnviromentalMana(){
		for(int i = 0; i < this.enviromentalMana.length; ++i){
			for(int j = 0; j < this.enviromentalMana.length; ++j){
				this.transferMana(i, j, i+1, j);
				this.transferMana(i, j, i-1, j);
				this.transferMana(i, j, i, j+1);
				this.transferMana(i, j, i, j-1);
			}
		}
	}
	
	private void transferMana(int x, int y, int x2, int y2){
		if(this.isInBounds(x, y) && this.isInBounds(x2, y2) && this.enviromentalMana[x][y] < this.maxMana - this.repairRate){
			if(this.enviromentalMana[x][y] < this.enviromentalMana[x2][y2]){
				this.enviromentalMana[x][y] += (this.repairRate/4);
				this.enviromentalMana[x2][y2] -= (this.repairRate/4);
			}
		}
	}
	
	public void render(int x, int y, int cx, int cy, int height, int width, GameContainer arg0, Graphics g) throws SlickException{
		super.render(x, y, cx, cy, height, width);
		float alpha;
		for(int i = 0; i < getHeight(); ++i){
			for(int j = 0; j < getWidth(); ++j){
				if(arg0.getInput().isKeyDown(Input.KEY_M)){
					alpha = (float) ((this.enviromentalMana[i][j] * 0.75) / this.maxMana);
					
					g.setColor(new Color(Color.blue.getRed(), Color.blue.getGreen(), Color.blue.getBlue(), alpha));
					g.fill(new Rectangle(i*Main.TILE_HEIGHT, j*Main.TILE_HEIGHT, Main.TILE_HEIGHT, Main.TILE_HEIGHT));
				}
				this.entities[i][j].render(arg0, g);
			}
		}
	}
	
	public void clickEvents(Selection selection) throws SlickException{
		entities[selection.getAdjustedX((int)(this.tileHeight))][selection.getAdjustedY((int)(this.tileWidth))].clickEvent(this);
	}
	
	public void hoverEvents(Selection selection, GameContainer arg0, Graphics g) throws SlickException{
		tiles[selection.getAdjustedX((int)(this.tileHeight))][selection.getAdjustedY((int)(this.tileWidth))].hoverEvent(arg0, g);
		entities[selection.getAdjustedX((int)(this.tileHeight))][selection.getAdjustedY((int)(this.tileWidth))].hoverEvent(arg0, g);
	}
}
