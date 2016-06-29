package screen;

import main.Main;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

import entities.Combatant;
import skills.Skill;
import state.Board;
import tools.Pathfinding;
import tools.Pathfinding.Node;

public class SpellTargetScreen extends TargetScreen {

	private Skill skill;
	private Node[][] skillSelection;
	private Color highlightColor;
	
	public SpellTargetScreen(Combatant figure, Board board, Skill skill) {
		super(figure, board, MeasureType.MANHATTAN);
		this.skill = skill;
		this.highlightColor = new Color(Color.cyan.r, Color.cyan.g, Color.cyan.b, 0.5f);
	}

	@Override
	public void render(GameContainer arg0, Graphics g) throws SlickException {
		super.render(arg0, g);
		g.setColor(this.highlightColor);
		if(this.skillSelection != null){
			for(int i = 0; i < this.skillSelection.length; ++i){
				for(int j = 0; j < this.skillSelection.length; ++j){
					Node node = this.skillSelection[i][j];
					if(node.getWeight() <= this.skill.getArea()){
						g.fill(new Rectangle(node.getX()*Main.TILE_HEIGHT, node.getY()*Main.TILE_HEIGHT, Main.TILE_HEIGHT, Main.TILE_HEIGHT));
					}
				}
			}
		}
	}
	
	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		super.update(gc, delta);
		this.skillSelection = Pathfinding.findNodesInRange(Main.getSelection(), super.getBoard(), this.skill.getArea(), true);
	}
	
	@Override
	public Node[][] nodeMethod() {
		return Pathfinding.findNodesInRange(super.getFigure(), super.getBoard(), this.skill.getRange(), true);
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
		return new Color(0,0,0,0.10f);
	}

	@Override
	public boolean renderNode(Node node) {
		return node.getWeight() <= this.skill.getRange();
	}

	@Override
	public void onTargetClick() {
		List<Combatant> list = new ArrayList<>();
		Combatant[] array = null;
		for(int i = 0; i < this.skillSelection.length; ++i){
			for(int j = 0; j < this.skillSelection.length; ++j){
				Node node = this.skillSelection[i][j];
				if(node.getWeight() <= this.skill.getArea() && super.getBoard().figureAt(node.getX(), node.getY()) instanceof Combatant){
					if(skill.isValidTarget(super.getFigure(), (Combatant) super.getBoard().figureAt(node.getX(), node.getY())))
						list.add((Combatant) super.getBoard().figureAt(node.getX(), node.getY()));
				}
			}
		}
		if(list.size() > 0){
			array = new Combatant[list.size()];
			try {
				skill.onCast(super.getFigure(), list.toArray(array));
			} catch (SlickException e) {
				e.printStackTrace();
			}
			Main.removeScreen(this);
		}
	}

	@Override
	public int getMaxRange() {
		return this.skill.getRange();
	}
}