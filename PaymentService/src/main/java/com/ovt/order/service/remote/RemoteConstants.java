/**
 * RemoteConstants.java
 * 
 * Copyright@2016 OVT Inc. All rights reserved. 
 * 
 * 2016年1月11日
 */
package com.ovt.order.service.remote;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ovt.order.service.AppPropertiesService;

/**
 * RemoteConstants
 * 
 * @Author leo.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
@Component
public class RemoteConstants
{
    @Autowired
    private AppPropertiesService appProperties;

    public static String DOORBELL_SERVER_PAY_CB;

    public static String DOORBELL_SERVER_REFUND_CB;

    @PostConstruct
    public void init()
    {
        
        DOORBELL_SERVER_PAY_CB = appProperties.getDoorbellServer()
                + "/orders/callback/paid";
        
        DOORBELL_SERVER_REFUND_CB = appProperties.getDoorbellServer()
                + "/orders/callback/refund";
    }
}
