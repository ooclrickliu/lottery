/**
 * AliRefundLogMapper.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * 2015年12月21日
 */
package com.ovt.order.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.ovt.order.dao.constant.DBConstants.TABLES.ALI_REFUND_LOG;
import com.ovt.order.dao.vo.AliRefundLog;
import com.ovt.order.dao.vo.RefundLog;

/**
 * AliRefundLogMapper
 * 
 * @Author leo.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public class AliRefundLogMapper implements RowMapper<RefundLog>
{

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet,
     * int)
     */
    @Override
    public RefundLog mapRow(ResultSet rs, int rowNum) throws SQLException
    {
        AliRefundLog aliRefundLog = new AliRefundLog();
        aliRefundLog.setId(rs.getLong(ALI_REFUND_LOG.ID));
        aliRefundLog.setBatchNo(rs.getString(ALI_REFUND_LOG.BATCH_NO));
        aliRefundLog.setNotifyTime(rs.getTimestamp(ALI_REFUND_LOG.NOTIFY_TIME));
        aliRefundLog.setNotifyType(rs.getString(ALI_REFUND_LOG.NOTIFY_TYPE));
        aliRefundLog.setNotifyId(rs.getString(ALI_REFUND_LOG.NOTIFY_ID));
        aliRefundLog.setSignType(rs.getString(ALI_REFUND_LOG.SIGN_TYPE));
        aliRefundLog.setSign(rs.getString(ALI_REFUND_LOG.SIGN));
        aliRefundLog.setSuccessNum(rs.getString(ALI_REFUND_LOG.SUCCESS_NUM));
        aliRefundLog.setResultDetails(rs.getString(ALI_REFUND_LOG.RESULT_DETAILS));
        
        return aliRefundLog;
    }

}
