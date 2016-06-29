package entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.Main;
import menu.FigureInfoSlider;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

import characters.Figure;
import screen.TurnOptionScreen;
import skills.EffectFactory;
import skills.EffectFactory.Effect;
import state.Board;

public class Combatant implements Entity{

	private int effectFlickerTime = 0, renderedEffect = 0;
	private int health, energy;
	private int heightPos, widthPos;
	private int force;
	private boolean renderEffect = false;
	private FigureInfoSlider infoSlider = null;
	private Figure figure;
	private Board board;
	
	private boolean hasMove = true;
	private boolean hasAction = true;
	private boolean turnOver = false;
	
	private Effect death;
	private Map<String, Effect> effects;
	private List<String> toRemove;
	
	public static Combatant constructCombatant(Figure figure, Board board, int xloc, int yloc) throws Exception{
		Combatant temp;
		if(figure == null || board == null){
			throw new IllegalArgumentException("Figure or Board passed to Combatant is null");
		}else if(!board.isPathable(xloc, yloc))
			throw new Exception("Passed placement location not pathable");
		
		temp = new Combatant(figure, board, xloc, yloc);
		
		return temp;
	}
	
	private Combatant(Figure figure, Board board, int xloc, int yloc) throws SlickException{
		this.widthPos = xloc;
		this.heightPos = yloc;
		this.board = board;
		this.figure = figure;
		this.death = EffectFactory.instanceOf().getDeathEffect(this);
		this.energy = 0;
		this.effects = new HashMap<>();
		this.toRemove = new ArrayList<>();
		
		this.revive(1.0f);
		this.board.addEntity(this);
	}
	
	public int displayedEffect(){return this.renderedEffect;}
	public int getCurLife(){return this.health;}
	public int getCurEnergy(){return this.energy;}
	public int getWidthPos(){return this.widthPos;}
	public int getHeightPos(){return this.heightPos;}
	public int getForce(){return this.force;}
	public boolean hasMove(){return this.hasMove;}
	public boolean hasAction(){return this.hasAction;}
	public boolean isTurnOver(){return this.turnOver;}
	public List<Effect> getEffectList(){return new ArrayList<>(this.effects.values());}
	public Figure getFigure(){return this.figure;}
	public Board getBoard(){return this.board;}
	
	private void clearEffects(){
		if(this.toRemove.size() > 0){
			for(String string : this.toRemove)
				this.effects.remove(string);
			this.toRemove.clear();
		}
	}
	
	public void removeEffect(Effect effect){
		this.toRemove.add(effect.getName());
	}
	
	public void addEffect(Effect effect){
		if(this.effects.containsKey(effect.getName())){
			if(this.effects.get(effect.getName()).getDuration() < effect.getDuration())
				this.effects.put(effect.getName(), effect);
		}else
			this.effects.put(effect.getName(), effect);
	}
	
	public void setForce(int force){
		this.force = force;
	}
	
	public boolean isAllied(Combatant figure){
		return this.force == figure.force;
	}
	
	public void startTurn(){
		this.clearEffects();
		if(!this.isAlive()){
			this.turnOver = true;
		}else{
			this.turnOver = false;
			for(Effect effect : this.effects.values())
				effect.onTurnStart();
		}
	}
	
	public void endTurn(){
		if(this.isAlive()){
			this.hasAction = true;
			this.hasMove = true;
			this.turnOver = true;
			this.gainLife(5);
			
			if(this.figure.getRace().isManaAttuned())
				this.gainEnergy(5);
			else
				this.gainEnergy(this.board.drainMana(widthPos, heightPos));
			for(Effect effect : this.effects.values())
				effect.onAfflictedTurnEnd();
		}
		this.clearEffects();
	}
	
	public void moveFigure(int newX, int newY, Board board){
		board.removeEntity(this);		
		this.hasMove = false;
		this.widthPos = newX;
		this.heightPos = newY;
		board.addEntity(this);		
	}
	
	public void removeEnergy(int amount){
		this.energy -= amount;
		this.energy = this.energy < 0 ? 0 : this.energy;
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
	
	public void attack(Combatant target){
		for(Effect effect : this.effects.values())
			effect.onAttack(target);
		
		target.takeDamage(this, 15);
	}
	
	public void takeDamage(Combatant attacker, int amount){
		for(Effect effect : this.effects.values())
			effect.onAttacked(attacker);
		
		this.health = this.health - amount < 0 ? 0 : this.health - amount;
		if(!this.isAlive()){
			for(Effect effect : this.effects.values())
				effect.onDeath();
		}
		
		if(!this.isAlive()){
			this.effects.clear();
			this.effects.put(this.death.getName(), this.death);
			this.hasAction = false;
			this.hasMove = false;
			this.turnOver = true;
		}
	}
	
	public void gainEnergy(int value){
		if(this.isAlive()){
			value = value < 0 ? 0 : value;
			this.energy = (this.energy + value) < this.figure.getMaxEnergy() ? this.energy + value : this.figure.getMaxEnergy();
		}
	}
	
	public void revive(float percent){
		if(!this.isAlive()){
			this.health = (int)((float)this.figure.getMaxLife() * percent);
			this.health = this.health > this.figure.getMaxLife() ? this.figure.getMaxLife() : this.health;
		}
	}
	
	public void gainLife(int value){
		if(this.isAlive()){
			value = value < 0 ? 0 : value;
			this.health = (this.health + value) < this.figure.getMaxLife() ? this.health + value : this.figure.getMaxLife();
		}
	}
	
	private void renderResourceBars(GameContainer arg0, Graphics g) throws SlickException{
		int barHeight = Main.TILE_HEIGHT / 8;
		this.drawBars(Color.cyan, this.energy, this.figure.getMaxEnergy(), barHeight, barHeight, g);
		this.drawBars(Color.red, this.health, this.figure.getMaxLife(), barHeight, (barHeight*2)+1, g);
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
	
	private boolean isHovered(){
		if(Main.getSelection().getAdjustedX(Main.TILE_HEIGHT) == this.getWidthPos() && Main.getSelection().getAdjustedY(Main.TILE_HEIGHT) == this.getHeightPos())
			return true;
		return false;
	}
	
	@Override
	public void update(GameContainer arg0, int delta){
		this.effectFlickerTime += delta;
		if(this.infoSlider == null)
			this.infoSlider = new FigureInfoSlider(arg0, this);
		
		if(this.isHovered())
			this.infoSlider.slideOut(delta);
		else
			this.infoSlider.slideIn(delta);
		
		if(this.effectFlickerTime >= Main.UPDATE_SECOND){
			this.effectFlickerTime -= Main.UPDATE_SECOND;
			this.renderEffect = !this.renderEffect;
			if(this.renderEffect){
				++this.renderedEffect;
				if(this.renderedEffect >= this.effects.size()){
					this.renderedEffect = 0;
				}
				if(this.effects.size() > 0)
					this.getEffectList().get(this.renderedEffect).resetAnimation();
			}
		}
	}
	
	@Override
	public void render(GameContainer arg0, Graphics g) throws SlickException{
		if(this.isAlive()){
			this.figure.getAnimations().get("idle").draw(this.widthPos * Main.TILE_HEIGHT, this.heightPos * Main.TILE_HEIGHT);
			this.renderResourceBars(arg0, g);
			if(this.effects.size() > 0 && this.renderedEffect < this.effects.size() && this.renderEffect)
				this.getEffectList().get(this.renderedEffect).render(g);
		}else{
			g.drawImage(this.figure.getDeathSprite(), this.widthPos * Main.TILE_HEIGHT, this.heightPos * Main.TILE_HEIGHT);
			this.death.render(g);
		}
		
		if(this.infoSlider != null)
			this.infoSlider.render(g);
	}
	
	@Override
	public void clickEvent(Board board) throws SlickException {
		new TurnOptionScreen(this, board);
	}

	@Override
	public int[] getCoords() {
		return new int[]{this.getWidthPos(), this.getHeightPos()};
	}
}
