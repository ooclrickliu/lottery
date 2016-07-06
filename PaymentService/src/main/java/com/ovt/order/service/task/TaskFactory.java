/**
 * TaskFactory.java
 * 
 * Copyright@2016 OVT Inc. All rights reserved. 
 * 
 * 2016年1月20日
 */
package com.ovt.order.service.task;

import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

/**
 * TaskFactory
 * 
 * @Author brad.zhou
 * @Version 1.0
 * @See 
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
@Component
@EnableScheduling
public class TaskFactory
{
    @Bean
    public OrderTask newOrderTask()
    {
        return new OrderTask();
    }
    
    @Bean
    public AccountCheckTask newAccountCheckTask()
    {
        return new AccountCheckTask();
    }
}
