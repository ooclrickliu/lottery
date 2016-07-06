/**
 * PublishState.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * May 14, 2015
 */
package cn.wisdom.lottery.payment.dao.constant;

/**
 * PublishState
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public enum PublishState
{

    NOT_PUBLISH(0), PUBLISHED(1);

    private int value;

    private PublishState(int value)
    {
        this.value = value;
    }

    public static PublishState toPublishState(int value)
    {
        switch (value)
        {
        case 0:
            return NOT_PUBLISH;
        case 1:
            return PUBLISHED;

        default:
            return NOT_PUBLISH;
        }
    }

    public int getValue()
    {
        return value;
    }

}
