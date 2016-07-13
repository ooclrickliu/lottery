package cn.wisdom.lottery.payment.service;

import java.util.List;
import java.util.Map;

import cn.wisdom.lottery.payment.dao.constant.LotteryType;
import cn.wisdom.lottery.payment.dao.vo.Lottery;
import cn.wisdom.lottery.payment.dao.vo.PrizeLottery;
import cn.wisdom.lottery.payment.dao.vo.PrizeLotterySSQ;
import cn.wisdom.lottery.payment.service.exception.ServiceException;
import cn.wisdom.lottery.payment.service.remote.response.LotteryOpenData;

public interface LotteryPrizeService {

	/**
	 * Get current Qi by lottery type.
	 * 
	 * @return
	 * @throws ServiceException
	 */
	LotteryOpenData getCurrentPeriod(LotteryType lotteryType) throws ServiceException;

	/**
	 * Get next N periods by lottery type, including current period.
	 * 
	 * @param lotteryType
	 * @param n
	 * @return
	 */
	List<Integer> getNextNPeriods(LotteryType lotteryType, int n);

	/**
	 * Get the latest open info.
	 * 
	 * @param lotteryType
	 * @return
	 */
	LotteryOpenData getLatestOpenInfo(LotteryType lotteryType);

	/**
	 * Get open info of specified period.
	 * 
	 * @param lotteryType
	 * @param perid
	 * @return
	 */
	LotteryOpenData getOpenInfo(LotteryType lotteryType, int period);
	
	/**
	 * Get prize no of lottery by type and time.
	 * 
	 * @param lotteryType
	 * @param time
	 * @return
	 * @throws ServiceException
	 */
	String getPrizeNo(LotteryType lotteryType, int time) throws ServiceException;

	/**
	 * Save the prize lottery info.
	 * 
	 * @param prizeLotterySSQ
	 */
	void savePrizeLottery(PrizeLottery prizeLottery, LotteryType lotteryType);

	/**
	 * Calculate lottery prize range according open prize info.
	 * 
	 * @param lottery
	 * @param ssq
	 * @return Key: numberId, Value: (key: prizeRange, value: hitNumber)
	 */
	Map<Long, Map<Integer, Integer>> getPrizeInfo(Lottery lottery, PrizeLotterySSQ ssq);
	
	/**
	 * Calculate prize bonus according prize info.
	 * @param prizeInfo
	 * @return
	 */
	int getPrizeBonus(Map<Long, Map<Integer, Integer>> prizeInfo);
}
