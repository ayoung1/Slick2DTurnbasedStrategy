package trig;

public class Trig{
	
	public static double manhattanDistanceBetweenPoints(int x1, int y1, int x2, int y2){
		return Math.abs(x2-x1) + Math.abs(y2-y1);
	}
	
	public static double euclideanDistanceBetweenPoints(int x1, int y1, int x2, int y2){
		double term1 = Math.pow(x2-x1, 2);
		double term2 = Math.pow(y2-y1, 2);
		
		return Math.sqrt(term1 + term2);
	}
	
}