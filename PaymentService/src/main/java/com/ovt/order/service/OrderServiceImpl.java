/**
 * OrderServiceImpl.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * 2015年12月21日
 */
package com.ovt.order.service;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ovt.common.exception.OVTRuntimeException;
import com.ovt.common.utils.CollectionUtils;
import com.ovt.common.utils.DataConvertUtils;
import com.ovt.common.utils.DateTimeUtils;
import com.ovt.common.utils.NumberGeneratorUtil;
import com.ovt.common.utils.StringUtils;
import com.ovt.order.dao.OrderDao;
import com.ovt.order.dao.RefundRequestDao;
import com.ovt.order.dao.constant.OrderQueryConstants;
import com.ovt.order.dao.constant.OrderState;
import com.ovt.order.dao.vo.AppAliPaymentLog;
import com.ovt.order.dao.vo.Order;
import com.ovt.order.dao.vo.PageInfo;
import com.ovt.order.dao.vo.Refund;
import com.ovt.order.dao.vo.RefundRequest;
import com.ovt.order.dao.vo.TransferRequest;
import com.ovt.order.service.exception.ServiceErrorCode;
import com.ovt.order.service.exception.ServiceException;
import com.ovt.order.service.utils.StateChecker;

/**
 * OrderServiceImpl
 * 
 * @Author brad.zhou
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
@Service
public class OrderServiceImpl implements OrderService
{
    @Autowired
    private OrderDao orderDao;

    @Autowired
    private RefundRequestDao refundRequestDao;

    @Autowired
    private AppPropertiesService appProperties;

    private static int ORDER_DELETE_FLAG_DELETE = 1;

    @Override
    public Order createOrder(Order order) throws ServiceException
    {
        this.checkOrder(order);

        order.setOrderNo(NumberGeneratorUtil.generateOrderNumber(order
                .getUserId()));
        order.setOrderState(OrderState.ORDER_WAIT_PAY);
        order.setIsDelete(0);

        try
        {
            orderDao.saveOrder(order);
        }
        catch (OVTRuntimeException e)
        {
            String message = MessageFormat.format("Failed to save order [{0}]",
                    order);
            throw new ServiceException(ServiceErrorCode.SAVE_ORDER_ERROR,
                    message, e);
        }

        return order;
    }

    /**
     * @param order
     * @throws ServiceException
     */
    private void checkOrder(Order order) throws ServiceException
    {
        if (order.getOrderTotalFee() < 0)
        {
            throw new ServiceException(ServiceErrorCode.NON_POSITIVE_TOTAL_FEE,
                    "The totalFee must >= 0!");
        }
    }

    @Override
    public Map<String, Order> getOrderMap(List<String> orderNos)
            throws ServiceException
    {
        Map<String, Order> orderMap = new HashMap<String, Order>();

        List<Order> orderList = null;
        try
        {
            orderList = orderDao.getOrders(orderNos);
        }
        catch (OVTRuntimeException e)
        {
            String message = MessageFormat.format(
                    "Failed to get all order list of user [{0}]",
                    StringUtils.getCSV(orderNos));
            throw new ServiceException(ServiceErrorCode.QUERY_ORDER_LIST_ERROR,
                    message, e);
        }

        if (CollectionUtils.isNotEmpty(orderList))
        {
            for (Order order : orderList)
            {
                orderMap.put(order.getOrderNo(), order);
            }
        }

        return orderMap;
    }

    @Override
    public List<Order> queryOrderListByUserId(String userId)
            throws ServiceException
    {
        List<Order> orderList = null;
        try
        {
            orderList = orderDao.getOrderListByUserId(userId);
        }
        catch (OVTRuntimeException e)
        {
            String message = MessageFormat.format(
                    "Failed to get all order list of user [{0}]", userId);
            throw new ServiceException(ServiceErrorCode.QUERY_ORDER_LIST_ERROR,
                    message, e);
        }

        return orderList;
    }

    @Override
    public List<Order> queryPaidOrderListByTimeScope(String startTime,
            String endTime) throws ServiceException
    {
        Timestamp startTimestamp = DateTimeUtils.toTimestamp(DateTimeUtils
                .parseDate(startTime, DateTimeUtils.PATTERN_SQL_DATETIME_FULL));
        Timestamp endTimestamp = DateTimeUtils.toTimestamp(DateTimeUtils
                .parseDate(endTime, DateTimeUtils.PATTERN_SQL_DATETIME_FULL));

        List<Order> orderList = null;
        try
        {
            orderList = orderDao.getPaidOrderListByTimeScope(startTimestamp,
                    endTimestamp);
        }
        catch (OVTRuntimeException e)
        {
            String message = MessageFormat
                    .format("Failed to get all paid order list that paid time between {0} and {1}",
                            DateTimeUtils.formatSqlDateTime(startTimestamp),
                            DateTimeUtils.formatSqlDateTime(endTimestamp));
            throw new ServiceException(ServiceErrorCode.QUERY_ORDER_LIST_ERROR,
                    message, e);
        }

        return orderList;
    }

    @Override
    public List<Order> queryAllPaidOrderListOrderByPayTime()
            throws ServiceException
    {
        List<Order> orderList = null;
        try
        {
            orderList = orderDao.getAllPaidOrderListOrderByPayTime();
        }
        catch (OVTRuntimeException e)
        {
            String message = "Failed to get all paid order list";
            throw new ServiceException(ServiceErrorCode.QUERY_ORDER_LIST_ERROR,
                    message, e);
        }

        return orderList;
    }

    @Override
    public List<Order> queryRefundedOrderListByTimeScope(String startTime,
            String endTime) throws ServiceException
    {
        Timestamp startTimestamp = DateTimeUtils.toTimestamp(DateTimeUtils
                .parseDate(startTime, DateTimeUtils.PATTERN_SQL_DATETIME_FULL));
        Timestamp endTimestamp = DateTimeUtils.toTimestamp(DateTimeUtils
                .parseDate(endTime, DateTimeUtils.PATTERN_SQL_DATETIME_FULL));

        List<Order> orderList = null;
        try
        {
            orderList = orderDao.getRefundedOrderListByTimeScope(
                    startTimestamp, endTimestamp);
        }
        catch (OVTRuntimeException e)
        {
            String message = MessageFormat
                    .format("Failed to get all refunded order list that paid time between {0} and {1}",
                            DateTimeUtils.formatSqlDateTime(startTimestamp),
                            DateTimeUtils.formatSqlDateTime(endTimestamp));
            throw new ServiceException(ServiceErrorCode.QUERY_ORDER_LIST_ERROR,
                    message, e);
        }

        return orderList;
    }

    @Override
    public List<Order> queryAllRefundedOrderListOrderByRefundTime()
            throws ServiceException
    {
        List<Order> orderList = null;
        try
        {
            orderList = orderDao.AllRefundedOrderListOrderByRefundTime();
        }
        catch (OVTRuntimeException e)
        {
            String message = "Failed to get all refunded order list";
            throw new ServiceException(ServiceErrorCode.QUERY_ORDER_LIST_ERROR,
                    message, e);
        }

        return orderList;
    }

    @Override
    public List<Order> queryOrderListByState(PageInfo pageInfo,
            String orderState) throws ServiceException
    {
        List<Order> orderList = null;
        try
        {
            orderList = orderDao.getOrderListByState(pageInfo, orderState);
        }
        catch (OVTRuntimeException e)
        {
            throw new ServiceException(ServiceErrorCode.QUERY_ORDER_LIST_ERROR,
                    "Failed to get order list", e);
        }

        return orderList;
    }

    @Override
    public List<Order> queryOrderList(PageInfo pageInfo,
            Map<String, String> query) throws ServiceException
    {
        String queryInfo = getQueryInfo(query);
        List<Order> orderList = null;
        try
        {
            orderList = orderDao.getOrderList(pageInfo, queryInfo);
        }
        catch (OVTRuntimeException e)
        {
            throw new ServiceException(ServiceErrorCode.QUERY_ORDER_LIST_ERROR,
                    "Failed to get order list", e);
        }

        return orderList;
    }

    private static String getQueryInfo(Map<String, String> query)
    {
        String queryInfo = StringUtils.BLANK;
        if (CollectionUtils.isNotEmpty(query))
        {
            for (String key : query.keySet())
            {
                if (OrderQueryConstants.queryFileds.contains(key))
                {
                    String value = query.get(key);
                    if (StringUtils.isNotBlank(value))
                    {
                        queryInfo += key + StringUtils.SQL_EQUAL + value
                                + StringUtils.SQL_AND;
                    }
                }
            }
            String startTime = query.get(OrderQueryConstants.startTime);
            if (StringUtils.isNotBlank(startTime))
            {
                String endTime = query.get(OrderQueryConstants.endTime);
                queryInfo += OrderQueryConstants.timeField + startTime
                        + StringUtils.SQL_AND + endTime + StringUtils.SQL_AND;
            }
        }
        return queryInfo;
    }

    @Override
    public List<Order> queryRefundableOrders(String userId)
            throws ServiceException
    {
        List<Order> orderList = null;
        try
        {
            orderList = orderDao.getRefundableOrders(userId);
        }
        catch (OVTRuntimeException e)
        {
            String message = MessageFormat
                    .format("Failed to get refundable order list of user [{0}]",
                            userId);
            throw new ServiceException(
                    ServiceErrorCode.QUERY_REFUNDABLE_ORDERS_ERROR, message, e);
        }

        return orderList;
    }

    @Override
    public Order queryOrderInfo(String orderNo, boolean queryItem)
            throws ServiceException
    {
        Order order = null;
        try
        {
            order = orderDao.getOrder(orderNo, queryItem);
            if (order == null)
            {
                throw new ServiceException(
                        ServiceErrorCode.INVALID_ORDER_NUMBER,
                        "Invalid orderNo!");
            }
        }
        catch (OVTRuntimeException e)
        {
            String message = MessageFormat.format(
                    "Failed to get detail info of order [{0}]", orderNo);
            throw new ServiceException(ServiceErrorCode.QUERY_ORDER_INFO_ERROR,
                    message, e);
        }

        return order;
    }

    @Override
    public Order queryOrderInfo(String userId, String orderNo)
            throws ServiceException
    {
        Order order = queryOrderInfo(orderNo, false);
        if (order != null)
        {
            if (!(order.getUserId().equals(userId)))
            {
                throw new ServiceException(
                        ServiceErrorCode.MISMATCH_USER_ID,
                        "Cannot find the correct order,"
                                + "the userId is mismatch between "
                                + "method argument and object that is query from DB!");
            }
        }

        return order;
    }

    @Override
    public boolean cancelOrder(String userId, String orderNo)
            throws ServiceException
    {
        boolean result = false;
        Order order = queryOrderInfo(userId, orderNo);
        if (order != null)
        {
            OrderState state = order.getOrderState();

            if (StateChecker.checkStateChangeValid(state,
                    OrderState.ORDER_CANCELED))
            {
                try
                {
                    result = orderDao.updateOrderState(order,
                            OrderState.ORDER_CANCELED);
                }
                catch (OVTRuntimeException e)
                {
                    String message = MessageFormat.format(
                            "Failed to update state of order [{0}]", orderNo);
                    throw new ServiceException(
                            ServiceErrorCode.UPDATE_ORDER_STATE_ERROR, message,
                            e);
                }

            }
            else
            {
                throw new ServiceException(
                        ServiceErrorCode.INVALID_ORDER_STATE,
                        "Invalid order state!");
            }
        }

        return result;
    }

    @Override
    public boolean deleteOrder(String userId, String orderNo)
            throws ServiceException
    {
        boolean result = false;
        Order order = queryOrderInfo(userId, orderNo);

        if (order != null)
        {
            OrderState state = order.getOrderState();
            if (StateChecker.checkStateChangeValid(state,
                    OrderState.ORDER_DELETE))
            {
                try
                {
                    result = orderDao.updateOrderDeleteFlag(order,
                            ORDER_DELETE_FLAG_DELETE);
                }
                catch (OVTRuntimeException e)
                {
                    String message = MessageFormat.format(
                            "Failed to update deleteFlag of order [{0}]",
                            orderNo);
                    throw new ServiceException(
                            ServiceErrorCode.UPDATE_ORDER_DELETE_FLAG_ERROR,
                            message, e);
                }

            }
            else
            {
                throw new ServiceException(
                        ServiceErrorCode.INVALID_ORDER_STATE,
                        "Invalid order state!");
            }

        }

        return result;
    }

    @Override
    public RefundRequest applyForRefund(String userId, String orderNo,
            String refundReason, float refundFee, String refundDesc,
            String createBy) throws ServiceException
    {
        RefundRequest requestResult = null;

        RefundRequest refundRequest = new RefundRequest();
        refundRequest.setOrderNo(orderNo);
        refundRequest.setRefundReason(refundReason);
        refundRequest.setRefundFee(refundFee);
        refundRequest.setRefundDesc(refundDesc);
        refundRequest.setRefundState(OrderState.REFUND_APPLYING);
        refundRequest.setCreateBy(createBy);

        checkRefundPermission(userId, refundRequest);

        try
        {
            requestResult = refundRequestDao.saveRefundRequest(refundRequest);
        }
        catch (OVTRuntimeException e)
        {
            String message = MessageFormat.format(
                    "Failed to insert refundRequest [{0}] into DB, [{0}]",
                    orderNo);
            throw new ServiceException(
                    ServiceErrorCode.SAVE_REFUND_REQUEST_ERROR, message, e);
        }

        return requestResult;
    }

    @Override
    public List<RefundRequest> queryRefundRequestList() throws ServiceException
    {
        List<RefundRequest> refundRequestList = null;
        try
        {
            refundRequestList = refundRequestDao.queryAllRefundRequestList();
        }
        catch (OVTRuntimeException e)
        {
            String message = "Failed to get all refundRequest list";
            throw new ServiceException(
                    ServiceErrorCode.QUERY_REFUND_REQUESTS_ERROR, message, e);
        }

        return refundRequestList;
    }

    @Override
    public List<TransferRequest> queryTransferRequestList()
            throws ServiceException
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<RefundRequest> queryRefundRequestList(List<Long> requestIdList)
            throws ServiceException
    {
        List<RefundRequest> refundRequestList = null;
        try
        {
            refundRequestList = refundRequestDao
                    .queryRefundRequestList(requestIdList);
        }
        catch (OVTRuntimeException e)
        {
            String message = "Failed to get refundRequest list";
            throw new ServiceException(
                    ServiceErrorCode.QUERY_REFUND_REQUESTS_ERROR, message, e);
        }

        return refundRequestList;
    }

    public OrderState setOrderState(Order order, OrderState state)
            throws ServiceException
    {
        try
        {
            orderDao.updateOrderState(order, state);
        }
        catch (OVTRuntimeException e)
        {
            String message = "Failed to update order state!";
            throw new ServiceException(
                    ServiceErrorCode.UPDATE_ORDER_STATE_ERROR, message, e);
        }

        return state;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.order.service.OrderService#putAuditorId(java.lang.String,
     * long)
     */
    @Override
    public void putAuditorId(String batchNo, long auditorId)
            throws ServiceException
    {
        try
        {
            refundRequestDao.updateAuditorId(batchNo, auditorId);
        }
        catch (OVTRuntimeException e)
        {
            String message = "Failed to update auditor id!";
            throw new ServiceException(
                    ServiceErrorCode.UPDATE_AUDITOR_ID_ERROR, message, e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.service.OrderService#setRefundRequestList(java.util.List,
     * com.ovt.dao.contant.OrderState, long)
     */
    @Override
    public OrderState updateRefundRequestListStateAndBatchNo(
            List<RefundRequest> requestList, OrderState state, long auditorId,
            String batchNo) throws ServiceException
    {
        List<Long> requestIdList = new ArrayList<Long>();
        if (CollectionUtils.isNotEmpty(requestList))
        {
            for (RefundRequest request : requestList)
            {
                requestIdList.add(request.getId());
            }

            try
            {
                refundRequestDao.updateRefundRequestList(requestIdList, state,
                        auditorId, batchNo);
            }
            catch (OVTRuntimeException e)
            {
                String message = "Failed to update refundRequest list!";
                throw new ServiceException(
                        ServiceErrorCode.UPDATE_REFUND_REQUESTS_ERROR, message,
                        e);
            }
        }

        return state;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.service.OrderService#setRefundRequestList(java.util.List,
     * com.ovt.dao.contant.OrderState, long, java.lang.String)
     */
    @Override
    public OrderState setRefundRequestList(List<Long> requestIdList,
            OrderState state, long auditorId, String feedback)
            throws ServiceException
    {
        try
        {
            refundRequestDao.updateRefundRequestListFeedback(requestIdList,
                    state, auditorId, feedback);
        }
        catch (OVTRuntimeException e)
        {
            String message = "Failed to update refundRequest list!";
            throw new ServiceException(
                    ServiceErrorCode.UPDATE_REFUND_REQUESTS_ERROR, message, e);
        }

        return state;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ovt.order.service.OrderService#queryRefundRequest(java.lang.String)
     */
    @Override
    public List<RefundRequest> queryRefundRequest(String orderNo)
            throws ServiceException
    {
        List<RefundRequest> refundRequests = null;
        try
        {
            refundRequests = refundRequestDao.queryRefundRequest(orderNo);
        }
        catch (OVTRuntimeException e)
        {
            String message = "Failed to get refundRequest!";
            throw new ServiceException(
                    ServiceErrorCode.QUERY_REFUND_REQUEST_ERROR, message, e);
        }

        return refundRequests;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ovt.order.service.OrderService#queryRefundRequestState(java.lang.
     * String)
     */
    @Override
    public OrderState queryRefundRequestState(String orderNo)
            throws ServiceException
    {
        OrderState refundRequestState = null;
        try
        {
            refundRequestState = refundRequestDao
                    .queryRefundRequestState(orderNo);
        }
        catch (OVTRuntimeException e)
        {
            String message = "Failed to get refundRequest!";
            throw new ServiceException(
                    ServiceErrorCode.QUERY_REFUND_REQUEST_ERROR, message, e);
        }

        return refundRequestState;
    }

    @Override
    public RefundRequest queryRefundDoneRequestByOrderNo(String orderNo)
            throws ServiceException
    {
        RefundRequest refundRequest = null;
        try
        {
            refundRequest = refundRequestDao
                    .queryRefundDoneRequestByOrderNo(orderNo);
        }
        catch (OVTRuntimeException e)
        {
            String message = "Failed to get refundRequest!";
            throw new ServiceException(
                    ServiceErrorCode.QUERY_REFUND_REQUEST_ERROR, message, e);
        }

        return refundRequest;
    }

    @Override
    public List<RefundRequest> queryRefundRequestList(PageInfo pageInfo,
            String requestState) throws ServiceException
    {
        List<RefundRequest> refundRequests = null;
        try
        {
            refundRequests = refundRequestDao.queryRefundRequestList(pageInfo,
                    requestState);
        }
        catch (OVTRuntimeException e)
        {
            String message = "Failed to get refundRequest!";
            throw new ServiceException(
                    ServiceErrorCode.QUERY_REFUND_REQUEST_ERROR, message, e);
        }

        return refundRequests;
    }

    @Override
    public List<RefundRequest> queryRefundRequestList(PageInfo pageInfo,
            String orderNo, String requestState, String startTime,
            String endTime) throws ServiceException
    {
        List<RefundRequest> refundRequests = null;
        try
        {
            refundRequests = refundRequestDao.queryRefundRequestList(pageInfo,
                    orderNo, requestState, startTime, endTime);
        }
        catch (OVTRuntimeException e)
        {
            String message = "Failed to get refundRequest!";
            throw new ServiceException(
                    ServiceErrorCode.QUERY_REFUND_REQUEST_ERROR, message, e);
        }

        return refundRequests;
    }

    @Override
    public List<RefundRequest> queryNonProcessRefundRequestList(
            PageInfo pageInfo) throws ServiceException
    {
        List<RefundRequest> refundRequests = null;
        try
        {
            refundRequests = refundRequestDao
                    .queryNonProcessRefundRequestList(pageInfo);
        }
        catch (OVTRuntimeException e)
        {
            String message = "Failed to get refundRequest list that are not processed!";
            throw new ServiceException(
                    ServiceErrorCode.QUERY_NON_PROCESS_REFUND_REQUESTS_ERROR,
                    message, e);
        }

        return refundRequests;
    }

    @Override
    public List<RefundRequest> queryProcessedRefundRequestList(PageInfo pageInfo)
            throws ServiceException
    {
        List<RefundRequest> refundRequests = null;
        try
        {
            refundRequests = refundRequestDao
                    .queryProcessedRefundRequestList(pageInfo);
        }
        catch (OVTRuntimeException e)
        {
            String message = "Failed to get refundRequest list that are processed!";
            throw new ServiceException(
                    ServiceErrorCode.QUERY_PROCESSED_REFUND_REQUESTS_ERROR,
                    message, e);
        }

        return refundRequests;
    }

    @Override
    public List<RefundRequest> queryAppliedRefundRequestList(String orderNo)
            throws ServiceException
    {
        List<RefundRequest> refundRequests = null;

        try
        {
            refundRequests = refundRequestDao
                    .queryAppliedRefundRequestList(orderNo);
        }
        catch (OVTRuntimeException e)
        {
            String message = "Failed to get refundRequest list that had applied!";
            throw new ServiceException(
                    ServiceErrorCode.QUERY_APPLIED_REFUND_REQUESTS_ERROR,
                    message, e);
        }

        return refundRequests;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.order.service.OrderService#applyForRefund(java.lang.String,
     * java.util.List)
     */
    @Override
    public List<RefundRequest> applyForRefund(String userId,
            List<RefundRequest> refundRequests) throws ServiceException
    {

        for (RefundRequest refundRequest : refundRequests)
        {
            checkRefundPermission(userId, refundRequest);
        }

        List<RefundRequest> refundRequestsResult = new ArrayList<RefundRequest>();
        for (RefundRequest refundRequest : refundRequests)
        {
            try
            {
                refundRequest
                        .setCreateTime(new Timestamp(new Date().getTime()));
                refundRequest.setRefundState(OrderState.REFUND_APPLYING);
                RefundRequest temp = refundRequestDao
                        .saveRefundRequest(refundRequest);
                refundRequestsResult.add(temp);
            }
            catch (OVTRuntimeException e)
            {
                String message = MessageFormat.format(
                        "Failed to insert refundRequest [{0}] into DB!",
                        refundRequest.getOrderNo());
                throw new ServiceException(
                        ServiceErrorCode.SAVE_REFUND_REQUEST_ERROR, message, e);

            }

        }

        return refundRequestsResult;
    }

    private void checkRefundPermission(String userId,
            RefundRequest refundRequest) throws ServiceException
    {
        Order order = queryOrderInfo(userId, refundRequest.getOrderNo());

        // 1.鉴权未通过
        if (order == null)
        {
            String message = MessageFormat
                    .format("The order(orderNo = {0}) is not belong to user(userId = {1})",
                            refundRequest.getOrderNo(), userId);

            throw new ServiceException(ServiceErrorCode.INVALID_ORDER_NUMBER,
                    message);
        }
        else
        {
            OrderState state = order.getOrderState();
            // 2.订单删除
            if (order.getIsDelete() == ORDER_DELETE_FLAG_DELETE)
            {
                String message = MessageFormat.format(
                        "The order had deleted, orderNo = {0}",
                        refundRequest.getOrderNo());

                throw new ServiceException(ServiceErrorCode.ORDER_IS_DELETE,
                        message);
            }

            // 3.订单状态不可转移
            if (!(StateChecker.checkStateChangeValid(state,
                    OrderState.REFUND_APPLYING)))
            {
                String message = MessageFormat
                        .format("The order state make order itself cannot be applied for refund: orderNo = {0}, orderState = {1} ",
                                refundRequest.getOrderNo(),
                                order.getOrderState());

                throw new ServiceException(
                        ServiceErrorCode.INVALID_ORDER_STATE, message);
            }

            // 4.已经申请的退款总金额大于订单总金额与此次申请退款金额之差
            if ((order.getOrderTotalFee() - refundRequest.getRefundFee()) < getRefundTotalFee(refundRequest
                    .getOrderNo()))
            {
                String message = MessageFormat
                        .format("The refund total fee is bigger than order total fee, orderNo = {0}, refundFee = {1}",
                                refundRequest.getOrderNo(),
                                refundRequest.getRefundFee());

                throw new ServiceException(ServiceErrorCode.REFUND_FEE_TO_MUCH,
                        message);
            }

        }
    }

    private float getRefundTotalFee(String orderNo) throws ServiceException
    {
        List<RefundRequest> refundRequests = queryAppliedRefundRequestList(orderNo);
        if ((refundRequests != null) && (refundRequests.size() == 0))
        {
            return 0;
        }

        float totalFee = 0;
        for (RefundRequest refundRequest : refundRequests)
        {
            totalFee += refundRequest.getRefundFee();
        }

        return totalFee;
    }

    @Override
    public List<Long> revokeRefund(List<String> orderNos)
            throws ServiceException
    {
        if (CollectionUtils.isEmpty(orderNos))
        {
            String message = "The order number list is empty!";

            throw new ServiceException(
                    ServiceErrorCode.ORDER_NUMBER_LIST_IS_EMPTY, message);
        }

        List<Long> applyingIds = new ArrayList<Long>(); // 保存状态为REFUND_APPLYING的refund_request记录的ID
        for (String orderNo : orderNos)
        {
            List<RefundRequest> refundRequests = null;
            try
            {
                refundRequests = refundRequestDao
                        .queryRefundingRequest(orderNo);
            }
            catch (OVTRuntimeException e)
            {
                String message = "Failed to query refunding request !";
                throw new ServiceException(
                        ServiceErrorCode.QUERY_REFUND_REQUESTS_ERROR, message,
                        e);
            }

            if ((refundRequests == null) || (refundRequests.size() == 0))
            {
                String message = MessageFormat
                        .format("The order is not applying for refund, so you cannot execute revoke_refund action, orderNo = {0}",
                                orderNo);
                throw new ServiceException(
                        ServiceErrorCode.INVALID_ORDER_STATE, message);
            }
            for (RefundRequest refundRequest : refundRequests)
            {
                if (StateChecker.checkStateChangeValid(
                        refundRequest.getRefundState(),
                        OrderState.REFUND_CANCELED))
                {
                    applyingIds.add(refundRequest.getId());
                }
            }
        }

        try
        {
            refundRequestDao.updateRefundRequestListState(applyingIds,
                    OrderState.REFUND_CANCELED);
        }
        catch (OVTRuntimeException e)
        {
            String message = "Failed to update refundRequest list's state!";
            throw new ServiceException(
                    ServiceErrorCode.UPDATE_REFUND_REQUESTS_STATE_ERROR,
                    message, e);
        }

        return applyingIds;
    }

    @Override
    public void revokeRefund(String userId) throws ServiceException
    {
        if (StringUtils.isBlank(userId))
        {
            String message = "The user id is blank!";

            throw new ServiceException(ServiceErrorCode.USER_ID_IS_BLANK,
                    message);
        }

        try
        {
            refundRequestDao.updateRefundRequestState(userId,
                    OrderState.REFUND_CANCELED);
        }
        catch (OVTRuntimeException e)
        {
            String message = "Failed to update refundRequest list's state!";
            throw new ServiceException(
                    ServiceErrorCode.UPDATE_REFUND_REQUESTS_STATE_ERROR,
                    message, e);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.order.service.OrderService#isRefunding(java.lang.String)
     */
    @Override
    public boolean isRefunding(String orderNo) throws ServiceException
    {
        boolean result = false;
        try
        {
            result = refundRequestDao.isRefunding(orderNo);
        }
        catch (OVTRuntimeException e)
        {
            String message = "Failed to judge whether the order is being applied for refund!";
            throw new ServiceException(
                    ServiceErrorCode.JUDGE_ORDER_REFUNDED_ERROR, message, e);
        }

        return result;
    }

    @Override
    public List<Order> queryUnPaidOrderList(String userId, String createBy,
            boolean queryItem) throws ServiceException
    {
        List<Order> unPaidOrders = null;
        try
        {
            unPaidOrders = orderDao.getUnPaidOrderList(userId, createBy,
                    queryItem);
        }
        catch (OVTRuntimeException e)
        {
            String message = "Failed to get the unpaid order list!";
            throw new ServiceException(
                    ServiceErrorCode.GET_UNPAID_ORDERS_ERROR, message, e);
        }

        return unPaidOrders;
    }

    @Override
    public int queryUnPaidOrderNum(String userId, String createBy)
            throws ServiceException
    {
        int unPaidOrderNum = 0;
        try
        {
            unPaidOrderNum = orderDao.getUnPaidOrderNum(userId, createBy);
        }
        catch (OVTRuntimeException e)
        {
            String message = "Failed to get the unpaid order number!";
            throw new ServiceException(
                    ServiceErrorCode.GET_UNPAID_ORDERS_ERROR, message, e);
        }

        return unPaidOrderNum;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ovt.order.service.OrderService#queryApplyingRefundRequest(java.lang
     * .String)
     */
    @Override
    public List<RefundRequest> queryApplyingRefundRequest(String orderNo)
            throws ServiceException
    {
        List<RefundRequest> applyingRefundRequest = null;
        try
        {
            applyingRefundRequest = refundRequestDao
                    .queryRefundingRequest(orderNo);
        }
        catch (OVTRuntimeException e)
        {
            String message = "Failed to get the applying refund request list!";
            throw new ServiceException(
                    ServiceErrorCode.QUERY_APPLYING_REFUND_REQUESTS_ERROR,
                    message, e);
        }

        return applyingRefundRequest;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ovt.order.service.OrderService#refundNotifyCallBack(java.util.Map,
     * java.util.List)
     */
    @Override
    public void refundNotifyCallBack(List<Refund> refundSuccessList,
            List<Refund> refundFailedList) throws ServiceException
    {
        if (CollectionUtils.isNotEmpty(refundSuccessList))
        {
            List<Long> idList = new ArrayList<Long>();

            Map<Long, Float> refundFeeMap = new HashMap<Long, Float>();
            for (Refund refund : refundSuccessList)
            {
                idList.add(refund.getRefundRequestId());
                if (refund.getRefundFee() > 0)
                {
                    refundFeeMap.put(refund.getRefundRequestId(),
                            refund.getRefundFee());
                }
                else
                {
                    String errMsg = MessageFormat
                            .format("Refund fee must be a positive value, not refundFee = {0} in refundRequestId = {1}!",
                                    refund.getRefundFee(),
                                    refund.getRefundRequestId());
                    throw new ServiceException(
                            ServiceErrorCode.NON_POSITIVE_REFUND_FEE, errMsg);
                }
            }

            try
            {
                refundRequestDao.updateOrderRefundedFee(refundFeeMap);
            }
            catch (OVTRuntimeException e)
            {
                String message = "Failed to update order's refunded fee!";
                throw new ServiceException(
                        ServiceErrorCode.UPDATE_ORDER_REFUNDED_FEE_ERROR,
                        message, e);
            }

            try
            {
                refundRequestDao.updateRefundRequestListState(idList,
                        OrderState.REFUND_DONE);
            }
            catch (OVTRuntimeException e)
            {
                String message = "Failed to update refundRequest list's state!";
                throw new ServiceException(
                        ServiceErrorCode.UPDATE_REFUND_REQUESTS_STATE_ERROR,
                        message, e);
            }

        }

        if (CollectionUtils.isNotEmpty(refundFailedList))
        {
            Map<Long, String> refundRequetIdStateMap = new HashMap<Long, String>();
            for (Refund refund : refundFailedList)
            {
                refundRequetIdStateMap.put(refund.getRefundRequestId(),
                        refund.getRefundState());
            }

            try
            {
                refundRequestDao
                        .updateRefundRequestListFailResult(refundRequetIdStateMap);
            }
            catch (OVTRuntimeException e)
            {
                String message = "Failed to update refundRequest list's state!";
                throw new ServiceException(
                        ServiceErrorCode.UPDATE_REFUND_REQUESTS_STATE_ERROR,
                        message, e);
            }
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.order.service.OrderService#cleanExpiredUnpaidOrders()
     */
    @Override
    public void cleanExpiredUnpaidOrders() throws ServiceException
    {
        Date previousDate = null;
        previousDate = DateTimeUtils.addDays(new Date(),
                -DataConvertUtils.toInt(appProperties.getAlipayPayDeadline()));
        Timestamp comparedTime = DateTimeUtils.toTimestamp(previousDate);

        orderDao.cleanExpiredUnpaidOrders(comparedTime);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ovt.order.service.OrderService#queryRefundRequestListByBatchNo(java
     * .lang.String)
     */
    @Override
    public List<RefundRequest> queryRefundRequestListByBatchNo(String batchNo)
    {
        return null;
    }

    @Override
    public void updateRefundRequest(RefundRequest refundRequest)
            throws ServiceException
    {
        try
        {
            refundRequestDao.updateRefundRequest(refundRequest);
        }
        catch (OVTRuntimeException e)
        {
            String message = "Failed to update refundRequest!";
            throw new ServiceException(
                    ServiceErrorCode.UPDATE_REFUND_REQUESTS_ERROR, message, e);
        }
    }

    @Override
    public Order appAliNotify(AppAliPaymentLog appAliPaymentLog)
            throws ServiceException
    {
        Order order = null;
        if (appAliPaymentLog.getResultStatus().equalsIgnoreCase(
                AppAliPaymentLog.SUCCESS_STATUS)
                && appAliPaymentLog.getSuccess().equalsIgnoreCase(
                        AppAliPaymentLog.TRUE))
        {
            String orderNo = appAliPaymentLog.getOutTradeNo();
            order = queryOrderInfo(orderNo, false);
            if (order != null)
            {
                OrderState state = order.getOrderState();

                if (StateChecker.checkStateChangeValid(
                        OrderState.ORDER_WAIT_ALINOTIFY, state))
                {
                    return order;
                }

                if (StateChecker.checkStateChangeValid(state,
                        OrderState.ORDER_WAIT_ALINOTIFY))
                {
                    try
                    {
                        orderDao.updateOrderStateOfAppNotify(order,
                                OrderState.ORDER_WAIT_ALINOTIFY);

                        order = queryOrderInfo(orderNo, false);

                    }
                    catch (OVTRuntimeException e)
                    {
                        String message = MessageFormat.format(
                                "Failed to update state of order [{0}]",
                                orderNo);
                        throw new ServiceException(
                                ServiceErrorCode.UPDATE_ORDER_STATE_ERROR,
                                message, e);
                    }
                }
                else
                {
                    throw new ServiceException(
                            ServiceErrorCode.INVALID_ORDER_STATE,
                            "Invalid order state!");
                }
            }
        }

        return order;
    }

}
