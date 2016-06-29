package characters;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.SlickException;

import entities.Combatant;
import skills.EffectFactory.Effect;
import skills.EffectFactory.EffectCreation;
import skills.Skill;
import skills.Skill.SkillBuilder;

public class Witch extends Profession {

	public Witch() {
		super("Witch");
	}

	@Override
	protected List<Skill> generateProfessionSkills() {
		List<Skill> list = new ArrayList<>();
		SkillBuilder builder = new SkillBuilder();
		
		list.add(builder.setName("Curse")
						.setArea(1)
						.setRange(6)
						.setAllowedTargets("enemy")
						.setEffects(new EffectCreation(){

							@Override
							public Effect[] createEffects()
									throws SlickException {
								return new Effect[]{
										new Effect(3, "Curse"){
											private int damage = 25;
											private int dispellDamageIncrease = 5;
											
											@Override
											public void onDispell(Combatant dispeller){
												this.increaseDuration(getDuration());
												this.damage += this.dispellDamageIncrease;
												dispeller.addEffect(this);
											}
											
											@Override
											public void onRelease(){
												this.getAfflicted().takeDamage(getCaster(), this.damage);
											}
										}
								};
							}
							
						}).build());
		
		return list;
	}

}
