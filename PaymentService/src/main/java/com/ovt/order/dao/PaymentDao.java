/**
 * PaymentDao.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * 2015年12月18日
 */
package com.ovt.order.dao;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.ovt.order.dao.vo.PageInfo;
import com.ovt.order.dao.vo.Payment;

/**
 * PaymentDao
 * 
 * @Author leo.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public interface PaymentDao
{

    /**
     * Add new payment
     * 
     * @param payment
     * @return
     */
    public long save(Payment payment);

    /**
     * update payment
     * 
     * @param payment
     */
    public void update(Payment payment);

    /**
     * get payment by orderNo
     * 
     * @param orderNo
     * @return
     */
    public Payment getPayment(String orderNo);

    /**
     * get payment list
     * 
     * @return
     */
    public List<Payment> getPaymentList();

    /**
     * get payment list order by paytime.
     * @return
     */
    public List<Payment> getPaymentListOrderbyPayTime();
    
    /**
     * get payment list by time scope
     * 
     * @param startTime
     * @param endTime
     * @return
     */
    public List<Payment> getPaymentListByTimeScope(Timestamp startTime,
            Timestamp endTime);

    /**
     * get Map<orderNo, tradeNo> by orderNos
     * 
     * @param orderNos
     * @return
     */
    public Map<String, String> getTradeNoByOrderNo(List<String> orderNos);

    /**
     * delete payment by orderNo
     * 
     * @param orderNo
     */
    public void delete(String orderNo);

    /**
     * 
     * @param pageInfo
     * @param startTime
     * @param endTime
     * @return
     */
    public List<Payment> getPaymentListByTime(PageInfo pageInfo, String startTime,
            String endTime);
}
