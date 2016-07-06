/**
 * RefundReasonMapper.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * 2015年12月24日
 */
package com.ovt.order.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.ovt.order.dao.constant.DBConstants.TABLES.REFUND_REASON;
import com.ovt.order.dao.vo.RefundReason;

/**
 * RefundReasonMapper
 * 
 * @Author brad.zhou
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public class RefundReasonMapper implements RowMapper<RefundReason>
{

    @Override
    public RefundReason mapRow(ResultSet rs, int rowNum) throws SQLException
    {
        RefundReason refundReason = new RefundReason();
        refundReason.setId(rs.getLong(REFUND_REASON.ID));
        refundReason.setRefundReason(rs.getString(REFUND_REASON.REASON));

        return refundReason;
    }

}
