/**
 * TestMain.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * Dec 29, 2015
 */
package com.ovt.basic;

import java.security.NoSuchAlgorithmException;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.regex.Pattern;

import org.apache.http.client.utils.URLEncodedUtils;

import me.chanjar.weixin.common.util.crypto.SHA1;
import me.chanjar.weixin.common.util.http.URIUtil;
import cn.wisdom.lottery.common.utils.MathUtils;
import cn.wisdom.lottery.dao.mapper.UserMapper;


/**
 * TestMain
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public class TestMain
{
    public static void main(String[] args)
    {
//        float refundFee = 8.8f * ((float)5 / 30);
//        
//        System.out.println(refundFee);
        
//        System.out.println(MemberResourceStatus.valueOf("CANCELLED") == MemberResourceStatus.CANCELLED);
        
//        float f = 1.6666667f;
//        String a = formatNumber(f, 2);
//        System.out.println(Float.valueOf(a));
        
//        OrderRequest order = new OrderRequest();
//        order.setDeviceId("1");
//        order.setFlow("10");
//        order.setSpace("20");
//        order.setMonth("2");
        
//        try
//        {
////            final String json = JsonUtils.toJson(order);
//
//            String json = "{\"deviceId\":\"1234567890\",\"space\":\"20\",\"flow\":\"10\",\"month\":\"2\",\"totalFee\":\"0.0\"}";
//            order = JsonUtils.fromJson(json, OrderRequest.class);
//            
//            System.out.println(order);
//        }
//        catch (OVTException e)
//        {
//            e.printStackTrace();
//        }
        
//        Order order = new Order();
//        
//        try
//        {
//            String json = JsonUtils.toJson(order);
//            
//            System.out.println(json);
//        }
//        catch (OVTException e)
//        {
//            e.printStackTrace();
//        }
        
//        String string = "SELECT distinct device_id FROM member_resource WHERE mr_status in (''ACTIVE'', ''APPLY_CANCEL'')order by {0}";
//        System.out.println(MessageFormat.format(string, "device"));
        
//        String remark = "SPACE:1048576,2097152;validTime:2016-01-21 09:12:26,2016-11-16 09:12:26;FLOW:14400,28800;validTime:2016-01-21 09:12:26,2016-11-16 09:12:26;";
//        convertActionRemarkToOrderInfo(remark, new Order());
        
//        final Date fromDate = DateTimeUtils.parseDate("2016-02-01 00:00:01", DateTimeUtils.PATTERN_SQL_DATETIME_FULL);
//        final Date toDate = DateTimeUtils.parseDate("2016-04-31 23:59:59", DateTimeUtils.PATTERN_SQL_DATE);
////        System.out.println(DateTimeUtils.addDays(new Date(), 60));
//        //System.out.println(DateTimeUtils.monthDiff(fromDate, toDate));
//        NotifyInfo info = new NotifyInfo();
//        info.setDeviceId("1234");
//        info.setExpireTime(DateTimeUtils.formatSqlDateTime(DateTimeUtils.getCurrentTimestamp()));
//        info.setFlow(1234);
//        info.setSpace(4321);
//        try
//        {
//            System.out.println(JsonUtils.toJson(info));
//        }
//        catch (OVTException e)
//        {
//            e.printStackTrace();
//        }
        
//        Date expireTime = DateTimeUtils.addMonths(DateTimeUtils.getMonthEnd(DateTimeUtils.getCurrentTimestamp()), 3);
//        
//        String fromDate =
//                DateTimeUtils
//                        .formatSqlDateTime(DateTimeUtils.getNextMonthStart(DateTimeUtils.getCurrentTimestamp()));
//        String toDate = DateTimeUtils.formatSqlDateTime(expireTime);
//        
//        System.out.println(fromDate + " - " + toDate);
        
//        List<String> processedOrderStateList = Arrays.asList(
//                "REFUND_DOING",
//                "REFUND_REFUSED",
//                "REFUND_DONE",
//                "REFUND_FAILED");
//        
//        String format = "abc in ({0}) efg {1}";
//        final String str = MessageFormat.format(format, StringUtils.getCSV(processedOrderStateList, true), 1);
//        System.out.println(str);
        
//        Date current = DateTimeUtils.getCurrentTimestamp();
//        System.out.println(current.getDate());
//        float amount = 2;
//        amount -= 0.5;
//        System.out.println(amount);
        
//        Map<String, String[]> map = new HashMap<String, String[]>();
//        
//        String[] a = {"AA", "AAA"};
//        map.put("a", a);
//        
//        try
//        {
//            String mapStr = JsonUtils.toJson(map);
//            System.out.println(mapStr);
//        }
//        catch (OVTException e)
//        {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
        
//        int remainMonth =
//                DateTimeUtils.monthDiff(DateTimeUtils.parseDate("2016-02-22 10:12:56", DateTimeUtils.PATTERN_SQL_DATETIME_FULL),
//                        DateTimeUtils.parseDate("2016-02-29 10:12:56", DateTimeUtils.PATTERN_SQL_DATETIME_FULL));
//        
//        System.out.println(remainMonth);
        
//        String aString = "123131231:34;";
//        String[] a = aString.split(",|;");
//        
//        System.out.println(a.length);
        
//        System.out.println(new Timestamp(1473699461));
    	
//    	System.out.println("123".equals("" + 123));
    	
//    	UserMapper userMapper = new UserMapper();
//    	
//    	System.out.println("Done");
//    	System.out.println(System.currentTimeMillis());
    	
    	
//    	try {
//			String sign = SHA1.gen("wisdom", "1468634745", "1338596064");
//			
//			System.out.println(sign);
//			
//			
//			System.out.println("600a22c344239118388d8e7683c072174085f910");
//		} catch (NoSuchAlgorithmException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
    	
//    	String input = "http://www.southwisdom.cn/#/select";
//		System.out.println(URIUtil.encodeURIComponent(input));

//    	int times = 5;
//    	for (int i = 0; i < times; i++) {
//    		System.out.println("remainRate       remainCount         rate");
//    		System.out.println("------------------------------------------");
//    		int remainCount = 10;
//    		int remainRate = 100;
//    		
//    		while(remainCount > 0)
//    		{
//    			int rate = randomRate(remainCount, remainRate);
//    			System.out.println(remainCount + "      " + remainRate + "       " + rate);
//    			
//    			remainCount--;
//    			remainRate -= rate;
//    		}
//		}
//    	
//    	Pattern HELP_CODE_PATTERN = Pattern.compile("[\\d]{1,2}$");
//    	System.out.println(HELP_CODE_PATTERN.matcher("2a").matches());
    	
    	int rate = 3;
    	int total = 5;
    	float bonus = (float) (total * rate) / 100;
    	System.out.println(bonus);
    }
    
    public static int randomRate(int remainCount, int remainRate) {
		int rate = 0;
		if (remainCount > 1) {
			int min = 1;
			int max = remainRate / remainCount * 2;
			rate = MathUtils.rand(max);
			rate = rate <= min ? min : rate;
		}
		else if (remainCount == 1) {
			rate = remainRate;
		}
		return rate;
	}
    
    public static String formatNumber(double num,int maxFractionDigits) {
        NumberFormat nf = NumberFormat.getInstance(Locale.US);
        nf.setMaximumFractionDigits(maxFractionDigits);
        nf.setGroupingUsed(false);
        return nf.format(num);
   }
    
}
