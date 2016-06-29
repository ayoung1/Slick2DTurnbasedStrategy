package menu;

import main.Main;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.RoundedRectangle;

import skills.EffectFactory.Effect;
import entities.Combatant;

public class FigureInfoSlider extends RoundedRectangle{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7917553829621436764L;

	private int maxSlideX;
	private int minSlideX;
	private int timer;
	private Combatant figure;
	
	public FigureInfoSlider(GameContainer gc, Combatant figure) {
		super(0 - gc.getWidth(), gc.getHeight() - gc.getHeight()/4, gc.getWidth(), gc.getHeight()/4, 50);
		this.minSlideX = (int) this.minX;
		this.maxSlideX = 0 - gc.getWidth()/2;
		this.figure = figure;
	}
	
	public void render(Graphics g){
		String name = this.figure.getFigure().getName(), temp = "";
		int health = this.figure.getCurLife(), maxHealth = this.figure.getFigure().getMaxLife(),
				energy = this.figure.getCurEnergy(), maxEnergy = this.figure.getFigure().getMaxEnergy(),
				x = (int) (this.x + this.width*.55), y = (int) this.y;
		Effect effect;
		
		g.resetTransform();
		if(this.figure.getForce() == 0)
			g.setColor(new Color(Color.cyan.r, Color.cyan.g, Color.cyan.b, 0.75f));
		else
			g.setColor(new Color(Color.green.r, Color.green.g, Color.green.b, 0.75f));
		g.fill(this);
		
		g.setColor(Color.black);
		++x;
		g.drawString(name, x, y);
		y += Main.TEXT_HEIGHT;
		temp = "L." + this.figure.getFigure().getProfession().getLevel() + " " + this.figure.getFigure().getProfession().getName();
		g.drawString(temp, x, y);
		y += Main.TEXT_HEIGHT;
		temp = "Health " + health + "/" + maxHealth;
		g.drawString(temp, x, y);
		y += Main.TEXT_HEIGHT;
		temp = "Energy " + energy + "/" + maxEnergy;
		g.drawString(temp, x, y);
		
		if(this.figure.getEffectList().size() > 0){
			y += Main.TEXT_HEIGHT;
			effect = this.figure.getEffectList().get(this.figure.displayedEffect());
			temp = effect.getName() + " tr." + effect.getDuration();
			g.drawString(temp, x, y);
		}
		
		g.scale(Main.SCALE, Main.SCALE);
	}
	
	public void slideOut(int delta){
		this.timer += delta;
		if(this.timer >= Main.UPDATE_SECOND/100){
			this.timer -= Main.UPDATE_SECOND/100;
			if(this.x < this.maxSlideX){
				this.setBounds(this.x + 10, this.y, this.width, this.height);
			}
		}
	}
	
	public void slideIn(int delta){
		this.timer += delta;
		if(this.timer >= Main.UPDATE_SECOND/100){
			this.timer -= Main.UPDATE_SECOND/100;
			if(this.x > this.minSlideX){
				this.setBounds(this.x - 10, this.y, this.width, this.height);
			}
		}
	}
}
