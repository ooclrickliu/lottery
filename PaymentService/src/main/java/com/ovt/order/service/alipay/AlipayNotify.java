package com.ovt.order.service.alipay;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class AlipayNotify
{
    /**
     * 支付宝消息验证地址
     */
    private static final String HTTPS_VERIFY_URL = "https://mapi.alipay.com/gateway.do?service=notify_verify&";

    /**
     * 验证消息是否是支付宝发出的合法消息
     * 
     * @param params 通知返回来的参数数组
     * @return 验证结果
     */
    public static boolean verify(Map<String, String> params)
    {
        String responseTxt = "false";
        if (params.get("notify_id") != null)
        {
            //String notify_id = params.get("notify_id");
            //responseTxt = verifyResponse(notify_id);    //获取远程服务器ATN结果,验证返回URL
            responseTxt = "true";
        }
        String sign = "";
        if (params.get("sign") != null)
        {
            sign = params.get("sign");
        }
        boolean isSign = getSignVeryfy(params, sign);   //根据反馈回来的信息，生成签名结果

        if (isSign && responseTxt.equals("true"))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * 根据反馈回来的信息，生成签名结果
     * 
     * @param Params 通知返回来的参数数组
     * @param sign 比对的签名结果
     * @return 生成的签名结果
     */
    private static boolean getSignVeryfy(Map<String, String> Params, String sign)
    {
        Map<String, String> sParaNew = AlipayCore.paraFilter(Params);
        String preSignStr = AlipayCore.createLinkString(sParaNew);
        boolean isSign = false;
        if (AlipayConfig.sign_type.equals("RSA"))
        {
            isSign = RSA.verify(preSignStr, sign, AlipayConfig.ali_public_key,
                    AlipayConfig.input_charset);
        }
        return isSign;
    }

    /**
     * 获取远程服务器ATN结果,验证返回URL
     * 
     * @param notify_id 通知校验ID
     * @return 服务器ATN结果 验证结果集： invalid 命令参数不对 出现这个错误，请检测返回处理中partner和key是否为空
     *         true 返回正确信息 false 请检查防火墙或者是服务器阻止端口问题以及验证时间是否超过一分钟
     */
    private static String verifyResponse(String notify_id)
    {
        String partner = AlipayConfig.partner;
        String veryfy_url = HTTPS_VERIFY_URL + "partner=" + partner
                + "&notify_id=" + notify_id;

        return checkUrl(veryfy_url);
    }

    /**
     * 获取远程服务器ATN结果
     * 
     * @param urlvalue 指定URL路径地址
     * @return 服务器ATN结果 验证结果集： invalid命令参数不对 出现这个错误，请检测返回处理中partner和key是否为空 true
     *         返回正确信息 false 请检查防火墙或者是服务器阻止端口问题以及验证时间是否超过一分钟
     */
    private static String checkUrl(String urlvalue)
    {
        String inputLine = "";

        try
        {
            URL url = new URL(urlvalue);
            HttpURLConnection urlConnection = (HttpURLConnection) url
                    .openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    urlConnection.getInputStream()));
            inputLine = in.readLine().toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            inputLine = "";
        }

        return inputLine;
    }

    @SuppressWarnings({ "rawtypes" })
    public static Map<String, String> processNotifyParams(Map requestParams)
            throws UnsupportedEncodingException
    {        
        Map<String, String> params = new HashMap<String, String>();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();)
        {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++)
            {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            
            //valueStr = URLDecoder.decode(valueStr, "utf-8");
            // 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        return params;
    }
}
