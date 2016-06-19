package screen;

import main.Main;
import menu.CharacterActionMenu;
import menu.CharacterActionMenu.Button;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import skills.Skill;
import state.Board;
import entities.Figure;

public class ActionSelectionScreen implements Screen {

	private Figure figure;
	private Board board;
	private CharacterActionMenu characterActionMenu;
	private Button attackButton, itemButton, skillButton;
	
	public ActionSelectionScreen(Figure figure, Board board) throws SlickException{
		Main.stackScreen(this);
		this.figure = figure;
		this.board = board;
		this.createButtons();
		
		characterActionMenu = new CharacterActionMenu(Main.BASIC_TILES, figure, attackButton, skillButton, itemButton);
	}
	
	@SuppressWarnings("serial")
	private void createButtons() {
		this.skillButton = new Button("Skills", "data/icon-skills.png"){

			@Override
			public void onClick() {
				new SpellTargetScreen(figure, board, new Skill(figure, 5, 1){

					@Override
					public void onCast(Figure[] targets) {
						
					}});
			}

			@Override
			public boolean isDisplayed() {
				return true;
			}
			
		};
		
		this.itemButton = new Button("Item", "data/icon-items.png"){

			@Override
			public void onClick() {
				
			}

			@Override
			public boolean isDisplayed() {
				return true;
			}
			
		};
		
		this.attackButton = new Button("Attack", "data/icon-attack.png"){

			@Override
			public void onClick() {
				new AttackScreen(figure, board);
			}

			@Override
			public boolean isDisplayed() {
				return true;
			}
			
		};
	}

	@Override
	public void render(GameContainer arg0, Graphics g) throws SlickException {
		this.characterActionMenu.render(g);
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		if(this.figure.hasAction())
			this.characterActionMenu.update(gc);
		else
			Main.removeScreen(this);
	}

	@Override
	public void keyPressed(int key, char c) {
		
	}

}
