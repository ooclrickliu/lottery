/**
 * RefundLogDao.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * 2015年12月21日
 */
package com.ovt.order.dao;

import java.util.List;

import com.ovt.order.dao.vo.RefundLog;

/**
 * RefundLogDao
 * 
 * @Author leo.liu
 * @Version 1.0
 * @See 
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public interface RefundLogDao
{
    /**
     * save refund log
     * 
     * @param refundLog
     * @return
     */
    public long save(RefundLog refundLog);
    
    /**
     * get refund log list
     * 
     * @return
     */
    public List<RefundLog> getRefundLogList();
    
    /**
     * get refund log.
     * @param logId
     * @return RefundLog
     */
    public RefundLog getRefundLog(long logId);
}
