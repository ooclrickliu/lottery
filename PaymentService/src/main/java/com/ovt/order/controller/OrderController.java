/**
 * OrderController.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * 2015年12月21日
 */
package com.ovt.order.controller;

import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ovt.common.model.JsonDocument;
import com.ovt.common.utils.CollectionUtils;
import com.ovt.common.utils.DataConvertUtils;
import com.ovt.common.utils.StringUtils;
import com.ovt.order.controller.response.PaymentServiceAPIResult;
import com.ovt.order.dao.constant.OrderState;
import com.ovt.order.dao.vo.AppAliPaymentLog;
import com.ovt.order.dao.vo.Order;
import com.ovt.order.dao.vo.PageInfo;
import com.ovt.order.dao.vo.RefundRequest;
import com.ovt.order.service.OrderService;
import com.ovt.order.service.PaymentLogService;
import com.ovt.order.service.RefundService;
import com.ovt.order.service.exception.ServiceErrorCode;
import com.ovt.order.service.exception.ServiceException;

/**
 * OrderController
 * 
 * @Author brad.zhou
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */

@Controller
@RequestMapping("/orders")
public class OrderController
{
    @Autowired
    private OrderService orderService;

    @Autowired
    private RefundService refundService;

    @Autowired
    private PaymentLogService paymentLogService;

    @RequestMapping(method = RequestMethod.POST, value = "/create")
    @ResponseBody
    public JsonDocument createOrder(@RequestBody Order order)
            throws ServiceException, UnsupportedEncodingException
    {
        Order orderResult = null;
        if (order != null)
        {
            orderResult = orderService.createOrder(order);
        }
        else
        {
            String message = "The order object is null!";
            throw new ServiceException(ServiceErrorCode.ORDER_IS_NULL, message);
        }

        return new PaymentServiceAPIResult(orderResult);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/list")
    @ResponseBody
    public JsonDocument queryOrderList(@RequestParam String userId)
            throws ServiceException
    {

        List<Order> orders = orderService.queryOrderListByUserId(userId);

        return new PaymentServiceAPIResult(orders);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/list/timeScope/paid")
    @ResponseBody
    public JsonDocument queryPaidOrderListByTimeScope(
            @RequestParam String startTime, @RequestParam String endTime)
            throws ServiceException
    {

        List<Order> orders = orderService.queryPaidOrderListByTimeScope(
                startTime, endTime);

        return new PaymentServiceAPIResult(orders);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/list/paid/all")
    @ResponseBody
    public JsonDocument queryAllPaidOrderListOrderByPayTime()
            throws ServiceException
    {

        List<Order> orders = orderService.queryAllPaidOrderListOrderByPayTime();

        return new PaymentServiceAPIResult(orders);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/list/timeScope/refunded")
    @ResponseBody
    public JsonDocument queryRefundedOrderListByTimeScope(
            @RequestParam String startTime, @RequestParam String endTime)
            throws ServiceException
    {
        List<Order> orders = orderService.queryRefundedOrderListByTimeScope(
                startTime, endTime);

        return new PaymentServiceAPIResult(orders);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/list/refunded/all")
    @ResponseBody
    public JsonDocument queryAllRefundedOrderListOrderByRefundTime()
            throws ServiceException
    {
        List<Order> orders = orderService
                .queryAllRefundedOrderListOrderByRefundTime();

        return new PaymentServiceAPIResult(orders);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/list/adminold")
    @ResponseBody
    public JsonDocument queryOrderListAdminOld(
            @RequestParam String orderState,
            @RequestParam(required = false, defaultValue = "0") String page,
            @RequestParam(required = false, defaultValue = "10000") String limit,
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String order)
            throws ServiceException
    {
        PageInfo pageInfo = new PageInfo(DataConvertUtils.toInt(page),
                DataConvertUtils.toInt(limit), sortBy, order);

        List<Order> orders = orderService.queryOrderListByState(pageInfo,
                orderState);

        return new PaymentServiceAPIResult(orders);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/list/admin")
    @ResponseBody
    public JsonDocument queryOrderListAdmin(
            @RequestBody Map<String, String> query,
            @RequestParam(required = false, defaultValue = "0") String page,
            @RequestParam(required = false, defaultValue = "10000") String limit,
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String order)
            throws ServiceException
    {
        PageInfo pageInfo = new PageInfo(DataConvertUtils.toInt(page),
                DataConvertUtils.toInt(limit), sortBy, order);

        List<Order> orders = orderService.queryOrderList(pageInfo, query);

        return new PaymentServiceAPIResult(orders);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/refundableOrders")
    @ResponseBody
    public JsonDocument getRefundableOrders(@RequestParam String userId)
            throws ServiceException
    {

        List<Order> orders = orderService.queryRefundableOrders(userId);

        return new PaymentServiceAPIResult(orders);

    }

    @RequestMapping(method = RequestMethod.GET, value = "/unpaidList")
    @ResponseBody
    public JsonDocument getUnPaidOrderList(@RequestParam String userId,
            @RequestParam String createBy, @RequestParam boolean queryItem)
            throws ServiceException
    {

        List<Order> orders = orderService.queryUnPaidOrderList(userId,
                createBy, queryItem);

        return new PaymentServiceAPIResult(orders);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/unpaidNum")
    @ResponseBody
    public JsonDocument getUnPaidOrderNum(@RequestParam String userId,
            @RequestParam String createBy) throws ServiceException
    {

        int orderNum = orderService.queryUnPaidOrderNum(userId, createBy);

        return new PaymentServiceAPIResult(orderNum);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/detail")
    @ResponseBody
    public JsonDocument queryOrderInfo(@RequestParam String orderNo)
            throws ServiceException
    {
        Order order = orderService.queryOrderInfo(orderNo, true);

        return new PaymentServiceAPIResult(order);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/cancel")
    @ResponseBody
    public JsonDocument cancelOrder(@RequestParam String userId,
            @RequestParam String orderNo) throws ServiceException
    {
        boolean result = orderService.cancelOrder(userId, orderNo);

        return new PaymentServiceAPIResult(result);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/delete")
    @ResponseBody
    public JsonDocument deleteOrder(@RequestParam String userId,
            @RequestParam String orderNo) throws ServiceException
    {
        boolean result = orderService.deleteOrder(userId, orderNo);

        return new PaymentServiceAPIResult(result);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/app/alinotify")
    @ResponseBody
    public JsonDocument appAliNotify(
            @RequestBody Map<String, String[]> parameterMap)
            throws ServiceException
    {
        AppAliPaymentLog appAliPaymentLog = paymentLogService
                .saveAppAlipaymentLog(parameterMap);

        Order order = orderService.appAliNotify(appAliPaymentLog);

        return new PaymentServiceAPIResult(order);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/applyRefund")
    @ResponseBody
    public JsonDocument applyForRefund(@RequestParam String userId,
            @RequestBody RefundRequest refundRequest) throws ServiceException
    {
        RefundRequest result = null;

        if (refundRequest != null)
        {
            String orderNo = refundRequest.getOrderNo();
            String refundReason = refundRequest.getRefundReason();
            float refundFee = refundRequest.getRefundFee();
            String refundDesc = refundRequest.getRefundDesc();
            String createBy = refundRequest.getCreateBy();
            result = orderService.applyForRefund(userId, orderNo, refundReason,
                    refundFee, refundDesc, createBy);
        }
        else
        {
            String message = "The RefundRequest object is null!";
            throw new ServiceException(ServiceErrorCode.REGUND_REQUEST_IS_NULL,
                    message);
        }

        return new PaymentServiceAPIResult(result);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/repostRefund")
    @ResponseBody
    public JsonDocument repostRefundRequest(@RequestParam long requestId,
            float fee) throws ServiceException
    {
        List<Long> requestIdList = Arrays.asList(requestId);

        List<RefundRequest> refundRequestList = orderService
                .queryRefundRequestList(requestIdList);
        if (CollectionUtils.isEmpty(refundRequestList))
        {
            String errMsg = MessageFormat.format(
                    "Fail to query refund request list! requestId:{0}",
                    requestId);
            throw new ServiceException(
                    ServiceErrorCode.QUERY_REFUND_REQUESTS_ERROR, errMsg);
        }

        RefundRequest refundRequest = refundRequestList.get(0);
        if (!refundRequest.getRefundState().equals(OrderState.REFUND_FAILED))
        {
            String errMsg = MessageFormat.format(
                    "Fail to repost refund requestId:{0}, refund state is {1}",
                    requestId, refundRequest.getRefundState());
            throw new ServiceException(
                    ServiceErrorCode.REPOST_REFUND_REQUEST_ERROR, errMsg);
        }

        refundRequest.setBatchNo(StringUtils.BLANK);
        refundRequest.setRefundLog(StringUtils.BLANK);
        refundRequest.setRefundFee(fee);
        refundRequest.setRefundState(OrderState.REFUND_APPLYING);
        orderService.updateRefundRequest(refundRequest);

        return PaymentServiceAPIResult.SUCCESS;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/applyRefundList")
    @ResponseBody
    public JsonDocument applyForRefund(@RequestParam String userId,
            @RequestBody List<RefundRequest> refundRequests)
            throws ServiceException
    {

        List<RefundRequest> result = orderService.applyForRefund(userId,
                refundRequests);

        return new PaymentServiceAPIResult(result);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/revokeRefund")
    @ResponseBody
    public JsonDocument revokeRefund(@RequestBody List<String> orderNos)
            throws ServiceException
    {

        List<Long> applyingIds = orderService.revokeRefund(orderNos);

        return new PaymentServiceAPIResult(applyingIds);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/specialRevokeRefund")
    @ResponseBody
    public JsonDocument revokeRefund(@RequestParam String userId)
            throws ServiceException
    {
        orderService.revokeRefund(userId);

        return new PaymentServiceAPIResult();
    }

    /*
     * @RequestMapping(method = RequestMethod.GET, value = "/refundList")
     * 
     * @ResponseBody public JsonDocument queryRefundRequestList() throws
     * ServiceException { List<RefundRequest> refundRequests = orderService
     * .queryRefundRequestList();
     * 
     * return new PaymentServiceAPIResult(refundRequests); }
     */

    @RequestMapping(method = RequestMethod.GET, value = "/refundList")
    @ResponseBody
    public JsonDocument queryRefundRequestListByState(
            @RequestParam String requestState,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10000") int limit,
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String order)
            throws ServiceException
    {
        PageInfo pageInfo = new PageInfo(page, limit, sortBy, order);
        List<RefundRequest> refundRequests = orderService
                .queryRefundRequestList(pageInfo, requestState);

        return new PaymentServiceAPIResult(refundRequests);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/refundList2")
    @ResponseBody
    public JsonDocument queryRefundRequestListByState(
            @RequestParam String orderNo, @RequestParam String requestState,
            @RequestParam String startTime, @RequestParam String endTime,
            @RequestBody PageInfo pageInfo) throws ServiceException
    {
        List<RefundRequest> refundRequests = orderService
                .queryRefundRequestList(pageInfo, orderNo, requestState,
                        startTime, endTime);

        return new PaymentServiceAPIResult(refundRequests);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/applyingRefunds")
    @ResponseBody
    public JsonDocument queryApplyingRefundRequestList(
            @RequestParam String orderNo) throws ServiceException
    {
        List<RefundRequest> refundRequests = orderService
                .queryApplyingRefundRequest(orderNo);

        return new PaymentServiceAPIResult(refundRequests);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/nonProcessRefundList")
    @ResponseBody
    public JsonDocument queryNonProcessRefundRequestList(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10000") int limit,
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String order)
            throws ServiceException
    {
        PageInfo pageInfo = new PageInfo(page, limit, sortBy, order);
        List<RefundRequest> refundRequests = orderService
                .queryNonProcessRefundRequestList(pageInfo);

        return new PaymentServiceAPIResult(refundRequests);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/processedRefundList")
    @ResponseBody
    public JsonDocument queryProcessedRefundRequestList(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10000") int limit,
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String order)
            throws ServiceException
    {
        PageInfo pageInfo = new PageInfo(page, limit, sortBy, order);
        List<RefundRequest> refundRequests = orderService
                .queryProcessedRefundRequestList(pageInfo);

        return new PaymentServiceAPIResult(refundRequests);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/refundDetail")
    @ResponseBody
    public JsonDocument queryRefundRequest(@RequestParam String orderNo)
            throws ServiceException
    {
        List<RefundRequest> refundRequests = orderService
                .queryRefundRequest(orderNo);

        return new PaymentServiceAPIResult(refundRequests);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/refundState")
    @ResponseBody
    public JsonDocument queryRefundRequestState(@RequestParam String orderNo)
            throws ServiceException
    {
        final OrderState refundRequestState = orderService
                .queryRefundRequestState(orderNo);

        return new PaymentServiceAPIResult(refundRequestState);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/transferList")
    @ResponseBody
    public JsonDocument queryTransferList()
    {
        return null;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/refundListByIds")
    @ResponseBody
    public JsonDocument queryRefundRequestList(
            @RequestBody List<Long> refundRequestIds) throws ServiceException
    {
        List<RefundRequest> refundRequests = null;
        if ((refundRequestIds != null) && (refundRequestIds.size() > 0))
        {
            refundRequests = orderService
                    .queryRefundRequestList(refundRequestIds);
        }

        return new PaymentServiceAPIResult(refundRequests);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/doAgreeRefund")
    @ResponseBody
    public JsonDocument doAgreeRefundAction(
            @RequestBody List<Long> requestIdList, @RequestParam long auditorId)
            throws ServiceException
    {
        List<RefundRequest> refundRequests = orderService
                .queryRefundRequestList(requestIdList);

        List<RefundRequest> refundRequestRemoveTemps = new ArrayList<RefundRequest>();
        if (CollectionUtils.isNotEmpty(refundRequests))
        {
            for (RefundRequest refundRequest : refundRequests)
            {
                if (!refundRequest.getRefundState().equals(
                        OrderState.REFUND_APPLYING))
                {
                    refundRequestRemoveTemps.add(refundRequest);
                }
            }
        }
        refundRequests.removeAll(refundRequestRemoveTemps);

        String[] ret = refundService.doAliRefund(refundRequests);
        Object pageSource = ret[0];
        String batchNo = ret[1];

        orderService.updateRefundRequestListStateAndBatchNo(refundRequests,
                OrderState.REFUND_DOING, auditorId, batchNo);

        return new PaymentServiceAPIResult(pageSource);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/doAgreeRefund/repost")
    @ResponseBody
    public JsonDocument doAgreeRefundActionRepost(
            @RequestBody List<Long> requestIdList, @RequestParam long auditorId)
            throws ServiceException
    {
        List<RefundRequest> refundRequests = orderService
                .queryRefundRequestList(requestIdList);

        List<Long> requestIds = new ArrayList<Long>();
        if (CollectionUtils.isNotEmpty(refundRequests))
        {
            for (RefundRequest refundRequest : refundRequests)
            {
                if (refundRequest.getRefundState().equals(
                        OrderState.REFUND_DOING))
                {
                    requestIds.add(refundRequest.getId());
                }
            }
        }

        String[] ret = refundService.doAliRefundRepost(requestIds);
        Object pageSource = ret[0];
        String batchNo = ret[1];

        orderService.updateRefundRequestListStateAndBatchNo(refundRequests,
                OrderState.REFUND_DOING, auditorId, batchNo);

        return new PaymentServiceAPIResult(pageSource);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/doRejectRefund")
    @ResponseBody
    public JsonDocument doRejectRefundAction(@RequestParam long auditorId,
            @RequestBody List<Long> requestIds, @RequestParam String feedback)
            throws ServiceException
    {
        List<Long> requestIdList = new ArrayList<Long>();

        if (CollectionUtils.isNotEmpty(requestIds))
        {
            List<RefundRequest> requestList = orderService
                    .queryRefundRequestList(requestIds);
            for (RefundRequest refundRequest : requestList)
            {
                if (refundRequest.getRefundState().equals(
                        OrderState.REFUND_APPLYING))
                {
                    requestIdList.add(refundRequest.getId());
                }
            }
        }

        if (CollectionUtils.isNotEmpty(requestIdList))
        {
            orderService.setRefundRequestList(requestIdList,
                    OrderState.REFUND_REFUSED, auditorId, feedback);
        }

        return new PaymentServiceAPIResult(new Boolean(true));
    }

    @RequestMapping(method = RequestMethod.POST, value = "/isRefunding")
    @ResponseBody
    public JsonDocument isRefunding(@RequestParam String orderNo)
            throws ServiceException
    {
        boolean result = orderService.isRefunding(orderNo);

        return new PaymentServiceAPIResult(new Boolean(result));
    }

}
