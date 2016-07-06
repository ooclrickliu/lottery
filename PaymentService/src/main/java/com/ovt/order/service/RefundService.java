/**
 * RefundService.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * 2015年12月22日
 */
package com.ovt.order.service;

import java.util.List;
import java.util.Map;

import com.ovt.order.dao.vo.PageInfo;
import com.ovt.order.dao.vo.Refund;
import com.ovt.order.dao.vo.RefundRequest;
import com.ovt.order.service.exception.ServiceException;

/**
 * RefundService
 * 
 * @Author leo.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public interface RefundService
{
    /**
     * do alipay refund service
     * 
     * @param requests
     * @return String[]
     * @throws ServiceException
     */
    public String[] doAliRefund(List<RefundRequest> requests)
            throws ServiceException;

    /**
     * update refunds
     * 
     * @param refunds
     * @throws ServiceException
     */
    public void updateRefunds(List<Refund> refunds) throws ServiceException;

    /**
     * get refund by refund request id
     * 
     * @param refundRequestId
     * @return Refund
     * @throws ServiceException
     */
    public Refund getRefund(long refundRequestId) throws ServiceException;

    /**
     * get refund list
     * 
     * @param pageInfo
     * 
     * @return List<Refund>
     * @throws ServiceException
     */
    public List<Refund> getRefundList(PageInfo pageInfo)
            throws ServiceException;

    /**
     * get refund list order by refund time.
     * 
     * @return List<Refund>
     * @throws ServiceException
     */
    public List<Refund> getRefundListOrderByRefundTime()
            throws ServiceException;

    /**
     * get refund list by time scope
     * 
     * @param startTime
     * @param endTime
     * @return
     * @throws ServiceException
     */
    public List<Refund> getRefundListByTimeScope(String startTime,
            String endTime) throws ServiceException;;

    /**
     * get refund request ids by batch no
     * 
     * @param batchNo
     * @return List<Long>
     * @throws ServiceException
     */
    public List<Long> getRefundRequestIdsByBatchNo(String batchNo)
            throws ServiceException;

    /**
     * parse alipay refund call string
     * 
     * @param params
     * @return List<Refund>
     * @throws ServiceException
     */
    public List<Refund> parseAlipayRefundNotifyParams(Map<String, String> params)
            throws ServiceException;

    /**
     * @param requestIds
     * @return String
     * @throws ServiceException
     */
    public String[] doAliRefundRepost(List<Long> requestIds)
            throws ServiceException;

    /**
     * @param batchNo
     * @return
     * @throws ServiceException
     */
    public Map<String, Refund> getTradeNoRefundsMapByBatchNo(String batchNo)
            throws ServiceException;

    /**
     * 
     * @param pageInfo
     * @param startTime
     * @param endTime
     * @return
     * @throws ServiceException
     */
    public List<Refund> getRefundListByTime(PageInfo pageInfo,
            String startTime, String endTime) throws ServiceException;
}
