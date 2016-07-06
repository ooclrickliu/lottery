/**
 * ActiveState.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * May 12, 2015
 */
package cn.wisdom.lottery.payment.dao.constant;

/**
 * ActiveState
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public enum ActiveState
{

    ACTIVE(1), INACTIVE(-1);

    private int value;

    private ActiveState(int value)
    {
        this.value = value;
    }

    public static ActiveState toActiveState(int value)
    {
        switch (value)
        {
        case 1:
            return ACTIVE;
        case -1:
            return INACTIVE;

        default:
            return ACTIVE;
        }
    }

    public int getValue()
    {
        return value;
    }

}
