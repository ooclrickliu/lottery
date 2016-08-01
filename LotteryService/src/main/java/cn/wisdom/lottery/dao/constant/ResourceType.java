/**
 * ResourceType.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * Dec 29, 2015
 */
package cn.wisdom.lottery.dao.constant;

/**
 * ResourceType
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public enum ResourceType
{

    SPACE, FLOW;

    public static ResourceType getResourceType(String type)
    {
        if (type == null)
        {
            return null;
        }

        if (SPACE.toString().equalsIgnoreCase(type))
        {
            return SPACE;
        }
        else if (FLOW.toString().equalsIgnoreCase(type))
        {
            return FLOW;
        }
        else
        {
            return null;
        }

    }

}
