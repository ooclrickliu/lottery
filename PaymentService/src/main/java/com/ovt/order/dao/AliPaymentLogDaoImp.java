/**
 * AliPaymentLogDao.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * 2015年12月21日
 */
package com.ovt.order.dao;

import java.text.MessageFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ovt.order.dao.handler.PostSaveHandler;
import com.ovt.order.dao.mapper.AliPaymentLogMapper;
import com.ovt.order.dao.vo.AliPaymentLog;
import com.ovt.order.dao.vo.PaymentLog;

/**
 * AliPaymentLogDao
 * 
 * @Author leo.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
@Repository
public class AliPaymentLogDaoImp implements PaymentLogDao
{
    @Autowired
    private DaoHelper daoHelper;

    @Autowired
    private AliPaymentLogMapper aliPaymentLogMapper;

    private static final String SQL_INSERT_ALI_PAYMENT_LOG = "INSERT INTO alipay_payment_log"
            + "(notify_time, notify_type, notify_id, sign_type, sign, out_trade_no, subject,"
            + " trade_no, trade_status, buyer_id, buyer_email, total_fee, gmt_create,"
            + " gmt_payment, is_total_fee_adjust, use_coupon, discount, refund_status,"
            + " gmt_refund, create_time) VALUES"
            + " (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";

    private static final String SQL_GET_LOG_LIST = "SELECT id, notify_time, "
            + " notify_type, notify_id, sign_type, sign, out_trade_no, subject,"
            + " trade_no, trade_status, buyer_id, buyer_email, total_fee, gmt_create,"
            + " gmt_payment, is_total_fee_adjust, use_coupon, discount, refund_status,"
            + " gmt_refund, create_time FROM alipay_payment_log  WHERE trade_status='TRADE_SUCCESS'";
    
    private static final String SQL_GET_LOG = "SELECT id, notify_time, "
            + " notify_type, notify_id, sign_type, sign, out_trade_no, subject,"
            + " trade_no, trade_status, buyer_id, buyer_email, total_fee, gmt_create,"
            + " gmt_payment, is_total_fee_adjust, use_coupon, discount, refund_status,"
            + " gmt_refund, create_time FROM alipay_payment_log"
            + " WHERE id = ?";

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.dao.PaymentLogDao#save(com.ovt.dao.vo.PaymentLog)
     */
    @Override
    public long save(final PaymentLog paymentLog)
    {
        final AliPaymentLog aliPaymentLog = (AliPaymentLog) paymentLog;

        Object params[] = new Object[19];
        params[0] = aliPaymentLog.getNotifyTime();
        params[1] = aliPaymentLog.getNotifyType();
        params[2] = aliPaymentLog.getNotifyId();
        params[3] = aliPaymentLog.getSignType();
        params[4] = aliPaymentLog.getSign();
        params[5] = aliPaymentLog.getOutTradeNo();
        params[6] = aliPaymentLog.getSubject();
        params[7] = aliPaymentLog.getTradeNo();
        params[8] = aliPaymentLog.getTradeStatus();
        params[9] = aliPaymentLog.getBuyerId();
        params[10] = aliPaymentLog.getBuyerEmail();
        params[11] = aliPaymentLog.getTotalFee();
        params[12] = aliPaymentLog.getGmtCreate();
        params[13] = aliPaymentLog.getGmtPayment();
        params[14] = aliPaymentLog.getIsTotalFeeAdjust();
        params[15] = aliPaymentLog.getUseCoupon();
        params[16] = aliPaymentLog.getDiscount();
        params[17] = aliPaymentLog.getRefundStatus();
        params[18] = aliPaymentLog.getGmtRefund();

        PostSaveHandler handler = new PostSaveHandler()
        {
            @Override
            public void handle(Long id)
            {
                aliPaymentLog.setId(id);
            }
        };

        String errMsg = MessageFormat.format(
                "Failed to save alipayment log, order_no = {0}",
                aliPaymentLog.getOutTradeNo());

        daoHelper.save(SQL_INSERT_ALI_PAYMENT_LOG, handler, errMsg, true,
                params);

        return aliPaymentLog.getId();
    }


    @Override
    public List<PaymentLog> getPaymentLogList()
    {
        String errMsg = "Failed to query alipay payment list";

        List<PaymentLog> aliPaymentLogList = daoHelper.queryForList(
                SQL_GET_LOG_LIST, aliPaymentLogMapper, errMsg);

        return aliPaymentLogList;
    }


    @Override
    public PaymentLog getPaymentLog(String logId)
    {
        String errMsg = MessageFormat.format(
                "Failed to get alipayment log, logId = {0}",
                logId);

        PaymentLog paymentLog = daoHelper.queryForObject(SQL_GET_LOG,
                aliPaymentLogMapper, errMsg, logId);

        return paymentLog;
    }

}
