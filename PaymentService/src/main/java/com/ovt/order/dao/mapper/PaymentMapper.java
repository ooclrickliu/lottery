/**
 * PaymentMapper.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * 2015年12月21日
 */
package com.ovt.order.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.ovt.order.dao.constant.DBConstants.TABLES.PAYMENT;
import com.ovt.order.dao.vo.Payment;

/**
 * PaymentMapper
 * 
 * @Author leo.liu
 * @Version 1.0
 * @See 
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public class PaymentMapper implements RowMapper<Payment>
{

    /* (non-Javadoc)
     * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet, int)
     */
    @Override
    public Payment mapRow(final ResultSet rs, int rowNum) throws SQLException
    {
        Payment payment = new Payment();
        payment.setId(rs.getLong(PAYMENT.ID));
        payment.setOrderNo(rs.getString(PAYMENT.ORDER_NO));
        payment.setPayNo(rs.getString(PAYMENT.PAY_NO));
        payment.setPayType(rs.getString(PAYMENT.PAY_TYPE));
        payment.setPaySource(rs.getString(PAYMENT.PAY_SOURCE));
        payment.setPayFee(rs.getFloat(PAYMENT.PAY_FEE));
        payment.setPayState(rs.getString(PAYMENT.PAY_STATE));
        payment.setPayTime(rs.getTimestamp(PAYMENT.PAY_TIME));
        payment.setPayLogId(rs.getLong(PAYMENT.PAY_LOG_ID));
        payment.setCreateTime(rs.getTimestamp(PAYMENT.CREATE_TIME));
        payment.setDelete(rs.getBoolean(PAYMENT.IS_DELETE));
        
        return payment;
    }

}
