/**
 * RefundDao.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * 2015年12月21日
 */
package com.ovt.order.dao;

import java.sql.Timestamp;
import java.util.List;

import com.ovt.order.dao.vo.PageInfo;
import com.ovt.order.dao.vo.Refund;

/**
 * RefundDao
 * 
 * @Author leo.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public interface RefundDao
{
    /**
     * save refunds
     * 
     * @param refunds
     * @return
     */
    public void save(List<Refund> refunds);

    /**
     * update refunds
     * 
     * @param batchNo
     */
    public void update(List<Refund> refunds);

    /**
     * get refund by refundRequestId
     * 
     * @param refundRequestId
     * @return
     */
    public Refund getRefund(long refundRequestId);

    /**
     * get refund list
     * 
     * @param pageInfo
     * 
     * @return
     */
    public List<Refund> getRefundList(PageInfo pageInfo);

    public List<Refund> getRefundListOrderByRefundTime();
    
    /**
     * get refund list by time scope
     * 
     * @param startTime
     * @param endTime
     * @return
     */
    public List<Refund> getRefundListByTimeScope(Timestamp startTime,
            Timestamp endTime);

    /**
     * get refund request ids by batch no
     * 
     * @param batchNo
     * @return List<Long>
     */
    public List<Long> getRefundRequestIdsByBatchNo(String batchNo);

    /**
     * @param batchNo
     * @return
     */
    public List<Refund> getRefundsByBatchNo(String batchNo);

    /**
     * 
     * @param requestIds
     * @return
     */
    public List<Refund> getRefundsByRequestIds(List<Long> requestIds);

    /**
     * 
     * @param refundIds
     * @param newBatchNo
     */
    public void updateBatchNo(List<Long> refundIds, String newBatchNo);

    /**
     * 
     * @param pageInfo
     * @param startTime
     * @param endTime
     * @return
     */
    public List<Refund> getRefundListByTime(PageInfo pageInfo,
            String startTime, String endTime);
}
