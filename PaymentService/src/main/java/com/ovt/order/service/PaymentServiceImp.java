/**
 * PaymentServiceImp.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * 2015年12月22日
 */
package com.ovt.order.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ovt.common.exception.OVTRuntimeException;
import com.ovt.common.utils.DateTimeUtils;
import com.ovt.order.dao.PaymentDao;
import com.ovt.order.dao.constant.DBConstants.TABLES.ALI_PAYMENT_LOG;
import com.ovt.order.dao.constant.PayConstants.PAY_SOURCE;
import com.ovt.order.dao.constant.PayConstants.PAY_TYPE;
import com.ovt.order.dao.vo.PageInfo;
import com.ovt.order.dao.vo.Payment;
import com.ovt.order.service.exception.ServiceErrorCode;
import com.ovt.order.service.exception.ServiceException;

/**
 * PaymentServiceImp
 * 
 * @Author leo.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
@Service
public class PaymentServiceImp implements PaymentService
{
    @Autowired
    private PaymentDao paymentDao;

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.service.PaymentService#createPayment(java.lang.String)
     */
    @Override
    public Payment createPayment(String orderNo) throws ServiceException
    {
        // checkOrderNo

        Payment payment = new Payment();
        payment.setOrderNo(orderNo);

        try
        {
            paymentDao.save(payment);
        }
        catch (OVTRuntimeException e)
        {
            throw new ServiceException(ServiceErrorCode.SAVE_PAYMENT_ERROR,
                    "Failed to save payment!", e);
        }

        return payment;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.service.PaymentService#updatePayment(com.ovt.dao.vo.Payment)
     */
    @Override
    public void updatePayment(Payment payment) throws ServiceException
    {
        // check payment params

        try
        {
            paymentDao.update(payment);
        }
        catch (OVTRuntimeException e)
        {
            throw new ServiceException(ServiceErrorCode.UPDATE_PAYMENT_ERROR,
                    "Failed to update payment", e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.service.PaymentService#getPayment(java.lang.String)
     */
    @Override
    public Payment getPayment(String orderNo) throws ServiceException
    {
        Payment payment = null;

        try
        {
            payment = paymentDao.getPayment(orderNo);
        }
        catch (OVTRuntimeException e)
        {
            throw new ServiceException(ServiceErrorCode.GET_PAYMENT_ERROR,
                    "Failed to get payment", e);
        }

        return payment;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.service.PaymentService#deletePayment(java.lang.String)
     */
    @Override
    public void deletePayment(String orderNo) throws ServiceException
    {
        try
        {
            paymentDao.delete(orderNo);
        }
        catch (OVTRuntimeException e)
        {
            throw new ServiceException(ServiceErrorCode.DELETE_PAYMENT_ERROR,
                    "Failed to delete payment", e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.service.PaymentService#getPaymentList()
     */
    @Override
    public List<Payment> getPaymentList() throws ServiceException
    {
        List<Payment> paymentList = null;

        try
        {
            paymentList = paymentDao.getPaymentList();
        }
        catch (OVTRuntimeException e)
        {
            throw new ServiceException(ServiceErrorCode.GET_PAYMENTS_ERROR,
                    "Failed to get payment list", e);
        }

        return paymentList;
    }
    
    @Override
    public List<Payment> getPaymentListOrderByPayTime() throws ServiceException
    {
        List<Payment> paymentList = null;

        try
        {
            paymentList = paymentDao.getPaymentListOrderbyPayTime();
        }
        catch (OVTRuntimeException e)
        {
            throw new ServiceException(ServiceErrorCode.GET_PAYMENTS_ERROR,
                    "Failed to get payment list", e);
        }

        return paymentList;
    }

    @Override
    public List<Payment> getPaymentListByTimeScope(String startTime, String endTime)
            throws ServiceException
    {
        Timestamp startTimestamp = DateTimeUtils.toTimestamp(DateTimeUtils
                .parseDate(startTime, DateTimeUtils.PATTERN_SQL_DATETIME_FULL));
        Timestamp endTimestamp = DateTimeUtils.toTimestamp(DateTimeUtils
                .parseDate(endTime, DateTimeUtils.PATTERN_SQL_DATETIME_FULL));
        List<Payment> paymentList = null;

        try
        {
            paymentList = paymentDao.getPaymentListByTimeScope(startTimestamp,
                    endTimestamp);
        }
        catch (OVTRuntimeException e)
        {
            throw new ServiceException(ServiceErrorCode.GET_PAYMENTS_ERROR,
                    "Failed to get payment list by time scope", e);
        }

        return paymentList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ovt.service.PaymentService#parseAlipayPayNotifyParams(java.util.Map)
     */
    @Override
    public Payment parseAlipayPayNotifyParams(Map<String, String> params)
            throws ServiceException
    {
        Payment payment = new Payment();
        payment.setOrderNo(params.get(ALI_PAYMENT_LOG.OUT_TRADE_NO));
        payment.setPayNo(params.get(ALI_PAYMENT_LOG.TRADE_NO));
        payment.setPayType(PAY_TYPE.ONLINE);
        payment.setPaySource(PAY_SOURCE.ALIPAY);
        payment.setPayFee(Float.valueOf(params.get(ALI_PAYMENT_LOG.TOTAL_FEE)));
        payment.setPayState(params.get(ALI_PAYMENT_LOG.TRADE_STATUS));
        payment.setPayTime(Timestamp.valueOf(params
                .get(ALI_PAYMENT_LOG.GMT_CREATE)));

        return payment;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ovt.order.service.PaymentService#createPayment(com.ovt.order.dao.
     * vo.Payment)
     */
    @Override
    public long createPayment(Payment payment) throws ServiceException
    {
        long id = 0;
        try
        {
            id = paymentDao.save(payment);
        }
        catch (OVTRuntimeException e)
        {
            throw new ServiceException(ServiceErrorCode.SAVE_PAYMENT_ERROR,
                    "Failed to save payment!", e);
        }

        return id;
    }

    @Override
    public List<Payment> getPaymentListByTime(PageInfo pageInfo, String startTime,
            String endTime) throws ServiceException
    {
        List<Payment> paymentList = null;
        try
        {
            paymentList = paymentDao.getPaymentListByTime(pageInfo, startTime, endTime);
        }
        catch (OVTRuntimeException e)
        {
            throw new ServiceException(ServiceErrorCode.GET_PAYMENTS_ERROR,
                    "Failed to query payment list!", e);
        }
        return paymentList;
    }

}
