/**
 * GlobalControllerExceptionAdvisor.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * 2015年12月22日
 */
package com.ovt.order.controller.advisor;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ovt.common.log.Logger;
import com.ovt.common.log.LoggerFactory;
import com.ovt.common.model.JsonDocument;
import com.ovt.order.controller.response.PaymentServiceAPIResult;
import com.ovt.order.dao.constant.LoggerConstants;
import com.ovt.order.service.exception.ServiceErrorCode;
import com.ovt.order.service.exception.ServiceException;

/**
 * GlobalControllerExceptionAdvisor
 * 
 * @Author leo.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
@ControllerAdvice
public class GlobalControllerExceptionAdvisor
{
    private static final Logger LOGGER = LoggerFactory
            .getLogger(LoggerConstants.PAYMENT_SERVICE_LOGGER);

    @ExceptionHandler
    @ResponseBody
    public JsonDocument handleServiceException(ServiceException serviceException)
    {
        LOGGER.error("Controller catch a service exception!", serviceException);
        return new PaymentServiceAPIResult(serviceException.getErrorCode());
    }

    @ExceptionHandler
    @ResponseBody
    public JsonDocument handleRuntimeException(RuntimeException runtimeException)
    {
        LOGGER.error("Controller catch a runtime exception!", runtimeException);
        return new PaymentServiceAPIResult(ServiceErrorCode.SYSTEM_UNEXPECTED);
    }
}
