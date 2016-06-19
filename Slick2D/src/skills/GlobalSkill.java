package skills;

import entities.Figure;

public abstract class GlobalSkill extends Skill {

	public GlobalSkill(Figure caster) {
		super(caster, 30, 30);
	}
}
