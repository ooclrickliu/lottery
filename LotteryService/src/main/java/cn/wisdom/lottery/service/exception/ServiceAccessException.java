/**
 * ServiceAccessException.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * May 25, 2015
 */
package cn.wisdom.lottery.service.exception;

/**
 * ServiceAccessException
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[Service] 1.0
 */
public class ServiceAccessException extends ServiceException
{

    private static final long serialVersionUID = 3636241340716546024L;

    public ServiceAccessException()
    {
        super();
    }

    public ServiceAccessException(String errCode, String message)
    {
        super(errCode, message);
    }

    public ServiceAccessException(String errCode, Throwable cause)
    {
        super(errCode, cause);
    }

    public ServiceAccessException(String errCode, String message,
            Throwable cause)
    {
        super(errCode, message, cause);
    }
}
