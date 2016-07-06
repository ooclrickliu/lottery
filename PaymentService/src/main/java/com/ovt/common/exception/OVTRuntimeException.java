/**
 * OVTRuntimeException.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * May 7, 2015
 */
package com.ovt.common.exception;

/**
 * OVTRuntimeException
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public class OVTRuntimeException extends RuntimeException
{
    private static final long serialVersionUID = -755620367818583791L;

    private String errorCode;

    public OVTRuntimeException()
    {
        super();
    }

    public OVTRuntimeException(String errCode, String message)
    {
        super(message);
        this.errorCode = errCode;
    }

    public OVTRuntimeException(String errCode, Throwable cause)
    {
        super(cause);
        this.errorCode = errCode;
    }

    public OVTRuntimeException(String errCode, String message, Throwable cause)
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
