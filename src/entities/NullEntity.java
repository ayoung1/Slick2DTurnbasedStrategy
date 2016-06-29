package entities;

public class NullEntity implements Entity {
	
	private int x, y;
	
	public NullEntity(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	@Override
	public boolean isPathable(){return true;}

	@Override
	public int[] getCoords() {
		return new int[] {this.x, this.y};
	}
}
