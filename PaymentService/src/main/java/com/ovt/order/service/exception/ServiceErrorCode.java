/**
 * ServiceErrorCode.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * 2015年12月21日
 */
package com.ovt.order.service.exception;

/**
 * ServiceErrorCode
 * 
 * @Author brad.zhou
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public class ServiceErrorCode
{
    // order
    public static final String INVALID_ORDER_NUMBER = "InvalidOrderNumber";
    public static final String INVALID_ORDER_STATE = "InvalidOrderState";
    public static final String INVALID_TOTAL_FEE = "InvalidTotalFee";
    public static final String NON_POSITIVE_TOTAL_FEE = "NonPositiveTotalFee";
    public static final String MISMATCH_USER_ID = "MismatchUserId";
    public static final String ORDER_IS_DELETE = "OrderIsDelete";
    public static final String ORDER_NUMBER_LIST_IS_EMPTY = "OrderNumberListIsEmpty";
    public static final String QUERY_ORDER_LIST_ERROR = "QueryOrderListError";
    public static final String QUERY_REFUNDABLE_ORDERS_ERROR = "QueryRefundableOrdersError";
    public static final String QUERY_ORDER_INFO_ERROR = "QueryOrderInfoError";
    public static final String UPDATE_ORDER_STATE_ERROR = "UpdateOrderStateError";
    public static final String UPDATE_ORDER_DELETE_FLAG_ERROR = "UpdateOrderDeleteFlagError";
    public static final String UPDATE_ORDER_REFUNDED_FEE_ERROR = "UpdateOrderRefundFeeError";
    public static final String GET_UNPAID_ORDERS_ERROR = "GetUnpaidOrdersError";
    public static final String JUDGE_ORDER_REFUNDED_ERROR = "JudgeOrderRefundedError";
    public static final String GET_TRADE_NO_ERROR = "GetTradeNoError";
    public static final String ORDER_IS_NULL = "OrderIsNull";
    public static final String SAVE_ORDER_ERROR = "SaveOrderError";

    // refund request
    public static final String SAVE_REFUND_REQUEST_ERROR = "SaveRefundRequestError";
    public static final String REPOST_REFUND_REQUEST_ERROR = "RepostRefundRequestError";
    public static final String QUERY_REFUND_REQUESTS_ERROR = "QueryRefundRequestsError";
    public static final String QUERY_REFUND_REQUEST_ERROR = "QueryRefundRequestError";
    public static final String QUERY_NON_PROCESS_REFUND_REQUESTS_ERROR = "QueryNonProcessRefundRequestsError";
    public static final String QUERY_PROCESSED_REFUND_REQUESTS_ERROR = "QueryProcessedRefundRequestsError";
    public static final String QUERY_APPLIED_REFUND_REQUESTS_ERROR = "QueryAppliedRefundRequestsError"; //"REFUND_DOING", "REFUND_APPLYING", "REFUND_DONE"
    public static final String QUERY_APPLYING_REFUND_REQUESTS_ERROR = "QueryApplyingRefundRequestsError";   //"REFUND_APPLYING"
    public static final String GET_REFUND_REQUEST_IDS_ERROR = "GetRefundRequestIdsError";
    public static final String UPDATE_REFUND_REQUESTS_STATE_ERROR = "UpdateRefundRequestsStateError";
    public static final String UPDATE_REFUND_REQUESTS_ERROR = "UpdateRefundRequestsError";
    public static final String UPDATE_AUDITOR_ID_ERROR = "UpdateAuditorIdError";

    // refund
    public static final String REFUND_FEE_TO_MUCH = "RefundFeeToMuch";
    public static final String NON_POSITIVE_REFUND_FEE = "NonPositiveRefundFee";
    public static final String USER_ID_IS_BLANK = "UserIdIsBlank";
    public static final String SAVE_REFUNDS_ERROR = "SaveRefundsError";
    public static final String SAVE_REFUND_LOG_ERROR = "SaveRefundLogError";
    public static final String GET_REFUND_LOGS_ERROR = "GetRefundLogsError";
    public static final String GET_REFUND_LOG_ERROR = "GetRefundLogError";
    public static final String UPDATE_REFUNDS_ERROR = "UpdateRefundsError";
    public static final String GET_REFUND_ERROR = "GetRefundError";
    public static final String GET_REFUNDS_ERROR = "GetRefundsError";

    // payment
    public static final String SAVE_PAYMENT_LOG_ERROR = "SavePaymentLogError";
    public static final String GET_PAYMENT_LOGS_ERROR = "GetPaymentLogsError";
    public static final String GET_PAYMENT_LOG_ERROR = "GetPaymentLogError";
    public static final String GET_PAYMENT_ERROR = "GetPaymentError";
    public static final String GET_PAYMENTS_ERROR = "GetPaymentsError";
    public static final String SAVE_PAYMENT_ERROR = "SavePaymentError";
    public static final String UPDATE_PAYMENT_ERROR = "UpdatePaymentError";
    public static final String DELETE_PAYMENT_ERROR = "DeletePaymentError";
    public static final String PARSE_NOTIFY_PARAM_ERROR = "ParseNotifyParamError";
    
    // account page query
    public static final String ACCOUNT_PAGE_QUERY_ERROR  = "AccountPageQueryError";
    public static final String ACCOUNT_CHECK_ERROR_QUERY_ERROR  = "AccountCheckErrorQueryError";
    public static final String ACCOUNT_CHECK_ERROR_SAVE_ERROR  = "AccountCheckErrorSaveError";
    public static final String ACCOUNT_CHECK_ERROR_UPDATE_ERROR  = "AccountCheckErrorUpdateError";
    
    // access
    public static final String ACCESS_REMOTE_FAILED = "AccessRemoteFailed";

    // alipay
    public static final String INVALID_REFUND_NOTIFY_PARAMS = "InvalidRefundNotifyParams";
    public static final String INVALID_REFUND_SUCCESS_NUM = "InvalidRefundSuccessNum";
    public static final String INVALID_REFUND_BATCH_NO = "InvalidRefundBatchNo";

    // unexpected
    public static final String SYSTEM_UNEXPECTED = "Unexpected Error";
    public static final String REGUND_REQUEST_IS_NULL = "RefundRequestIsNull";
}
