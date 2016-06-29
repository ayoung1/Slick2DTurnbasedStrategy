package tools;

import java.util.Random;

public class Variance {
	private static Random random = new Random(System.currentTimeMillis());
	
	public static boolean hitByPercent(int percent){
		return hitByPercent(((double)percent) / 100.0);
	}
	
	public static boolean hitByPercent(double percent){
		return percent >= random.nextDouble();
	}
}
