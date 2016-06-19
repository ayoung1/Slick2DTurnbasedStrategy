package screen;

import main.Main;

import org.newdawn.slick.Color;
import state.Board;
import tools.Pathfinding;
import tools.Pathfinding.Node;
import entities.Figure;

public class MoveScreen extends TargetScreen {
	public MoveScreen(Figure figure, Board board){
		super(figure, board, MeasureType.MANHATTAN);
	}
	
	@Override
	public Node[][] nodeMethod() {
		return Pathfinding.findPathableNodes(super.getFigure(), super.getBoard(), super.getFigure().getMovement());
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
		return new Color(Color.blue.r, Color.blue.g, Color.blue.b, 0.25f);
	}

	@Override
	public boolean renderNode(Node node) {
		return node.getWeight() > 0 && node.getWeight() <= super.getFigure().getMovement();
	}

	@Override
	public void onTargetClick() {
		int x = Main.getSelection().getAdjustedX((int)(super.getBoard().getTileHeight())), y = Main.getSelection().getAdjustedY((int)(super.getBoard().getTileHeight()));
		super.getFigure().moveFigure(x, y, super.getBoard());
		super.getFigure().removeMovement();
		Main.removeScreen(this);
	}
	
	
}
