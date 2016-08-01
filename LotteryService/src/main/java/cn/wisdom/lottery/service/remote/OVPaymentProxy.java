/**
 * OVPaymentUtil.java
 * 
 * Copyright@2016 OVT Inc. All rights reserved. 
 * 
 * 2016-2-16
 */
package cn.wisdom.lottery.service.remote;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.wisdom.lottery.common.utils.StringUtils;
import cn.wisdom.lottery.dao.vo.AppProperty;

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
    private AppProperty appProperties;

    private OVPayment ovPayment;

    public OVPayment getOVPayment()
    {
        if (ovPayment == null)
        {
            ovPayment = new OVPayment(appProperties.paymentServiceUrl);
        }
        
        if(ovPayment != null && StringUtils.isBlank(ovPayment.getBaseUrl()))
        {
            ovPayment.setBaseUrl(appProperties.paymentServiceUrl);
        }

        return ovPayment;
    }
}