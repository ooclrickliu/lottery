package cn.wisdom.lottery.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.wisdom.lottery.dao.constant.LotteryType;
import cn.wisdom.lottery.dao.vo.Lottery;
import cn.wisdom.lottery.dao.vo.PrizeLotterySSQ;
import cn.wisdom.lottery.service.context.SessionContext;
import cn.wisdom.lottery.service.exception.ServiceException;
import cn.wisdom.lottery.service.remote.response.LotteryOpenData;

@Service
public class LotteryServiceFacadeImpl implements LotteryServiceFacade {

	@Autowired
	private LotteryService lotteryService;
	
	@Autowired
	private LotteryPrizeService lotteryPrizeService;
	
	@Override
	public LotteryOpenData getCurrentPeriod(LotteryType lotteryType) throws ServiceException {
		return lotteryPrizeService.getCurrentPeriod(lotteryType);
	}

	@Override
	public List<Integer> getNextNPeriods(LotteryType lotteryType, int n)
			throws ServiceException {
		return lotteryPrizeService.getNextNPeriods(lotteryType, n);
	}

	@Override
	public Lottery createPrivateOrder(LotteryType ssq, Lottery lottery) throws ServiceException {
		return lotteryService.createPrivateOrder(lottery);
	}

	@Override
	public LotteryOpenData getLatestOpenInfo(LotteryType lotteryType)
			throws ServiceException {
		
		return lotteryPrizeService.getLatestOpenInfo(lotteryType);
	}

	@Override
	public LotteryOpenData getOpenInfo(LotteryType lotteryType, int period)
			throws ServiceException {
		
		return lotteryPrizeService.getOpenInfo(lotteryType, period);
	}

	@Override
	public Lottery getLottery(String orderNo) throws ServiceException {
		
		return lotteryService.getLottery(orderNo);
	}

	@Override
	public List<Lottery> getMyLottery(String openid,
			LotteryType lotteryType, int limit) throws ServiceException {
		
		//TODO
//		int period = Integer.parseInt(currentPeriod.getExpect());
//		
//		return lotteryService.getMyLottery(openid, lotteryType, period, limit);
		
		return null;
	}

	@Override
	public void onPaidSuccess(String userId, String orderNo)
			throws ServiceException {
		lotteryService.onPaidSuccess(userId, orderNo);
	}

	@Override
	public void fetchTicket(String orderNo) throws ServiceException {
		long userId = SessionContext.getCurrentUser().getId();
		lotteryService.fetchTicket(orderNo, userId);
	}

	////////////////////////////////////
	
	@Override
	public void printTickets(List<String> orderNos, long merchantId) throws ServiceException {
		lotteryService.printTickets(orderNos, merchantId);
	}

	@Override
	public List<Lottery> queryLottery(LotteryType lotteryType, int period, long merchantId)
			throws ServiceException {
		return lotteryService.queryLottery(lotteryType, period, merchantId);
	}

	@Override
	public void savePrizeLottery(PrizeLotterySSQ prizeLotterySSQ,
			LotteryType lotteryType) {
		
		lotteryPrizeService.savePrizeLottery(prizeLotterySSQ, lotteryType);
	}

	@Override
	public List<Lottery> getPrintedLotteries(LotteryType lotteryType, int period)
			throws ServiceException {
		return lotteryService.getPrintedLotteries(lotteryType, period);
	}

	@Override
	public Map<Long, Map<Integer, Integer>> getPrizeInfo(Lottery lottery,
			PrizeLotterySSQ prizeLotterySSQ) throws ServiceException {
		return lotteryPrizeService.getPrizeInfo(lottery, prizeLotterySSQ);
	}

	@Override
	public int getPrizeBonus(Map<Long, Map<Integer, Integer>> prizeInfo) {
		return lotteryPrizeService.getPrizeBonus(prizeInfo);
	}

	@Override
	public void updatePrizeInfo(List<Lottery> prizeLotteries) {
		lotteryService.updatePrizeInfo(prizeLotteries);
	}

}