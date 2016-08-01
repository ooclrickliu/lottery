package cn.wisdom.lottery.service.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import cn.wisdom.lottery.common.exception.OVTException;
import cn.wisdom.lottery.common.log.Logger;
import cn.wisdom.lottery.common.log.LoggerFactory;
import cn.wisdom.lottery.common.utils.CollectionUtils;
import cn.wisdom.lottery.common.utils.DataConvertUtils;
import cn.wisdom.lottery.common.utils.JsonUtils;
import cn.wisdom.lottery.dao.constant.LotteryType;
import cn.wisdom.lottery.dao.vo.Lottery;
import cn.wisdom.lottery.dao.vo.PrizeLotterySSQ;
import cn.wisdom.lottery.service.LotteryServiceFacade;
import cn.wisdom.lottery.service.exception.ServiceException;
import cn.wisdom.lottery.service.remote.LotteryRemoteService;
import cn.wisdom.lottery.service.remote.response.LotteryOpenData;
import cn.wisdom.lottery.service.remote.response.LotteryOpenInfo;

public class LotteryOpenPrizeTask {

	@Autowired
	private LotteryRemoteService lotteryRemoteService;
	
	@Autowired
	private LotteryServiceFacade lotteryServiceFacade;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LotteryOpenPrizeTask.class.getName());

	/**
	 * 每周二、四、日21:15开奖, 开奖日的19点停止销售, 次日开始销售下期
	 */
	@Scheduled(cron = "0 20,22,25,30,36 21 ? * SUN,TUE,THU")
	public void openSSQPrize() {
		try {
			LOGGER.info("SSQ task start...");
			
			// 1. get current period
			LotteryOpenData currentPeriod = lotteryServiceFacade.getCurrentPeriod(LotteryType.SSQ);

			LOGGER.info("Current period: {}", currentPeriod);

			// 2. get open info from web service
			LotteryOpenInfo lotteryOpenInfo = lotteryRemoteService
					.getLotteryOpenInfo();
			List<LotteryOpenData> data = lotteryOpenInfo.getData();
			if (CollectionUtils.isNotEmpty(data)) {
				LotteryOpenData lotteryOpenData = data.get(0);

				LOGGER.info("Prize code: {}", lotteryOpenData.getOpencode());
				
				int period = DataConvertUtils.toInt(lotteryOpenData.getExpect());
				if (currentPeriod.getExpect().equals("" + period)) {
					return;
				}
				
				PrizeLotterySSQ ssq = new PrizeLotterySSQ(period, lotteryOpenData.getOpencode());
				
				lotteryServiceFacade.savePrizeLottery(ssq, LotteryType.SSQ);
				
				// 3. update prize info.
				updatePrizeInfo(ssq);
			}

			LOGGER.info("SSQ task over.");
		} catch (ServiceException e) {
			e.printStackTrace();
		}

	}
	
	public void reopenSSQPrize(int period) throws ServiceException {
		// 1. get openInfo
		LotteryOpenData openInfo = lotteryServiceFacade.getOpenInfo(LotteryType.SSQ, period);
		
		// 2. update prize info.
		PrizeLotterySSQ ssq = new PrizeLotterySSQ(period, openInfo.getOpencode());
		updatePrizeInfo(ssq);
	}

	private void updatePrizeInfo(PrizeLotterySSQ ssq) throws ServiceException {
		LOGGER.info("Start to update lotteries' prize info...");
		
		// 1. get all "Printed" tickets of this period
		List<Lottery> printedLotteries = lotteryServiceFacade.getPrintedLotteries(LotteryType.SSQ, ssq.getPeriod());
		
		// 2. calculate prize info & bonus.
		if (CollectionUtils.isNotEmpty(printedLotteries)) {
			List<Lottery> prizeLotteries = new ArrayList<Lottery>();
			for (Lottery lottery : printedLotteries) {
				Map<Long, Map<Integer, Integer>> prizeInfo = lotteryServiceFacade.getPrizeInfo(lottery, ssq);
				
				if (CollectionUtils.isNotEmpty(prizeInfo)) {
					try {
						lottery.setPrizeInfo(JsonUtils.toJson(prizeInfo));
					} catch (OVTException e) {
					}
					lottery.setPrizeBonus(lotteryServiceFacade.getPrizeBonus(prizeInfo));
					
					prizeLotteries.add(lottery);
				}
			}
			lotteryServiceFacade.updatePrizeInfo(prizeLotteries);
			
			// 3. notify owner
			notifyOwner(prizeLotteries);
		}
	}

	private void notifyOwner(List<Lottery> prizeLotteries) {
		if (CollectionUtils.isNotEmpty(prizeLotteries)) {
			LOGGER.info("Start to notify prize owner...");
			
			// TODO call WX message api.
			
			LOGGER.info("Complete notifying prize owner.");
		}
	}
}
