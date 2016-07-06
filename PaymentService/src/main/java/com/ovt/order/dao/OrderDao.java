/**
 * OrderDao.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * 2015年12月21日
 */
package com.ovt.order.dao;

import java.sql.Timestamp;
import java.util.List;

import com.ovt.order.dao.constant.OrderState;
import com.ovt.order.dao.vo.Order;
import com.ovt.order.dao.vo.PageInfo;

/**
 * OrderDao
 * 
 * @Author brad.zhou
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public interface OrderDao
{
    /**
     * Add a new order.
     * 
     * @param order
     * @return Order
     */
    public long saveOrder(Order order);

    /**
     * Get order's list by userId.
     * 
     * @param userId
     * @return List<Order>
     */
    public List<Order> getOrderListByUserId(String userId);

    /**
     * Get paid order list by time scope.
     * 
     * @param startTime
     * @param endTime
     * @return List<Order>
     */
    public List<Order> getPaidOrderListByTimeScope(Timestamp startTime,
            Timestamp endTime);

    /**
     * Get paid order list order by pay time asc.
     * 
     * @return List<Order>
     */
    public List<Order> getAllPaidOrderListOrderByPayTime();

    /**
     * Get refunded order list by time scope.
     * 
     * @param startTime
     * @param endTime
     * @return List<Order>
     */
    public List<Order> getRefundedOrderListByTimeScope(Timestamp startTime,
            Timestamp endTime);

    /**
     * Get refunded order list order by refund time asc.
     * 
     * @return List<Order>
     */
    public List<Order> AllRefundedOrderListOrderByRefundTime();

    /**
     * Get all order list.
     * 
     * @param orderState
     * @param pageInfo
     * 
     * @return List<Order>
     */
    public List<Order> getOrderListByState(PageInfo pageInfo, String orderState);

    /**
     * 
     * @param pageInfo
     * @param queryInfo
     * @return
     */
    public List<Order> getOrderList(PageInfo pageInfo, String queryInfo);

    /**
     * Get refundable order list by userId.
     * 
     * @param userId
     * @return List<Order>
     */
    public List<Order> getRefundableOrders(String userId);

    /**
     * Get unpaid order's list by userId and createBy.
     * 
     * @param userId
     * @param createBy
     * @param queryItem
     * @return List<Order>
     */
    public List<Order> getUnPaidOrderList(String userId, String createBy,
            boolean queryItem);

    /**
     * Get unpaid order number by userId and createBy.
     * 
     * @param userId
     * @param createBy
     * @return int
     */
    public int getUnPaidOrderNum(String userId, String createBy);

    /**
     * Get a order's detail information by orderNo.
     * 
     * @param orderNo
     * @param queryItem
     * @return Order
     */
    public Order getOrder(String orderNo, boolean queryItem);

    /**
     * Update a order's state.
     * 
     * @param orderNo
     * @param state
     * @return boolean
     */
    public boolean updateOrderState(Order order, OrderState state);

    /**
     * Update order's deleteFlag.
     * 
     * @param orderNo
     * @param flag
     * @return boolean
     */
    public boolean updateOrderDeleteFlag(Order order, int flag);

    /**
     * Clean expired unpaid orders.
     * 
     * @param comparedTime
     */
    public void cleanExpiredUnpaidOrders(Timestamp comparedTime);

    /**
     * 
     * @param orderNo
     * @param state
     */
    public void updateOrderStateOfAppNotify(Order order, OrderState state);

    public List<Order> getOrders(List<String> orderNos);
}
