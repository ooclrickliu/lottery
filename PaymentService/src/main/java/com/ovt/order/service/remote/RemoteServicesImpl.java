/**
 * RemoteServices.java
 * 
 * Copyright@2016 OVT Inc. All rights reserved. 
 * 
 * 2016年1月11日
 */
package com.ovt.order.service.remote;

import java.text.MessageFormat;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.ovt.common.model.JsonDocument;
import com.ovt.common.utils.StringUtils;
import com.ovt.order.service.exception.ServiceErrorCode;
import com.ovt.order.service.exception.ServiceException;

/**
 * RemoteServices
 * 
 * @Author leo.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
@Service
public class RemoteServicesImpl implements RemoteServices
{
    @Autowired
    private RestTemplate restTemplate;

    private static final String DOORBELL_SERVER_PAY_CB_PARAMS = "?deviceId={0}&orderNo={1}";

    private static final String DOORBELL_SERVER_REFUND_CB_PARAMS = "?refundRequestIds={0}";

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.order.service.remote.RemoteServices#
     * notifyDoorbellServiceForPaySuccess(java.lang.String, java.lang.String)
     */
    @Override
    public JsonDocument notifyDoorbellServiceForPaySuccess(String userId, String orderNo)
            throws ServiceException
    {
        String params = MessageFormat.format(DOORBELL_SERVER_PAY_CB_PARAMS, userId, orderNo);
        String url = RemoteConstants.DOORBELL_SERVER_PAY_CB + params;

        try
        {
            JsonDocument result = restTemplate.postForObject(url, null, JsonDocument.class);
            return result;
        }
        catch (RestClientException e)
        {
            String errCode = MessageFormat.format(
                    "Access doorbell service pay callback failed, url: {0}", url);
            throw new ServiceException(ServiceErrorCode.ACCESS_REMOTE_FAILED, errCode, e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.order.service.remote.RemoteServices#
     * notifyDoorbellServiceForRefundSuccess(java.util.List)
     */
    @Override
    public JsonDocument notifyDoorbellServiceForRefundSuccess(Collection<Long> refundRequestIds)
            throws ServiceException
    {
        String params = MessageFormat.format(DOORBELL_SERVER_REFUND_CB_PARAMS,
                StringUtils.getCSV(refundRequestIds));
        String url = RemoteConstants.DOORBELL_SERVER_REFUND_CB + params;

        try
        {
            JsonDocument result = restTemplate.postForObject(url, null, JsonDocument.class);
            return result;
        }
        catch (RestClientException e)
        {
            String errCode = MessageFormat.format(
                    "Access doorbell service refund callback failed, url: {0}", url);
            throw new ServiceException(ServiceErrorCode.ACCESS_REMOTE_FAILED, errCode, e);
        }
    }

    // public static void main(String[] args)
    // {
    // List<Long> refundRequestIds = new ArrayList<Long>();
    // refundRequestIds.add(1L);
    // refundRequestIds.add(2L);
    // refundRequestIds.add(3L);
    //
    // String params = MessageFormat.format(DOORBELL_SERVER_REFUND_CB_PARAMS,
    // StringUtils.getCSV(refundRequestIds));
    // String url =
    // "http://localhost:18080/DoorbellService/api/orders/callback/refund"
    // + params;
    // System.out.println(url);
    // RestTemplate restTemplate = new RestTemplate();
    // String result = restTemplate.postForObject(url, null, String.class);
    // System.out.println(result);
    // }

}
