/**
 * AlipayConstants.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * 2015年12月24日
 */
package com.ovt.order.service.alipay;

/**
 * AlipayConstants
 * 
 * @Author leo.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public class AlipayConstants
{
    public static final class Alipay
    {
        public static final String PARAM_NAME_SERVICE = "service";
        public static final String PARAM_NAME_PARTNER = "partner";
        public static final String PARAM_NAME_INPUT_CHARSET = "_input_charset";
        public static final String PARAM_NAME_NOTIFY_URL = "notify_url";
        public static final String PARAM_NAME_SELLER_EMAIL = "seller_email";
        public static final String PARAM_NAME_REFUND_DATE = "refund_date";
        public static final String PARAM_NAME_BATCH_NO = "batch_no";
        public static final String PARAM_NAME_BATCH_NUM = "batch_num";
        public static final String PARAM_NAME_DETAIL_DATA = "detail_data";
        public static final String PARAM_NAME_PAGE_NO = "page_no";
        public static final String PARAM_NAME_ORDER_NO = "merchant_out_order_no";
        public static final String PARAM_NAME_GMT_START_TIME = "gmt_start_time";
        public static final String PARAM_NAME_GMT_END_TIME = "gmt_end_time";

        public static final String ACCOUNT_QUERY_SERVICE_NAME = "account.page.query";
        public static final String REFUND_SERVICE_NAME = "refund_fastpay_by_platform_pwd";
        public static final String TRANSFER_SERVICE_NAME = "refund_fastpay_by_platform_pwd";

        public static final String REFUND_REASON = "协商退款";
        public static final String REFUND_CONFIRM_PAGE_OK = "确定";
    }
}