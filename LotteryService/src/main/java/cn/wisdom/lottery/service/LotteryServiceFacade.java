package cn.wisdom.lottery.service;

import java.util.List;
import java.util.Map;

import cn.wisdom.lottery.dao.constant.LotteryType;
import cn.wisdom.lottery.dao.constant.PrizeState;
import cn.wisdom.lottery.dao.vo.Lottery;
import cn.wisdom.lottery.dao.vo.LotteryPeriod;
import cn.wisdom.lottery.dao.vo.PageInfo;
import cn.wisdom.lottery.dao.vo.PrizeLotterySSQ;
import cn.wisdom.lottery.service.exception.ServiceException;
import cn.wisdom.lottery.service.remote.response.LotteryOpenData;

public interface LotteryServiceFacade {

	// /////////Customer///////////

	LotteryOpenData getCurrentPeriod(LotteryType lotteryType)
			throws ServiceException;

	List<PrizeLotterySSQ> getNextNPeriods(LotteryType lotteryType, int n)
			throws ServiceException;

	Lottery createLottery(LotteryType ssq, Lottery lottery)
			throws ServiceException;

	LotteryOpenData getLatestOpenInfo(LotteryType lotteryType)
			throws ServiceException;

	LotteryOpenData getOpenInfo(LotteryType lotteryType, int period)
			throws ServiceException;

	void onPaidSuccess(String orderNo, String openid) throws ServiceException;

	Lottery getLottery(long lotteryId) throws ServiceException;

	Lottery getLottery(long lotteryId, boolean queryNumber,
			boolean queryPeriod, boolean queryRedpack) throws ServiceException;

	void fetchTicket(long periodId) throws ServiceException;

	Lottery getMyLatestLottery(String openid) throws ServiceException;

	List<Lottery> getLotteries(long owner, PageInfo pageInfo)
			throws ServiceException;

	/**
	 * 1. 不能超过有效期，有效期为本期投注截止时间 2. 不能超过红包个数
	 * 
	 * @param lotteryId
	 * @return
	 * @throws ServiceException
	 */
	int snatchRedpack(long lotteryId) throws ServiceException;

	String submitPayRequest(long lotteryId, String payImgUrl);

	List<Lottery> getUnPaidLotteries(long owner);

	void deleteLottery(long owner, long lotteryId);

	void shareLotteryAsRedpack(long lotteryId, int count) throws ServiceException;

	List<Lottery> getSentRedpackList();

	List<Lottery> getReceivedRedpackList();

	// /////////Merchant///////////

	void confirmPay(long lotteryId);

	void confirmPayFail(long lotteryId);

	void printTicket(long periodId) throws ServiceException;

	List<Lottery> queryLottery(LotteryType lotteryType, int period,
			long merchantId) throws ServiceException;

	Lottery getLotteryByPeriod(long periodId);

	String uploadTicket(long periodId, String ticketImgUrl);

	// /////////Other////////////////
	void savePrizeLottery(PrizeLotterySSQ prizeLotterySSQ,
			LotteryType lotteryType) throws ServiceException;

	List<Lottery> getPaidLotteries(LotteryType lotteryType, int period)
			throws ServiceException;

	Map<String, Map<String, Integer>> getPrizeInfo(Lottery lottery,
			PrizeLotterySSQ prizeLotterySSQ) throws ServiceException;

	int getPrizeBonus(Map<String, Map<String, Integer>> prizeInfo);

	void updatePrizeInfo(List<LotteryPeriod> prizeLotteries);

	// /////////WxPay////////////////
	Map<String, String> unifiedOrder(Lottery lottery, String openId,
			String spbillCreateIp, String tradeType, String body)
			throws ServiceException;

	// /////////Task////////////////
	void updatePrizeState(int period, PrizeState prizeState);

	void calculateRedpacksPrize(Lottery lottery);

	void clearUnpaidLottery();
}
