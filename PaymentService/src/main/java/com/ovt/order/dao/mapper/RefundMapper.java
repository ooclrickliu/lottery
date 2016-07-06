/**
 * RefundMapper.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * 2015年12月21日
 */
package com.ovt.order.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.ovt.order.dao.constant.DBConstants.TABLES.REFUND;
import com.ovt.order.dao.vo.Refund;

/**
 * RefundMapper
 * 
 * @Author leo.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public class RefundMapper implements RowMapper<Refund>
{

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet,
     * int)
     */
    @Override
    public Refund mapRow(ResultSet rs, int rowNum) throws SQLException
    {
        Refund refund = new Refund();
        refund.setId(rs.getLong(REFUND.ID));
        refund.setRefundRequestId(rs.getLong(REFUND.REFUND_REQUEST_ID));
        refund.setBatchNo(rs.getString(REFUND.BATCH_NO));
        refund.setTradeNo(rs.getString(REFUND.TRADE_NO));
        refund.setRefundFee(rs.getFloat(REFUND.REFUND_FEE));
        refund.setRefundState(rs.getString(REFUND.REFUND_STATE));
        refund.setRefundTax(rs.getFloat(REFUND.REFUND_TAX));
        refund.setRefundTaxState(rs.getString(REFUND.REFUND_TAX_STATE));
        refund.setRefundTime(rs.getTimestamp(REFUND.REFUND_TIME));
        refund.setRefundLogId(rs.getLong(REFUND.REFUND_LOG_ID));
        refund.setCreateTime(rs.getTimestamp(REFUND.CREATE_TIME));

        return refund;
    }

}
