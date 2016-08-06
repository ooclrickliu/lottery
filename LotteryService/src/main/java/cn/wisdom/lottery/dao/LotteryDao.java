package cn.wisdom.lottery.dao;

import java.util.List;

import cn.wisdom.lottery.dao.constant.LotteryType;
import cn.wisdom.lottery.dao.vo.Lottery;

public interface LotteryDao {
	
	void saveLottery(Lottery lottery);

	Lottery getLottery(long lotteryId);
	
	Lottery getLottery(long lotteryId, boolean queryNumber, boolean queryPeriod);
	
	List<Lottery> getLottery(List<Long> lotteryIds, boolean queryNumber, boolean queryPeriod);
	
	List<Lottery> getLottery(LotteryType lotteryType, int period, long merchantId);

	Lottery getLotteryByOrder(String orderNo);

	Lottery getLatestLottery(long userId);

	List<Lottery> getLotteries(long owner);

	void updateTicketState(Lottery lottery);

	void updatePrintState(List<Lottery> lotteries);

	void updateDistributeState(Lottery lottery);

	void updateFetchState(Lottery lottery);

	List<Lottery> getPrintedLotteries(LotteryType lotteryType, int period);

	void updatePrizeInfo(List<Lottery> prizeLotteries);

	
}
