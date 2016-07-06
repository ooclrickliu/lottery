/**
 * OrderTester.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * 2015年12月28日
 */
package com.ovt.order.test;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.client.RestTemplate;

import com.ovt.common.exception.OVTException;
import com.ovt.common.model.JsonDocument;
import com.ovt.common.utils.JsonUtils;
import com.ovt.order.controller.response.PaymentServiceAPIResult;
import com.ovt.order.dao.vo.Order;
import com.ovt.order.dao.vo.OrderItem;
import com.ovt.order.dao.vo.RefundRequest;

import junit.framework.TestCase;

/**
 * OrderTester
 * 
 * @Author brad.zhou
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
@SuppressWarnings("rawtypes")
public class OrderTester extends TestCase
{
    private RestTemplate restTemplate = null;

    private static final String BASE_URL = "http://localhost:38080/PaymentService/api";

    public OrderTester()
    {
        restTemplate = new RestTemplate();

        // List messageConverters = new ArrayList();
        // messageConverters.add(new
        // StringHttpMessageConverter(StandardCharsets.UTF_8));
        // restTemplate.setMessageConverters(messageConverters);

        // List<HttpMessageConverter<?>> messageConverters = restTemplate
        // .getMessageConverters();
        // if (messageConverters == null)
        // {
        // messageConverters = new ArrayList<HttpMessageConverter<?>>();
        // }
        // else
        // {
        // System.out.println(messageConverters.size());
        //
        // for (HttpMessageConverter<?> httpMessageConverter :
        // messageConverters)
        // {
        // System.out.println(httpMessageConverter.getClass());
        // }
        // HttpMessageConverter<?> converterTarget = null;
        //
        // for (HttpMessageConverter<?> item : messageConverters)
        // {
        // if (item.getClass() == StringHttpMessageConverter.class)
        // {
        // converterTarget = item;
        // break;
        // }
        // }
        //
        // if (converterTarget != null)
        // {
        // messageConverters.remove(converterTarget);
        // }
        // }
        //
        // for (HttpMessageConverter<?> httpMessageConverter :
        // messageConverters)
        // {
        // System.out.println(httpMessageConverter.getClass());
        // }
        //
        // HttpMessageConverter<?> converter = new StringHttpMessageConverter(
        // StandardCharsets.UTF_8);
        //
        // messageConverters.add(converter);
        //
        // for (HttpMessageConverter<?> httpMessageConverter :
        // messageConverters)
        // {
        // System.out.println(httpMessageConverter.getClass());
        // }
        // restTemplate.setMessageConverters(messageConverters);

    }

    public void testCreateOrderTask() throws OVTException,
            UnsupportedEncodingException
    {
        String url = BASE_URL + "/orders/create";

        Order order = new Order();
        // String userId = "userId=1";
        // String totalFee = "totalFee=123.45";
        // String remark = "remark='请给我发白色版本，OK？'";

        String userId = "1";
        float totalFee = 123.45F;
        String remark = "请给我发白色版本";
        order.setUserId(userId);
        order.setOrderTotalFee(totalFee);
        order.setOrderRemark(remark);
        order.setCreateBy("1");

        // url += "?" + userId + "&" + totalFee + "&" + remark;
        // System.out.println(url);
        // Map<String, Object> data = new HashMap<String, Object>();
        // data.put("userId", 1);
        // data.put("totalFee", 123.45);
        // data.put("remark", "请给我发白色的版本");

        List<OrderItem> orderItems = new ArrayList<OrderItem>();
        orderItems.add(new OrderItem("1512283600100058", "Beer", 10, 2));
        orderItems.add(new OrderItem("1512282452100136", "Chicken", 23, 3));
        order.setOrderItemList(orderItems);

        // data.put("productList", orderItems);

        // String requestJson = JsonUtils.toJson(data);
        // // requestJson = "user=" + requestJson;
        // System.out.println(requestJson);

        // HttpHeaders headers = new HttpHeaders();
        // headers.setContentType(MediaType.APPLICATION_JSON);
        //
        // HttpEntity<String> entity = new HttpEntity<String>(requestJson,
        // headers);
        //
        // List messageConverters = new ArrayList();
        // messageConverters.add(new MappingJackson2HttpMessageConverter());
        // messageConverters.add(new FormHttpMessageConverter());
        // messageConverters.add(new StringHttpMessageConverter());
        // restTemplate.setMessageConverters(messageConverters);
        System.out.println(new PaymentServiceAPIResult(order));

        JsonDocument result = restTemplate.postForObject(url, order,
                JsonDocument.class);

        // System.out.println(result.getData().getClass()); class
        // java.util.LinkedHashMap

        System.out.println(result);

    }

    public void testQueryOrderList()
    {
        String url = BASE_URL + "/orders/list";
        String userId = "userId=1";
        url += "?" + userId;
        // Map<String, Object> data = new HashMap<String, Object>();
        // data.put("userId", 1);
        JsonDocument result = restTemplate
                .getForObject(url, JsonDocument.class);

        Object data = result.getData();

        List<Order> orders = new ArrayList<Order>();
        if (data instanceof List)
        {
            List dataList = (List) data;

            for (Object entry : dataList)
            {
                String jsonString;
                try
                {
                    jsonString = JsonUtils.toJson(entry);
                    Order order = JsonUtils.fromJson(jsonString, Order.class);

                    orders.add(order);
                }
                catch (OVTException e)
                {
                    e.printStackTrace();
                }
            }
        }
        System.out.println(orders.size());
        System.out.println(orders.get(0).getOrderNo());
        System.out.println(data);
    }

    public void testQueryOrderInfo()
    {
        String url = BASE_URL + "/orders/detail";
        String userId = "userId=1";
        String orderNo = "orderNo=1460612015191";

        url += "?" + userId + "&" + orderNo;

        JsonDocument result = restTemplate
                .getForObject(url, JsonDocument.class);

        // System.out.println(result.getData().getClass());
        System.out.println(result.getData().getClass());

        Object resultMap = result.getData();

        try
        {
            String jsonString = JsonUtils.toJson(resultMap);
            Order order = JsonUtils.fromJson(jsonString, Order.class);
            System.out.println(order.getOrderNo());
            System.out.println(order.getOrderItemList().size());
        }
        catch (OVTException e)
        {
            e.printStackTrace();
        }

        // for (Object key : resultMap.keySet())
        // {
        // System.out.println("key=" + key + "value=" + resultMap.get(key));
        // }
        //
        System.out.println(result);
    }

    public void testCancelOrder()
    {
        String url = BASE_URL + "/orders/cancel";
        String userId = "userId=1";
        String orderNo = "orderNo=1460609314156";

        url += "?" + userId + "&" + orderNo;

        JsonDocument result = restTemplate.postForObject(url, null,
                JsonDocument.class);
        System.out.println(result.getData().getClass()); // class
                                                         // java.lang.Boolean
    }

    public void testDeleteOrder()
    {
        String url = BASE_URL + "/orders/delete";
        String userId = "userId=1";
        String orderNo = "orderNo=1234567890";

        url += "?" + userId + "&" + orderNo;

        JsonDocument result = restTemplate.postForObject(url, null,
                JsonDocument.class);
        System.out.println(result);
    }

    public void testApplyForRefund()
    {
        String url = BASE_URL + "/orders/applyRefund";
        String userId = "userId=1";
        url += "?" + userId;
        String orderNo = "1451884092185";
        String refundReason = "次品";
        float refundFee = 12F;
        String refundDesc = "东西是次品，我不想要了。";

        RefundRequest refundRequest = new RefundRequest();
        refundRequest.setOrderNo(orderNo);
        refundRequest.setRefundReason(refundReason);
        refundRequest.setRefundFee(refundFee);
        refundRequest.setRefundDesc(refundDesc);

        JsonDocument result = restTemplate.postForObject(url, refundRequest,
                JsonDocument.class);
        System.out.println(result);
    }

    public void testQueryRefundRequestList()
    {
        String url = BASE_URL + "/orders/refundList";

        JsonDocument result = restTemplate
                .getForObject(url, JsonDocument.class);
        System.out.println(result);
    }

    public void testDoAgreeRefundAction()
    {
        // 需要和Leo的refundService一起测试
        String url = BASE_URL + "/orders/doAgreeRefund";
        String auditorId = "auditorId=1";
        url += "?" + auditorId;
        List<Long> requestIdList = new ArrayList<Long>();
        requestIdList.add(1L);
        requestIdList.add(2L);
        requestIdList.add(3L);
        requestIdList.add(4L);

        JsonDocument result = restTemplate.postForObject(url, requestIdList,
                JsonDocument.class);
        System.out.println(result);

    }

    public void testDoRejectRefundAction()
    {
        String url = BASE_URL + "/orders/doRejectRefund";
        String auditorId = "auditorId=1";
        url += "?" + auditorId;

        List<RefundRequest> refundRequests = new ArrayList<RefundRequest>();
        refundRequests.add(new RefundRequest(1, "不允许退款！"));
        refundRequests.add(new RefundRequest(2, "达不到退款条件！"));

        JsonDocument result = restTemplate.postForObject(url, refundRequests,
                JsonDocument.class);
        System.out.println(result);

    }

    public void testQueryNonProcessRefundRequestList()
    {
        String url = BASE_URL + "/orders/nonProcessRefundList";

        JsonDocument result = restTemplate
                .getForObject(url, JsonDocument.class);
        System.out.println(result);

    }

    public void testQueryProcessedRefundRequestList()
    {
        String url = BASE_URL + "/orders/processedRefundList";

        JsonDocument result = restTemplate
                .getForObject(url, JsonDocument.class);
        System.out.println(result);

    }

    public void testQueryRefundRequest()
    {
        String url = BASE_URL + "/orders/refundDetail";
        String orderNo = "orderNo=1451884092185";
        url += "?" + orderNo;

        JsonDocument result = restTemplate
                .getForObject(url, JsonDocument.class);

        System.out.println(result);

    }

    public void testApplyForRefundList()
    {
        String url = BASE_URL + "/orders/applyRefundList";
        String userId = "userId=1";

        List<RefundRequest> refundRequests = new ArrayList<RefundRequest>();
        refundRequests.add(new RefundRequest("1234567890", "1", "贵", 123,
                "东西太贵了"));
        refundRequests.add(new RefundRequest("1451884092185", "1", "丑",
                123.45F, "东西太丑了"));
        refundRequests.add(new RefundRequest("123456789012", "1", "贵", 456,
                "东西太贵了"));
        refundRequests.add(new RefundRequest("123456789034", "1", "丑", 644,
                "东西太丑了"));

        url += "?" + userId;

        JsonDocument result = restTemplate.postForObject(url, refundRequests,
                JsonDocument.class);
        System.out.println(result);
    }

    public void testRevokeRefund()
    {
        String url = BASE_URL + "/orders/revokeRefund";

        List<String> orderNos = new ArrayList<String>();
        orderNos.add("1234567890");
        orderNos.add("1451884092185");
        orderNos.add("123456789012");

        JsonDocument result = restTemplate.postForObject(url, orderNos,
                JsonDocument.class);
        System.out.println(result);
    }

    public void testSpecialRevokeRefund()
    {
        String url = BASE_URL + "/orders/specialRevokeRefund";

        String userId = "userId=1";

        url += "?" + userId;

        JsonDocument result = restTemplate.postForObject(url, null,
                JsonDocument.class);
        
        System.out.println(result);
    }

    public void testIsRefunding()
    {
        String url = BASE_URL + "/orders/isRefunding";
        String orderNo = "orderNo=1234567890";
        url += "?" + orderNo;

        JsonDocument result = restTemplate.postForObject(url, null,
                JsonDocument.class);
        System.out.println(result);
    }

    public void testUnPaidOrderList()
    {
        String url = BASE_URL + "/orders/unpaidList";
        String userId = "userId=1";
        String createBy = "createBy=1";
        String queryItem = "queryItem=false";

        url += "?" + userId + "&" + createBy + "&" + queryItem;

        JsonDocument result = restTemplate
                .getForObject(url, JsonDocument.class);
        System.out.println(result);
    }

    public void testApplyingRefundRequestList()
    {
        String url = BASE_URL + "/orders/applyingRefunds";
        String orderNo = "orderNo=1234567890";

        url += "?" + orderNo;

        JsonDocument result = restTemplate
                .getForObject(url, JsonDocument.class);
        System.out.println(result);
    }

    public void testQueryRefundRequestListByIds()
    {
        String url = BASE_URL + "/orders/refundListByIds";

        List<Long> refundRequestIds = new ArrayList<Long>();
        refundRequestIds.add(1L);
        refundRequestIds.add(2L);
        refundRequestIds.add(3L);
        refundRequestIds.add(4L);
        refundRequestIds.add(5L);

        JsonDocument result = restTemplate.postForObject(url, refundRequestIds,
                JsonDocument.class);
        System.out.println(result);
    }

    public void testQueryRefundableOrderList()
    {
        String url = BASE_URL + "/orders/refundableOrders";
        String userId = "userId=1";
        url += "?" + userId;

        JsonDocument result = restTemplate
                .getForObject(url, JsonDocument.class);

        Object data = result.getData();

        List<Order> orders = new ArrayList<Order>();
        if (data instanceof List)
        {
            List dataList = (List) data;

            for (Object entry : dataList)
            {
                String jsonString;
                try
                {
                    jsonString = JsonUtils.toJson(entry);
                    Order order = JsonUtils.fromJson(jsonString, Order.class);

                    orders.add(order);
                }
                catch (OVTException e)
                {
                    e.printStackTrace();
                }
            }
        }
        System.out.println(orders.size());
        System.out.println(orders.get(0).getOrderNo());
        System.out.println(data);
    }

    public static void main(String[] args) throws OVTException,
            UnsupportedEncodingException
    {
//         new OrderTester().testCreateOrderTask();
         new OrderTester().testQueryOrderList();
        // new OrderTester().testQueryOrderInfo();
        // new OrderTester().testCancelOrder();
        // new OrderTester().testDeleteOrder();
        // new OrderTester().testApplyForRefund();
        // new OrderTester().testQueryRefundRequestList();
        // new OrderTester().testDoRejectRefundAction();
        // new OrderTester().testDoAgreeRefundAction();
        // new OrderTester().testQueryNonProcessRefundRequestList();
        // new OrderTester().testQueryProcessedRefundRequestList();
        // new OrderTester().testQueryRefundRequest();
        // new OrderTester().testApplyForRefundList();
        // new OrderTester().testRevokeRefund();
        // new OrderTester().testIsRefunding();
        // new OrderTester().testUnPaidOrderList();
        // new OrderTester().testApplyingRefundRequestList();
        // new OrderTester().testQueryRefundRequestListByIds();
//        new OrderTester().testQueryRefundableOrderList();
//        new OrderTester().testSpecialRevokeRefund();
    }
}
