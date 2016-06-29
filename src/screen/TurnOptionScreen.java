package screen;

import main.Main;
import menu.CharacterActionMenu;
import menu.CharacterActionMenu.Button;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import state.Board;
import entities.Combatant;

public class TurnOptionScreen implements Screen {
	
	private CharacterActionMenu characterActionMenu;
	private Combatant figure;
	private Board board;
	private Button moveButton;
	private Button actionButton;
	private Button endTurnButton;
	
	public TurnOptionScreen(Combatant figure, Board board) throws SlickException{
		Main.stackScreen(this);
		this.figure = figure;
		this.board = board;
		this.createButtons();
		
		characterActionMenu = new CharacterActionMenu(Main.BASIC_TILES, figure, actionButton, moveButton, endTurnButton);
	}
	
	@SuppressWarnings("serial")
	private void createButtons(){
		endTurnButton = new Button("End Turn", "data/icon-endturn.png"){

			@Override
			public void onClick() {
				Main.removeScreen(TurnOptionScreen.this);
				figure.endTurn();
			}

			@Override
			public boolean isDisplayed() {
				return !figure.isTurnOver();
			}
		};
		actionButton = new Button("Action", "data/icon-action.png"){

			@Override
			public void onClick() {
				try {
					new ActionSelectionScreen(figure, board);
				} catch (SlickException e) {
					e.printStackTrace();
					Main.removeScreen(TurnOptionScreen.this);
				}
			}

			@Override
			public boolean isDisplayed() {
				return figure.hasAction() && !figure.isTurnOver();
			}
		};
		moveButton = new Button("Move", "data/icon-move.png"){

			@Override
			public void onClick() {
				new MoveScreen(figure, board);
			}

			@Override
			public boolean isDisplayed() {
				return figure.hasMove() && !figure.isTurnOver();
			}
		};
	}
	
	
	
	@Override
	public void render(GameContainer arg0, Graphics g) throws SlickException {
		characterActionMenu.render(g);
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		characterActionMenu.update(gc);
	}

	@Override
	public void keyPressed(int key, char c) {
		
	}

}
