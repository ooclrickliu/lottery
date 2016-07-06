/**
 * PaymentService.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * 2015年12月22日
 */
package com.ovt.order.service;

import java.util.List;
import java.util.Map;

import com.ovt.order.dao.vo.PageInfo;
import com.ovt.order.dao.vo.Payment;
import com.ovt.order.service.exception.ServiceException;

/**
 * PaymentService
 * 
 * @Author leo.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public interface PaymentService
{
    /**
     * create a payment for an order
     * 
     * @param orderNo
     * @return Payment
     * @throws ServiceException
     */
    public Payment createPayment(String orderNo) throws ServiceException;

    /**
     * create a payment for an order.
     * 
     * @param payment
     * @return long
     * @throws ServiceException
     */
    public long createPayment(Payment payment) throws ServiceException;

    /**
     * update payment
     * 
     * @param payment
     * @throws ServiceException
     */
    public void updatePayment(Payment payment) throws ServiceException;

    /**
     * get payment by order no
     * 
     * @param orderNo
     * @return Payment
     * @throws ServiceException
     */
    public Payment getPayment(String orderNo) throws ServiceException;

    /**
     * delete a payment by order no
     * 
     * @param orderNo
     * @throws ServiceException
     */
    public void deletePayment(String orderNo) throws ServiceException;

    /**
     * get payment list
     * 
     * @return List<Payment>
     * @throws ServiceException
     */
    public List<Payment> getPaymentList() throws ServiceException;
    
    /**
     * get payment list order by paytime.
     * 
     * @return List<Payment>
     * @throws ServiceException
     */
    public List<Payment> getPaymentListOrderByPayTime() throws ServiceException;

    /**
     * get payment list by time scope
     * 
     * @param startTime
     * @param endTime
     * @return
     * @throws ServiceException
     */
    public List<Payment> getPaymentListByTimeScope(String startTime, String endTime)
            throws ServiceException;;

    /**
     * generate a payment from notify params
     * 
     * @return Payment
     * @throws ServiceException
     */
    public Payment parseAlipayPayNotifyParams(Map<String, String> params)
            throws ServiceException;

    /**
     * 
     * @param pageInfo
     * @param startTime
     * @param endTime
     * @return
     * @throws ServiceException
     */
    public List<Payment> getPaymentListByTime(PageInfo pageInfo, String startTime,
            String endTime) throws ServiceException;
}
