package characters;

public abstract class Race {
	private String name;
	
	public Race(String name){
		this.name = name;
	}
	
	public String getName(){return this.name;}
	
	public abstract boolean isManaAttuned();
}
