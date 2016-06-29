package characters;

import java.util.ArrayList;
import java.util.List;

import skills.Skill;

public abstract class Profession {
	private static int expRequired = 100;
	
	private String name;
	private List<Skill> skills;
	private List<Skill> professionSkills;
	private List<Skill> loadout;
	private int level;
	private int exp;
	
	public Profession(String name){
		this.name = name;
		this.skills = new ArrayList<>();
		this.loadout = new ArrayList<>();
		this.professionSkills = this.generateProfessionSkills();
		this.loadout.add(this.professionSkills.get(0));
		this.level = 100;
		this.exp = 0;
	}
	
	public String getName() {
		return name;
	}
	
	public List<Skill> getLoadout() {
		return this.loadout;
	}
	
	public List<Skill> getSkills() {
		return skills;
	}

	public List<Skill> getProfessionSkills(){
		return this.professionSkills;
	}
	
	public int getLevel() {
		return level;
	}

	public int getExp() {
		return exp;
	}

	public void addExp(int exp) {
		exp = exp > 0 ? exp : 0;
		this.exp += exp;
		if(this.exp > expRequired){
			this.exp = 0;
			++this.level;
		}
	}
	
	protected abstract List<Skill> generateProfessionSkills();
}
