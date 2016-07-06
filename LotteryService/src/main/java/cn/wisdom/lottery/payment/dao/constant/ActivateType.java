/**
 * ActivateType.java
 * 
 * Copyright@2016 OVT Inc. All rights reserved. 
 * 
 * Feb 2, 2016
 */
package cn.wisdom.lottery.payment.dao.constant;

/**
 * ActivateType
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See 
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public enum ActivateType
{
    NOW, NEXT_MONTH;
    
    public static ActivateType getValue(String enumName)
    {
        if (enumName == null)
        {
            return null;
        }

        if (NOW.toString().equalsIgnoreCase(enumName))
        {
            return NOW;
        }
        else if (NEXT_MONTH.toString().equalsIgnoreCase(enumName))
        {
            return NEXT_MONTH;
        }
        else
        {
            return null;
        }
    }
}
