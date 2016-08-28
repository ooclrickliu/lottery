package cn.wisdom.lottery.service.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import cn.wisdom.lottery.common.log.Logger;
import cn.wisdom.lottery.common.log.LoggerFactory;
import cn.wisdom.lottery.service.LotteryServiceFacade;

public class ClearUnPaidLottery {
	
	@Autowired
	private LotteryServiceFacade lotteryServiceFacade;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ClearUnPaidLottery.class.getName());

	@Scheduled(cron = "0 58 18 ? * SUN,TUE,THU")
	public void execute()
	{
		LOGGER.info("Start to clear UnPaid lottery...");
		
		lotteryServiceFacade.clearUnpaidLottery();
		
		LOGGER.info("Clear UnPaid lottery done!");
	}
}
