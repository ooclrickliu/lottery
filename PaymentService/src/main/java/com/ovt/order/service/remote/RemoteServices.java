/**
 * RemoteServices.java
 * 
 * Copyright@2016 OVT Inc. All rights reserved. 
 * 
 * 2016年1月11日
 */
package com.ovt.order.service.remote;

import java.util.Collection;

import com.ovt.common.model.JsonDocument;
import com.ovt.order.service.exception.ServiceException;

/**
 * RemoteServices
 * 
 * @Author leo.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public interface RemoteServices
{
    /**
     * notify doorbell server pay success.
     * 
     * @param userId
     * @param orderNo
     * @return JsonDocument
     * @throws ServiceException
     */
    public JsonDocument notifyDoorbellServiceForPaySuccess(String userId,
            String orderNo) throws ServiceException;

    /**
     * notify doorbell server refund success
     * 
     * @param refundRequestIds
     * @return
     * @throws ServiceException
     */
    public JsonDocument notifyDoorbellServiceForRefundSuccess(
            Collection<Long> refundRequestIds) throws ServiceException;
}
