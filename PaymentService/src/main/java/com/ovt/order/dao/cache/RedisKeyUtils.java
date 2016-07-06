/**
 * RedisKeyUtil.java
 * 
 * Copyright@2016 OVT Inc. All rights reserved. 
 * 
 * Apr 13, 2016
 */
package com.ovt.order.dao.cache;

import java.text.MessageFormat;

/**
 * RedisKeyUtil
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See 
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public class RedisKeyUtils
{
    private static final String KEY_PATTERN_UNPAID_ORDER = "User:{0}_Create:{1}";
    
    private static final String KEY_PATTERN_ORDER = "O:{0}";

    public static String buildUserUnpaidOrderKey(String userId, String createBy)
    {
        return MessageFormat.format(KEY_PATTERN_UNPAID_ORDER, userId, createBy);
    }
    
    public static String buildOrderKey(String orderNo)
    {
        return MessageFormat.format(KEY_PATTERN_ORDER, orderNo);
    }
}
