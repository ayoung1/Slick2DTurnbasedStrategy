package skills;

import main.Main;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import tools.Sprites;
import entities.Combatant;

public class EffectFactory{
	public interface EffectCreation{
		public Effect[] createEffects() throws SlickException;
	}
	
	public static abstract class Effect {
		private String name = "";
		private int duration;
		private boolean isBuff, store = true;
		private Animation animation;
		private Combatant afflicted, caster;
		
		public Effect(int duration, Animation animation, String name){
			this.duration = duration > 0 ? duration : 1;
			this.animation = animation;
			this.name = name;
		}
		
		public Effect(int duration, String name) throws SlickException{
			this.animation = null;
			this.duration = duration > 0 ? duration : 1;
			this.name = name;
		}
		
		Effect setCaster(Combatant caster){this.caster = caster; return this;}
		Effect setAfflicted(Combatant f){this.afflicted = f; return this;}
		
		public int getDuration(){return this.duration;}
		public String getName(){return this.name;}
		public Combatant getAfflicted(){return this.afflicted;}
		public Combatant getCaster(){return this.caster;}
		public boolean isBuff(){return this.isBuff;}
		
		public final void onAfflictedTurnEnd(){
			--this.duration;
			if(duration <= 0)
				this.release();
			else
				this.onTurnEnd();
		}
		
		public final void onApplication(Combatant caster, Combatant afflicted){
			this.caster = caster;
			this.afflicted = afflicted;
			this.onCast(caster, afflicted);
			if(this.store)
				this.afflicted.addEffect(this);
		};
		
		public final void increaseDuration(int amount){
			this.duration += amount;
		}
		
		public final void dispell(Combatant dispeller){
			this.afflicted.removeEffect(this);
			this.onDispell(dispeller);
		}
		
		private final void release(){	
			this.afflicted.removeEffect(this);
			this.onRelease();
		};
		
		public void onCast(Combatant caster, Combatant afflicted){};
		protected void onDispell(Combatant dispeller){};
		protected void onRelease(){};
		public void onTurnStart(){};
		public void onAttack(Combatant attacked){};
		public void onAttacked(Combatant attacker){};
		public void onTurnEnd(){};
		public void onDeath(){};
		
		public void resetAnimation(){
			if(this.animation != null)
				this.animation.restart();
		}
		
		public void render(Graphics g){
			float offset = Main.TILE_HEIGHT * 0.75f;
			if(this.animation != null)
				this.animation.draw(this.afflicted.getWidthPos() * Main.TILE_HEIGHT, this.afflicted.getHeightPos() * Main.TILE_HEIGHT - offset);
		}
	}
	
	private static EffectFactory factory;
	
	public static EffectFactory instanceOf(){
		if(factory == null)
			factory = new EffectFactory();
		return factory;
	}
	
	public Effect getDeathEffect(Combatant figure) throws SlickException{
		return new Effect(0, Sprites.getStatusAnimation("death"), "Dead"){}.setAfflicted(figure);
	}
	
	public Effect getDamageEffect(int amount) throws SlickException{
		return new Effect(0, ""){
			private int damage = amount;
			
			@Override
			public void onCast(Combatant caster, Combatant afflicted){
				super.store = false;
				super.afflicted.takeDamage(super.caster, damage);
				super.onRelease();
			}
		};
	}
	
	public Effect getHotEffect(int duration, int healPerTick, String name) throws SlickException{
		return new Effect(duration, name){
			private int heal = healPerTick;
			
			private void heal(){
				super.afflicted.gainLife(this.heal);
			}
			
			@Override
			public void onTurnStart(){
				this.heal();
			}
		};
	}
	
	public Effect getDotEffect(int duration, int damagePerTick, String name) throws SlickException{
		return new Effect(duration, name){
			private int damage = damagePerTick;
			
			private void damage(){
				super.afflicted.takeDamage(super.caster, this.damage);
			}
			
			@Override
			public void onTurnStart(){
				this.damage();
			}
		};
	}
	
	public Effect getDisableEffect(int duration) throws SlickException{
		return new Effect(duration, Sprites.getStatusAnimation("disable"), "Disabled"){
			private void effect(){
				super.afflicted.removeAction();
			}
			
			@Override
			public void onTurnStart(){
				this.effect();
			}
			
			@Override
			public void onCast(Combatant caster, Combatant afflicted){
				this.effect();
			}
			
			@Override
			public void onTurnEnd(){
				this.effect();
			}
		};
	}
	
	public Effect getStunEffect(int duration) throws SlickException{
		return new Effect(duration, "Stunned"){
			private void effect(){
				super.afflicted.removeAction();
				super.afflicted.removeMovement();
			}
			
			@Override
			public void onTurnStart(){
				this.effect();
			}
			
			@Override
			public void onCast(Combatant caster, Combatant afflicted){
				this.effect();
			}
			
			@Override
			public void onTurnEnd(){
				this.effect();
			}
		};
	}
}
