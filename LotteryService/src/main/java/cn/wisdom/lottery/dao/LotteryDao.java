package cn.wisdom.lottery.dao;

import java.util.List;

import cn.wisdom.lottery.dao.constant.LotteryType;
import cn.wisdom.lottery.dao.constant.PrizeState;
import cn.wisdom.lottery.dao.vo.Lottery;
import cn.wisdom.lottery.dao.vo.LotteryPeriod;

public interface LotteryDao {
	
	void saveLottery(Lottery lottery);

	Lottery getLottery(long lotteryId);
	
	Lottery getLottery(long lotteryId, boolean queryNumber, boolean queryPeriod);
	
	List<Lottery> getLottery(List<Long> lotteryIds, boolean queryNumber, boolean queryPeriod);
	
	List<Lottery> getLottery(LotteryType lotteryType, int period, long merchantId);

	Lottery getLotteryByOrder(String orderNo);

	Lottery getLotteryByPeriod(long periodId);

	Lottery getLatestLottery(long userId);

	List<Lottery> getLotteries(long owner);

	void updatePayState(Lottery lottery);

	void updatePrintState(long periodId);

	void updateDistributeState(Lottery lottery);

	void updateFetchState(LotteryPeriod lotteryPeriod);

	List<Lottery> getPaidLotteries(LotteryType lotteryType, int period);

	void updatePrizeInfo(List<LotteryPeriod> prizeLotteries);

	void updatePrizeState(int period, PrizeState prizeState);

	
}
