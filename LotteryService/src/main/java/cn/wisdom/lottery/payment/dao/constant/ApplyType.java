/**
 * ApplyType.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * May 14, 2015
 */
package cn.wisdom.lottery.payment.dao.constant;

/**
 * ApplyType
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See 
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public enum ApplyType
{

    TRIAL(0), PAID(1);

    private int value;

    private ApplyType(int value)
    {
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }
    
    public static ApplyType toApplyType(int value)
    {
        switch (value)
        {
        case 0:
            return TRIAL;
        case 1:
            return PAID;

        default:
            return TRIAL;
        }
    }
}
