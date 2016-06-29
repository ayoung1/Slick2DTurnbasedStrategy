package skills;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.SlickException;

import entities.Combatant;
import skills.EffectFactory.Effect;
import skills.EffectFactory.EffectCreation;
import state.Board;

public class Skill {
	
	public static final int NAME_LENGTH = 10;
	public static final int TOOLTIP_LENGTH = 25;
	
	public static class SkillBuilder{
		
		private String name = "Unnamed", toolTip = "Tooltip Missing", icon = "data/blank.png";
		private int range = 1, area = 0, cost = 0;
		private TargetType[] allowedTargets = new TargetType[] {};
		private EffectCreation effectCreation = new EffectCreation() {
			@Override
			public Effect[] createEffects() {
				return new Effect[]{};
			}
		};
		
		public SkillBuilder setName(String name){
			this.name = name.length() < NAME_LENGTH ? name : name.substring(0, NAME_LENGTH);
			return this;
		}
		
		public SkillBuilder setTooltip(String toolTip){
			this.toolTip = toolTip.length() < TOOLTIP_LENGTH ? toolTip : toolTip.substring(0, TOOLTIP_LENGTH);
			return this;
		}
		
		public SkillBuilder setArea(int area){
			this.area = area;
			return this;
		}
		
		public SkillBuilder setIconURL(String url){
			this.icon = url;
			return this;
		}
		
		public SkillBuilder setRange(int range){
			this.range = range;
			return this;
		}
		
		public SkillBuilder setCost(int cost){
			this.cost = cost;
			return this;
		}
		
		public SkillBuilder setAllowedTargets(String ... id){
			List<TargetType> list = new ArrayList<>();
			for(String string : id){
				for(TargetType type : TargetType.values()){
					if(type.getId().compareToIgnoreCase(string) == 0)
						list.add(type);
				}
			}
			this.allowedTargets = list.toArray(this.allowedTargets);
			return this;
		}
		
		public SkillBuilder setEffects(EffectCreation creation){
			this.effectCreation = creation;
			return this;
		}
		
		public Skill build(){
			return new Skill(this.name, this.toolTip, this.icon, this.range, this.area, this.cost, this.allowedTargets, this.effectCreation);
		}
	}
	
	private TargetType[] allowedTargets;
	private EffectCreation effects;
	private String name, toolTip, icon;
	private int range;
	private int area;
	private int cost;
	
	private Skill(String name, String toolTip, String icon, int range, int area, int cost, TargetType[] allowedTargets, EffectCreation effects){
		this.area = --area;
		this.icon = icon;
		this.range = range;
		this.name = name;
		this.cost = cost;
		this.toolTip = toolTip;
		this.allowedTargets = allowedTargets;
		this.effects = effects;
		this.filterDescription();
	}
	
	public String getName(){return this.name;}
	public String getIconURL(){return this.icon;}
	public int getRange(){return this.range;}
	public int getArea(){return this.area;}
	public int getCost(){return this.cost;}
	
	private void filterDescription(){
		this.toolTip = this.toolTip.replaceAll("%name", this.name);
		this.toolTip = this.toolTip.replaceAll("%range", "" + this.range);
		this.toolTip = this.toolTip.replaceAll("%area", "" + this.area);
	}
	
	public void onSelect(Board board){
		
	}
	
	public final void onCast(Combatant caster, Combatant[] targets) throws SlickException{
		caster.removeEnergy(cost);
		caster.removeAction();
		this.spellEffect(caster, targets);
	}
	
	public boolean isValidTarget(Combatant caster, Combatant target){
		boolean valid = true;
		
		for(TargetType type : this.allowedTargets)
			valid = valid && type.isValidTarget(caster, target);
		
		return valid;
	}
	
	private void spellEffect(Combatant caster, Combatant[] targets) throws SlickException{
		for(Combatant target : targets){
			for(Effect effect : this.effects.createEffects()){
				effect.onApplication(caster, target);
			}
		}
	}
}
