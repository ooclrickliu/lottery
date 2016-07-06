/**
 * OVPaymentUtil.java
 * 
 * Copyright@2016 OVT Inc. All rights reserved. 
 * 
 * 2016-2-16
 */
package cn.wisdom.lottery.payment.service.remote;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.wisdom.lottery.payment.common.utils.StringUtils;
import cn.wisdom.lottery.payment.service.AppPropertiesService;
import com.ovt.order.util.OVPayment;

/**
 * OVPaymentUtil
 * 
 * @Author leo.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
@Service
public class OVPaymentProxy
{
    @Autowired
    private AppPropertiesService appProperties;

    private OVPayment ovPayment;

    public OVPayment getOVPayment()
    {
        if (ovPayment == null)
        {
            ovPayment = new OVPayment(appProperties.getPaymentServiceUrl());
        }
        
        if(ovPayment != null && StringUtils.isBlank(ovPayment.getBaseUrl()))
        {
            ovPayment.setBaseUrl(appProperties.getPaymentServiceUrl());
        }

        return ovPayment;
    }
}
