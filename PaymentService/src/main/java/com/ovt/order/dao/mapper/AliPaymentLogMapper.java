/**
 * AliPaymentLogMapper.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * 2015年12月21日
 */
package com.ovt.order.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.ovt.order.dao.constant.DBConstants.TABLES.ALI_PAYMENT_LOG;
import com.ovt.order.dao.vo.AliPaymentLog;
import com.ovt.order.dao.vo.PaymentLog;

/**
 * AliPaymentLogMapper
 * 
 * @Author leo.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public class AliPaymentLogMapper implements RowMapper<PaymentLog>
{

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet,
     * int)
     */
    @Override
    public PaymentLog mapRow(ResultSet rs, int rowNum) throws SQLException
    {
        AliPaymentLog aliPaymentLog = new AliPaymentLog();
        aliPaymentLog.setId(rs.getLong(ALI_PAYMENT_LOG.ID));
        aliPaymentLog.setNotifyTime(rs
                .getTimestamp(ALI_PAYMENT_LOG.NOTIFY_TIME));
        aliPaymentLog.setNotifyType(rs.getString(ALI_PAYMENT_LOG.NOTIFY_TYPE));
        aliPaymentLog.setNotifyId(rs.getString(ALI_PAYMENT_LOG.NOTIFY_ID));
        aliPaymentLog.setSignType(rs.getString(ALI_PAYMENT_LOG.SIGN_TYPE));
        aliPaymentLog.setSign(rs.getString(ALI_PAYMENT_LOG.SIGN));
        aliPaymentLog.setOutTradeNo(rs.getString(ALI_PAYMENT_LOG.OUT_TRADE_NO));
        aliPaymentLog.setSubject(rs.getString(ALI_PAYMENT_LOG.SUBJECT));
        aliPaymentLog.setTradeNo(rs.getString(ALI_PAYMENT_LOG.TRADE_NO));
        aliPaymentLog
                .setTradeStatus(rs.getString(ALI_PAYMENT_LOG.TRADE_STATUS));
        aliPaymentLog.setBuyerId(rs.getString(ALI_PAYMENT_LOG.BUYER_ID));
        aliPaymentLog.setBuyerEmail(rs.getString(ALI_PAYMENT_LOG.BUYER_EMAIL));
        aliPaymentLog.setTotalFee(rs.getFloat(ALI_PAYMENT_LOG.TOTAL_FEE));
        aliPaymentLog.setGmtCreate(rs.getTimestamp(ALI_PAYMENT_LOG.GMT_CREATE));
        aliPaymentLog.setGmtPayment(rs
                .getTimestamp(ALI_PAYMENT_LOG.GMT_PAYMENT));
        aliPaymentLog.setIsTotalFeeAdjust(rs
                .getString(ALI_PAYMENT_LOG.IS_TOTAL_FEE_ADJUST).trim());
        aliPaymentLog.setUseCoupon(rs.getString(ALI_PAYMENT_LOG.USE_COUPON).trim());
        aliPaymentLog.setDiscount(rs.getFloat(ALI_PAYMENT_LOG.DISCOUNT));
        aliPaymentLog.setRefundStatus(rs
                .getString(ALI_PAYMENT_LOG.REFUND_STATUS));
        aliPaymentLog.setGmtRefund(rs.getTimestamp(ALI_PAYMENT_LOG.GMT_REFUND));
        aliPaymentLog.setCreateTime(rs
                .getTimestamp(ALI_PAYMENT_LOG.CREATE_TIME));

        return aliPaymentLog;
    }

}
