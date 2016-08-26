/**
 * TaskFactory.java
 * 
 * Copyright@2016 OVT Inc. All rights reserved. 
 * 
 * Jan 15, 2016
 */
package cn.wisdom.lottery.service.task;

import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

/**
 * TaskFactory
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
@Component
@EnableScheduling
public class TaskFactory
{
	
    @Bean
    public LotteryOpenPrizeTask newLotteryPublishTask()
    {
        return new LotteryOpenPrizeTask();
    }
    
    @Bean
    public LotteryPrintNotifyTask newLotteryPrintNotifyTask()
    {
    	return new LotteryPrintNotifyTask();
    }
}
