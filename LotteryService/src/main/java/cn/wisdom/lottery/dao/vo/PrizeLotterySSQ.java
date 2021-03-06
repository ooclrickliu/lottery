package cn.wisdom.lottery.dao.vo;

import java.util.ArrayList;
import java.util.List;

import cn.wisdom.lottery.common.utils.CollectionUtils;
import cn.wisdom.lottery.common.utils.DataConvertUtils;
import cn.wisdom.lottery.common.utils.StringUtils;
import cn.wisdom.lottery.dao.annotation.Column;

public class PrizeLotterySSQ extends PrizeLottery {
	
	private List<Integer> red = new ArrayList<Integer>();

	private List<Integer> blue = new ArrayList<Integer>();
	
	@Column("number")
	private String number;
	
	//08,10,11,20,21,27+11
//	private static String LOTTERY_FORMAT_SSQ = "(0-33)\\+(0-16)+";
	
	public PrizeLotterySSQ()
	{
		
	}

	public PrizeLotterySSQ(int period, String number) {
		
		this.period = period;
		this.number = number;
		
		parser(number);
	}
	
	public static void main(String[] args) {
//		PrizeLotterySSQ ssq = new PrizeLotterySSQ(0, "06,10,11,12,20,25,32+12,13");
		PrizeLotterySSQ ssq = new PrizeLotterySSQ(0, "01,02,03,04,05,06,07,08,09,10,11,12,13,14,15,16,17,18,18,20,21,22,23,24,25,26,27,28,29,30,31,32+01,02,03,04,05,06,07,08,09,10,12,13,14,15,16");
		System.out.println(ssq.getRed());
		System.out.println(ssq.getBlue());
	}
	
	//"06,10,11,12,20,25+12"
	private void parser(String numbers) {
		// check format
		
		// parse
		String[] colors = numbers.split("\\+");
		if (colors != null) {
			if (colors.length > 0 && colors[0] != null) {
				String[] reds = colors[0].split(",");
				for (String r : reds) {
					this.red.add(DataConvertUtils.toInt(r));
				}
			}
			if (colors.length > 1 && colors[1] != null) {
				String[] blues = colors[1].split(",");
				for (String b : blues) {
					this.blue.add(DataConvertUtils.toInt(b));
				}
			}
		}
		
	}
	
	@Override
	public String getNumber() {
		if (StringUtils.isNotBlank(number)) {
			return number;
		}
		else {
			number = "";
			
			if (CollectionUtils.isNotEmpty(this.red)) {
				for (int r : this.red) {
					number += r + ",";
				}
				number = number.substring(0, number.length() - 1);
			}
			if (CollectionUtils.isNotEmpty(this.blue)) {
				number += "+";
				for (int b : this.blue) {
					number += b + ",";
				}
				number = number.substring(0, number.length() - 1);
			}
		}
		
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public List<Integer> getRed() {
		return red;
	}

	public List<Integer> getBlue() {
		return blue;
	}

	public void setRed(List<Integer> red) {
		this.red = red;
	}

	public void setBlue(List<Integer> blue) {
		this.blue = blue;
	}

}
