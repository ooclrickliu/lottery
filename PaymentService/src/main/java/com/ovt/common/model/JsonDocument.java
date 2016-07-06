/**
 * JsonDocument.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * May 4, 2015
 */
package com.ovt.common.model;

/**
 * JsonDocument is the api unified output data format
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public class JsonDocument
{
    private Object data;

    private String serviceCode = "";

    private String stateCode = "";
    
    public static final String STATE_SUCCESS = "success";

    public static final String STATE_FAILED = "fail";
    
    public JsonDocument()
    {
        
    }

    public JsonDocument(String serviceCode, Object data)
    {
        this();
        this.serviceCode = serviceCode;
        this.stateCode = STATE_SUCCESS;
        this.data = data;
    }

    public JsonDocument(String serviceCode, String errCode)
    {
        this();
        this.serviceCode = serviceCode;
        this.stateCode = errCode;
    }

    public Object getData()
    {
        return data;
    }

    public void setData(Object data)
    {
        this.data = data;
    }

    public String getServiceCode()
    {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode)
    {
        this.serviceCode = serviceCode;
    }

    public String getStateCode()
    {
        return stateCode;
    }

    public void setStateCode(String stateCode)
    {
        this.stateCode = stateCode;
    }

    @Override
    public String toString()
    {
        return "JsonDocument [data=" + data + ", serviceCode=" + serviceCode
                + ", stateCode=" + stateCode + "]";
    }
}
