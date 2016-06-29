package menu;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Rectangle;

import screen.Screen;

public abstract class Menu implements Screen{

	public class MenuButton extends Rectangle{
		/**
		 * 
		 */
		private static final long serialVersionUID = -4894519754517191700L;
		private MenuOption option;
		
		private MenuButton(MenuOption option) {
			super(0, 0, 0f, 0f);
			this.option = option;
		}
		
		public void click(int mx, int my, Menu menu){
			if(this.isMouseHovered(mx, my))
				this.option.onClick(menu);
		}
		
		public boolean isMouseHovered(int mx, int my){
			return this.minX <= mx && this.maxX >= mx && this.minY <= my && this.maxY >= my;
		}
	}
	
	private List<MenuButton> menuButtons;
	
	public Menu(MenuOption ... options){
		this.menuButtons = new ArrayList<>();
		for(MenuOption option : options)
			this.menuButtons.add(new MenuButton(option));
	}
	
	@Override
	public void update(GameContainer gc, int delta){
		if(gc.getInput().isMouseButtonDown(0)){
			for(MenuButton button : menuButtons)
				button.click(Mouse.getX(), Mouse.getY(), this);
		}
	}
}
