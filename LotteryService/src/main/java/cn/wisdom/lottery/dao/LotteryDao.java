package cn.wisdom.lottery.dao;

import java.util.List;

import cn.wisdom.lottery.dao.constant.LotteryType;
import cn.wisdom.lottery.dao.constant.PrizeState;
import cn.wisdom.lottery.dao.vo.Lottery;
import cn.wisdom.lottery.dao.vo.LotteryPeriod;
import cn.wisdom.lottery.dao.vo.LotteryRedpack;
import cn.wisdom.lottery.dao.vo.PageInfo;

public interface LotteryDao {
	
	void saveLottery(Lottery lottery);

	/**
	 * Get lottery with all detail info [number, period, redpack].
	 * 
	 * @param lotteryId
	 * @return
	 */
	Lottery getLottery(long lotteryId);
	
	Lottery getLottery(long lotteryId, boolean queryNumber, boolean queryPeriod, boolean queryRedpack);
	
	List<Lottery> getLottery(List<Long> lotteryIds, boolean queryNumber, boolean queryPeriod);
	
	List<Lottery> getLottery(LotteryType lotteryType, int period, long merchantId);

	Lottery getLotteryByOrder(String orderNo);

	Lottery getLotteryByPeriod(long periodId);

	Lottery getLatestLottery(long userId);

	List<Lottery> getLotteries(long owner, PageInfo pageInfo);

	List<Lottery> getUnPaidLotteries(long owner);

	void updatePayState(Lottery lottery);

	void updatePrintState(long periodId);

	void updateDistributeState(Lottery lottery);

	void updateFetchState(LotteryPeriod lotteryPeriod);

	List<Lottery> getPaidLotteries(LotteryType lotteryType, int period);

	void updatePrizeInfo(List<LotteryPeriod> prizeLotteries, List<LotteryRedpack> prizeLotteryRedpacks);

	void updatePrizeState(int period, PrizeState prizeState);

	void saveRedpack(LotteryRedpack redpack);

	void updatePayImg(Lottery lottery);

	void updateTicketImage(long periodId, String name);

	void deleteUnPaidLottery();

	void deleteLottery(long owner, long lotteryId);

	void updateAsRedpack(Lottery lottery);

	int increaseSnatchNum(long lotteryId);

	List<Lottery> getRedpacksBySender(long sender);

	List<Lottery> getRedpacksByReceiver(long receiver);

	List<Lottery> getValidRedpackLotteries(long userId, int period);
	
}
