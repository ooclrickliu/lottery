/**
 * OrderService.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * Dec 29, 2015
 */
package cn.wisdom.lottery.service;

import java.util.Map;

import cn.wisdom.lottery.dao.vo.Lottery;
import cn.wisdom.lottery.service.exception.ServiceException;

/**
 * OrderService
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public interface LotteryWxPayService
{
    /**
     * 调用微信统一下单接口
     * @param lottery
     * @param openId
     * @param spbillCreateIp
     * @param tradeType
     * @param body
     * @return
     * @throws ServiceException
     */
	public Map<String, String> unifiedOrder(Lottery lottery, String openId,
            String spbillCreateIp, String tradeType, String body) throws ServiceException;
}
