package cn.wisdom.lottery.payment.dao;

import java.util.List;

import cn.wisdom.lottery.payment.dao.constant.LotteryType;
import cn.wisdom.lottery.payment.dao.vo.PrizeLottery;
import cn.wisdom.lottery.payment.service.remote.response.LotteryOpenData;

public interface PrizeLotteryDao {

	PrizeLottery getPrizeLottery(LotteryType lotteryType, int period);
	
	void saveOpenNumbers(PrizeLottery lottery, LotteryType lotteryType);

	LotteryOpenData getCurrentPeriod(LotteryType lotteryType);

	LotteryOpenData getLastestOpenInfo(LotteryType lotteryType);

	List<Integer> getNextNPeriods(LotteryType lotteryType, int n);
}
