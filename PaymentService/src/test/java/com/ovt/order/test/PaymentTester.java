/**
 * PaymentTester.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * 2015年12月30日
 */
package com.ovt.order.test;


import org.springframework.web.client.RestTemplate;

import com.ovt.common.model.JsonDocument;

/**
 * PaymentTester
 * 
 * @Author brad.zhou
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public class PaymentTester
{
    private RestTemplate restTemplate = new RestTemplate();

    private static final String BASE_URL = "http://localhost:18080/PaymentService/api";

    public void testAliNotify()
    {
        String url = BASE_URL + "/pay/alinotify";

        String params = "discount=0.00"
                + "&payment_type=1"
                + "&subject=测试"
                + "&trade_no=2013082244524842"
                + "&buyer_email=dlwdgl@gmail.com"
                + "&gmt_create=2013-08-22 14:45:23"
                + "&notify_type=trade_status_sync"
                + "&quantity=1"
                + "&out_trade_no=1451884092186"
                + "&seller_id=2088501624816263"
                + "&notify_time=2013-08-22 14:45:24"
                + "&body=测试测试"
                + "&trade_status=TRADE_SUCCESS"
                + "&is_total_fee_adjust=N"
                + "&total_fee=123.45"
                + "&gmt_payment=2013-08-22 14:45:24"
                + "&seller_email=xxx@alipay.com"
                + "&price=1.00"
                + "&buyer_id=2088602315385429"
                + "&notify_id=64ce1b6ab92d00ede0ee56ade98fdf2f4c"
                + "&use_coupon=N"
                + "&sign_type=RSA&sign=1glihU9DPWee+UJ82u3+mw3Bdnr9u01at0M/xJnPsGuHh+JA5bk3zbWaoWhU6GmLab3dIM4JNdktTcEUI9/FBGhgfLO39BKX/eBCFQ3bXAmIZn4l26fiwoO613BptT44GTEtnPiQ6+tnLsGlVSrFZaLB9FVhrGfipH2SWJcnwYs=";
        url += "?" + params;
        String result = restTemplate.postForObject(url, null, String.class);

        System.out.println(result);
    }

    public void testGetPaymentList()
    {
        String url = BASE_URL + "/pay/list";

        JsonDocument result = restTemplate
                .getForObject(url, JsonDocument.class);

        System.out.println(result);
    }

    public void testGetAliPaymentLogList()
    {
        String url = BASE_URL + "/pay/log/list";

        JsonDocument result = restTemplate
                .getForObject(url, JsonDocument.class);

        System.out.println(result);
    }

    public void testGetAliPayment()
    {
        String url = BASE_URL + "/pay/detail";
        String orderNo = "orderNo=1234567890";
        url += "?" + orderNo;

        JsonDocument result = restTemplate
                .getForObject(url, JsonDocument.class);

        System.out.println(result);
    }

    public void testGetAliPaymentLog()
    {
        String url = BASE_URL + "/pay/log/detail";
        String orderNo = "orderNo=1234567891";
        url += "?" + orderNo;

        JsonDocument result = restTemplate
                .getForObject(url, JsonDocument.class);

        System.out.println(result);
    }
}
