/**
 * RefundTester.java
 * 
 * Copyright@2016 OVT Inc. All rights reserved. 
 * 
 * 2016年1月5日
 */
package com.ovt.order.test;

import org.springframework.web.client.RestTemplate;

import com.ovt.common.model.JsonDocument;

/**
 * RefundTester
 * 
 * @Author brad.zhou
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public class RefundTester
{
    private RestTemplate restTemplate = new RestTemplate();

    private static final String BASE_URL = "http://localhost:18080/PaymentService/api";

    public void testAliNotify()
    {
        String url = BASE_URL + "/refund/alinotify";

        String params = "notify_time=2009-08-12+11%3A08%3A32"
                + "&notify_type=batch_refund_notify"
                + "&notify_id=70fec0c2730b27528665af4517c27b95"
                + "&sign_type=MD5&sign=_p_w_l_h_j0b_gd_aejia7n_ko4_m%252Fu_w_jd3_nx_s_k_mxus9_hoxg_y_r_lunli_pmma29_t_q%3D%3D"
                + "&batch_no=201601041000" + "&success_num=2"
                + "&result_details=2010031906272929%5E80%5ESUCCESS";
        url += "?" + params;
        String result = restTemplate.postForObject(url, null, String.class);

        System.out.println(result);

    }

    public void testGetRefundList()
    {
        String url = BASE_URL + "/refund/list";

        JsonDocument result = restTemplate
                .getForObject(url, JsonDocument.class);

        System.out.println(result);
    }

    public void testGetAliRefundLogList()
    {
        String url = BASE_URL + "/refund/log/list";

        JsonDocument result = restTemplate
                .getForObject(url, JsonDocument.class);

        System.out.println(result);
    }
    
    public void testGetRefund()
    {
        String url = BASE_URL + "/refund/detail";
        String orderNo = "orderNo=1451884092185";
        url += "?" + orderNo;
        
        JsonDocument result = restTemplate
                .getForObject(url, JsonDocument.class);

        System.out.println(result);
    }

    public void testGetAliRefundLog()
    {
        String url = BASE_URL + "/refund/log/detail";
        String longId = "logId=1";
        url += "?" + longId;
        
        JsonDocument result = restTemplate
                .getForObject(url, JsonDocument.class);

        System.out.println(result);
    }
}
