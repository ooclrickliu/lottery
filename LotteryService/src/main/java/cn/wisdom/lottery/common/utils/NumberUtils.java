package cn.wisdom.lottery.common.utils;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class NumberUtils {

	private static final DecimalFormat df = new DecimalFormat("0.00");
	static {
		df.setRoundingMode(RoundingMode.HALF_UP);
	}
	
	public static float formatFloat(float number)
	{
		return DataConvertUtils.toFloat(df.format(number));
	}
	
	public static void main(String[] args) {
		System.out.println(NumberUtils.formatFloat(0.0151f));
	}
}
