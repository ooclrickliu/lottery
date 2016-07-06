/**
 * PaymentServiceAPIResult.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * 2015年12月22日
 */
package com.ovt.order.controller.response;

import com.ovt.common.model.JsonDocument;

/**
 * PaymentServiceAPIResult
 * 
 * @Author leo.liu
 * @Version 1.0
 * @See 
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public class PaymentServiceAPIResult extends JsonDocument
{
    public static final JsonDocument SUCCESS = new PaymentServiceAPIResult();
    
    private static final String SERVICE_OV_PAYMENT = "OV_PAYMENT";
    
    public PaymentServiceAPIResult()
    {
        super(SERVICE_OV_PAYMENT, JsonDocument.STATE_SUCCESS);
    }
    
    public PaymentServiceAPIResult(Object data)
    {
        super(SERVICE_OV_PAYMENT, data);
    }
    
    public PaymentServiceAPIResult(String errCode)
    {
        super(SERVICE_OV_PAYMENT, errCode);
    }
}
