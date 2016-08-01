/**
 * RemoteServiceException.java
 * 
 * Copyright@2016 OVT Inc. All rights reserved. 
 * 
 * Jan 6, 2016
 */
package cn.wisdom.lottery.service.exception;

/**
 * RemoteServiceException
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See 
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public class RemoteServiceException extends ServiceException
{

    private static final long serialVersionUID = 1L;
    
    public RemoteServiceException()
    {
        super();
    }

    public RemoteServiceException(String errCode, String message)
    {
        super(errCode, message);
    }

    public RemoteServiceException(String errCode, Throwable cause)
    {
        super(errCode, cause);
    }

    public RemoteServiceException(String errCode, String message,
            Throwable cause)
    {
        super(errCode, message, cause);
    }

}
