/**
 * AppPropertiesService.java
 * 
 * Copyright@2016 OVT Inc. All rights reserved. 
 * 
 * 2016-2-17
 */
package cn.wisdom.lottery.payment.service;

import java.util.Map;

import cn.wisdom.lottery.payment.dao.vo.AppProperty;

/**
 * AppPropertiesService
 * 
 * @Author leo.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public interface AppPropertiesService
{
    public int getCookieAccessTokenAge();

    public String getResourceManagerUrl();

    public String getPaymentServiceUrl();

    public boolean isDebugMode();

    public boolean isDebugPay();

    public String getRefundDeductFee();

    public Map<String, AppProperty> getAppPropertiesMap();
}
