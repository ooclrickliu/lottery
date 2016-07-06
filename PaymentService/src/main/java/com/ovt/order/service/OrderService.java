/**
 * OrderService.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * 2015年12月21日
 */
package com.ovt.order.service;

import java.util.List;
import java.util.Map;

import com.ovt.order.dao.constant.OrderState;
import com.ovt.order.dao.vo.AppAliPaymentLog;
import com.ovt.order.dao.vo.Order;
import com.ovt.order.dao.vo.PageInfo;
import com.ovt.order.dao.vo.Refund;
import com.ovt.order.dao.vo.RefundRequest;
import com.ovt.order.dao.vo.TransferRequest;
import com.ovt.order.service.exception.ServiceException;

/**
 * OrderService
 * 
 * @Author brad.zhou
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public interface OrderService
{
    /**
     * Register new order.
     * 
     * @param userId
     * @param totalFee
     * @param productList
     * @return Order
     * @throws ServiceException
     */
    public Order createOrder(Order order) throws ServiceException;

    /**
     * Get order list by userId.
     * 
     * @param userId
     * @return List<Order>
     * @throws ServiceException
     */
    public List<Order> queryOrderListByUserId(String userId)
            throws ServiceException;

    /**
     * Get paid order list by time scope.
     * 
     * @param startTime
     * @param endTime
     * @return List<Order>
     * @throws ServiceException
     */
    public List<Order> queryPaidOrderListByTimeScope(String startTime,
            String endTime) throws ServiceException;

    /**
     * Get all paid order list order by pay time asc.
     * 
     * @return List<Order>
     * @throws ServiceException
     */
    public List<Order> queryAllPaidOrderListOrderByPayTime()
            throws ServiceException;

    /**
     * Get refunded order list by time scope.
     * 
     * @param startTime
     * @param endTime
     * @return List<Order>
     * @throws ServiceException
     */
    public List<Order> queryRefundedOrderListByTimeScope(String startTime,
            String endTime) throws ServiceException;

    /**
     * Get all refunded order list order by refund time asc.
     * 
     * @return List<Order>
     * @throws ServiceException
     */
    public List<Order> queryAllRefundedOrderListOrderByRefundTime()
            throws ServiceException;

    /**
     * Get all order list.
     * 
     * @param orderState
     * @param pageInfo
     * 
     * @return List<Order>
     * @throws ServiceException
     */
    public List<Order> queryOrderListByState(PageInfo pageInfo,
            String orderState) throws ServiceException;

    /**
     * Get refundable order list by userId.
     * 
     * @param userId
     * @return List<Order>
     * @throws ServiceException
     */
    public List<Order> queryRefundableOrders(String userId)
            throws ServiceException;

    /**
     * Get unpaid order list by userId and createBy.
     * 
     * @param userId
     * @param createBy
     * @param queryItem
     * @return List<Order>
     * @throws ServiceException
     */
    public List<Order> queryUnPaidOrderList(String userId, String createBy,
            boolean queryItem) throws ServiceException;

    /**
     * Get unpaid order number by userId and createBy.
     * 
     * @param userId
     * @param createBy
     * @throws ServiceException
     */
    public int queryUnPaidOrderNum(String userId, String createBy)
            throws ServiceException;

    /**
     * Get order detail information by orderNo.
     * 
     * @param orderNo
     * @param queryItem
     * @return Order
     * @throws ServiceException
     */
    public Order queryOrderInfo(String orderNo, boolean queryItem)
            throws ServiceException;

    /**
     * Get order detail information by userId and orderNo.
     * 
     * @param userId
     * @param orderNo
     * @return Order
     * @throws ServiceException
     */
    public Order queryOrderInfo(String userId, String orderNo)
            throws ServiceException;

    /**
     * Cancel a exist order.
     * 
     * @param userId
     * @param orderNo
     * @return boolean
     * @throws ServiceException
     */
    public boolean cancelOrder(String userId, String orderNo)
            throws ServiceException;

    /**
     * Delete a exist order.
     * 
     * @param userId
     * @param orderNo
     * @return boolean
     * @throws ServiceException
     */
    public boolean deleteOrder(String userId, String orderNo)
            throws ServiceException;

    /**
     * Apply for refund by users themselves.
     * 
     * @param userId
     * @param orderId
     * @param refundReasonId
     * @param refundFee
     * @param refundDesc
     * @return RefundRequest
     * @throws ServiceException
     */
    public RefundRequest applyForRefund(String userId, String orderNo,
            String refundReason, float refundFee, String refundDesc,
            String createBY) throws ServiceException;

    /**
     * Apply for refund by users themselves.
     * 
     * @param userId
     * @param refundRequests
     * @return List<RefundRequest>
     * @throws ServiceException
     */
    public List<RefundRequest> applyForRefund(String userId,
            List<RefundRequest> refundRequests) throws ServiceException;

    /**
     * revoke refund request according to orderNo list.
     * 
     * @param orderNo
     * @return List<Long>
     * @throws ServiceException
     */
    public List<Long> revokeRefund(List<String> orderNo)
            throws ServiceException;

    /**
     * revoke refund request according to userId.
     * 
     * @param orderNo
     * @return
     * @throws ServiceException
     */
    public void revokeRefund(String userId) throws ServiceException;

    /**
     * Get RefundRequest's list that are not processed by auditor.
     * 
     * @param pageInfo
     * 
     * @return List<RefundRequest>
     * @throws ServiceException
     */
    public List<RefundRequest> queryNonProcessRefundRequestList(
            PageInfo pageInfo) throws ServiceException;

    /**
     * Get RefundRequest's list that are processed by auditor.
     * 
     * @param pageInfo
     * 
     * @return List<RefundRequest>
     * @throws ServiceException
     */
    public List<RefundRequest> queryProcessedRefundRequestList(PageInfo pageInfo)
            throws ServiceException;

    /**
     * Get RefundRequest's list by auditor.
     * 
     * @return List<RefundRequest>
     * @throws ServiceException
     */
    public List<RefundRequest> queryRefundRequestList() throws ServiceException;

    /**
     * Get TransferRequest's list by auditor.
     * 
     * @return List<TransferRequest>
     * @throws ServiceException
     */
    public List<TransferRequest> queryTransferRequestList()
            throws ServiceException;

    /**
     * Get RefundRequest list by requestId list.
     * 
     * @param List<Long> requestIdList
     * @return List<RefundRequest>
     * @throws ServiceException
     */
    public List<RefundRequest> queryRefundRequestList(List<Long> requestIdList)
            throws ServiceException;

    /**
     * Get Applying RefundRequest list by requestId list.
     * 
     * @param orderNo
     * @return
     * @throws ServiceException
     */
    public List<RefundRequest> queryApplyingRefundRequest(String orderNo)
            throws ServiceException;

    /**
     * Get RefundRequest list by orderNo.
     * 
     * @param orderNo
     * @return List<RefundRequest>
     * @throws ServiceException
     */
    public List<RefundRequest> queryRefundRequest(String orderNo)
            throws ServiceException;

    /**
     * Get RefundRequest list by orderNo.
     * 
     * @param orderNo
     * @return List<RefundRequest>
     * @throws ServiceException
     */
    public OrderState queryRefundRequestState(String orderNo)
            throws ServiceException;

    /**
     * Get RefundRequest list that has Applied by orderNo.
     * 
     * @param orderNo
     * @return List<RefundRequest>
     * @throws ServiceException
     */
    public List<RefundRequest> queryAppliedRefundRequestList(String orderNo)
            throws ServiceException;

    /**
     * Set order state by orderNo and state.
     * 
     * @param orderNo
     * @param state
     * @return OrderState
     * @throws ServiceException
     */
    public OrderState setOrderState(Order order, OrderState state)
            throws ServiceException;

    /**
     * Set refundRequestList state by requestId list and state.
     * 
     * @param refundSuccessList
     * @param refundFailedList
     * @throws ServiceException
     */
    public void refundNotifyCallBack(List<Refund> refundSuccessList,
            List<Refund> refundFailedList) throws ServiceException;

    /**
     * Set refundRequestList by requestId list, state and auditorId.
     * 
     * @param requestListId
     * @param state
     * @param auditorId
     * @param batchNo
     * @return OrderState
     * @throws ServiceException
     */
    public OrderState updateRefundRequestListStateAndBatchNo(
            List<RefundRequest> refundRequests, OrderState state,
            long auditorId, String batchNo) throws ServiceException;

    /**
     * Set refundRequestList by requestId list, state, feedback and auditorId.
     * 
     * @param requestListId
     * @param state
     * @param auditorId
     * @param feedback
     * @return OrderState
     * @throws ServiceException
     */
    public OrderState setRefundRequestList(List<Long> requestIdList,
            OrderState state, long auditorId, String feedback)
            throws ServiceException;

    /**
     * Judge whether the order is being applied for refund by orderNo.
     * 
     * @param orderNo
     * @return boolean
     * @throws ServiceException
     */
    public boolean isRefunding(String orderNo) throws ServiceException;

    /**
     * Clean expired unpaid orders.
     * 
     * @throws ServiceException
     */
    public void cleanExpiredUnpaidOrders() throws ServiceException;

    /**
     * @param batchNo
     * @return
     */
    public List<RefundRequest> queryRefundRequestListByBatchNo(String batchNo);

    /**
     * @param batchNo
     * @param auditorId
     * @throws ServiceException
     */
    public void putAuditorId(String batchNo, long auditorId)
            throws ServiceException;

    /**
     * 
     * @param refundRequest
     * @throws ServiceException
     */
    public void updateRefundRequest(RefundRequest refundRequest)
            throws ServiceException;

    /**
     * 
     * @param appAliPaymentLog
     * @return
     * @throws ServiceException
     */
    public Order appAliNotify(AppAliPaymentLog appAliPaymentLog)
            throws ServiceException;

    /**
     * 
     * @param pageInfo
     * @param requestState
     * @return
     * @throws ServiceException
     */
    public List<RefundRequest> queryRefundRequestList(PageInfo pageInfo,
            String requestState) throws ServiceException;

    /**
     * 
     * @param orderNo
     * @return
     * @throws ServiceException
     */
    public RefundRequest queryRefundDoneRequestByOrderNo(String orderNo)
            throws ServiceException;

    public List<Order> queryOrderList(PageInfo pageInfo,
            Map<String, String> query) throws ServiceException;

    public Map<String, Order> getOrderMap(List<String> orderNos)
            throws ServiceException;

    public List<RefundRequest> queryRefundRequestList(PageInfo pageInfo,
            String orderNo, String requestState, String startTime,
            String endTime) throws ServiceException;

}
