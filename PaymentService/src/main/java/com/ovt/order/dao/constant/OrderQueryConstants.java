package com.ovt.order.dao.constant;

import java.util.Arrays;
import java.util.List;

public class OrderQueryConstants
{
    public static final List<String> queryFileds = Arrays.asList("order_no",
            "order_state", "user_id", "create_by");
    public static final String startTime = "startTime";
    public static final String endTime = "endTime";
    public static final String timeField = "create_time between ";
}
