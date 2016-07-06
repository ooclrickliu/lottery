/**
 * OVTException.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * May 7, 2015
 */
package com.ovt.order.util.exception;

/**
 * OVTException
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[Common] 1.0
 */
public class OVTException extends Exception
{
    private static final long serialVersionUID = 1999264699582196652L;

    private String errorCode;

    public OVTException()
    {
        super();
    }

    public OVTException(String errCode, String message)
    {
        super(message);
        this.errorCode = errCode;
    }

    public OVTException(String errCode, Throwable cause)
    {
        super(cause);
        this.errorCode = errCode;
    }

    public OVTException(String errCode, String message, Throwable cause)
    {
        super(message, cause);
        this.errorCode = errCode;
    }

    public String getErrorCode()
    {
        return errorCode;
    }

    public void setErrorCode(String errorCode)
    {
        this.errorCode = errorCode;
    }
}
