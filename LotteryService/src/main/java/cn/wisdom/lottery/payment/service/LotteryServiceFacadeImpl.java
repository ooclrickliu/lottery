package cn.wisdom.lottery.payment.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.wisdom.lottery.payment.dao.constant.LotteryType;
import cn.wisdom.lottery.payment.dao.vo.Lottery;
import cn.wisdom.lottery.payment.service.context.SessionContext;
import cn.wisdom.lottery.payment.service.exception.ServiceException;
import cn.wisdom.lottery.payment.service.remote.response.LotteryOpenData;

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
	public Lottery getLottery(String orderNo) throws ServiceException {
		
		return lotteryService.getLottery(orderNo);
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

}
