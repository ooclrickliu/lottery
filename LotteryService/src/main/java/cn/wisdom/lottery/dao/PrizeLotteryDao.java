package cn.wisdom.lottery.dao;

import java.util.List;

import cn.wisdom.lottery.dao.constant.LotteryType;
import cn.wisdom.lottery.dao.vo.PrizeLottery;
import cn.wisdom.lottery.dao.vo.PrizeLotterySSQ;
import cn.wisdom.lottery.service.remote.response.LotteryOpenData;

public interface PrizeLotteryDao {

	PrizeLottery getPrizeLottery(LotteryType lotteryType, int period);
	
	void saveOpenNumbers(PrizeLottery lottery, LotteryType lotteryType);

	LotteryOpenData getCurrentPeriod(LotteryType lotteryType);

	LotteryOpenData getLastestOpenInfo(LotteryType lotteryType);

	LotteryOpenData getOpenInfo(LotteryType lotteryType, int period);

	List<PrizeLotterySSQ> getNextNPeriods(LotteryType lotteryType, int n);
}
