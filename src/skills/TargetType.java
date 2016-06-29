package skills;

import entities.Combatant;

public enum TargetType {
	ANY("any") {
		@Override
		public boolean isValidTarget(Combatant caster, Combatant target) {
			return true;
		}
	},ALLY("ally") {
		@Override
		public boolean isValidTarget(Combatant caster, Combatant target) {
			return caster.isAllied(target);
		}
	},ENEMY("enemy") {
		@Override
		public boolean isValidTarget(Combatant caster, Combatant target) {
			return !caster.isAllied(target);
		}
	},ALIVE("alive") {
		@Override
		public boolean isValidTarget(Combatant caster, Combatant target) {
			return target.isAlive();
		}
	},DEAD("dead") {
		@Override
		public boolean isValidTarget(Combatant caster, Combatant target) {
			return !target.isAlive();
		}
	};
	
	private String id;		
	private TargetType(String id){
		this.id = id;
	}
	
	public String getId(){return this.id;}
	
	public abstract boolean isValidTarget(Combatant caster, Combatant target);
}
