/**
 * ServiceException.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * 2015年12月21日
 */
package com.ovt.order.service.exception;

import com.ovt.common.exception.OVTException;

/**
 * ServiceException
 * 
 * @Author brad.zhou
 * @Version 1.0
 * @See 
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public class ServiceException extends OVTException
{
    private static final long serialVersionUID = -2563559231919729710L;

    public ServiceException()
    {
        super();
    }
    
    public ServiceException(String errCode, String message)
    {
        super(errCode, message);
    }
    
    public ServiceException(String errCode, Throwable cause)
    {
        super(errCode, cause);
    }
    
    public ServiceException(String errCode, String message, Throwable cause)
    {
        super(errCode, message, cause);
    }
}
