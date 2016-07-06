/**
 * PaymentConstant.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * 2015年12月23日
 */
package com.ovt.order.dao.constant;

/**
 * PaymentConstant
 * 
 * @Author leo.liu
 * @Version 1.0
 * @See 
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public class PayConstants
{
    public static final class PAY_TYPE
    {
        public static final String ONLINE = "online";
        public static final String OFFLINE = "offline";
    }
    
    public static final class PAY_SOURCE
    {
        public static final String ALIPAY = "Alipay";
        public static final String WXPAY = "Wxpay";
    }
    
    public static final class ALIPAY_TRADE_STATUS
    {
        public static final String FINISHED = "TRADE_FINISHED";
        public static final String SUCCESS = "TRADE_SUCCESS";
    }
    
    public static final class REFUND_STATUS
    {
        public static final String APPLYING = "APPLYING";
        public static final String SUCCESS = "SUCCESS";
    }
}
