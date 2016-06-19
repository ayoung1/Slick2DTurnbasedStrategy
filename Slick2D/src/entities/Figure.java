package entities;

import main.Main;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

import screen.TurnOptionScreen;
import state.Board;

public class Figure implements Entity {

	private int heightPos, widthPos;
	private int health, maxHealth;
	private int energy, maxEnergy;
	private int attackRange;
	private int movement;
	private String name;
	private SpriteSheet sprite;
	private Image deathSprite;
	
	private boolean hasMove = true;
	private boolean hasAction = true;
	private boolean turnOver = false;
	
	public Figure(int width, int height, Board board, int spriteHeight, Image deathSprite, String spriteURL) throws SlickException{
		this.name = "Satella";
		this.heightPos = height;
		this.widthPos = width;
		this.energy = this.maxEnergy = 50;
		this.health = this.maxHealth = 50;
		this.movement = 3;
		this.attackRange = 1;
		this.deathSprite = deathSprite;
		board.addFigure(this);
		this.sprite = new SpriteSheet(spriteURL, spriteHeight, spriteHeight);
		
		this.health = 40;
		this.energy = 10;
	}
	
	public int[] getCoords(){return new int[] {this.widthPos, this.heightPos};}
	public int getMovement(){return this.movement;}
	public int getAttackRange(){return this.attackRange;}
	public boolean hasMove(){return this.hasMove;}
	public boolean hasAction(){return this.hasAction;}
	public boolean isTurnOver(){return this.turnOver;}
	
	public int getWidthPos(){return this.widthPos;}
	public int getHeightPos(){return this.heightPos;}
	
	public void startTurn(){
		if(this.isAlive()){
			this.turnOver = false;
			this.gainLife(5);
			this.gainEnergy(5);
		}
	}
	
	public void endTurn(){
		if(this.isAlive()){
			this.hasAction = true;
			this.hasMove = true;
			this.turnOver = true;
		}
	}
	
	public void moveFigure(int newX, int newY, Board board){
		board.removeFigure(this);		
		this.hasMove = false;
		this.widthPos = newX;
		this.heightPos = newY;
		board.addFigure(this);		
	}
	
	public boolean isAlive(){
		return this.health > 0;
	}
	
	public void removeMovement(){
		this.hasMove = false;
	}
	
	public void removeAction(){
		this.hasAction = false;
	}
	
	public void attack(Figure target){
		target.takeDamage(this, 15);
	}
	
	public void takeDamage(Figure attacker, int amount){
		this.health = this.health - amount < 0 ? 0 : this.health - amount;
		if(!this.isAlive()){
			this.hasAction = false;
			this.hasMove = false;
			this.turnOver = true;
		}
	}
	
	public void gainEnergy(int value){
		value = value < 0 ? 0 : value;
		
		this.energy = (this.energy + value) < this.maxEnergy ? this.energy + value : this.maxEnergy;
	}
	
	public void gainLife(int value){
		value = value < 0 ? 0 : value;
		
		this.health = (this.health + value) < this.maxHealth ? this.health + value : this.maxHealth;
	}
	
	@Override
	public void hoverEvent(GameContainer arg0, Graphics g)throws SlickException {
		int height = arg0.getHeight(), width = arg0.getWidth(), x = 0, y = 0;
		String temp;
		
		if(getCoords()[0] < (width/(Main.TILE_HEIGHT * Main.SCALE)) / 2){
			x = arg0.getWidth() - (width / 3);
		}
		height = height / 2;
		width = width / 3;
		
		Shape box = new Rectangle(x, y, width, height);
		
		g.resetTransform();
		g.setColor(Color.black);
		g.fill(box);
		
		g.setColor(Color.white);
		++x;
		g.drawString((name.length() > 17)? name.substring(0, 17) : name, x, y + 1);
		y += Main.TEXT_HEIGHT;
		temp = "Health " + health + "/" + maxHealth;
		g.drawString((temp.length() > 17)? temp.substring(0, 17) : temp, x, y + 1);
		y += Main.TEXT_HEIGHT;
		temp = "Energy " + energy + "/" + maxEnergy;
		g.drawString((temp.length() > 17)? temp.substring(0, 17) : temp, x, y + 1);
		g.scale(Main.SCALE, Main.SCALE);
	}

	@Override
	public void renderResourceBars(GameContainer arg0, Graphics g) throws SlickException{
		int barHeight = Main.TILE_HEIGHT / 8;
		this.drawBars(Color.yellow, this.energy, this.maxEnergy, barHeight, barHeight, g);
		this.drawBars(Color.red, this.health, this.maxHealth, barHeight, (barHeight*2)+1, g);
	}
	
	private void drawBars(Color color, int cur, int max, int height, int heightOffset, Graphics g){
		if(cur > max)
			cur = max;
		
		double offset = Main.TILE_HEIGHT;
		offset *= 0.1;
		double barLength = (double)Main.TILE_HEIGHT - (offset * 2);
		double percent = (double)cur / max;
		double length = barLength * percent;
		g.setColor(color);
		
		g.draw(new Rectangle((float) ((widthPos*Main.TILE_HEIGHT) + offset), (heightPos*Main.TILE_HEIGHT) - heightOffset, (float) barLength, height));
		g.fill(new Rectangle((float) ((widthPos*Main.TILE_HEIGHT) + offset), (heightPos*Main.TILE_HEIGHT) - heightOffset, (float) length, height));
	}
	
	@Override
	public void render(GameContainer arg0, Graphics g) throws SlickException{
		if(this.isAlive())
			g.drawImage(sprite.getSprite(1, 0), this.widthPos * Main.TILE_HEIGHT, this.heightPos * Main.TILE_HEIGHT);
		else
			g.drawImage(this.deathSprite, this.widthPos * Main.TILE_HEIGHT, this.heightPos * Main.TILE_HEIGHT);
	}
	
	@Override
	public void clickEvent(Board board) throws SlickException {
		new TurnOptionScreen(this, board);
	}

}
