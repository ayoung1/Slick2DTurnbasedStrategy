package state;

import screen.Screen;
import tools.Selection;

public abstract class State {
	private Screen mainScreen;
	
	public State(Screen mainScreen){
		this.mainScreen = mainScreen;
	}
	
	public Screen getScreen(){
		return this.mainScreen;
	}
	
	public abstract Selection getSelection();
}
