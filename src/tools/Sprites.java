package tools;

import java.util.ArrayList;
import java.util.List;

import main.Main;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

public class Sprites {

	public static Animation getStatusAnimation(String status) throws SlickException{
		SpriteSheet sheet = null;
		List<Image> frames = new ArrayList<>();
		
		switch(status){
		case("disable"):
			sheet = new SpriteSheet("data/status/status-disable.png", Main.TILE_HEIGHT, Main.TILE_HEIGHT);
			break;
		case("death"):
			sheet = new SpriteSheet("data/status/status-death.png", Main.TILE_HEIGHT, Main.TILE_HEIGHT);
		}
		
		if(sheet == null)
			return new Animation(new Image[]{new SpriteSheet("data/blank.png", Main.TILE_HEIGHT, Main.TILE_HEIGHT).getSprite(0, 0)}, Main.UPDATE_SECOND/2);
		
		for(int i = 0; i < sheet.getWidth()/Main.TILE_HEIGHT; ++i)
			frames.add(frames.size(), sheet.getSprite(i, 0));
		
		return new Animation(frames.toArray(new Image[frames.size()]), Main.UPDATE_SECOND/2);
	}
}