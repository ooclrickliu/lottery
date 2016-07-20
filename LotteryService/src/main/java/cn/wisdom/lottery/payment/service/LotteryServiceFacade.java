package cn.wisdom.lottery.payment.service;

import java.util.List;
import java.util.Map;

import cn.wisdom.lottery.payment.dao.constant.LotteryType;
import cn.wisdom.lottery.payment.dao.vo.Lottery;
import cn.wisdom.lottery.payment.dao.vo.PrizeLotterySSQ;
import cn.wisdom.lottery.payment.service.exception.ServiceException;
import cn.wisdom.lottery.payment.service.remote.response.LotteryOpenData;

public interface LotteryServiceFacade {

	///////////Customer///////////
	
	LotteryOpenData getCurrentPeriod(LotteryType lotteryType) throws ServiceException;
	
	List<Integer> getNextNPeriods(LotteryType lotteryType, int n) throws ServiceException;

	Lottery createPrivateOrder(LotteryType ssq, Lottery lottery) throws ServiceException;

	LotteryOpenData getLatestOpenInfo(LotteryType lotteryType) throws ServiceException;
	
	LotteryOpenData getOpenInfo(LotteryType lotteryType, int period) throws ServiceException;

	void onPaidSuccess(String userId, String orderNo) throws ServiceException;
	
	Lottery getLottery(String orderNo) throws ServiceException;

	void fetchTicket(String orderNo) throws ServiceException;

	List<Lottery> getMyLottery(String openid, LotteryType lotteryType, int limit) throws ServiceException;

	///////////Merchant///////////
	
	void printTickets(List<String> orderNos, long merchantId) throws ServiceException;

	List<Lottery> queryLottery(LotteryType lotteryType, int period, long merchantId) throws ServiceException;

	///////////Other////////////////
	void savePrizeLottery(PrizeLotterySSQ prizeLotterySSQ, LotteryType lotteryType) throws ServiceException;

	List<Lottery> getPrintedLotteries(LotteryType lotteryType, int period) throws ServiceException;

	Map<Long, Map<Integer, Integer>> getPrizeInfo(Lottery lottery,
			PrizeLotterySSQ prizeLotterySSQ) throws ServiceException;

	int getPrizeBonus(Map<Long, Map<Integer, Integer>> prizeInfo);

	void updatePrizeInfo(List<Lottery> prizeLotteries);
}
