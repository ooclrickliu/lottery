/**
 * RefundRequestMapper.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * 2015年12月23日
 */
package com.ovt.order.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.ovt.order.dao.constant.OrderState;
import com.ovt.order.dao.constant.DBConstants.TABLES.REFUND_REQUEST;
import com.ovt.order.dao.vo.RefundRequest;

/**
 * RefundRequestMapper
 * 
 * @Author brad.zhou
 * @Version 1.0
 * @See 
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public class RefundRequestMapper implements RowMapper<RefundRequest>
{

    @Override
    public RefundRequest mapRow(ResultSet rs, int rowNum) throws SQLException
    {
        RefundRequest refundRequest = new RefundRequest();
        refundRequest.setId(rs.getLong(REFUND_REQUEST.ID));
        refundRequest.setOrderNo(rs.getString(REFUND_REQUEST.ORDER_NO));
        refundRequest.setCreateBy(rs.getString(REFUND_REQUEST.CREATE_BY));
        refundRequest.setRefundReason(rs.getString(REFUND_REQUEST.REFUND_REASON));
        refundRequest.setRefundFee(rs.getFloat(REFUND_REQUEST.REFUND_FEE));
        refundRequest.setRefundDesc(rs.getString(REFUND_REQUEST.REFUND_DESC));
        refundRequest.setAuditorId(rs.getLong(REFUND_REQUEST.AUDITOR_ID));
        refundRequest.setFeedback(rs.getString(REFUND_REQUEST.FEEDBACK));
        refundRequest.setCreateTime(rs.getTimestamp(REFUND_REQUEST.CREATE_TIME));
        refundRequest.setRefundState(OrderState.valueOf(rs.getString(REFUND_REQUEST.REFUND_STATE)));
        refundRequest.setBatchNo(rs.getString(REFUND_REQUEST.BATCH_NO));
        refundRequest.setRefundLog(rs.getString(REFUND_REQUEST.REFUND_LOG));
        return refundRequest;
    }

}
