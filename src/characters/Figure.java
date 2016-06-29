package characters;

import java.util.HashMap;
import java.util.Map;

import main.Main;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

public class Figure {

	private int maxHealth;
	private int maxEnergy;
	private int attackRange;
	private int movement;
	private String name;
	private Map<String, Animation> animations;
	private Image deathSprite;
	private Profession profession;
	private Race race;
	
	public Figure(String spriteURL, Image deathSprite) throws SlickException{
		this.name = "Satella";
		this.maxEnergy = 50;
		this.maxHealth = 50;
		this.movement = 3;
		this.attackRange = 1;
		this.deathSprite = deathSprite;
		this.animations = new HashMap<>();
		SpriteSheet sheet = new SpriteSheet(spriteURL, Main.TILE_HEIGHT, Main.TILE_HEIGHT);
		
		this.animations.put("idle", new Animation(new Image[]{sheet.getSprite(1, 0)}, Main.UPDATE_SECOND));
		this.race = new Race("Human"){
			@Override
			public boolean isManaAttuned() {
				return false;
			}
		};
		this.profession = new Witch();
	}
	
	public Race getRace(){return this.race;}
	public String getName(){return this.name;}
	public int getMaxLife(){return this.maxHealth;}
	public int getMovement(){return this.movement;}
	public int getMaxEnergy(){return this.maxEnergy;}
	public int getAttackRange(){return this.attackRange;}
	public Image getDeathSprite(){return this.deathSprite;}
	public Profession getProfession(){return this.profession;}
	public Map<String, Animation> getAnimations(){return this.animations;}
}
