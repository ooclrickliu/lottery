/**
 * PaymentLogService.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * 2015年12月22日
 */
package com.ovt.order.service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import com.ovt.order.dao.vo.AliPaymentLog;
import com.ovt.order.dao.vo.AppAliPaymentLog;
import com.ovt.order.service.exception.ServiceException;

/**
 * PaymentLogService
 * 
 * @Author leo.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public interface PaymentLogService
{
    /**
     * save alipay payment log
     * 
     * @param params
     * @return AliPaymentLog
     * @throws ServiceException
     * @throws ParseException
     */
    public AliPaymentLog saveAliPaymentLog(Map<String, String> params)
            throws ServiceException;

    /**
     * get alipay payment log list
     * 
     * @return List<AliPaymentLog>
     * @throws ServiceException
     */
    public List<AliPaymentLog> getAliPaymentLogList() throws ServiceException;

    /**
     * get alipay payment log.
     * 
     * @return AliPaymentLog
     * @throws ServiceException
     */
    public AliPaymentLog getAliPaymentLog(String orderNo)
            throws ServiceException;

    /**
     * 
     * @param parameterMap
     * @return
     * @throws ServiceException
     */
    public AppAliPaymentLog saveAppAlipaymentLog(
            Map<String, String[]> parameterMap) throws ServiceException;
}
