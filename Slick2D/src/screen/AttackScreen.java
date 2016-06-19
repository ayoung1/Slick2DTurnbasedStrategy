package screen;

import main.Main;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import state.Board;
import tools.Pathfinding;
import tools.Pathfinding.Node;
import entities.Figure;

public class AttackScreen extends TargetScreen {
	public AttackScreen(Figure figure, Board board){
		super(figure, board, MeasureType.MANHATTAN);
	}
	
	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException{
		super.render(gc, g);
		super.getBoard().hoverEvents(Main.getSelection(), gc, g);
	}
	
	@Override
	public Node[][] nodeMethod() {
		return Pathfinding.findNodesInRange(super.getFigure(), super.getBoard(), super.getFigure().getAttackRange(), true);
	}

	@Override
	public Color getHighlightColor() {
		float opac = .5f;
		if(!super.renderingFlicker()){
			opac = opac/2;
		}
		return new Color(255, 255, 255, opac);
	}

	@Override
	public Color getSelectorColor() {
		return new Color(Color.red.r, Color.red.g, Color.red.b, 0.25f);
	}

	@Override
	public boolean renderNode(Node node) {
		return (node.getWeight() <= super.getFigure().getAttackRange()) && super.getBoard().figureAt(node.getX(), node.getY()) != super.getFigure();
	}

	@Override
	public void onTargetClick() {		
		if(super.getBoard().figureAt(Main.getSelection()) instanceof Figure){
			Figure figure = (Figure)super.getBoard().figureAt(Main.getSelection());
			
			super.getFigure().attack(figure);
			super.getFigure().removeAction();
		
			Main.removeScreen(this);
		}
	}
	
	
}
