/**
 * StateChecker.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * 2015年12月22日
 */
package com.ovt.order.service.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ovt.order.dao.constant.OrderState;

/**
 * StateChecker
 * 
 * @Author leo.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public class StateChecker
{
    /**
     * Key: [from state] Value: [to states]
     * 
     * Means order state can only transit from [from state] to [to states].
     */
    public static final Map<OrderState, List<OrderState>> StateTransitRules = new HashMap<OrderState, List<OrderState>>();

    static
    {
        StateTransitRules.put(OrderState.ORDER_WAIT_PAY, Arrays.asList(
                OrderState.ORDER_PAID, OrderState.ORDER_CANCELED,
                OrderState.ORDER_WAIT_ALINOTIFY));
        
        StateTransitRules.put(OrderState.ORDER_WAIT_ALINOTIFY, Arrays.asList(
                OrderState.ORDER_PAID));

        StateTransitRules.put(OrderState.ORDER_PAID, Arrays.asList(
                OrderState.ORDER_DELETE, OrderState.REFUND_APPLYING));

        StateTransitRules.put(OrderState.ORDER_CANCELED,
                Arrays.asList(OrderState.ORDER_DELETE));

        StateTransitRules.put(OrderState.REFUND_APPLYING, Arrays.asList(
                OrderState.REFUND_DOING, OrderState.REFUND_REFUSED,
                OrderState.REFUND_CANCELED));

        StateTransitRules.put(OrderState.REFUND_DOING,
                Arrays.asList(OrderState.REFUND_DONE));
    }

    public static boolean checkStateChangeValid(OrderState currentState,
            OrderState newState)
    {
        if (StateTransitRules.containsKey(currentState)
                && StateTransitRules.get(currentState).contains(newState))
        {
            return true;
        }

        return false;
    }
}
