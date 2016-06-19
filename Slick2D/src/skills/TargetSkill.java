package skills;

import entities.Figure;

public abstract class TargetSkill extends Skill {

	public TargetSkill(Figure caster, int range, int area) {
		super(caster, range, area);
	}
}
