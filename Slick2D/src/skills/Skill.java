package skills;

import entities.Figure;
import screen.SpellTargetScreen;
import state.Board;

public abstract class Skill {
	private Figure caster;
	private int range;
	private int area;
	
	public Skill(Figure caster, int range, int area){
		this.caster = caster;
		this.area = --area;
		this.range = range;
	}
	
	public Figure getCaster(){return this.caster;}
	public int getRange(){return this.range;}
	public int getArea(){return this.area;}
	
	public void onSelect(Board board){
		new SpellTargetScreen(this.caster, board, this);
	}
	
	public void onCast(Figure[] targets){
		this.caster.removeAction();
	}
	
	public abstract boolean isValidTarget(Figure target);
}
