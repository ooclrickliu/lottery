/**
 * PaymentController.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * 2015年12月22日
 */
package com.ovt.order.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ovt.common.log.Logger;
import com.ovt.common.log.LoggerFactory;
import com.ovt.common.model.JsonDocument;
import com.ovt.common.utils.DataConvertUtils;
import com.ovt.order.controller.response.PaymentServiceAPIResult;
import com.ovt.order.dao.constant.DBConstants.TABLES.ALI_PAYMENT_LOG;
import com.ovt.order.dao.constant.LoggerConstants;
import com.ovt.order.dao.constant.OrderState;
import com.ovt.order.dao.constant.PayConstants.ALIPAY_TRADE_STATUS;
import com.ovt.order.dao.threadpool.OVTask;
import com.ovt.order.dao.threadpool.OVThreadPoolExecutor;
import com.ovt.order.dao.vo.AliPaymentLog;
import com.ovt.order.dao.vo.Order;
import com.ovt.order.dao.vo.PageInfo;
import com.ovt.order.dao.vo.Payment;
import com.ovt.order.service.OrderService;
import com.ovt.order.service.PaymentLogService;
import com.ovt.order.service.PaymentService;
import com.ovt.order.service.alipay.AlipayNotify;
import com.ovt.order.service.exception.InvalidDataInputException;
import com.ovt.order.service.exception.ServiceErrorCode;
import com.ovt.order.service.exception.ServiceException;
import com.ovt.order.service.remote.RemoteServices;
import com.ovt.order.service.utils.StateChecker;

/**
 * PaymentController
 * 
 * @Author leo.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
@Controller
@RequestMapping("/pay")
public class PaymentController
{
    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PaymentLogService paymentLogService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private RemoteServices remoteServices;

    @Autowired
    private OVThreadPoolExecutor ovExecutor;

    private static final Logger LOGGER = LoggerFactory
            .getLogger(LoggerConstants.ALIPAY_NOTIFY_LOGGER);

    private static final Long DELAY_TIME = 3000L;

    private static final int MAX_COUNT = 4;

    private static final String SUCCESS = "success";

    private static final String FAILURE = "failure";

    @RequestMapping(method = RequestMethod.POST, value = "/test/alinotify")
    @ResponseBody
    public String aliNotifyTest(HttpServletRequest request)
            throws ServiceException, IOException
    {
        Map<String, String> params = AlipayNotify.processNotifyParams(request
                .getParameterMap());

        LOGGER.info("aliPayNotify: {}", "<Processed>: " + params.toString());
        AliPaymentLog aliPaymentLog = paymentLogService
                .saveAliPaymentLog(params); // 记录支付日志（保存支付宝服务器发来的异步消息中的参数）
        Order order = orderService.queryOrderInfo(
                aliPaymentLog.getOutTradeNo(), false);
        String trade_status = params.get(ALI_PAYMENT_LOG.TRADE_STATUS);

        if (trade_status.equals(ALIPAY_TRADE_STATUS.SUCCESS)
                || trade_status.equals(ALIPAY_TRADE_STATUS.FINISHED))
        {
            checkAliNotifyParams(order, params); // 业务参数校验

            Payment payment = paymentService.parseAlipayPayNotifyParams(params); // 将异步消息中的参数解析成一条payment记录，代表支付成功的记录
            payment.setPayLogId(aliPaymentLog.getId());
            paymentService.createPayment(payment); // 保存payment记录

            orderService.setOrderState(order, OrderState.ORDER_PAID); // 更新订单状态

            asyncNotifyPaidSuccess(order);
        }

        return JsonDocument.STATE_SUCCESS;

    }

    @RequestMapping(method = RequestMethod.POST, value = "/alinotify")
    @ResponseBody
    public String aliNotify(HttpServletRequest request)
            throws ServiceException, IOException
    {
        Map<String, String> params = AlipayNotify.processNotifyParams(request
                .getParameterMap());

        LOGGER.info("aliPayNotify: {}", "<Processed>: " + params.toString());
        if (AlipayNotify.verify(params)) // 验证消息是否是支付宝发出的合法消息(包括了验证签名)
        {
            AliPaymentLog aliPaymentLog = paymentLogService
                    .saveAliPaymentLog(params); // 记录支付日志（保存支付宝服务器发来的异步消息中的参数）
            Order order = orderService.queryOrderInfo(
                    aliPaymentLog.getOutTradeNo(), false);

            String trade_status = params.get(ALI_PAYMENT_LOG.TRADE_STATUS);

            if (trade_status.equals(ALIPAY_TRADE_STATUS.SUCCESS)
                    || trade_status.equals(ALIPAY_TRADE_STATUS.FINISHED))
            {
                checkAliNotifyParams(order, params); // 业务参数校验

                Payment payment = paymentService
                        .parseAlipayPayNotifyParams(params); // 将异步消息中的参数解析成一条payment记录，代表支付成功的记录
                payment.setPayLogId(aliPaymentLog.getId());
                paymentService.createPayment(payment); // 保存payment记录

                orderService.setOrderState(order, OrderState.ORDER_PAID); // 更新订单状态

                asyncNotifyPaidSuccess(order);
            }

            return JsonDocument.STATE_SUCCESS;
        }
        else
        {
            return JsonDocument.STATE_FAILED;
        }

    }

    @RequestMapping(method = RequestMethod.GET, value = "/list")
    @ResponseBody
    public JsonDocument getPaymentList() throws ServiceException
    {
        List<Payment> paymentList = paymentService.getPaymentList();

        return new PaymentServiceAPIResult(paymentList);
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/list/orderby")
    @ResponseBody
    public JsonDocument getPaymentListOrderByPayTime() throws ServiceException
    {
        List<Payment> paymentList = paymentService.getPaymentListOrderByPayTime();

        return new PaymentServiceAPIResult(paymentList);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/list/timeScope")
    @ResponseBody
    public JsonDocument getPaymentListByTimeScope(@RequestParam String startTime,
            @RequestParam String endTime) throws ServiceException
    {
        List<Payment> paymentList = paymentService.getPaymentListByTimeScope(
                startTime, endTime);

        return new PaymentServiceAPIResult(paymentList);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/list/byTime")
    @ResponseBody
    public JsonDocument getPaymentListByTime(
            @RequestParam String startTime,
            @RequestParam String endTime,
            @RequestParam(required = false, defaultValue = "0") String page,
            @RequestParam(required = false, defaultValue = "10000") String limit,
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String order)
            throws ServiceException
    {
        PageInfo pageInfo = new PageInfo(DataConvertUtils.toInt(page),
                DataConvertUtils.toInt(limit), sortBy, order);

        List<Payment> paymentList = paymentService.getPaymentListByTime(
                pageInfo, startTime, endTime);

        return new PaymentServiceAPIResult(paymentList);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/detail")
    @ResponseBody
    public JsonDocument getPayment(@RequestParam String orderNo)
            throws ServiceException
    {
        Payment payment = paymentService.getPayment(orderNo);

        return new PaymentServiceAPIResult(payment);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/log/list")
    @ResponseBody
    public JsonDocument getAliPaymentLogList() throws ServiceException
    {
        List<AliPaymentLog> aliPaymentList = paymentLogService
                .getAliPaymentLogList();

        return new PaymentServiceAPIResult(aliPaymentList);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/log/detail/alipay")
    @ResponseBody
    public JsonDocument getAliPaymentLog(@RequestParam String logId)
            throws ServiceException
    {
        AliPaymentLog aliPaymentLog = paymentLogService.getAliPaymentLog(logId);

        return new PaymentServiceAPIResult(aliPaymentLog);
    }

    private void checkAliNotifyParams(Order order, Map<String, String> params)
            throws ServiceException
    {
        if (order == null)
        {
            throw new InvalidDataInputException(
                    ServiceErrorCode.INVALID_ORDER_NUMBER,
                    "Invalid order number");
        }

        if (!StateChecker.checkStateChangeValid(order.getOrderState(),
                OrderState.ORDER_PAID))
        {
            throw new InvalidDataInputException(
                    ServiceErrorCode.INVALID_ORDER_STATE, "Invalid order state");
        }
        if (!params.containsKey(ALI_PAYMENT_LOG.TOTAL_FEE)
                || order.getOrderTotalFee() != Float.valueOf(params
                        .get(ALI_PAYMENT_LOG.TOTAL_FEE)))
        {
            throw new InvalidDataInputException(
                    ServiceErrorCode.INVALID_TOTAL_FEE, "Invalid total fee");

        }
    }

    private String resendNotifyAction(Order order, int resendCount)
    {
        String errorMsg = new StringBuffer()
                .append("The payment asynchronous notify task has failed ")
                .append(resendCount).append(" times, orderNo=")
                .append(order.getOrderNo()).toString();
        try
        {
            JsonDocument result = remoteServices
                    .notifyDoorbellServiceForPaySuccess(order.getUserId(),
                            order.getOrderNo());

            /**
             * If 1>.Notify action is success. 2>.Resend count is bigger than
             * MAX_COUNT. Then, notify action is over.
             */
            if (result.getStateCode().equalsIgnoreCase(SUCCESS))
            {
                return SUCCESS;
            }
            else if (resendCount >= MAX_COUNT)
            {
                return FAILURE;
            }
            else
            {
                LOGGER.error(errorMsg);
                try
                {
                    Thread.sleep(DELAY_TIME);

                }
                catch (InterruptedException e)
                {
                    LOGGER.error(e);
                }
                return resendNotifyAction(order, ++resendCount);
            }
        }
        catch (ServiceException e)
        {
            LOGGER.error(errorMsg, e);

            /**
             * 在第4次发送异步Notify时，DoorBell Server宕机，需要判断resendCount
             */
            if (resendCount >= MAX_COUNT)
            {
                return FAILURE;
            }
            else
            {
                try
                {
                    Thread.sleep(DELAY_TIME);
                }
                catch (InterruptedException e1)
                {
                    LOGGER.error(e1);
                }
                return resendNotifyAction(order, ++resendCount);
            }
        }
    }

    private void asyncNotifyPaidSuccess(final Order order)
    {
        OVTask ovTask = new OVTask()
        {
            @Override
            public void run()
            {
                resendNotifyAction(order, 1);
            }

            @Override
            public String getDescption()
            {
                return "This is a asynchronous notify pay_result task!";
            }
        };

        ovExecutor.submitTask(ovTask);
    }
}
