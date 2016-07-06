/**
 * RefundController.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * 2015年12月22日
 */
package com.ovt.order.controller;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ovt.common.exception.OVTException;
import com.ovt.common.log.Logger;
import com.ovt.common.log.LoggerFactory;
import com.ovt.common.model.JsonDocument;
import com.ovt.common.utils.CollectionUtils;
import com.ovt.common.utils.JsonUtils;
import com.ovt.order.controller.response.PaymentServiceAPIResult;
import com.ovt.order.dao.constant.DBConstants.TABLES.ALI_REFUND_LOG;
import com.ovt.order.dao.constant.LoggerConstants;
import com.ovt.order.dao.constant.PayConstants.REFUND_STATUS;
import com.ovt.order.dao.threadpool.OVTask;
import com.ovt.order.dao.threadpool.OVThreadPoolExecutor;
import com.ovt.order.dao.vo.AliRefundLog;
import com.ovt.order.dao.vo.PageInfo;
import com.ovt.order.dao.vo.Refund;
import com.ovt.order.dao.vo.RefundRequest;
import com.ovt.order.service.OrderService;
import com.ovt.order.service.RefundLogService;
import com.ovt.order.service.RefundService;
import com.ovt.order.service.alipay.AlipayNotify;
import com.ovt.order.service.exception.ServiceErrorCode;
import com.ovt.order.service.exception.ServiceException;
import com.ovt.order.service.remote.RemoteServicesImpl;

/**
 * RefundController
 * 
 * @Author leo.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
@Controller
@RequestMapping("/refund")
public class RefundController
{
    @Autowired
    private RefundService refundService;

    @Autowired
    private RefundLogService refundLogService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private RemoteServicesImpl remoteServices;

    @Autowired
    private OVThreadPoolExecutor ovExecutor;

    private static final Logger LOGGER = LoggerFactory
            .getLogger(LoggerConstants.ALIPAY_NOTIFY_LOGGER);

    private static final Long DELAY_TIME = 3000L;

    private static final int MAX_COUNT = 4;

    private static final String SUCCESS = "success";

    private static final String FAILURE = "failure";

    @RequestMapping(method = RequestMethod.POST, value = "/alinotify")
    @ResponseBody
    public String aliNotify(HttpServletRequest request)
            throws ServiceException, IOException
    {
        LOGGER.info("aliRefundNotify: {}", this.paramToString(request));
        Map<String, String> params = AlipayNotify.processNotifyParams(request
                .getParameterMap());

        if (AlipayNotify.verify(params))
        {
            Map<String, Refund> tradeNoRefundsMap = refundService
                    .getTradeNoRefundsMapByBatchNo(params
                            .get(ALI_REFUND_LOG.BATCH_NO));

            // check notify params
            checkAlipayRefundNotifyParams(params, tradeNoRefundsMap.keySet()
                    .size());

            // save alipay refund log
            long refundLogId = refundLogService.saveAliRefundLog(params);

            // update refunds
            List<Refund> refundSuccessList = new ArrayList<Refund>();
            List<Long> refundSuccessRequestIds = new ArrayList<Long>();
            List<Refund> refundFailedList = new ArrayList<Refund>();
            List<Refund> refundUpdateList = new ArrayList<Refund>();
            List<Refund> refunds = refundService
                    .parseAlipayRefundNotifyParams(params);
            if (CollectionUtils.isNotEmpty(refunds))
            {
                for (Refund refund : refunds)
                {
                    if (!tradeNoRefundsMap.containsKey(refund.getTradeNo()))
                    {
                        throw new ServiceException(
                                ServiceErrorCode.INVALID_REFUND_NOTIFY_PARAMS,
                                "Invalid alipay tradeNo: "
                                        + refund.getTradeNo());
                    }

                    Refund tempRefund = tradeNoRefundsMap.get(refund
                            .getTradeNo());
                    if (tempRefund.getRefundState().equals(
                            REFUND_STATUS.APPLYING))
                    {
                        refund.setRefundRequestId(tempRefund
                                .getRefundRequestId());
                        refund.setRefundLogId(refundLogId);

                        if (refund.getRefundState().equals(
                                REFUND_STATUS.SUCCESS))
                        {
                            refundSuccessList.add(refund);
                            refundSuccessRequestIds.add(tempRefund
                                    .getRefundRequestId());
                        }
                        else
                        {
                            refundFailedList.add(refund);
                        }
                        refundUpdateList.add(refund);
                    }
                }
            }

            refundService.updateRefunds(refundUpdateList);

            // update refund request state
            orderService.refundNotifyCallBack(refundSuccessList,
                    refundFailedList);

            asyncNotifyRefundSuccess(refundSuccessRequestIds);

            return JsonDocument.STATE_SUCCESS;
        }
        else
        {
            return JsonDocument.STATE_FAILED;
        }

    }

    private String paramToString(HttpServletRequest request)
    {
        String input = "";
        try
        {
            input = JsonUtils.toJson(request.getParameterMap());
        }
        catch (OVTException e)
        {
            input = request.toString();
        }
        return input;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/list")
    @ResponseBody
    public JsonDocument getRefundList(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10000") int limit,
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String order)
            throws ServiceException
    {
        PageInfo pageInfo = new PageInfo(page, limit, sortBy, order);
        List<Refund> refundList = refundService.getRefundList(pageInfo);

        return new PaymentServiceAPIResult(refundList);
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/list/orderby")
    @ResponseBody
    public JsonDocument getRefundListOrderByRefundTime()
            throws ServiceException
    {
        List<Refund> refundList = refundService.getRefundListOrderByRefundTime();

        return new PaymentServiceAPIResult(refundList);
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/list/byTime")
    @ResponseBody
    public JsonDocument getRefundListByTime(
            @RequestParam String startTime,
            @RequestParam String endTime,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10000") int limit,
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String order)
            throws ServiceException
    {
        PageInfo pageInfo = new PageInfo(page, limit, sortBy, order);
        List<Refund> refundList = refundService.getRefundListByTime(pageInfo, startTime, endTime);

        return new PaymentServiceAPIResult(refundList);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/list/timeScope")
    @ResponseBody
    public JsonDocument getRefundListByTimeScope(@RequestParam String startTime,
            @RequestParam String endTime) throws ServiceException
    {
        List<Refund> paymentList = refundService.getRefundListByTimeScope(startTime,
                endTime);

        return new PaymentServiceAPIResult(paymentList);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/detail")
    @ResponseBody
    public JsonDocument getRefund(@RequestParam String orderNo)
            throws ServiceException
    {
        RefundRequest refundRequest = orderService
                .queryRefundDoneRequestByOrderNo(orderNo);
        if (refundRequest == null)
        {
            String errMsg = MessageFormat.format(
                    "Failed to query order by orderNo {0}!", orderNo);
            throw new ServiceException(ServiceErrorCode.INVALID_ORDER_NUMBER,
                    errMsg);
        }

        Refund refund = refundService.getRefund(refundRequest.getId());

        return new PaymentServiceAPIResult(refund);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/log/list")
    @ResponseBody
    public JsonDocument getAliRefundLogList() throws ServiceException
    {
        List<AliRefundLog> aliRefundLogList = refundLogService
                .getAliRefundLogList();

        return new PaymentServiceAPIResult(aliRefundLogList);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/log/detail")
    @ResponseBody
    public JsonDocument getAliRefundLog(@RequestParam long logId)
            throws ServiceException
    {
        AliRefundLog aliRefundLog = refundLogService.getAliRefundLog(logId);

        return new PaymentServiceAPIResult(aliRefundLog);
    }

    private void checkAlipayRefundNotifyParams(Map<String, String> params,
            int totalRefundNumofBatch) throws ServiceException
    {
        if (!params.containsKey(ALI_REFUND_LOG.SUCCESS_NUM))
        {
            throw new ServiceException(
                    ServiceErrorCode.INVALID_REFUND_NOTIFY_PARAMS,
                    "Invalid alipay refund notify params");
        }

        long successNum = Long.valueOf(params.get(ALI_REFUND_LOG.SUCCESS_NUM));

        if (totalRefundNumofBatch <= 0)
        {
            throw new ServiceException(
                    ServiceErrorCode.INVALID_REFUND_BATCH_NO,
                    "Invalid alipay refund batch no");
        }

        if (successNum > totalRefundNumofBatch || successNum < 0)
        {
            throw new ServiceException(
                    ServiceErrorCode.INVALID_REFUND_SUCCESS_NUM,
                    "Invalid alipay refund success num");
        }
    }

    private String resendNotifyAction(Collection<Long> refundSuccessRequestIds,
            int resendCount)
    {
        String errorMsg = new StringBuffer()
                .append("The refund asynchronous notify task has failed ")
                .append(resendCount).append(" times!").toString();
        try
        {
            JsonDocument result = remoteServices
                    .notifyDoorbellServiceForRefundSuccess(refundSuccessRequestIds);
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
                return resendNotifyAction(refundSuccessRequestIds,
                        ++resendCount);
            }
        }
        /**
         * Doorbell Server has crashed.
         **/
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
                return resendNotifyAction(refundSuccessRequestIds,
                        ++resendCount);
            }
        }

    }

    private void asyncNotifyRefundSuccess(
            final Collection<Long> refundSuccessRequestIds)
    {
        OVTask ovTask = new OVTask()
        {
            @Override
            public void run()
            {
                resendNotifyAction(refundSuccessRequestIds, 1);
            }

            @Override
            public String getDescption()
            {
                return "This is a asynchronous notify refund_result task!";
            }
        };

        ovExecutor.submitTask(ovTask);
    }
}
