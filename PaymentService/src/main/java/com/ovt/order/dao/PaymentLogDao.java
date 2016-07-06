/**
 * PaymentLogDao.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * 2015年12月21日
 */
package com.ovt.order.dao;

import java.util.List;

import com.ovt.order.dao.vo.PaymentLog;

/**
 * PaymentLogDao
 * 
 * @Author leo.liu
 * @Version 1.0
 * @See 
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public interface PaymentLogDao
{
    /**
     * save payment log
     * 
     * @param paymentLog
     * @return
     */
    public long save(PaymentLog paymentLog);
    
    /**
     * get payment log list
     * 
     * @return
     */
    public List<PaymentLog> getPaymentLogList();
    
    /**
     * get payment log.
     * @param logId
     * 
     * @return
     */
    public PaymentLog getPaymentLog(String logId);
    
    
}
