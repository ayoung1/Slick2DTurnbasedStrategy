package skills;

import entities.Figure;

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
	
	public void onSelect(){}
	
	public abstract void onCast(Figure[] targets);
}
