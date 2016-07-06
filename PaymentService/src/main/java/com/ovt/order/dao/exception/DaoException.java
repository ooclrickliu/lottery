/**
 * DaoException.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * May 7, 2015
 */
package com.ovt.order.dao.exception;

import com.ovt.common.exception.OVTRuntimeException;

/**
 * DaoException
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public class DaoException extends OVTRuntimeException
{
    private static final long serialVersionUID = 8390021013387062726L;

    public DaoException()
    {
        super();
    }

    public DaoException(String errCode, String message)
    {
        super(errCode, message);
    }

    public DaoException(String errCode, Throwable cause)
    {
        super(errCode, cause);
    }

    public DaoException(String errCode, String message, Throwable cause)
    {
        super(errCode, message, cause);
    }
}
