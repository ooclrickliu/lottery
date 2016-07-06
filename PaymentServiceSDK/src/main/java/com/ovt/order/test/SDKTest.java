/**
 * SDKTest.java
 * 
 * Copyright@2016 OVT Inc. All rights reserved. 
 * 
 * 2016年1月8日
 */
package com.ovt.order.test;

import java.util.ArrayList;
import java.util.List;

import com.ovt.order.util.OVPayment;
import com.ovt.order.util.entity.Order;
import com.ovt.order.util.entity.OrderItem;
import com.ovt.order.util.entity.RefundRequest;
import com.ovt.order.util.exception.PaymentSDKException;

/**
 * SDKTest
 * 
 * @Author brad.zhou
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public class SDKTest
{
    static OVPayment ovPayment = new OVPayment("http://localhost:28080/PaymentService/api");
    
    public static void testCreateOrder() throws PaymentSDKException
    {
        Order order = new Order();
        order.setOrderNo("8395712403472");
        order.setUserId("1");
        order.setOrderTotalFee(123);
        order.setOrderRemark("请给我开10G流量");
        order.setCreateBy("6");

        List<OrderItem> orderItems = new ArrayList<OrderItem>();
        orderItems.add(new OrderItem("122387420343 ", "流量", 100, 2));
        orderItems.add(new OrderItem("342387420343 ", "磁盘", 50, 3));

        order.setOrderItemList(orderItems);

        Order result = ovPayment.createOrder(order);
        System.out.println(result.getId());
    }

    public static void testApplyForRefund() throws PaymentSDKException
    {
        RefundRequest refundRequest = new RefundRequest("1234567890", "6", "贵",
                13, "东西太贵了");
        RefundRequest result = ovPayment.applyForRefund("1", refundRequest);
        System.out.println(result.getId());
    }

    public static void testGetOrderList() throws PaymentSDKException
    {

        List<Order> result = ovPayment.getOrderList("1");
        System.out.println(result.size());
        for (Order order : result)
        {
            System.out.println(order.getId());
        }
    }

    public static void testRefundRequest() throws PaymentSDKException
    {
        List<Long> refundRequestIds = new ArrayList<Long>();
        refundRequestIds.add(1L);
        refundRequestIds.add(2L);
        refundRequestIds.add(3L);
        refundRequestIds.add(4L);
        refundRequestIds.add(5L);

        List<RefundRequest> result = ovPayment
                .getRefundRequest(refundRequestIds);

        System.out.println(result.size());
        for (RefundRequest refundRequest : result)
        {
            System.out.println(refundRequest.getId());
        }
    }

    public static void main(String[] args) throws PaymentSDKException
    {
        // testCreateOrder();
        // testApplyForRefund();
        // testGetOrderList();
        // testApplyingRefundRequestList();
        // testRefundRequest();
        // testGetRefundableOrders();
    }
}
