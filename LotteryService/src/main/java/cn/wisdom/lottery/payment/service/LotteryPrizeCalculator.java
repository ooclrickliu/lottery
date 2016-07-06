package cn.wisdom.lottery.payment.service;

import java.util.Map;

/**
 * 彩票中奖结果计算器
 * 
 * @author zhi.liu
 *
 */
public interface LotteryPrizeCalculator {

	/**
	 * 计算中奖结果, 格式如下：
	 * {
	 *   1 : 1,   //一等奖 1注 
	 *   2 : 4,   //二等奖 4注 
	 *   3 : 10   //三等奖 10注 
	 * }
	 * 
	 * @param rTotal
	 * @param bTotal
	 * @param rHits
	 * @param bHits
	 * @return
	 */
	Map<Integer, Integer> getPrizeInfo(int rTotal, int bTotal, int rHits, int bHits);
}
