/**
 * PriceService.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * Dec 29, 2015
 */
package cn.wisdom.lottery.payment.service;

import cn.wisdom.lottery.payment.dao.vo.Lottery;
import cn.wisdom.lottery.payment.service.exception.ServiceException;

/**
 * PriceService
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public interface LotteryPriceService
{

    /**
     * Calculate order fee.
     * 
     * @param memberProfile
     * @param fullMemberProfile
     * @param order
     * @return
     */
    int calculateLotteryTotalFee(Lottery lottery) throws ServiceException;
}
