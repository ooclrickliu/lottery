/**
 * NumberGeneratorUtil.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * 2015��12��22��
 */
package com.ovt.common.utils;

import java.util.Date;
import java.util.Random;

/**
 * NumberGeneratorUtil
 * 
 * @Author brad.zhou
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public class NumberGeneratorUtil
{
    private static long refund_batch_num = 1000;

    public static String generateOrderNumber(String userId)
    {
        StringBuffer orderNo = new StringBuffer();
        orderNo.append(((Long) System.currentTimeMillis()).toString()
                .substring(0, 10));
        orderNo.append(userId);
        Random random = new Random();
        for (int i = 0; i < 2; i++)
        {
            orderNo.append(random.nextInt(10));
        }
        return orderNo.toString();
    }

    public static String generateRefundBatchNo()
    {
        StringBuffer batchNo = new StringBuffer();
        String currentDate = DataConvertUtils.toString(new Date());

        batchNo.append(currentDate.replace("-", ""));
        batchNo.append(++refund_batch_num);
        Random random = new Random();
        for (int i = 0; i < 2; i++)
        {
            batchNo.append(random.nextInt(10));
        }

        return batchNo.toString();
    }
}
