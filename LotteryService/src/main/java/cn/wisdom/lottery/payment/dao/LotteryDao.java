package cn.wisdom.lottery.payment.dao;

import java.util.List;

import cn.wisdom.lottery.payment.dao.constant.LotteryType;
import cn.wisdom.lottery.payment.dao.vo.Lottery;

public interface LotteryDao {
	
	void saveLottery(Lottery lottery);

	Lottery getLottery(String orderNo);
	
	Lottery getLottery(String orderNo, boolean queryNumber, boolean queryPeriod);
	
	List<Lottery> getLottery(List<String> orderNos, boolean queryNumber, boolean queryPeriod);
	
	List<Lottery> getLottery(LotteryType lotteryType, int period, long merchantId);

	List<Lottery> getLottery(String openid, LotteryType lotteryType, int period, int limit);

	void updateTicketState(Lottery lottery);

	void updatePrintState(List<Lottery> lotteries);

	void updateDistributeState(Lottery lottery);

	void updateFetchState(Lottery lottery);

	List<Lottery> getPrintedLotteries(LotteryType lotteryType, int period);

	void updatePrizeInfo(List<Lottery> prizeLotteries);

	
}
