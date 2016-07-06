/**
 * RefundLogService.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * 2015年12月22日
 */
package com.ovt.order.service;

import java.util.List;
import java.util.Map;

import com.ovt.order.dao.vo.AliRefundLog;
import com.ovt.order.service.exception.ServiceException;

/**
 * RefundLogService
 * 
 * @Author leo.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public interface RefundLogService
{
    /**
     * save alipay refund log
     * 
     * @param params
     * @return long
     * @throws ServiceException
     */
    public long saveAliRefundLog(Map<String, String> params)
            throws ServiceException;

    /**
     * get alipay refund log list
     * 
     * @return List<AliRefundLog>
     * @throws ServiceException
     */
    public List<AliRefundLog> getAliRefundLogList() throws ServiceException;
    
    /**
     * get alipay refund log.
     * @param logId
     * @return AliRefundLog
     * @throws ServiceException
     */
    public AliRefundLog getAliRefundLog(long logId) throws ServiceException;

}
