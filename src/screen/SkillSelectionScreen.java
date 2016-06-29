package screen;


import java.util.List;

import main.Main;
import menu.CharacterActionMenu;
import menu.CharacterActionMenu.Button;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import skills.Skill;
import state.Board;
import entities.Combatant;

public class SkillSelectionScreen implements Screen{

	private Combatant figure;
	private Board board;
	private CharacterActionMenu characterActionMenu;
	private Button[] skills;
	
	public SkillSelectionScreen(Combatant figure, Board board) throws SlickException{
		Main.stackScreen(this);
		this.figure = figure;
		this.board = board;
		this.createButtons();
		
		characterActionMenu = new CharacterActionMenu(Main.BASIC_TILES, figure, skills);
	}
	
	@SuppressWarnings("serial")
	private void createButtons() {
		int counter = 0;
		List<Skill> list = figure.getFigure().getProfession().getLoadout();
		this.skills = new Button[list.size()];
		
		for(Skill skill : list){
			this.skills[counter] = new Button(skill.getName(), skill.getIconURL()){

				@Override
				public void onClick() {
					new SpellTargetScreen(figure, board, skill);
				}

				@Override
				public boolean isDisplayed() {
					return figure.getCurEnergy() >= skill.getCost();
				}
				
			};
			
			++counter;
		}
	}
	
	@Override
	public void render(GameContainer arg0, Graphics g) throws SlickException {
		this.characterActionMenu.render(g);
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		this.characterActionMenu.update(gc);
	}

	@Override
	public void keyPressed(int key, char c) {}

}
