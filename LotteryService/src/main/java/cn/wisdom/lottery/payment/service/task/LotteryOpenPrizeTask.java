package cn.wisdom.lottery.payment.service.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import cn.wisdom.lottery.payment.common.exception.OVTException;
import cn.wisdom.lottery.payment.common.log.Logger;
import cn.wisdom.lottery.payment.common.log.LoggerFactory;
import cn.wisdom.lottery.payment.common.utils.CollectionUtils;
import cn.wisdom.lottery.payment.common.utils.DataConvertUtils;
import cn.wisdom.lottery.payment.common.utils.JsonUtils;
import cn.wisdom.lottery.payment.dao.constant.LotteryType;
import cn.wisdom.lottery.payment.dao.vo.Lottery;
import cn.wisdom.lottery.payment.dao.vo.PrizeLotterySSQ;
import cn.wisdom.lottery.payment.service.LotteryPrizeService;
import cn.wisdom.lottery.payment.service.LotteryService;
import cn.wisdom.lottery.payment.service.exception.ServiceException;
import cn.wisdom.lottery.payment.service.remote.LotteryRemoteService;
import cn.wisdom.lottery.payment.service.remote.response.LotteryOpenData;
import cn.wisdom.lottery.payment.service.remote.response.LotteryOpenInfo;

public class LotteryOpenPrizeTask {

	@Autowired
	private LotteryRemoteService lotteryRemoteService;

	@Autowired
	private LotteryPrizeService lotteryPrizeService;
	
	@Autowired
	private LotteryService lotteryService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LotteryOpenPrizeTask.class.getName());

	/**
	 * 每周二、四、日21:15开奖, 开奖日的19点停止销售, 次日开始销售下期
	 */
	@Scheduled(cron = "0 20,22,25,30,36 21 ? * SUN,TUE,THU")
	public void openSSQPrize() {
		try {
			LOGGER.info("SSQ task start...");
			
			// 1. get current period
			LotteryOpenData currentPeriod = lotteryPrizeService.getCurrentPeriod(LotteryType.SSQ);

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
				
				lotteryPrizeService.savePrizeLottery(ssq, LotteryType.SSQ);
				
				// 3. update prize info.
				updatePrizeInfo(ssq);
			}

			LOGGER.info("SSQ task over.");
		} catch (ServiceException e) {
			e.printStackTrace();
		}

	}

	private void updatePrizeInfo(PrizeLotterySSQ ssq) throws ServiceException {
		LOGGER.info("Start to update lotteries' prize info...");
		
		// 1. get all "Printed" tickets of this period
		List<Lottery> printedLotteries = lotteryService.getPrintedLotteries(LotteryType.SSQ, ssq.getPeriod());
		
		// 2. calculate prize info & bonus.
		if (CollectionUtils.isNotEmpty(printedLotteries)) {
			List<Lottery> prizeLotteries = new ArrayList<Lottery>();
			for (Lottery lottery : printedLotteries) {
				Map<Long, Map<Integer, Integer>> prizeInfo = lotteryPrizeService.getPrizeInfo(lottery, ssq);
				
				if (CollectionUtils.isNotEmpty(prizeInfo)) {
					try {
						lottery.setPrizeInfo(JsonUtils.toJson(prizeInfo));
					} catch (OVTException e) {
					}
					lottery.setPrizeBonus(lotteryPrizeService.getPrizeBonus(prizeInfo));
					
					prizeLotteries.add(lottery);
				}
			}
			lotteryService.updatePrizeInfo(prizeLotteries);
			
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
