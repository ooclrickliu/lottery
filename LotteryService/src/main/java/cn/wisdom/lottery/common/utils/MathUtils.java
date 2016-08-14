package cn.wisdom.lottery.common.utils;

import java.util.Random;

public class MathUtils {
	
	private static Random random = new Random();

	public static int factor(int n) {
		int factor = 1;
		
		while (n > 1) {
			factor *= n;
			n--;
		}
		
		return factor;
	}
	
	/**
	 * Cn,m
	 * 
	 * @param n
	 * @param m
	 * @return
	 */
	public static int Cn_m(int n, int m) {
		int result = 0;
		
		if (n > 0 && m <= n) {
			result = factor(n) / factor(m) / factor(n - m);
		}
		
		return result;
	}
	
	public static void main(String[] args)
    {
        System.out.println(System.currentTimeMillis());
        Cn_m(33, 16);
        System.out.println(System.currentTimeMillis());
    }

	public static int rand(int max) {
		return random.nextInt(max);
	}
}
