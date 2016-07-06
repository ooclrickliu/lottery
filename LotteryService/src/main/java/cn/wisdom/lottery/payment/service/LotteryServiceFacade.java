package cn.wisdom.lottery.payment.service;

import java.util.List;

import cn.wisdom.lottery.payment.dao.constant.LotteryType;
import cn.wisdom.lottery.payment.dao.vo.Lottery;
import cn.wisdom.lottery.payment.service.exception.ServiceException;
import cn.wisdom.lottery.payment.service.remote.response.LotteryOpenData;

public interface LotteryServiceFacade {

	///////////Customer///////////
	
	LotteryOpenData getCurrentPeriod(LotteryType lotteryType) throws ServiceException;
	
	List<Integer> getNextNPeriods(LotteryType lotteryType, int n) throws ServiceException;

	Lottery createPrivateOrder(LotteryType ssq, Lottery lottery) throws ServiceException;

	LotteryOpenData getLatestOpenInfo(LotteryType lotteryType) throws ServiceException;

	void onPaidSuccess(String userId, String orderNo) throws ServiceException;
	
	Lottery getLottery(String orderNo) throws ServiceException;

	void fetchTicket(String orderNo) throws ServiceException;

	///////////Merchant///////////
	
	void printTickets(List<String> orderNos, long merchantId) throws ServiceException;

	List<Lottery> queryLottery(LotteryType lotteryType, int period, long merchantId) throws ServiceException;
}
