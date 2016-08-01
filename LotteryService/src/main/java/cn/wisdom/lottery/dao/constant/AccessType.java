/**
 * AccessType.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * May 14, 2015
 */
package cn.wisdom.lottery.dao.constant;

/**
 * AccessType
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public enum AccessType
{

    NATIVE(1), HTTP(2);

    private int value;

    private AccessType(int value)
    {
        this.value = value;
    }

    public static AccessType toAccessType(int value)
    {
        switch (value)
        {
        case 0:
            return NATIVE;
        case 2:
            return HTTP;

        default:
            return NATIVE;
        }
    }

    public int getValue()
    {
        return value;
    }

}
