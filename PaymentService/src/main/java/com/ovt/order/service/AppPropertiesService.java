package com.ovt.order.service;

import java.util.Map;

import com.ovt.order.dao.vo.AppProperty;

public interface AppPropertiesService
{
    public String getAlipayPartener();

    public String getAlipayPrivateKey();

    public String getAlipayInputCharset();

    public String getAlipaySignType();

    public String getAlipaySellerEmail();

    public String getAlipayRefundNotifyUrl();

    public String getAlipayTransferNotifyUrl();

    public String getDoorbellServer();

    public Map<String, AppProperty> getAppPropertiesMap();

    public void setAppPropertiesMap(Map<String, AppProperty> appPropertiesMap);

    public String getAlipayPayDeadline();
}
