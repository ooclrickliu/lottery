/**
 * OVOrder.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * 2015年12月25日
 */
package com.ovt.order.util;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.web.client.RestTemplate;

import com.ovt.order.util.entity.AliPaymentLog;
import com.ovt.order.util.entity.AliRefundLog;
import com.ovt.order.util.entity.JsonDocument;
import com.ovt.order.util.entity.JsonUtils;
import com.ovt.order.util.entity.OVTException;
import com.ovt.order.util.entity.Order;
import com.ovt.order.util.entity.OrderState;
import com.ovt.order.util.entity.PageInfo;
import com.ovt.order.util.entity.Payment;
import com.ovt.order.util.entity.PaymentLog;
import com.ovt.order.util.entity.Refund;
import com.ovt.order.util.entity.RefundRequest;
import com.ovt.order.util.exception.PaymentSDKErrorCode;
import com.ovt.order.util.exception.PaymentSDKException;

/**
 * OVOrder
 * 
 * @Author leo.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class OVPayment
{

    public String baseUrl;

    private RestTemplate restTemplate = new RestTemplate();

    public OVPayment(String url)
    {
        this.baseUrl = url;
    }

    public String getBaseUrl()
    {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl)
    {
        this.baseUrl = baseUrl;
    }

    /**
     * create order
     * 
     * @param order : the object that will be saved.
     * @return process result (json)
     * @throws PaymentSDKException
     */
    public Order createOrder(Order order) throws PaymentSDKException
    {
        String url = baseUrl + "/orders/create";

        JsonDocument result = restTemplate.postForObject(url, order,
                JsonDocument.class);

        Order orderResult = (Order) result2Object(result, Order.class);

        return orderResult;
    }

    public String testPayAliNitify(String orderNo, float totalFee)
            throws PaymentSDKException
    {
        String url = baseUrl + "/pay/test/alinotify";
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        String now = formater.format(new Date());   
        String param = "?discount=0.00&payment_type=1&subject=测试&trade_no=2013082244524842&"
                + "buyer_email=dlwdgl@gmail.com&gmt_create=" + now + "&"
                + "notify_type=trade_status_sync&quantity=1&out_trade_no="
                + orderNo
                + "&"
                + "seller_id=2088501624816263&notify_time=" + now + "&body=测试测试&"
                + "trade_status=TRADE_SUCCESS&is_total_fee_adjust=N&total_fee="
                + totalFee
                + "&"
                + "gmt_payment=" + now + "&seller_email=xxx@alipay.com&"
                + "price=1.00&buyer_id=2088602315385429&notify_id=64ce1b6ab92d00ede0ee56ade98fdf2f4c&"
                + "use_coupon=N&sign_type=RSA&sign=oweor0934iralwkfj9123498";

        url = url + param;

        String result = restTemplate.postForObject(url, null, String.class);

        return result;
    }
    
    public String testPayAliNitify(String orderNo, float totalFee, String time)
            throws PaymentSDKException
    {
        String url = baseUrl + "/pay/test/alinotify";

        String param = "?discount=0.00&payment_type=1&subject=测试&trade_no="
                + orderNo
                + "&buyer_email=dlwdgl@gmail.com&gmt_create="
                + time
                + "&"
                + "notify_type=trade_status_sync&quantity=1&out_trade_no="
                + orderNo
                + "&seller_id=2088501624816263&notify_time="
                + time
                + "&body=测试测试&"
                + "trade_status=TRADE_SUCCESS&is_total_fee_adjust=N&total_fee="
                + totalFee
                + "&"
                + "gmt_payment="
                + time
                + "&seller_email=xxx@alipay.com&"
                + "price=1.00&buyer_id=2088602315385429&notify_id=64ce1b6ab92d00ede0ee56ade98fdf2f4c&"
                + "use_coupon=N&sign_type=RSA&sign=oweor0934iralwkfj9123498";

        url = url + param;

        String result = restTemplate.postForObject(url, null, String.class);

        return result;
    }

    /**
     * cancel order
     * 
     * @param userId : user id in third-party system
     * @param orderNo : order number
     * @return process result (json)
     * @throws PaymentSDKException
     */
    public boolean cancelOrder(String userId, String orderNo)
            throws PaymentSDKException
    {
        String url = baseUrl + "/orders/cancel";
        String userIdString = "userId=" + userId;
        String orderNoString = "orderNo=" + orderNo;
        url += "?" + userIdString + "&" + orderNoString;

        JsonDocument result = restTemplate.postForObject(url, null,
                JsonDocument.class);

        Boolean booleanResult = (Boolean) result2Object(result, Boolean.class);

        return booleanResult.booleanValue();
    }

    /**
     * delete order
     * 
     * @param userId : user id in third-party system
     * @param orderNo : order number
     * @return process result (json)
     * @throws PaymentSDKException
     */
    public boolean deleteOrder(String userId, String orderNo)
            throws PaymentSDKException
    {
        String url = baseUrl + "/orders/delete";
        String userIdString = "userId=" + userId;
        String orderNoString = "orderNo=" + orderNo;
        url += "?" + userIdString + "&" + orderNoString;

        JsonDocument result = restTemplate.postForObject(url, null,
                JsonDocument.class);

        Boolean booleanResult = (Boolean) result2Object(result, Boolean.class);

        return booleanResult.booleanValue();
    }

    /**
     * set order app paid
     * 
     * @param userId : user id in third-party system
     * @param orderNo : order number
     * @return process result (json)
     * @throws PaymentSDKException
     */
    public Order setOrderAppPaid(Map<String, String[]> parameterMap)
            throws PaymentSDKException
    {
        String url = baseUrl + "/orders/app/alinotify";
        JsonDocument result = restTemplate.postForObject(url, parameterMap,
                JsonDocument.class);

        Order orderResult = (Order) result2Object(result, Order.class);

        return orderResult;
    }

    /**
     * get order list
     * 
     * @param userId : user id in third-party system
     * @return process result (json)
     * @throws PaymentSDKException
     */
    public List<Order> getOrderList(String userId)// DESC(id)
            throws PaymentSDKException
    {
        String url = baseUrl + "/orders/list";
        String userIdString = "userId=" + userId;
        url += "?" + userIdString;

        JsonDocument result = restTemplate
                .getForObject(url, JsonDocument.class);

        List<Order> orders = (List<Order>) result2List(result, Order.class);

        return orders;
    }

    public List<Order> getPaidOrderListByTimeScope(String startTime,
            String endTime) throws PaymentSDKException
    {
        String url = baseUrl + "/orders/list/timeScope/paid";
        String startTimeString = "startTime=" + startTime;
        String endTimeString = "endTime=" + endTime;
        url += "?" + startTimeString + "&" + endTimeString;

        JsonDocument result = restTemplate
                .getForObject(url, JsonDocument.class);

        List<Order> orders = (List<Order>) result2List(result, Order.class);

        return orders;
    }

    public List<Order> getAllPaidOrderListOrderByPayTime() throws PaymentSDKException
    {
        String url = baseUrl + "/orders/list/paid/all";

        JsonDocument result = restTemplate
                .getForObject(url, JsonDocument.class);

        List<Order> orders = (List<Order>) result2List(result, Order.class);

        return orders;
    }
    
    public List<Order> getRefundedOrderListByTimeScope(String startTime,
            String endTime) throws PaymentSDKException
    {
        String url = baseUrl + "/orders/list/timeScope/refunded";
        String startTimeString = "startTime=" + startTime;
        String endTimeString = "endTime=" + endTime;
        url += "?" + startTimeString + "&" + endTimeString;

        JsonDocument result = restTemplate
                .getForObject(url, JsonDocument.class);

        List<Order> orders = (List<Order>) result2List(result, Order.class);

        return orders;
    }
    
    public List<Order> getAllRefundedOrderListOrderByRefundTime() throws PaymentSDKException
    {
        String url = baseUrl + "/orders/list/refunded/all";

        JsonDocument result = restTemplate
                .getForObject(url, JsonDocument.class);

        List<Order> orders = (List<Order>) result2List(result, Order.class);

        return orders;
    }

    /**
     * get order list
     * 
     * @param userId : user id in third-party system
     * @return process result (json)
     * @throws PaymentSDKException
     */
    public List<Order> getOrderListByState(PageInfo pageInfo, String orderState)
            throws PaymentSDKException
    {
        String url = baseUrl + "/orders/list/adminold";
        url += "?orderState=" + orderState;
        url += "&page=" + pageInfo.getPageNo() + "&limit="
                + pageInfo.getPageSize() + "&sortBy=" + pageInfo.getSortBy()
                + "&order=" + pageInfo.getOrder().toString();

        JsonDocument result = restTemplate
                .getForObject(url, JsonDocument.class);

        List<Order> orders = (List<Order>) result2List(result, Order.class);

        return orders;
    }

    public List<Order> getOrderList(PageInfo pageInfo, Map<String, String> query)
            throws PaymentSDKException
    {
        String url = baseUrl + "/orders/list/admin";
        url += "?page=" + pageInfo.getPageNo() + "&limit="
                + pageInfo.getPageSize() + "&sortBy=" + pageInfo.getSortBy()
                + "&order=" + pageInfo.getOrder().toString();

        JsonDocument result = restTemplate.postForObject(url, query,
                JsonDocument.class);

        List<Order> orders = (List<Order>) result2List(result, Order.class);

        return orders;
    }

    /**
     * get all order list
     * 
     * @param userId : user id in third-party system
     * @return process result (json)
     * @throws PaymentSDKException
     */
    public List<Order> getOrderListAll()// DESC(id)
            throws PaymentSDKException
    {
        String url = baseUrl + "/orders/list/all";

        JsonDocument result = restTemplate
                .getForObject(url, JsonDocument.class);

        List<Order> orders = (List<Order>) result2List(result, Order.class);

        return orders;
    }

    public Order getOrderDetail(String orderNo) throws PaymentSDKException
    {
        String url = baseUrl + "/orders/detail";
        String orderNoString = "orderNo=" + orderNo;
        url += "?" + orderNoString;

        JsonDocument result = restTemplate
                .getForObject(url, JsonDocument.class);

        Order orderResult = (Order) result2Object(result, Order.class);

        return orderResult;
    }

    public Payment getPayment(String orderNo) throws PaymentSDKException
    {
        String url = baseUrl + "/pay/detail";
        String orderNoString = "orderNo=" + orderNo;
        url += "?" + orderNoString;

        JsonDocument result = restTemplate
                .getForObject(url, JsonDocument.class);

        Payment paymentResult = (Payment) result2Object(result, Payment.class);

        return paymentResult;
    }

    public AliPaymentLog getAliPaymentLog(String logId)
            throws PaymentSDKException
    {
        String url = baseUrl + "/pay/log/detail/alipay";
        String orderNoString = "logId=" + logId;
        url += "?" + orderNoString;

        JsonDocument result = restTemplate
                .getForObject(url, JsonDocument.class);

        AliPaymentLog paymentLogResult = (AliPaymentLog) result2Object(result,
                AliPaymentLog.class);

        return paymentLogResult;
    }

    public List<Payment> getPaymentList() throws PaymentSDKException
    {
        String url = baseUrl + "/pay/list";

        JsonDocument result = restTemplate
                .getForObject(url, JsonDocument.class);

        List<Payment> payments = (List<Payment>) result2List(result,
                Payment.class);

        return payments;
    }
    
    public List<Payment> getPaymentListOrderByPayTime() throws PaymentSDKException
    {
        String url = baseUrl + "/pay/list/orderby";

        JsonDocument result = restTemplate
                .getForObject(url, JsonDocument.class);

        List<Payment> payments = (List<Payment>) result2List(result,
                Payment.class);

        return payments;
    }

    public List<Payment> getPaymentListByTimeScope(String startTime,
            String endTime) throws PaymentSDKException
    {
        String url = baseUrl + "/pay/list/timeScope";

        String startTimeString = "startTime=" + startTime;
        String endTimeString = "endTime=" + endTime;
        url += "?" + startTimeString + "&" + endTimeString;

        JsonDocument result = restTemplate
                .getForObject(url, JsonDocument.class);

        List<Payment> payments = (List<Payment>) result2List(result,
                Payment.class);

        return payments;
    }

    public List<Payment> getPaymentListByTime(PageInfo pageInfo,
            String startTime, String endTime) throws PaymentSDKException
    {
        String url = baseUrl + "/pay/list/byTime";

        url += "?startTime=" + startTime;
        url += "&endTime=" + endTime;
        url += "&page=" + pageInfo.getPageNo() + "&limit="
                + pageInfo.getPageSize() + "&sortBy=" + pageInfo.getSortBy()
                + "&order=" + pageInfo.getOrder().toString();

        JsonDocument result = restTemplate.postForObject(url, null,
                JsonDocument.class);

        List<Payment> payments = (List<Payment>) result2List(result,
                Payment.class);

        return payments;
    }

    public List<PaymentLog> getPaymentLogList() throws PaymentSDKException
    {
        String url = baseUrl + "/log/list";

        JsonDocument result = restTemplate
                .getForObject(url, JsonDocument.class);

        List<PaymentLog> paymentLogs = (List<PaymentLog>) result2List(result,
                PaymentLog.class);

        return paymentLogs;
    }

    public RefundRequest applyForRefund(String userId,
            RefundRequest refundRequest) throws PaymentSDKException
    {
        String url = baseUrl + "/orders/applyRefund";

        String userIdString = "userId=" + userId;
        url += "?" + userIdString;
        JsonDocument result = restTemplate.postForObject(url, refundRequest,
                JsonDocument.class);

        RefundRequest refundRequestResult = (RefundRequest) result2Object(
                result, RefundRequest.class);

        return refundRequestResult;
    }

    public RefundRequest reApplyRefundRequest(long requestId, float fee)
            throws PaymentSDKException
    {
        String url = baseUrl + "/orders/repostRefund";
        url += "?" + "requestId=" + requestId + "&fee=" + fee;

        JsonDocument result = restTemplate.postForObject(url, null,
                JsonDocument.class);

        RefundRequest refundRequestResult = (RefundRequest) result2Object(
                result, RefundRequest.class);

        return refundRequestResult;
    }

    public List<Long> revokeRefund(List<String> orderNos)
            throws PaymentSDKException
    {
        String url = baseUrl + "/orders/revokeRefund";

        JsonDocument result = restTemplate.postForObject(url, orderNos,
                JsonDocument.class);

        List<Long> refundRequestIds = (List<Long>) result2List(result,
                Long.class);

        return refundRequestIds;
    }

    public String refund(List<Long> ids, long auditorId)
            throws PaymentSDKException
    {
        String url = baseUrl + "/orders/doAgreeRefund";
        String auditorIdString = "auditorId=" + auditorId;
        url += "?" + auditorIdString;

        JsonDocument result = restTemplate.postForObject(url, ids,
                JsonDocument.class);

        String stringResult = (String) result2Object(result, String.class);

        return stringResult;
    }

    public String refundRepost(List<Long> ids, long auditorId)
            throws PaymentSDKException
    {
        String url = baseUrl + "/orders/doAgreeRefund/repost";
        String auditorIdString = "auditorId=" + auditorId;
        url += "?" + auditorIdString;

        JsonDocument result = restTemplate.postForObject(url, ids,
                JsonDocument.class);

        String stringResult = (String) result2Object(result, String.class);

        return stringResult;
    }

    public boolean rejectRefund(List<Long> requestIds, String feedback,
            long auditorId) throws PaymentSDKException
    {
        String url = baseUrl + "/orders/doRejectRefund";
        String auditorIdString = "auditorId=" + auditorId;
        String feedbackString = "feedback=" + feedback;
        url += "?" + auditorIdString + "&" + feedbackString;

        JsonDocument result = restTemplate.postForObject(url, requestIds,
                JsonDocument.class);

        Boolean booleanResult = (Boolean) result2Object(result, Boolean.class);

        return booleanResult.booleanValue();
    }

    public List<RefundRequest> getRefundRequestList(PageInfo pageInfo,
            String requestState) throws PaymentSDKException
    {
        String url = baseUrl + "/orders/refundList";
        url += "?requestState=" + requestState;
        url += "&page=" + pageInfo.getPageNo() + "&limit="
                + pageInfo.getPageSize() + "&sortBy=" + pageInfo.getSortBy()
                + "&order=" + pageInfo.getOrder().toString();

        JsonDocument result = restTemplate
                .getForObject(url, JsonDocument.class);

        List<RefundRequest> refundRequests = (List<RefundRequest>) result2List(
                result, RefundRequest.class);

        return refundRequests;
    }

    public List<RefundRequest> getRefundRequestList(PageInfo pageInfo,
            String orderNo, String requestState, String startTime,
            String endTime) throws PaymentSDKException
    {
        String url = baseUrl + "/orders/refundList2";
        url += "?orderNo=" + orderNo;
        url += "&requestState=" + requestState;
        url += "&startTime=" + startTime;
        url += "&endTime=" + endTime;

        JsonDocument result = restTemplate.postForObject(url, pageInfo,
                JsonDocument.class);

        List<RefundRequest> refundRequests = (List<RefundRequest>) result2List(
                result, RefundRequest.class);

        return refundRequests;
    }

    public List<RefundRequest> getProcessedRefundRequestList(PageInfo pageInfo)
            throws PaymentSDKException
    {
        String url = baseUrl + "/orders/processedRefundList";
        url += "?page=" + pageInfo.getPageNo() + "&limit="
                + pageInfo.getPageSize() + "&sortBy=" + pageInfo.getSortBy()
                + "&order=" + pageInfo.getOrder().toString();

        JsonDocument result = restTemplate
                .getForObject(url, JsonDocument.class);

        List<RefundRequest> refundRequests = (List<RefundRequest>) result2List(
                result, RefundRequest.class);

        return refundRequests;

    }

    public List<RefundRequest> getNonProcessRefundRequestList(PageInfo pageInfo)
            throws PaymentSDKException
    {
        String url = baseUrl + "/orders/nonProcessRefundList";
        url += "?page=" + pageInfo.getPageNo() + "&limit="
                + pageInfo.getPageSize() + "&sortBy=" + pageInfo.getSortBy()
                + "&order=" + pageInfo.getOrder().toString();

        JsonDocument result = restTemplate
                .getForObject(url, JsonDocument.class);

        List<RefundRequest> refundRequests = (List<RefundRequest>) result2List(
                result, RefundRequest.class);

        return refundRequests;
    }

    public List<RefundRequest> getRefundRequest(String orderNo)
            throws PaymentSDKException
    {
        String url = baseUrl + "/orders/refundDetail";
        String orderNoString = "orderNo=" + orderNo;
        url += "?" + orderNoString;

        JsonDocument result = restTemplate
                .getForObject(url, JsonDocument.class);

        List<RefundRequest> refundRequests = (List<RefundRequest>) result2List(
                result, RefundRequest.class);

        return refundRequests;
    }

    public OrderState getRefundRequestState(String orderNo)
            throws PaymentSDKException
    {
        String url = baseUrl + "/orders/refundState";
        String orderNoString = "orderNo=" + orderNo;
        url += "?" + orderNoString;

        JsonDocument result = restTemplate
                .getForObject(url, JsonDocument.class);

        OrderState refundRequestState = (OrderState) result2Object(result,
                OrderState.class);

        return refundRequestState;
    }

    /**
     * @param refundRequestIds
     * @return
     */
    public List<RefundRequest> getRefundRequest(
            Collection<Long> refundRequestIds) throws PaymentSDKException
    {
        String url = baseUrl + "/orders/refundListByIds";

        JsonDocument result = restTemplate.postForObject(url, refundRequestIds,
                JsonDocument.class);

        List<RefundRequest> refundRequests = (List<RefundRequest>) result2List(
                result, RefundRequest.class);

        return refundRequests;
    }

    public Refund getRefundDetail(String orderNo) throws PaymentSDKException
    {
        String url = baseUrl + "/refund/detail";
        String orderNoString = "orderNo=" + orderNo;
        url += "?" + orderNoString;

        JsonDocument result = restTemplate
                .getForObject(url, JsonDocument.class);

        Refund refundResult = (Refund) result2Object(result, Refund.class);

        return refundResult;
    }

    public AliRefundLog getAliRefundLog(String logId)
            throws PaymentSDKException
    {
        String url = baseUrl + "/refund/log/detail";
        String orderNoString = "logId=" + logId;
        url += "?" + orderNoString;

        JsonDocument result = restTemplate
                .getForObject(url, JsonDocument.class);

        AliRefundLog refundLogResult = (AliRefundLog) result2Object(result,
                AliRefundLog.class);

        return refundLogResult;
    }

    public List<Refund> getRefundList() throws PaymentSDKException
    {
        String url = baseUrl + "/refund/list";

        JsonDocument result = restTemplate
                .getForObject(url, JsonDocument.class);

        List<Refund> refundList = (List<Refund>) result2List(result,
                Refund.class);

        return refundList;
    }
    
    public List<Refund> getRefundListOrderByRefundTime() throws PaymentSDKException
    {
        String url = baseUrl + "/refund/list/orderby";

        JsonDocument result = restTemplate
                .getForObject(url, JsonDocument.class);

        List<Refund> refundList = (List<Refund>) result2List(result,
                Refund.class);

        return refundList;
    }

    public List<Refund> getRefundListByTimeScope(String startTime,
            String endTime) throws PaymentSDKException
    {
        String url = baseUrl + "/refund/list/timeScope";

        String startTimeString = "startTime=" + startTime;
        String endTimeString = "endTime=" + endTime;
        url += "?" + startTimeString + "&" + endTimeString;

        JsonDocument result = restTemplate
                .getForObject(url, JsonDocument.class);

        List<Refund> refunds = (List<Refund>) result2List(result, Refund.class);

        return refunds;
    }

    public List<Refund> getRefundListByTime(PageInfo pageInfo,
            String startTime, String endTime) throws PaymentSDKException
    {
        String url = baseUrl + "/refund/list/byTime";

        url += "?startTime=" + startTime;
        url += "&endTime=" + endTime;
        url += "&page=" + pageInfo.getPageNo() + "&limit="
                + pageInfo.getPageSize() + "&sortBy=" + pageInfo.getSortBy()
                + "&order=" + pageInfo.getOrder().toString();

        JsonDocument result = restTemplate.postForObject(url, null,
                JsonDocument.class);

        List<Refund> refundList = (List<Refund>) result2List(result,
                Refund.class);

        return refundList;
    }

    public List<Order> getUnPaidOrderList(String userId, String createBy,
            boolean queryItem) throws PaymentSDKException// DESC(id)
    {
        String url = baseUrl + "/orders/unpaidList";
        String userIdsString = "userId=" + userId;
        String createBysString = "createBy=" + createBy;
        String queryItemBoolean = "queryItem=" + queryItem;

        url += "?" + userIdsString + "&" + createBysString + "&"
                + queryItemBoolean;

        JsonDocument result = restTemplate
                .getForObject(url, JsonDocument.class);

        List<Order> orders = (List<Order>) result2List(result, Order.class);

        return orders;
    }

    public int getUnPaidOrderNum(String userId, String createBy)
            throws PaymentSDKException
    {
        String url = baseUrl + "/orders/unpaidNum";
        String userIdsString = "userId=" + userId;
        String createBysString = "createBy=" + createBy;

        url += "?" + userIdsString + "&" + createBysString;

        JsonDocument result = restTemplate
                .getForObject(url, JsonDocument.class);

        Integer unpaidNum = (Integer) result2Object(result, Integer.class);

        return unpaidNum;
    }

    private Object result2Object(JsonDocument jsonResult, Class clazz)
            throws PaymentSDKException
    {
        Object result = null;
        if (jsonResult.getStateCode().equals(JsonDocument.STATE_SUCCESS))
        {

            try
            {
                String jsonString = JsonUtils.toJson(jsonResult.getData());
                result = JsonUtils.fromJson(jsonString, clazz);
            }
            catch (OVTException e)
            {
                final String errMsg = "Failed to convert json to object!";
                throw new PaymentSDKException(
                        PaymentSDKErrorCode.JSON_CONVERT_ERROR, errMsg, e);
            }
        }
        else
        {
            String errMsg = MessageFormat.format(
                    "ServiceException from {0}, Error_Code: {1}!",
                    jsonResult.getServiceCode(), jsonResult.getStateCode());
            throw new PaymentSDKException(jsonResult.getStateCode(), errMsg);
        }

        return result;
    }

    private Object result2List(JsonDocument jsonResult, Class clazz)
            throws PaymentSDKException
    {
        List result = new ArrayList();
        if (jsonResult.getStateCode().equals(JsonDocument.STATE_SUCCESS))
        {
            Object data = jsonResult.getData();
            if (data instanceof List)
            {
                List dataList = (List) data;

                for (Object entry : dataList)
                {
                    String jsonString;
                    try
                    {
                        jsonString = JsonUtils.toJson(entry);
                        Object object = JsonUtils.fromJson(jsonString, clazz);
                        result.add(object);
                    }
                    catch (OVTException e)
                    {
                        final String errMsg = "Failed to convert json to object!";
                        throw new PaymentSDKException(
                                PaymentSDKErrorCode.JSON_CONVERT_ERROR, errMsg,
                                e);
                    }
                }
            }
        }
        else
        {
            String errMsg = MessageFormat.format(
                    "ServiceException from {0}, Error_Code: {1}!",
                    jsonResult.getServiceCode(), jsonResult.getStateCode());
            throw new PaymentSDKException(jsonResult.getStateCode(), errMsg);
        }

        return result;
    }

    public JsonDocument queryAccountPage(String pageNo, String startTime,
            String endTime)
    {
        String url = baseUrl + "/account/query";
        String pageNoString = "pageNo=" + pageNo;
        String startTimeString = "startTime=" + startTime;
        String endTimeString = "endTime=" + endTime;

        url += "?" + pageNoString + "&" + startTimeString + "&" + endTimeString;

        JsonDocument result = restTemplate.postForObject(url, null,
                JsonDocument.class);

        return result;
    }

    public JsonDocument queryAccountPageByOrderNo(String pageNo, String orderNo)
    {
        String url = baseUrl + "/account/query/byOrder";
        String pageNoString = "pageNo=" + pageNo;
        String orderNoString = "orderNo=" + orderNo;

        url += "?" + pageNoString + "&" + orderNoString;

        JsonDocument result = restTemplate.postForObject(url, null,
                JsonDocument.class);

        return result;
    }

}
