/**
 * PaymentSDKException.java
 * 
 * Copyright@2016 OVT Inc. All rights reserved. 
 * 
 * 2016年1月7日
 */
package com.ovt.order.util.exception;

/**
 * PaymentSDKException
 * 
 * @Author brad.zhou
 * @Version 1.0
 * @See 
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public class PaymentSDKException extends OVTException
{
    private static final long serialVersionUID = 2082130378236693432L;

    public PaymentSDKException()
    {
        super();
    }

    public PaymentSDKException(String errCode, String message)
    {
        super(errCode, message);
    }

    public PaymentSDKException(String errCode, Throwable cause)
    {
        super(errCode, cause);
    }

    public PaymentSDKException(String errCode, String message, Throwable cause)
    {
        super(errCode, message, cause);
    }
}
