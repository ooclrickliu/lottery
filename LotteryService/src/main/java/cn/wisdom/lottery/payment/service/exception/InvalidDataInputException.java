/**
 * InvalidDataInputException.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * May 6, 2015
 */
package cn.wisdom.lottery.payment.service.exception;

/**
 * InvalidDataInputException
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public class InvalidDataInputException extends ServiceException
{

    private static final long serialVersionUID = 2814230619753247016L;

    public InvalidDataInputException(String errCode, String message)
    {
        super(errCode, message);
    }
}
