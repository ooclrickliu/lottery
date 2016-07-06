/**
 * GlobalExceptionController.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * Jun 17, 2015
 */
package cn.wisdom.lottery.payment.api.advisor;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.wisdom.lottery.payment.api.response.DoorbellPaymentAPIResult;
import cn.wisdom.lottery.payment.common.log.Logger;
import cn.wisdom.lottery.payment.common.log.LoggerFactory;
import cn.wisdom.lottery.payment.common.model.JsonDocument;
import cn.wisdom.lottery.payment.dao.constant.LoggerConstants;
import cn.wisdom.lottery.payment.service.exception.ServiceErrorCode;
import cn.wisdom.lottery.payment.service.exception.ServiceException;

/**
 * GlobalExceptionController
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
@ControllerAdvice
public class GlobalControllerExceptionAdvisor
{
    private static final Logger LOGGER = LoggerFactory
            .getLogger(LoggerConstants.SYSTEM_LOGGER);

    @ExceptionHandler
    @ResponseBody
    public JsonDocument handleServiceException(ServiceException serviceException)
    {
        LOGGER.error("Controller catches service exception!", serviceException);
        return new DoorbellPaymentAPIResult(serviceException.getErrorCode());
    }

    @ExceptionHandler
    @ResponseBody
    public JsonDocument handleRuntimeException(RuntimeException runtimeException)
    {
        LOGGER.error("Controller catches runtime exception!", runtimeException);
        return new DoorbellPaymentAPIResult(ServiceErrorCode.SYSTEM_UNEXPECTED);
    }
}
