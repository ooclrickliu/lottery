package cn.wisdom.lottery.payment.common.utils;

public class MathUtils {

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
}
