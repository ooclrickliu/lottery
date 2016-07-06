/**
 * DBConstants.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * May 5, 2015
 */
package com.ovt.order.dao.constant;

/**
 * DBConstants
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public class DBConstants
{

    public static final class TABLES
    {

        public static class BASE_ENTITY
        {
            public static final String ID = "id";
            public static final String CREATE_TIME = "create_time";
            public static final String UPDATE_TIME = "update_time";
            public static final String IS_DELETE = "is_delete";
        }

        public static final class PAYMENT extends BASE_ENTITY
        {
            public static final String ORDER_NO = "order_no";
            public static final String PAY_NO = "pay_no";
            public static final String PAY_TYPE = "pay_type";
            public static final String PAY_SOURCE = "pay_source";
            public static final String PAY_FEE = "pay_fee";
            public static final String PAY_STATE = "pay_state";
            public static final String PAY_TIME = "pay_time";
            public static final String PAY_LOG_ID = "pay_log_id";
        }

        public static final class ALI_PAYMENT_LOG extends BASE_ENTITY
        {
            public static final String NOTIFY_TIME = "notify_time";
            public static final String NOTIFY_TYPE = "notify_type";
            public static final String NOTIFY_ID = "notify_id";
            public static final String SIGN_TYPE = "sign_type";
            public static final String SIGN = "sign";
            public static final String OUT_TRADE_NO = "out_trade_no";
            public static final String SUBJECT = "subject";
            public static final String TRADE_NO = "trade_no";
            public static final String TRADE_STATUS = "trade_status";
            public static final String BUYER_ID = "buyer_id";
            public static final String BUYER_EMAIL = "buyer_email";
            public static final String TOTAL_FEE = "total_fee";
            public static final String GMT_CREATE = "gmt_create";
            public static final String GMT_PAYMENT = "gmt_payment";
            public static final String IS_TOTAL_FEE_ADJUST = "is_total_fee_adjust";
            public static final String USE_COUPON = "use_coupon";
            public static final String DISCOUNT = "discount";
            public static final String REFUND_STATUS = "refund_status";
            public static final String GMT_REFUND = "gmt_refund";
        }

        public static final class REFUND extends BASE_ENTITY
        {
            public static final String REFUND_REQUEST_ID = "refund_request_id";
            public static final String BATCH_NO = "batch_no";
            public static final String TRADE_NO = "trade_no";
            public static final String REFUND_FEE = "refund_fee";
            public static final String REFUND_STATE = "refund_state";
            public static final String REFUND_TAX = "refund_tax";
            public static final String REFUND_TAX_STATE = "refund_tax_state";
            public static final String REFUND_TIME = "refund_time";
            public static final String REFUND_LOG_ID = "refund_log_id";
        }

        public static final class ALI_REFUND_LOG extends BASE_ENTITY
        {
            public static final String BATCH_NO = "batch_no";
            public static final String NOTIFY_TIME = "notify_time";
            public static final String NOTIFY_TYPE = "notify_type";
            public static final String NOTIFY_ID = "notify_id";
            public static final String SIGN_TYPE = "sign_type";
            public static final String SIGN = "sign";
            public static final String SUCCESS_NUM = "success_num";
            public static final String RESULT_DETAILS = "result_details";
        }

        public static final class ORDER extends BASE_ENTITY
        {
            public static final String ORDER_NO = "order_no";
            public static final String ORDER_STATE = "order_state";
            public static final String ORDER_TOTAL_FEE = "order_total_fee";
            public static final String REFUNDED_FEE = "refunded_fee";
            public static final String USER_ID = "user_id";
            public static final String CREATE_BY = "create_by";
            public static final String CREATE_TIME = "create_time";
            public static final String UPDATE_TIME = "update_time";
            public static final String ORDER_REMARK = "order_remark";
            public static final String IS_DELETE = "is_delete";
            public static final String EXTRA_1 = "extra_1";
            public static final String EXTRA_2 = "extra_2";
            public static final String EXTRA_3 = "extra_3";
            public static final String PAY_TIME = "pay_time";
            public static final String REFUND_TIME = "refund_time";
        }

        public static final class ORDER_ITEM extends BASE_ENTITY
        {
            public static final String ORDER_ID = "order_id";
            public static final String ITEM_NO = "item_no";
            public static final String ITEM_NAME = "item_name";
            public static final String ITEM_PRICE = "item_price";
            public static final String ITEM_NUM = "item_num";
        }

        public static final class REFUND_REQUEST extends BASE_ENTITY
        {
            public static final String ORDER_NO = "order_no";
            public static final String CREATE_BY = "create_by";
            public static final String REFUND_REASON = "refund_reason";
            public static final String REFUND_FEE = "refund_fee";
            public static final String REFUND_DESC = "refund_desc";
            public static final String FEEDBACK = "feedback";
            public static final String AUDITOR_ID = "auditor_id";
            public static final String CREATE_TIME = "create_time";
            public static final String REFUND_STATE = "refund_state";
            public static final String BATCH_NO = "batch_no";
            public static final String REFUND_LOG = "refund_log";
        }

        public static final class TRANSFER_REQUEST extends BASE_ENTITY
        {
            public static final String RECEIVER_ACCOUNT = "receiver_account";
            public static final String REFUND_REASON_ID = "refund_reason_id";
            public static final String REFUND_FEE = "refund_fee";
            public static final String REFUND_DESC = "refund_desc";
            public static final String REFUND_STATE = "refund_state";
        }

        public static final class REFUND_REASON extends BASE_ENTITY
        {
            public static final String REASON = "reason";
        }
        
        public static final class APP_PROPERTY extends BASE_ENTITY
        {
            public static final String PROP_NAME = "prop_name";
            public static final String PROP_VALUE = "prop_value";
            public static final String DESC = "desc";
        }
        
        public static final class ACCOUNT_CHECK_ERROR extends BASE_ENTITY
        {
            public static final String ORDER_NO = "order_no";
            public static final String DETAIL = "detail";
            public static final String ISREAD = "is_read";
        }
        
        public static final class APP_ALI_PAYMENT_LOG extends BASE_ENTITY
        {
            public static final String RESULT = "result";
            public static final String RESULT_STATUS = "resultStatus";
            public static final String MEMO = "memo";
            public static final String PARTNER = "partner";
            public static final String SELLER_ID = "seller_id";
            public static final String OUT_TRADE_NO = "out_trade_no";
            public static final String SUBJECT = "subject";
            public static final String BODY = "body";
            public static final String TOTAL_FEE = "total_fee";
            public static final String NOTIFY_URL = "notify_url";
            public static final String SERVICE = "service";
            public static final String PAYMENT_TYPE = "payment_type";
            public static final String INPUT_CHARSET = "_input_charset";
            public static final String IT_B_PAY = "it_b_pay";
            public static final String SUCCESS = "success";
            public static final String SIGN_TYPE = "sign_type";
            public static final String SIGN = "sign";
        }
    }
}
