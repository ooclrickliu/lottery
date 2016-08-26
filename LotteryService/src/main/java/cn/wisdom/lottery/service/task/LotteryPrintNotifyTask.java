package cn.wisdom.lottery.service.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import cn.wisdom.lottery.api.response.QueryLotteryResponse;
import cn.wisdom.lottery.common.log.Logger;
import cn.wisdom.lottery.common.log.LoggerFactory;
import cn.wisdom.lottery.common.utils.CollectionUtils;
import cn.wisdom.lottery.common.utils.DataConvertUtils;
import cn.wisdom.lottery.common.utils.NumberUtils;
import cn.wisdom.lottery.dao.constant.LotteryType;
import cn.wisdom.lottery.dao.vo.Lottery;
import cn.wisdom.lottery.service.LotteryServiceFacade;
import cn.wisdom.lottery.service.exception.ServiceException;
import cn.wisdom.lottery.service.remote.response.LotteryOpenData;
import cn.wisdom.lottery.service.wx.MessageNotifier;

public class LotteryPrintNotifyTask {
	
	@Autowired
	private LotteryServiceFacade lotteryServiceFacade;
	
	@Autowired
	private MessageNotifier messageNotifier;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LotteryPrintNotifyTask.class.getName());

	@Scheduled(cron = "0 1 19 ? * SUN,TUE,THU")
	public void notifyPrint()
	{
		try {
			LOGGER.info("Notify merchant print tickets task start...");
			
			// 1. get period
			LotteryOpenData latestOpenInfo = lotteryServiceFacade.getLatestOpenInfo(LotteryType.SSQ);
			int period = DataConvertUtils.toInt(latestOpenInfo.getExpect());
			
			LOGGER.info("Period - {}", latestOpenInfo.getExpect());
			
			// 2. get paid lotteries of period & group by merchant
			List<Lottery> paidLotteries = lotteryServiceFacade.getPaidLotteries(LotteryType.SSQ, period);
			if (CollectionUtils.isNotEmpty(paidLotteries)) {
				Map<Long, List<Lottery>> merchantLotteryMap = new HashMap<Long, List<Lottery>>();
				for (Lottery lottery : paidLotteries) {
					List<Lottery> list = merchantLotteryMap.get(lottery.getMerchant());
					if (list == null) {
						list = new ArrayList<Lottery>();
						merchantLotteryMap.put(lottery.getMerchant(), list);
					}
					list.add(lottery);
				}
				
				// 3. send notification to merchant one by one
				for (long merchant : merchantLotteryMap.keySet()) {
					LOGGER.info("Notify merchant({}) to print tickets ...", merchant);
					QueryLotteryResponse response = new QueryLotteryResponse();
					response.setOpenInfo(latestOpenInfo);
					response.setLotteries(merchantLotteryMap.get(merchant));
					
					// 4. sum fee
					float totalFee = 0;
					for (Lottery lottery : response.getLotteries()) {
						totalFee += lottery.getTotalFee();
					}
					response.setTotalFee(NumberUtils.formatFloat(totalFee));
					
					messageNotifier.notifyMerchantPrintTickets(merchant, response);
				}
			}
			
			LOGGER.info("Notify merchant print tickets task over!");
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}

}
