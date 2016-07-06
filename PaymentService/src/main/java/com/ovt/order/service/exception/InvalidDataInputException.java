/**
 * InvalidDataInputException.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * 2015年12月21日
 */
package com.ovt.order.service.exception;

/**
 * InvalidDataInputException
 * 
 * @Author brad.zhou
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public class InvalidDataInputException extends ServiceException
{
    private static final long serialVersionUID = 4832886590720128120L;

    public InvalidDataInputException(String errCode, String message)
    {
        super(errCode, message);
    }
}
