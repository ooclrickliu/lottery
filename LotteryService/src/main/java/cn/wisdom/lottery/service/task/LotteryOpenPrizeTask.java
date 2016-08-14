package cn.wisdom.lottery.service.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import cn.wisdom.lottery.common.log.Logger;
import cn.wisdom.lottery.common.log.LoggerFactory;
import cn.wisdom.lottery.common.utils.CollectionUtils;
import cn.wisdom.lottery.common.utils.DataConvertUtils;
import cn.wisdom.lottery.common.utils.StringUtils;
import cn.wisdom.lottery.dao.constant.LotteryType;
import cn.wisdom.lottery.dao.constant.PrizeState;
import cn.wisdom.lottery.dao.vo.Lottery;
import cn.wisdom.lottery.dao.vo.LotteryPeriod;
import cn.wisdom.lottery.dao.vo.PrizeLotterySSQ;
import cn.wisdom.lottery.service.LotteryServiceFacade;
import cn.wisdom.lottery.service.exception.ServiceException;
import cn.wisdom.lottery.service.remote.LotteryRemoteService;
import cn.wisdom.lottery.service.remote.response.LotteryOpenData;
import cn.wisdom.lottery.service.remote.response.LotteryOpenInfo;
import cn.wisdom.lottery.service.wx.MessageNotifier;

public class LotteryOpenPrizeTask {

	@Autowired
	private LotteryRemoteService lotteryRemoteService;
	
	@Autowired
	private LotteryServiceFacade lotteryServiceFacade;
	
	@Autowired
	private MessageNotifier messageNotifier;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LotteryOpenPrizeTask.class.getName());

	/**
	 * 每周二、四、日21:15开奖, 开奖日的19点停止销售, 次日开始销售下期
	 */
	@Scheduled(cron = "0 25,30,36,37,38,40,45 21 ? * SUN,TUE,THU")
	public void openSSQPrize() {
		try {
			LOGGER.info("SSQ task start...");
			
			// 1. 根据我们的定义，19点以后的当前时间已经是下一期了，所以要查上一期的开奖信息，如果为空，则需要更新，否则不用更新
			LotteryOpenData latestOpenInfo = lotteryServiceFacade.getLatestOpenInfo(LotteryType.SSQ);
			if (StringUtils.isNotBlank(latestOpenInfo.getOpencode())) {
				
				LOGGER.info(latestOpenInfo.getExpect() + " period has already opened: " + latestOpenInfo.getOpencode());
				LOGGER.info("SSQ task over.");
				return;
			}
			
			LOGGER.info("SSQ period {} is openning ...", latestOpenInfo.getExpect());

			// 2. get open info from web service
			LotteryOpenInfo lotteryOpenInfo = lotteryRemoteService
					.getLotteryOpenInfo();
			List<LotteryOpenData> data = lotteryOpenInfo.getData();
			if (CollectionUtils.isNotEmpty(data)) {
				LotteryOpenData lotteryOpenData = data.get(0);

				LOGGER.info("Prize number: {}", lotteryOpenData.getOpencode());
				
				if (!latestOpenInfo.getExpect().equals(lotteryOpenData.getExpect())) {
					return;
				}
				
				int period = DataConvertUtils.toInt(latestOpenInfo.getExpect());
				PrizeLotterySSQ ssq = new PrizeLotterySSQ(period , lotteryOpenData.getOpencode());
				
				// 3. 保存开奖结果
				lotteryServiceFacade.savePrizeLottery(ssq, LotteryType.SSQ);
				
				// 4. 更新所有当期已支付的彩票中奖信息
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

	private void updatePrizeInfo(PrizeLotterySSQ openInfo) throws ServiceException {
		LOGGER.info("Start to update lotteries' prize info...");
		
		// 1. get all "Paid" tickets of this period
		List<Lottery> paidLotteries = lotteryServiceFacade.getPaidLotteries(LotteryType.SSQ, openInfo.getPeriod());
		
		// 2. calculate prize info & bonus.
		if (CollectionUtils.isNotEmpty(paidLotteries)) {
			// 3. update all prize state to "Lose" first.
			lotteryServiceFacade.updatePrizeState(openInfo.getPeriod(), PrizeState.Lose);
			
			// 4. calculate and update prize info.
			List<Lottery> prizeLotteries = new ArrayList<Lottery>();
			List<LotteryPeriod> prizeLotteryPeriods = new ArrayList<LotteryPeriod>();
			for (Lottery lottery : paidLotteries) {
				Map<String, Map<String, Integer>> prizeInfo = lotteryServiceFacade.getPrizeInfo(lottery, openInfo);
				
				if (CollectionUtils.isNotEmpty(prizeInfo)) {
					LotteryPeriod period = new LotteryPeriod();
					period.setLotteryId(lottery.getId());
					period.setPeriod(openInfo.getPeriod());
					
					period.setPrizeInfoMap(prizeInfo);
					period.setPrizeBonus(lotteryServiceFacade.getPrizeBonus(prizeInfo));
					
					lottery.getPeriods().add(period);
					
					prizeLotteries.add(lottery);
					prizeLotteryPeriods.add(period);
					
					// redpack
					lotteryServiceFacade.calculateRedpacksPrize(lottery);
				}
			}
			
			if (CollectionUtils.isNotEmpty(prizeLotteryPeriods)) {
				lotteryServiceFacade.updatePrizeInfo(prizeLotteryPeriods);
			}
			
			// 5. notify owner & merchant
			notify(LotteryType.SSQ, openInfo, prizeLotteries);
		}
	}

	private void notify(LotteryType lotteryType, PrizeLotterySSQ openInfo, List<Lottery> prizeLotteries) {
		LOGGER.info("Start to notify prize info ...");
		
		messageNotifier.notifyMerchantPrizeInfo(lotteryType, openInfo, prizeLotteries);
		messageNotifier.notifyCustomerPrizeInfo(prizeLotteries);
		
		LOGGER.info("Complete notifying prize info.");
	}
}
