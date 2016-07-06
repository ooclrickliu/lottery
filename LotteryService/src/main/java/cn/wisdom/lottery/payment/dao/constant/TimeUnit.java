/**
 * TimeUnit.java
 * 
 * Copyright@2016 OVT Inc. All rights reserved. 
 * 
 * 2016年1月6日
 */
package cn.wisdom.lottery.payment.dao.constant;

/**
 * TimeUnit
 * 
 * @Author jinzhong.zheng
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public enum TimeUnit
{
    DAY, MONTH, QUARTER, YEAR;

    public static TimeUnit getTimeUnit(String unit)
    {
        if (unit == null)
        {
            return null;
        }
        
        if(DAY.toString().equalsIgnoreCase(unit))
        {
            return DAY;
        }
        else if(MONTH.toString().equalsIgnoreCase(unit))
        {
            return MONTH;
        }
        else if (QUARTER.toString().equalsIgnoreCase(unit)) {
            return QUARTER;
        }
        else if (YEAR.toString().equalsIgnoreCase(unit)) {
            return YEAR;
        }
        else
        {
            return null;
        }
    }
}
