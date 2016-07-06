/**
 * OrderState.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * 2015年12月22日
 */
package com.ovt.order.dao.constant;

/**
 * OrderState
 * 
 * @Author leo.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public enum OrderState
{
    //order state, In order table.
    ORDER_WAIT_PAY,
    ORDER_WAIT_ALINOTIFY,
    ORDER_PAID,
    ORDER_CANCELED,
    ORDER_DELETE,
    ORDER_FINISHED,
    
    // refund request state, In refund_request Table.
    REFUND_APPLYING, 
    REFUND_DOING, 
    REFUND_REFUSED,
    REFUND_DONE,
    REFUND_CANCELED,
    REFUND_FAILED
}
