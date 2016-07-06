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

import com.ovt.order.dao.constant.DBConstants.TABLES.APP_ALI_PAYMENT_LOG;
import com.ovt.order.dao.vo.AppAliPaymentLog;

/**
 * AliPaymentLogMapper
 * 
 * @Author leo.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public class AppAliPaymentLogMapper implements RowMapper<AppAliPaymentLog>
{

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet,
     * int)
     */
    @Override
    public AppAliPaymentLog mapRow(ResultSet rs, int rowNum)
            throws SQLException
    {
        AppAliPaymentLog aliPaymentLog = new AppAliPaymentLog();
        aliPaymentLog.setId(rs.getLong(APP_ALI_PAYMENT_LOG.ID));
        aliPaymentLog.setResultStatus(rs
                .getString(APP_ALI_PAYMENT_LOG.RESULT_STATUS));
        aliPaymentLog.setMemo(rs.getString(APP_ALI_PAYMENT_LOG.MEMO));
        aliPaymentLog.setPartner(rs.getString(APP_ALI_PAYMENT_LOG.PARTNER));
        aliPaymentLog.setSellerId(rs.getString(APP_ALI_PAYMENT_LOG.SELLER_ID));
        aliPaymentLog.setOutTradeNo(rs
                .getString(APP_ALI_PAYMENT_LOG.OUT_TRADE_NO));
        aliPaymentLog.setSubject(rs.getString(APP_ALI_PAYMENT_LOG.SUBJECT));
        aliPaymentLog.setBody(rs.getString(APP_ALI_PAYMENT_LOG.BODY));
        aliPaymentLog.setTotalFee(rs.getString(APP_ALI_PAYMENT_LOG.TOTAL_FEE));
        aliPaymentLog
                .setNotifyUrl(rs.getString(APP_ALI_PAYMENT_LOG.NOTIFY_URL));
        aliPaymentLog.setService(rs.getString(APP_ALI_PAYMENT_LOG.SERVICE));
        aliPaymentLog.setPaymentType(rs
                .getString(APP_ALI_PAYMENT_LOG.PAYMENT_TYPE));
        aliPaymentLog.setInputCharset(rs
                .getString(APP_ALI_PAYMENT_LOG.INPUT_CHARSET));
        aliPaymentLog.setItBPay(rs.getString(APP_ALI_PAYMENT_LOG.IT_B_PAY));
        aliPaymentLog.setSuccess(rs.getString(APP_ALI_PAYMENT_LOG.SUCCESS));
        aliPaymentLog.setSignType(rs.getString(APP_ALI_PAYMENT_LOG.SIGN_TYPE));
        aliPaymentLog.setSign(rs.getString(APP_ALI_PAYMENT_LOG.SIGN));
        aliPaymentLog.setCreateTime(rs
                .getTimestamp(APP_ALI_PAYMENT_LOG.CREATE_TIME));

        return aliPaymentLog;
    }

}
