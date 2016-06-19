package menu;

import java.util.ArrayList;
import java.util.List;

import main.Main;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;

import entities.Figure;

public class CharacterActionMenu {

	@SuppressWarnings("serial")
	public static abstract class Button extends Rectangle{
		private Image sprite = null;
		private String name;
		private int x, y;
		
		public Button(String name, String imageURL){
			super(0, 0, Main.TILE_HEIGHT, Main.TILE_HEIGHT);
			this.name = name;
			try {
				this.sprite = new SpriteSheet(imageURL, Main.TILE_HEIGHT, Main.TILE_HEIGHT);
			} catch (SlickException e) {
				e.printStackTrace();
				try {
					this.sprite = new SpriteSheet("data/blank.png", Main.TILE_HEIGHT, Main.TILE_HEIGHT);
				} catch (SlickException e1) {
					e1.printStackTrace();
					System.exit(-1 );
				}
			}
		}
		
		public Button(String name, int spriteX, int spriteY) {
			super(0, 0, Main.TILE_HEIGHT, Main.TILE_HEIGHT);
			this.name = name;
			this.x = spriteX;
			this.y = spriteY;
		}
		
		private boolean withinBounds(int mouseX, int mouseY){
			mouseY = (int) (240 - (mouseY / Main.SCALE));
			mouseX = (int) (mouseX / Main.SCALE);
						
			return (mouseX > this.minX && mouseX < this.maxX && mouseY > this.minY && mouseY < this.maxY);
		}
		
		private void assignImage(SpriteSheet sheet){
			this.sprite = sheet.getSprite(x, y);
		}
		
		public abstract void onClick();
		public abstract boolean isDisplayed();
	}
	
	private List<Button> actions;
	private Figure figure;
	
	public CharacterActionMenu(SpriteSheet spriteSheet, Figure figure, Button ... buttons){
		this.actions = new ArrayList<>();
		this.figure = figure;
		for(Button button : buttons){
			if(button.sprite == null)
				button.assignImage(spriteSheet);
			this.actions.add(0, button);
		}
	}
	
	private void setButtonLocations(){
		int[] coords = figure.getCoords();
		int length = actions.size() * Main.TILE_HEIGHT;
		int figureWidth = coords[0] * Main.TILE_HEIGHT;
		int figureHeight = coords[1] * Main.TILE_HEIGHT;
		int direction = 0;
		int count = 0;
		int position = 0;
		
		if(figureWidth + length > 240)
			direction = 1;
		else
			direction = -1;
		
		if(figure.getHeightPos() > 0){
			figureHeight -= Main.TILE_HEIGHT;
		}else
			figureHeight += Main.TILE_HEIGHT;
		
		if(direction < 0){
			--position;
			for(Button button : actions){
				if(button.isDisplayed())
					++position;
			}
		}
		
		for(Button button : actions){
			button = actions.get(count);
			if(button.isDisplayed()){
				button.setY(figureHeight);
				button.setX(figureWidth - ((Main.TILE_HEIGHT * position) * direction));
				position += direction;
			}
			++count;
		}
	}
	
	public void render(Graphics g){
		g.setColor(Color.white);
		for(Button button : actions){
			if(button.isDisplayed()){
				if(button.withinBounds(Mouse.getX(), Mouse.getY())){
					g.resetTransform();
					g.drawString(button.name, actions.get(actions.size()-1).getX()*Main.SCALE, (actions.get(0).getY()*Main.SCALE) - Main.TEXT_HEIGHT);
					g.scale(Main.SCALE, Main.SCALE);
				}
				g.drawImage(button.sprite, button.getX(), button.getY());
			}
		}
	}
	
	public void update(GameContainer gc){
		this.setButtonLocations();

		if(gc.getInput().isMousePressed(0)){
			for(Button button : actions){
				if(button.withinBounds(Mouse.getX(), Mouse.getY()) && button.isDisplayed())
					button.onClick();
			}
		}
	}
}
