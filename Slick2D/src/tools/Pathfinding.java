package tools;

import main.Main;
import state.Board;
import trig.Trig;
import entities.Figure;

public class Pathfinding {

	public static class Node{
		private int x, y, weight = Integer.MAX_VALUE-1;
		
		public Node(int x, int y){
			this.x = x;
			this.y = y;
		}
		
		public int getWeight(){return weight;}
		public int getX(){return x;}
		public int getY(){return y;}
	}
	
	public static Node[][] findPathableNodes(Selection selection, Board board, int range){
		return findPathableNodes(selection.getAdjustedX(Main.TILE_HEIGHT), selection.getAdjustedY(Main.TILE_HEIGHT), board, range);
	}
	
	public static Node[][] findNodesInRange(Selection selection, Board board, int range, boolean ignoreBlockingTiles){
		return findNodesInRange(selection.getAdjustedX(Main.TILE_HEIGHT), selection.getAdjustedY(Main.TILE_HEIGHT), board, range, ignoreBlockingTiles);
	}
	
	public static Node[][] findPathableNodes(Figure figure, Board board, int range){
		return findPathableNodes(figure.getWidthPos(), figure.getHeightPos(), board, range);
	}
	
	public static Node[][] findNodesInRange(Figure figure, Board board, int range, boolean ignoreBlockingTiles){
		return findNodesInRange(figure.getWidthPos(), figure.getHeightPos(), board, range, ignoreBlockingTiles);
	}
	
	public static Node[][] findPathableNodes(int width, int height, Board board, int range){
		Node[][] nodes = setupArray(width, height, range);
		
		return calculateWeights(nodes, nodes.length, board);
	}
	
	public static Node[][] findNodesInRange(int width, int height, Board board, int range, boolean ignoreBlockingTiles){
		Node[][] nodes = setupArray(width, height, range);
		
		return calculateWeightsIgnoreBlocks(nodes, nodes.length, board, width, height, ignoreBlockingTiles);
	}
	
	private static Node[][] setupArray(int width, int height, int range){
		int mov = range;
		int sqr = (mov*2) + 1;
		Node[][] nodes = new Node[sqr][sqr];
		
		for(int x = width - mov, i = 0; x < width + mov+1; ++x, ++i){
			for(int y = height - mov, j = 0; y < height + mov+1; ++y, ++j){
				nodes[i][j] = new Node(x, y);
			}
		}
		
		nodes[sqr - mov - 1][sqr - mov - 1].weight = 0;
		return nodes;
	}
	
	private static boolean inRange(int i, int j, int length){
		return (i >= 0) && (i < length) && (j >= 0) && (j < length);
	}
	
	private static Node[][] calculateWeightsIgnoreBlocks(Node[][] nodes, int length, Board board, int width, int height, boolean ignoreBlocks){
		for(int i = 0; i < length; ++i){
			for(int j = 0; j < length; ++j){
				if(!ignoreBlocks && !board.isPathable(nodes[i][j].x, nodes[i][j].y))
					continue;
				nodes[i][j].weight = (int) Trig.manhattanDistanceBetweenPoints(nodes[i][j].x, nodes[i][j].y, width, height);
			}
		}
		
		return nodes;
	}
	
	private static Node[][] calculateWeights(Node[][] nodes, int length, Board board){
		boolean modified;
		do{
			modified = false;
			
			for(int i = 0; i < length; ++i){
				for(int j = 0; j < length; ++j){
					if(!board.isPathable(nodes[i][j].x, nodes[i][j].y)){
						if(nodes[i][j].weight != 0)
							continue;
					}
					if(inRange(i+1, j, length) && board.isPathable(nodes[i+1][j].x, nodes[i+1][j].y)){
						if(nodes[i+1][j].weight > nodes[i][j].weight + 1){
							nodes[i+1][j].weight = nodes[i][j].weight + 1;
							modified = true;
						}
					}
					if(inRange(i-1, j, length) && board.isPathable(nodes[i-1][j].x, nodes[i-1][j].y)){
						if(nodes[i-1][j].weight > nodes[i][j].weight + 1){
							nodes[i-1][j].weight = nodes[i][j].weight + 1;
							modified = true;
						}
					}
					if(inRange(i, j+1, length) && board.isPathable(nodes[i][j+1].x, nodes[i][j+1].y)){
						if(nodes[i][j+1].weight > nodes[i][j].weight + 1){
							nodes[i][j+1].weight = nodes[i][j].weight + 1;
							modified = true;
						}
					}
					if(inRange(i, j-1, length) && board.isPathable(nodes[i][j-1].x, nodes[i][j-1].y)){
						if(nodes[i][j-1].weight > nodes[i][j].weight + 1){
							nodes[i][j-1].weight = nodes[i][j].weight + 1;
							modified = true;
						}
					}
				}
			}
			
		}while(modified);
		return nodes;
	}
}