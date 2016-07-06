/**
 * OrderTask.java
 * 
 * Copyright@2016 OVT Inc. All rights reserved. 
 * 
 * 2016年1月20日
 */
package com.ovt.order.service.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import com.ovt.common.log.Logger;
import com.ovt.common.log.LoggerFactory;
import com.ovt.order.service.OrderService;
import com.ovt.order.service.exception.ServiceException;

/**
 * OrderTask
 * 
 * @Author brad.zhou
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public class OrderTask
{
    @Autowired
    private OrderService orderService;

    private Logger logger = LoggerFactory.getLogger(OrderService.class
            .getName());

    @Scheduled(cron = "0 0 1,13 * * ?")
    public void cleanExpiredUnPaidOrders()
    {
        logger.info("Check expired unpaid orders task start!");

        try
        {
            orderService.cleanExpiredUnpaidOrders();
        }
        catch (ServiceException e)
        {
            logger.error("Clean expired unpaid orders failed", e);
        }

        logger.info("Check expire unpaid orders task complete!");
    }
}
